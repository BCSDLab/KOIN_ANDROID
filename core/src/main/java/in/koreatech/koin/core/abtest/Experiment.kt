package `in`.koreatech.koin.core.abtest

enum class Experiment(
    val experimentTitle: String,
    vararg val experimentGroups: String
) {

    BENEFIT_STORE("Benefit", ExperimentGroup.A, ExperimentGroup.B),
    DINING_SHARE("campus_share_v1", ExperimentGroup.SHARE_ORIGINAL, ExperimentGroup.SHARE_NEW),
    MAIN_DINING_SEE_MORE("c_main_dining_v1", ExperimentGroup.MAIN_DINING_ORIGINAL, ExperimentGroup.MAIN_DINING_NEW);

    init {
        require(experimentGroups.isNotEmpty()) { "Experiment should have at least one group" }
    }
}

object ExperimentGroup {
    const val A = "A"
    const val B = "B"

    const val SHARE_ORIGINAL = "share_original"
    const val SHARE_NEW = "share_new"

    const val MAIN_DINING_ORIGINAL = "main_dining_original"
    const val MAIN_DINING_NEW = "main_dining_new"
}