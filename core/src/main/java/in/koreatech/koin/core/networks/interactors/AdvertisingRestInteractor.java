package in.koreatech.koin.core.networks.interactors;

import android.util.Log;

import com.google.gson.JsonObject;

import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.services.AdvertisingService;
import in.koreatech.koin.core.util.FormValidatorUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.networks.RetrofitManager.addAuthorizationBearer;

/**
 * Created by hansol on 2020.1.1...
 * Edited by seongyun on 2020. 02. 26...
 */
public class AdvertisingRestInteractor implements AdvertisingInteractor {
    private final String TAG = AdvertisingInteractor.class.getSimpleName();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AdvertisingRestInteractor() {
    }

    @Override
    public void readAdList(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(AdvertisingService.class).getAdList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Advertising>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Advertising ad) {
                        if (!ad.ads.isEmpty()) {
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
    public void updateGrantCheck(int articleId, ApiCallback apiCallback) {
        if (DefaultSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS) {
            AdDetail adDetail = new AdDetail();
            adDetail.id = articleId;
            adDetail.grantEdit = false;
            apiCallback.onSuccess(adDetail);
        } else {
            String token = DefaultSharedPreferencesHelper.getInstance().loadToken();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("article_id", articleId);
            RetrofitManager.getInstance().getRetrofit().create(AdvertisingService.class).postGrantCheck(addAuthorizationBearer(token), jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AdDetail>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(AdDetail response) {
                            if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                                AdDetail adDetail = new AdDetail();
                                adDetail.id = articleId;
                                adDetail.grantEdit = response.grantEdit;
                                apiCallback.onSuccess(adDetail);
                            } else {
                                apiCallback.onFailure(new Throwable(response.error));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (((HttpException) throwable).code() == 403) {
                                AdDetail adDetail = new AdDetail();
                                adDetail.id = articleId;
                                adDetail.grantEdit = false;
                                apiCallback.onSuccess(adDetail);
                            } else {
                                if (throwable instanceof HttpException) {
                                    Log.d(TAG, ((HttpException) throwable).code() + " ");
                                }
                                apiCallback.onFailure(throwable);
                            }
                        }

                        @Override
                        public void onComplete() {
                            compositeDisposable.dispose();
                        }
                    });
        }
    }
}
