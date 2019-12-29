package in.koreatech.koin.core.networks.interactors;

import android.util.Log;

import java.util.ArrayList;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.core.networks.responses.StoresResponse;
import in.koreatech.koin.core.networks.services.ShopService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class StoreRestInteractor implements StoreInteractor {
    private final String TAG = StoreRestInteractor.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public StoreRestInteractor() {
    }

    @Override
    public void readStoreList(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(ShopService.class).getShopList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StoresResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StoresResponse stores) {
                        if (!stores.storeArrayList.isEmpty()) {
                            apiCallback.onSuccess(stores);
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

                    }
                });
    }

    @Override
    public void readStore(int id, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(ShopService.class).getStore(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Store>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Store store) {
                        if (store != null) {
                            apiCallback.onSuccess(store);
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
