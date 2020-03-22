package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import com.google.gson.JsonObject;

import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.service.EventService;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.util.FormValidatorUtil;
import io.reactivex.Observer;
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
    public void readEventList(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getAdList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if (!event.getEventArrayList().isEmpty()) {
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
                                Event adDetail = new Event();
                                adDetail.setId(articleId);
                                adDetail.setGrantEdit(response.isGrantEdit());
                                apiCallback.onSuccess(adDetail);
                            } else {
                                apiCallback.onFailure(new Throwable(response.getError()));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (((HttpException) throwable).code() == 403) {
                                Event adDetail = new Event();
                                adDetail.setId(articleId);
                                adDetail.setGrantEdit(false);
                                apiCallback.onSuccess(adDetail);
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
    public void readEventDetail(int id, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).getEventList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Event event) {
                        if (event != null) {
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
    public void createEvent(Event ad, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", ad.getTitle());
        jsonObject.addProperty("event_title", ad.getEventTitle());
        jsonObject.addProperty("content", ad.getContent());
        jsonObject.addProperty("shop_id", ad.getShopId());
        jsonObject.addProperty("start_date", ad.getStartDate());
        jsonObject.addProperty("end_date", ad.getEndDate());
        if (ad.getThumbnail() != null)
            jsonObject.addProperty("thumbnail", ad.getThumbnail());

        RetrofitManager.getInstance().getRetrofit().create(EventService.class).postEvent(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Event response) {
                        if (response != null) {
//                            if(FormValidatorUtil.validateStringIsEmpty(response.eventTitle))
//                                response.eve
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
    public void updateEvent(int articleId, Event adDetail, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", adDetail.getTitle());
        jsonObject.addProperty("event_title", adDetail.getEventTitle());
        jsonObject.addProperty("content", adDetail.getContent());
        jsonObject.addProperty("start_date", adDetail.getStartDate());
        jsonObject.addProperty("end_date", adDetail.getEndDate());
        jsonObject.addProperty("shop_id", adDetail.getShopId());
        if(adDetail.getThumbnail() != null)
            jsonObject.addProperty("thumbnail", adDetail.getThumbnail());
        RetrofitManager.getInstance().getRetrofit().create(EventService.class).updateEvent(articleId, addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Event adDetail) {
                        if (FormValidatorUtil.validateStringIsEmpty(adDetail.getError()))
                            apiCallback.onSuccess(adDetail);
                        else
                            apiCallback.onFailure(new Throwable(adDetail.getError()));
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