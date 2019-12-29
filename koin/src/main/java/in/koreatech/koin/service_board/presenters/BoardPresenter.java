package in.koreatech.koin.service_board.presenters;

import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.service_board.contracts.BoardContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.responses.ArticlePageResponse;
import in.koreatech.koin.core.networks.interactors.CommunityInteractor;

import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_ANONYMOUS;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class BoardPresenter implements BasePresenter {

    private final BoardContract.View boardView;

    private final CommunityInteractor communityInteractor;

    public BoardPresenter(BoardContract.View boardView, CommunityInteractor communityInteractor) {
        this.boardView = boardView;
        this.communityInteractor = communityInteractor;
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
