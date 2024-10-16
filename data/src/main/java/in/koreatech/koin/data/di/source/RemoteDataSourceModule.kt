package `in`.koreatech.koin.data.di.source

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.api.ArticleApi
import `in`.koreatech.koin.data.api.BusApi
import `in`.koreatech.koin.data.api.CoopShopApi
import `in`.koreatech.koin.data.api.DeptApi
import `in`.koreatech.koin.data.api.DiningApi
import `in`.koreatech.koin.data.api.LandApi
import `in`.koreatech.koin.data.api.OwnerApi
import `in`.koreatech.koin.data.api.PreSignedUrlApi
import `in`.koreatech.koin.data.api.StoreApi
import `in`.koreatech.koin.data.api.UploadUrlApi
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.VersionApi
import `in`.koreatech.koin.data.api.auth.ArticleAuthApi
import `in`.koreatech.koin.data.api.auth.OwnerAuthApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.source.remote.ArticleRemoteDataSource
import `in`.koreatech.koin.data.source.remote.BusRemoteDataSource
import `in`.koreatech.koin.data.source.remote.CoopShopRemoteDataSource
import `in`.koreatech.koin.data.source.remote.DeptRemoteDataSource
import `in`.koreatech.koin.data.source.remote.DiningRemoteDataSource
import `in`.koreatech.koin.data.source.remote.LandRemoteDataSource
import `in`.koreatech.koin.data.source.remote.NotificationRemoteDataSource
import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.data.source.remote.PreSignedUrlRemoteDataSource
import `in`.koreatech.koin.data.source.remote.StoreRemoteDataSource
import `in`.koreatech.koin.data.source.remote.UploadUrlRemoteDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.data.source.remote.VersionRemoteDataSource
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
    ): DiningRemoteDataSource {
        return DiningRemoteDataSource(diningApi)
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
        userAuthApi: UserAuthApi
    ): StoreRemoteDataSource {
        return StoreRemoteDataSource(storeApi, userAuthApi)
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
        articleAuthApi: ArticleAuthApi
    ): ArticleRemoteDataSource {
        return ArticleRemoteDataSource(articleApi, articleAuthApi)
    }

    @Provides
    @Singleton
    fun provideCoopShopRemoteDataSource(
        coopShopApi: CoopShopApi,
    ): CoopShopRemoteDataSource {
        return CoopShopRemoteDataSource(coopShopApi)
    }
}