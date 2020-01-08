package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.response.BusResponse;
import in.koreatech.koin.data.network.service.CityBusService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by hyerim on 2018. 8. 13....
 */
public class CityBusRestInteractor implements CityBusInteractor {
    private final String TAG = "CityBusRestInteractor";

    public CityBusRestInteractor() {
    }

    @Override
    public void readCityBusList(ApiCallback apiCallback, String depart, String arrival) {
        RetrofitManager.getInstance().getRetrofit().create(CityBusService.class).getBusList(depart, arrival)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BusResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BusResponse busResponse) {
                        if (busResponse != null) {
                            apiCallback.onSuccess(busResponse);
                        } else {
                            apiCallback.onFailure(new Throwable("fail"));
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
