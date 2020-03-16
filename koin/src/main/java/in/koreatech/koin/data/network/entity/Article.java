package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Article {
    @SerializedName("id")
    @Expose
    private int articleUid;

    @SerializedName("board_id")
    @Expose
    private int boardUid;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("user_id")
    @Expose
    private String authorUid;

    @SerializedName("nickname")
    @Expose
    private String authorNickname;

    @SerializedName("hit")
    @Expose
    private int hitCount;

    @SerializedName("comment_count")
    @Expose
    private int commentCount;

    @SerializedName("is_solved")
    @Expose
    private Boolean isSolved;

    @SerializedName("updated_at")
    @Expose
    private String updateDate;

    @SerializedName("created_at")
    @Expose
    private String createDate;


    @SerializedName("comments")
    @Expose
    private ArrayList<Comment> commentArrayList = new ArrayList<>();


    @SerializedName("tag")
    @Expose
    private String tag;

    @SerializedName("grantEdit")
    @Expose
    private boolean isGrantEdit;

    @SerializedName("erorr")
    @Expose
    private String error;

    public Article() {
    }

    //create
    public Article(int boardUid, String title, String content) {
        this.boardUid = boardUid;
        this.title = title;
        this.content = content;
    }

    //update
    public Article(int boardUid, int articleUid, String title, String content) {
        this.boardUid = boardUid;
        this.articleUid = articleUid;
        this.title = title;
        this.content = content;
    }

    public Article(int articleUid, String title, String content, String authorNickname, int hitCount, int commentCount, Boolean isSolved, String updateDate, String createDate) {
        this.articleUid = articleUid;
        this.title = title;
        this.content = content;
        this.authorNickname = authorNickname;
        this.hitCount = hitCount;
        this.commentCount = commentCount;
        this.isSolved = isSolved;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.commentArrayList = new ArrayList<>();
    }

    public Article(int articleUid, int boardUid, String title, String content, String authorUid, String authorNickname, int hitCount, int commentCount, Boolean isSolved, String updateDate, String createDate, ArrayList<Comment> commentArrayList, String tag) {
        this.articleUid = articleUid;
        this.boardUid = boardUid;
        this.title = title;
        this.content = content;
        this.authorUid = authorUid;
        this.authorNickname = authorNickname;
        this.hitCount = hitCount;
        this.commentCount = commentCount;
        this.isSolved = isSolved;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.commentArrayList = new ArrayList<>();
        this.commentArrayList.addAll(commentArrayList);
        this.tag = tag;
    }

    public int getArticleUid() {
        return articleUid;
    }

    public void setArticleUid(int articleUid) {
        this.articleUid = articleUid;
    }

    public int getBoardUid() {
        return boardUid;
    }

    public void setBoardUid(int boardUid) {
        this.boardUid = boardUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean getSolved() {
        return isSolved;
    }

    public void setSolved(Boolean solved) {
        isSolved = solved;
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

    public ArrayList<Comment> getCommentArrayList() {
        return commentArrayList;
    }

    public void setCommentArrayList(ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    public boolean isGrantEdit() {
        return isGrantEdit;
    }

    public void setGrantEdit(boolean grantEdit) {
        isGrantEdit = grantEdit;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
