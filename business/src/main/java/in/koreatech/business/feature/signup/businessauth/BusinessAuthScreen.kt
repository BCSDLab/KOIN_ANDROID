package `in`.koreatech.business.feature.signup.businessauth

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.signup.dialog.BusinessAlertDialog
import `in`.koreatech.business.feature.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorDescription
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.koin.domain.model.store.AttachStore
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BusinessAuthScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessAuthViewModel = hiltViewModel(),
    email: String,
    password: String,
    onBackClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    val context = LocalContext.current

    val state = viewModel.collectAsState().value
    var fileName = ""
    var fileSize = 0L

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uriList ->
            state.inputStream.clear()
            state.fileInfo.clear()
            uriList.forEach {
                val inputStream = context.contentResolver.openInputStream(it)
                viewModel.onImageUrlsChanged(state.selectedImages)

                if (it.scheme.equals("content")) {
                    val cursor = context.contentResolver.query(it, null, null, null, null)
                    cursor.use {
                        if (cursor != null && cursor.moveToFirst()) {
                            val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                            if (fileNameIndex != -1 && fileSizeIndex != -1) {
                                fileName = cursor.getString(fileNameIndex)
                                fileSize = cursor.getLong(fileSizeIndex)
                            }
                        }
                    }
                }
                state.selectedImages.add(AttachStore(it.toString(), fileName))
                if (inputStream != null) {
                    viewModel.presignedUrl(
                        uri = it,
                        fileName = fileName,
                        fileSize = fileSize,
                        fileType = "image/" + fileName.split(".")[1],
                        fileStream = inputStream
                    )

                }
            }
        }
    )
    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { viewModel.onNavigateToBackScreen() }) {
            Icon(
                modifier = Modifier.padding(start = 10.dp),
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_icon),
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.master_sign_up),
                fontSize = 24.sp,
                fontWeight = Bold,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    color = ColorSecondary, text = stringResource(id = R.string.business_auth)
                )
                Text(
                    color = ColorSecondary, text = stringResource(id = R.string.three_third)
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                drawLine(
                    color = ColorSecondary,
                    start = Offset(0f - 35, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinedTextField(
                value = state.name,
                onValueChange = { viewModel.onNameChanged(it) },
                label = stringResource(id = R.string.master_name)
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinedTextField(
                    modifier = Modifier.width(197.dp),
                    value = state.storeName,
                    onValueChange = { viewModel.onShopNameChanged(it) },
                    label = stringResource(id = R.string.enter_store_name)
                )
                Button(
                    modifier = Modifier,
                    onClick = { viewModel.onNavigateToSearchStore() },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorPrimary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(text = stringResource(id = R.string.search_store))
                }
            }
            LinedTextField(
                value = state.shopNumber,
                onValueChange = { viewModel.onStoreNumberChanged(it) },
                label = stringResource(id = R.string.business_registration_number)
            )
            LinedTextField(
                value = state.phoneNumber,
                onValueChange = { viewModel.onPhoneNumberChanged(it) },
                label = stringResource(id = R.string.personal_contact)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp),
            ) {

                if (state.selectedImages.isNotEmpty()) UploadFileList(
                    modifier,
                    state.selectedImages
                )
                else
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(125.dp)
                            .border(BorderStroke(1.dp, ColorHelper))
                            .clickable { viewModel.onDialogVisibilityChanged(true) },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.plus_square),
                            contentDescription = stringResource(id = R.string.upload_file_icon),
                            tint = ColorSecondary
                        )
                        Text(
                            text = stringResource(id = R.string.file_upload_prompt),
                            fontSize = 12.sp,
                            fontWeight = Bold,
                            color = Color.Black
                        )
                        Text(
                            text = stringResource(id = R.string.file_upload_instruction),
                            fontSize = 11.sp,
                            color = ColorDescription,
                        )
                    }

            }

            Spacer(modifier = Modifier.height(47.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
                shape = RectangleShape,
                enabled = state.isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    disabledBackgroundColor = ColorDisabledButton,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                ),

                onClick = {
                    state.fileInfo.forEach {
                        viewModel.uploadImage(
                            it.preSignedUrl,
                            state.inputStream[state.fileInfo.indexOf(it)],
                            it.mediaType,
                            it.fileSize,
                        )
                    }
                    viewModel.sendRegisterRequest(
                        state.fileUrl,
                        state.shopNumber,
                        email,
                        state.name,
                        password,
                        state.phoneNumber,
                        state.shopId,
                        state.storeName,
                    )

                }) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 15.sp,
                    color = Color.White,
                )

                BusinessAlertDialog(
                    onDismissRequest = { viewModel.onDialogVisibilityChanged(false) },
                    onConfirmation = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                        viewModel.onDialogVisibilityChanged(false)
                    },
                    dialogTitle = stringResource(id = R.string.file_upload),
                    dialogText = stringResource(id = R.string.file_upload_requirements),
                    positiveButtonText = stringResource(id = R.string.select_file),
                    visibility = state.dialogVisibility
                )
            }
        }
        viewModel.collectSideEffect {
            when (it) {
                BusinessAuthSideEffect.NavigateToSearchStore -> {
                    onSearchClicked()
                }

                BusinessAuthSideEffect.NavigateToBackScreen -> {
                    onBackClicked()
                }

                BusinessAuthSideEffect.NavigateToNextScreen -> {
                    onNextClicked()
                }
            }

        }
    }
}

@Composable
fun UploadFileList(modifier: Modifier, item: MutableList<AttachStore>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .border(BorderStroke(1.dp, ColorHelper)),
    ) {

        items(item.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.file_icon),
                    contentDescription = stringResource(id = R.string.file_icon),
                    tint = ColorMinor,
                )
                Text(text = item[it].title, fontSize = 15.sp, color = ColorMinor)
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

