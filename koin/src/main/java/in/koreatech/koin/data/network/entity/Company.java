package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company {
    //콜밴 연락처 Unique ID
    @SerializedName("id")
    @Expose
    private String contactUid;

    //콜밴 업체 이름
    @SerializedName("name")
    @Expose
    private String name;

    //콜밴 업체 전화번호
    @SerializedName("phone")
    @Expose
    private String phone;

    //콜밴 업체 전화 횟수
    @SerializedName("hit")
    @Expose
    private Long callCount;

    //콜밴 업체 카드 계산 가능 여부
    @SerializedName("pay_card")
    @Expose
    private Boolean isCardOk;

    //콜밴 업체 계좌이체 계산 가능 여부
    @SerializedName("pay_bank")
    @Expose
    private Boolean isAccountOk;

    //콜밴 업체가 DB에 업데이트 된 날짜
    @SerializedName("updated_at")
    @Expose
    private String updateDate;

    //콜밴 업체가 DB에 생성 된 날짜
    @SerializedName("created_at")
    @Expose
    private String createDate;

    @SerializedName("error")
    @Expose
    private String error;

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

    public String getContactUid() {
        return contactUid;
    }

    public void setContactUid(String contactUid) {
        this.contactUid = contactUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCallCount() {
        return callCount;
    }

    public void setCallCount(Long callCount) {
        this.callCount = callCount;
    }

    public Boolean getCardOk() {
        return isCardOk;
    }

    public void setCardOk(Boolean cardOk) {
        isCardOk = cardOk;
    }

    public Boolean getAccountOk() {
        return isAccountOk;
    }

    public void setAccountOk(Boolean accountOk) {
        isAccountOk = accountOk;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
