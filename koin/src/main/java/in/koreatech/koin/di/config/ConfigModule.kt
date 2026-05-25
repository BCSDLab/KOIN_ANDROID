package `in`.koreatech.koin.di.config

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {
    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = Firebase.crashlytics

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = Firebase.messaging
}
