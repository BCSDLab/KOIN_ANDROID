package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.Attachment

data class AttachmentResponse(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
) {

    fun toAttachment() = Attachment(
        name = name,
        url = url,
    )
}