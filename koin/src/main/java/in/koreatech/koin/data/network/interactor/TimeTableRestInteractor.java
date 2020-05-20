package in.koreatech.koin.data.network.interactor;


import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.service.TimeTableService;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;

import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.util.SaveManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static in.koreatech.koin.core.network.RetrofitManager.addAuthorizationBearer;

public class TimeTableRestInteractor implements TimeTableInteractor {
    private final String TAG = "TimeTableRestInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
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
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
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

    @Override
    public void readTimeTableFromLocal(String semester, ApiCallback apiCallback) {
        Observable.just(SaveManager.loadTimeTable(TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable(semester)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeTable>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(TimeTable timeTable) {
                        apiCallback.onSuccess(timeTable);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiCallback.onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void editTimeTableItemAtLocal(TimeTable.TimeTableItem timeTableItem, String semester, ApiCallback apiCallback) {
        Observable.just(SaveManager.loadTimeTable(TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable(semester)))
                .map(savedTimeTable ->
                        {
                            if (savedTimeTable.getTimeTableItems() != null) {
                                int id = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTableBlockID();
                                if (id == Integer.MAX_VALUE) id = -1;
                                timeTableItem.setId(++id);
                                TimeTableSharedPreferencesHelper.getInstance().saveTimeTableBlockID(id);
                                savedTimeTable.addTimeTableItem(timeTableItem);
                                TimeTableSharedPreferencesHelper.getInstance().saveTimeTable(semester, SaveManager.saveTimeTable(savedTimeTable, semester));
                                return savedTimeTable;
                            } else
                                return null;
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeTable>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(TimeTable timeTable) {
                        apiCallback.onSuccess(timeTable);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiCallback.onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void deleteTimeTableItemAtLocal(String semester, int id, ApiCallback apiCallback) {
        Observable.just(SaveManager.loadTimeTable(TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable(semester)))
                .map(savedTimeTable ->
                        {   TimeTable saveTable = new TimeTable();
                            if (savedTimeTable.getTimeTableItems() != null) {
                                saveTable.setSemester(savedTimeTable.getSemester());
                                for (TimeTable.TimeTableItem timeTableItem : savedTimeTable.getTimeTableItems()) {
                                    if (timeTableItem.getId() != id) {
                                        saveTable.addTimeTableItem(timeTableItem);
                                    }
                                }
                                TimeTableSharedPreferencesHelper.getInstance().saveTimeTable(semester, SaveManager.saveTimeTable(saveTable, saveTable.getSemester()));
                                return SaveManager.loadTimeTable(TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable(semester));
                            } else
                                return null;
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeTable>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(TimeTable timeTable) {
                        apiCallback.onSuccess(timeTable);
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiCallback.onFailure(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
