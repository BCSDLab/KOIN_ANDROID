package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.response.ArticlePageResponse;
import in.koreatech.koin.data.network.interactor.CommunityInteractor;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;

public class BoardPresenter {

    private final BoardContract.View boardView;

    private final CommunityInteractor communityInteractor;

    public BoardPresenter(BoardContract.View boardView, CommunityInteractor communityInteractor) {
        this.boardView = boardView;
        this.communityInteractor = communityInteractor;
        boardView.setPresenter(this);
    }

    private final ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArticlePageResponse articlePage = (ArticlePageResponse) (object);
            boardView.onBoardListDataReceived(articlePage);
            boardView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            boardView.showMessage(throwable.getMessage());
            boardView.hideLoading();
        }
    };

    private final ApiCallback grantApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            boardView.onArticleGranDataReceived((Article) object);
            boardView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            boardView.showMessage(throwable.getMessage());
            boardView.hideLoading();
        }
    };


    public void getArticlePage(int boardUid, int pageNum) {
        boardView.showLoading();
        if (boardUid != ID_ANONYMOUS)
            communityInteractor.readArticleList(boardUid, pageNum, apiCallback);
        else
            communityInteractor.readAnonymousArticleList(pageNum, apiCallback);
    }

    public void getArticleGrant(int articleUid) {
        boardView.showLoading();
        communityInteractor.updateGrantCheck(articleUid, grantApiCallback);
    }

}
