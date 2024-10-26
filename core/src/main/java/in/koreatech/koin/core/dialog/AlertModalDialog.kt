package `in`.koreatech.koin.core.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import `in`.koreatech.koin.core.databinding.AlertModalDialogBinding

class AlertModalDialog(
    private val context: Context,
    private val data: AlertModalDialogData,
    private val onPositiveButtonClicked: (Dialog) -> Unit,
    private val onNegativeButtonClicked: (Dialog) -> Unit
) : Dialog(context) {

    private lateinit var binding: AlertModalDialogBinding

    override fun onStart() {
        super.onStart()
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlertModalDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            textViewAlertModalTitle.text = context.getString(data.title)
            textViewAlertModalMessage.text = context.getString(data.message)
            buttonPositive.text = context.getString(data.positiveButtonText)
            buttonNegative.text = context.getString(data.negativeButtonText)

            buttonPositive.setOnClickListener {
                onPositiveButtonClicked(this@AlertModalDialog)
            }

            buttonNegative.setOnClickListener {
                onNegativeButtonClicked(this@AlertModalDialog)
            }
        }
    }
}
