package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import `in`.koreatech.koin.feature.club.component.getKoinClubCancelButtonColor
import `in`.koreatech.koin.feature.club.utils.pickMedia
import java.time.DayOfWeek
import java.time.LocalDate
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ClubRecruitCreateScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onRecruitCreated: () -> Unit = {},
    viewModel: ClubRecruitCreateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(sideEffect, context, onRecruitCreated, onNavigateUp)
    }

    Scaffold(
        modifier = modifier.imePadding(),
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.club_recruit_create_title),
                onNavigationIconClick = onNavigateUp
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { contentPadding ->
        ClubRecruitCreateScreenImpl(
            modifier = Modifier
                .padding(contentPadding),
            imageUrl = uiState.recruitImageUrl,
            uploadImage = { fileSize, fileType, fileName, fileUri ->
                viewModel.getPreSignedUrl(
                    fileSize = fileSize,
                    fileType = fileType,
                    fileName = fileName,
                    imageUri = fileUri
                )
            },
            showDatePickerDialogState = uiState.showDatePickerDialog,
            showDatePickerDialog = viewModel::showDatePickerDialog,
            dismissDatePickerDialog = viewModel::dismissDatePickerDialog,
            recruitStartDate = uiState.recruitStartDate,
            recruitEndDate = uiState.recruitEndDate,
            setRecruitStartDate = viewModel::setRecruitStartDate,
            setRecruitEndDate = viewModel::setRecruitEndDate,
            recruitAlways = uiState.recruitAlways,
            changeRecruitAlways = viewModel::changeRecruitAlways,
            createRecruitment = viewModel::createClubRecruitment,
            createRecruitmentCancel = viewModel::postNavigateUp,
            showCreateRequestDialogState = uiState.showCreateRequestDialog,
            showCreateRequestDialog = viewModel::showCreateRequestDialog,
            dismissCreateRequestDialog = viewModel::dismissCreateRequestDialog,
            showCreateCancelDialogState = uiState.showCreateCancelDialog,
            showCreateCancelDialog = viewModel::showCreateCancelDialog,
            dismissCreateCancelDialog = viewModel::dismissCreateCancelDialog
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubRecruitCreateScreenImpl(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    uploadImage: (fileSize: Long, fileType: String, fileName: String, fileUri: Uri) -> Unit = { _, _, _, _ -> },
    showDatePickerDialogState: Boolean = false,
    showDatePickerDialog: () -> Unit = {},
    dismissDatePickerDialog: () -> Unit = {},
    recruitStartDate: LocalDate = LocalDate.now(),
    recruitEndDate: LocalDate = LocalDate.now().plusDays(1),
    setRecruitStartDate: (LocalDate) -> Unit = {},
    setRecruitEndDate: (LocalDate) -> Unit = {},
    recruitAlways: Boolean = false,
    changeRecruitAlways: () -> Unit = {},
    createRecruitment: (String) -> Unit = {},
    createRecruitmentCancel: () -> Unit = {},
    showCreateRequestDialogState: Boolean = false,
    showCreateRequestDialog: () -> Unit = {},
    dismissCreateRequestDialog: () -> Unit = {},
    showCreateCancelDialogState: Boolean = false,
    showCreateCancelDialog: () -> Unit = {},
    dismissCreateCancelDialog: () -> Unit = {}
) {
    val context = LocalContext.current

    var isStartDateSelected by remember { mutableStateOf(true) }

    val pickMedia = pickMedia(
        context = context,
        onResult = uploadImage
    )
    var recruitDescriptionText by remember { mutableStateOf("") }

    if (showDatePickerDialogState) {
        KoinClubDatePickerDialog(
            onPositive = {
                if (isStartDateSelected) {
                    setRecruitStartDate(it)
                } else {
                    setRecruitEndDate(it)
                }
                dismissDatePickerDialog()
            },
            onDismiss = dismissDatePickerDialog,
            onNegative = dismissDatePickerDialog
        )
    }

    if (showCreateRequestDialogState) {
        KoinClubExtraSmallDialog(
            description = stringResource(R.string.club_recruit_create_request_dialog_description),
            descriptionStyle = KoinTheme.typography.medium15,
            descriptionColor = KoinTheme.colors.neutral600,
            positiveButtonText = stringResource(R.string.club_recruit_create_request_dialog_positive),
            negativeButtonText = stringResource(R.string.club_recruit_create_request_dialog_negative),
            positiveButtonColors = FilledButtonColors.Primary,
            onPositive = {
                dismissCreateRequestDialog()
                createRecruitment(recruitDescriptionText)
            },
            onNegative = dismissCreateRequestDialog,
            onDismiss = dismissCreateRequestDialog
        )
    }

    if (showCreateCancelDialogState) {
        KoinClubExtraSmallDialog(
            description = stringResource(R.string.club_recruit_create_cancel_dialog_description),
            descriptionStyle = KoinTheme.typography.medium15,
            descriptionColor = KoinTheme.colors.neutral600,
            positiveButtonText = stringResource(R.string.club_recruit_create_cancel_dialog_positive),
            negativeButtonText = stringResource(R.string.club_recruit_create_cancel_dialog_negative),
            positiveButtonColors = getKoinClubCancelButtonColor(),
            onPositive = {
                dismissCreateCancelDialog()
                createRecruitmentCancel()
            },
            onNegative = dismissCreateCancelDialog,
            onDismiss = dismissCreateCancelDialog
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
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.club_recruit_create_date_text),
                    style = KoinTheme.typography.medium18
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.club_recruit_create_always_recruit_text),
                        style = KoinTheme.typography.medium18
                    )
                    Checkbox(
                        checked = recruitAlways,
                        onCheckedChange = { changeRecruitAlways() }
                    )
                }
            }
            if (!recruitAlways) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KoinClubDateSelectBox(
                        text = stringResource(
                            R.string.club_recruit_create_date_format,
                            recruitStartDate.year,
                            recruitStartDate.monthValue,
                            recruitStartDate.dayOfMonth,
                            getDayOfWeek(recruitStartDate.dayOfWeek)
                        ),
                        onClick = {
                            isStartDateSelected = true
                            showDatePickerDialog()
                        }
                    )
                    Text(
                        text = "~",
                        style = KoinTheme.typography.bold20,
                        textAlign = TextAlign.Center
                    )
                    KoinClubDateSelectBox(
                        text = stringResource(
                            R.string.club_recruit_create_date_format,
                            recruitEndDate.year,
                            recruitEndDate.monthValue,
                            recruitEndDate.dayOfMonth,
                            getDayOfWeek(recruitEndDate.dayOfWeek)
                        ),
                        onClick = {
                            isStartDateSelected = false
                            showDatePickerDialog()
                        }
                    )
                }
            }
        }
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
                    .clip(KoinTheme.shapes.extraLarge)
                    .border(1.dp, Color.Unspecified, KoinTheme.shapes.extraLarge)
                    .background(KoinTheme.colors.neutral200)
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (imageUrl.isEmpty()) {
                    Image(
                        modifier = Modifier,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_club_create_upload_logo),
                        contentDescription = null
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = imageUrl,
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
                }
                Text(
                    text = stringResource(R.string.club_recruit_create_logo_description),
                    style = KoinTheme.typography.medium18,
                    color = KoinTheme.colors.neutral500
                )
            }
            Text(
                text = stringResource(R.string.club_recruit_create_image_intro),
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.club_recruit_create_recruit_description),
                style = KoinTheme.typography.medium18
            )
            KoinClubBasicTextField(
                value = recruitDescriptionText,
                onValueChange = { recruitDescriptionText = it },
                modifier = Modifier
                    .fillMaxWidth(),
                minLines = 2,
                maxLength = 255,
                hint = stringResource(R.string.club_recruit_create_recruit_description_hint)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilledButton(
                text = stringResource(R.string.club_recruit_create_cancel),
                onClick = showCreateCancelDialog,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
            Spacer(Modifier.width(8.dp))
            FilledButton(
                text = stringResource(R.string.club_recruit_create_request),
                onClick = showCreateRequestDialog,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
        }
    }
}

fun handleSideEffect(
    sideEffect: ClubRecruitCreateSideEffect,
    context: Context,
    onCreateSuccess: () -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    when (sideEffect) {
        is ClubRecruitCreateSideEffect.ClubImageUploadFailure -> context.let {
            Toast.makeText(it, it.getString(R.string.club_image_upload_failed), Toast.LENGTH_SHORT).show()
        }
        is ClubRecruitCreateSideEffect.RecruitCreateSuccess -> context.let {
            Toast.makeText(it, it.getString(R.string.club_recruit_create_sucess), Toast.LENGTH_SHORT).show()
            onCreateSuccess()
        }
        is ClubRecruitCreateSideEffect.RecruitCreateFailure -> context.let {
            Toast.makeText(it, it.getString(R.string.club_recruit_create_error_create_failure), Toast.LENGTH_SHORT).show()
        }
        is ClubRecruitCreateSideEffect.NavigateUp -> {
            onNavigateUp()
        }
    }
}

private fun getDayOfWeek(dayOfWeek: DayOfWeek): String =
    when (dayOfWeek) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
    }
