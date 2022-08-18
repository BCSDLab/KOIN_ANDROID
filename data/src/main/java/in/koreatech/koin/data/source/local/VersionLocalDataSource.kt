package `in`.koreatech.koin.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VersionLocalDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    fun getVersion(): String? {
        return applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName, 0
        )?.versionName
    }
}