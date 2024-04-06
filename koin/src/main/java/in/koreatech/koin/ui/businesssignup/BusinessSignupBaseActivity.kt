package `in`.koreatech.koin.ui.businesssignup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessSignupBaseBinding
import `in`.koreatech.koin.ui.businesssignup.fragment.BusinessCertificationFragment
import `in`.koreatech.koin.ui.businesssignup.fragment.BusinessSearchStoreFragment
import `in`.koreatech.koin.ui.businesssignup.fragment.BusinessSignUpBasicInfoFragment
import `in`.koreatech.koin.ui.businesssignup.fragment.BusinessVerificationFragment
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBaseViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessSignupBaseActivity: ActivityBase(R.layout.activity_business_signup_base) {
    companion object {
        private const val BASIC_INFO_FRAGMENT = "basicInfoFragment"
        private const val VERIFICATION_FRAGMENT = "verificationFragment"
        private const val CERTIFICATION_FRAGMENT = "certificationFragment"
        private const val SEARCH_STORE_FRAGMENT = "searchSToreFragment"
        private const val COMPLETE_ACTIVITY = "completeActivity"
    }

    private val binding by dataBinding<ActivityBusinessSignupBaseBinding>()
    override val screenTitle = "회원가입 For Business"
    private val viewModel by viewModels<BusinessSignUpBaseViewModel>()
    private var curFragmentTag = BASIC_INFO_FRAGMENT

    private val basicInfoFragment = BusinessSignUpBasicInfoFragment()
    private val verificationFragment = BusinessVerificationFragment()
    private val certificationFragment = BusinessCertificationFragment()
    private val searchStoreFragment = BusinessSearchStoreFragment()

    private lateinit var fileUrls: List<String>
    private lateinit var companyNumber: String
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var password: String
    private lateinit var phoneNumber: String
    private var shopId = -1
    private lateinit var shopName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showFragment(basicInfoFragment)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener {
            when(curFragmentTag) {
                BASIC_INFO_FRAGMENT -> finish()
                else -> backFragment()
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        observeLiveData(toBeShownFragment) {
            curFragmentTag = it
            currentFragment()
        }

        observeLiveData(saveFileUrls) {
            fileUrls = it
        }

        observeLiveData(saveCompanyNumber) {
            companyNumber = it
        }

        observeLiveData(saveEmail) {
            email = it
        }

        observeLiveData(saveName) {
            name = it
        }

        observeLiveData(savePassword) {
            password = it
        }

        observeLiveData(savePhoneNumber) {
            phoneNumber = it
        }

        observeLiveData(saveShopId) {
            shopId = it
        }

        observeLiveData(saveShopName) {
            shopName = it
        }
    }

    private fun currentFragment() {
        when (curFragmentTag) {
            BASIC_INFO_FRAGMENT -> showFragment(basicInfoFragment)
            VERIFICATION_FRAGMENT -> showFragment(verificationFragment)
            CERTIFICATION_FRAGMENT -> showFragment(certificationFragment)
            SEARCH_STORE_FRAGMENT -> showFragment(searchStoreFragment)
            COMPLETE_ACTIVITY -> {
                val intent = Intent(this@BusinessSignupBaseActivity, BusinessSignUpCompleteActivity::class.java)

                intent.putExtra("fileUrls", fileUrls.toTypedArray())
                intent.putExtra("companyNumber", companyNumber)
                intent.putExtra("email", email)
                intent.putExtra("name", name)
                intent.putExtra("password", password)
                intent.putExtra("phoneNumber", phoneNumber)
                intent.putExtra("shopId", shopId)
                intent.putExtra("shopName", shopName)

                startActivity(intent)
            }
        }
    }

    private fun backFragment() {
        when(curFragmentTag) {
            VERIFICATION_FRAGMENT -> viewModel.setFragmentTag("basicInfoFragment")
            CERTIFICATION_FRAGMENT -> showFragment(verificationFragment)
            SEARCH_STORE_FRAGMENT -> showFragment(certificationFragment)
        }
    }

    private fun showFragment(shownFragment: Fragment) {
        supportFragmentManager.commit {
            replace(binding.fragmentContainerView.id, shownFragment)
        }
    }
}