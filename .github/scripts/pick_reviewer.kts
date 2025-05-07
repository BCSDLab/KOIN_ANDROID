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

enum class Developer(val githubName: String, val team: Set<KoinTeam>, val isMentor: Boolean = false) {
    YUNJAENA("yunjaena", setOf(), true),
    SKDUD0629("skdud0629", setOf(KoinTeam.BUSINESS)),
    JAEYOUNG290("JaeYoung290", setOf(KoinTeam.BUSINESS)),
    KONGWOOJIN("kongwoojin", setOf(KoinTeam.CAMPUS, KoinTeam.USER)),
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
    Developer.SKDUD0629 to Developer.KYM_P,
    Developer.JAEYOUNG290 to Developer.KONGWOOJIN,
    Developer.KONGWOOJIN to Developer.JAEYOUNG290,
    Developer.KYM_P to Developer.JUSANG3057,
    Developer.JUSANG3057 to Developer.SKDUD0629,
)

/**
 * Regex to extract the team from the pull request title.
 */
val titleRegex = Regex("(?<=\\[)(user|campus|business)(?=\\])")

/**
 * Export the reviewer name to GitHub Actions output.
 * @param name The name of the reviewer.
 */
fun exportReviewer(name: String) {
    val githubOutput = System.getenv("GITHUB_OUTPUT")
    File(githubOutput).appendText("reviewer=$name\n")
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
    exportReviewer(reviewer.githubName)
}

/**
 * Pick a random reviewer from the other team members.
 * The developer and reviewer should not be in the same team.
 */
fun pickRandomReviewer(prOwnerTeam: KoinTeam, developer: Developer) {
    val otherTeamDevelopers = Developer.entries
        .filter { it != developer }
        .filter { !it.isMentor }
        .filter { !it.team.contains(prOwnerTeam) }
    val randomReviewer = otherTeamDevelopers.random()
    exportReviewer(randomReviewer.githubName)
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

    if (team == null) {
        return
    }

    pickRandomReviewer(team, developer)
    pickMentor()
}

main(args)
