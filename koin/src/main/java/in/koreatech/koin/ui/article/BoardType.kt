package `in`.koreatech.koin.ui.article

import androidx.annotation.StringRes
import `in`.koreatech.koin.R

enum class BoardType(
    val id: Int,
    @StringRes val koreanName: Int
) {
    // FREE(1, R.string.article_free),              // NOT USED
    // EMPLOYMENT(2, R.string.article_employment),  // NOT USED
    // ANONYMOUS(3, R.string.article_anonymous),    // NOT USED
    ALL(4, R.string.article_all),
    NORMAL(5, R.string.article_normal),
    SCHOLARSHIP(6, R.string.article_scholarship),
    SCHOOL(7,  R.string.article_school),
    RECRUIT(8, R.string.article_recruit),
    KOIN(9, R.string.article_koin),
    // QNA(10, R.string.article_qna),               // NOT USED
    // PROMOTION(11, R.string.article_promotion),   // NOT USED
    //IPP(10, R.string.article_ipp),                // API 준비중
    //STUDENT(11, R.string.article_student),        // API 준비중
}