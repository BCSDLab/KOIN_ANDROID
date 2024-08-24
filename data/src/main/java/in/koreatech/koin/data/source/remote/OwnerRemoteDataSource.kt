package `in`.koreatech.koin.data.source.remote

import OwnerRegisterRequest
import `in`.koreatech.koin.data.api.OwnerApi
import `in`.koreatech.koin.data.api.auth.OwnerAuthApi
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.request.owner.OwnerEmailRegisterRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.request.owner.VerificationCodeSmsRequest
import `in`.koreatech.koin.data.request.owner.VerificationSmsRequest
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreMenuRegisterResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse

class OwnerRemoteDataSource(
    private val ownerApi: OwnerApi,
    private val ownerAuthApi: OwnerAuthApi
) {
    suspend fun postVerificationCode(ownerVerificationCode: OwnerVerificationCodeRequest): OwnerVerificationCodeResponse {
        return ownerApi.postVerificationCode(ownerVerificationCode)
    }

    suspend fun postVerificationEmail(ownerVerificationEmail: OwnerVerificationEmailRequest) {
        return ownerApi.postVerificationEmail(ownerVerificationEmail)
    }

    suspend fun changePasswordVerificationCode(ownerVerificationCode: OwnerVerificationCodeRequest) {
        return ownerApi.changePasswordVerificationCode(ownerVerificationCode)
    }

    suspend fun changePasswordVerificationEmail(ownerVerificationEmail: OwnerVerificationEmailRequest) {
        return ownerApi.changePasswordVerificationEmail(ownerVerificationEmail)
    }

    suspend fun postOwnerEmailRegister(ownerEmailRegisterRequest: OwnerEmailRegisterRequest): OwnerResponse {
        return ownerApi.postOwnerEmailRegister(ownerEmailRegisterRequest)
    }

    suspend fun postOwnerRegister(ownerRegisterRequest: OwnerRegisterRequest){
        return ownerApi.postOwnerRegister(ownerRegisterRequest)
    }

    suspend fun ownerChangePassword(ownerChangePasswordRequest: OwnerChangePasswordRequest) {
        return ownerApi.changePassword(ownerChangePasswordRequest)
    }

    suspend fun checkExistsAccount(account: String) {
        ownerApi.checkExistsAccount(account)
    }

    suspend fun postVerificationSms(ownerVerificationEmail: VerificationSmsRequest) {
        return ownerApi.postVerificationSms(ownerVerificationEmail)
    }

    suspend fun postVerificationCodeSms(ownerVerificationCode: VerificationCodeSmsRequest): OwnerVerificationCodeResponse {
        return ownerApi.postVerificationCodeSms(ownerVerificationCode)
    }

    suspend fun getMyShopList(): List<StoreItemResponse> {
        return ownerAuthApi.getMyShopList().shops
    }

    suspend fun getOwnerShopInfo(storeUid: Int): StoreRegisterResponse {
        return ownerAuthApi.getOwnerShopInfo(storeUid)
    }

    suspend fun getOwnerShopMenus(storeUid: Int): StoreMenuResponse {
        return ownerAuthApi.getOwnerShopMenus(storeUid)
    }

    suspend fun getOwnerShopEvents(storeUid: Int): StoreDetailEventResponse {
        return ownerAuthApi.getOwnerShopEvents(storeUid)
    }

    suspend fun postStoreRegister(storeRegisterResponse: StoreRegisterResponse): StoreRegisterResponse {
        return ownerAuthApi.postMyStore(storeRegisterResponse)
    }

    suspend fun postStoreMenu(storeId: Int, storeMenuRegisterResponse: StoreMenuRegisterResponse): StoreMenuRegisterResponse {
        return ownerAuthApi.postShopMenu(storeId, storeMenuRegisterResponse)
    }

    suspend fun deleteOwnerShopEvent(storeUid: Int, eventId: Int) {
        ownerAuthApi.deleteOwnerShopEvent(storeUid, eventId)
    }

    suspend fun modifyOwnerShopInfo(storeUid: Int, storeInfo: StoreRegisterResponse) {
        ownerAuthApi.modifyOwnerShopInfo(storeUid, storeInfo)
    }
}