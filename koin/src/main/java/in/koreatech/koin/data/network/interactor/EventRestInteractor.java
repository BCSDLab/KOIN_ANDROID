package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.service.EventService;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.util.FormValidatorUtil;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.network.RetrofitManager.addAuthorizationBearer;

public class EventRestInteractor implements EventInteractor {
    private final String TAG = "EventRestInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public EventRestInteractor() {
    }

    @Override
    public void readEventList(int page, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getEventList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if (FormValidatorUtil.validateStringIsEmpty(event.getError())) {
                            apiCallback.onSuccess(event);
                        } else {
                            apiCallback.onFailure(new Throwable("홍보 게시물을 받아오지 못했습니다."));
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void updateGrantCheck(int articleId, ApiCallback apiCallback) {
        if (UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS) {
            Event adDetail = new Event();
            adDetail.setId(articleId);
            adDetail.setGrantEdit(false);
            apiCallback.onSuccess(adDetail);
        } else {
            String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("article_id", articleId);
            RetrofitManager.getInstance().getRetrofit().create(EventService.class).postGrantCheck(addAuthorizationBearer(token), jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Event>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Event response) {
                            if (FormValidatorUtil.validateStringIsEmpty(response.getError())) {
                                Event eventDetail = new Event();
                                eventDetail.setId(articleId);
                                eventDetail.setGrantEdit(response.isGrantEdit());
                                apiCallback.onSuccess(eventDetail);
                            } else {
                                apiCallback.onFailure(new Throwable(response.getError()));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (((HttpException) throwable).code() == 403) {
                                Event eventDetail = new Event();
                                eventDetail.setId(articleId);
                                eventDetail.setGrantEdit(false);
                                apiCallback.onSuccess(eventDetail);
                            } else {
                                if (throwable instanceof HttpException) {
                                    Log.d(TAG, ((HttpException) throwable).code() + " ");
                                }
                                apiCallback.onFailure(throwable);
                            }
                        }

                        @Override
                        public void onComplete() {
                            compositeDisposable.dispose();
                        }
                    });
        }
    }

    @Override
    public void readMyShopList(ApiCallback apiCallback) {
        if(UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS) {
            Event event = new Event();
            apiCallback.onSuccess(event);
        } else {
            String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
            RetrofitManager.getInstance().getRetrofit().create(EventService.class).getMyShopList(addAuthorizationBearer(token))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Event>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Event event) {
                            if(FormValidatorUtil.validateStringIsEmpty(event.getError())) {
                                apiCallback.onSuccess(event);
                            } else {
                                apiCallback.onFailure(new Throwable("보유 상점 목록을 받아오지 못했습니다."));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if(((HttpException) throwable).code() == 403) {
                                Event event = new Event();
                                apiCallback.onSuccess(event);
                            } else {
                                if (throwable instanceof HttpException) {
                                    Log.d(TAG, ((HttpException) throwable).code() + " ");
                                }
                                apiCallback.onFailure(throwable);
                            }
                        }

                        @Override
                        public void onComplete() {
                            compositeDisposable.dispose();
                        }
                    });
        }
    }

    @Override
    public void readPendingEventList(int page, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getPendingEventList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if(FormValidatorUtil.validateStringIsEmpty(event.getError())) {
                            apiCallback.onSuccess(event);
                        } else {
                            apiCallback.onFailure(new Throwable("홍보 게시물을 받아오지 못했습니다."));
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
    public void readClosedEventList(int page, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getClosedEventList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if(FormValidatorUtil.validateStringIsEmpty(event.getError())) {
                            apiCallback.onSuccess(event);
                        } else {
                            apiCallback.onFailure(new Throwable("홍보 게시물을 받아오지 못했습니다."));
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void readMyPendingEvent(ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getMyPendingEvent(addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if(FormValidatorUtil.validateStringIsEmpty(event.getError())) {
                            apiCallback.onSuccess(event);
                        } else {
                            apiCallback.onFailure(new Throwable("진행 중인 이벤트가 없습니다."));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(throwable instanceof HttpException) {
                            Log.d(TAG, ((HttpException) throwable).code() + " ");
                        }
                        apiCallback.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void readPendingRandomEvent(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getRandomEventList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if(FormValidatorUtil.validateStringIsEmpty(event.getError())) {
                            apiCallback.onSuccess(event);
                        } else {
                            apiCallback.onFailure(new Throwable("진행 중인 이벤트를 받아오지 못했습니다."));
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void readEventDetail(int id, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        if(token == null)
            token = "";
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getEventDetail(addAuthorizationBearer(token), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if (FormValidatorUtil.validateStringIsEmpty(event.getError())) {
                            apiCallback.onSuccess(event);
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void createEvent(Event event, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", event.getTitle());
        jsonObject.addProperty("event_title", event.getEventTitle());
        jsonObject.addProperty("content", event.getContent());
        jsonObject.addProperty("shop_id", event.getShopId());
        jsonObject.addProperty("start_date", event.getStartDate());
        jsonObject.addProperty("end_date", event.getEndDate());
        if (event.getThumbnail() != null)
            jsonObject.addProperty("thumbnail", event.getThumbnail());

        RetrofitManager.getInstance().getRetrofit().create(EventService.class).postEvent(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Event response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.getError())) {
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable("fail"));
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(((HttpException)throwable).code() == 412) {
                            apiCallback.onFailure(new Throwable("이미 진행 중인 이벤트가 있습니다."));
                        } else {
                            if (throwable instanceof HttpException) {
                                Log.d(TAG, ((HttpException) throwable).code() + " ");
                            }
                            apiCallback.onFailure(throwable);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void updateEvent(int articleId, Event adDetail, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", adDetail.getTitle());
        jsonObject.addProperty("event_title", adDetail.getEventTitle());
        jsonObject.addProperty("content", adDetail.getContent());
        jsonObject.addProperty("start_date", adDetail.getStartDate());
        jsonObject.addProperty("end_date", adDetail.getEndDate());
        jsonObject.addProperty("shop_id", adDetail.getShopId());
        jsonObject.addProperty("thumbnail", (adDetail.getThumbnail() == null) ? "" : adDetail.getThumbnail());
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).updateEvent(articleId, addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if (FormValidatorUtil.validateStringIsEmpty(event.getError()))
                            apiCallback.onSuccess(event);
                        else
                            apiCallback.onFailure(new Throwable(event.getError()));
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void deleteEvent(int articleId, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).deleteEvent(articleId, addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DefaultResponse defaultResponse) {
                        if (FormValidatorUtil.validateStringIsEmpty(defaultResponse.getError())) {
                            apiCallback.onSuccess(defaultResponse);
                        } else {
                            apiCallback.onFailure(new Throwable("Fail"));
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void createEventComment(int articleId, Comment comment, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", comment.getContent());
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).postEventComment(articleId, addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Comment comment) {
                        if (FormValidatorUtil.validateStringIsEmpty(comment.getError())) {
                            comment.setArticleUid(articleId);
                            apiCallback.onSuccess(comment);
                        } else {
                            apiCallback.onFailure(new Throwable(comment.getError()));
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void updateEventComment(Comment comment, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", comment.getContent());
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).updateEventComment(comment.getArticleUid(), comment.getCommentUid(), addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Comment response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.getError())) {
                            response.setArticleUid(comment.getArticleUid());
                            response.setCommentUid(comment.getCommentUid());
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.getError()));
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void deleteEventComment(int articleId, int commentId, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).deleteEventComment(articleId, commentId, addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DefaultResponse response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.getError())) {
                            Comment comment = new Comment();
                            comment.setArticleUid(articleId);
                            apiCallback.onSuccess(comment);
                        } else {
                            apiCallback.onFailure(new Throwable(response.getError()));
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
                        compositeDisposable.dispose();
                    }
                });
    }
}