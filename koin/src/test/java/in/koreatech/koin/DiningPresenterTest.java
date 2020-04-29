package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.UnknownHostException;
import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Dining;
import in.koreatech.koin.data.network.interactor.DiningInteractor;
import in.koreatech.koin.ui.dining.presenter.DiningContract;
import in.koreatech.koin.ui.dining.presenter.DiningPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class DiningPresenterTest {
    @Mock
    DiningContract.View diningView;
    @Mock
    DiningInteractor diningInteractor;

    private DiningPresenter diningPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setupDiningPresenter() {
        MockitoAnnotations.initMocks(this);
        diningPresenter = new DiningPresenter(diningView, diningInteractor);
    }

    @Test
    public void createPresenter_SetsThePresenterToView() {
        diningPresenter = new DiningPresenter(diningView, diningInteractor);
        verify(diningView).setPresenter(diningPresenter);
    }

    @Test
    public void loadDiningList_ShowsUpdateDiningInfo() {
        Dining dining = new Dining();
        dining.setUid(1004);
        dining.setDate("2020-04-29");
        dining.setType("BREAKFAST");
        dining.setPlace("한식");
        dining.setPriceCard(2700);
        dining.setPriceCash(3000);
        dining.setKcal(888);

        ArrayList<Dining> diningList = new ArrayList<>();
        diningList.add(dining);

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(diningList);
            return null;
        }).when(diningInteractor).readDiningList(any(), any(ApiCallback.class));

        diningPresenter.getDiningList("2020-04-29");
        verify(diningView).showLoading();
        verify(diningView).onDiningListDataReceived(diningList);
        verify(diningView).hideLoading();
    }

    @Test
    public void errorDiningList_UnknownHost_ShowsNetworkError() {
        UnknownHostException unknownHostException = new UnknownHostException();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(unknownHostException);
            return null;
        }).when(diningInteractor).readDiningList(any(), any(ApiCallback.class));

        diningPresenter.getDiningList("2020-04-29");

        verify(diningView).showLoading();
        verify(diningView).showNetworkError();
        verify(diningView).hideLoading();
    }

    @Test
    public void errorDiningList_Exception_ShowsUserInterface() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(diningInteractor).readDiningList(any(), any(ApiCallback.class));

        diningPresenter.getDiningList("2020-04-29");

        verify(diningView).showLoading();
        verify(diningView).showUserInterface();
        verify(diningView).hideLoading();
    }
}
