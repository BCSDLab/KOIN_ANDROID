package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse
import `in`.koreatech.koin.domain.model.owner.OwnerAuthToken

fun OwnerVerificationCodeResponse.toAuthToken() = OwnerAuthToken(token = token)