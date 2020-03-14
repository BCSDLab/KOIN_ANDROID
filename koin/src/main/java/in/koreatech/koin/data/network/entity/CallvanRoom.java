package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallvanRoom {
    // 콜밴 쉐어링 방 Unique Id
    @SerializedName("id")
    @Expose
    private int uid;

    @SerializedName("user_id")
    @Expose
    private int userId;

    //출발지
    @SerializedName("departure_place")
    @Expose
    private String startingPlace;

    //도착지
    @SerializedName("arrival_place")
    @Expose
    private String endingPlace;

    // 출발 날짜
    //yyyy-MM-dd HH:mm:ss
    @SerializedName("departure_datetime")
    @Expose
    private String startingDate;

    //현재 인원
    @SerializedName("current_people")
    @Expose
    private int currentPeople;

    //최대 인원
    @SerializedName("maximum_people")
    @Expose
    private int maximumPeople;

    // Room 생성시각
    // yyyy-MM-dd HH:mm:ss
    @SerializedName("created_at")
    @Expose
    private String createDate;

    // Room 수정시각
    // yyyy-MM-dd HH:mm:ss
    @SerializedName("updated_at")
    @Expose
    private String updateDate;

    @SerializedName("error")
    @Expose
    private String error;

    public CallvanRoom() {
    }

    public CallvanRoom(int uid) {
        this.uid = uid;
    }

    public CallvanRoom(String startingPlace, String endingPlace, String startingDate, int maximumPeople) {
        this.startingPlace = startingPlace;
        this.endingPlace = endingPlace;
        this.startingDate = startingDate;
        this.maximumPeople = maximumPeople;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartingPlace() {
        return startingPlace;
    }

    public void setStartingPlace(String startingPlace) {
        this.startingPlace = startingPlace;
    }

    public String getEndingPlace() {
        return endingPlace;
    }

    public void setEndingPlace(String endingPlace) {
        this.endingPlace = endingPlace;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public int getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(int currentPeople) {
        this.currentPeople = currentPeople;
    }

    public int getMaximumPeople() {
        return maximumPeople;
    }

    public void setMaximumPeople(int maximumPeople) {
        this.maximumPeople = maximumPeople;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
