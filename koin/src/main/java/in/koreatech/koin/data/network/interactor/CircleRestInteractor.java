package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.service.CircleService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


public class CircleRestInteractor implements CircleInteractor {
    private final String TAG = CircleRestInteractor.class.getSimpleName();

    public CircleRestInteractor() {
    }

    @Override
    public void readCircleList(int page, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CircleService.class).getCircleList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Circle>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Circle circle) {
                        if (!circle.circles.isEmpty()) {
                            apiCallback.onSuccess(circle);
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
    public void readCircle(int id, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CircleService.class).getCircle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Circle>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Circle circle) {
                        if (circle != null) {
                            apiCallback.onSuccess(circle);
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

}
