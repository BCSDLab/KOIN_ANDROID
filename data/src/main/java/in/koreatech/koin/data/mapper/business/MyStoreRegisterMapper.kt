package `in`.koreatech.koin.data.mapper.business

import `in`.koreatech.koin.data.response.business.MyStoreDayOffReponse
import `in`.koreatech.koin.domain.model.business.mystore.MyStoreDayOff

fun ArrayList<MyStoreDayOff>.toMyStoreDayOffReponse(): ArrayList<MyStoreDayOffReponse> {
    val responseList = ArrayList<MyStoreDayOffReponse>()
    for (dayOff in this) {

        if(dayOff.closed){
            val response = MyStoreDayOffReponse(null, true, dayOff.dayOfWeek, null )
            responseList.add(response)
        }
        else{
            val response = MyStoreDayOffReponse(dayOff.closeTime.toString(), dayOff.closed, dayOff.dayOfWeek, dayOff.openTime.toString() )
            responseList.add(response)
        }
    }
    return responseList
}

fun String.toPhoneNumber() : String{
    val digitsOnly = this.filter { it.isDigit() }

    return when (digitsOnly.length) {
        11 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 7)}-${digitsOnly.substring(7)}"
        10 -> "${digitsOnly.substring(0, 3)}-${digitsOnly.substring(3, 6)}-${digitsOnly.substring(6)}"
        else -> digitsOnly
    }
}

fun String.toCategory(): ArrayList<Int>{
    val categoryId: ArrayList<Int> = ArrayList<Int>()
    when(this){
        "치킨" -> categoryId.add(2)
        "피자" -> categoryId.add(3)
        "도시락" -> categoryId.add(4)
        "족발" -> categoryId.add(5)
        "중국집" -> categoryId.add(6)
        "일반음식점" -> categoryId.add(7)
        "카페" -> categoryId.add(8)
        "미용실" -> categoryId.add(9)
        "기타" -> categoryId.add(10)
    }
    return categoryId
}

fun String.toImageUri(): ArrayList<String>{
    val imageUri: ArrayList<String> = ArrayList<String>()
    imageUri.add(this)
    return imageUri
}