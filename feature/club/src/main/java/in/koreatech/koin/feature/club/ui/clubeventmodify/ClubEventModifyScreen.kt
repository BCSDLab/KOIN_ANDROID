package `in`.koreatech.koin.feature.club.ui.clubeventmodify

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubBasicTextField
import `in`.koreatech.koin.feature.club.component.KoinClubDatePickerDialog
import `in`.koreatech.koin.feature.club.component.KoinClubDateSelectBox
import `in`.koreatech.koin.feature.club.component.KoinClubExtraSmallDialog
import `in`.koreatech.koin.feature.club.component.KoinClubExtraSmallDialogDanger
import `in`.koreatech.koin.feature.club.component.KoinClubTimePickerDialog
import `in`.koreatech.koin.feature.club.utils.getDayOfWeek
import `in`.koreatech.koin.feature.club.utils.pickMultipleMedia
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubEventModifyScreen(
    modifier: Modifier = Modifier,
    viewModel: ClubEventModifyViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onEventModified: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, context, onEventModified, onNavigateUp)
    }

    Scaffold(
        modifier = modifier.imePadding(),
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.club_event_create_title),
                onNavigationIconClick = { viewModel.updateCreateCancelDialog(true) }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { contentPadding ->
        ClubEventCreateScreenImpl(
            modifier = Modifier
                .padding(contentPadding),
            eventName = uiState.eventName,
            eventIntroduce = uiState.eventIntroduce,
            eventContent = uiState.eventContent,
            eventStartDateTime = uiState.eventStartDateTime,
            eventEndDateTime = uiState.eventEndDateTime,
            imageUrls = uiState.eventImageUrls,
            uploadImage = viewModel::getPreSignedUrl,
            onImageDeleteClick = viewModel::deleteImageUrl,
            showModifyCancelDialogState = uiState.showModifyCancelDialog,
            showModifyRequestDialogState = uiState.showModifyRequestDialog,
            showDatePickerDialogState = uiState.showDatePickerDialog,
            showTimePickerDialogState = uiState.showTimePickerDialog,
            updateModifyCancelDialog = viewModel::updateCreateCancelDialog,
            updateModifyRequestDialog = viewModel::updateCreateRequestDialog,
            updateDatePickerDialog = viewModel::updateDatePickerDialog,
            updateTimePickerDialog = viewModel::updateTimePickerDialog,
            updateEventName = viewModel::updateEventName,
            updateEventIntroduce = viewModel::updateEventIntroduce,
            updateEventContent = viewModel::updateEventContent,
            setEventStartDate = viewModel::setEventStartDate,
            setEventEndDate = viewModel::setEventEndDate,
            setEventStartTime = viewModel::setEventStartTime,
            setEventEndTime = viewModel::setEventEndTime,
            modifyEvent = viewModel::modifyClubEvent,
            modifyEventCancel = viewModel::postNavigateUp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubEventCreateScreenImpl(
    eventName: String,
    eventIntroduce: String,
    eventContent: String,
    eventStartDateTime: LocalDateTime,
    eventEndDateTime: LocalDateTime,
    modifier: Modifier = Modifier,
    imageUrls: List<String> = persistentListOf(),
    showModifyCancelDialogState: Boolean = false,
    showModifyRequestDialogState: Boolean = false,
    showDatePickerDialogState: Boolean = false,
    showTimePickerDialogState: Boolean = false,
    updateModifyCancelDialog: (Boolean) -> Unit = {},
    updateModifyRequestDialog: (Boolean) -> Unit = {},
    updateDatePickerDialog: (Boolean) -> Unit = {},
    updateTimePickerDialog: (Boolean) -> Unit = {},
    updateEventName: (String) -> Unit = {},
    updateEventIntroduce: (String) -> Unit = {},
    updateEventContent: (String) -> Unit = {},
    setEventStartDate: (LocalDate) -> Unit = {},
    setEventEndDate: (LocalDate) -> Unit = {},
    setEventStartTime: (LocalTime) -> Unit = {},
    setEventEndTime: (LocalTime) -> Unit = {},
    modifyEvent: () -> Unit = {},
    modifyEventCancel: () -> Unit = {},
    uploadImage: (fileSize: Long, fileType: String, fileName: String, fileUri: Uri) -> Unit = { _, _, _, _ -> },
    onImageDeleteClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    var isStartDateSelected by remember { mutableStateOf(true) }

    val textFieldMinLines = 1
    val textFieldMaxLength = 255
    val introTextFieldMaxLength = 70
    val contentTextFieldMaxLines = 2

    val maxImageItems = 44

    val pickMultipleMedia = pickMultipleMedia(
        context = context,
        maxItems = maxImageItems - imageUrls.size,
        onResult = uploadImage
    )

    val pagerState = rememberPagerState(pageCount = { imageUrls.size + 1 })

    if (showDatePickerDialogState) {
        KoinClubDatePickerDialog(
            defaultDate = if (isStartDateSelected) {
                eventStartDateTime.toLocalDate()
            } else {
                eventEndDateTime.toLocalDate()
            },
            onPositive = {
                if (isStartDateSelected) {
                    setEventStartDate(it)
                } else {
                    setEventEndDate(it)
                }
                updateDatePickerDialog(false)
            },
            onDismiss = { updateDatePickerDialog(false) },
            onNegative = { updateDatePickerDialog(false) }
        )
    }

    if (showTimePickerDialogState) {
        KoinClubTimePickerDialog(
            title = "시간을 선택해주세요.",
            isStartTime = isStartDateSelected,
            defaultTime = if (isStartDateSelected) {
                eventStartDateTime.toLocalTime()
            } else {
                eventEndDateTime.toLocalTime()
            },
            onPositive = {
                if (isStartDateSelected) {
                    setEventStartTime(it)
                } else {
                    setEventEndTime(it)
                }
                updateTimePickerDialog(false)
            },
            onDismiss = { updateTimePickerDialog(false) },
            onNegative = { updateTimePickerDialog(false) }
        )
    }

    if (showModifyRequestDialogState) {
        KoinClubExtraSmallDialog(
            title = "",
            description = stringResource(R.string.club_recruit_create_request_dialog_description),
            descriptionStyle = KoinTheme.typography.medium15,
            descriptionColor = KoinTheme.colors.neutral600,
            positiveButtonText = stringResource(R.string.club_recruit_create_request_dialog_positive),
            negativeButtonText = stringResource(R.string.club_recruit_create_request_dialog_negative),
            titleTextAlign = TextAlign.Center,
            descriptionTextAlign = TextAlign.Center,
            positiveButtonColors = FilledButtonColors.Primary,
            onPositive = {
                updateModifyRequestDialog(false)
                modifyEvent()
            },
            onNegative = { updateModifyRequestDialog(false) },
            onDismiss = { updateModifyRequestDialog(false) }
        )
    }

    if (showModifyCancelDialogState) {
        KoinClubExtraSmallDialog(
            description = stringResource(R.string.club_recruit_create_cancel_dialog_description),
            descriptionStyle = KoinTheme.typography.medium15,
            descriptionColor = KoinTheme.colors.neutral600,
            positiveButtonText = stringResource(R.string.club_recruit_create_cancel_dialog_positive),
            negativeButtonText = stringResource(R.string.club_recruit_create_cancel_dialog_negative),
            titleTextAlign = TextAlign.Center,
            descriptionTextAlign = TextAlign.Center,
            positiveButtonColors = KoinClubExtraSmallDialogDanger.positiveButtonColors(),
            onPositive = {
                updateModifyCancelDialog(false)
                modifyEventCancel()
            },
            onNegative = { updateModifyCancelDialog(false) },
            onDismiss = { updateModifyCancelDialog(false) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.wrapContentSize()
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f / 4f)
                    .clip(KoinTheme.shapes.extraLarge)
                    .border(1.dp, Color.Unspecified, KoinTheme.shapes.extraLarge)
                    .background(KoinTheme.colors.neutral200)
                    .clickable {
                        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (imageUrls.size <= page) {
                    Image(
                        modifier = Modifier,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_club_create_upload_logo),
                        contentDescription = null
                    )
                } else {
                    Box {
                        SubcomposeAsyncImage(
                            model = imageUrls[page],
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        )
                        Image(
                            painter = painterResource(R.drawable.icon_qna_delete),
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                                .clickable { onImageDeleteClick(page) }
                                .background(
                                    color = KoinTheme.colors.neutral0.copy(alpha = 0.5f),
                                    shape = KoinTheme.shapes.extraLarge
                                )
                                .padding(vertical = 2.dp, horizontal = 2.dp)
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.club_event_create_logo_description),
                    style = KoinTheme.typography.medium18,
                    color = KoinTheme.colors.neutral500,
                    textAlign = TextAlign.Center
                )
            }
        }
        Text(
            text = stringResource(R.string.club_event_create_image_intro),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral500
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "행사 이름:",
                style = KoinTheme.typography.medium18
            )
            KoinClubBasicTextField(
                value = eventName,
                onValueChange = { updateEventName(it) },
                modifier = Modifier
                    .weight(1f),
                minLines = textFieldMinLines,
                maxLength = textFieldMaxLength,
                hint = stringResource(R.string.club_event_create_name_hint)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "행사 기간",
                style = KoinTheme.typography.medium18
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KoinClubDateSelectBox(
                        text = stringResource(
                            R.string.club_date_picker_format,
                            eventStartDateTime.year,
                            eventStartDateTime.monthValue,
                            eventStartDateTime.dayOfMonth,
                            getDayOfWeek(eventStartDateTime.dayOfWeek)
                        ),
                        onClick = {
                            isStartDateSelected = true
                            updateDatePickerDialog(true)
                        }
                    )
                    KoinClubDateSelectBox(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(
                            R.string.club_date_picker_time_format,
                            eventStartDateTime.hour,
                            eventStartDateTime.minute
                        ),
                        onClick = {
                            isStartDateSelected = true
                            updateTimePickerDialog(true)
                        }
                    )
                }
                Text(
                    text = "~",
                    style = KoinTheme.typography.bold20,
                    textAlign = TextAlign.Center
                )
                Column(
                    modifier = Modifier.width(intrinsicSize = IntrinsicSize.Max),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KoinClubDateSelectBox(
                        text = stringResource(
                            R.string.club_date_picker_format,
                            eventEndDateTime.year,
                            eventEndDateTime.monthValue,
                            eventEndDateTime.dayOfMonth,
                            getDayOfWeek(eventEndDateTime.dayOfWeek)
                        ),
                        onClick = {
                            isStartDateSelected = false
                            updateDatePickerDialog(true)
                        }
                    )
                    KoinClubDateSelectBox(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(
                            R.string.club_date_picker_time_format,
                            eventEndDateTime.hour,
                            eventEndDateTime.minute
                        ),
                        onClick = {
                            isStartDateSelected = false
                            updateTimePickerDialog(true)
                        }
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "행사 내용",
                style = KoinTheme.typography.medium18
            )
            KoinClubBasicTextField(
                value = eventIntroduce,
                onValueChange = { updateEventIntroduce(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                minLines = textFieldMinLines,
                maxLength = introTextFieldMaxLength,
                hint = stringResource(R.string.club_event_create_intro_hint)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "상세 내용",
                style = KoinTheme.typography.medium18
            )
            KoinClubBasicTextField(
                value = eventContent,
                onValueChange = { updateEventContent(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                minLines = contentTextFieldMaxLines,
                maxLength = textFieldMaxLength,
                hint = stringResource(R.string.club_event_create_content_hint)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilledButton(
                text = stringResource(R.string.club_event_modify_cancel),
                onClick = { updateModifyCancelDialog(true) },
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
            Spacer(Modifier.width(8.dp))
            FilledButton(
                text = stringResource(R.string.club_event_modify_request),
                onClick = { updateModifyRequestDialog(true) },
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
        }
    }
}

fun handleSideEffect(
    sideEffect: ClubEventModifySideEffect,
    context: Context,
    onCreateSuccess: () -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    when (sideEffect) {
        is ClubEventModifySideEffect.ClubImageUploadFailure -> context.let {
            Toast.makeText(it, it.getString(R.string.club_image_upload_failed), Toast.LENGTH_SHORT).show()
        }
        is ClubEventModifySideEffect.EventCreateSuccess -> {
            onCreateSuccess()
        }
        is ClubEventModifySideEffect.EventCreateFailure -> context.let {
            Toast.makeText(it, it.getString(R.string.club_event_modify_error_failure), Toast.LENGTH_SHORT).show()
        }
        is ClubEventModifySideEffect.LoadClubEventError -> context.let {
            Toast.makeText(it, it.getString(R.string.club_event_modify_error_load), Toast.LENGTH_SHORT).show()
            onNavigateUp()
        }
        is ClubEventModifySideEffect.NavigateUp -> {
            onNavigateUp()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClubEventCreateScreenImplPreview() {
    ClubEventCreateScreenImpl(
        eventName = "이름",
        eventIntroduce = "설명",
        eventContent = "세부 설명",
        eventStartDateTime = LocalDateTime.now().withMinute(0).withSecond(0),
        eventEndDateTime = LocalDateTime.now().plusDays(1)
    )
}
