package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.ShuttleSemester
import java.time.LocalDate

data class ShuttleSemesterState(
    val name: String,
    val from: LocalDate,
    val to: LocalDate
) {

    companion object {
        val EMPTY = ShuttleSemesterState(
            name = "",
            from = LocalDate.now(),
            to = LocalDate.now()
        )
    }
}

fun ShuttleSemester.toShuttleSemesterState() = ShuttleSemesterState(
    name = name,
    from = from,
    to = to
)