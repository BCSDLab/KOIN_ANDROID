package `in`.koreatech.koin.domain.model.owner

data class OwnerAuthToken(
    val token: String,
    val owner: Owner
)