package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class Boards {
    @SerializedName("id")
    @Expose
    public int boardUid;

    @SerializedName("tag")
    @Expose
    public String tag;

    @SerializedName("name")
    @Expose
    public String boardName;

    @SerializedName("is_anonymous")
    @Expose
    public Boolean isAnonymous;

    @SerializedName("article_count")
    @Expose
    public Long articleCount;

    @SerializedName("updated_at")
    @Expose
    public String updateDate;

    @SerializedName("created_at")
    @Expose
    public String createDate;

    public Boards() {}

    public Boards(int boardUid, String tag, String boardName, Boolean isAnonymous, Long articleCount) {
        this.boardUid = boardUid;
        this.tag = tag;
        this.boardName = boardName;
        this.isAnonymous = isAnonymous;
        this.articleCount = articleCount;
    }
}
