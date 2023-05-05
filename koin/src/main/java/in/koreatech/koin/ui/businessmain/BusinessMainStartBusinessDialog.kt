package `in`.koreatech.koin.ui.businessmain

import `in`.koreatech.koin.databinding.CustomAlertDialogBusinessStartBinding
import android.app.Dialog
import android.content.Context
import android.os.Bundle

class BusinessMainStartBusinessDialog(
    context: Context
) : Dialog(context) {
    private lateinit var binding: CustomAlertDialogBusinessStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomAlertDialogBusinessStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        with(binding) {
            textViewDialogCheck.setOnClickListener {
                dismiss()
            }
        }
    }
}