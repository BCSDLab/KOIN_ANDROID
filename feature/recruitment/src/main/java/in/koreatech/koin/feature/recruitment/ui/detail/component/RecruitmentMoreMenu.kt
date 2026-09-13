package `in`.koreatech.koin.feature.recruitment.ui.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun RecruitmentMoreMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    topPadding: Dp = 24.dp
) {
    if (!expanded) return

    val density = LocalDensity.current

    Popup(
        alignment = Alignment.TopEnd,
        offset = IntOffset(0, with(density) { topPadding.roundToPx() }),
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Column(
            modifier = modifier
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(RebrandKoinTheme.colors.neutral50)
                .width(IntrinsicSize.Max)
        ) {
            RecruitmentMoreMenuItem(
                text = stringResource(R.string.recruitment_more_edit),
                color = RebrandKoinTheme.colors.neutral800,
                onClick = onEditClick
            )
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
            RecruitmentMoreMenuItem(
                text = stringResource(R.string.recruitment_more_delete),
                color = RebrandKoinTheme.colors.danger700,
                onClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun RecruitmentMoreMenuItem(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(start = 12.dp, end = 43.dp, top = 8.dp, bottom = 8.dp),
        text = text,
        style = RebrandKoinTheme.typography.regular12.copy(color = color)
    )
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentMoreMenuPreview() {
    RebrandKoinTheme {
        RecruitmentMoreMenu(
            expanded = true,
            onDismissRequest = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
