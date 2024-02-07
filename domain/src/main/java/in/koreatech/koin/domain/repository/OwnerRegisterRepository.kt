package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.Owner

interface OwnerRegisterRepository {
    suspend fun ownerRegister(
        attachments: Owner.AttachmentUrls,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: String,
        shopName: String
    ): Result<Unit>
}