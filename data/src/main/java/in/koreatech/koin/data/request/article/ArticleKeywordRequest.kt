package `in`.koreatech.koin.data.request.article

import com.google.gson.annotations.SerializedName

data class ArticleKeywordRequest(
    @SerializedName("keyword") val keyword: String
)