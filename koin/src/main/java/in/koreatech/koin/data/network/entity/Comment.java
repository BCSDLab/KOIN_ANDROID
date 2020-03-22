package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Comment {
    @SerializedName("id")
    @Expose
    private int commentUid;

    @SerializedName("article_id")
    @Expose
    private int articleUid;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("user_id")
    @Expose
    private int authorUid;

    @SerializedName("nickname")
    @Expose
    private String authorNickname;

    @SerializedName("grantEdit")
    @Expose
    private boolean grantEdit;

    @SerializedName("grantDelete")
    @Expose
    private boolean grantDelete;

    @SerializedName("updated_at")
    @Expose
    private String updateDate;

    @SerializedName("created_at")
    @Expose
    private String createDate;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("password")
    @Expose
    private String password;

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

    public int getCommentUid() {
        return commentUid;
    }

    public void setCommentUid(int commentUid) {
        this.commentUid = commentUid;
    }

    public int getArticleUid() {
        return articleUid;
    }

    public void setArticleUid(int articleUid) {
        this.articleUid = articleUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(int authorUid) {
        this.authorUid = authorUid;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public boolean isGrantEdit() {
        return grantEdit;
    }

    public void setGrantEdit(boolean grantEdit) {
        this.grantEdit = grantEdit;
    }

    public boolean isGrantDelete() {
        return grantDelete;
    }

    public void setGrantDelete(boolean grantDelete) {
        this.grantDelete = grantDelete;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
