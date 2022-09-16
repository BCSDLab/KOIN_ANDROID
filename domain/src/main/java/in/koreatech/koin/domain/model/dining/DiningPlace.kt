package `in`.koreatech.koin.domain.model.dining

sealed class DiningPlace(
    val placeCode: Int,
    val placeEnglish: String,
    val placeKorean: String
) {
    object Korean: DiningPlace(0, "Korean", "한식")
    object Onedish: DiningPlace(1, "Onedish", "일품식")
    object Western: DiningPlace(2, "Western", "양식")
    object Special: DiningPlace(3, "Special", "특식")
    object Nungsu: DiningPlace(4, "Nungsu", "능수관")
    object Subakyeo: DiningPlace(5, "Subakyeo", "수박여")
}
