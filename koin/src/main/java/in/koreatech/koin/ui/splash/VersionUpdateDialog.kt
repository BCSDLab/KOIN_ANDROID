package `in`.koreatech.koin.ui.splash

import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.VersionUpdateDialogBinding
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.util.ext.windowHeight
import `in`.koreatech.koin.util.ext.windowWidth
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment

class VersionUpdateDialog(
    private val versionUpdatePriority: VersionUpdatePriority,
    private val currentVersion: String,
    private val latestVersion: String
) : DialogFragment() {
    private lateinit var binding: VersionUpdateDialogBinding

    var dialogOptionClickListener: DialogOptionClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VersionUpdateDialogBinding.inflate(
            LayoutInflater.from(context)
        )

        with(binding) {
            versionUpdateDialogLaterTextview.isVisible =
                versionUpdatePriority != VersionUpdatePriority.High
            versionUpdateDialogInfoTextview.text = requireActivity().getString(
                if (versionUpdatePriority != VersionUpdatePriority.High) {
                    R.string.version_update_message_recommended
                } else {
                    R.string.version_update_message_required
                }
            )
            versionUpdateDialogCurrentVersionTextview.text =
                requireActivity().getString(R.string.version_update_current_version, currentVersion)
            versionUpdateDialogUpdateVersionTextview.text =
                requireActivity().getString(R.string.version_update_update_version, latestVersion)

            versionUpdateDialogLaterTextview.setOnClickListener {
                dialogOptionClickListener?.onLaterButtonClicked()
                dismiss()
            }
            versionUpdateDialogUpdateTextview.setOnClickListener {
                dialogOptionClickListener?.onUpdateButtonClicked()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        params?.width = (windowWidth * 0.8).toInt()
        params?.height = (windowHeight * 0.4).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    inline fun setDialogOptionClickListener(
        crossinline onLaterButtonClicked: () -> Unit,
        crossinline onUpdateButtonClicked: () -> Unit
    ) {
        dialogOptionClickListener = object : DialogOptionClickListener {
            override fun onLaterButtonClicked() {
                onLaterButtonClicked()
            }

            override fun onUpdateButtonClicked() {
                onUpdateButtonClicked()
            }

        }
    }

    interface DialogOptionClickListener {
        fun onLaterButtonClicked()
        fun onUpdateButtonClicked()
    }
}