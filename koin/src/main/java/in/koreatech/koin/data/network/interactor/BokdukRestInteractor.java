package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.BokdukRoom;
import in.koreatech.koin.data.network.service.LandService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class BokdukRestInteractor implements BokdukInteractor {
    private final String TAG = LandRestInteractor.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BokdukRestInteractor() {
    }

    @Override
    public void readBokdukList(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(LandService.class).getLandList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BokdukRoom>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(BokdukRoom land) {
                        if (!land.lands.isEmpty()) {
                            apiCallback.onSuccess(land);
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
                        mCompositeDisposable.dispose();
                    }
                });

    }
}
