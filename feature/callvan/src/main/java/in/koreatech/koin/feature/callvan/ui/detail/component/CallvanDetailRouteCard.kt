package `in`.koreatech.koin.feature.callvan.ui.detail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanRouteInfo

@Composable
fun CallvanDetailRouteCard(
    departure: String,
    destination: String,
    dateTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, KoinTheme.colors.neutral200),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CallvanRouteInfo(
            modifier = Modifier.weight(1f),
            departure = departure,
            destination = destination
        )
        Text(
            text = dateTime,
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral600
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CallvanDetailRouteCardPreview() {
    CallvanDetailRouteCard(
        departure = "테니스장",
        destination = "천안 시외터미널",
        dateTime = "02.05 (월) 14:00"
    )
}
