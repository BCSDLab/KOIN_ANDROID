package `in`.koreatech.koin.domain.model.user

/**
 * 로깅에 사용되는 유저 정보
 * @param userId {학번 앞 6자리}_{User ID}
 * @param gender 남성: "0", 여성: "1", 알수없음: ""
 * @param major 학생의 전공
 *
 * @see User
 */
data class LoggerUserData(
    val userId: String,
    val gender: String,
    val major: String,
)
