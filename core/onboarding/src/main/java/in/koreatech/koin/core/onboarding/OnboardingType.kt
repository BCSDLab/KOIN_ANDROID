package `in`.koreatech.koin.core.onboarding

import androidx.annotation.StringRes

enum class OnboardingType(
    @StringRes val descriptionResId: Int,
) {
    DINING_IMAGE(R.string.dining_image_tooltip),
    ARTICLE_KEYWORD(R.string.article_keyword_tooltip)
}