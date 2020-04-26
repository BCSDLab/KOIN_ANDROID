package in.koreatech.koin.ui.bus.presenter;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.interactor.TermInteractor;

public class BusTimeTablePresenter {
    private String TAG = "BusTimeTablePresenter";
    private final BusTimeTableContract.View busTimeTableView;
    private final TermInteractor termInteractor;

    public BusTimeTablePresenter(BusTimeTableContract.View busTimeTableView, TermInteractor termInteractor) {
        this.busTimeTableView = busTimeTableView;
        this.termInteractor = termInteractor;
        this.busTimeTableView.setPresenter(this);
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
