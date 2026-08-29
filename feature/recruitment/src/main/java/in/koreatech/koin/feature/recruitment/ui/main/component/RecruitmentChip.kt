package `in`.koreatech.koin.feature.recruitment.ui.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory

@Immutable
data class RecruitmentChipColors(
    val containerColor: Color,
    val contentColor: Color
)

object RecruitmentChipDefaults {
    val Shape: Shape = RoundedCornerShape(100.dp)
    val PillShape: Shape = RoundedCornerShape(40.dp)
    val PillHeight = 36.dp
    val ContentPadding = PaddingValues(horizontal = 8.dp, vertical = 1.dp)
    val IconSize = 14.dp

    @Composable
    fun colors(
        containerColor: Color = RebrandKoinTheme.colors.neutral200,
        contentColor: Color = RebrandKoinTheme.colors.neutral500
    ): RecruitmentChipColors = RecruitmentChipColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun categoryColors(category: RecruitmentCategory): RecruitmentChipColors {
        val palette = RebrandKoinTheme.colors
        return when (category) {
            RecruitmentCategory.CONTEST -> colors(palette.info200, palette.info700)
            RecruitmentCategory.EXTERNAL_ACTIVITY -> colors(palette.success200, palette.success700)
            RecruitmentCategory.STUDY -> colors(palette.primary100, palette.primary600)
            RecruitmentCategory.PROJECT -> colors(palette.warning100, palette.warning700)
            RecruitmentCategory.ETC -> colors(palette.neutral200, palette.neutral600)
        }
    }

    @Composable
    fun selectableColors(isSelected: Boolean): RecruitmentChipColors = colors(
        containerColor = RebrandKoinTheme.colors.neutral0,
        contentColor = if (isSelected) {
            RebrandKoinTheme.colors.primary500
        } else {
            RebrandKoinTheme.colors.neutral500
        }
    )
}

@Suppress("LongParameterList")
@Composable
fun RecruitmentChip(
    text: String,
    modifier: Modifier = Modifier,
    colors: RecruitmentChipColors = RecruitmentChipDefaults.colors(),
    textStyle: TextStyle = RebrandKoinTheme.typography.regular10,
    shape: Shape = RecruitmentChipDefaults.Shape,
    contentPadding: PaddingValues = RecruitmentChipDefaults.ContentPadding,
    border: BorderStroke? = null,
    trailingIcon: ImageVector? = null,
    trailingIconSize: Dp = RecruitmentChipDefaults.IconSize,
    trailingIconTint: Color = colors.contentColor,
    trailingIconContentDescription: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    showClickRipple: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .clip(shape)
            .background(colors.containerColor)
            .then(if (border == null) Modifier else Modifier.border(border, shape))
            .then(
                when {
                    onClick == null -> Modifier
                    showClickRipple -> Modifier.clickable(onClick = onClick)
                    else -> Modifier.noRippleClickable(onClick = onClick)
                }
            )
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = textStyle,
            color = colors.contentColor
        )
        if (trailingIcon != null) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = trailingIconContentDescription,
                tint = trailingIconTint,
                modifier = Modifier
                    .size(trailingIconSize)
                    .then(
                        if (onTrailingIconClick == null) {
                            Modifier
                        } else {
                            Modifier.noRippleClickable(onClick = onTrailingIconClick)
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
private fun RecruitmentChipPreview() {
    RebrandKoinTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RecruitmentChip(text = "프론트엔드 1명")
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                RecruitmentCategory.ALL.forEach { category ->
                    RecruitmentChip(
                        text = stringResource(category.labelRes),
                        colors = RecruitmentChipDefaults.categoryColors(category),
                        textStyle = RebrandKoinTheme.typography.regular10.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
