package `in`.koreatech.koin.ui.article.state

import `in`.koreatech.koin.domain.model.article.Attachment

data class AttachmentState(
    val title: String,
    val url: String,
    val size: String,
) {

    fun toAttachment() = Attachment(
        title = title,
        url = url,
        size = size,
    )
}

fun Attachment.toAttachmentState() = AttachmentState(
    title = title,
    url = url,
    size = size,
)