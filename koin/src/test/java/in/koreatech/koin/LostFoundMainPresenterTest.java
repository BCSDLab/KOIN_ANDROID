package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.response.LostAndFoundPageResponse;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundMainContract;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundMainPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class LostFoundMainPresenterTest {
    @Mock
    LostAndFoundInteractor lostAndFoundInteractor;
    @Mock
    LostFoundMainContract.View lostFoundMainView;

    private LostFoundMainPresenter lostFoundMainPresenter;
    private ApiCallback apiCallback;
    private LostAndFoundPageResponse lostAndFoundPageResponse;
    private int testPage = 1;
    private int testLimit = 10;

    @Before
    public void setupLostFoundMainPresenterTest() {
        MockitoAnnotations.initMocks(this);
        lostFoundMainPresenter = new LostFoundMainPresenter(lostFoundMainView, lostAndFoundInteractor);
        LostItem lostItem = new LostItem();
        lostItem.setTitle("TEST TITLE");
        lostItem.setContent("FOR TEST");
        ArrayList<LostItem> lostItems = new ArrayList<>();
        lostItems.add(lostItem);
        lostAndFoundPageResponse = new LostAndFoundPageResponse(1, lostItems, 10);
    }

    @Test
    public void createPresenter_setsThePresenterIntoView() {
        lostFoundMainPresenter = new LostFoundMainPresenter(lostFoundMainView, lostAndFoundInteractor);
        verify(lostFoundMainView).setPresenter(lostFoundMainPresenter);
    }

    @Test
    public void loadLostItemFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(lostAndFoundPageResponse);
            return null;
        }).when(lostAndFoundInteractor).readLostAndFoundList(anyInt(), anyInt(), any(ApiCallback.class));

        lostFoundMainPresenter.getLostItem(testPage, testLimit);

        verify(lostFoundMainView).showLoading();
        verify(lostFoundMainView).showLostAndFoundPageResponse(lostAndFoundPageResponse);
        verify(lostFoundMainView).hideLoading();
    }

    @Test
    public void errorLostItemFromServer_WrongObject_ShowsErrorToastMessage() {
        LostItem lostItem = new LostItem();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostAndFoundInteractor).readLostAndFoundList(anyInt(), anyInt(), any(ApiCallback.class));

        lostFoundMainPresenter.getLostItem(testPage, testLimit);

        verify(lostFoundMainView).showLoading();
        verify(lostFoundMainView).showMessage("리스트를 받아오지 못했습니다.");
        verify(lostFoundMainView).hideLoading();
    }

    @Test
    public void errorLostItemFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostAndFoundInteractor).readLostAndFoundList(anyInt(), anyInt(), any(ApiCallback.class));

        lostFoundMainPresenter.getLostItem(testPage, testLimit);

        verify(lostFoundMainView).showLoading();
        verify(lostFoundMainView).showMessage("리스트를 받아오지 못했습니다.");
        verify(lostFoundMainView).hideLoading();
    }
}
