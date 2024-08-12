package `in`.koreatech.koin.data.di.source

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.ArticleApi
import `in`.koreatech.koin.data.api.*
import `in`.koreatech.koin.data.api.auth.DiningAuthApi
import `in`.koreatech.koin.data.api.auth.OwnerAuthApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.remote.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {
    @Provides
    @Singleton
    fun provideNotificationRemoteDataSource(
        userAuthApi: UserAuthApi,
    ): NotificationRemoteDataSource {
        return NotificationRemoteDataSource(userAuthApi)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        userApi: UserApi,
        userAuthApi: UserAuthApi,
    ): UserRemoteDataSource {
        return UserRemoteDataSource(userApi, userAuthApi)
    }

    @Provides
    @Singleton
    fun provideOwnerRemoteDataSource(
        ownerApi: OwnerApi,
        ownerAuthApi: OwnerAuthApi,
    ): OwnerRemoteDataSource {
        return OwnerRemoteDataSource(ownerApi, ownerAuthApi)
    }

    @Provides
    @Singleton
    fun provideUploadUrlRemoteDataSource(
        uploadUrlApi: UploadUrlApi,
    ): UploadUrlRemoteDataSource {
        return UploadUrlRemoteDataSource(uploadUrlApi)
    }

    @Provides
    @Singleton
    fun provideVersionRemoteDataSource(
        versionApi: VersionApi,
    ): VersionRemoteDataSource {
        return VersionRemoteDataSource(versionApi)
    }

    @Provides
    @Singleton
    fun provideDeptRemoteDataSource(
        deptApi: DeptApi,
    ): DeptRemoteDataSource {
        return DeptRemoteDataSource(deptApi)
    }

    @Provides
    @Singleton
    fun provideDiningRemoteDataSource(
        diningApi: DiningApi,
        diningAuthApi: DiningAuthApi
    ): DiningRemoteDataSource {
        return DiningRemoteDataSource(diningApi, diningAuthApi)
    }

    @Provides
    @Singleton
    fun provideBusRemoteDataSource(
        busApi: BusApi,
    ): BusRemoteDataSource {
        return BusRemoteDataSource(busApi)
    }

    @Provides
    @Singleton
    fun provideStoreRemoteDataSource(
        storeApi: StoreApi,
    ): StoreRemoteDataSource {
        return StoreRemoteDataSource(storeApi)
    }

    @Provides
    @Singleton
    fun provideLandRemoteDataSource(
        landApi: LandApi,
    ): LandRemoteDataSource {
        return LandRemoteDataSource(landApi)
    }

    @Provides
    @Singleton
    fun providePreSignedUrlRemoteDataSource(
        preSignedUrlApi: PreSignedUrlApi,
    ): PreSignedUrlRemoteDataSource {
        return PreSignedUrlRemoteDataSource(preSignedUrlApi)
    }

    @Provides
    @Singleton
    fun provideArticleRemoteDataSource(
        articleApi: ArticleApi,
    ): ArticleRemoteDataSource {
        return ArticleRemoteDataSource(articleApi)
    }
}