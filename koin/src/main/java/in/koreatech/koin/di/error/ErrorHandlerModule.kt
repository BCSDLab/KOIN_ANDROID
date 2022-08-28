package `in`.koreatech.koin.di.error

import `in`.koreatech.koin.data.error.UserErrorHandlerImpl
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlerModule {

    @Provides
    @Singleton
    fun provideUserErrorHandler(
        @ApplicationContext applicationContext: Context
    ): UserErrorHandler = UserErrorHandlerImpl(applicationContext)
}