package `in`.koreatech.koin.data.source.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TokenLocalDataSource @Inject constructor(
    @ApplicationContext applicationContext: Context,
    private val dispatchersIO: CoroutineDispatcher,
) {
    var masterKey = MasterKey.Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    var sharedPreferences = EncryptedSharedPreferences.create(
        applicationContext,
        SHARED_PREF_FILENAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var sharedHistoryPreferences = EncryptedSharedPreferences.create(
        applicationContext,
        SHARED_PREF_HISTORY_KEY,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    var sharedOwnerPreferences = EncryptedSharedPreferences.create(
        applicationContext,
        OWNER_SHARED_PREF_FILENAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveAccessToken(
        accessToken: String,
    ) = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            putString(SHARED_PREF_KEY, accessToken)
            apply()
        }
    }

    suspend fun saveRefreshToken(
        refreshToken: String,
    ) = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            putString(SHARED_PREF_REFRESH_KEY, refreshToken)
            apply()
        }
    }

    suspend fun saveAccessHistoryId(
        accessHistoryId: String,
    ) = withContext(dispatchersIO) {
        with(sharedHistoryPreferences.edit()) {
            putString(SHARED_PREF_HISTORY_KEY, accessHistoryId)
            apply()
        }
    }


    suspend fun getAccessToken(): String? = withContext(dispatchersIO) {
        sharedPreferences.getString(SHARED_PREF_KEY, null)
    }

    suspend fun getRefreshToken(): String? = withContext(dispatchersIO) {
        sharedPreferences.getString(SHARED_PREF_REFRESH_KEY, null)
    }

    suspend fun getAccessHistoryId(): String? = withContext(dispatchersIO) {
        sharedHistoryPreferences.getString(SHARED_PREF_HISTORY_KEY, null)
    }

    suspend fun removeAccessToken() = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            remove(SHARED_PREF_KEY)
            apply()
        }
    }

    suspend fun saveOwnerAccessToken(
        accessToken: String
    ) = with(Dispatchers.IO) {
        with(sharedOwnerPreferences.edit()) {
            putString(OWNER_SHARED_PREF_FILENAME, accessToken)
            apply()
        }
    }

    suspend fun getOwnerAccessToken(): String? = withContext(Dispatchers.IO) {
        sharedOwnerPreferences.getString(OWNER_SHARED_PREF_FILENAME, null)
    }

    suspend fun removeOwnerAccessToken() = withContext(Dispatchers.IO) {
        with(sharedOwnerPreferences.edit()) {
            remove(OWNER_SHARED_PREF_FILENAME)
            apply()
        }
    }

    suspend fun removeRefreshToken() = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            remove(SHARED_PREF_REFRESH_KEY)
            apply()
        }
    }

    suspend fun getDeviceToken(): String? = withContext(dispatchersIO) {
        sharedPreferences.getString(SHARED_DEVICE_KEY, null)
    }

    fun saveDeviceToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(SHARED_DEVICE_KEY, token)
            apply()
        }
    }

    suspend fun removeDeviceToken() = withContext(dispatchersIO) {
        with(sharedPreferences.edit()) {
            remove(SHARED_DEVICE_KEY)
            apply()
        }
    }

    companion object {
        private const val SHARED_PREF_FILENAME = "token"
        private const val OWNER_SHARED_PREF_FILENAME = "ownerToken"

        private const val SHARED_PREF_KEY = "accessToken"
        private const val SHARED_PREF_REFRESH_KEY = "refreshToken"
        private const val SHARED_PREF_HISTORY_KEY = "accessHistoryId"

        private const val SHARED_DEVICE_KEY = "deviceToken"
    }
}