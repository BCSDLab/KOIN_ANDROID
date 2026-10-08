package `in`.koreatech.koin.core.abtest

enum class Experiment(
    val experimentTitle: String,
    vararg val experimentGroups: String
) {
    DINING_SHARE("campus_share_v1", ExperimentGroup.SHARE_ORIGINAL, ExperimentGroup.SHARE_NEW),
    DINING_SOLDOUT("icon_location", ExperimentGroup.DINING_SOLDOUT_A, ExperimentGroup.DINING_SOLDOUT_B)
    ;

    init {
        require(experimentGroups.isNotEmpty()) { "Experiment should have at least one group" }
    }
}

object ExperimentGroup {
    const val SHARE_ORIGINAL = "share_original"
    const val SHARE_NEW = "share_new"
    const val DINING_SOLDOUT_A = "soldout_design_A"
    const val DINING_SOLDOUT_B = "soldout_design_B"
}
