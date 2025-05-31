package `in`.koreatech.koin.feature.club.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun pickMedia(
    context: Context,
    onResult: (fileSize: Long, fileType: String, fileName: String, fileUri: Uri) -> Unit
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    return rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
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