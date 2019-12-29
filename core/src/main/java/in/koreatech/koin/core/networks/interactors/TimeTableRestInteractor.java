package in.koreatech.koin.core.networks.interactors;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.RetrofitManager;

import in.koreatech.koin.core.networks.entity.TimeTable;
import in.koreatech.koin.core.networks.responses.DefaultResponse;

import in.koreatech.koin.core.networks.services.TimeTableService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.networks.RetrofitManager.addAuthorizationBearer;

public class TimeTableRestInteractor implements TimeTableInteractor {
    private final String TAG = TimeTableRestInteractor.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public TimeTableRestInteractor() {
    }

    @Override
    public void deleteSemesterTimeTable(ApiCallback apiCallback, String semester) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).deleteTimeTables(addAuthorizationBearer(token), semester)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(DefaultResponse response) {
                        if (response != null) {
                            apiCallback.onSuccess(true);
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
//                        mCompositeDisposable.dispose();
                    }
                });

    }

    @Override
    public void deleteTimeTable(ApiCallback apiCallback, int id) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).deleteTimeTable(addAuthorizationBearer(token), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(DefaultResponse response) {
                        if (response != null) {
                            apiCallback.onSuccess(true);
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
//                        mCompositeDisposable.dispose();
                    }
                });

    }

    @Override
    public void readTimeTable(ApiCallback apiCallback, String semester) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).getTimeTablesList(addAuthorizationBearer(token), semester)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeTable>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(TimeTable response) {
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


    @Override
    public void createTimeTable(ApiCallback apiCallback, JsonObject jsonObject) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).postTimeTables(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeTable>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(TimeTable response) {
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

    @Override
    public void editTimeTable(ApiCallback apiCallback, JsonObject jsonObject) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).putEditTimeTables(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<TimeTable>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArrayList<TimeTable> response) {
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
