package `in`.koreatech.koin.domain.model.business.insertMenu

enum class MenuCategory(var isCheck: Boolean, val option: String){
    Option1(false, "이벤트 메뉴"),
    Option2(false, "대표 메뉴"),
    Option3(false,"사이드 메뉴"),
    Option4(false,"세트 메뉴")
}