package in.koreatech.koin.core.networks.interactors;

import android.util.Log;

import java.util.ArrayList;

import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.Company;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.entity.Version;
import in.koreatech.koin.core.networks.services.CallvanService;
import in.koreatech.koin.core.networks.services.MarketService;
import in.koreatech.koin.core.networks.services.VersionService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.networks.RetrofitManager.addAuthorizationBearer;

public class AppVersionRestInteractor implements AppVersionInteractor {

    private final String TAG = AppVersionRestInteractor.class.getName();

    @Override
    public void readAppVersion(String code,ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(VersionService.class).getVersion(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Version>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Version response) {
                        if (response != null) {
                            apiCallback.onSuccess(response);
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
