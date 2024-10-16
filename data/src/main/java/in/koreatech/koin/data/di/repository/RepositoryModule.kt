package `in`.koreatech.koin.data.di.repository

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.data.repository.*
import `in`.koreatech.koin.data.source.local.*
import `in`.koreatech.koin.data.source.remote.*
import `in`.koreatech.koin.domain.repository.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationRemoteDataSource: NotificationRemoteDataSource
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(
        tokenLocalDataSource: TokenLocalDataSource,
        userLocalDataSource: UserLocalDataSource
    ): TokenRepository {
        return TokenRepositoryImpl(tokenLocalDataSource, userLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        tokenLocalDataSource: TokenLocalDataSource,
        userLocalDataSource: UserLocalDataSource
    ): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource, tokenLocalDataSource, userLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideSignupRepository(
        userRemoteDataSource: UserRemoteDataSource,
        signupTermsLocalDataSource: SignupTermsLocalDataSource
    ): SignupRepository {
        return SignupRepositoryImpl(userRemoteDataSource, signupTermsLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideOwnerSignupRepository(
        ownerRemoteDataSource: OwnerRemoteDataSource,
        signupTermsLocalDataSource: SignupTermsLocalDataSource
    ): OwnerSignupRepository {
        return OwnerSignupRepositoryImpl(ownerRemoteDataSource, signupTermsLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideOwnerVerificationCodeRepository(
        ownerRemoteDataSource: OwnerRemoteDataSource
    ): OwnerVerificationCodeRepository {
        return OwnerVerificationCodeRepositoryImpl(ownerRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideUploadUrlRepository(
        uploadUrlRemoteDataSource: UploadUrlRemoteDataSource
    ): UploadUrlRepository {
        return UploadUrlRepositoryImpl(uploadUrlRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideRegisterRepository(
        ownerRemoteDataSource: OwnerRemoteDataSource
    ): OwnerRegisterRepository {
        return OwnerRegisterRepositoryImpl(ownerRemoteDataSource)
    }


    @Provides
    @Singleton
    fun provideVersionRepository(
        versionLocalDataSource: VersionLocalDataSource,
        versionRemoteDataSource: VersionRemoteDataSource,
    ): VersionRepository {
        return VersionRepositoryImpl(
            versionLocalDataSource,
            versionRemoteDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideDeptRepository(
        deptRemoteDataSource: DeptRemoteDataSource,
        deptLocalDataSource: DeptLocalDataSource
    ): DeptRepository {
        return DeptRepositoryImpl(
            deptRemoteDataSource, deptLocalDataSource
        )
    }

    @Provides
    @Singleton
    fun provideDiningRepository(
        diningRemoteDataSource: DiningRemoteDataSource
    ): DiningRepository {
        return DiningRepositoryImpl(diningRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideBusRepository(
        @ApplicationContext applicationContext: Context,
        busLocalDataSource: BusLocalDataSource,
        busRemoteDataSource: BusRemoteDataSource
    ): BusRepository {
        return BusRepositoryImpl(applicationContext, busLocalDataSource, busRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideStoreRepository(
        storeRemoteDataSource: StoreRemoteDataSource
    ): StoreRepository {
        return StoreRepositoryImpl(storeRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideLandRepository(
        landRemoteDataSource: LandRemoteDataSource
    ): LandRepository {
        return LandRepositoryImpl(landRemoteDataSource)
    }

    @Provides
    @Singleton
    fun providePreSignedUrlRepository(
        preSignedUrlRemoteDataSource: PreSignedUrlRemoteDataSource,
        uploadImageLocalDataSource: UploadImageLocalDataSource
    ): PreSignedUrlRepository {
        return PreSignedUrlRepositoryImpl(preSignedUrlRemoteDataSource, uploadImageLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideOwnerChangePasswordRepository(
        ownerRemoteDataSource: OwnerRemoteDataSource
    ): OwnerChangePasswordRepository {
        return OwnerChangePasswordRepositoryImpl(ownerRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideOwnerShopRepository(
        ownerRemoteDataSource: OwnerRemoteDataSource
    ): OwnerShopRepository {
        return OwnerShopRepositoryImpl(ownerRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideOnBoardingRepository(
        onBoardingLocalDataSource: OnBoardingLocalDataSource
    ): OnBoardingRepository {
        return OnBoardingRepositoryImpl(onBoardingLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(
        articleRemoteDataSource: ArticleRemoteDataSource,
        articleLocalDataSource: ArticleLocalDataSource,
        userRepository: UserRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ArticleRepository {
        return ArticleRepositoryImpl(
            articleRemoteDataSource,
            articleLocalDataSource,
            userRepository,
            CoroutineScope(SupervisorJob() + dispatcher)
        )
    }

    @Provides
    @Singleton
    fun provideCoopShopRepository(
        coopShopRemoteDataSource: CoopShopRemoteDataSource
    ): CoopShopRepository {
        return CoopShopRepositoryImpl(coopShopRemoteDataSource)
    }
}