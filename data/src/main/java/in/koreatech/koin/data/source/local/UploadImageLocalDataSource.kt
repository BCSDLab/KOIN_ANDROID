package `in`.koreatech.koin.data.source.local

import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.core.upload.rotateBitmap
import `in`.koreatech.koin.core.upload.toCompressJPEG
import `in`.koreatech.koin.core.upload.toResizeBitmap
import javax.inject.Inject

class UploadImageLocalDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    fun uriToBitmap(uriString: String, fileSize: Long): ByteArray{
        val uri = Uri.parse(uriString)
        val bitmapInputStream = applicationContext.contentResolver.openInputStream(uri)
        val exifInterfaceInputStream = applicationContext.contentResolver.openInputStream(uri)
        lateinit var imageBitmap: Bitmap

        if (uri.scheme.equals("content")) {
            val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null && cursor.moveToFirst()) {

                    if (bitmapInputStream != null && exifInterfaceInputStream != null) {
                        val bitmap = bitmapInputStream.toResizeBitmap(fileSize)
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            val exif = ExifInterface(exifInterfaceInputStream)
                            val rotatedBitmap = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                                ExifInterface.ORIENTATION_ROTATE_90 -> bitmap?.rotateBitmap(90f)
                                ExifInterface.ORIENTATION_ROTATE_180 -> bitmap?.rotateBitmap(180f)
                                ExifInterface.ORIENTATION_ROTATE_270 -> bitmap?.rotateBitmap(270f)
                                else -> bitmap
                            }
                            if(rotatedBitmap != null){
                                imageBitmap = rotatedBitmap
                            }
                        }
                        else{
                            if(bitmap != null) imageBitmap = bitmap
                        }
                    }

                    bitmapInputStream?.close()
                    exifInterfaceInputStream?.close()
                }
            }
        }
        return imageBitmap.toCompressJPEG()
    }
}