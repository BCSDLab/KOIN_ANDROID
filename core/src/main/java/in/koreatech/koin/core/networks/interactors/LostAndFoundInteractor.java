package in.koreatech.koin.core.networks.interactors;

import java.io.File;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.LostAndFound;
import in.koreatech.koin.core.networks.entity.LostItem;

public interface LostAndFoundInteractor {
    void readLostAndFoundList(int limit, int page, final ApiCallback apiCallback);

    void readLostAndFoundDetail(int id, final ApiCallback apiCallback);

    void readGrantedDetail(int id, final ApiCallback apiCallback);

    void createCommentDetail(int id, String content, final ApiCallback apiCallback);

    void deleteCommentDetail(int itemId, int commentId, final ApiCallback apiCallback);

    void editCommentDetail(int itemId, int commentId, String content, final ApiCallback apiCallback);

    void editCotentEdit(int id, LostItem lostItem, final ApiCallback apiCallback);

    void uploadImage(File file, final ApiCallback apiCallback);

    void createLostAndFoundItem(LostItem lostItem, final ApiCallback apiCallback);

    void deleteLostAndFoundItem(int id, final ApiCallback apiCallback);
}
