package in.koreatech.koin.data.network.interactor;


import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.KoinApplication;
import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.service.TimeTableService;
import in.koreatech.koin.data.repository.TokenRepositoryImpl;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;

import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.source.local.TokenLocalDataSource;
import in.koreatech.koin.domain.repository.TokenRepository;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static in.koreatech.koin.core.network.RetrofitManager.addAuthorizationBearer;

import javax.inject.Inject;

public class TimeTableRestInteractor implements TimeTableInteractor {
    private final String TAG = "TimeTableRestInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final TokenRepository tokenRepository;

    public TimeTableRestInteractor(KoinApplication application) {
        tokenRepository = application.getTokenRepository();
    }

    @Override
    public void deleteSemesterTimeTable(ApiCallback apiCallback, String semester) {
        String token = tokenRepository.getAccessTokenBlocking();
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
                            apiCallback.onFailure(new Throwable("fail delete semester time table"));
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {
//                        compositeDisposable.dispose();
                    }
                });

    }

    @Override
    public void deleteTimeTable(ApiCallback apiCallback, int id) {
        String token = tokenRepository.getAccessTokenBlocking();
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
                            apiCallback.onFailure(new Throwable("fail delete timetable"));
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {
//                        compositeDisposable.dispose();
                    }
                });

    }

    @Override
    public void readTimeTable(ApiCallback apiCallback, String semester) {
        String token = tokenRepository.getAccessTokenBlocking();
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
                            apiCallback.onFailure(new Throwable("fail read time table"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void createTimeTable(ApiCallback apiCallback, JsonObject jsonObject) {
        String token = tokenRepository.getAccessTokenBlocking();
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).postTimeTables(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeTable>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(TimeTable response) {
                        if (response != null) {
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable("fail create timetable"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void editTimeTable(ApiCallback apiCallback, JsonObject jsonObject) {
        String token = tokenRepository.getAccessTokenBlocking();
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).putEditTimeTables(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<TimeTable>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArrayList<TimeTable> response) {
                        if (response != null) {
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable("fail edit time table"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void readSemesters(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(TimeTableService.class).getSemesters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Semester>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArrayList<Semester> response) {
                        if (response != null) {
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable("fail read semester"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
