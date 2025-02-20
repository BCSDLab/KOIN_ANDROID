package `in`.koreatech.business.feature.event.writeevent.writeevent

import CalendarBottomDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.ui.theme.Black1
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorTextDescription
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.koin.core.R
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun WriteEventScreen(
    goToMyStoreScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    viewModel: WriteEventViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        WriteEventScreenImpl(
            onBackPressed = onBackPressed,
            writeEventState = state,
            onChangeTitle = viewModel::onTitleChanged,
            onChangeContent = viewModel::onContentChanged,
            onRegisterImage = viewModel::registerEventImageUri,
            onDeleteImage = viewModel::deleteImage,
            onStartYearChanged = viewModel::onStartYearChanged,
            onStartMonthChanged = viewModel::onStartMonthChanged,
            onStartDayChanged = viewModel::onStartDayChanged,
            onEndYearChanged = viewModel::onEndYearChanged,
            onEndMonthChanged = viewModel::onEndMonthChanged,
            onEndDayChanged = viewModel::onEndDayChanged,
        )

        if (state.showCalendarAlert) {
            CalendarBottomDialog(
                viewModel = viewModel,
                showDialog = state.showCalendarAlert,
                onDismiss = { viewModel.onCalendarVisibilityChanged(false) },
                onDateSelected = { },
            )
        }
    }

}

@Composable
fun WriteEventScreenImpl(
    onBackPressed: () -> Unit = {},
    writeEventState: WriteEventState = WriteEventState(),
    onChangeTitle: (String) -> Unit = {},
    onChangeContent: (String) -> Unit = {},
    onRegisterImage: (Uri) -> Unit = {},
    onDeleteImage: (Int) -> Unit = {},
    onStartYearChanged:(String) -> Unit = {},
    onStartMonthChanged:(String) -> Unit = {},
    onStartDayChanged:(String) -> Unit = {},
    onEndYearChanged:(String) -> Unit = {},
    onEndMonthChanged:(String) -> Unit = {},
    onEndDayChanged:(String) -> Unit = {},
    onNextButtonClicked: () -> Unit = {}
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onRegisterImage(uri)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(ColorPrimary),
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                onClick = { onBackPressed() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_white_arrow_back),
                    contentDescription = stringResource(R.string.back),
                )
            }
            Text(
                text = stringResource(R.string.event_write),
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(color = Color.White, fontSize = 20.sp),
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                    text = stringResource(id = R.string.menu_name),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Text(
                        modifier = Modifier.padding(start = 24.dp),
                        text = stringResource(id = R.string.event_upload_image_instruction),
                        fontSize = 12.sp,
                        color = Gray6
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CountLimitText(
                        text = "${writeEventState.images.size}/${WriteEventViewModel.MAX_IMAGE_LENGTH}",
                        inputTextLength = writeEventState.images.size,
                        limit = WriteEventViewModel.MAX_IMAGE_LENGTH
                    )
                }

                if(writeEventState.images.isNotEmpty()){
                    EventImageView(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 5.dp)
                            .fillMaxWidth()
                            .height(112.dp)
                        ,
                        imageList = writeEventState.images,
                        onDeleteImage = onDeleteImage
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .clickable(
                            enabled = writeEventState.images.size < WriteEventViewModel.MAX_IMAGE_LENGTH
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                        .clip(RoundedCornerShape(5.dp))
                        .background(ColorTextField),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_add_image),
                        contentDescription = stringResource(id = R.string.register_image),
                        modifier = Modifier.size(20.dp),
                        colorFilter = if (writeEventState.images.size == WriteEventViewModel.MAX_IMAGE_LENGTH) ColorFilter.tint(
                            Gray6
                        ) else null
                    )
                    Text(
                        text = stringResource(id = R.string.register_image),
                        modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
                        fontWeight = FontWeight.Bold,
                        color = if (writeEventState.images.size == WriteEventViewModel.MAX_IMAGE_LENGTH) Gray6 else Gray1
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 22.dp)
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = stringResource(id = R.string.title),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CountLimitText(
                        text = "${writeEventState.title.length}/${WriteEventViewModel.MAX_TITLE_LENGTH}",
                        inputTextLength = writeEventState.title.length,
                        limit = WriteEventViewModel.MAX_TITLE_LENGTH
                    )
                }

                BorderTextField(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 5.dp),
                    textFieldModifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    inputString = writeEventState.title,
                    onStringChange = onChangeTitle,
                    hintString = stringResource(id = R.string.event_input_title_instruction)
                )

            }

            item {
                Row(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 22.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(186.dp)
                )
                {
                    Text(
                        text = stringResource(id = R.string.event_content),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CountLimitText(
                        text = "${writeEventState.content.length}/${WriteEventViewModel.MAX_CONTENT_LENGTH}",
                        inputTextLength = writeEventState.content.length,
                        limit = WriteEventViewModel.MAX_CONTENT_LENGTH
                    )
                }
                BorderTextField(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 5.dp)
                        .height(123.dp),
                    textFieldModifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    inputString = writeEventState.content,
                    onStringChange = onChangeContent,
                    hintString = stringResource(id = R.string.event_input_content_instruction)
                )
            }

            item {
                Text(
                    modifier = Modifier.padding(start = 24.dp, top = 22.dp),
                    text = stringResource(id = R.string.event_period),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                DateInputRow(
                    modifier = Modifier
                        .height(40.dp),
                    optionString = stringResource(id = R.string.start_date),
                    date = writeEventState.startDate,
                )

                DateInputRow(
                    modifier = Modifier
                        .height(40.dp),
                    optionString = stringResource(id = R.string.end_date),
                    date = writeEventState.endDate,
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 200.dp, bottom = 20.dp)
                        .fillMaxWidth()
                        .height(43.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onBackPressed() },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(ColorTextField),
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(120.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_do_cancellation),
                            fontSize = 15.sp,
                            color = Gray1
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_cancel),
                            contentDescription = stringResource(id = R.string.common_do_cancellation)
                        )
                    }

                    Button(
                        onClick = onNextButtonClicked,
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(ColorPrimary),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(id = R.string.register),
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_register),
                            contentDescription = stringResource(id = R.string.register)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BorderTextField(
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    inputString: String = "",
    onStringChange: (String) -> Unit = {},
    hintString: String = "",
    textAlign: TextAlign = TextAlign.Unspecified,
    contentAlignment : Alignment = Alignment.TopStart,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Box(
        modifier = modifier
            .border(width = 1.dp, color = ColorMinor, shape = RoundedCornerShape(4.dp))
        ,
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = inputString,
            onValueChange = onStringChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = textAlign
            ),
            keyboardOptions = keyboardOptions,
            modifier = textFieldModifier,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = contentAlignment
                ) {
                    if (inputString.isEmpty()) {
                        Text(
                            text = hintString,
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = textAlign
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun EventImageView(
    modifier: Modifier = Modifier,
    imageList: List<Uri> = emptyList(),
    onDeleteImage: (Int) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorTextField),
    ) {
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 8.dp)
        ) {
            itemsIndexed(imageList) { index, item ->
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .padding(end = 10.dp)
                        .padding(bottom = 8.dp)
                    ,
                    contentAlignment = Alignment.TopEnd
                )
                {
                    Image(
                        modifier = Modifier
                            .size(96.dp),
                        painter = rememberAsyncImagePainter(
                            item
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        modifier = Modifier
                            .size(16.dp)
                            .offset(x = 7.dp, y = (-7).dp)
                            .clickable {
                                onDeleteImage(index)
                            },
                        painter = painterResource(id = R.drawable.ic_delete_button),
                        contentDescription = ""
                    )
                }
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
        modifier = Modifier.padding(end = 24.dp),
        fontSize = 12.sp,
        text = text
    )
}

@Composable
private fun DateInputRow(
    modifier: Modifier = Modifier,
    viewModel: WriteEventViewModel = hiltViewModel(),
    optionString: String = "시작일",
    date: String = "",
    ) {
    Row(
        modifier = Modifier
            .padding(top = 11.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(ColorTextField)
        ) {
            Text(
                text = optionString,
                fontWeight = FontWeight.Bold,
                color = Black1,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.5.dp))

        Box(
            modifier = Modifier.wrapContentWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date,
                maxLines = 1,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = ColorPrimary400,
            )
        }
        IconButton(
            onClick = { viewModel.onCalendarVisibilityChanged(true) },
            modifier = Modifier
                .padding(start = 8.dp)
                .size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = stringResource(R.string.calendar) ,
                tint = ColorPrimary400,
            )
        }
    }
}

@Preview
@Composable
fun PreviewWriteEventScreen() {
    WriteEventScreenImpl()
}

@Preview
@Composable
fun PreviewImageview() {
    DateInputRow()
}