package `in`.koreatech.koin.core.designsystem.component.input

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

const val CHAT_MESSAGE_MAX_LENGTH = 1500

object KoinChatInputDefaults {
    val windowInsets: WindowInsets
        @Composable
        get() =
            WindowInsets.systemBars.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            )

    @Composable
    fun colors(
        iconContentColor: Color = KoinTheme.colors.primary600,
        iconContainerColor: Color = KoinTheme.colors.info200,
        textContentColor: Color = KoinTheme.colors.neutral800,
        textContainerColor: Color = KoinTheme.colors.neutral0,
        placeholderContentColor: Color = KoinTheme.colors.neutral500,
        backgroundColor: Color = KoinTheme.colors.neutral100
    ): KoinChatInputColors = KoinChatInputColors(
        iconContentColor = iconContentColor,
        iconContainerColor = iconContainerColor,
        textContentColor = textContentColor,
        textContainerColor = textContainerColor,
        placeholderContentColor = placeholderContentColor,
        backgroundColor = backgroundColor
    )

    @Composable
    fun purpleColors(
        iconContentColor: Color = RebrandKoinTheme.colors.primary600,
        iconContainerColor: Color = RebrandKoinTheme.colors.neutral0,
        textContentColor: Color = RebrandKoinTheme.colors.neutral800,
        textContainerColor: Color = RebrandKoinTheme.colors.neutral0,
        placeholderContentColor: Color = RebrandKoinTheme.colors.neutral500,
        backgroundColor: Color = RebrandKoinTheme.colors.neutral100
    ): KoinChatInputColors = KoinChatInputColors(
        iconContentColor = iconContentColor,
        iconContainerColor = iconContainerColor,
        textContentColor = textContentColor,
        textContainerColor = textContainerColor,
        placeholderContentColor = placeholderContentColor,
        backgroundColor = backgroundColor
    )
}

@Immutable
class KoinChatInputColors(
    val iconContentColor: Color,
    val iconContainerColor: Color,
    val textContentColor: Color,
    val textContainerColor: Color,
    val placeholderContentColor: Color,
    val backgroundColor: Color
)

@Composable
fun KoinChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onImageSelected: (uris: List<Uri>) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = KoinChatInputDefaults.windowInsets,
    colors: KoinChatInputColors = KoinChatInputDefaults.colors(),
    enabled: Boolean = true
) {
    val pickMultipleMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
        onImageSelected(uris)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.backgroundColor)
            .padding(16.dp)
            .windowInsetsPadding(windowInsets)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Max),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .background(colors.iconContainerColor, KoinTheme.shapes.medium)
                    .noRippleClickable(
                        enabled = enabled,
                        onClick = {
                            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    )
                    .padding(12.dp),
                painter = painterResource(id = R.drawable.ic_chat_add_photo),
                tint = colors.iconContentColor,
                contentDescription = stringResource(id = R.string.chat_add_image)
            )

            KoinChatTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = stringResource(id = R.string.chat_input_placeholder),
                enabled = enabled,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(
                        color = colors.textContainerColor,
                        shape = KoinTheme.shapes.medium
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .fillMaxHeight()
                    .weight(1f),
                colors = colors
            )

            Icon(
                modifier = Modifier
                    .background(colors.iconContainerColor, KoinTheme.shapes.medium)
                    .noRippleClickable(enabled = enabled, onClick = onSendClick)
                    .padding(12.dp),
                painter = painterResource(id = R.drawable.ic_chat_send),
                tint = colors.iconContentColor,
                contentDescription = stringResource(id = R.string.chat_send)
            )
        }
    }
}

@Composable
private fun KoinChatTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: KoinChatInputColors = KoinChatInputDefaults.colors(),
    enabled: Boolean = true
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        textStyle = KoinTheme.typography.regular14.copy(color = colors.textContentColor),
        enabled = enabled,
        onValueChange = {
            if (value.length < CHAT_MESSAGE_MAX_LENGTH) {
                onValueChange(it)
            } else {
                onValueChange(it.take(CHAT_MESSAGE_MAX_LENGTH))
            }
        },
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = KoinTheme.typography.regular12,
                        color = colors.placeholderContentColor
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview
@Composable
private fun KoinChatInputPreview() {
    KoinSurface {
        KoinChatInput(
            modifier = Modifier,
            value = "",
            onValueChange = {},
            onImageSelected = {},
            onSendClick = {},
            colors = KoinChatInputDefaults.purpleColors()
        )
    }
}
