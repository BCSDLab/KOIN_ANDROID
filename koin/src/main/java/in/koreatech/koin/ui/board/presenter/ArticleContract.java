package in.koreatech.koin.ui.board.presenter;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Article;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public interface ArticleContract {
    interface View extends BaseView<ArticlePresenter> {
        void checkRequiredInfo();

        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void updateUserInterface();

        void onArticleDataReceived(Article article);

        void onArticleDeleteCompleteReceived(boolean isSuccess);

        void onClickEditButton();

        void onClickRemoveButton();

        void showEditAndDeleteMenu();

        void hideEditAndDeleteMenu();

        void showErrorGrantedDeleteContent();

        void showSuccessGrantedDeleteContent();

        void showErrorDeleteContent();

        void showSuccessDeleteContent();

        void showErrorAdjustGrantedContent();

        void showSuccessAdjustGrantedContent();
    }
}
