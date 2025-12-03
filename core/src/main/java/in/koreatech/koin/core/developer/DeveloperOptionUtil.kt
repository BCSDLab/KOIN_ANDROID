package `in`.koreatech.koin.core.developer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object DeveloperOptionUtil {
    private val _developerOptions = MutableStateFlow(mapOf<DeveloperOption, Boolean>())

    fun getDeveloperOption(developerOption: DeveloperOption): Flow<Boolean> {
        return _developerOptions.map { it[developerOption] ?: false }.distinctUntilChanged()
    }

    fun setDeveloperOption(developerOption: DeveloperOption, value: Boolean) {
        _developerOptions.update {
            it.toMutableMap().apply { put(developerOption, value) }
        }
    }
}
