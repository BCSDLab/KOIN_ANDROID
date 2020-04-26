package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;

import in.koreatech.koin.constant.BusType;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Bus;
import in.koreatech.koin.data.network.entity.RegularSemesterBus;
import in.koreatech.koin.data.network.entity.SeasonalSemesterBus;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.entity.VacationBus;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.ui.bus.presenter.BusTimeTableSearchContract;
import in.koreatech.koin.ui.bus.presenter.BusTimeTableSearchPresenter;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class BusTimeTableSearchPresenterTest {
    @Mock
    BusTimeTableSearchContract.View busTimeTableSearchView;
    @Mock
    TermInteractor termInteractor;

    private BusTimeTableSearchPresenter busTimeTableSearchPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setupBusTimeTableSearchPresenter() {
        MockitoAnnotations.initMocks(this);
        busTimeTableSearchPresenter = new BusTimeTableSearchPresenter(busTimeTableSearchView, termInteractor);
    }

    @Test
    public void createPresenter_SetsThePresenterToView() {
        busTimeTableSearchPresenter = new BusTimeTableSearchPresenter(busTimeTableSearchView, termInteractor);
        verify(busTimeTableSearchView).setPresenter(busTimeTableSearchPresenter);
    }

    @Test
    public void loadTermInfo_UpdateTermInfo() {
        // 10 : 1학기
        Term term = new Term(10);
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(term);
            return null;
        }).when(termInteractor).readTerm(any(ApiCallback.class));

        busTimeTableSearchPresenter.getTermInfo();
        verify(busTimeTableSearchView).updateTermInfo(term.getTerm());
        verify(busTimeTableSearchView).hideLoading();
    }

    @Test
    public void loadDaesungBusAndLoadIntoView() {
        busTimeTableSearchPresenter.getDaesungBus(0, 1, "", "11:30");
        String busTime = "";
        try {
            busTime = Bus.getNearExpressTimeToString(BusType.getValueOf(0), BusType.getValueOf(1), Integer.parseInt("11"), Integer.parseInt("30"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        verify(busTimeTableSearchView).showLoading();
        verify(busTimeTableSearchView).updateDaesungBusTime(busTime);
        verify(busTimeTableSearchView).hideLoading();
    }

    @Test
    public void errorDaesungBus_WrongTimeFormat_ShowsUpdateFailDepartInfo() {
        busTimeTableSearchPresenter.getDaesungBus(0, 1, "", "11");
        verify(busTimeTableSearchView).showLoading();
        verify(busTimeTableSearchView).updateFailDaesungBusDepartInfo();
        verify(busTimeTableSearchView).hideLoading();
    }

    @Test
    public void loadShuttleBusAndLoadIntoView() {
        // TODO: 현재 시간 연동
        busTimeTableSearchPresenter.getShuttleBus(0, 1, "2020-04-26", "17:25", 10);
        verify(busTimeTableSearchView).showLoading();
        Bus bus = new RegularSemesterBus();
        String busTime = "";
        try {
            busTime = bus.getNearShuttleTimeToString(BusType.getValueOf(0), BusType.getValueOf(1), Integer.parseInt("2020"), Integer.parseInt("04"), Integer.parseInt("26"), Integer.parseInt("17"), Integer.parseInt("25"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        verify(busTimeTableSearchView).updateShuttleBusTime(busTime);
        verify(busTimeTableSearchView).hideLoading();
    }
}
