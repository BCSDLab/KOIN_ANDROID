package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import `in`.koreatech.koin.data.mapper.toInt
import `in`.koreatech.koin.data.mapper.toUser
import `in`.koreatech.koin.data.response.user.UserResponse
import `in`.koreatech.koin.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserDataStore @Inject constructor(
    private val userDataStore: DataStore<Preferences>
) {
    // Exception 이 발생할 경우 null 반환
    val user: Flow<User?> = userDataStore.data.map { pref ->
        try {
            pref[PREF_KEY_USER_INFO].let {
                if (it == null) return@map null
                if (!it.equals("Anonymous") && pref[PREF_KEY_IS_LOGIN] == true) {
                    return@map Gson().fromJson(it, UserResponse::class.java).toUser()
                }
            }
            return@map User.Anonymous
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
        val PREF_KEY_IS_LOGIN = booleanPreferencesKey("KEY_USER_IS_LOGIN")
        val PREF_KEY_USER_INFO = stringPreferencesKey("KEY_USER_INFO")
    }
}