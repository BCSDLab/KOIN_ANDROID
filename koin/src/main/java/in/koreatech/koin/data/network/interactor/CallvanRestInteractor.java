package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.CallvanRoom;
import in.koreatech.koin.data.network.entity.Company;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.service.CallvanService;
import in.koreatech.koin.util.FormValidatorUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.network.RetrofitManager.addAuthorizationBearer;

/**
 * 콜밴쉐어링과 관련된 api 호출 class
 * Created by hyerim on 2018. 6. 17....
 */
public class CallvanRestInteractor implements CallvanInteractor {
    private final String TAG = "CallvanRestInteractor";
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public CallvanRestInteractor() {
    }

    /**
     * 콜밴 회사 리스트 받아오는 api
     *
     * @param apiCallback api 결과를 담아 보낼 callback
     */
    @Override
    public void readCompanyList(ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).getCompanyList(addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Company>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArrayList<Company> response) {
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
//                        mCompositeDisposable.dispose();
                    }
                });

    }

    /**
     * 콜밴 회사 정보를 받아오는 api
     *
     * @param uid         콜밴 회사 id
     * @param apiCallback api 결과를 담을 callback
     */
    @Override
    public void readCompany(int uid, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).getCompany(uid, addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Company>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Company response) {
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
//                        mCompositeDisposable.dispose();
                    }
                });

    }

    /**
     * 콜밴 방 리스트 받아오는 api
     *
     * @param apiCallback api 결과를 담아 보낼 callback
     */
    @Override
    public void readRoomList(ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).getCallvanRoomsList(addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<CallvanRoom>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArrayList<CallvanRoom> response) {
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
//                        mCompositeDisposable.dispose();
                    }
                });
    }

    /**
     * 콜밴 방을 생성하는 api
     *
     * @param room        생성될 방 정보를 담은 room 파라미터
     * @param apiCallback api 결과를 담을 callback
     */
    @Override
    public void createRoom(CallvanRoom room, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).postCallvanRoom(addAuthorizationBearer(token), room)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CallvanRoom>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(CallvanRoom response) {
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
//                        mCompositeDisposable.dispose();
                    }
                });
    }

    /**
     * 콜밴 방 상세 정보를 받아오는 api
     *
     * @param roomUid     콜밴 방 id
     * @param apiCallback api 결과를 받을 callback
     */
    @Override
    public void readRoom(int roomUid, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).getCallvanRoom(roomUid, UserInfoSharedPreferencesHelper.getInstance().loadToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CallvanRoom>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(CallvanRoom response) {
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
//                        mCompositeDisposable.dispose();
                    }
                });
    }

    /**
     * 콜밴 방 정보를 업데이트하는 api
     *
     * @param room        업데이트할 방 정보를 담은 room
     * @param apiCallback api 결과를 담을 callback
     */
    @Override
    public void updateRoom(CallvanRoom room, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).putCallvanRoom(addAuthorizationBearer(token), room)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CallvanRoom>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(CallvanRoom response) {
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
//                        mCompositeDisposable.dispose();
                    }
                });
    }

    /**
     * 콜밴 방을 지우는 api
     *
     * @param roomUid     지울 콜밴 방 id
     * @param apiCallback api 결과를 받을 callback
     */
    @Override
    public void deleteRoom(int roomUid, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).deleteCallvanRoom(roomUid, addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
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

    /**
     * 해당 콜밴 방에 참가인원을 추가하는 api
     *
     * @param roomUid     참가자를 추가할 콜밴 방의 id
     * @param apiCallback api 결과를 받을 callback
     */
    @Override
    public void createParticipant(int roomUid, ApiCallback apiCallback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("room_id", roomUid);
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).postParticipant(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.getError())) {
                            apiCallback.onSuccess(roomUid);
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

    /**
     * 해당 콜밴 방의 참가인원을 감소하는 api
     *
     * @param roomUid     참가자를 감소시킬 콜밴 방의 id
     * @param apiCallback api 결과를 받을 callback
     */
    @Override
    public void deleteParticipant(int roomUid, ApiCallback apiCallback) {
        JsonObject roomId = new JsonObject();
        roomId.addProperty("room_id", roomUid);
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(CallvanService.class).deleteParticipant(addAuthorizationBearer(token), roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CallvanRoom>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(CallvanRoom response) {
                        apiCallback.onSuccess(true);

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
}
