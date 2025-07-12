package `in`.koreatech.koin.domain.repository

interface ModalRepository {
    fun setInfoRequiredShown(value: Boolean)
    fun setIsInfoRequired(value: Boolean)
    fun getInfoRequiredShown(): Boolean
    fun getIsInfoRequired(): Boolean
}
