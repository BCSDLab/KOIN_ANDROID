package `in`.koreatech.business.feature.store

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.LinedAlertTextField
import `in`.koreatech.business.feature.textfield.LinedWhiteTextField
import `in`.koreatech.business.ui.theme.ColorCardBackground
import `in`.koreatech.business.ui.theme.ColorOnCardBackground
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorTextDescription
import `in`.koreatech.business.ui.theme.ColorTextFieldDescription
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalGlideComposeApi::class)
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

    val context = LocalContext.current
    val pickImageLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts
                .PickMultipleVisualMedia(WriteEventViewModel.MAX_IMAGE_LENGTH)
        ) {
            if(it.isNotEmpty()) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    viewModel.onImagesChanged(it)
                else {
                    viewModel.onImagesChanged((state.images + it).distinct())
                }
            }
        }

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
            CountLimitText(text = "${state.images.size}/${WriteEventViewModel.MAX_IMAGE_LENGTH}",
                inputTextLength = state.images.size, limit = WriteEventViewModel.MAX_IMAGE_LENGTH)
        }
        if(state.images.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(ColorCardBackground)
                    .padding(top = 5.dp)
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                state.images.forEachIndexed { i, uri ->
                    if(i != 0)
                        Spacer(modifier = Modifier.width(11.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        GlideImage(
                            model = uri,
                            contentDescription = stringResource(id = R.string.register_image),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 4.dp, end = 4.dp),
                            contentScale = ContentScale.FillBounds
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(id = R.string.delete),
                            modifier = Modifier.clickable {
                                viewModel.deleteImage(uri)
                            }
                        )
                    }
                }
                for (i in 0 until WriteEventViewModel.MAX_IMAGE_LENGTH - state.images.size) {
                    Spacer(modifier = Modifier.width(11.dp))
                    Spacer(modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            Spacer(modifier = Modifier.height(3.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                        true
                    else {
                        state.images.size < WriteEventViewModel.MAX_IMAGE_LENGTH
                    }
                ) {
                    pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                .padding(top = 5.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(ColorCardBackground),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(R.drawable.ic_add_image),
                contentDescription = stringResource(id = R.string.register_image),
                modifier = Modifier.size(20.dp),
                colorFilter = if(state.images.size == WriteEventViewModel.MAX_IMAGE_LENGTH) ColorFilter.tint(ColorTextFieldDescription) else null
            )
            Text(
                text = stringResource(id = R.string.register_image),
                modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
                fontWeight = FontWeight.Bold,
                color = if(state.images.size == WriteEventViewModel.MAX_IMAGE_LENGTH) ColorTextFieldDescription else ColorOnCardBackground
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
            CountLimitText(text = "${state.title.length}/${WriteEventViewModel.MAX_TITLE_LENGTH}",
                inputTextLength = state.title.length, limit = WriteEventViewModel.MAX_TITLE_LENGTH)
        }
        LinedAlertTextField(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            value = state.title,
            onValueChange = { viewModel.onTitleChanged(it) },
            label = stringResource(id = R.string.input_event_title_instruction),
            showAlert = state.showTitleInputAlert,
            alertMessage = stringResource(R.string.text_should_inputted)
        )

        Row(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.event_content)
            )
            Spacer(modifier = Modifier.weight(1f))
            CountLimitText(text = "${state.content.length}/${WriteEventViewModel.MAX_CONTENT_LENGTH}",
                inputTextLength = state.content.length, limit = WriteEventViewModel.MAX_CONTENT_LENGTH)
        }
        LinedAlertTextField(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            value = state.content,
            onValueChange = { viewModel.onContentChanged(it) },
            label = stringResource(id = R.string.input_event_content_instruction),
            showAlert = state.showContentInputAlert,
            alertMessage = stringResource(R.string.text_should_inputted)
        )

        Text(
            modifier = Modifier.padding(top = 17.dp),
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
                dayFocusRequester = startDayFR,
                showDateInputAlert = state.showDateInputAlert
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
                dayFocusRequester = endDayFR,
                showDateInputAlert = state.showDateInputAlert
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
        ) {
            Image(
                colorFilter = ColorFilter.tint(ColorSecondary),
                painter = painterResource(R.drawable.exclamation),
                contentDescription = "Alert",
                alpha = if (state.showDateInputAlert) 1f else 0f
            )
            Text(
                modifier = Modifier.padding(start = 1.5.dp),
                text = if (state.showDateInputAlert) stringResource(id = R.string.text_should_inputted) else "",
                color = ColorSecondary,
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
            WriteEventSideEffect.ToastImageLimit -> {
                ToastUtil.getInstance().makeShort(context.getString(R.string.image_limit, WriteEventViewModel.MAX_IMAGE_LENGTH))
            }
        }
    }
}

@Composable
private fun CountLimitText(
    text: String,
    inputTextLength: Int,
    limit: Int
) {
    Text(
        color = if(inputTextLength == limit) ColorSecondary else ColorTextDescription,
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
    dayFocusRequester: FocusRequester = FocusRequester(),
    showDateInputAlert: Boolean = false
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            showAlert = showDateInputAlert
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            showAlert = showDateInputAlert
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            showAlert = showDateInputAlert
        )
    }
}