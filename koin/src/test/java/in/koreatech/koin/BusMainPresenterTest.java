package in.koreatech.koin;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Term;
import in.koreatech.koin.data.network.interactor.CityBusInteractor;
import in.koreatech.koin.data.network.interactor.TermInteractor;
import in.koreatech.koin.data.network.response.BusResponse;
import in.koreatech.koin.ui.bus.presenter.BusMainContract;
import in.koreatech.koin.ui.bus.presenter.BusMainPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class BusMainPresenterTest {
    @Mock
    BusMainContract.View busMainView;
    @Mock
    CityBusInteractor busInteractor;
    @Mock
    TermInteractor termInteractor;

    private BusMainPresenter busMainPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setupBusMainPresenter() {
        MockitoAnnotations.initMocks(this);
        busMainPresenter = new BusMainPresenter(busMainView, busInteractor, termInteractor);
    }

    @Test
    public void createPresenter_SetsThePresenterToView() {
        busMainPresenter = new BusMainPresenter(busMainView, busInteractor, termInteractor);
        verify(busMainView).setPresenter(busMainPresenter);
    }

    @Test
    public void loadTermInfo_ShowsUpdateShuttleBusInfo() {
        Term term = new Term(10);   // 10 : 1학기
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(term);
            return null;
        }).when(termInteractor).readTerm(any(ApiCallback.class));

        busMainPresenter.getTermInfo();
        verify(busMainView).updateShuttleBusInfo(term.getTerm());
        verify(busMainView).hideLoading();
    }

    @Test
    public void errorLoadTermInfo_ShowsUpdateFailCityBusDepartInfo() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(termInteractor).readTerm(any(ApiCallback.class));

        busMainPresenter.getTermInfo();
        verify(busMainView).updateFailCityBusDepartInfo();
        verify(busMainView).hideLoading();
    }


    @Test
    public void loadCityBus_ShowsUpdateCityBus() {
        BusResponse busResponse = new BusResponse();
        busResponse.busNumber = 400;
        busResponse.nextBusNumber = 401;
        busResponse.remainTime = 100;
        busResponse.nextRemainTime = 200;

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(busResponse);
            return null;
        }).when(busInteractor).readCityBusList(any(ApiCallback.class), any(), any());

        // koreatech -> terminal
        busMainPresenter.getCityBus(0, 1);
        verify(busMainView).updateCityBusDepartInfo(busResponse.busNumber, busResponse.nextBusNumber);
        verify(busMainView).updateCityBusTime(busResponse.remainTime, busResponse.nextRemainTime);
        verify(busMainView).hideLoading();
    }

    @Test
    public void errorLoadCityBus_ShowsUpdateFailCityBusDepartInfo() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = (ApiCallback) invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(busInteractor).readCityBusList(any(ApiCallback.class), any(), any());

        // koreatech -> terminal
        busMainPresenter.getCityBus(0, 1);
        verify(busMainView).updateFailCityBusDepartInfo();
        verify(busMainView).hideLoading();
    }

    // TODO: getShuttleBus 테스트 방법 좀 더 생각하기
}