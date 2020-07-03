package in.koreatech.koin.data.network.interactor;

import java.io.File;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.MarketItem;

public interface MarketUsedInteractor {
    void readMarketList(int type, int limit, final ApiCallback apiCallback);

    void readMarketDetail(int id, final ApiCallback apiCallback);

    void readGrantedDetail(int id, final ApiCallback apiCallback);

    void createCommentDetail(int id, String content, final ApiCallback apiCallback);

    void deleteCommentDetail(int itemId, int commentId, final ApiCallback apiCallback);

    void editCommentDetail(int itemId, int commentId, String content, final ApiCallback apiCallback);

    void editCotentEdit(int id, MarketItem marketItem, final ApiCallback apiCallback);

    void uploadThumbnailImage(File file, final ApiCallback apiCallback);

    void uploadImage(File file, final ApiCallback apiCallback);

    void uploadImage(File file, String uuid, final ApiCallback apiCallback);

    void createMarketItem(MarketItem marketItem, final ApiCallback apiCallback);

    void deleteMarketItem(int id, final ApiCallback apiCallback);
}