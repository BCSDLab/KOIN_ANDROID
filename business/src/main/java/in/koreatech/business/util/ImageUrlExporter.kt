package `in`.koreatech.business.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import `in`.koreatech.koin.domain.model.owner.ImageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getImageInfo(context: Context, uri: Uri): ImageInfo {
    return withContext(Dispatchers.IO) {
        lateinit var imageInfo: ImageInfo
        val inputStream = context.contentResolver.openInputStream(uri)

        if (uri.scheme.equals("content")) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (cursor.moveToFirst()) {
                    val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                    if (fileNameIndex != -1 && fileSizeIndex != -1) {
                        val fileName = cursor.getString(fileNameIndex)
                        val fileSize = cursor.getLong(fileSizeIndex)

                        inputStream?.use {
                            imageInfo = ImageInfo(fileSize, "image/" + fileName.split(".")[1], fileName)
                        }
                    }
                }
            }
        }
        imageInfo
    }
}