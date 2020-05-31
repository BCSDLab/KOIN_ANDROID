package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.ui.bus.presenter.BusTimeTableContract;
import in.koreatech.koin.ui.bus.presenter.BusTimeTablePresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class BusTimeTablePresenterTest {
    @Mock
    BusTimeTableContract.View busTimeTableView;
    @Mock
    TermInteractor termInteractor;

    private BusTimeTablePresenter busTimeTablePresenter;
    private ApiCallback apiCallback;

    @Before
    public void setupBusTimeTablePresenter() {
        MockitoAnnotations.initMocks(this);
        busTimeTablePresenter = new BusTimeTablePresenter(busTimeTableView, termInteractor);
    }

    @Test
    public void createPresenter_SetsThePresenterToView() {
        busTimeTablePresenter = new BusTimeTablePresenter(busTimeTableView, termInteractor);
        verify(busTimeTableView).setPresenter(busTimeTablePresenter);
    }

    @Test
    public void loadTerm_ShowsSetBusTimeTableSpinner() {
        // 10 : 1학기
        Term term = new Term(10);
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(term);
            return null;
        }).when(termInteractor).readTerm(any(ApiCallback.class));

        busTimeTablePresenter.getTerm();
        verify(busTimeTableView).setBusTimeTableSpinner(term.getTerm());
    }
}
