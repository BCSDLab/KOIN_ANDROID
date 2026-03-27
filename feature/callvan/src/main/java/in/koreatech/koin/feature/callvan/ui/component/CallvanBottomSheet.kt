package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallvanBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    showCloseButton: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = RebrandKoinTheme.colors.neutral0,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                if (showCloseButton) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_bottom_sheet_close),
                            contentDescription = null,
                            tint = RebrandKoinTheme.colors.neutral600
                        )
                    }
                }
            }
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
            content()
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
        }
    }
}

@Preview
@Composable
private fun CallvanBottomSheetPreview() {
    CallvanBottomSheet(
        title = "알림을 모두 삭제할까요?",
        onDismiss = {},
        showCloseButton = true
    ) {}
}
