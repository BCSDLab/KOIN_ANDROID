package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.data.network.service.VersionService;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.ui.timetable.presenter.TimetablePresenter;
import in.koreatech.koin.util.FormValidatorUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class AppVersionRestInteractor implements AppVersionInteractor {

    private final String TAG = "AppVersionRest";

    @Override
    public void readAppVersion(String code, ApiCallback apiCallback) {
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
                            if (code.equals(TimetablePresenter.TIMETABLE_SERVICE_CODE)) {
                                String version = TimeTableSharedPreferencesHelper.getInstance().loadTimeTableVersion();
                                version = (FormValidatorUtil.validateStringIsEmpty(version)) ? "" : version;
                                response.setLocalVersion(version);
                                apiCallback.onSuccess(response);
                                TimeTableSharedPreferencesHelper.getInstance().saveTimeTableVersion(response.getVersion());
                                return;
                            }
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable("fail read app version"));
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
