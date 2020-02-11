package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.service.SearchArticleService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class SearchArticleRestInteractor implements SearchArticleInteractor {

    private final String TAG = "SearchArticleRestInteractor";

    public SearchArticleRestInteractor() {
    }

    /**
     * article/search api로부터 정보를 받아오는 함수
     * @param page 페이지 숫자
     * @param searchType 검색 타입으로 0 - 제목, 1 - 제목 + 내용, 2 - 작성자
     * @param query 검색한 단어
     * @param apiCallback
     */
    @Override
    public void readSearchArticleList(int page, int searchType, String query, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(SearchArticleService.class).getSearchedArticles(page,searchType,query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchedArticle>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchedArticle searchedArticle) {
                        if (!searchedArticle.searchedArticles.isEmpty()) {
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
}
