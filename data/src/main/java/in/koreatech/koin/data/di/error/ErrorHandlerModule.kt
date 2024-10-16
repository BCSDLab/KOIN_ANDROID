package `in`.koreatech.koin.data.di.error

import `in`.koreatech.koin.data.error.BusErrorHandlerImpl
import `in`.koreatech.koin.data.error.DeptErrorHandlerImpl
import `in`.koreatech.koin.data.error.TokenErrorHandlerImpl
import `in`.koreatech.koin.data.error.UserErrorHandlerImpl
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.error.dept.DeptErrorHandler
import `in`.koreatech.koin.domain.error.token.TokenErrorHandler
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.error.CoopShopErrorHandlerImpl
import `in`.koreatech.koin.data.error.OwnerErrorHandlerImpl
import `in`.koreatech.koin.domain.error.coopshop.CoopShopErrorHandler
import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlerModule {

    @Provides
    @Singleton
    fun provideUserErrorHandler(
        @ApplicationContext applicationContext: Context
    ): UserErrorHandler = UserErrorHandlerImpl(applicationContext)

    @Provides
    @Singleton
    fun provideDeptErrorHandler(
        @ApplicationContext applicationContext: Context
    ): DeptErrorHandler = DeptErrorHandlerImpl(applicationContext)

    @Provides
    @Singleton
    fun provideTokenErrorHandler(
        @ApplicationContext applicationContext: Context
    ): TokenErrorHandler = TokenErrorHandlerImpl(applicationContext)

    @Provides
    @Singleton
    fun provideBusErrorHandler(
        @ApplicationContext applicationContext: Context
    ): BusErrorHandler = BusErrorHandlerImpl(applicationContext)

    @Provides
    @Singleton
    fun provideOwnerErrorHandler(
        @ApplicationContext applicationContext: Context
    ): OwnerErrorHandler = OwnerErrorHandlerImpl(applicationContext)

    @Provides
    @Singleton
    fun provideCoopShopErrorHandler(
        @ApplicationContext applicationContext: Context
    ): CoopShopErrorHandler = CoopShopErrorHandlerImpl(applicationContext)
}