package `in`.koreatech.koin.feature.lostandfound.enums

/*
 Included from main koin module because we can't access ArticleBoardType from lostandfound module
 */

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.lostandfound.R

enum class ArticleBoardType(
    val id: Int,
    @StringRes val koreanName: Int,
    @StringRes val simpleKoreanName: Int,
    val linkType: LinkType,
    val exposedInAll: Boolean = true
) {
    ALL(4, R.string.article_all, R.string.article_all, LinkType.NONE),
    LOSTANDFOUND(14, R.string.article_lost_and_found, R.string.article_lost_and_found, LinkType.NONE),
    NORMAL(5, R.string.article_normal, R.string.article_normal_simple, LinkType.ARTICLE),
    SCHOLARSHIP(6, R.string.article_scholarship, R.string.article_scholarship_simple, LinkType.ARTICLE),
    SCHOOL(7, R.string.article_school, R.string.article_school_simple, LinkType.ARTICLE),
    RECRUIT(8, R.string.article_recruit, R.string.article_recruit_simple, LinkType.STEMS),
    IPP(12, R.string.article_ipp, R.string.article_ipp_simple, LinkType.PORTAL),
    STUDENT(13, R.string.article_student, R.string.article_student_simple, LinkType.PORTAL, false),
    KOIN(9, R.string.article_koin, R.string.article_koin, LinkType.NONE, false)
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
