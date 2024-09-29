package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName

data class KeywordsResponse(
    @SerializedName("keywords") val keywords: List<String>
)