#!/usr/bin/env kotlin

/*
 * Copyright (c) 2025. BCSD Lab.
 */

import java.io.File
import kotlin.random.Random

/**
 * If developer is a mentor, set isMentor to true.
 * If developer should not be picked as a reviewer, set shouldPick to false.
 */
enum class Developer(val githubName: String, val isMentor: Boolean = false, val shouldPick: Boolean = true) {
    YUNJAENA("yunjaena", true),
    SKDUD0629("skdud0629", shouldPick = false),
    JAEYOUNG290("JaeYoung290"),
    KONGWOOJIN("kongwoojin"),
    KYM_P("KYM-P"),
    JUSANG3057("jusang3057")
}

/**
 * Reviewer pairs for each developer.
 * first element is developer and second element is reviewer.
 *
 * Pair rule
 * don't add mentor here
 */
val reviewerPair = listOf(
    Developer.JAEYOUNG290 to Developer.KONGWOOJIN,
    Developer.KONGWOOJIN to Developer.JAEYOUNG290,
    Developer.KYM_P to Developer.JUSANG3057,
    Developer.JUSANG3057 to Developer.KYM_P,
)

/**
 * Export the reviewer name to GitHub Actions output.
 * @param firstReviewer The name of the reviewer.
 * @param secondReviewer The name of the second reviewer (optional).
 */
fun exportReviewer(firstReviewer: String, secondReviewer: String = "") {
    val githubOutput = System.getenv("GITHUB_OUTPUT")
    File(githubOutput).appendText("reviewer1=$firstReviewer\n")
    File(githubOutput).appendText("reviewer2=$secondReviewer\n")
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
 */
fun pickRandomReviewer(developer: Developer?) {
    val otherDevelopers = Developer.entries
        .filter { it != developer }
        .filter { it.shouldPick }
        .filter { !it.isMentor }
    val randomDevelopers = otherDevelopers.shuffled().take(2)
    exportReviewer(randomDevelopers[0].githubName, randomDevelopers[1].githubName)
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

    pickRandomReviewer(developer)
    pickMentor()
}

main(args)
