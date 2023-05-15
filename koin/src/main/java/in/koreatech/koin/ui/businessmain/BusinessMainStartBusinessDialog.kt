package `in`.koreatech.koin.ui.businessmain

import `in`.koreatech.koin.databinding.CustomAlertDialogBusinessStartBinding
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class BusinessMainStartBusinessDialog : DialogFragment() {

    private lateinit var binding: CustomAlertDialogBusinessStartBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = Dialog(requireContext())
        binding = CustomAlertDialogBusinessStartBinding.inflate(requireActivity().layoutInflater)
        builder.setContentView(binding.root)
        initView()
        return builder
    }

    private fun initView() {
        with(binding) {
            textViewDialogCheck.setOnClickListener {
                dismiss()
            }
        }
    }
}
