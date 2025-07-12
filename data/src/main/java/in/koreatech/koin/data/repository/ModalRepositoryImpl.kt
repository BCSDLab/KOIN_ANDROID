package `in`.koreatech.koin.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import `in`.koreatech.koin.domain.repository.ModalRepository
import javax.inject.Inject

class ModalRepositoryImpl @Inject constructor(
    context: Context
) : ModalRepository {
    private val infoRequiredPrefs: SharedPreferences = context.getSharedPreferences("info_required", Context.MODE_PRIVATE)

    override fun setInfoRequiredShown(value: Boolean) {
        infoRequiredPrefs.edit { putBoolean("infoRequiredShown", value) }
    }

    override fun setIsInfoRequired(value: Boolean) {
        infoRequiredPrefs.edit { putBoolean("isInfoRequired", value) }
    }

    override fun getInfoRequiredShown(): Boolean {
        return infoRequiredPrefs.getBoolean("infoRequiredShown", false)
    }

    override fun getIsInfoRequired(): Boolean {
        return infoRequiredPrefs.getBoolean("isInfoRequired", true)
    }
}
