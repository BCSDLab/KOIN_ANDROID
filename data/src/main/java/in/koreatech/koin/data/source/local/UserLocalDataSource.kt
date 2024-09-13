package `in`.koreatech.koin.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.mapper.toInt
import `in`.koreatech.koin.data.mapper.toUser
import `in`.koreatech.koin.data.response.user.UserResponse
import `in`.koreatech.koin.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    @ApplicationContext applicationContext: Context,
) {
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREF_NAME
    )

    private val userDataStore = applicationContext.userDataStore

    // Exception 이 발생할 경우 null 반환
    val user: Flow<User?> = userDataStore.data.map { pref ->
        try {
            if (pref[PREF_KEY_IS_LOGIN] == true) {
                return@map Gson().fromJson(pref[PREF_KEY_USER_INFO], UserResponse::class.java).toUser()
            } else {
                return@map User.Anonymous
            }
        } catch (e: Exception) {
            return@map null
        }
    }

    suspend fun updateIsLogin(isLogin: Boolean) {
        userDataStore.edit { pref ->
            pref[PREF_KEY_IS_LOGIN] = isLogin
        }
    }

    // TODO::유저 정보 중 필수 값 확인 후 수정
    suspend fun updateUserInfo(user: User) {
        userDataStore.edit { pref ->
            pref[PREF_KEY_USER_INFO] = if (user is User.Student) {
                Gson().toJson(
                    UserResponse(
                        anonymousNickname = user.anonymousNickname,
                        email = user.email,
                        gender = user.gender.toInt(),
                        major = user.major,
                        name = user.name ?: "",
                        nickname = user.nickname,
                        phoneNumber = user.phoneNumber,
                        studentNumber = user.studentNumber
                    )
                )
            } else {
                "Anonymous"
            }
        }
    }

    private companion object {
        const val PREF_NAME = "PREF_USER_NAME"
        val PREF_KEY_IS_LOGIN = booleanPreferencesKey("KEY_USER_IS_LOGIN")
        val PREF_KEY_USER_INFO = stringPreferencesKey("KEY_USER_INFO")
    }
}