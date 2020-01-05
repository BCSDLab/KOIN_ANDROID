package in.koreatech.koin.core.networks.interactors;

import android.util.Log;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.services.AdvertisingService;
import in.koreatech.koin.core.util.ToastUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by hansol on 2020.1.3...
 */
public class AdDetailRestInterator implements AdDetailInterator {

    private final String TAG = AdvertisingInteractor.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public AdDetailRestInterator() {
    }

    @Override
    public void readAdDetailList(int id, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(AdvertisingService.class).getAdDetailList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AdDetail>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(AdDetail ad) {
                        if (ad != null) {
                            apiCallback.onSuccess(ad);
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
