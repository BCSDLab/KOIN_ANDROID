package `in`.koreatech.business.feature.insertstore.insertmaininfo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import `in`.koreatech.business.feature.insertstore.selectcategory.InsertStoreProgressBar
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.upload.toResizeBitmap
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.io.InputStream


@Composable
fun InsertBasicInfoScreen(
    onBackPressed: () -> Unit,
    navigateToInsertDetailInfoScreen: (InsertBasicInfoScreenState) -> Unit,
    viewModel: InsertBasicInfoScreenViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value

    InsertBasicInfoScreenImpl(
        storeImage = state.storeImage,
        storeName = state.storeName,
        storeAddress = state.storeAddress,
        storeImageIsEmpty = state.storeImageIsEmpty,
        isBasicInfoValid = state.isBasicInfoValid,
        onStoreImageChange = {
            viewModel.insertStoreImage(it)
        },
        onStoreNameChange = {
            viewModel.insertStoreName(it)
        },
        onUploadImage = {
            viewModel.getPreSignedUrl(it.first.first, it.first.second, it.second.first, it.second.second)
        },

        onStoreAddressChange = {
            viewModel.insertStoreAddress(it)
        },
        onNextButtonClicked = {
            viewModel.onNextButtonClick()
        },
        onBackPressed = onBackPressed
    )

    HandleSideEffects(viewModel, navigateToInsertDetailInfoScreen)
}

@Composable
fun InsertBasicInfoScreenImpl(
    modifier: Modifier = Modifier,
    storeImage: Uri = Uri.EMPTY,
    storeImageIsEmpty: Boolean = true,
    storeName: String = "",
    storeAddress: String = "",
    isBasicInfoValid: Boolean = false,
    onStoreImageChange: (Uri) -> Unit = {},
    onUploadImage:(Pair<Pair<Long, String>, Pair<String, Bitmap?>>) -> Unit = {},
    onStoreNameChange: (String) -> Unit = {},
    onStoreAddressChange: (String) -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {

                val inputStream = context.contentResolver.openInputStream(it)

                if (it.scheme.equals("content")) {
                    val cursor = context.contentResolver.query(it, null, null, null, null)
                    cursor.use {
                        if (cursor != null && cursor.moveToFirst()) {
                            val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                            if (fileNameIndex != -1 && fileSizeIndex != -1) {
                                val fileName = cursor.getString(fileNameIndex)
                                val fileSize = cursor.getLong(fileSizeIndex)

                                if (inputStream != null) {
                                    onUploadImage(
                                        Pair(
                                            Pair(fileSize, "image/" + fileName.split(".")[1]),
                                            Pair(fileName, inputStream.toResizeBitmap(fileSize))
                                        )
                                    )
                                }
                                inputStream?.close()
                            }
                        }
                    }
                }

                onStoreImageChange(it)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp, bottom = 18.dp)
                .size(40.dp)
                .clickable {
                    onBackPressed()
                }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = "backArrow",
                modifier = modifier
                    .size(40.dp)
            )
        }

        Text(
            modifier = modifier.padding(top = 35.dp, start = 40.dp),
            text = stringResource(id = R.string.insert_store),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = modifier.padding(top = 34.dp, start = 40.dp),
            text = stringResource(id = R.string.insert_store_main_info),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        InsertStoreProgressBar(modifier, 0.50f, R.string.insert_store_basic_info, R.string.page_two)


        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .height(200.dp)
                .border(width = 1.dp, color = ColorMinor)
                .clickable {
                    galleryLauncher.launch(takePhotoFromAlbumIntent)
                }
            ,
            contentAlignment = Alignment.Center
        ){
            if(storeImageIsEmpty){
                Image(
                    modifier = Modifier
                        .padding(horizontal = 71.dp, vertical = 53.dp),
                    painter = painterResource(R.drawable.ic_no_store_image),
                    contentDescription = "emptyStoreImage"
                )
            }
            else{
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(
                        storeImage
                    ),
                    contentDescription = "storeImage",
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        NameTextField(
            stringResource(id = R.string.insert_store_store_name),
            storeName,
            onStoreNameChange,
            32.dp
        )

        NameTextField(
            stringResource(id = R.string.insert_store_store_address),
            storeAddress,
            onStoreAddressChange,
            24.dp
        )

        Button(
            onClick = onNextButtonClicked,
            colors = if(isBasicInfoValid) ButtonDefaults.buttonColors(ColorPrimary)
                    else ButtonDefaults.buttonColors(ColorDisabledButton),
            shape = RectangleShape,
            modifier = modifier
                .padding(top = 57.dp, start = 240.dp, end = 16.dp)
                .height(38.dp)
                .width(105.dp)
        ) {
            Text(
                text = stringResource(id = R.string.next),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

    }

}

@Composable
private fun HandleSideEffects(viewModel: InsertBasicInfoScreenViewModel, navigateToInsertDetailInfoScreen: (InsertBasicInfoScreenState) -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is InsertBasicInfoScreenSideEffect.NavigateToInsertDetailInfoScreen -> navigateToInsertDetailInfoScreen(sideEffect.storeBasicInfo)
            is InsertBasicInfoScreenSideEffect.ShowMessage -> {
                val message = when (sideEffect.type) {
                    BasicInfoErrorType.NullStoreName -> context.getString(R.string.insert_store_null_store_name)
                    BasicInfoErrorType.NullStoreAddress -> context.getString(R.string.insert_store_null_store_address)
                    BasicInfoErrorType.NullStoreImage -> context.getString(R.string.insert_store_null_store_image)
                    BasicInfoErrorType.FailUploadImage -> context.getString(R.string.insert_store_fail_upload_store_image)
                }
                ToastUtil.getInstance().makeShort(message)
            }
        }
    }
}

@Composable
fun NameTextField(
    textString: String = "",
    inputString: String = "",
    onStringChange: (String) -> Unit = {},
    paddingTopValue: Dp = 10.dp
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(top = paddingTopValue),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = textString,
            fontSize = 14.sp,
            color = ColorActiveButton,
            fontWeight = FontWeight.Bold
        )

        BorderTextField(inputString, onStringChange)
    }
}

@Composable
fun BorderTextField(
    inputString: String = "",
    onStringChange: (String) -> Unit = {},
){
    Box(
        modifier = Modifier
            .padding(start = 30.dp)
            .border(width = 1.dp, color = ColorMinor)
            .height(37.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = inputString,
            onValueChange = onStringChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        )
    }
}

private val takePhotoFromAlbumIntent =
    Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
        putExtra(
            Intent.EXTRA_MIME_TYPES,
            arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
        )
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
    }



@Preview
@Composable
fun PreviewInsertBasicInfo() {
    InsertBasicInfoScreenImpl()
}