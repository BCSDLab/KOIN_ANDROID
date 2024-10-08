package `in`.koreatech.koin.core.upload

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Calendar


fun InputStream.toResizeBitmap(fileSize: Long): Bitmap? {

    return if(fileSize >= 1000000)
    {
        val option = BitmapFactory.Options()

        option.apply {
            inJustDecodeBounds = false
            inSampleSize = 4
        }

        BitmapFactory.decodeStream(this, null, option)
    }
    else
        BitmapFactory.decodeStream(this)
}

fun Bitmap.rotateBitmap(angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.toCompressJPEG(): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
}
fun createImageFile(context: Context): Uri? {
    val contentResolver = context.contentResolver
    val content = ContentValues().apply {
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
}