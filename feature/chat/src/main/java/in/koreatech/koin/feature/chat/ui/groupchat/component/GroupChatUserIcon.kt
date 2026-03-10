@file:Suppress("MatchingDeclarationName")

package `in`.koreatech.koin.feature.chat.ui.groupchat.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R

object GroupUserIconColors {
    private val Color0 = Color(0xFFDDB1FE)
    private val Color1 = Color(0xFFD39AFE)
    private val Color2 = Color(0xFFCE86FD)
    private val Color3 = Color(0xFFC969FC)
    private val Color4 = Color(0xFFC358FC)
    private val Color5 = Color(0xFFB611F5)
    private val Color6 = Color(0xFF980AC9)
    private val Color7 = Color(0xFF7D08A4)

    private val Image1 = R.drawable.ic_chat_group_profile_1
    private val Image2 = R.drawable.ic_chat_group_profile_2
    private val Image3 = R.drawable.ic_chat_group_profile_3
    private val Image4 = R.drawable.ic_chat_group_profile_4
    private val Image5 = R.drawable.ic_chat_group_profile_5
    private val Image6 = R.drawable.ic_chat_group_profile_6
    private val Image7 = R.drawable.ic_chat_group_profile_7
    private val Image8 = R.drawable.ic_chat_group_profile_8

    private val colors = listOf(Color0, Color1, Color2, Color3, Color4, Color5, Color6, Color7)
    private val images = listOf(Image1, Image2, Image3, Image4, Image5, Image6, Image7, Image8)

    fun getColorByIndex(index: Int): Color {
        return colors.getOrElse(index) { Color0 }
    }

    fun getImageByIndex(index: Int): Int {
        return images.getOrElse(index) { Image1 }
    }
}

@Composable
fun GroupChatUserIcon(
    colorIndex: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(RebrandKoinTheme.shapes.small)
            .border(width = 1.dp, color = GroupUserIconColors.getColorByIndex(colorIndex), shape = RebrandKoinTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = GroupUserIconColors.getImageByIndex(colorIndex)),
            contentDescription = stringResource(id = R.string.chat_user_profile_image),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupChatUserIconPreview() {
    KoinSurface {
        Row(
            modifier = Modifier.background(KoinTheme.colors.neutral0)
        ) {
            repeat(8) { index ->
                GroupChatUserIcon(
                    colorIndex = index
                )
            }
        }
    }
}
