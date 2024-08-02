package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import `in`.koreatech.koin.domain.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class UserDataStore @Inject constructor(
    private val userDataStore: DataStore<Preferences>
) {
    fun updateIsLogin(isLogin: Boolean) {
//        TODO::구현
    }

    fun updateUserInfo(user: User) {
//        TODO::구현
    }

    fun getUserInfo(): Flow<User> {
//        TODO::구현
        return flow { }
    }

    private companion object {
        val PREF_KEY_IS_LOGIN = "KEY_USER_IS_LOGIN"
        val PREF_KEY_USER_INFO = "KEY_USER_INFO"
    }
}
