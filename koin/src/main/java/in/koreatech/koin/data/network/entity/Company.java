package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {
    //콜밴 연락처 Unique ID
    @SerializedName("id")
    @Expose
    public String contactUid;

    //콜밴 업체 이름
    @SerializedName("name")
    @Expose
    public String name;

    //콜밴 업체 전화번호
    @SerializedName("phone")
    @Expose
    public String phone;

    //콜밴 업체 전화 횟수
    @SerializedName("hit")
    @Expose
    public Long callCount;

    //콜밴 업체 카드 계산 가능 여부
    @SerializedName("pay_card")
    @Expose
    public Boolean isCardOk;

    //콜밴 업체 계좌이체 계산 가능 여부
    @SerializedName("pay_bank")
    @Expose
    public Boolean isAccountOk;

    //콜밴 업체가 DB에 업데이트 된 날짜
    @SerializedName("updated_at")
    @Expose
    public String updateDate;

    //콜밴 업체가 DB에 생성 된 날짜
    @SerializedName("created_at")
    @Expose
    public String createDate;

    @SerializedName("error")
    @Expose
    public String error;

    public Company() {
    }

    public Company(String contactUid) {
        this.contactUid = contactUid;
    }

    public Company(String contactUid, String name, String phone, Long callCount, Boolean isCardOk, Boolean isAccountOk) {
        this.contactUid = contactUid;
        this.name = name;
        this.phone = phone;
        this.callCount = callCount;
        this.isCardOk = isCardOk;
        this.isAccountOk = isAccountOk;
    }

}
