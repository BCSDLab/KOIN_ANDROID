import java.io.File

val NO_VERSION_DATA = "NO_VERSION"

fun searchReleaseVersion(prBody: String): String {
    val versionRegex = Regex("v\\d+\\.\\d+\\.\\d+")
    return versionRegex.find(prBody)?.value ?: NO_VERSION_DATA
}

fun restoreOutput(releaseVersion: String) {
    val outputPath = System.getenv("GITHUB_OUTPUT")
    File(outputPath).appendText("release_version=${releaseVersion}\n")
}

fun main() {
    val prBody = args.firstOrNull() ?: NO_VERSION_DATA
    var version = searchReleaseVersion(prBody = prBody)
    restoreOutput(releaseVersion = version)
}

main()