package in.koreatech.koin.service_callvan.presenters;

import java.util.ArrayList;


import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.service_callvan.contracts.CompanyContract;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Company;
import in.koreatech.koin.core.networks.interactors.CallvanInteractor;


/**
 * Created by hyerim on 2018. 6. 17....
 */
public class CompanyPresenter implements BasePresenter {

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
