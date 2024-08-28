package `in`.koreatech.koin.data.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.DefaultDispatcher
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import `in`.koreatech.koin.domain.usecase.article.FetchHotArticlesUseCase
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
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

    @Provides
    @Singleton
    fun provideGetMarketPreSignedUrlUseCase(
        uploadUrlRepository: UploadUrlRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): GetMarketPreSignedUrlUseCase {
        return GetMarketPreSignedUrlUseCase(uploadUrlRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideUploadFileUseCase(
        preSignedUrlRepository: PreSignedUrlRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): UploadFileUseCase {
        return UploadFileUseCase(preSignedUrlRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideFetchHotArticlesUseCase(
        articleRepository: ArticleRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): FetchHotArticlesUseCase {
        return FetchHotArticlesUseCase(articleRepository, coroutineDispatcher)
    }
}
