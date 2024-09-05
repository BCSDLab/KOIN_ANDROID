package `in`.koreatech.koin.ui.changepassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentChangePasswordVerifyPwdBinding

@AndroidEntryPoint
class ChangePasswordVerifyPwdFragment : Fragment(R.layout.fragment_change_password_verify_pwd) {

    private val binding by dataBinding<FragmentChangePasswordVerifyPwdBinding>()
    private val viewModel by activityViewModels<ChangePasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnNext.setOnClickListener { viewModel.changeNextPage() }
        }
    }

    companion object {
        fun getInstance(): ChangePasswordVerifyPwdFragment = ChangePasswordVerifyPwdFragment()
    }
}