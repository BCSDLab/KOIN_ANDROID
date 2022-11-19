package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.service.BusService;
import in.koreatech.koin.data.response.bus.BusResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class BusRestInteractor implements BusInteractor {
    private final String TAG = "CityBusRestInteractor";

    public BusRestInteractor() {
    }

    @Override
    public void readCityBusList(ApiCallback apiCallback, String depart, String arrival) {
        RetrofitManager.getInstance().getRetrofit().create(BusService.class).getBusList("city", depart, arrival)
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
                            apiCallback.onFailure(new Throwable("fail read city bus list"));
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
    public void readDaesungBusList(ApiCallback apiCallback, String depart, String arrival) {

        RetrofitManager.getInstance().getRetrofit().create(BusService.class).getBusList("express", depart, arrival)
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
                            apiCallback.onFailure(new Throwable("fail read express bus list"));
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
    public void readShuttleBusList(ApiCallback apiCallback, String depart, String arrival) {

        RetrofitManager.getInstance().getRetrofit().create(BusService.class).getBusList("shuttle", depart, arrival)
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
                            apiCallback.onFailure(new Throwable("fail read shuttle bus list"));
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