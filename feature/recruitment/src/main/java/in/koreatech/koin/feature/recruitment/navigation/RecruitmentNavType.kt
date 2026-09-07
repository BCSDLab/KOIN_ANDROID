package `in`.koreatech.koin.feature.recruitment.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class RecruitmentNavType {
    @Serializable
    data object RecruitmentCreate : RecruitmentNavType()

    @Serializable
    data class RecruitmentApply(
        val recruitmentId: Int,
        val roles: List<RecruitmentRoleArg> = emptyList()
    ) : RecruitmentNavType()

    @Serializable
    data object Profile : RecruitmentNavType()

    @Serializable
    data class ProfileCreate(val isEditMode: Boolean = false) : RecruitmentNavType()

    @Serializable
    data object RecruitmentMain : RecruitmentNavType()

    @Serializable
    data class RecruitmentDetail(val postId: Int) : RecruitmentNavType()

    @Serializable
    data class RecruitmentGroupChat(
        val recruitmentId: Int,
        val chatRoomId: Int
    ) : RecruitmentNavType()

    @Serializable
    data class RecruitmentDirectChat(
        val recruitmentId: Int,
        val applicationId: Int
    ) : RecruitmentNavType()

    @Serializable
    data object Notification : RecruitmentNavType()

    @Serializable
    data class ApplicantManagement(val postId: Int) : RecruitmentNavType()

    @Serializable
    data class ApplicantDetail(val postId: Int, val applicantId: Int) : RecruitmentNavType()

    @Serializable
    data object MyRecruitment : RecruitmentNavType()

    @Serializable
    data class RecruitmentModify(val postId: Int) : RecruitmentNavType()

    @Serializable
    data object MyAppliedRecruitment : RecruitmentNavType()
}

@Serializable
data class RecruitmentRoleArg(
    val id: Int,
    val name: String,
    val isClosed: Boolean = false
)

val RecruitmentRoleArgListNavType = object : NavType<List<RecruitmentRoleArg>>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): List<RecruitmentRoleArg>? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): List<RecruitmentRoleArg> {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: List<RecruitmentRoleArg>) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: List<RecruitmentRoleArg>): String {
        return Json.encodeToString(value)
    }
}
