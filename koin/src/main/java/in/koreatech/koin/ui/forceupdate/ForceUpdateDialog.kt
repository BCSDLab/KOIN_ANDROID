package `in`.koreatech.koin.ui.forceupdate

import android.os.Bundle
import android.view.View
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.BaseDialogFragment
import `in`.koreatech.koin.databinding.DialogForceUpdateBinding
import `in`.koreatech.koin.util.ext.navigatePlayStore

class ForceUpdateDialog : BaseDialogFragment<DialogForceUpdateBinding>(
    R.layout.dialog_force_update,
    DialogForceUpdateBinding::inflate
) {
    companion object {
        const val TAG = "ForceUpdateDialog"

        fun newInstance(): ForceUpdateDialog {
            val args = Bundle()

            val fragment = ForceUpdateDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutSizeRatio(widthPercent = 0.77f)
        initEvent()
    }

    private fun initEvent() {
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
        binding.btnStore.setOnClickListener {
            requireContext().navigatePlayStore()
        }
    }
}