package `in`.koreatech.koin.ui.businesssignup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusinessSignupBaseActivity: ActivityBase(R.layout.activity_business_signup_base) {
    private val binding by dataBinding<ActivityBusinessSignupBaseBinding>()
    private val viewModel by viewModels<BusinessSignUpBaseViewModel>()
    private var curFragmentTag = "BASIC_INFO_FRAGMENT"

    private val basicInfoFragment = BusinessSignUpBasicInfoFragment()
    private val verificationFragment = BusinessVerificationFragment()
    private val certificationFragment = BusinessCertificationFragment()
    private val searchStoreFragment = BusinessSearchStoreFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showFragment(basicInfoFragment)
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        signupBackButton.setOnClickListener {
            when(curFragmentTag) {
                "BASIC_INFO_FRAGMENT" -> finish()
                else -> backFragment()
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        observeLiveData(toBeShownFragment) {
            curFragmentTag = it
            currentFragment()
        }
    }

    private fun currentFragment() {
        when (curFragmentTag) {
            "BASIC_INFO_FRAGMENT" -> showFragment(basicInfoFragment)
            "VERIFICATION_FRAGMENT" -> showFragment(verificationFragment)
            "CERTIFICATION_FRAGMENT" -> showFragment(certificationFragment)
            "SEARCH_STORE_FRAGMENT" -> showFragment(searchStoreFragment)
        }
    }

    private fun backFragment() {
        when(curFragmentTag) {
            "VERIFICATION_FRAGMENT" -> viewModel.setFragmentTag("BASIC_INFO_FRAGMENT")
            "CERTIFICATION_FRAGMENT" -> showFragment(verificationFragment)
            "SEARCH_STORE_FRAGMENT" -> showFragment(certificationFragment)
        }
    }

    private fun showFragment(shownFragment: Fragment) {
        supportFragmentManager.commit {
            replace(binding.fragmentContainerView.id, shownFragment)
        }
    }
}