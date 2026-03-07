package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.model.CallvanLocationOption

@Composable
fun CallvanLocationSection(
    departureLocation: CallvanLocationOption?,
    arrivalLocation: CallvanLocationOption?,
    departureCustomText: String?,
    arrivalCustomText: String?,
    onDepartureClick: () -> Unit,
    onArrivalClick: () -> Unit,
    onSwap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        CallvanLocationItem(
            label = stringResource(R.string.callvan_create_departure_label),
            location = departureLocation,
            placeholder = stringResource(R.string.callvan_create_departure_placeholder),
            modifier = Modifier.width(124.dp),
            customDisplayName = if (departureLocation == CallvanLocationOption.OTHER) departureCustomText else null,
            onClick = onDepartureClick
        )
        CallvanSwapButton(onClick = onSwap)
        CallvanLocationItem(
            label = stringResource(R.string.callvan_create_arrival_label),
            location = arrivalLocation,
            placeholder = stringResource(R.string.callvan_create_arrival_placeholder),
            modifier = Modifier.width(124.dp),
            customDisplayName = if (arrivalLocation == CallvanLocationOption.OTHER) arrivalCustomText else null,
            onClick = onArrivalClick
        )
    }
}

@Composable
private fun CallvanLocationItem(
    label: String,
    location: CallvanLocationOption?,
    placeholder: String,
    customDisplayName: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.height(65.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = KoinTheme.typography.medium16,
            color = RebrandKoinTheme.colors.primary500
        )
        if (location != null) {
            val displayText = customDisplayName?.takeIf { it.isNotBlank() } ?: location.displayName
            Text(
                text = displayText,
                style = KoinTheme.typography.medium18,
                color = KoinTheme.colors.neutral800,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(KoinTheme.colors.neutral100, RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick)
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = placeholder,
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral500
                )
            }
        }
    }
}

@Composable
private fun CallvanSwapButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(31.dp)
            .clip(CircleShape)
            .border(0.8.dp, KoinTheme.colors.neutral300, CircleShape)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_tabler_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .size(13.dp)
                .offset(x = 12.2.dp, y = 6.dp),
            tint = RebrandKoinTheme.colors.primary500
        )
        Icon(
            painter = painterResource(R.drawable.ic_tabler_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .size(13.dp)
                .offset(x = 5.6.dp, y = 11.8.dp)
                .scale(scaleX = -1f, scaleY = 1f),
            tint = RebrandKoinTheme.colors.primary500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanLocationSectionPreview() {
    CallvanLocationSection(
        departureLocation = null,
        arrivalLocation = null,
        departureCustomText = null,
        arrivalCustomText = null,
        onDepartureClick = {},
        onArrivalClick = {},
        onSwap = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanLocationSectionFilledPreview() {
    CallvanLocationSection(
        departureLocation = CallvanLocationOption.FRONT_GATE,
        arrivalLocation = CallvanLocationOption.CHEONAN_TERMINAL,
        departureCustomText = null,
        arrivalCustomText = null,
        onDepartureClick = {},
        onArrivalClick = {},
        onSwap = {}
    )
}
