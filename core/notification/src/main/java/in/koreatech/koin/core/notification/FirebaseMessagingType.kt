package `in`.koreatech.koin.core.notification

enum class FirebaseMessagingType {
    TEAM_RECRUITMENT,
    UNKNOWN;

    companion object {
        fun toFirebaseMessagingType(type: String): FirebaseMessagingType {
            return when {
                TeamRecruitmentType.contains(type) -> TEAM_RECRUITMENT
                else -> UNKNOWN
            }
        }
    }
}

enum class TeamRecruitmentType {
    NEW_APPLICATION,
    APPLICATION_ACCEPTED,
    APPLICATION_REJECTED,
    RECRUITMENT_CLOSED,
    RECRUITMENT_DELETED,
    NEW_CHAT_MESSAGE;

    companion object {
        fun contains(type: String): Boolean = TeamRecruitmentType.entries.find { it.name.equals(type, ignoreCase = true) } != null
    }
}