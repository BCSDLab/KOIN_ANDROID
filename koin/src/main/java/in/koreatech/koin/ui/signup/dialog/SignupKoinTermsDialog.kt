package `in`.koreatech.koin.ui.signup.dialog

import `in`.koreatech.koin.core.databinding.DialogTermsBinding
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.ui.signup.viewmodel.SignupKoinTermViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.windowHeight
import `in`.koreatech.koin.util.ext.windowWidth
import `in`.koreatech.koin.util.ext.withLoading
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupKoinTermsDialog : DialogFragment(), IProgressDialog {
    private val binding: DialogTermsBinding by lazy {
        DialogTermsBinding.inflate(layoutInflater)
    }
    private val signupKoinTermViewModel by viewModels<SignupKoinTermViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewModel()
        binding.dialogTermsTitle.text = "코인 이용약관"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signupKoinTermViewModel.getKoinTermText()
    }

    private fun initViewModel() = with(signupKoinTermViewModel) {
        withLoading(viewLifecycleOwner, this)
        observeLiveData(content) {
            if (it != null) {
                it.onSuccess { text ->
                    binding.dialogTermsContent.text = text
                }.onFailure { t ->
                    binding.dialogTermsContent.text = "약관을 가져오는 중 오류가 발생했습니다.\n${t.localizedMessage}."
                }
            } else {
                binding.dialogTermsContent.text = ""
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        params?.width = (windowWidth * 0.9).toInt()
        params?.height = (windowHeight * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun showProgressDialog(message: String?) {
        binding.dialogTermsProgressBar.isVisible = true
        binding.dialogTermsContent.isVisible = false
    }

    override fun showProgressDialog(resId: Int) {
        binding.dialogTermsProgressBar.isVisible = true
        binding.dialogTermsContent.isVisible = false
    }

    override fun hideProgressDialog() {
        binding.dialogTermsProgressBar.isVisible = false
        binding.dialogTermsContent.isVisible = true
    }
}