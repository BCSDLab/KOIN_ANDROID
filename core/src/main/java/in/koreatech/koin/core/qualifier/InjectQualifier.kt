package `in`.koreatech.koin.core.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Auth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NoAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class REFRESH

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServerUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher