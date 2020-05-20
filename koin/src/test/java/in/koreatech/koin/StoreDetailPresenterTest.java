package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.StoreInteractor;
import in.koreatech.koin.ui.store.presenter.StoreDetailContract;
import in.koreatech.koin.ui.store.presenter.StoreDetailPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class StoreDetailPresenterTest {
    @Mock
    StoreDetailContract.View storeDetailView;
    @Mock
    StoreInteractor storeInteractor;

    private StoreDetailPresenter storeDetailPresenter;
    private ApiCallback apiCallback;
    private int testId;

    @Before
    public void setupStoreDetailPresenter() {
        MockitoAnnotations.initMocks(this);
        storeDetailPresenter = new StoreDetailPresenter(storeDetailView, storeInteractor);
        testId = 9999;
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        storeDetailPresenter = new StoreDetailPresenter(storeDetailView, storeInteractor);
        verify(storeDetailView).setPresenter(storeDetailPresenter);
    }

    @Test
    public void loadStoreDetailInfoFromServerAndLoadIntoView() {
        Store store = new Store();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(store);
            return null;
        }).when(storeInteractor).readStore(anyInt(), any(ApiCallback.class));

        storeDetailPresenter.getStore(testId);

        verify(storeDetailView).showLoading();
        verify(storeDetailView).onStoreDataReceived(store);
        verify(storeDetailView).hideLoading();
    }

    @Test
    public void errorStoreDetailInfoFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(storeInteractor).readStore(anyInt(), any(ApiCallback.class));

        storeDetailPresenter.getStore(testId);

        verify(storeDetailView).showLoading();
        verify(storeDetailView).showMessage(R.string.store_get_data_fail);
        verify(storeDetailView).hideLoading();
    }
}
