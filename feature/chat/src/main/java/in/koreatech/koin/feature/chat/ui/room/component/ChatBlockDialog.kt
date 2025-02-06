package `in`.koreatech.koin.feature.chat.ui.room.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.feature.chat.R

@Composable
fun ChatBlockDialog(
    onBlockUser: () -> Unit = {},
    onBlockCancel: () -> Unit = {}
) {
    ChoiceDialog(
        title = stringResource(id = R.string.block_dialog_title),
        description = stringResource(id = R.string.block_dialog_description),
        onPositive = onBlockUser,
        onNegative = onBlockCancel
    )
}