package `in`.koreatech.koin.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.DefaultDispatcher
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.core.qualifier.MainDispatcher
import `in`.koreatech.koin.core.qualifier.UnconfinedDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoroutineDispatchersModule {

    @IoDispatcher
    @Singleton
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @UnconfinedDispatcher
    @Singleton
    @Provides
    fun providesUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    @DefaultDispatcher
    @Singleton
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @MainDispatcher
    @Singleton
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}