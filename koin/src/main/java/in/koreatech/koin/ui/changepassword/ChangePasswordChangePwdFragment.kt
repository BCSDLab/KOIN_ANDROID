package `in`.koreatech.koin.ui.changepassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.FragmentChangePasswordChangePwdBinding

@AndroidEntryPoint
class ChangePasswordChangePwdFragment : Fragment(R.layout.fragment_change_password_change_pwd) {

    private val binding by dataBinding<FragmentChangePasswordChangePwdBinding>()
    private val viewModel by activityViewModels<ChangePasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            btnNext.setOnClickListener { }
        }
    }

    companion object {
        fun getInstance(): ChangePasswordChangePwdFragment = ChangePasswordChangePwdFragment()
    }
}