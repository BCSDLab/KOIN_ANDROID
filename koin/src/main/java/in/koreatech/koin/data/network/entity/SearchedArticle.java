package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchedArticle implements Serializable {

    @SerializedName("totalPage")
    @Expose
    private int totalPage;

    @SerializedName("articles")
    @Expose
    private ArrayList<SearchedArticle> searchedArticles;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("table_id")
    @Expose
    private int tableId;

    @SerializedName("service_name")
    @Expose
    private String serviceName;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("hit")
    @Expose
    private int hit;

    @SerializedName("comment_count")
    @Expose
    private int commentCount;

    @SerializedName("permalink")
    @Expose
    private String permalink;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getTotalPage() {
        return totalPage;
    }

    public ArrayList<SearchedArticle> getSearchedArticles() {
        return searchedArticles;
    }

    public void setSearchedArticles(ArrayList<SearchedArticle> searchedArticles) {
        if (this.searchedArticles != null) {
            this.searchedArticles.clear();
            this.searchedArticles.addAll(searchedArticles);
        }
    }

    public int getId() {
        return id;
    }

    public int getTableId() {
        return tableId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public int getHit() {
        return hit;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}