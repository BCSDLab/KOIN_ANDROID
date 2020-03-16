package in.koreatech.koin.data.network.entity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import in.koreatech.koin.util.TimeUtil;


public class Message {
    private String uid;
    private String userName;
    private String message;
    private String createDate;
    private Boolean isNotice;
    private Boolean isDeleted;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Boolean getNotice() {
        return isNotice;
    }

    public void setNotice(Boolean notice) {
        isNotice = notice;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
