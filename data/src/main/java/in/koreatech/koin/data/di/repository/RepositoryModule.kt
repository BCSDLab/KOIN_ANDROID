package `in`.koreatech.koin.data.di.repository

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.data.repository.ArticleRepositoryImpl
import `in`.koreatech.koin.data.repository.BusRepositoryImpl
import `in`.koreatech.koin.data.repository.CoopShopRepositoryImpl
import `in`.koreatech.koin.data.repository.DeptRepositoryImpl
import `in`.koreatech.koin.data.repository.DiningRepositoryImpl
import `in`.koreatech.koin.data.repository.LandRepositoryImpl
import `in`.koreatech.koin.data.repository.NotificationRepositoryImpl
import `in`.koreatech.koin.data.repository.OnboardingRepositoryImpl
import `in`.koreatech.koin.data.repository.OwnerChangePasswordRepositoryImpl
import `in`.koreatech.koin.data.repository.OwnerRegisterRepositoryImpl
import `in`.koreatech.koin.data.repository.OwnerShopRepositoryImpl
import `in`.koreatech.koin.data.repository.OwnerSignupRepositoryImpl
import `in`.koreatech.koin.data.repository.OwnerVerificationCodeRepositoryImpl
import `in`.koreatech.koin.data.repository.PreSignedUrlRepositoryImpl
import `in`.koreatech.koin.data.repository.SignupRepositoryImpl
import `in`.koreatech.koin.data.repository.StoreRepositoryImpl
import `in`.koreatech.koin.data.repository.TokenRepositoryImpl
import `in`.koreatech.koin.data.repository.UploadUrlRepositoryImpl
import `in`.koreatech.koin.data.repository.UserRepositoryImpl
import `in`.koreatech.koin.data.repository.VersionRepositoryImpl
import `in`.koreatech.koin.data.source.local.ArticleLocalDataSource
import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.local.DeptLocalDataSource
import `in`.koreatech.koin.data.source.local.OnboardingLocalDataSource
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.UploadImageLocalDataSource
import `in`.koreatech.koin.data.source.local.UserLocalDataSource
import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
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
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import `in`.koreatech.koin.domain.repository.DeptRepository
import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.repository.LandRepository
import `in`.koreatech.koin.domain.repository.NotificationRepository
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.repository.VersionRepository
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
    fun provideOnboardingRepository(
        onboardingLocalDataSource: OnboardingLocalDataSource
    ): OnboardingRepository {
        return OnboardingRepositoryImpl(onboardingLocalDataSource)
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