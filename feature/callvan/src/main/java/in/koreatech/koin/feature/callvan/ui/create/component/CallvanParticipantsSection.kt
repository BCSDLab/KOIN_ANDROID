package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanParticipantsSection(
    count: Int,
    modifier: Modifier = Modifier,
    onDecrement: () -> Unit = {},
    onIncrement: () -> Unit = {}
) {
    val participantsText = stringResource(R.string.callvan_create_participants_count, count)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.callvan_create_participants_label),
                style = RebrandKoinTheme.typography.medium16,
                color = RebrandKoinTheme.colors.primary500
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(RebrandKoinTheme.typography.bold12.toSpanStyle()) {
                        append(stringResource(R.string.callvan_create_participants_hint_bold))
                    }
                    append(stringResource(R.string.callvan_create_participants_hint_normal))
                },
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .border(1.dp, RebrandKoinTheme.colors.neutral400, RoundedCornerShape(4.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = participantsText,
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral800,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = RebrandKoinTheme.colors.neutral400
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_remove),
                contentDescription = null,
                tint = RebrandKoinTheme.colors.primary500,
                modifier = Modifier
                    .clickable(onClick = onDecrement)
                    .padding(horizontal = 8.dp, vertical = 7.dp)
                    .size(24.dp)
            )
            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = RebrandKoinTheme.colors.neutral400)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral800
                )
            }
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = RebrandKoinTheme.colors.primary500,
                modifier = Modifier
                    .clickable(onClick = onIncrement)
                    .padding(horizontal = 8.dp, vertical = 7.dp)
                    .size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanParticipantsSectionPreview() {
    RebrandKoinTheme {
        CallvanParticipantsSection(
            count = 4,
            onDecrement = {},
            onIncrement = {}
        )
    }
}
