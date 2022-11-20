package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.service.BusService;
import in.koreatech.koin.data.response.bus.BusResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class BusRestInteractor implements BusInteractor {
    private final String TAG = "CityBusRestInteractor";

    public BusRestInteractor() {
    }

    @Override
    public Disposable readCityBusList(ApiCallback apiCallback, String depart, String arrival) {
        return RetrofitManager.getInstance().getRetrofit().create(BusService.class).getBusList("city", depart, arrival)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        busResponse -> {
                            if (busResponse != null) {
                                apiCallback.onSuccess(busResponse);
                            } else {
                                apiCallback.onFailure(new Throwable("fail read city bus list"));
                            }
                        },
                        throwable -> {
                            if (throwable instanceof HttpException) {
                                Log.d(TAG, ((HttpException) throwable).code() + " ");
                            }
                            apiCallback.onFailure(throwable);
                        }
                );
    }

    @Override
    public Disposable readDaesungBusList(ApiCallback apiCallback, String depart, String arrival) {
        return RetrofitManager.getInstance().getRetrofit().create(BusService.class).getBusList("express", depart, arrival)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        busResponse -> {
                            if (busResponse != null) {
                                apiCallback.onSuccess(busResponse);
                            } else {
                                apiCallback.onFailure(new Throwable("fail read express bus list"));
                            }
                        },
                        throwable -> {
                            if (throwable instanceof HttpException) {
                                Log.d(TAG, ((HttpException) throwable).code() + " ");
                            }
                            apiCallback.onFailure(throwable);
                        }
                );
    }

    @Override
    public Disposable readShuttleBusList(ApiCallback apiCallback, String depart, String arrival) {
        return RetrofitManager.getInstance().getRetrofit().create(BusService.class).getBusList("shuttle", depart, arrival)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        busResponse -> {
                            if (busResponse != null) {
                                apiCallback.onSuccess(busResponse);
                            } else {
                                apiCallback.onFailure(new Throwable("fail read shuttle bus list"));
                            }
                        },
                        throwable -> {
                            if (throwable instanceof HttpException) {
                                Log.d(TAG, ((HttpException) throwable).code() + " ");
                            }
                            apiCallback.onFailure(throwable);
                        }
                );
    }

}