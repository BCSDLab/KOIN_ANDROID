package `in`.koreatech.koin.core.util

enum class HttpStatus(
    val code: Int,
    val description: String
) {
    /**
     * 2xx Success (성공)
     */
    OK(200, "200 OK."),
    CREATED(201, "201 Created."),
    NO_CONTENT(204, "No Content."),

    /**
     * 400 Bad Request (잘못된 요청)
     */
    BAD_REQUEST(400, "400 Bad Request."),

    /**
     * 401 Unauthorized (인증 필요)
     */
    UNAUTHORIZED(401, "401 Unauthorized."),

    /**
     * 403 Forbidden (인가 필요)
     */
    FORBIDDEN(403, "403 Forbidden."),

    /**
     * 404 Not Found (리소스를 찾을 수 없음)
     */
    NOT_FOUND(404, "404 Not Found."),

    /**
     * 409 CONFLICT (중복 혹은 충돌)
     */
    CONFLICT(409, "409 Conflict."),

    /**
     * 429 Too Many Requests (요청량 초과)
     */
    TOO_MANY_REQUESTS(429, "429 Too Many Requests."),

    /**
     * 500 Internal Server Error (서버 오류)
     */
    INTERNAL_SERVER_ERROR(500, "500 Internal Server Error.")
}
