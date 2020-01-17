package in.koreatech.koin.data.network.interactor;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.core.network.RetrofitManager;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.response.ArticlePageResponse;
import in.koreatech.koin.data.network.entity.Boards;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.service.CommunityService;
import in.koreatech.koin.util.FormValidatorUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static in.koreatech.koin.core.network.RetrofitManager.addAuthorizationBearer;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class CommunityRestInteractor implements CommunityInteractor {
    private final String TAG = "CommunityRestInteractor";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CommunityRestInteractor() {
    }

    @Override
    public void readBoardsList(ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).getBoardsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Boards>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArrayList<Boards> response) {
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
//                        compositeDisposable.dispose();
                    }
                });

    }

    @Override
    public void readBoard(String uid, ApiCallback apiCallback) {

        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).getBoardInfo(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boards>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Boards response) {
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
                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void readArticleList(int boardUid, int pageNum, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).getArticleList(boardUid, pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticlePageResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArticlePageResponse response) {
                        if (!response.articleArrayList.isEmpty()) {
                            for (int i = 0; i < response.articleArrayList.size(); i++) {
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).title)) {
                                    response.articleArrayList.get(i).title = "";
                                }
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).content)) {
                                    response.articleArrayList.get(i).content = "";
                                }
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).authorNickname)) {
                                    response.articleArrayList.get(i).authorNickname = "";
                                }
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).authorUid)) {
                                    response.articleArrayList.get(i).authorUid = "";
                                }
                            }
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable("마지막 글입니다"));
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
    public void readAnonymousArticleList(int pageNum, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).getAnonymousArticleList(pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticlePageResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(ArticlePageResponse response) {
                        if (!response.articleArrayList.isEmpty()) {
                            for (int i = 0; i < response.articleArrayList.size(); i++) {
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).title)) {
                                    response.articleArrayList.get(i).title = "";
                                }
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).content)) {
                                    response.articleArrayList.get(i).content = "";
                                }
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).authorNickname)) {
                                    response.articleArrayList.get(i).authorNickname = "";
                                }
                                if (FormValidatorUtil.validateStringIsEmpty(response.articleArrayList.get(i).authorUid)) {
                                    response.articleArrayList.get(i).authorUid = "";
                                }
                            }
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable("마지막 글입니다"));
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
    public void createArticle(Article article, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("board_id", article.boardUid);
        jsonObject.addProperty("content", article.content);
        jsonObject.addProperty("title", article.title);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).postArticle(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
                        if (response != null) {

                            if (FormValidatorUtil.validateStringIsEmpty(response.title)) {
                                response.title = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.content)) {
                                response.content = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorNickname)) {
                                response.authorNickname = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorUid)) {
                                response.authorUid = "";
                            }
                            if (response.commentArrayList == null) {
                                response.commentArrayList = new ArrayList<>();
                            }

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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void readArticle(int articleUid, ApiCallback apiCallback) {
        Observable<Article> articleObservable;
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        if (UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS)
            articleObservable = RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).getArticle(String.valueOf(articleUid));
        else
            articleObservable = RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).getArticle(addAuthorizationBearer(token), String.valueOf(articleUid));

        articleObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
                        if (response != null) {

                            if (FormValidatorUtil.validateStringIsEmpty(response.title)) {
                                response.title = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.content)) {
                                response.content = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorNickname)) {
                                response.authorNickname = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorUid)) {
                                response.authorUid = "";
                            }
                            if (response.commentArrayList == null) {
                                response.commentArrayList = new ArrayList<>();
                            }

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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void readAnonymousArticle(int articleUid, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).getAnonymousArticle(articleUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
                        if (response != null) {

                            if (FormValidatorUtil.validateStringIsEmpty(response.title)) {
                                response.title = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.content)) {
                                response.content = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorNickname)) {
                                response.authorNickname = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorUid)) {
                                response.authorUid = "";
                            }
                            if (response.commentArrayList == null) {
                                response.commentArrayList = new ArrayList<>();
                            }

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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void updateArticle(Article article, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject articleJsonObject = new JsonObject();
        articleJsonObject.addProperty("board_id", article.boardUid);
        articleJsonObject.addProperty("content", article.content);
        articleJsonObject.addProperty("title", article.title);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).putArticle(String.valueOf(article.articleUid), addAuthorizationBearer(token), articleJsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
                        if (response != null) {

                            if (FormValidatorUtil.validateStringIsEmpty(response.title)) {
                                response.title = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.content)) {
                                response.content = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorNickname)) {
                                response.authorNickname = "";
                            }
                            if (FormValidatorUtil.validateStringIsEmpty(response.authorUid)) {
                                response.authorUid = "";
                            }
                            if (response.commentArrayList == null) {
                                response.commentArrayList = new ArrayList<>();
                            }

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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void deleteArticle(int articleUid, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();

        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).deleteArticle(String.valueOf(articleUid), addAuthorizationBearer(token))
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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void createComment(int articleUid, String content, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", content);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).postComment(String.valueOf(articleUid), addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            response.articleUid = articleUid;
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
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
    public void readComment(int articleUid, int commentUid, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();

        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class)
                .getComment(String.valueOf(articleUid), String.valueOf(commentUid), addAuthorizationBearer(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            response.articleUid = articleUid;
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
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
    public void updateComment(Comment comment, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", comment.content);

        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class)
                .putComment(String.valueOf(comment.articleUid), String.valueOf(comment.commentUid), addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            response.articleUid = comment.articleUid;
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
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
    public void deleteComment(int articleUid, int commentUid, ApiCallback apiCallback) {
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();

        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class)
                .deleteComment(String.valueOf(articleUid), String.valueOf(commentUid),
                        addAuthorizationBearer(token))
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
                            Comment comment = new Comment();
                            comment.articleUid = articleUid;
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

                    }
                });
    }

    @Override
    public void updateGrantCheck(int articleUid, ApiCallback apiCallback) {
        if (UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS) {
            Article article = new Article();
            article.isGrantEdit = false;
            article.articleUid = articleUid;
            apiCallback.onSuccess(article);
            return;
        }
        String token = UserInfoSharedPreferencesHelper.getInstance().loadToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("article_id", articleUid);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).postGrantCheck(addAuthorizationBearer(token), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            Article article = new Article();
                            article.isGrantEdit = response.isGrantEdit;
                            article.articleUid = articleUid;
                            apiCallback.onSuccess(article);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        if (((HttpException) throwable).code() == 403) {
                            Article article = new Article();
                            article.isGrantEdit = false;
                            article.articleUid = articleUid;
                            apiCallback.onSuccess(article);
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
    public void createAnonymousComment(int articleUid, String content, String nickname, String password, ApiCallback apiCallback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("nickname", nickname);
        jsonObject.addProperty("password", password);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).postAnonymousComment(String.valueOf(articleUid), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            response.articleUid = articleUid;
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
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
    public void readAnonymousComment(int articleUid, int commentUid, ApiCallback apiCallback) {

        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class)
                .getAnonymousComment(String.valueOf(articleUid), String.valueOf(commentUid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            response.articleUid = articleUid;
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
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
    public void updateAnonymousComment(Comment comment, ApiCallback apiCallback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", comment.content);
        jsonObject.addProperty("password", comment.password);

        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class)
                .putAnonymousComment(String.valueOf(comment.articleUid), String.valueOf(comment.commentUid), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comment>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Comment response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            response.articleUid = comment.articleUid;
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
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
    public void deleteAnonymousComment(int articleUid, int commentUid, String password, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class)
                .deleteAnonymousComment(String.valueOf(articleUid), String.valueOf(commentUid), password)
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
                            Comment comment = new Comment();
                            comment.articleUid = articleUid;
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

                    }
                });
    }

    @Override
    public void updateAnonymousGrantCheck(int articleUid, String password, ApiCallback apiCallback) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("article_id", articleUid);
        jsonObject.addProperty("password", password);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).postAnonymousGrantCheck(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            Article article = new Article();
                            article.isGrantEdit = response.isGrantEdit;
                            article.articleUid = articleUid;
                            apiCallback.onSuccess(article);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        if (((HttpException) throwable).code() == 403) {
                            Article article = new Article();
                            article.isGrantEdit = false;
                            article.articleUid = articleUid;
                            apiCallback.onSuccess(article);
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
    public void updateAnonymousCommentGrantCheck(int commentUid, String password, ApiCallback apiCallback) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comment_id", commentUid);
        jsonObject.addProperty("password", password);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).postAnonymousCommentGrantCheck(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
                        if (FormValidatorUtil.validateStringIsEmpty(response.error)) {
                            apiCallback.onSuccess(response);
                        } else {
                            apiCallback.onFailure(new Throwable(response.error));
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        if (((HttpException) throwable).code() == 403) {
                            Article article = new Article();
                            apiCallback.onSuccess(article);
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
    public void deleteAnonymousArticle(int articleUid, String password, ApiCallback apiCallback) {
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).deleteAnoymousArticle(String.valueOf(articleUid), password)
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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void createAnonymousArticle(String title, String content, String nickname, String password, ApiCallback apiCallback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("nickname", nickname);
        jsonObject.addProperty("password", password);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).postAnonymousArticle(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
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
//                        compositeDisposable.dispose();
                    }
                });
    }

    @Override
    public void updateAnonymousArticle(int articleUid, String title, String content, String password, ApiCallback apiCallback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("password", password);
        RetrofitManager.getInstance().getRetrofit().create(CommunityService.class).putAnoymousArticle(Integer.toString(articleUid), jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Article>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Article response) {
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
//                        compositeDisposable.dispose();
                    }
                });
    }
}
