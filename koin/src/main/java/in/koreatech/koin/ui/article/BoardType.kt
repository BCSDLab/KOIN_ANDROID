package `in`.koreatech.koin.ui.article

import androidx.annotation.StringRes
import `in`.koreatech.koin.R

enum class BoardType(
    val id: Int,
    @StringRes val koreanName: Int,
    @StringRes val simpleKoreanName: Int,
    val exposedInAll: Boolean = true
) {
    // FREE(1, R.string.article_free),              // NOT USED
    // EMPLOYMENT(2, R.string.article_employment),  // NOT USED
    // ANONYMOUS(3, R.string.article_anonymous),    // NOT USED
    ALL(4, R.string.article_all, R.string.article_all),
    NORMAL(5, R.string.article_normal, R.string.article_normal_simple),
    SCHOLARSHIP(6, R.string.article_scholarship, R.string.article_scholarship_simple),
    SCHOOL(7,  R.string.article_school, R.string.article_school_simple),
    RECRUIT(8, R.string.article_recruit, R.string.article_recruit_simple),
    // KOIN(9, R.string.article_koin, R.string.article_koin),
    // QNA(10, R.string.article_qna),               // NOT USED
    // PROMOTION(11, R.string.article_promotion),   // NOT USED
     IPP(12, R.string.article_ipp, R.string.article_ipp_simple),
     STUDENT(13, R.string.article_student, R.string.article_student_simple, false),
}