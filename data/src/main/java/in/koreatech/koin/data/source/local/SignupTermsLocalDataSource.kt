package `in`.koreatech.koin.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.channels.AsynchronousFileChannel

class SignupTermsLocalDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    suspend fun getPrivacyTermText(): String = withContext(Dispatchers.IO) {
        applicationContext.assets.open(PRIVACY_TERMS_TEXT_FILE_NAME).bufferedReader().useLines { lines ->
            lines.joinToString("\n")
        }
    }

    suspend fun getKoinTermText(): String = withContext(Dispatchers.IO) {
        applicationContext.assets.open(KOIN_TERMS_TEXT_FILE_NAME).bufferedReader().useLines { lines ->
            lines.joinToString("\n")
        }
    }

    companion object {
        const val PRIVACY_TERMS_TEXT_FILE_NAME = "Terms_personal_information.txt"
        const val KOIN_TERMS_TEXT_FILE_NAME = "Terms_koin_sign_up.txt"
    }
}