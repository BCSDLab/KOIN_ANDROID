package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Comment;

/**
 * Created by hansol on 2020.1.3...
 */
public interface AdDetailInterator {
    void readAdDetailList(int id, final ApiCallback apiCallback);

    void createAdDetail(AdDetail ad, final ApiCallback apiCallback);

    void updateAdDetail(int articleId, AdDetail ad, final ApiCallback apiCallback);

    void deleteAdDetail(int articleId, final ApiCallback apiCallback);

    // AdDetail Comment
    void createAdDetailComment(int articleId, Comment comment, final ApiCallback apiCallback);

    void updateAdDetailComment(Comment comment, final ApiCallback apiCallback);

    void deleteAdDetailComment(int articleId, int commentId, final ApiCallback apiCallback);
}
