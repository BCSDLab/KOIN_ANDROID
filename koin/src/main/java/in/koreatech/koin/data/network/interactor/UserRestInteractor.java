package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import com.google.gson.JsonObject;

import in.koreatech.koin.constant.URLConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Auth;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.network.response.AuthResponse;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.response.UserInfoEditResponse;
import in.koreatech.koin.data.network.service.UserService;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.HashGeneratorUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.network.RetrofitManager.addAuthorizationBearer;

public class UserRestInteractor implements UserInteractor {
    private final String TAG = "UserRestInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public UserRestInteractor() {
    }

    @Override
    public void readToken(String id, String password, boolean isPasswordHash, ApiCallback apiCallback) {
        final String hashPassword;

        if (!isPasswordHash) {
            hashPassword = HashGeneratorUtil.generateSHA256(password);
        } else {
            hashPassword = password;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(URLConstant.USER.ID, id);
        jsonObject.addProperty(URLConstant.USER.PW, hashPassword);

        RetrofitManager.getInstance().getRetrofit().create(UserService.class).getToken(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AuthResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(AuthResponse response) {
                        if (!FormValidatorUtil.validateStringIsEmpty(response.getToken())) {
                            Auth auth = new Auth(response.getToken(), response.getUser());
                            apiCallback.onSuccess(auth);
                        } else {
                            apiCallback.onFailure(new Throwable("로그인에 실패하였습니다"));
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable instanceof HttpException) {
                            Log.d(TAG, ((HttpException) throwable).code() + " ");
                            Log.d(TAG, ((HttpException) throwable).message());
                        }
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {
//                        compositeDisposable.dispose();
                    }
                });
    }


    @Override
    public void createToken(String email, String password, ApiCallback apiCallback) {
        final String hashPassword = HashGeneratorUtil.generateSHA256(password);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(URLConstant.USER.ID, email);
        jsonObject.addProperty(URLConstant.USER.PW, hashPassword);

        RetrofitManager.getInstance().getRetrofit().create(UserService.class).postRegister(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.getError())) {
                            apiCallback.onSuccess(true);
                        } else {
                            apiCallback.onFailure(new Throwable("인증 메일을 보내지 못하였습니다"));
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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void createFindPassword(String email, ApiCallback apiCallback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(URLConstant.USER.ID, email);

        RetrofitManager.getInstance().getRetrofit().create(UserService.class).postPasswordReset(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.getError())) {
                            apiCallback.onSuccess(true);
                        } else {
                            apiCallback.onFailure(new Throwable("초기화 메일을 보내지 못하였습니다"));
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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void readUser(ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(UserService.class).getUser(addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(User response) {
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
    public void updateUser(ApiCallback apiCallback, User user) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(UserService.class).putUser(addAuthorizationBearer(token), user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(User response) {
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
    public void deleteUser(ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(UserService.class).deleteUser(addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse response) {
                        apiCallback.onSuccess(response);
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
    public void readCheckUserNickName(String nickname, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(UserService.class).checkNickName(nickname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfoEditResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(UserInfoEditResponse response) {
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