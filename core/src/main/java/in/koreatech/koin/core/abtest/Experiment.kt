package `in`.koreatech.koin.core.abtest

enum class Experiment(
    val experimentTitle: String,
    vararg val experimentGroups: String
) {
    DINING_SHARE("campus_share_v1", ExperimentGroup.SHARE_ORIGINAL, ExperimentGroup.SHARE_NEW)
    ;

    init {
        require(experimentGroups.isNotEmpty()) { "Experiment should have at least one group" }
    }
}

object ExperimentGroup {
    const val SHARE_ORIGINAL = "share_original"
    const val SHARE_NEW = "share_new"
}
