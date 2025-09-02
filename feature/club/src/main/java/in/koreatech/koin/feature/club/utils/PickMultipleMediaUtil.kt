package `in`.koreatech.koin.feature.club.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun pickMultipleMedia(
    context: Context,
    maxItems: Int,
    onResult: (fileSize: Long, fileType: String, fileName: String, fileUri: Uri) -> Unit
): ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>> {
    val coroutineScope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxItems)) { uris ->
        coroutineScope.launch(Dispatchers.IO) {
            uris.forEach { uri ->
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor.use {
                    if (cursor != null && cursor.moveToFirst()) {
                        val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (fileNameIndex != -1 && fileSizeIndex != -1) {
                            val fileName = cursor.getString(fileNameIndex)
                            val fileSize = cursor.getLong(fileSizeIndex)
                            val fileType =
                                context.contentResolver.getType(uri)
                                    ?: "image/${fileName.split(".").last()}"
                            onResult(fileSize, fileType, fileName, uri)
                        }
                    }
                }
            }
        }
    }
}
