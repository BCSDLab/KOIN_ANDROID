package `in`.koreatech.koin.core.upload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.InputStream


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
fun Bitmap.toCompressJPEG(): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
}