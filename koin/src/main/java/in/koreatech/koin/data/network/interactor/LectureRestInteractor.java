package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.service.LectureService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LectureRestInteractor implements LectureInteractor {

    private final String TAG = "LectureRestInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LectureRestInteractor() {
    }

    @Override
    public void readLecture(String semester, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(LectureService.class).getLectureList(semester)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Lecture>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(ArrayList<Lecture> response) {
                        if (!response.isEmpty()) {
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
