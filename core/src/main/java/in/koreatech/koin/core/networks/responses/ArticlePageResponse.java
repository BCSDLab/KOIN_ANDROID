package in.koreatech.koin.core.networks.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.entity.Article;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class ArticlePageResponse {

    @SerializedName("articles")
    @Expose
    public ArrayList<Article> articleArrayList;

    @SerializedName("totalPage")
    @Expose
    public int totalPage;

    public ArticlePageResponse(ArrayList<Article> articleArrayList, int totalPage) {
        this.articleArrayList = articleArrayList;
        this.totalPage = totalPage;
    }

    @SerializedName("code")
    @Expose
    private String code;
}
