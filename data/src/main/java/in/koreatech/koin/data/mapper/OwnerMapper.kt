package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.owner.AttachmentUrlRequest
import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse
import `in`.koreatech.koin.domain.model.owner.OwnerAuthToken
import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl

fun OwnerVerificationCodeResponse.toAuthToken() = OwnerAuthToken(token = token)

fun List<OwnerRegisterUrl>.toFileUrlList(): List<AttachmentUrlRequest> {
    val list = mutableListOf<AttachmentUrlRequest>()

    this.forEach {
        list.add(it.toFileUrl())
    }

    return list
}

fun OwnerRegisterUrl.toFileUrl(): AttachmentUrlRequest = AttachmentUrlRequest(fileUrl = fileUrl)

fun List<String>.strToOwnerRegisterUrl(): List<OwnerRegisterUrl> {
    val list = mutableListOf<OwnerRegisterUrl>()

    this.forEach {
        list.add(OwnerRegisterUrl(it))
    }

    return list
}