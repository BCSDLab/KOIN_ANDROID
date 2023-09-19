package `in`.koreatech.koin.domain.model.owner

import `in`.koreatech.koin.domain.model.store.Store

sealed class Owner {
    data class Businessman(
        val attachments: Attachments,
        val isGraduated: Boolean,
        val companyNumber: String?,
        val email: String?,
        val name: String?,
        val password: String?,
        val phoneNumber: String?,
        val shop: Store?
    ): Owner()

    data class Attachments(val fileName: String, val fileUrl: String, val id: Int)

    object Anonymous: Owner()

    val isOwner get() = this is Businessman
}