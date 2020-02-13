package in.koreatech.koin.core.networks.interactors;

import android.util.Log;

import com.google.gson.JsonObject;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.Bus;
import in.koreatech.koin.core.networks.entity.CityBus;
import in.koreatech.koin.core.networks.entity.Term;
import in.koreatech.koin.core.networks.responses.BusResponse;
import in.koreatech.koin.core.networks.services.CityBusService;
import in.koreatech.koin.core.networks.services.TermService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;

/**
 * Created by hyerim on 2018. 8. 13....
 */
public class CityBusRestInteractor implements CityBusInteractor {
    private final String TAG = CityBusRestInteractor.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public CityBusRestInteractor() {
    }

    @Override
    public void readCityBusList(ApiCallback apiCallback, String depart, String arrival) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();

        Observable<BusResponse> busResponseObservable = retrofit.create(CityBusService.class).getBusList(depart, arrival)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        Observable<Term> termObservable = retrofit.create(TermService.class).getTerm()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<Bus> TermAndBusResponse = Observable.zip(termObservable, busResponseObservable, new BiFunction<Term, BusResponse, Bus>() {
            @Override
            public Bus apply(Term term, BusResponse busResponse) throws Exception {
                return new Bus(term, busResponse);
            }

        });

        TermAndBusResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }


                    @Override
                    public void onNext(Bus bus) {
                        if (bus != null) {
                            apiCallback.onSuccess(bus);
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
        /*
        RetrofitManager.getInstance().getRetrofit().create(CityBusService.class).getBusList(depart,arrival)
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

         */
    }

}
