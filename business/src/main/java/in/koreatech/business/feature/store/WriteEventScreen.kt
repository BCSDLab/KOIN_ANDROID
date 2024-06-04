package `in`.koreatech.business.feature.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.LinedWhiteTextField
import `in`.koreatech.business.ui.theme.ColorCardBackground
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextDescription
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Preview
@Composable
fun WriteEventScreen(
    modifier: Modifier = Modifier,
    viewModel: WriteEventViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val scrollState = rememberScrollState()
    val startYearFR = remember { FocusRequester() }
    val startMonthFR = remember { FocusRequester() }
    val startDayFR = remember { FocusRequester() }
    val endYearFR = remember { FocusRequester() }
    val endMonthFR = remember { FocusRequester() }
    val endDayFR = remember { FocusRequester() }

    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.image)
        )
        Row {
            Text(
                text = stringResource(id = R.string.upload_event_image_instruction),
                color = ColorTextDescription
            )
            Spacer(modifier = Modifier.weight(1f))
            CountLimitText(text = "0/3")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(color = ColorCardBackground),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(R.drawable.ic_add_image),
                contentDescription = stringResource(id = R.string.register_image),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(id = R.string.register_image),
                modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.padding(top = 22.dp)
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.title)
            )
            Spacer(modifier = Modifier.weight(1f))
            CountLimitText(text = "0/25")
        }
        LinedWhiteTextField(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            value = state.title,
            onValueChange = { viewModel.onTitleChanged(it) },
            label = stringResource(id = R.string.input_event_title_instruction)
        )

        Row(
            modifier = Modifier.padding(top = 19.dp)
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.event_content)
            )
            Spacer(modifier = Modifier.weight(1f))
            CountLimitText(text = "0/500")
        }
        LinedWhiteTextField(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            value = state.content,
            onValueChange = { viewModel.onContentChanged(it) },
            label = stringResource(id = R.string.input_event_content_instruction)
        )

        Text(
            modifier = Modifier.padding(top = 24.dp),
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.event_period)
        )

        Row(
            modifier = Modifier
                .padding(top = 11.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(ColorCardBackground)
            ) {
                Text(
                    text = stringResource(id = R.string.start_date),
                    fontWeight = FontWeight.Bold,
                    color = ColorTextDescription,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            DateInputRow(
                modifier = Modifier.padding(start = 16.dp),
                year = state.startYear,
                month = state.startMonth,
                day = state.startDay,
                onYearChanged = { viewModel.onStartYearChanged(it) },
                onMonthChanged = { viewModel.onStartMonthChanged(it) },
                onDayChanged = { viewModel.onStartDayChanged(it) },
                yearFocusRequester = startYearFR,
                monthFocusRequester = startMonthFR,
                dayFocusRequester = startDayFR
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(ColorCardBackground)
            ) {
                Text(
                    text = stringResource(id = R.string.end_date),
                    fontWeight = FontWeight.Bold,
                    color = ColorTextDescription,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            DateInputRow(
                modifier = Modifier.padding(start = 16.dp),
                year = state.endYear,
                month = state.endMonth,
                day = state.endDay,
                onYearChanged = { viewModel.onEndYearChanged(it) },
                onMonthChanged = { viewModel.onEndMonthChanged(it) },
                onDayChanged = { viewModel.onEndDayChanged(it) },
                yearFocusRequester = endYearFR,
                monthFocusRequester = endMonthFR,
                dayFocusRequester = endDayFR
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 72.dp)
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(ColorCardBackground)
            ) {
                Image(
                    modifier = Modifier.padding(start = 21.dp, top = 14.dp, bottom = 14.dp),
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = stringResource(id = R.string.action_cancel)
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp, top = 14.dp, bottom = 14.dp, end = 21.dp),
                    text = stringResource(id = R.string.action_cancel),
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(5.dp))
                    .background(ColorPrimary)
                    .clickable {
                        viewModel.registerEvent()
                    },
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    modifier = Modifier.padding(start = 21.dp, top = 14.dp, bottom = 14.dp),
                    painter = painterResource(id = R.drawable.ic_register),
                    contentDescription = stringResource(id = R.string.action_register)
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp, top = 14.dp, bottom = 14.dp, end = 21.dp),
                    color = Color.White,
                    text = stringResource(id = R.string.action_register),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    viewModel.collectSideEffect {
        when(it) {
            WriteEventSideEffect.FocusStartYear -> {
                startYearFR.requestFocus()
            }
            WriteEventSideEffect.FocusStartMonth -> {
                startMonthFR.requestFocus()
            }
            WriteEventSideEffect.FocusStartDay -> {
                startDayFR.requestFocus()
            }
            WriteEventSideEffect.FocusEndYear -> {
                endYearFR.requestFocus()
            }
            WriteEventSideEffect.FocusEndMonth -> {
                endMonthFR.requestFocus()
            }
            WriteEventSideEffect.FocusEndDay -> {
                endDayFR.requestFocus()
            }
        }
    }
}

@Composable
private fun CountLimitText(
    text: String
) {
    Text(
        color = ColorTextDescription,
        modifier = Modifier.padding(end = 8.dp),
        text = text
    )
}

@Composable
private fun DateInputRow(
    modifier: Modifier = Modifier,
    year: String = "",
    month: String = "",
    day: String = "",
    onYearChanged: (String) -> Unit = {},
    onMonthChanged: (String) -> Unit = {},
    onDayChanged: (String) -> Unit = {},
    yearFocusRequester: FocusRequester = FocusRequester(),
    monthFocusRequester: FocusRequester = FocusRequester(),
    dayFocusRequester: FocusRequester = FocusRequester()
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinedWhiteTextField(
            value = year,
            onValueChange = onYearChanged,
            label = "0000",
            singleLine = true,
            modifier = Modifier.focusRequester(yearFocusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.size(12.5.dp))
        Text(text = "/", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(12.5.dp))
        LinedWhiteTextField(
            value = month,
            onValueChange = onMonthChanged,
            label = "00",
            singleLine = true,
            modifier = Modifier.focusRequester(monthFocusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.size(12.5.dp))
        Text(text = "/", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.size(12.5.dp))
        LinedWhiteTextField(
            value = day,
            onValueChange = onDayChanged,
            label = "00",
            singleLine = true,
            modifier = Modifier.focusRequester(dayFocusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}