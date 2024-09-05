package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.Attachment

data class AttachmentResponse(
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("size") val size: String,
) {

    fun toAttachment() = Attachment(
        title = title,
        url = url,
        size = size,
    )
}