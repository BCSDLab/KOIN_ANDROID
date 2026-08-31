package `in`.koreatech.koin.feature.recruitment.ui.chat.components

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

private const val RECRUITMENT_CHAT_MESSAGE_MAX_LENGTH = 500

object RecruitmentChatInputDefaults {
    val windowInsets: WindowInsets
        @Composable
        get() = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)

    @Composable
    fun colors(
        iconContentColor: Color = RebrandKoinTheme.colors.primary600,
        iconContainerColor: Color = RebrandKoinTheme.colors.neutral0,
        textContentColor: Color = RebrandKoinTheme.colors.neutral800,
        textContainerColor: Color = RebrandKoinTheme.colors.neutral0,
        placeholderContentColor: Color = RebrandKoinTheme.colors.neutral500,
        backgroundColor: Color = RebrandKoinTheme.colors.neutral100
    ): RecruitmentChatInputColors = RecruitmentChatInputColors(
        iconContentColor = iconContentColor,
        iconContainerColor = iconContainerColor,
        textContentColor = textContentColor,
        textContainerColor = textContainerColor,
        placeholderContentColor = placeholderContentColor,
        backgroundColor = backgroundColor
    )
}

@Immutable
data class RecruitmentChatInputColors(
    val iconContentColor: Color,
    val iconContainerColor: Color,
    val textContentColor: Color,
    val textContainerColor: Color,
    val placeholderContentColor: Color,
    val backgroundColor: Color
)

@Composable
fun RecruitmentChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onImageButtonClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = RecruitmentChatInputDefaults.windowInsets,
    colors: RecruitmentChatInputColors = RecruitmentChatInputDefaults.colors()
) {
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
                    .background(colors.iconContainerColor, RebrandKoinTheme.shapes.medium)
                    .noRippleClickable { onImageButtonClick() }
                    .padding(12.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_recruitment_chat_add_photo),
                tint = colors.iconContentColor,
                contentDescription = stringResource(id = R.string.recruitment_chat_add_image)
            )

            RecruitmentChatTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = stringResource(id = R.string.recruitment_chat_input_placeholder),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(
                        color = colors.textContainerColor,
                        shape = RebrandKoinTheme.shapes.medium
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .fillMaxHeight()
                    .weight(1f),
                colors = colors
            )

            Icon(
                modifier = Modifier
                    .background(colors.iconContainerColor, RebrandKoinTheme.shapes.medium)
                    .noRippleClickable { onSendClick() }
                    .padding(12.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_recruitment_chat_send),
                tint = colors.iconContentColor,
                contentDescription = stringResource(id = R.string.recruitment_chat_send)
            )
        }
    }
}

@Composable
private fun RecruitmentChatTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: RecruitmentChatInputColors = RecruitmentChatInputDefaults.colors()
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        textStyle = RebrandKoinTheme.typography.regular12.copy(color = colors.textContentColor),
        onValueChange = { onValueChange(it.take(RECRUITMENT_CHAT_MESSAGE_MAX_LENGTH)) },
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = RebrandKoinTheme.typography.regular12,
                        color = colors.placeholderContentColor
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentChatInputPreview() {
    KoinSurface {
        RecruitmentChatInput(
            value = "",
            onValueChange = {},
            onImageButtonClick = {},
            onSendClick = {}
        )
    }
}
