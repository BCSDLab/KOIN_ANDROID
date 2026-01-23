package `in`.koreatech.koin.feature.lostandfound.ui.modify

import android.net.Uri
import `in`.koreatech.koin.feature.lostandfound.ui.detail.LostAndFoundDetailSideEffect
import `in`.koreatech.koin.feature.lostandfound.ui.write.LostAndFoundWriteArticleItemState

sealed class LostAndFoundModifySideEffect {

    data object FailedToUploadImage : LostAndFoundModifySideEffect()

    data object UploadedMaxImage : LostAndFoundModifySideEffect()

    data object DeletedArticle : LostAndFoundModifySideEffect()

    data object LostAndFoundModifyArticle : LostAndFoundModifySideEffect()

    data object LostAndFoundModifyArticleFailed : LostAndFoundModifySideEffect()
}
