package `in`.koreatech.koin.ui.store.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.LoginRequestDialogBinding
import `in`.koreatech.koin.util.ext.windowWidth

class LoginRequestDialog(
    val goToLogin: () -> Unit
) : DialogFragment() {
    private lateinit var binding: LoginRequestDialogBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginRequestDialogBinding.inflate(inflater, container, false)
        val spannableString = SpannableString(getString(R.string.login_request_to_write_review))
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorPrimary)),
            8,
            11,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewTitle.text = spannableString
        binding.buttonDelete.setOnClickListener {
            goToLogin()
            dismiss()
        }
        binding.buttonCancle.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        params?.width = (windowWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}
