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
annotation class UserAgent

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ServerUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OwnerAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OwnerUserAgent

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PreSignedUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PreSignedUserAgent

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Refresh

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UnconfinedDispatcher
