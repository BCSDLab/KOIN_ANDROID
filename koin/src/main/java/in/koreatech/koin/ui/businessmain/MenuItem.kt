package `in`.koreatech.koin.ui.businessmain

data class MenuItem(
    val title: String,
    val imageResource: Int,
    var isSelected: Boolean = false
)
