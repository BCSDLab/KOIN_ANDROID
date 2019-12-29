package in.koreatech.koin.core.networks.interactors;

import android.util.Log;
import java.util.ArrayList;

import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;
import in.koreatech.koin.core.networks.entity.Boards;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.core.networks.entity.Land;
import in.koreatech.koin.core.networks.services.CommunityService;
import in.koreatech.koin.core.networks.services.LandService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


public class LandRestInteractor implements LandInteractor {
    private final String TAG = LandRestInteractor.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void readLandDetail(int landId, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(LandService.class).getLandDetail(landId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Land>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Land land) {
                        if(land != null){
                            apiCallback.onSuccess(land);
                        }
                        else{
                            apiCallback.onFailure(new Throwable("Fail to find Land"));
                        }
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
                        mCompositeDisposable.dispose();
                    }
                });
    }
                    

}
