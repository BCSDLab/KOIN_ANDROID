package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.util.TimeUtil
import java.util.Date

class CheckCorrectDateRangeUseCase {
    operator fun invoke(date: Date): Boolean {
        val dateDifference = TimeUtil.getDateDifferenceWithToday(date)
        return dateDifference < 7 && dateDifference > -7
    }
}