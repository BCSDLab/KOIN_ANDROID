package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.File;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.response.GrantCheckResponse;
import in.koreatech.koin.data.network.response.LostAndFoundPageResponse;
import in.koreatech.koin.data.network.service.LostAndFoundService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

import static in.koreatech.koin.core.network.RetrofitManager.addAuthorizationBearer;

public class LostAndFoundRestInteractor implements LostAndFoundInteractor {
    private final String TAG = "LostAndFoundInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void readLostAndFoundList(int limit, int page, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).getLostItemList(limit, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LostAndFoundPageResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(LostAndFoundPageResponse response) {
                        if (!response.lostItemArrayList.isEmpty()) {
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
    public void readLostAndFoundDetail(int id, ApiCallback apiCallback) {
        Observable<LostItem> observable;
        if (UserInfoSharedPreferencesHelper.getInstance().loadUser() == null)
            observable = RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).getLostItemDetail(id);
        else {
            String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
            observable = RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).getLostItemDetail(id, addAuthorizationBearer(token));
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LostItem>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(LostItem response) {
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
    public void readGrantedDetail(int id, ApiCallback apiCallback) {
        if (UserInfoSharedPreferencesHelper.getInstance().loadUser() == null) {
            GrantCheckResponse grantCheckResponse = new GrantCheckResponse();
            grantCheckResponse.isGrantEdit = false;
            apiCallback.onSuccess(grantCheckResponse);
            return;
        }
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("lostItem_id", id);

        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).postLostItemGrantedCheck(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GrantCheckResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(GrantCheckResponse response) {
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
    public void createCommentDetail(int id, String content, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", content);
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).postLostItemComment(id, addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
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
    public void deleteCommentDetail(int itemId, int commentId, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).deleteLostItemComment(itemId, commentId, addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse response) {
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
    public void editCommentDetail(int itemId, int commentId, String content, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", content);
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).putLostItemComment(itemId, commentId, addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
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
    public void editCotentEdit(int id, LostItem lostItem, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).putLostAndFoundContent(id, addAuthorizationBearer(token), lostItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LostItem>() {

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(LostItem response) {
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
    public void uploadImage(File file, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).postLostItemImage(addAuthorizationBearer(token), filePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LostItem>() {

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(LostItem response) {
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
    public void createLostAndFoundItem(LostItem lostItem, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).postLostItem(addAuthorizationBearer(token), lostItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LostItem>() {

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(LostItem response) {
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
    public void deleteLostAndFoundItem(int id, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(LostAndFoundService.class).deleteLostItem(id, addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse response) {
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
