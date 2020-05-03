package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.data.network.response.MarketPageResponse;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class MarketUsedPresenterTest {
    @Mock
    MarketUsedContract.View marketUsedView;
    @Mock
    MarketUsedInteractor marketUsedInteractor;

    private MarketUsedPresenter marketUsedPresenter;
    private ApiCallback apiCallback;
    private MarketPageResponse marketPageResponse;
    private Item item;
    private int marketId;

    @Before
    public void setupMarketUsedPresenter() {
        MockitoAnnotations.initMocks(this);
        marketUsedPresenter = new MarketUsedPresenter(marketUsedView, marketUsedInteractor);
        item = new Item();
        marketId = 1122;
        item.setId(marketId);
        item.setTitle("TEST TITLE");
        item.setContent("TEST CONTENT");
        ArrayList<Item> marketList = new ArrayList<>();
        marketList.add(item);
        marketPageResponse = new MarketPageResponse(marketList, 1, 10);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        marketUsedPresenter = new MarketUsedPresenter(marketUsedView, marketUsedInteractor);
        verify(marketUsedView).setPresenter(marketUsedPresenter);
    }

    @Test
    public void loadMarketListFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(marketPageResponse);
            return null;
        }).when(marketUsedInteractor).readMarketList(anyInt(), anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readMarket(1, 10);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).onMarketDataReceived(marketPageResponse);
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void errorMarketListFromServer_WrongObject_ShowsErrorToastMessagew() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readMarketList(anyInt(), anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readMarket(1, 10);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).showMarketDataReceivedFail();
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void errorMarketListFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).readMarketList(anyInt(), anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readMarket(1, 10);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).showMarketDataReceivedFail();
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void checkGrantEdit_PositiveGranted_ShowsErrorToastMessage() {
        item.setGrantEdit(true);

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readGrantedDetail(marketId);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).onGrantedDataReceived(true);
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void checkGrantEdit_NegativeGranted_ShowsErrorToastMessage() {
        item.setGrantEdit(false);

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readGrantedDetail(marketId);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).onGrantedDataReceived(false);
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void errorCheckGrantEdit_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readGrantedDetail(marketId);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).onGrantedDataReceived(false);
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void loadMarketDetailFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readDetailMarket(marketId);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).onMarketDataReceived(item);
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void errorMarketDetailFromServer_WrongObject_ShowsErrorToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(marketPageResponse);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readDetailMarket(marketId);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).showMarketDataReceivedFail();
        verify(marketUsedView).hideLoading();
    }

    @Test
    public void errorMarketDetailFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedPresenter.readDetailMarket(marketId);

        verify(marketUsedView).showLoading();
        verify(marketUsedView).showMarketDataReceivedFail();
        verify(marketUsedView).hideLoading();
    }
}
