package `in`.koreatech.business.feature.signup.businessauth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupSideEffect
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupViewModel
import `in`.koreatech.business.feature.signup.dialog.BusinessAlertDialog
import `in`.koreatech.business.feature.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorTextDescription
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.koin.domain.model.store.AttachStore
import kotlinx.coroutines.CoroutineScope
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun AttachFileScreen(
    modifier: Modifier = Modifier,
    accountSetupViewModel: AccountSetupViewModel = hiltViewModel(),
    businessAuthViewModel: BusinessAuthViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    val context = LocalContext.current
    val businessAuthState = businessAuthViewModel.collectAsState().value
    val accountSetupState = accountSetupViewModel.collectAsState().value
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5),
        onResult = {
            businessAuthViewModel.changeImageUri(
                context,
                it
            )
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            IconButton(
                onClick = businessAuthViewModel::onNavigateToBackScreen,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back_icon),
                )
            }

            Text(
                text = stringResource(id = R.string.sign_up),
                fontSize = 18.sp,
                fontWeight = Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier,
                    color = ColorPrimary,
                    fontWeight = Bold,
                    text = stringResource(id = R.string.business_auth)
                )
                Text(
                    text = stringResource(id = R.string.three_third),
                    color = ColorPrimary,
                    fontWeight = Bold,
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                drawLine(
                    color = ColorUnarchived,
                    start = Offset(-40f, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ColorPrimary,
                    start = Offset(-40f, 0f),
                    end = Offset(size.width + 40, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = stringResource(id = R.string.enter_private_number_attach_file),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.private_number),
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))

            LinedTextField(
                value = accountSetupState.phoneNumber,
                onValueChange = {
                  accountSetupViewModel.onPhoneNumChanged(it)
                },
                label = stringResource(id = R.string.enter_personal_contact),

            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.instruction_file),
                fontSize = 14.sp,
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.file_upload_instruction),
                    fontSize = 12.sp,
                    color = ColorTextDescription
                )
                Text(
                    modifier = Modifier.padding(bottom = 20.dp),
                    text = "${businessAuthState.selectedImages.size}/5",
                    fontSize = 12.sp,
                    color = if(businessAuthState.selectedImages.size==0) ColorTextDescription else ColorSecondary
                )
            }


            Spacer(modifier = Modifier.height(8.dp))

            if (businessAuthState.selectedImages.isNotEmpty()) {
                UploadFileList(
                    modifier,
                    businessAuthState.selectedImages,
                ) {
                    val list = mutableListOf<String>()
                    businessAuthState.selectedImages.forEach {
                        list.add(it.title)
                    }
                    list.removeAt(it)
                    businessAuthViewModel.onImageUrlsChanged(
                        list.map {
                            AttachStore(
                                it,
                                it
                            )
                        }.toMutableList()
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RectangleShape,
                enabled = businessAuthState.selectedImages.isEmpty(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField,
                    contentColor = Gray1,
                    disabledBackgroundColor = ColorTextField,
                    disabledContentColor = Gray3,
                ),
                onClick = { businessAuthViewModel.onDialogVisibilityChanged(true) }) {
                Icon(
                    painter = painterResource(id = R.drawable.attach_file_add),
                    contentDescription = stringResource(id = R.string.attach_file)
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(id = R.string.file_upload),
                    color = if (businessAuthState.selectedImages.isEmpty()) Gray1 else Gray3,
                    fontSize = 13.sp,
                    fontWeight = Bold,
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
                shape = RoundedCornerShape(4.dp),
                enabled = businessAuthState.isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    disabledBackgroundColor = ColorDisabledButton,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                ),

                onClick = {
                    businessAuthViewModel.sendRegisterRequest(
                        fileUrls = businessAuthState.fileInfo.map { it.resultUrl },
                        companyNumber = businessAuthState.companyNumber,
                        phoneNumber = accountSetupState.phoneNumber,
                        name = businessAuthState.name,
                        password = accountSetupState.password,
                        shopId = businessAuthState.shopId,
                        shopName = businessAuthState.shopName,
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
            Spacer(modifier = Modifier.height(20.dp))

        }

    }


    businessAuthViewModel.collectSideEffect {
        when (it) {
            is BusinessAuthSideEffect.NavigateToNextScreen -> onNextClicked()
            BusinessAuthSideEffect.NavigateToBackScreen -> onBackClicked()
            BusinessAuthSideEffect.NavigateToSearchStore -> {}
        }
    }

}

@Composable
fun UploadFileList(
    modifier: Modifier,
    fileList: MutableList<AttachStore>,
    onDelete: (Int) -> Unit = {}
) {
    Column(modifier = Modifier.height(fileList.size * 40.dp)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(fileList.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorTextField)
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDelete(index) }
                            .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.ic_delete_button),
                        contentDescription = stringResource(id = R.string.file_icon),
                    )

                    Text(
                        text = fileList[index].title,
                        fontSize = 15.sp,
                        color = ColorMinor
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

