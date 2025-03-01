package `in`.koreatech.koin.domain.model.article

data class ArticleNoti (
    val title: String,
    val sub: String,
    val value: String,
    val type: ArticleNotiType
)

val articleNotiContent = listOf(
    ArticleNoti("자취방 양도글, 가장 먼저 확인하고 싶을 때?", "공지가 업로드 되면 바로 알려주는\n키워드 알림 설정하러가기", "자취방 양도", ArticleNotiType.KEYWORD),
    ArticleNoti("키워드가 포함된 공지가 업로드 되면\n가장 먼저 알림을 보내드려요!", "키워드 알림 설정 바로가기", "안내글", ArticleNotiType.KEYWORD),
    ArticleNoti("근로 공지, 놓치고 싶지 않다면?", "공지가 업로드 되면 바로 알려주는\n키워드 알림 설정하러가기", "근로", ArticleNotiType.KEYWORD),
    ArticleNoti("해외탐방 공지, 놓치고 싶지 않다면?", "공지가 업로드 되면 바로 알려주는\n키워드 알림 설정하러가기", "해외탐방", ArticleNotiType.KEYWORD),
    ArticleNoti("분실물은 여기 다 모아뒀어요", "바로가기 >>", "", ArticleNotiType.LOST_AND_FOUND),
    ArticleNoti("분실물 찾기 어렵지~ 않아요~", "분실물을 찾는 가장 빠른 방법!", "", ArticleNotiType.LOST_AND_FOUND),
)

enum class ArticleNotiType {
    KEYWORD,
    LOST_AND_FOUND
}