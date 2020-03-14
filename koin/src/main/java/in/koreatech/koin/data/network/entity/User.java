package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;

public class User {
    // Firebase User Id
    @SerializedName("id")
    @Expose
    private String uid;

    // 이메일 주소(아이디)
    @SerializedName("portal_account")
    @Expose
    private String userId;

    //비밀번호
    @SerializedName("password")
    @Expose
    private String password;

    // 닉네임
    @SerializedName("nickname")
    @Expose
    private String userNickName;

    //익명 닉네임
    @SerializedName("anonymous_nickname")
    @Expose
    private String anonymousNickName;

    // 이름
    @SerializedName("name")
    @Expose
    private String userName;

    //학번
    @SerializedName("student_number")
    @Expose
    private String studentId;

    //전공
    @SerializedName("major")
    @Expose
    private String major;

    //사용자 신분( 학생 : 0, 교수 : 1, 교직원 : 2)
    @SerializedName("identity")
    @Expose
    private int identity;

    //졸업 여부
    @SerializedName("is_graduated")
    @Expose
    private boolean isGraduate;

    //핸드폰 번호
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    //성별 ( 남 :0 여 : 1)
    @SerializedName("gender")
    @Expose
    private int gender;

    //프로필 이미지 url
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;

    //생성 날짜
    @SerializedName("created_at")
    @Expose
    private String createDate;

    //수정 날짜
    @SerializedName("updated_at")
    @Expose
    private String updateDate;


    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    public User(String uid, String userId, String userNickName, String anonymousNickName, String userName, String studentId, String major, int identity, boolean isGraduate, String phoneNumber, int grade, int gender, String profileImageUrl) {
        this.uid = uid;
        this.userId = userId;
        this.userNickName = userNickName;
        this.anonymousNickName = anonymousNickName;
        this.userName = userName;
        this.studentId = studentId;
        this.major = major;
        this.identity = identity;
        this.isGraduate = isGraduate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
    }

    public User(String uid, String userId, String password, String userNickName, String anonymousNickName, String userName, String studentId, String major, int identity, boolean isGraduate, String phoneNumber, int grade, int gender, String profileImageUrl) {
        this.uid = uid;
        this.userId = userId;
        this.password = password;
        this.userNickName = userNickName;
        this.anonymousNickName = anonymousNickName;
        this.userName = userName;
        this.studentId = studentId;
        this.major = major;
        this.identity = identity;
        this.isGraduate = isGraduate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
    }

    public User(String uid, String userId, String userNickName, String anonymousNickName, String userName, String studentId, String major, int identity, boolean isGraduate, String phoneNumber, int grade, int gender, String profileImageUrl, String createDate, String updateDate) {
        this.uid = uid;
        this.userId = userId;
        this.userNickName = userNickName;
        this.anonymousNickName = anonymousNickName;
        this.userName = userName;
        this.studentId = studentId;
        this.major = major;
        this.identity = identity;
        this.isGraduate = isGraduate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getAnonymousNickName() {
        return anonymousNickName;
    }

    public void setAnonymousNickName(String anonymousNickName) {
        this.anonymousNickName = anonymousNickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean isGraduate() {
        return isGraduate;
    }

    public void setGraduate(boolean graduate) {
        isGraduate = graduate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
}
