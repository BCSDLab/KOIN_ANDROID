package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Land;
import in.koreatech.koin.data.network.interactor.LandInteractor;
import in.koreatech.koin.ui.land.presenter.LandDetailContract;
import in.koreatech.koin.ui.land.presenter.LandDetailPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class LandDetailPresenterTest {
    @Mock
    LandInteractor landInteractor;
    @Mock
    LandDetailContract.View landDetailView;
    @Mock
    Land land;

    private LandDetailPresenter landDetailPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setupLandDetailPresenter() {
        MockitoAnnotations.initMocks(this);
        landDetailPresenter = new LandDetailPresenter(landDetailView, landInteractor);
    }

    @Test
    public void createPresenter_SetsThePresenterToView() {
        landDetailPresenter = new LandDetailPresenter(landDetailView, landInteractor);
        verify(landDetailView).setPresenter(landDetailPresenter);
    }

    @Test
    public void loadLandDetailInfoFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(land);
            return null;
        }).when(landInteractor).readLandDetail(anyInt(), any(ApiCallback.class));

        landDetailPresenter.getLandDetailInfo(111);

        verify(landDetailView).showLoading();
        verify(landDetailView).onLandDetailDataReceived(land);
        verify(landDetailView).hideLoading();
    }

    @Test
    public void errorLandDetailInfo_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(landInteractor).readLandDetail(anyInt(), any(ApiCallback.class));

        landDetailPresenter.getLandDetailInfo(222);

        verify(landDetailView).showLoading();
        verify(landDetailView).showMessage("원룸 정보를 받아오지 못했습니다.");
        verify(landDetailView).hideLoading();
    }
}
