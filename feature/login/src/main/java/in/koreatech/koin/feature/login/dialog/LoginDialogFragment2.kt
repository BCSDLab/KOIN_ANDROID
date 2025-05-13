package `in`.koreatech.koin.feature.login.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import `in`.koreatech.koin.feature.login.R
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog

class LoginDialogFragment2(
    private val title: @Composable () -> Unit,
    private val description: @Composable () -> Unit,
    private val onPositive: () -> Unit,
    private val onNegative: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                ChoiceDialog(
                    title = title,
                    description = description,
                    onPositive = {
                        onPositive()
                        dismiss()
                    },
                    onNegative = {
                        onNegative()
                    },
                    positiveButtonText = getString(R.string.go_login),
                    negativeButtonText = getString(R.string.close)
                )
            }
        }
        return AlertDialog.Builder(requireContext())
            .setView(composeView)
            .setCancelable(true)
            .create()
    }
}
