package in.koreatech.koin.core.networks.interactors;

import in.koreatech.koin.core.networks.ApiCallback;

public interface SearchArticleInteractor {
    void readSearchArticleList(int page, int searchType, String query, ApiCallback apiCallback);
}
