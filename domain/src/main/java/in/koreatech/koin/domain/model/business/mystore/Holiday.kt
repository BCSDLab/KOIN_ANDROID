package `in`.koreatech.koin.domain.model.business.mystore

enum class Holiday(var isHoliday: Boolean, val day: String, val dayEng: String){
    Mon(false, "월", "MONDAY"),
    Tues(false, "화", "TUESDAY"),
    Wed(false,"수","WEDNESDAY"),
    Thurs(false,"목", "THURSDAY"),
    Fri(false,"금", "FRIDAY"),
    Sat(false, "토", "SATURDAY"),
    Sun(false,"일", "SUNDAY")
}