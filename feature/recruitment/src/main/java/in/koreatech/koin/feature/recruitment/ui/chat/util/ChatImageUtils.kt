package `in`.koreatech.koin.feature.recruitment.ui.chat.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap

fun handleSelectedImages(
    uris: List<Uri>,
    context: Context,
    uploadImage: (Long, String, String, Uri) -> Unit
) {
    uris.forEach { uri ->
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (!cursor.moveToFirst()) return@use

            val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

            val fileName = if (fileNameIndex != -1 && !cursor.isNull(fileNameIndex)) {
                cursor.getString(fileNameIndex)
            } else {
                uri.lastPathSegment
            } ?: return@use

            if (fileSizeIndex == -1 || cursor.isNull(fileSizeIndex)) return@use
            val fileSize = cursor.getLong(fileSizeIndex)

            val fileType = context.contentResolver.getType(uri)
                ?: MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(fileName.substringAfterLast(".").lowercase())
                ?: "image/*"

            uploadImage(fileSize, fileType, fileName, uri)
        }
    }
}
