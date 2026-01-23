package `in`.koreatech.koin.feature.lostandfound.ui.modify

sealed class LostAndFoundModifySideEffect {

    data object FailedToUploadImage : LostAndFoundModifySideEffect()

    data object UploadedMaxImage : LostAndFoundModifySideEffect()

    data object DeletedArticle : LostAndFoundModifySideEffect()

    data object LostAndFoundModifyArticle : LostAndFoundModifySideEffect()

    data object LostAndFoundModifyArticleFailed : LostAndFoundModifySideEffect()
}
