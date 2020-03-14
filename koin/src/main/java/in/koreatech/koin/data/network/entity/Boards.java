package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Boards {
    @SerializedName("id")
    @Expose
    private int boardUid;

    @SerializedName("tag")
    @Expose
    private String tag;

    @SerializedName("name")
    @Expose
    private String boardName;

    @SerializedName("is_anonymous")
    @Expose
    private Boolean isAnonymous;

    @SerializedName("article_count")
    @Expose
    private Long articleCount;

    @SerializedName("updated_at")
    @Expose
    private String updateDate;

    @SerializedName("created_at")
    @Expose
    private String createDate;

    public Boards() {}

    public Boards(int boardUid, String tag, String boardName, Boolean isAnonymous, Long articleCount) {
        this.boardUid = boardUid;
        this.tag = tag;
        this.boardName = boardName;
        this.isAnonymous = isAnonymous;
        this.articleCount = articleCount;
    }

    public int getBoardUid() {
        return boardUid;
    }

    public void setBoardUid(int boardUid) {
        this.boardUid = boardUid;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Boolean getAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
    }

    public Long getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Long articleCount) {
        this.articleCount = articleCount;
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
}
