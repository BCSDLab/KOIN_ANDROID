package `in`.koreatech.koin.core.file

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import javax.inject.Singleton


class FileUtil private constructor() {

    private lateinit var context: Context

    companion object {
        private var instance: FileUtil? = null

        fun getInstance(): FileUtil {
            return instance ?: synchronized(this) {
                instance ?: FileUtil().also {
                    instance = it
                }
            }
        }
    }

    fun init(context: Context){
        this@FileUtil.context = context
    }

    fun createImageFile(): Uri? {
        val contentResolver = context.contentResolver
        val content = ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }
}