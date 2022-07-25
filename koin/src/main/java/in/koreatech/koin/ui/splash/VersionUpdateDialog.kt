package `in`.koreatech.koin.ui.splash

import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.VersionUpdateDialogBinding
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import `in`.koreatech.koin.ui.splash.viewmodel.SplashViewModel
import `in`.koreatech.koin.util.ext.windowHeight
import `in`.koreatech.koin.util.ext.windowWidth
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class VersionUpdateDialog(
    private val versionUpdatePriority: VersionUpdatePriority,
    private val currentVersion: String,
    private val latestVersion: String,
) : DialogFragment() {
    private lateinit var binding: VersionUpdateDialogBinding

    private val viewModel by activityViewModels<SplashViewModel>()

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
                viewModel.checkToken()
                dismiss()
            }
            versionUpdateDialogUpdateTextview.setOnClickListener {
                gotoPlayStore()
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


    private fun gotoPlayStore() {
        val appPackageName: String = requireContext().packageName
        try {
            val appStoreIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            appStoreIntent.setPackage("com.android.vending")
            ContextCompat.startActivity(requireContext(), appStoreIntent, null)
        } catch (exception: ActivityNotFoundException) {
            ContextCompat.startActivity(
                requireContext(),
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                ),
                null
            )
        }
    }
}