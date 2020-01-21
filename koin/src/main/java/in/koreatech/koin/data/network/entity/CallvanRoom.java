package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallvanRoom {
    // 콜밴 쉐어링 방 Unique Id
    @SerializedName("id")
    @Expose
    public int uid;

    @SerializedName("user_id")
    @Expose
    public int userId;

    //출발지
    @SerializedName("departure_place")
    @Expose
    public String startingPlace;

    //도착지
    @SerializedName("arrival_place")
    @Expose
    public String endingPlace;

    // 출발 날짜
    //yyyy-MM-dd HH:mm:ss
    @SerializedName("departure_datetime")
    @Expose
    public String startingDate;

    //현재 인원
    @SerializedName("current_people")
    @Expose
    public int currentPeople;

    //최대 인원
    @SerializedName("maximum_people")
    @Expose
    public int maximumPeople;

    // Room 생성시각
    // yyyy-MM-dd HH:mm:ss
    @SerializedName("created_at")
    @Expose
    public String createDate;

    // Room 수정시각
    // yyyy-MM-dd HH:mm:ss
    @SerializedName("updated_at")
    @Expose
    public String updateDate;

    @SerializedName("error")
    @Expose
    public String error;

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


}
