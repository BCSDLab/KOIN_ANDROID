package `in`.koreatech.koin.feature.club.ui.clubmodify

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.CLUB_DESCRIPTION_MAX_LENGTH
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.DetailDialog
import `in`.koreatech.koin.feature.club.component.KoinClubBasicTextField
import `in`.koreatech.koin.feature.club.component.KoinClubDropdown
import `in`.koreatech.koin.feature.club.component.KoinClubTextFieldAlert
import `in`.koreatech.koin.feature.club.model.ClubCategories
import `in`.koreatech.koin.feature.club.model.clubCategories
import `in`.koreatech.koin.feature.club.utils.pickMedia
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubModifyScreen(
    modifier: Modifier = Modifier,
    viewModel: ClubModifyViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = { },
    onClubModified: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(it, onClubModified, context)
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.club_list_title),
                onNavigationIconClick = {
                    onNavigateUp()
                }
            )
        },
        // Let's try to draw content behind the system bars
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        ClubModifyScreenImpl(
            isLoading = uiState.isLoading,
            clubName = uiState.clubName,
            clubNameRequired = uiState.clubNameRequired,
            clubDescription = uiState.clubDescription,
            clubCategory = uiState.clubCategory,
            isClubImageChanged = uiState.isClubImageChanged,
            isClubCategoryDropdownExpanded = uiState.isClubCategoryDropdownExpanded,
            clubCategoryRequired = uiState.clubCategoryRequired,
            location = uiState.location,
            locationRequired = uiState.locationRequired,
            instagramUrl = uiState.instagramUrl,
            googleFormUrl = uiState.googleFormUrl,
            openChatUrl = uiState.openChatUrl,
            phoneNumber = uiState.phoneNumber,
            shouldShowModifyDialog = uiState.shouldShowModifyDialog,
            imageUrl = uiState.clubImageUrl,
            isLikeHidden = uiState.isLikeHidden,
            likes = uiState.likes,
            modifier = modifier.padding(innerPadding),
            onClubDescriptionChange = viewModel::updateClubDescription,
            onClubCategoryChange = viewModel::updateClubCategory,
            onClubCategoryDropdownExpandedChange = viewModel::updateIsClubCategoryDropdownExpanded,
            onLocationChange = viewModel::updateLocation,
            onInstagramUrlChange = viewModel::updateInstagramUrl,
            onGoogleFormUrlChange = viewModel::updateGoogleFormUrl,
            onOpenChatUrlChange = viewModel::updateOpenChatUrl,
            onPhoneNumberChange = viewModel::updatePhoneNumber,
            onRequestModifyClub = viewModel::requestModifyClub,
            onCancelModifyClub = onNavigateUp,
            onShouldShowModifyDialogChange = viewModel::updateShowModifyDialog,
            uploadImage = { fileSize, fileType, fileName, fileUri ->
                viewModel.getPreSignedUrl(
                    fileSize = fileSize,
                    fileType = fileType,
                    fileName = fileName,
                    imageUri = fileUri
                )
            },
            onLikeHiddenChange = viewModel::updateIsLikeHidden
        )
    }
}

@Composable
fun ClubModifyScreenImpl(
    isLoading: Boolean,
    clubName: String,
    clubNameRequired: Boolean,
    clubDescription: String,
    clubCategory: ClubCategories?,
    isClubImageChanged: Boolean,
    isClubCategoryDropdownExpanded: Boolean,
    clubCategoryRequired: Boolean,
    location: String,
    locationRequired: Boolean,
    instagramUrl: String,
    googleFormUrl: String,
    openChatUrl: String,
    phoneNumber: String,
    shouldShowModifyDialog: Boolean,
    imageUrl: String,
    isLikeHidden: Boolean,
    likes: Int,
    modifier: Modifier = Modifier,
    onClubDescriptionChange: (String) -> Unit = {},
    onClubCategoryChange: (ClubCategories) -> Unit = {},
    onClubCategoryDropdownExpandedChange: (Boolean) -> Unit = {},
    onLocationChange: (String) -> Unit = {},
    onInstagramUrlChange: (String) -> Unit = {},
    onGoogleFormUrlChange: (String) -> Unit = {},
    onOpenChatUrlChange: (String) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onRequestModifyClub: () -> Unit = {},
    onCancelModifyClub: () -> Unit = {},
    onShouldShowModifyDialogChange: (Boolean) -> Unit = {},
    uploadImage: (fileSize: Long, fileType: String, fileName: String, fileUri: Uri) -> Unit = { _, _, _, _ -> },
    onLikeHiddenChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val pickMedia = pickMedia(
        context = context,
        onResult = uploadImage
    )

    if (shouldShowModifyDialog) {
        DetailDialog(
            title = stringResource(R.string.club_modify_dialog_title),
            titleStyle = KoinTheme.typography.bold18,
            positiveButtonText = stringResource(R.string.club_modify_dialog_positive),
            onDismiss = { onShouldShowModifyDialogChange(false) },
            onNegative = { onShouldShowModifyDialogChange(false) },
            onPositive = {
                onShouldShowModifyDialogChange(false)
                onRequestModifyClub()
            },
            content = {
                Column {
                    Text(
                        text = stringResource(
                            R.string.club_modify_dialog_content,
                            clubName,
                            stringResource(R.string.club_create_category_with_suffix, stringResource(clubCategory!!.stringRes)),
                            location
                        ),
                        style = KoinTheme.typography.regular16
                    )
                }
            }
        )
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .size(200.dp)
                    .clip(KoinTheme.shapes.extraLarge)
                    .background(KoinTheme.colors.neutral200)
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        success = {
                            SubcomposeAsyncImageContent()
                            if (!isClubImageChanged) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = KoinTheme.colors.neutral0.copy(alpha = 0.5f))
                                        .wrapContentHeight(Alignment.CenterVertically),
                                    textAlign = TextAlign.Center,
                                    text = stringResource(R.string.club_modify_logo_description),
                                    style = KoinTheme.typography.medium18,
                                    color = KoinTheme.colors.neutral600
                                )
                            }
                        },
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
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            FilledButton(
                modifier = Modifier.width(100.dp),
                text = stringResource(R.string.club_modify_cancel),
                onClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Club.CLUB_CORRECTION_CANCEL,
                        "취소"
                    )
                    onCancelModifyClub()
                },
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                textStyle = KoinTheme.typography.medium14
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilledButton(
                modifier = Modifier.width(100.dp),
                text = stringResource(R.string.club_modify_save),
                onClick = {
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Club.CLUB_CORRECTION_SAVE,
                        "저장"
                    )
                    onShouldShowModifyDialogChange(true)
                },
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                textStyle = KoinTheme.typography.medium14
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.noRippleClickable {
                onLikeHiddenChange(!isLikeHidden)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = clubName,
                style = KoinTheme.typography.medium16
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.icon_like_true),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.club_modify_likes, likes),
                style = KoinTheme.typography.medium14
            )

            Spacer(modifier = Modifier.width(4.dp))

            Image(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(if (isLikeHidden) R.drawable.ic_club_invisible else R.drawable.ic_club_visible),
                contentDescription = null
            )
        }

        if (clubNameRequired) {
            Spacer(modifier = Modifier.height(4.dp))

            KoinClubTextFieldAlert(
                text = stringResource(R.string.club_create_warning_required)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.club_create_category_header),
                style = KoinTheme.typography.medium14
            )

            Spacer(modifier = Modifier.width(10.dp))

            KoinClubDropdown(
                text = if (clubCategory != null) {
                    stringResource(R.string.club_create_category_with_suffix, stringResource(clubCategory.stringRes))
                } else {
                    stringResource(R.string.club_create_category_placeholder)
                },
                isDropdownExpanded = isClubCategoryDropdownExpanded,
                onDropdownExpandChange = onClubCategoryDropdownExpandedChange,
                items = clubCategories.map { stringResource(R.string.club_create_category_with_suffix, stringResource(it.stringRes)) }.toImmutableList(),
                onItemSelected = { index ->
                    onClubCategoryChange(clubCategories[index])
                },
                borderColor = if (clubCategoryRequired) KoinTheme.colors.sub500 else KoinTheme.colors.neutral300
            )
        }

        if (clubCategoryRequired) {
            Spacer(modifier = Modifier.height(4.dp))

            KoinClubTextFieldAlert(
                text = stringResource(R.string.club_create_warning_required)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.club_create_location_header),
            style = KoinTheme.typography.medium14
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinClubBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = location,
            onValueChange = onLocationChange,
            hint = stringResource(R.string.club_create_location_hint),
            borderColor = if (locationRequired) KoinTheme.colors.sub500 else KoinTheme.colors.primary300
        )

        if (locationRequired) {
            Spacer(modifier = Modifier.height(4.dp))

            KoinClubTextFieldAlert(
                text = stringResource(R.string.club_create_warning_required)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.club_create_introduction_header),
            style = KoinTheme.typography.medium14
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinClubBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = clubDescription,
            onValueChange = onClubDescriptionChange,
            hint = stringResource(R.string.club_create_introduction_hint),
            maxLines = CLUB_DESCRIPTION_MAX_LENGTH
        )

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(vertical = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = stringResource(R.string.club_create_contact_instagram_header),
                    style = KoinTheme.typography.medium14
                )

                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = stringResource(R.string.club_create_contact_google_form_header),
                    style = KoinTheme.typography.medium14
                )

                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = stringResource(R.string.club_create_contact_open_chat_header),
                    style = KoinTheme.typography.medium14
                )

                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = stringResource(R.string.club_create_contact_phone_header),
                    style = KoinTheme.typography.medium14
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.width(270.dp)
            ) {
                KoinClubBasicTextField(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth(),
                    value = instagramUrl,
                    onValueChange = onInstagramUrlChange,
                    hint = stringResource(R.string.club_create_contact_instagram_hint)
                )

                KoinClubBasicTextField(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth(),
                    value = googleFormUrl,
                    onValueChange = onGoogleFormUrlChange,
                    hint = stringResource(R.string.club_create_contact_google_form_hint)
                )

                KoinClubBasicTextField(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth(),
                    value = openChatUrl,
                    onValueChange = onOpenChatUrlChange,
                    hint = stringResource(R.string.club_create_contact_open_chat_hint)
                )

                KoinClubBasicTextField(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth(),
                    value = phoneNumber,
                    onValueChange = onPhoneNumberChange,
                    hint = stringResource(R.string.club_create_contact_phone_hint)
                )
            }
        }

        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

fun handleSideEffect(
    sideEffect: ClubModifySideEffect,
    onClubModified: () -> Unit = { },
    context: Context
) {
    when (sideEffect) {
        ClubModifySideEffect.ClubModifySuccess -> {
            onClubModified()
        }

        ClubModifySideEffect.ClubImageUploadFailure -> context.let {
            Toast.makeText(it, it.getString(R.string.club_image_upload_failed), Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubModifyScreenPreview() {
    ClubModifyScreenImpl(
        isLoading = false,
        clubName = "Club Name",
        clubNameRequired = false,
        clubDescription = "This is a club description.",
        clubCategory = ClubCategories.ACADEMIC,
        clubCategoryRequired = false,
        isClubImageChanged = false,
        isClubCategoryDropdownExpanded = false,
        location = "Location",
        locationRequired = false,
        instagramUrl = "https://instagram.com/club",
        googleFormUrl = "https://forms.gle/club",
        openChatUrl = "https://open.kakao.com/o/gjK8f3Yc",
        phoneNumber = "010-1234-5678",
        shouldShowModifyDialog = false,
        imageUrl = "",
        isLikeHidden = false,
        likes = 0
    )
}
