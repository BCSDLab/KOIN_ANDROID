package `in`.koreatech.koin.feature.chat.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlin.collections.forEach

internal fun handleSelectedImages(
    uris: List<Uri>,
    context: Context,
    uploadImage: (Long, String, String, Uri) -> Unit
) {
    uris.forEach { uri ->
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (!cursor.moveToFirst()) return@use

            val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

            if (fileNameIndex == -1 || fileSizeIndex == -1) return@use

            val fileName = cursor.getString(fileNameIndex)
            val fileSize = cursor.getLong(fileSizeIndex)
            val fileType = context.contentResolver.getType(uri) ?: "image/${fileName.substringAfterLast(".")}"

            uploadImage(fileSize, fileType, fileName, uri)
        }
    }
}
