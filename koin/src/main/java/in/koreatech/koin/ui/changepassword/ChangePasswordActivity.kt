package `in`.koreatech.koin.ui.changepassword

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.databinding.ActivityChangePasswordBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordActivity : ActivityBase() {
    override val screenTitle: String
        get() = "비밀번호 변경"

    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel by viewModels<ChangePasswordViewModel>()

    private val vpAdapter by lazy {
        ChangePasswordPageAdapter(this)
    }

    val onBackPressedCallback: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.currentStep.value == ChangePasswordPage.entries.first())
                    finish()
                else
                    viewModel.changePrevPage()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListeners()
        initObservers()
    }

    private fun initView() {

        with(binding) {
            appbarChangePassword.setOnClickListener {
                when(it.id) {
                    AppBarBase.getLeftButtonId() -> { onBackPressedDispatcher.onBackPressed() }
                }
            }
        }
        with(binding.vpContainer) {
            adapter = vpAdapter
            isUserInputEnabled = false
        }
    }

    private fun initListeners() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentStep.collect {
                    binding.vpContainer.setCurrentItem(it.ordinal, true)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onFinishStep.collect {
                    viewModel.changeUserPassword()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPasswordChangeSuccess.collect { isSuccessful ->
                    if(isSuccessful) {
                        setResult(ChangePasswordContract.RESULT_PASSWORD_CHANGED)
                        finish()
                    }
                    else {
                        ToastUtil.getInstance().makeShort(R.string.server_failed)
                    }
                }
            }
        }
    }

    inner class ChangePasswordPageAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = CHANGE_PASSWORD_PAGES.size
        override fun createFragment(position: Int): Fragment = CHANGE_PASSWORD_PAGES[position]()
    }

    companion object {
        private val CHANGE_PASSWORD_PAGES = arrayOf(
            { ChangePasswordVerifyPwdFragment.getInstance() },
            { ChangePasswordChangePwdFragment.getInstance() }
        )
    }
}