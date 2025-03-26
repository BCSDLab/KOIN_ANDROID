package `in`.koreatech.koin.data.requestbody

import java.io.InputStream
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.source

class S3RequestBody(
    private val inputStream: InputStream,
    private val mediaType: MediaType,
    private val contentLength: Long
) : RequestBody() {
    override fun writeTo(sink: okio.BufferedSink) {
        inputStream.source().use(sink::writeAll)
    }

    override fun contentType(): MediaType = mediaType

    override fun contentLength(): Long = contentLength
}
