package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority

data class VersionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("version") val version: String,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

fun VersionResponse.toVersion() = Version(
    currentVersion = "",
    latestVersion = version,
    title = title ?: "",
    content = content ?: "",
    versionUpdatePriority = VersionUpdatePriority.None
)
