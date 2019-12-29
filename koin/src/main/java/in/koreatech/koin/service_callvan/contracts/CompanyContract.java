package in.koreatech.koin.service_callvan.contracts;

import java.util.ArrayList;

import in.koreatech.koin.core.bases.BaseView;
import in.koreatech.koin.core.networks.entity.Company;
import in.koreatech.koin.service_callvan.presenters.CompanyPresenter;

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
