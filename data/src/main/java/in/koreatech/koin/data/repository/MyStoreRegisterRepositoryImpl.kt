package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.business.toCategory
import `in`.koreatech.koin.data.mapper.business.toImageUri
import `in`.koreatech.koin.data.mapper.business.toMyStoreDayOffReponse
import `in`.koreatech.koin.data.mapper.business.toPhoneNumber
import `in`.koreatech.koin.data.response.business.MyStoreDayOffReponse
import `in`.koreatech.koin.data.response.business.MyStoreRegisterResponse
import `in`.koreatech.koin.data.source.remote.business.MyStoreRegisterDataSource
import `in`.koreatech.koin.domain.error.owner.InvalidAccessException
import `in`.koreatech.koin.domain.error.owner.NotHaveAuthorityException
import `in`.koreatech.koin.domain.model.business.mystore.MyStoreDayOff
import `in`.koreatech.koin.domain.repository.MyStoreRegisterRepository
import retrofit2.HttpException
import javax.inject.Inject


class MyStoreRegisterRepositoryImpl @Inject constructor(
    private val myStoreRegisterDataSource: MyStoreRegisterDataSource,
) : MyStoreRegisterRepository {
    override suspend fun registerOwnerStore(
        name: String,
        category: String,
        address: String,
        imageUri: String,
        phoneNumber: String,
        deliveryPrice: String,
        description: String,
        dayOff: ArrayList<MyStoreDayOff>,
        openTime: String,
        closeTime: String,
        isDeliveryOk: Boolean,
        isCardOk: Boolean,
        isBankOk: Boolean
    ): Result<Unit> {
        return try {

            myStoreRegisterDataSource.putMyStoreItems(
                MyStoreRegisterResponse(
                    address = address,
                    categoryIds = category.toCategory(),
                    delivery = isDeliveryOk,
                    delivery_price = deliveryPrice.toInt(),
                    imageUrls = imageUri.toImageUri(),
                    name = name,
                    open = dayOff.toMyStoreDayOffReponse(),
                    payBank = isBankOk,
                    payCard = isCardOk,
                    phone = phoneNumber?.toPhoneNumber() ?: ""
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            if(e.code() == 401) Result.failure(InvalidAccessException())
            else if(e.code() == 403) Result.failure(NotHaveAuthorityException())
            else Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}