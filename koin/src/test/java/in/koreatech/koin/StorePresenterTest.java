package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.StoreInteractor;
import in.koreatech.koin.data.network.response.StoresResponse;
import in.koreatech.koin.ui.store.presenter.StoreContract;
import in.koreatech.koin.ui.store.presenter.StorePresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class StorePresenterTest {
    @Mock
    StoreContract.View storeView;
    @Mock
    StoreInteractor storeInteractor;

    private StorePresenter storePresenter;
    private ApiCallback apiCallback;
    private ArrayList<Store> storeArrayList;

    @Before
    public void setupStorePresenter() {
        MockitoAnnotations.initMocks(this);
        storePresenter = new StorePresenter(storeView, storeInteractor);
        Store testStore = new Store();
        storeArrayList = new ArrayList<>();
        storeArrayList.add(testStore);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        storePresenter = new StorePresenter(storeView, storeInteractor);
        verify(storeView).setPresenter(storePresenter);
    }

    @Test
    public void loadStoreListFromServerAndLoadIntoView() {
        StoresResponse storesResponse = new StoresResponse(storeArrayList);

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(storesResponse);
            return null;
        }).when(storeInteractor).readStoreList(any(ApiCallback.class));

        storePresenter.getStoreList();

        verify(storeView).showLoading();
        verify(storeView).onStoreListDataReceived(storeArrayList);
        verify(storeView).hideLoading();
    }

    @Test
    public void errorStoreListFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(storeInteractor).readStoreList(any(ApiCallback.class));

        storePresenter.getStoreList();

        verify(storeView).showLoading();
        verify(storeView).showMessage(R.string.store_get_data_fail);
        verify(storeView).hideLoading();
    }
}
