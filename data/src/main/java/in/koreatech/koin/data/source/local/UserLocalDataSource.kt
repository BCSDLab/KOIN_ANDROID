package `in`.koreatech.koin.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    @ApplicationContext applicationContext: Context,
){
    companion object {
        const val PREF_NAME = "PREF_USER_NAME"
    }
}