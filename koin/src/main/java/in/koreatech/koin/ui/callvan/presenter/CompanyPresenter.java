package in.koreatech.koin.ui.callvan.presenter;

import java.util.ArrayList;


import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Company;
import in.koreatech.koin.data.network.interactor.CallvanInteractor;

public class CompanyPresenter {

    private final CompanyContract.View companyView;

    private final CallvanInteractor callvanInteractor;

    public CompanyPresenter(CompanyContract.View companyView, CallvanInteractor callvanInteractor) {
        this.companyView = companyView;
        this.callvanInteractor = callvanInteractor;
    }

    private final ApiCallback listApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Company> companyArrayList = (ArrayList<Company>) object;
            companyView.onCallvanCompaniesDataReceived(companyArrayList);
        }

        @Override
        public void onFailure(Throwable throwable) {
            companyView.showMessage(throwable.getMessage());
        }
    };

    private final ApiCallback companyApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            Company company = (Company) object;
            companyView.onCallvanCompanyDataReceived(company);
        }

        @Override
        public void onFailure(Throwable throwable) {
            companyView.showMessage(throwable.getMessage());
        }
    };

    public void getCompanyList() {
        callvanInteractor.readCompanyList(listApiCallback);
    }

    public void getCompanyWithCallcountIncrease(int uid) {
        callvanInteractor.readCompany(uid, companyApiCallback);
    }

}
