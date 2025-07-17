package `in`.koreatech.koin.feature.club.ui.clubeventcreate

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.height
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
import `in`.koreatech.koin.feature.club.component.KoinClubTextFieldAlert
import `in`.koreatech.koin.feature.club.component.KoinClubTimePickerDialog
import `in`.koreatech.koin.feature.club.utils.getDayOfWeek
import `in`.koreatech.koin.feature.club.utils.pickMedia
import `in`.koreatech.koin.feature.club.utils.pickMultipleMedia
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubEventCreateScreen(
    modifier: Modifier = Modifier,
    viewModel: ClubEventCreateViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onEventCreated: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, context, onEventCreated, onNavigateUp)
    }

    BackHandler {
        viewModel.updateCreateCancelDialog(true)
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
            onMaxImageError = viewModel::postMaxImageLimitError,
            onImageDeleteClick = viewModel::deleteImageUrl,
            maxImageLimit = uiState.maxImageLimit,
            isLoading = uiState.isLoading,
            showCreateCancelDialogState = uiState.showCreateCancelDialog,
            showCreateRequestDialogState = uiState.showCreateRequestDialog,
            showDatePickerDialogState = uiState.showDatePickerDialog,
            showTimePickerDialogState = uiState.showTimePickerDialog,
            eventNameRequired = uiState.eventNameRequired,
            eventIntroduceRequired = uiState.eventIntroduceRequired,
            updateCreateCancelDialog = viewModel::updateCreateCancelDialog,
            updateCreateRequestDialog = viewModel::updateCreateRequestDialog,
            updateDatePickerDialog = viewModel::updateDatePickerDialog,
            updateTimePickerDialog = viewModel::updateTimePickerDialog,
            updateEventName = viewModel::updateEventName,
            updateEventIntroduce = viewModel::updateEventIntroduce,
            updateEventContent = viewModel::updateEventContent,
            setEventStartDate = viewModel::setEventStartDate,
            setEventEndDate = viewModel::setEventEndDate,
            setEventStartTime = viewModel::setEventStartTime,
            setEventEndTime = viewModel::setEventEndTime,
            createEvent = viewModel::createClubEvent,
            createEventCancel = viewModel::postNavigateUp
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
    maxImageLimit: Int = 7,
    isLoading: Boolean = false,
    showCreateCancelDialogState: Boolean = false,
    showCreateRequestDialogState: Boolean = false,
    showDatePickerDialogState: Boolean = false,
    showTimePickerDialogState: Boolean = false,
    eventNameRequired: Boolean = false,
    eventIntroduceRequired: Boolean = false,
    updateCreateCancelDialog: (Boolean) -> Unit = {},
    updateCreateRequestDialog: (Boolean) -> Unit = {},
    updateDatePickerDialog: (Boolean) -> Unit = {},
    updateTimePickerDialog: (Boolean) -> Unit = {},
    updateEventName: (String) -> Unit = {},
    updateEventIntroduce: (String) -> Unit = {},
    updateEventContent: (String) -> Unit = {},
    setEventStartDate: (LocalDate) -> Unit = {},
    setEventEndDate: (LocalDate) -> Unit = {},
    setEventStartTime: (LocalTime) -> Unit = {},
    setEventEndTime: (LocalTime) -> Unit = {},
    createEvent: () -> Unit = {},
    createEventCancel: () -> Unit = {},
    uploadImage: (fileSize: Long, fileType: String, fileName: String, fileUri: Uri) -> Unit = { _, _, _, _ -> },
    onMaxImageError: () -> Unit = {},
    onImageDeleteClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    var isStartDateSelected by remember { mutableStateOf(true) }

    val textFieldMinLines = 1
    val textFieldMaxLength = 255
    val introTextFieldMaxLength = 70
    val contentTextFieldMaxLines = 2

    val pickMultipleMedia = pickMultipleMedia(
        context = context,
        maxItems = maxOf(maxImageLimit - imageUrls.size, 2),
        onResult = uploadImage
    )

    val pickMedia = pickMedia(
        context = context,
        onResult = uploadImage
    )

    val pagerState = rememberPagerState(pageCount = { minOf(imageUrls.size + 1, maxImageLimit) })

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
            title = stringResource(R.string.club_time_picker_title),
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

    if (showCreateRequestDialogState) {
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
                updateCreateRequestDialog(false)
                createEvent()
            },
            onNegative = { updateCreateRequestDialog(false) },
            onDismiss = { updateCreateRequestDialog(false) }
        )
    }

    if (showCreateCancelDialogState) {
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
                updateCreateCancelDialog(false)
                createEventCancel()
            },
            onNegative = { updateCreateCancelDialog(false) },
            onDismiss = { updateCreateCancelDialog(false) }
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
                    .clickable(!isLoading) {
                        when {
                            maxImageLimit - imageUrls.size >= 2 -> {
                                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                            maxImageLimit - imageUrls.size == 1 -> {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                            else -> {
                                onMaxImageError()
                            }
                        }
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
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.club_recruit_create_name),
                    style = KoinTheme.typography.medium18
                )
                KoinClubBasicTextField(
                    value = eventName,
                    onValueChange = { updateEventName(it) },
                    modifier = Modifier
                        .weight(1f),
                    minLines = textFieldMinLines,
                    maxLength = textFieldMaxLength,
                    borderColor = if (eventNameRequired) KoinTheme.colors.sub500 else KoinTheme.colors.neutral100,
                    hint = stringResource(R.string.club_event_create_name_hint)
                )
            }
            if (eventNameRequired) {
                Spacer(modifier = Modifier.height(4.dp))

                KoinClubTextFieldAlert(
                    text = stringResource(R.string.club_create_warning_required)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.club_recruit_create_period),
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
                            R.string.club_time_picker_format,
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
                            R.string.club_time_picker_format,
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
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.club_recruit_create_introduce),
                style = KoinTheme.typography.medium18
            )
            Spacer(modifier = Modifier.height(12.dp))
            KoinClubBasicTextField(
                value = eventIntroduce,
                onValueChange = { updateEventIntroduce(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                minLines = textFieldMinLines,
                maxLength = introTextFieldMaxLength,
                borderColor = if (eventIntroduceRequired) KoinTheme.colors.sub500 else KoinTheme.colors.neutral100,
                hint = stringResource(R.string.club_event_create_intro_hint)
            )
            if (eventIntroduceRequired) {
                Spacer(modifier = Modifier.height(4.dp))

                KoinClubTextFieldAlert(
                    text = stringResource(R.string.club_create_warning_required)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.club_recruit_create_recruit_description),
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
                text = stringResource(R.string.club_event_create_cancel),
                onClick = { updateCreateCancelDialog(true) },
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
            Spacer(Modifier.width(8.dp))
            FilledButton(
                text = stringResource(R.string.club_event_create_request),
                onClick = { updateCreateRequestDialog(true) },
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
        }
    }
}

fun handleSideEffect(
    sideEffect: ClubEventCreateSideEffect,
    context: Context,
    onCreateSuccess: () -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    when (sideEffect) {
        is ClubEventCreateSideEffect.ClubImageUploadFailure -> context.let {
            Toast.makeText(it, it.getString(R.string.club_image_upload_failed), Toast.LENGTH_SHORT).show()
        }
        is ClubEventCreateSideEffect.EventCreateSuccess -> {
            onCreateSuccess()
        }
        is ClubEventCreateSideEffect.EventCreateFailure -> context.let {
            Toast.makeText(it, it.getString(R.string.club_event_create_error_create_failure), Toast.LENGTH_SHORT).show()
        }
        is ClubEventCreateSideEffect.NavigateUp -> {
            onNavigateUp()
        }
        is ClubEventCreateSideEffect.MaxImageLimit -> context.let {
            Toast.makeText(it, it.getString(R.string.club_event_max_image_error), Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClubEventCreateScreenImplPreview() {
    ClubEventCreateScreenImpl(
        eventName = "",
        eventIntroduce = "",
        eventContent = "",
        eventStartDateTime = LocalDateTime.now().withMinute(0).withSecond(0),
        eventEndDateTime = LocalDateTime.now().plusDays(1)
    )
}
