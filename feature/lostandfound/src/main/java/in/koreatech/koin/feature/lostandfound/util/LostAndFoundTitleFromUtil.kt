package `in`.koreatech.koin.feature.lostandfound.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun toLostAndFoundTitleForm(
    foundPlace: String,
    foundDate: LocalDate
): String {
    val foundDateFormatType = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return "${
        foundPlace.replace(
            "\n",
            " "
        )
    } | ${foundDate.format(foundDateFormatType)}"
}