package `in`.koreatech.koin.domain.model.bus

import java.time.LocalDate

data class ShuttleSemester(
    val name: String,
    val from: LocalDate,
    val to: LocalDate,
)
