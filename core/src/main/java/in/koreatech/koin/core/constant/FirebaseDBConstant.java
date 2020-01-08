package in.koreatech.koin.core.constant;

import static in.koreatech.koin.core.BuildConfig.IS_DEBUG;

/**
 * Created by hyerim on 2018. 4. 29....
 * <p>
 * Firebase Database 경로
 */
public class FirebaseDBConstant {
    public static final String BASE_CHANNEL = "production";
    public static final String BASE_CHANNEL_DEBUG = "stage";

    // Callvan Sharing Root Reference
    public static final String CALL_VAN_SHARING = "callvan_sharing";

    // Message
    public static final String ROOM_CHAT_MESSAGE = "room_chat_messages";
    public static final String TOTAL_CHAT_MESSAGE = "room_total_chat_messages";

    public static String getBaseChannel() {
        if (!IS_DEBUG) {
            return BASE_CHANNEL;
        } else {
            return BASE_CHANNEL_DEBUG;
        }

    }
}
