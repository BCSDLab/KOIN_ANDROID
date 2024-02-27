package `in`.koreatech.koin.ui.userinfo.state

data class EmailState(
    val email: String,
    val isemailDuplicated: Boolean? = null
) {
    companion object {
        fun newEmail(email: String) = EmailState(email)
    }
}