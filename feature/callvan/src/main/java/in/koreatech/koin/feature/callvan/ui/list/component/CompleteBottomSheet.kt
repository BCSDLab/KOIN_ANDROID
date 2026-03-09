package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanConfirmBottomSheet

@Composable
fun CompleteBottomSheet(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CallvanConfirmBottomSheet(
        title = stringResource(R.string.callvan_complete_title),
        description = stringResource(R.string.callvan_complete_description),
        confirmText = stringResource(R.string.callvan_confirm_positive),
        cancelText = stringResource(R.string.callvan_confirm_negative),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Preview
@Composable
private fun CompleteBottomSheetPreview() {
    CallvanConfirmBottomSheet(
        title = "이용 완료 상태로 변경할까요?",
        description = "• 콜밴 이용(탑승, 정산)이 모두 완료된 뒤 눌러야 합니다.\n• 완료 시 대화 내역이 삭제되며, 되돌릴 수 없습니다.",
        confirmText = "확인",
        cancelText = "취소",
        onConfirm = {},
        onDismiss = {}
    )
}