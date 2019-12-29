package in.koreatech.koin.core.networks;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import in.koreatech.koin.core.constants.FirebaseDBConstant;
import in.koreatech.koin.core.networks.entity.Message;

/**
 * Created by hyerim on 2018. 7. 1....
 */
public class FirebaseDbManager {
    public static String createTotalChatMessage() {
        String messageKey = FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.TOTAL_CHAT_MESSAGE).push().getKey();
        Message message = new Message();
        Map<String, Object> messageValue = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(messageKey, messageValue);

        FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.TOTAL_CHAT_MESSAGE).updateChildren(childUpdates);
        return messageKey;
    }

    public static String createRoomChatMessage(String roomUid) {
        String messageKey = FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.ROOM_CHAT_MESSAGE).child(roomUid).push().getKey();
        Message message = new Message();
        Map<String, Object> messageValue = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(messageKey, messageValue);

        FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.ROOM_CHAT_MESSAGE).child(roomUid).updateChildren(childUpdates);
        return messageKey;
    }

    //total
    public static void editMessage(String messageKey, Message message) {
        Map<String, Object> messageValue = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(messageKey, messageValue);

        FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.TOTAL_CHAT_MESSAGE).updateChildren(childUpdates);
    }

    public static void updateMessage(String uid, String messageKey, String userName, String messageBody, boolean isNotice) {
        Message message = new Message(uid, userName, messageBody, isNotice);

        editMessage(messageKey, message);
    }

    //room
    public static void editMessage(String messageKey, Message message, String roomUid) {
        Map<String, Object> messageValue = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(messageKey, messageValue);

        FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.ROOM_CHAT_MESSAGE).child(roomUid).updateChildren(childUpdates);
    }

    public static void updateMessage(String uid, String messageKey, String userName, String messageBody, String roomUid, boolean isNotice) {
        Message message = new Message(uid, userName, messageBody, isNotice);

        editMessage(messageKey, message, roomUid);
    }

    public static void deleteMessage(String messageKey) {
        FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.TOTAL_CHAT_MESSAGE).child(messageKey).removeValue();
    }
}
