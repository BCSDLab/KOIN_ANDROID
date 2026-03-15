package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.model.CallvanLocationOption
import `in`.koreatech.koin.feature.callvan.ui.displayNameRes

@Composable
fun CallvanLocationSection(
    departureLocation: CallvanLocationOption?,
    arrivalLocation: CallvanLocationOption?,
    departureCustomText: String?,
    arrivalCustomText: String?,
    modifier: Modifier = Modifier,
    onDepartureClick: () -> Unit = {},
    onArrivalClick: () -> Unit = {},
    onSwap: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        CallvanLocationItem(
            label = stringResource(R.string.callvan_create_departure_label),
            location = departureLocation,
            placeholder = stringResource(R.string.callvan_create_departure_placeholder),
            modifier = Modifier
                .weight(1f)
                .padding(end = 31.5.dp),
            customDisplayName = if (departureLocation == CallvanLocationOption.CUSTOM) departureCustomText else null,
            onClick = onDepartureClick
        )
        CallvanSwapButton(onClick = onSwap)
        CallvanLocationItem(
            label = stringResource(R.string.callvan_create_arrival_label),
            location = arrivalLocation,
            placeholder = stringResource(R.string.callvan_create_arrival_placeholder),
            modifier = Modifier
                .weight(1f)
                .padding(start = 31.5.dp),
            customDisplayName = if (arrivalLocation == CallvanLocationOption.CUSTOM) arrivalCustomText else null,
            onClick = onArrivalClick
        )
    }
}

@Composable
private fun CallvanLocationItem(
    label: String,
    location: CallvanLocationOption?,
    placeholder: String,
    modifier: Modifier = Modifier,
    customDisplayName: String? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = RebrandKoinTheme.typography.medium16,
            color = RebrandKoinTheme.colors.primary500
        )
        if (location != null) {
            val displayText = customDisplayName?.takeIf { it.isNotBlank() } ?: stringResource(location.displayNameRes())
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClickLabel = placeholder,
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    style = RebrandKoinTheme.typography.medium18,
                    color = RebrandKoinTheme.colors.neutral800,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RebrandKoinTheme.colors.neutral100, RoundedCornerShape(8.dp))
                    .clickable(
                        onClickLabel = placeholder,
                        onClick = onClick
                    )
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = placeholder,
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
        }
    }
}

@Composable
private fun CallvanSwapButton(onClick: () -> Unit) {
    val swapButtonDescription = stringResource(R.string.callvan_create_swap_button)

    Box(
        modifier = Modifier
            .size(31.dp)
            .clip(CircleShape)
            .border(0.8.dp, RebrandKoinTheme.colors.neutral300, CircleShape)
            .clickable(
                onClickLabel = swapButtonDescription,
                onClick = onClick
            )
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_tabler_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .size(13.dp)
                .offset(x = 6.dp, y = 12.dp),
            tint = RebrandKoinTheme.colors.primary500
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_tabler_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .size(13.dp)
                .offset(x = 12.dp, y = 6.dp)
                .scale(scaleX = -1f, scaleY = 1f),
            tint = RebrandKoinTheme.colors.primary500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanLocationSectionPreview() {
    RebrandKoinTheme {
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
}

@Preview(showBackground = true)
@Composable
private fun CallvanLocationSectionFilledPreview() {
    RebrandKoinTheme {
        CallvanLocationSection(
            departureLocation = CallvanLocationOption.FRONT_GATE,
            arrivalLocation = CallvanLocationOption.TERMINAL,
            departureCustomText = null,
            arrivalCustomText = null,
            onDepartureClick = {},
            onArrivalClick = {},
            onSwap = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanLocationSectionCustomPreview() {
    RebrandKoinTheme {
        CallvanLocationSection(
            departureLocation = CallvanLocationOption.CUSTOM,
            arrivalLocation = CallvanLocationOption.CUSTOM,
            departureCustomText = "출발지",
            arrivalCustomText = "testtesttesttest",
            onDepartureClick = {},
            onArrivalClick = {},
            onSwap = {}
        )
    }
}
