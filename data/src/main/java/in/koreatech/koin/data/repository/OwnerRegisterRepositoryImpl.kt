package `in`.koreatech.koin.data.repository

import OwnerRegisterRequest
import `in`.koreatech.koin.data.mapper.toCategory
import `in`.koreatech.koin.data.mapper.toFileUrlList
import `in`.koreatech.koin.data.mapper.toMyStoreDayOffResponse
import `in`.koreatech.koin.data.mapper.toPhoneNumber
import `in`.koreatech.koin.data.mapper.toStringArray
import `in`.koreatech.koin.data.request.owner.OwnerEmailRegisterRequest
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import retrofit2.HttpException
import java.io.EOFException

class OwnerRegisterRepositoryImpl(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
) : OwnerRegisterRepository {
    override suspend fun ownerEmailRegister(
        attachments: List<OwnerRegisterUrl>,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int?,
        shopName: String
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.postOwnerEmailRegister(
                OwnerEmailRegisterRequest(
                    attachments.toFileUrlList(),
                    companyNumber,
                    email,
                    name,
                    password,
                    phoneNumber,
                    shopId,
                    shopName
                )
            )

            Result.success(Unit)
        } catch (e: EOFException) {
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun ownerRegister(
        attachments: List<OwnerRegisterUrl>,
        companyNumber: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int?,
        shopName: String
    ) {
        ownerRemoteDataSource.postOwnerRegister(
            OwnerRegisterRequest(
                attachments.toFileUrlList(),
                companyNumber,
                name,
                password,
                phoneNumber,
                shopId,
                shopName
            )
        )
    }


    override suspend fun storeRegister(
        name: String,
        category: Int,
        address: String,
        imageUri: String,
        phoneNumber: String,
        deliveryPrice: String,
        description: String,
        operatingTime: List<OperatingTime>,
        isDeliveryOk: Boolean,
        isCardOk: Boolean,
        isBankOk: Boolean
    ): Result<Unit> {
        return try {
            ownerRemoteDataSource.postStoreRegister(
                StoreRegisterResponse(
                    address = address,
                    categoryIds = category.toCategory(),
                    delivery = isDeliveryOk,
                    delivery_price = deliveryPrice.toInt(),
                    description = description,
                    imageUrls = imageUri.toStringArray(),
                    name = name,
                    open = operatingTime.toMyStoreDayOffResponse(),
                    payBank = isBankOk,
                    payCard = isCardOk,
                    phone = phoneNumber?.toPhoneNumber() ?: ""
                )
            )
            Result.success(Unit)
        } catch (e: EOFException) {
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}