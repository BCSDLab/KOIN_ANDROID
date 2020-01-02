package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class Comment {
    @SerializedName("id")
    @Expose
    public int commentUid;

    @SerializedName("article_id")
    @Expose
    public int articleUid;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("user_id")
    @Expose
    public int authorUid;

    @SerializedName("nickname")
    @Expose
    public String authorNickname;

    @SerializedName("grantEdit")
    @Expose
    public boolean grantEdit;

    @SerializedName("grantDelete")
    @Expose
    public boolean grantDelete;

    @SerializedName("updated_at")
    @Expose
    public String updateDate;

    @SerializedName("created_at")
    @Expose
    public String createDate;

    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("password")
    @Expose
    public String password;

    public Comment() {

    }

    public Comment(int commentUid, String content) {
        this.commentUid = commentUid;
        this.content = content;
    }

    public Comment(int commentUid, String content, String password) {
        this.commentUid = commentUid;
        this.content = content;
        this.password = password;
    }

    public Comment(int commentUid, int articleUid, String content, int authorUid, String authorNickname, boolean grantEdit, boolean grantDelete, String updateDate, String createDate) {
        this.commentUid = commentUid;
        this.articleUid = articleUid;
        this.content = content;
        this.authorUid = authorUid;
        this.authorNickname = authorNickname;
        this.grantEdit = grantEdit;
        this.grantDelete = grantDelete;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }
}
