package `in`.koreatech.koin.ui.businesssignup

import android.content.Intent
import `in`.koreatech.koin.R
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.data.mapper.strToOwnerRegisterUrl
import `in`.koreatech.koin.databinding.ActivityBusinessSignUpCompleteBinding
import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl
import `in`.koreatech.koin.ui.businesslogin.BusinessLoginActivity
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpCompleteViewModel
import `in`.koreatech.koin.util.SnackbarUtil
import `in`.koreatech.koin.util.ext.observeLiveData

@AndroidEntryPoint
class BusinessSignUpCompleteActivity : ActivityBase(R.layout.activity_business_sign_up_complete) {
    private val binding by dataBinding<ActivityBusinessSignUpCompleteBinding>()
    private val businessSignUpCompleteViewModel by viewModels<BusinessSignUpCompleteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener {
            finish()
        }

        val fileUrls = intent.getStringArrayExtra("fileUrls")?.toList() ?: emptyList()
        val storeNumber = intent.getStringExtra("companyNumber") ?: ""
        val email = intent.getStringExtra("email") ?: "error"
        val name = intent.getStringExtra("name") ?: "error"
        val password = intent.getStringExtra("password") ?: "error"
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: "error"
        val storeId = intent.getIntExtra("shopId", -1)
        val storeName = intent.getStringExtra("shopName") ?: "error"

        goLoginActivityButton.setOnClickListener {
            businessSignUpCompleteViewModel.sendRegisterRequest(
                fileUrls.strToOwnerRegisterUrl(), storeNumber, email, name, password, phoneNumber, storeId, storeName
            )
        }
    }

    private fun initViewModel() = with(businessSignUpCompleteViewModel) {
        observeLiveData(businessCompleteContinuationState) {
            startActivity(Intent(this@BusinessSignUpCompleteActivity, BusinessLoginActivity::class.java))
        }

        observeLiveData(businessCompleteContinuationError) { t ->
            SnackbarUtil.makeShortSnackbar(
                binding.root,
                when(t) {
                    is OwnerError.NotValidEmailException -> getString(R.string.is_not_valid_email)
                    is OwnerError.AlreadyUsingEmailException -> getString(R.string.is_already_using_email)
                    is OwnerError.AlreadyUsingRegistrationNumberException -> getString(R.string.is_already_registration_number)
                    is OwnerError.AlreadyValidIdException -> getString(R.string.is_already_using_id)
                    is OwnerError.OverDueTimeException -> getString(R.string.overdue_time)
                    is OwnerError.IncorrectVerificationCodeException -> getString(R.string.incorrect_verification_code)
                    else -> getString(R.string.failed_signup)
                }
            )
        }
    }
}