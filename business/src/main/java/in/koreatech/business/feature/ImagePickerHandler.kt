package `in`.koreatech.business.feature

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable


@Composable
fun launchImagePicker(
    contentResolver: ContentResolver,
    maxItem: Int = 3,
    initImageUrls: () -> Unit,
    getPreSignedUrl: (Pair<Pair<Long, String>, Pair<String, String>>) -> Unit = {},
    clearFileInfo: () -> Unit = {},
): ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>> {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItem)
    ) { uriList ->
        clearFileInfo()
        initImageUrls()
        uriList.forEach {
            var fileName = ""
            var fileSize = 0L
            val inputStream = contentResolver.openInputStream(it)
            if (it.scheme.equals("content")) {
                val cursor = contentResolver.query(it, null, null, null, null)
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
                if (inputStream != null) {
                    getPreSignedUrl(
                        Pair(
                            Pair(fileSize, "image/" + fileName.split(".")[1]),
                            Pair(fileName, it.toString())
                        )
                    )
                }
                inputStream?.close()
            }
        }
    }
    return galleryLauncher
}
