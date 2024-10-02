package `in`.koreatech.koin.ui.article.state

import `in`.koreatech.koin.domain.model.article.Attachment

data class AttachmentState(
    val name: String,
    val url: String,
    val size: String
)

fun Attachment.toAttachmentState() = AttachmentState(
    name = name.removeFileSize(),
    url = url,
    size = name.extractFileSize() ?: "-"
)

/** 파일 크기 추출
 * 본교 파일 이름은 "수강 신청 안내(129 KB)" 이런 형태임
 */
private fun String.extractFileSize(): String? {
    val regex = Regex("""\d+\s?[KMG]?B""")
    return regex.findAll(this).lastOrNull()?.value
}

private fun String.removeFileSize(): String {
    val regex = Regex("""\d+\s?[KMG]?B""")
    val match = regex.findAll(this).lastOrNull()?.value
    return if (match != null) {
        try {
            this.removeRange(this.indexOf(match) - 1, this.length)
        } catch (e: Exception) {
            this
        }
    } else {
        this
    }
}