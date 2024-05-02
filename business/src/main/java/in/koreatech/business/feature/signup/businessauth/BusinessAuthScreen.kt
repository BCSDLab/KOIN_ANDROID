package `in`.koreatech.business.feature.signup.businessauth

import android.graphics.BitmapFactory.decodeStream
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
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupViewModel
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
    accountSetupViewModel: AccountSetupViewModel = hiltViewModel(),
    businessAuthViewModel: BusinessAuthViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    val context = LocalContext.current

    val businessAuthState = businessAuthViewModel.collectAsState().value
    val accountSetupState = accountSetupViewModel.collectAsState().value

    var fileName = ""
    var fileSize = 0L


    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uriList ->
            businessAuthState.bitmap.clear()
            businessAuthState.fileInfo.clear()
            uriList.forEach {
                val inputStream = context.contentResolver.openInputStream(it)
                businessAuthViewModel.onImageUrlsChanged(mutableListOf())
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
               businessAuthViewModel.onImageUrlsChanged(uriList.map { AttachStore(it.toString(), fileName) }.toMutableList())
                if (inputStream != null) {
                    businessAuthViewModel.getPreSignedUrl(
                        uri = it,
                        fileName = fileName,
                        fileSize = fileSize,
                        fileType = "image/" + fileName.split(".")[1],
                        bitmap = decodeStream(inputStream)
                    )

                }
                inputStream?.close()
            }
        }
    )
    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { businessAuthViewModel.onNavigateToBackScreen() }) {
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
                    start = Offset(-35f, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinedTextField(
                value = businessAuthState.name,
                onValueChange = { businessAuthViewModel.onNameChanged(it) },
                label = stringResource(id = R.string.master_name)
            )

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinedTextField(
                    modifier = Modifier.width(197.dp),
                    value = businessAuthState.shopName,
                    onValueChange = { businessAuthViewModel.onShopNameChanged(it) },
                    label = stringResource(id = R.string.enter_store_name)
                )
                Button(
                    modifier = Modifier,
                    onClick = { businessAuthViewModel.onNavigateToSearchStore() },
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
                value = businessAuthState.shopNumber,
                onValueChange = { businessAuthViewModel.onStoreNumberChanged(it) },
                label = stringResource(id = R.string.business_registration_number)
            )
            LinedTextField(
                value = businessAuthState.phoneNumber,
                onValueChange = { businessAuthViewModel.onPhoneNumberChanged(it) },
                label = stringResource(id = R.string.personal_contact)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp),
            ) {

                if (businessAuthState.selectedImages.isNotEmpty()) UploadFileList(
                    modifier,
                    businessAuthState.selectedImages
                )
                else
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(125.dp)
                            .border(BorderStroke(1.dp, ColorHelper))
                            .clickable { businessAuthViewModel.onDialogVisibilityChanged(true) },
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
                enabled = businessAuthState.isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    disabledBackgroundColor = ColorDisabledButton,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                ),

                onClick = {
                    businessAuthState.fileInfo.forEach {
                        businessAuthViewModel.uploadImage(
                            it.preSignedUrl,
                            businessAuthState.bitmap[businessAuthState.fileInfo.indexOf(it)].toString(),
                            it.mediaType,
                            it.fileSize,
                        )
                    }
                    businessAuthViewModel.sendRegisterRequest(
                        businessAuthState.fileInfo.map { it.resultUrl },
                        businessAuthState.shopNumber,
                        accountSetupState.email,
                        businessAuthState.name,
                        accountSetupState.password,
                        businessAuthState.phoneNumber,
                        businessAuthState.shopId,
                        businessAuthState.shopName,
                    )

                }) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 15.sp,
                    color = Color.White,
                )

                BusinessAlertDialog(
                    onDismissRequest = { businessAuthViewModel.onDialogVisibilityChanged(false) },
                    onConfirmation = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                        businessAuthViewModel.onDialogVisibilityChanged(false)
                    },
                    dialogTitle = stringResource(id = R.string.file_upload),
                    dialogText = stringResource(id = R.string.file_upload_requirements),
                    positiveButtonText = stringResource(id = R.string.select_file),
                    visibility = businessAuthState.dialogVisibility
                )
            }
        }
        businessAuthViewModel.collectSideEffect {
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

