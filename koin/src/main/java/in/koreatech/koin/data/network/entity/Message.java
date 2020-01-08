package in.koreatech.koin.data.network.entity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import in.koreatech.koin.util.TimeUtil;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public class Message {
    public String uid;
    public String userName;
    public String message;
    public String createDate;
    public Boolean isNotice;
    public Boolean isDeleted;

    public Message() {
        this.createDate = TimeUtil.getCurrentTimeToMessage();
        this.isNotice = false;
        this.isDeleted = false;
    }

    public Message(String uid, String userName, String message, Boolean isNotice) {
        this.uid = uid;
        this.userName = userName;
        this.message = message;
        this.createDate = TimeUtil.getCurrentTimeToMessage();
        this.isNotice = isNotice;
        this.isDeleted = false;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("userName", userName);
        result.put("message", message);
        result.put("createDate", createDate);
        result.put("isNotice", isNotice);
        result.put("isDeleted", isDeleted);
        return result;
    }

    @Exclude
    public static Message parseSnapshot(DataSnapshot snapshot) {
        Message msg = new Message();
        msg.uid = (String) snapshot.child("uid").getValue();
        msg.userName = (String) snapshot.child("userName").getValue();
        msg.message = (String) snapshot.child("message").getValue();
        msg.createDate = (String) snapshot.child("createDate").getValue();
        msg.isNotice = (Boolean) snapshot.child("isNotice").getValue();
        msg.isDeleted = (Boolean) snapshot.child("isDeleted").getValue();
        return msg;
    }

}
