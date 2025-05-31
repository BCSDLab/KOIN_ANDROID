package `in`.koreatech.koin.feature.club.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeClubQnasInfo(
    val rootCount: Int,
    val totalCount: Int,
    val qnas: List<ParcelizeQna>
) : Parcelable {
    @Parcelize
    data class ParcelizeQna(
        val id: Int,
        val authorId: Int,
        val nickname: String,
        val content: String,
        val createdAt: String,
        val children: List<ParcelizeQna>
    ) : Parcelable
}

fun ClubQnasInfo?.toParcelizeClubQnasInfo(): ParcelizeClubQnasInfo? {
    if (this == null) return null
    return ParcelizeClubQnasInfo(
        rootCount = rootCount,
        totalCount = totalCount,
        qnas = qnas.map { qna ->
            ParcelizeClubQnasInfo.ParcelizeQna(
                id = qna.id,
                authorId = qna.authorId,
                nickname = qna.nickname,
                content = qna.content,
                createdAt = qna.createdAt,
                children = qna.children.map { child ->
                    ParcelizeClubQnasInfo.ParcelizeQna(
                        id = child.id,
                        authorId = child.authorId,
                        nickname = child.nickname,
                        content = child.content,
                        createdAt = child.createdAt,
                        children = emptyList() // Assuming children of children are not needed
                    )
                }
            )
        }
    )
}