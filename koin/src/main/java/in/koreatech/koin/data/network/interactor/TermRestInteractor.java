package in.koreatech.koin.data.network.interactor;


import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.service.TermService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TermRestInteractor implements TermInteractor {
    private final String TAG = "TermInteractor";
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void readTerm(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(TermService.class).getTerm()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Term>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Term term) {
                        if (term != null) {
                            apiCallback.onSuccess(term);
                        } else {
                            apiCallback.onFailure(new Throwable("fail to read term"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
