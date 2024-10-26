package `in`.koreatech.koin.data.di.source

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.source.remote.firebase.messaging.FirebaseMessageDataSource
import `in`.koreatech.koin.data.source.remote.firebase.messaging.FirebaseMessagingDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsDataSourceRemoteModule {
    @Binds
    @Singleton
    abstract fun bindsFirebaseMessagingDataSource(
        firebaseMessageDataSourceImpl: FirebaseMessagingDataSourceImpl
    ): FirebaseMessageDataSource
}