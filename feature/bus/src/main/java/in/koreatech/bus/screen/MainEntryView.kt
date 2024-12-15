package `in`.koreatech.bus.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R

@Composable
fun MainEntryView(
    onShuttleTicketClicked: () -> Unit,
    onTimetableCardClicked: () -> Unit,
    onSearchCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val unibusInteractionSource = remember { MutableInteractionSource() }
    val timetableInteractionSource = remember { MutableInteractionSource() }
    val searchInteractionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.bus),
                style = KoinTheme.typography.bold18,
                color = KoinTheme.colors.primary500
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .clickable(
                        interactionSource = unibusInteractionSource,
                        indication = ripple(bounded = false)
                    ) {
                        onShuttleTicketClicked()
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_qr),
                    contentDescription = stringResource(R.string.unibus_shortcut_content_description),
                    tint = KoinTheme.colors.primary500
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(R.string.shuttle_ticket),
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.primary500
                )
            }
        }

        Row {
            MainEntryCard(
                title = stringResource(R.string.main_title_bus_timetable),
                description = stringResource(R.string.shortcut),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = KoinTheme.colors.neutral50)
                    .clickable(
                        interactionSource = timetableInteractionSource,
                        indication = ripple(bounded = false)
                    ) {
                        onTimetableCardClicked()
                    }.padding(horizontal = 16.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            MainEntryCard(
                title = stringResource(R.string.main_title_bus_search),
                description = stringResource(R.string.see),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = KoinTheme.colors.neutral50)
                    .clickable(
                        interactionSource = searchInteractionSource,
                        indication = ripple(bounded = false)
                    ) {
                        onSearchCardClicked()
                    }.padding(horizontal = 16.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun MainEntryCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Text(
                text = title,
                style = KoinTheme.typography.medium14
            )
            Text(
                text = description,
                style = KoinTheme.typography.regular12
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = title
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun MainEntryViewPreview(){
    MainEntryView(
        onShuttleTicketClicked = {},
        onTimetableCardClicked = {},
        onSearchCardClicked = {},
        modifier = Modifier.fillMaxWidth()
    )
}