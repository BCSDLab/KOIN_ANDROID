package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.service.SearchArticleService;
import in.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class SearchArticleRestInteractor implements SearchArticleInteractor {
    public static final int MAX_SAVE_SIZE = 20;
    private final String TAG = "SearchArticleInteractor";

    public SearchArticleRestInteractor() {
    }

    /**
     * article/search api로부터 정보를 받아오는 함수
     *
     * @param page        페이지 숫자
     * @param searchType  검색 타입으로 0 - 제목, 1 - 제목 + 내용, 2 - 작성자
     * @param query       검색한 단어
     * @param apiCallback
     */
    @Override
    public void readSearchArticleList(int page, int searchType, String query, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(SearchArticleService.class).getSearchedArticles(page, searchType, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchedArticle>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchedArticle searchedArticle) {
                        if (!searchedArticle.getSearchedArticles().isEmpty()) {
                            apiCallback.onSuccess(searchedArticle);
                        } else {
                            apiCallback.onFailure(new Throwable("서버와의 연결이 불안정합니다"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable instanceof HttpException) {
                            Log.d(TAG, ((HttpException) throwable).code() + " ");
                        }
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void saveSearchText(String text, ApiCallback apiCallback) {
        try {
            if (RecentSearchSharedPreference.getInstance().getRecentSearch().isEmpty()) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(text.trim());
                RecentSearchSharedPreference.getInstance().saveRecentSearch(arrayList);
                apiCallback.onSuccess(text);
            } else {
                ArrayList<String> arrayList = RecentSearchSharedPreference.getInstance().getRecentSearch();
                if (arrayList.size() >= MAX_SAVE_SIZE) arrayList.remove(arrayList.size() - 1);
                arrayList.remove(text);// 공통된 문자가 있으면 지워준다.
                arrayList.add(0, text.trim());
                RecentSearchSharedPreference.getInstance().saveRecentSearch(arrayList);
                apiCallback.onSuccess(text);
            }
        } catch (Exception exception) {
            apiCallback.onFailure(exception);
        }
    }
}