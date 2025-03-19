package `in`.koreatech.koin.domain.model.user

data class LoginInformation(
    val portalAccount: String,
    val passwordHashed: String
)
