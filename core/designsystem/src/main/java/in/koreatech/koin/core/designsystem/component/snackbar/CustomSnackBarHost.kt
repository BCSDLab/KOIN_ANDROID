package `in`.koreatech.koin.core.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

// Surface 사용하지 않을 경우, SnackBar 커스텀
@Composable
fun CustomSnackBarHost(
    hotState: SnackbarHostState,
    radius: Dp = 0.dp,
    messageTextStyle: TextStyle = KoinTheme.typography.regular12.copy(
        color = Color.White
    ),
    actionLabelTextStyle: TextStyle = KoinTheme.typography.regular12.copy(
        color = Color.White
    ),
    background: Color = Color.Black,
    alignment: Alignment = Alignment.BottomCenter,
    paddingValues: PaddingValues = PaddingValues(bottom = 20.dp, start = 10.dp, end = 10.dp),
    innerPaddingValues: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 16.dp),
    onAction: (() -> Unit)? = null
) {
    SnackbarHost(
        hostState = hotState,
    ) { snackbarData ->
        SnackBarContent(
            messageText = snackbarData.visuals.message,
            actionLabelText = snackbarData.visuals.actionLabel ?: "",
            radius = radius,
            background = background,
            messageTextStyle = messageTextStyle,
            actionLabelTextStyle = actionLabelTextStyle,
            alignment = alignment,
            paddingValues = paddingValues,
            innerPaddingValues = innerPaddingValues,
            onAction = {
                onAction?.invoke()
                snackbarData.dismiss()
            }
        )
    }

}

@Composable
private fun SnackBarContent(
    messageText: String,
    actionLabelText: String,
    modifier: Modifier = Modifier,
    radius: Dp = 0.dp,
    background: Color = Color.Black,
    messageTextStyle: TextStyle = KoinTheme.typography.regular12.copy(
        color = Color.White
    ),
    actionLabelTextStyle: TextStyle = KoinTheme.typography.regular12.copy(
        color = Color.White
    ),
    alignment: Alignment = Alignment.BottomCenter,
    paddingValues: PaddingValues = PaddingValues(bottom = 20.dp, start = 10.dp, end = 10.dp),
    innerPaddingValues: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 16.dp),
    onAction: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = alignment
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(radius))
                .background(background)
                .padding(innerPaddingValues)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = messageText,
                    style = messageTextStyle,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.weight(0.05f))
                if (actionLabelText.isNotEmpty()) {
                    Text(
                        modifier = Modifier.weight(0.2f).noRippleClickable { onAction() },
                        text = actionLabelText,
                        style = actionLabelTextStyle,
                        textAlign = TextAlign.End
                    )
                }

            }
        }
    }
}

fun SnackbarHostState.dismissIfShown() {
    if (currentSnackbarData != null) {
        currentSnackbarData?.dismiss()
    }
}

suspend fun SnackbarHostState.showSnackBarWithDismiss(
    message: String,
    actionLabel: String = "",
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    dismissIfShown()
    showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SnackBarContentPreview() {
    SnackBarContent(
        messageText = "스낵바 메시지",
        actionLabelText = "닫기",
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SnackBarContentPreview2() {
    SnackBarContent(
        messageText = "스낵바 메시지",
        actionLabelText = "되돌리기",
        paddingValues = PaddingValues(bottom = 0.dp, start = 10.dp, end = 10.dp),
    )
}