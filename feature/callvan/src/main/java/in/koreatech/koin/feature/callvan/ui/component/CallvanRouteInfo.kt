package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanRouteInfo(
    departure: String,
    destination: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_callvan_line),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.callvan_detail_departure, departure),
                    style = KoinTheme.typography.medium14,
                    color = KoinTheme.colors.neutral800
                )
                Text(
                    text = stringResource(R.string.callvan_detail_destination, destination),
                    style = KoinTheme.typography.medium14,
                    color = KoinTheme.colors.neutral800
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanRouteInfoPreview() {
    CallvanRouteInfo(
        departure = "테니스장",
        destination = "천안 시외터미널"
    )
}
