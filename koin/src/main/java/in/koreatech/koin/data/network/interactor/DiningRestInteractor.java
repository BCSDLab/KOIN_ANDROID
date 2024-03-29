package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.service.DiningService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class DiningRestInteractor implements DiningInteractor {
    private final String TAG = "DiningRestInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DiningRestInteractor() {
    }

    @Override
    public void readDiningList(String date, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(DiningService.class).getDiningMenu(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Dining>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Dining> dinings) {
                        if (!dinings.isEmpty()) {
                            apiCallback.onSuccess(dinings);
                        } else {
                            apiCallback.onFailure(new Throwable("fail read dining list"));
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
    public Observable<ArrayList<Dining>> readDingingList(String date) {
        return RetrofitManager.getInstance().getRetrofit().create(DiningService.class).getDiningMenu(date);
    }
}
