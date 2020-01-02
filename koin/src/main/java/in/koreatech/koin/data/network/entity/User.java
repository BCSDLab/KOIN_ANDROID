package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;

/**
 * Created by hyerim on 2018. 4. 29....
 */
public class User {
    // Firebase User Id
    @SerializedName("id")
    @Expose
    public String uid;

    // 이메일 주소(아이디)
    @SerializedName("portal_account")
    @Expose
    public String userId;

    //비밀번호
    @SerializedName("password")
    @Expose
    public String password;

    // 닉네임
    @SerializedName("nickname")
    @Expose
    public String userNickName;

    //익명 닉네임
    @SerializedName("anonymous_nickname")
    @Expose
    public String anonymousNickName;

    // 이름
    @SerializedName("name")
    @Expose
    public String userName;

    //학번
    @SerializedName("student_number")
    @Expose
    public String studentId;

    //전공
    @SerializedName("major")
    @Expose
    public String major;

    //사용자 신분( 학생 : 0, 교수 : 1, 교직원 : 2)
    @SerializedName("identity")
    @Expose
    public int identity;

    //졸업 여부
    @SerializedName("is_graduated")
    @Expose
    public boolean isGraduate;

    //핸드폰 번호
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;

    //성별 ( 남 :0 여 : 1)
    @SerializedName("gender")
    @Expose
    public int gender;

    //프로필 이미지 url
    @SerializedName("profile_image_url")
    @Expose
    public String profileImageUrl;

    //생성 날짜
    @SerializedName("created_at")
    @Expose
    public String createDate;

    //수정 날짜
    @SerializedName("updated_at")
    @Expose
    public String updateDate;


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

}
