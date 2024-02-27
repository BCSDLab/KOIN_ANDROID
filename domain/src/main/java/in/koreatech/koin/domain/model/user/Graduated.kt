package `in`.koreatech.koin.domain.model.user

sealed class Graduated {
    object Graduate: Graduated()
    object Student: Graduated()
}