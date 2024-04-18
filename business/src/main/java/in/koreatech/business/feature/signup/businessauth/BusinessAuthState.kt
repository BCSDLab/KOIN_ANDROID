package `in`.koreatech.business.feature.signup.businessauth

data class BusinessAuthState(
    val name: String = "",
    val storeName: String = "",
    val storeNumber: String = "",
    val phoneNumber: String = "",
    val openAlertDialog: Boolean = false,
    val selectedImageUris :MutableList<String> = mutableListOf(),
    val dialogVisibility:Boolean = false
)
