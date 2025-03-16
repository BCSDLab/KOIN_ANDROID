package `in`.koreatech.koin.ui.article

import androidx.annotation.StringRes
import `in`.koreatech.koin.R

enum class ArticleBoardType(
    val id: Int,
    @StringRes val koreanName: Int,
    @StringRes val simpleKoreanName: Int,
    val linkType: LinkType,
    val exposedInAll: Boolean = true
) {
    // FREE(1, R.string.article_free),              // NOT USED
    // EMPLOYMENT(2, R.string.article_employment),  // NOT USED
    // ANONYMOUS(3, R.string.article_anonymous),    // NOT USED
    ALL(4, R.string.article_all, R.string.article_all, LinkType.NONE),
    LOSTANDFOUND(
        14,
        R.string.article_lost_and_found,
        R.string.article_lost_and_found,
        LinkType.NONE
    ),
    NORMAL(5, R.string.article_normal, R.string.article_normal_simple, LinkType.ARTICLE),
    SCHOLARSHIP(
        6,
        R.string.article_scholarship,
        R.string.article_scholarship_simple,
        LinkType.ARTICLE
    ),
    SCHOOL(7, R.string.article_school, R.string.article_school_simple, LinkType.ARTICLE),
    RECRUIT(8, R.string.article_recruit, R.string.article_recruit_simple, LinkType.STEMS),

    // QNA(10, R.string.article_qna),               // NOT USED
    // PROMOTION(11, R.string.article_promotion),   // NOT USED
    IPP(12, R.string.article_ipp, R.string.article_ipp_simple, LinkType.PORTAL),
    STUDENT(13, R.string.article_student, R.string.article_student_simple, LinkType.PORTAL, false),
    KOIN(9, R.string.article_koin, R.string.article_koin, LinkType.NONE, false) ;

    companion object {
        fun fromId(id: Int): ArticleBoardType {
            return when (id) {
                4 -> ALL
                14 -> LOSTANDFOUND
                5 -> NORMAL
                6 -> SCHOLARSHIP
                7 -> SCHOOL
                8 -> RECRUIT
                12 -> IPP
                13 -> STUDENT
                9 -> KOIN
                else -> ALL
            }
        }
    }
}

/**
 * Koreatech 페이지로 이동할 때 사용
 * @property NONE 링크 없음
 * @property ARTICLE 원본 게시글로 이동 (로그인 필요없는 게시판)
 * @property PORTAL 아우누리로 이동 (로그인 필요한 게시판)
 * @property STEMS 학생종합경력개발로 이동 (로그인 필요한 게시판)
 */
enum class LinkType {
    NONE,
    ARTICLE,
    PORTAL,
    STEMS
}
