package `in`.koreatech.koin.core.notification.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.notification.Notifier
import `in`.koreatech.koin.core.notification.NotifierImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    @Singleton
    abstract fun bindsNotifier(notifierImpl: NotifierImpl): Notifier
}
