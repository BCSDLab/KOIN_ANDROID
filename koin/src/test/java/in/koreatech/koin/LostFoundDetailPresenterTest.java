package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.data.network.response.GrantCheckResponse;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundDetailContract;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundDetailPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class LostFoundDetailPresenterTest {
    @Mock
    LostFoundDetailContract.View lostFoundDetailView;
    @Mock
    LostAndFoundInteractor lostFoundInteractor;

    private int testId = 1234;
    private LostFoundDetailPresenter lostFoundDetailPresenter;
    private ApiCallback apiCallback;
    private LostItem lostItem;

    @Before
    public void setLostFoundDetailPresenter() {
        MockitoAnnotations.initMocks(this);
        lostFoundDetailPresenter = new LostFoundDetailPresenter(lostFoundDetailView, lostFoundInteractor);
        lostItem = new LostItem();
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        lostFoundDetailPresenter = new LostFoundDetailPresenter(lostFoundDetailView, lostFoundInteractor);
        verify(lostFoundDetailView).setPresenter(lostFoundDetailPresenter);
    }

    @Test
    public void loadLostFoundDetailFromServerAndLoadIntoView() {
        GrantCheckResponse grantCheckResponse = new GrantCheckResponse();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(grantCheckResponse);
            return null;
        }).when(lostFoundInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.getLostFoundDetailById(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showGranted(grantCheckResponse.isGrantEdit);
        verify(lostFoundDetailView).updateLostDetailData(lostItem);
        verify(lostFoundDetailView).hideLoading();
    }

    @Test
    public void errorCheckGrantEditFromServer_WrongObject_ShowsErrorToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.getLostFoundDetailById(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showGranted(false);
        verify(lostFoundDetailView).showMessage(R.string.error_network);
    }

    @Test
    public void errorCheckGrantEditFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostFoundInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.getLostFoundDetailById(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showGranted(false);
        verify(lostFoundDetailView).showMessage(R.string.error_network);
    }

    @Test
    public void errorLoadLostFoundDetailFromServer_WrongObject_ShowsErrorToastMessage() {
        GrantCheckResponse grantCheckResponse = new GrantCheckResponse();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(grantCheckResponse);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.getLostFoundDetailById(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showMessage(R.string.error_network);
        verify(lostFoundDetailView).hideLoading();
    }

    @Test
    public void errorLoadLostFoundDetailFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.getLostFoundDetailById(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showMessage(R.string.error_network);
        verify(lostFoundDetailView).hideLoading();
    }

    @Test
    public void deleteLostFoundItemFromServerAndLoadIntoView() {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.success = true;

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(defaultResponse);
            return null;
        }).when(lostFoundInteractor).deleteLostAndFoundItem(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.removeItem(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showSuccessDeleted();
        verify(lostFoundDetailView).hideLoading();
    }

    @Test
    public void errorDeleteLostFoundItemFromServer_WrongObject_ShowsErrorToastMessage() {
        GrantCheckResponse grantCheckResponse = new GrantCheckResponse();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(grantCheckResponse);
            return null;
        }).when(lostFoundInteractor).deleteLostAndFoundItem(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.removeItem(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showMessage(R.string.lost_and_found_delete_fail);
        verify(lostFoundDetailView).hideLoading();
    }

    @Test
    public void errorDeleteLostFoundItemFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostFoundInteractor).deleteLostAndFoundItem(anyInt(), any(ApiCallback.class));

        lostFoundDetailPresenter.removeItem(testId);

        verify(lostFoundDetailView).showLoading();
        verify(lostFoundDetailView).showMessage(R.string.lost_and_found_delete_fail);
        verify(lostFoundDetailView).hideLoading();
    }
}
