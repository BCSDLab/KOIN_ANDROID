package `in`.koreatech.koin.ui.userinfo.state

/**
 * 닉네임 중복 검사 상태를 나타내는 클래스
 */
enum class NicknameCheckState {
    /**
     * 중복 검사를 하지 않은 경우
     */
    NEED_CHECK,

    /**
     * 중복인 경우
     */
    EXIST,

    /**
     * 중복이 아닌 경우
     */
    POSSIBLE,

    /**
     * 이전 닉네임과 동일한 경우
     */
    SAME_AS_BEFORE;

    fun isAvailable(): Boolean = this == SAME_AS_BEFORE || this == POSSIBLE
}