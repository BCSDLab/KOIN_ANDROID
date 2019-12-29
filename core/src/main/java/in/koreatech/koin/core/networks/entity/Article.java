package in.koreatech.koin.core.networks.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class Article {
    @SerializedName("id")
    @Expose
    public int articleUid;

    @SerializedName("board_id")
    @Expose
    public int boardUid;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("user_id")
    @Expose
    public String authorUid;

    @SerializedName("nickname")
    @Expose
    public String authorNickname;

    @SerializedName("hit")
    @Expose
    public int hitCount;

    @SerializedName("comment_count")
    @Expose
    public int commentCount;

    @SerializedName("is_solved")
    @Expose
    public Boolean isSolved;

    @SerializedName("updated_at")
    @Expose
    public String updateDate;

    @SerializedName("created_at")
    @Expose
    public String createDate;


    @SerializedName("comments")
    @Expose
    public ArrayList<Comment> commentArrayList = new ArrayList<>();


    @SerializedName("tag")
    @Expose
    public String tag;

    @SerializedName("grantEdit")
    @Expose
    public boolean isGrantEdit;

    @SerializedName("erorr")
    @Expose
    public String error;

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
}
