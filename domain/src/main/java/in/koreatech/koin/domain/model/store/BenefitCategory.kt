package `in`.koreatech.koin.domain.model.store

data class BenefitCategory (
    val id: Int,
    val title: String,
    val detail: String,
    val onImageUrl: String,
    val offImageUrl: String,
)

data class BenefitCategoryList(
    val benefitCategories: List<BenefitCategory>
)