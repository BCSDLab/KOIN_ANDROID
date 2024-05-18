package `in`.koreatech.koin.data.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.DefaultDispatcher
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.usecase.store.SearchStoreUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideSearchStoreUseCase(
        storeRepository: StoreRepository,
        @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): SearchStoreUseCase {
        return SearchStoreUseCase(storeRepository, coroutineDispatcher)
    }
}
