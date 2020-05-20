package in.koreatech.koin.data.network.interactor;

import in.koreatech.koin.core.network.ApiCallback;

public interface SearchArticleInteractor {
    void readSearchArticleList(int page, int searchType, String query, ApiCallback apiCallback);

    void saveSearchText(String text, ApiCallback apiCallback);

    void loadSavedSearchText(ApiCallback apiCallback);
}