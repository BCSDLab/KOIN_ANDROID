package `in`.koreatech.koin.di.config

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import `in`.koreatech.koin.core.notification.NotificationResolver
import `in`.koreatech.koin.firebase.ChatNotificationResolver
import `in`.koreatech.koin.firebase.OrderNotificationResolver

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationResolverModule {
    @Binds
    @IntoMap
    @StringKey("order")
    abstract fun bindsOrderNotificationResolver(
        resolver: OrderNotificationResolver
    ): NotificationResolver

    @Binds
    @IntoMap
    @StringKey("chat")
    abstract fun bindsChatNotificationResolver(
        resolver: ChatNotificationResolver
    ): NotificationResolver
}
