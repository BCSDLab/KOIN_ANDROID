package `in`.koreatech.koin.data.di.repository

import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.repository.TimetableRepositoryImpl
import `in`.koreatech.koin.data.repository.firebase.messaging.FirebaseMessagingRepositoryImpl
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.repository.firebase.messaging.FirebaseMessagingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsFirebaseMessagingRepository(
        firebaseMessagingRepositoryImpl: FirebaseMessagingRepositoryImpl
    ): FirebaseMessagingRepository

    @Binds
    @Singleton
    abstract fun bindsTimetableRepository(
        timetableRepositoryImpl: TimetableRepositoryImpl
    ): TimetableRepository
}