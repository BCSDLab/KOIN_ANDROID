package in.koreatech.koin.service_bus.presenters;


import in.koreatech.koin.core.bases.BasePresenter;
import in.koreatech.koin.core.networks.ApiCallback;
import in.koreatech.koin.core.networks.entity.Term;
import in.koreatech.koin.core.networks.interactors.TermInteractor;
import in.koreatech.koin.service_bus.contracts.BusTimeTableContract;

public class BusTimeTablePresenter implements BasePresenter {
    private String TAG = BusTimeTablePresenter.class.getSimpleName();
    private final BusTimeTableContract.View busTimeTableView;
    private final TermInteractor termInteractor;

    public BusTimeTablePresenter(BusTimeTableContract.View busTimeTableView, TermInteractor termInteractor) {
        this.busTimeTableView = busTimeTableView;
        this.termInteractor = termInteractor;
    }

    private final ApiCallback apiCallback = new ApiCallback() {                 //방학인지 학기중인지 정보를 받아오는 api callback
        @Override
        public void onSuccess(Object object) {
            Term term = (Term) object;
            busTimeTableView.setBusTimeTableSpinner(term.getTerm());
        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    };

    public void getTerm() {
        termInteractor.readTerm(apiCallback);
    }

}
