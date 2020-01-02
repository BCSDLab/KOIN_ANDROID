package in.koreatech.koin.ui.callvan.presenter;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Company;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public interface CompanyContract {
    interface View extends BaseView<CompanyPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void requestPermission();

        void onCallvanCompaniesDataReceived(ArrayList<Company> contactList);

        ArrayList<Company> sortCompanyListDescendingByCallCount(ArrayList<Company> contactList);

        void updateUserInterface();

        void showContactDialog(final int clickedPosition);

        void onClickCallButton(int clickedPosition);

        void UserCallStateCheck();

        void onCallvanCompanyDataReceived(Company company);

    }
}
