package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester

data class ShuttleSemesterState(
    val name: String,
    val term: String
) {

    companion object {
        val EMPTY = ShuttleSemesterState(
            name = "",
            term = ""
        )
    }
}

fun ShuttleSemester.toShuttleSemesterState() = ShuttleSemesterState(
    name = name,
    term = term
)