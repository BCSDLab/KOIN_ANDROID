package `in`.koreatech.koin.feature.timetable.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import timber.log.Timber

class BitmapUtils(
    private val context: Context
) {
    fun capture(
        view: View,
        onSavedTimeTable: (Bitmap) -> Unit
    ) {
        val bitmap = generateBitmap(view)
        onSavedTimeTable(bitmap)
    }

    fun saveBitmapImage(bitmap: Bitmap): Boolean {
        val timeStamp = System.currentTimeMillis()

        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timeStamp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timeStamp)
            // Pictures/앱 이름
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "koin")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = context.contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            e.message
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)

                    Timber.e("Saved...")
                    return true
                } catch (e: Exception) {
                    Timber.e("Failed save : ${e.message}")
                    return false
                }
            } else {
                val imageFileFolder =
                    File(Environment.getExternalStorageDirectory().toString() + "/" + "koin")
                if (!imageFileFolder.exists()) {
                    imageFileFolder.mkdirs()
                }
                val mImageName = "$timeStamp.png"
                val imageFile = File(imageFileFolder, mImageName)
                try {
                    val outputStream: OutputStream = FileOutputStream(imageFile)
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.close()
                    } catch (e: Exception) {
                        e.message
                    }
                    values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                    context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )

                    Timber.e("Saved...")
                    return true
                } catch (e: Exception) {
                    Timber.e("Failed save : ${e.message}")
                    return false
                }
            }
        }
        return false
    }

    private fun generateBitmap(view: View): Bitmap {
        val bitmap =
            Bitmap.createBitmap(
                view.width,
                view.height,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        view.layout(
            view.left,
            view.top,
            view.right,
            view.bottom
        )
        view.draw(canvas)
        return bitmap
    }
}
