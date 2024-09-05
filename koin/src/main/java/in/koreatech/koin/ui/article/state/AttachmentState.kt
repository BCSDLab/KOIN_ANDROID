package `in`.koreatech.koin.ui.article.state

import `in`.koreatech.koin.domain.model.article.Attachment

data class AttachmentState(
    val title: String,
    val url: String
) {

    fun toAttachment() = Attachment(
        name = title,
        url = url
    )
}

fun Attachment.toAttachmentState() = AttachmentState(
    title = name,
    url = url
)