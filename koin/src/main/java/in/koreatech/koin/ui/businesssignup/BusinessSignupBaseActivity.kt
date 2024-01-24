package `in`.koreatech.koin.ui.businesssignup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessSignupBaseBinding
import `in`.koreatech.koin.ui.businesssignup.fragment.BusinessCertificationFragment
import `in`.koreatech.koin.ui.businesssignup.fragment.BusinessSignUpBasicInfoFragment
import `in`.koreatech.koin.ui.businesssignup.fragment.BusinessVerificationFragment
import `in`.koreatech.koin.ui.businesssignup.viewmodel.BusinessSignUpBaseViewModel
import `in`.koreatech.koin.util.ext.observeLiveData

@AndroidEntryPoint
class BusinessSignupBaseActivity: ActivityBase(R.layout.activity_business_signup_base) {
    private val binding by dataBinding<ActivityBusinessSignupBaseBinding>()
    private val viewModel by viewModels<BusinessSignUpBaseViewModel>()
    private var curFragmentTag = "basicInfoFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        changeFragment(Fragment(), BusinessSignUpBasicInfoFragment())

        supportFragmentManager.commit {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            addToBackStack(null)
        }

        signupBackButton.setOnClickListener {
            val shownFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)?: Fragment()

            when(curFragmentTag) {
                "basicInfoFragment" -> {
                    finish()
                }
                "verificationFragment" -> {
                    changeFragment(shownFragment, BusinessSignUpBasicInfoFragment())
                }
                "certificationFragment" -> {
                    changeFragment(shownFragment, BusinessVerificationFragment())
                }
                "searchStoreFragment" -> {
                    changeFragment(shownFragment, BusinessCertificationFragment())
                }
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        observeLiveData(toBeShownFragment) {
            curFragmentTag = it
        }
    }

    private fun changeFragment(curFragment: Fragment, nextFragment: Fragment) {
        supportFragmentManager.commit {
            hide(curFragment)
            showFragment(nextFragment)
        }
    }

    private fun FragmentTransaction.showFragment(fragment: Fragment) {
        supportFragmentManager.executePendingTransactions()
        if(fragment.isAdded) show(fragment)
        else add(binding.fragmentContainerView.id, fragment).show(fragment)
    }
}