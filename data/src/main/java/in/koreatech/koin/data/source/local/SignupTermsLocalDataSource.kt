package `in`.koreatech.koin.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.data.entity.term.TermEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SignupTermsLocalDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend fun getPrivacyTermText(): String = withContext(coroutineDispatcher) {
        applicationContext.assets.open(PRIVACY_TERMS_TEXT_FILE_NAME).bufferedReader().useLines { lines ->
            lines.joinToString("\n")
        }
    }

    suspend fun getKoinTermText(): String = withContext(coroutineDispatcher) {
        applicationContext.assets.open(KOIN_TERMS_TEXT_FILE_NAME).bufferedReader().useLines { lines ->
            lines.joinToString("\n")
        }
    }

    suspend fun getPrivacyTerm(): TermEntity = withContext(coroutineDispatcher) {
        applicationContext.assets.open(PRIVACY_TERMS_JSON_FILE_NAME).bufferedReader().use { it.readText() }.let {
            Json.decodeFromString<TermEntity>(it)
        }
    }

    suspend fun getKoinTerms(): TermEntity = withContext(coroutineDispatcher) {
        applicationContext.assets.open(KOIN_TERMS_JSON_FILE_NAME).bufferedReader().use { it.readText() }.let {
            Json.decodeFromString<TermEntity>(it)
        }
    }

    companion object {
        const val PRIVACY_TERMS_TEXT_FILE_NAME = "Terms_personal_information.txt"
        const val PRIVACY_TERMS_JSON_FILE_NAME = "Terms_personal_information.json"
        const val KOIN_TERMS_TEXT_FILE_NAME = "Terms_koin_sign_up.txt"
        const val KOIN_TERMS_JSON_FILE_NAME = "Terms_koin_sign_up.json"
    }
}