package `in`.koreatech.koin.core.onboarding

import androidx.annotation.StringRes

/**
 * OnboardingType
 * @property descriptionResId 툴팁 내용 리소스 아이디. 툴팁이 아닐 경우 0 할당
 */
enum class OnboardingType(
    @StringRes val descriptionResId: Int,
) {
    DINING_IMAGE(R.string.dining_image_tooltip),
    DINING_NOTIFICATION(0),
    DINING_SHARE(R.string.dining_share_tooltip),
    ARTICLE_KEYWORD(R.string.article_keyword_tooltip)
}