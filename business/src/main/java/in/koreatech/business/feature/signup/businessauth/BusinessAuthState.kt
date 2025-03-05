package `in`.koreatech.business.feature.signup.businessauth

import android.graphics.Bitmap
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

data class BusinessAuthState(
    val name: String = "",
    val shopName: String = "",
    val companyNumber: String = "", // 사업자 등록 번호
    val shopNumber: String = "", // 가게 전화번호
    val shopId: Int? = null,
    val openAlertDialog: Boolean = false,
    val selectedImages: MutableList<AttachStore> = mutableListOf(),
    val dialogVisibility: Boolean = false,
    val imageUriList: List<String> = emptyList(),
    val fileInfo: MutableList<StoreUrl> = mutableListOf(),
    val bitmap: MutableList<Bitmap> = mutableListOf(),
    val signupContinuationState: SignupContinuationState = SignupContinuationState.RequestedSmsValidation,
    val error: Throwable? = null,
) {
    val isButtonEnabled: Boolean
        get() = name.isNotEmpty() && shopName.isNotEmpty() && companyNumber.isNotEmpty() && selectedImages.isNotEmpty() && signupContinuationState == SignupContinuationState.SuccessUploadFiles
}
