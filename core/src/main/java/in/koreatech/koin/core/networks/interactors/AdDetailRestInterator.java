package in.koreatech.koin.core.networks.interactors;

import android.util.Log;

import com.google.gson.JsonObject;

import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.services.AdvertisingService;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.ToastUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.networks.RetrofitManager.addAuthorizationBearer;

/**
 * Created by hansol on 2020.1.3...
 */
public class AdDetailRestInterator implements AdDetailInterator {

    private final String TAG = AdvertisingInteractor.class.getSimpleName();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void createAdDetail(AdDetail ad, ApiCallback apiCallback) {
        String token = DefaultSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", ad.title);
        jsonObject.addProperty("event_title", ad.eventTitle);
        jsonObject.addProperty("content", ad.content);
        jsonObject.addProperty("shop_id", ad.shopId);
        jsonObject.addProperty("start_date", ad.startDate);
        jsonObject.addProperty("end_date", ad.endDate);
        if (ad.thumbnail != null)
            jsonObject.addProperty("thumbnail", ad.thumbnail);

        RetrofitManager.getInstance().getRetrofit().create(AdvertisingService.class).postAdDetail(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AdDetail>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(AdDetail response) {
                        if (response != null) {
//                            if(FormValidatorUtil.validateStringIsEmpty(response.eventTitle))
//                                response.eve
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

    @Override
    public void updateAdDetail(int articleId, AdDetail adDetail, ApiCallback apiCallback) {
        String token = DefaultSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", adDetail.title);
        jsonObject.addProperty("event_title", adDetail.eventTitle);
        jsonObject.addProperty("content", adDetail.content);
        jsonObject.addProperty("start_date", adDetail.startDate);
        jsonObject.addProperty("end_date", adDetail.endDate);
        RetrofitManager.getInstance().getRetrofit().create(AdvertisingService.class).updateAdDetail(articleId, addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AdDetail>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AdDetail adDetail) {
                        if (adDetail != null)
                            apiCallback.onSuccess(adDetail);
                        else
                            apiCallback.onFailure(new Throwable("Fail"));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(throwable instanceof HttpException){
                            Log.d(TAG, ((HttpException) throwable).code() + " ");
                        }
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.dispose();
                    }
                });
    }
}
