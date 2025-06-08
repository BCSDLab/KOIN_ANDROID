package `in`.koreatech.koin.data.response.club

import com.google.gson.annotations.SerializedName

data class ClubsResponse(
    @SerializedName("clubs")
    val clubs: List<ClubItemResponse>
) {
    data class ClubItemResponse(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("likes")
        val likes: Int,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("is_liked")
        val isLiked: Boolean,
        @SerializedName("is_like_hidden")
        val isLikeHidden: Boolean
    )
}
