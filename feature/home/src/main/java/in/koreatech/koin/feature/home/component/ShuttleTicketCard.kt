package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.home.R

@Composable
fun ShuttleTicketCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .widthIn(max = 150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFFFF))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(R.string.shuttle_ticket_title),
                style = RebrandKoinTheme.typography.bold13,
                color = Color(0xFF323232)
            )

            Text(
                text = stringResource(R.string.shuttle_ticket_description),
                style = RebrandKoinTheme.typography.bold12,
                color = Color(0xFF6F6F6F)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .size(40.dp)
                .padding(4.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_home_shuttle_ticket),
            contentDescription = null,
            tint = Color(0xFFB611F5)
        )
    }
}

@Preview
@Composable
private fun ShuttleTicketCardPreview() {
    ShuttleTicketCard()
}
