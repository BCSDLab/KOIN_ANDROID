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
enum class Developer(val githubName: String, val isMentor: Boolean = false) {
    YUNJAENA("yunjaena", true),
    JAEYOUNG290("JaeYoung290"),
    KONGWOOJIN("kongwoojin"),
    KYM_P("KYM-P"),
    JUSANG3057("jusang3057"),
    TTRR1007("TTRR1007")
}

/**
 * Reviewer count
 * Need to edit pick_reviewer.yml too
 */
val reviewerCount = 2

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
    Developer.KYM_P to Developer.TTRR1007,
    Developer.JUSANG3057 to Developer.KYM_P,
    Developer.TTRR1007 to Developer.JUSANG3057
)

/**
 * Regex to extract the team from the pull request title.
 */
val titleRegex = Regex("""(?<=\[)(user|campus|business)(?=\])""")

/**
 * Export the reviewer name to GitHub Actions output.
 * @param reviewers The name of the reviewers.
 */
fun exportReviewer(reviewers: List<String>) {
    val githubOutput = System.getenv("GITHUB_OUTPUT")
    reviewers.forEachIndexed { index, reviewer ->
        File(githubOutput).appendText("reviewer${index + 1}=$reviewer\n")
    }
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
    exportReviewer(listOf(reviewer.githubName))
}

/**
 * Pick a random reviewer.
 */
fun pickRandomReviewer(developer: Developer?) {
    val reviewers = Developer.entries
        .filter { it != developer }
        .filter { !it.isMentor }
        .shuffled()
        .take(reviewerCount)
    exportReviewer(reviewers.map { it.githubName })
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
