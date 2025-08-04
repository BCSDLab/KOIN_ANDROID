#!/usr/bin/env kotlin

/*
 * Copyright (c) 2025. BCSD Lab.
 */

import java.io.File
import kotlin.random.Random

enum class KoinTeam {
    BUSINESS,
    CAMPUS,
    USER
}

/**
 * If developer is a mentor, set isMentor to true.
 * If developer should not be picked as a reviewer, set shouldPick to false.
 */
enum class Developer(val githubName: String, val team: Set<KoinTeam>, val isMentor: Boolean = false, val shouldPick: Boolean = true) {
    YUNJAENA("yunjaena", setOf(), true),
    SKDUD0629("skdud0629", setOf(), shouldPick = false),
    JAEYOUNG290("JaeYoung290", setOf(KoinTeam.BUSINESS)),
    KONGWOOJIN("kongwoojin", setOf(KoinTeam.BUSINESS, KoinTeam.CAMPUS, KoinTeam.USER)),
    KYM_P("KYM-P", setOf(KoinTeam.CAMPUS)),
    JUSANG3057("jusang3057", setOf(KoinTeam.USER))
}

/**
 * Reviewer pairs for each developer.
 * first element is developer and second element is reviewer.
 *
 * Pair rule
 * developer and reviewer should not be in the same team
 * don't add mentor here
 */
val reviewerPair = listOf(
    Developer.JAEYOUNG290 to Developer.KONGWOOJIN,
    Developer.KONGWOOJIN to Developer.JAEYOUNG290,
    Developer.KYM_P to Developer.JUSANG3057,
    Developer.JUSANG3057 to Developer.KYM_P,
)

/**
 * Regex to extract the team from the pull request title.
 */
val titleRegex = Regex("""(?<=\[)(user|campus|business)(?=\])""")

/**
 * Export the reviewer name to GitHub Actions output.
 * @param firstReviewer The name of the reviewer.
 * @param secondReviewer The name of the second reviewer (optional).
 */
fun exportReviewer(firstReviewer: String, secondReviewer: String = "") {
    val githubOutput = System.getenv("GITHUB_OUTPUT")
    File(githubOutput).appendText("reviewer1=$firstReviewer\n, reviewer2=$secondReviewer\n")
}

/**
 * Export the mentor name to GitHub Actions output.
 * @param name The name of the mentor.
 */
fun exportMentor(name: String) {
    val githubOutput = System.getenv("GITHUB_OUTPUT")
    File(githubOutput).appendText("mentor=$name\n")
}

/**
 * Pick a paired reviewer for the developer.
 * The developer and reviewer should not be in the same team.
 */
fun pickPairedReviewer(developer: Developer) {
    val reviewer = reviewerPair.first { it.first == developer }.second
    exportReviewer(reviewer.githubName, "")
}

/**
 * Pick a random reviewer.
 * The developer and reviewer should not be in the same team.
 */
fun pickRandomReviewer(prOwnerTeam: KoinTeam?, developer: Developer) {
    val otherTeamDevelopers = Developer.entries
        .filter { it != developer }
        .filter { it.shouldPick }
        .filter { !it.isMentor }
        .filter { if (prOwnerTeam != null) !it.team.contains(prOwnerTeam) else true }
    val randomReviewerFromOtherTeam = otherTeamDevelopers.random()

    val sameTeamDevelopers = Developer.entries
        .filter { it != developer }
        .filter { it.shouldPick }
        .filter { !it.isMentor }
        .filter { it != randomReviewerFromOtherTeam }
        .filter { if (prOwnerTeam != null) it.team.contains(prOwnerTeam) else true }
    val randomReviewerFromSameTeam = sameTeamDevelopers.randomOrNull()
    exportReviewer(randomReviewerFromOtherTeam.githubName, randomReviewerFromSameTeam?.githubName ?: "")
}

/**
 * Pick a mentor.
 */
fun pickMentor() {
    val shouldAddMentor = Random.nextInt() % 10 == 0 // 10%
    if (shouldAddMentor) {
        val mentor = Developer.entries.filter { it.isMentor }.random()
        exportMentor(mentor.githubName)
    }
}

fun main(args: Array<String>) {
    val githubActor = System.getenv("GITHUB_ACTOR")
    val developer = Developer.entries.firstOrNull { it.githubName == githubActor }

    if (developer == null) {
        return
    }

    val team = titleRegex.find(args[0].lowercase())?.value.let {
        when (it) {
            "user" -> KoinTeam.USER
            "campus" -> KoinTeam.CAMPUS
            "business" -> KoinTeam.BUSINESS
            else -> null
        }
    }

    pickRandomReviewer(team, developer)
    pickMentor()
}

main(args)
