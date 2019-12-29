package in.koreatech.koin.core.constants;

/**
 * Created by hyerim on 2018. 4. 29....
 *
 * KOIN API StatusCode
 */
public class StatusCode {
    public static final int OK = 200;
    public static final int ENROLLED = 201;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403; //jwt-invalid
    public static final int NOT_FOUND = 404;
    public static final int DUPLICATED = 409;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
}
