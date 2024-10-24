package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName

data class ArticleKeywordWrapperResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("keywords") val keywords: List<ArticleKeywordResponse>
) {
    data class ArticleKeywordResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("keyword") val keyword: String,
    )
}