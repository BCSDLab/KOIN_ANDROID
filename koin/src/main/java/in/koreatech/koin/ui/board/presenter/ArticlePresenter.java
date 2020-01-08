package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class ArticlePresenter implements BasePresenter {
    private final ArticleContract.View articleView;

    private final CommunityInteractor communityInteractor;

    public ArticlePresenter(ArticleContract.View articleView, CommunityInteractor communityInteractor) {
        this.articleView = articleView;
        this.communityInteractor = communityInteractor;
    }

    private final ApiCallback articleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Article article = (Article) (object);
            articleView.onArticleDataReceived(article);
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showMessage(throwable.getMessage());
            articleView.hideLoading();
        }
    };

    private final ApiCallback deleteArticleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            articleView.onArticleDeleteCompleteReceived(true);
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showMessage(throwable.getMessage());
            articleView.hideLoading();
        }
    };

//    private final ApiCallback commentApiCallback = new ApiCallback() {
//        @Override
//        public void onSuccess(Object object) {
//            int articleUid = ((Comment) object).articleUid;
//            getArticle(articleUid);
//        }
//
//        @Override
//        public void onFailure(Throwable throwable) {
//
//        }
//    };
//
//    private final ApiCallback commentAnoymousApiCallback = new ApiCallback() {
//        @Override
//        public void onSuccess(Object object) {
//            int articleUid = ((Comment) object).articleUid;
//            getAnonymousArticle(articleUid);
//        }
//
//        @Override
//        public void onFailure(Throwable throwable) {
//
//        }
//    };

    private final ApiCallback grantUpdateApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            boolean isGrantEdit = ((Article) object).isGrantEdit;
            if (isGrantEdit)
                articleView.showEditAndDeleteMenu();
            else
                articleView.hideEditAndDeleteMenu();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.hideEditAndDeleteMenu();
            articleView.hideLoading();
        }
    };

    private final ApiCallback grantAnonymousAdjustApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            boolean isGrantEdit = ((Article) object).isGrantEdit;
            if (isGrantEdit)
                articleView.showSuccessAdjustGrantedContent();
            else
                articleView.showErrorAdjustGrantedContent();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showErrorAdjustGrantedContent();
            articleView.hideLoading();
        }
    };

    private final ApiCallback grantAnonymousDeleteApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            boolean isGrantEdit = ((Article) object).isGrantEdit;
            if (isGrantEdit)
                articleView.showSuccessGrantedDeleteContent();
            else
                articleView.showErrorGrantedDeleteContent();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showErrorGrantedDeleteContent();
            articleView.hideLoading();
        }
    };

//    private final ApiCallback grantAnonymousDeleteCommentGrantedApiCallback = new ApiCallback() {
//        @Override
//        public void onSuccess(Object object) {
//            boolean isGrantEdit = ((Article) object).isGrantEdit;
//            if (isGrantEdit)
//                articleView.showSuccessGrantedDeleteComment();
//            else
//                articleView.showErrorGrantedDeleteComment();
//
//        }
//
//        @Override
//        public void onFailure(Throwable throwable) {
//            articleView.showErrorGrantedDeleteComment();
//        }
//    };

    //    private final ApiCallback grantAnonymousAdjustCommentGrantedApiCallback = new ApiCallback() {
//        @Override
//        public void onSuccess(Object object) {
//            boolean isGrantEdit = ((Article) object).isGrantEdit;
//            if (isGrantEdit)
//                articleView.showSuccessGrantedAdjustComment();
//            else
//                articleView.showErrorGrantedAdjustComment();
//        }
//
//        @Override
//        public void onFailure(Throwable throwable) {
//            articleView.showErrorGrantedAdjustComment();
//        }
//    };
//
    private final ApiCallback deleteAnoymousArticleApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            articleView.showSuccessDeleteContent();
            articleView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            articleView.showErrorDeleteContent();
            articleView.hideLoading();
        }
    };

    public void getArticle(int articleUid) {
        articleView.showLoading();
        communityInteractor.readArticle(articleUid, articleApiCallback);
    }

    public void getAnonymousArticle(int articleUid) {
        articleView.showLoading();
        communityInteractor.readAnonymousArticle(articleUid, articleApiCallback);
    }

    public void updateArticle(Article article) {
        articleView.showLoading();
        communityInteractor.updateArticle(article, articleApiCallback);
    }

    public void deleteArticle(int articleUid) {
        articleView.showLoading();
        communityInteractor.deleteArticle(articleUid, deleteArticleApiCallback);
    }

//    public void createComment(int articleUid, String content) {
//        communityInteractor.createComment(articleUid, content, commentAnoymousApiCallback);
//    }

    public void readComment(int articleUid, int commentUid) {
//        communityInteractor.readComment(articleUid, commentUid, commentApiCallback);
    }

//    public void updateComment(int articleUid, Comment comment) {
//        comment.articleUid = articleUid;
//        communityInteractor.updateComment(comment, commentAnoymousApiCallback);
//    }
//
//    public void deleteComment(int articleUid, int commentUid) {
//        communityInteractor.deleteComment(articleUid, commentUid, commentAnoymousApiCallback);
//    }

    public void checkGranted(int articleUid) {
        articleView.showLoading();
        communityInteractor.updateGrantCheck(articleUid, grantUpdateApiCallback);
    }

    public void deleteAnoymousArticle(int articleUid, String password) {
        articleView.showLoading();
        communityInteractor.deleteAnonymousArticle(articleUid, password, deleteAnoymousArticleApiCallback);
    }
//
//    public void createAnonymousComment(int articleUid, String content, String nickname, String password) {
//        communityInteractor.createAnonymousComment(articleUid, content, nickname, password, commentAnoymousApiCallback);
//    }
//
//    public void updateAnonymousComment(int articleUid, Comment comment) {
//        comment.articleUid = articleUid;
//        communityInteractor.updateAnonymousComment(comment, commentAnoymousApiCallback);
//    }
//
//    public void deleteAnonymousComment(int articleUid, int commentUid, String password) {
//        communityInteractor.deleteAnonymousComment(articleUid, commentUid, password, commentAnoymousApiCallback);
//    }

    public void checkAnonymousAdjustGranted(int articleUid, String password) {
        articleView.showLoading();
        communityInteractor.updateAnonymousGrantCheck(articleUid, password, grantAnonymousAdjustApiCallback);
    }

    public void checkAnonymousDeleteGranted(int articleUid, String password) {
        articleView.showLoading();
        communityInteractor.updateAnonymousGrantCheck(articleUid, password, grantAnonymousDeleteApiCallback);
    }

//    public void checkAnonymousCommentDeleteGranted(int commentUid, String password) {
//        communityInteractor.updateAnonymousCommentGrantCheck(commentUid, password, grantAnonymousDeleteCommentGrantedApiCallback);
//    }
//
//    public void checkAnonymousCommentAdjustGranted(int commentUid, String password) {
//        communityInteractor.updateAnonymousCommentGrantCheck(commentUid, password, grantAnonymousAdjustCommentGrantedApiCallback);
//    }

}
