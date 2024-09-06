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
import `in`.koreatech.koin.databinding.ReviewDeleteDialogBinding
import `in`.koreatech.koin.util.ext.windowWidth

class ReviewDeleteCheckDialog(
    val onDelete: () -> Unit,
    val onCancel: () -> Unit,
) : DialogFragment() {
    private lateinit var binding: ReviewDeleteDialogBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReviewDeleteDialogBinding.inflate(inflater, container, false)
        val spannableString = SpannableString(getString(R.string.check_delete_review))
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorAccent)),
            4,
            6,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textViewTitle.text = spannableString
        binding.buttonDelete.setOnClickListener {
            onDelete()
            dismiss()
        }
        binding.buttonCancle.setOnClickListener {
            onCancel()
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