package `in`.koreatech.koin.domain.model.store

enum class OpenStatus {
    OPERATING,
    PREPARING,
    CLOSED
}

fun OpenStatus.isOpen(): Boolean {
    return this == OpenStatus.OPERATING
}
