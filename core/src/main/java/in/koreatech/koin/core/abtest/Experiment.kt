package `in`.koreatech.koin.core.abtest

enum class Experiment(
    val experimentTitle: String,
    vararg val experimentGroups: String
) {
    BENEFIT_STORE("Benefit", ExperimentGroup.A, ExperimentGroup.B),
    DINING_SHARE("campus_share_v1", ExperimentGroup.SHARE_ORIGINAL, ExperimentGroup.SHARE_NEW),
    MAIN_DINING_SEE_MORE("c_main_dining_v1", ExperimentGroup.MAIN_DINING_ORIGINAL, ExperimentGroup.MAIN_DINING_NEW),
    MAIN_ARTICLE_KEYWORD_BANNER("c_keyword_ banner_v1", ExperimentGroup.MAIN_BANNER_ORIGINAL, ExperimentGroup.MAIN_BANNER_NEW),
    BUSINESS_CALL("business_call", ExperimentGroup.CALL_NUMBER, ExperimentGroup.CALL_FLOATING),
    MAIN_BANNER_UI("a_main_banner_ui", ExperimentGroup.BOTTOM_BANNER, ExperimentGroup.CENTER_BANNER),
    MAIN_CLUB_UI("a_main_club_ui", ExperimentGroup.CATEGORY, ExperimentGroup.HOT)
    ;

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

    const val MAIN_BANNER_ORIGINAL = "banner_original"
    const val MAIN_BANNER_NEW = "banner_new"

    const val CALL_NUMBER = "call_number"
    const val CALL_FLOATING = "call_floating"

    const val BOTTOM_BANNER = "bottom_banner"
    const val CENTER_BANNER = "center_banner"

    const val CATEGORY = "category"
    const val HOT = "hot"
}
