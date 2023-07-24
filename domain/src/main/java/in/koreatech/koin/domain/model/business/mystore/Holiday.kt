package `in`.koreatech.koin.domain.model.business.mystore

enum class Holiday(var isHoliday: Boolean, val day: String){
    Mon(false, "월"),
    Tues(false, "화"),
    Wed(false,"수"),
    Thurs(false,"목"),
    Fri(false,"금"),
    Sat(false, "토"),
    Sun(false,"일")
}