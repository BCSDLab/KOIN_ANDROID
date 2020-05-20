package in.koreatech.koin;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleInteractor;
import in.koreatech.koin.ui.circle.presenter.CircleDetailContract;
import in.koreatech.koin.ui.circle.presenter.CircleDetailPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class CircleDetailPresenterTest {

    @Mock
    private CircleDetailContract.View circleDetailView;
    @Mock
    private CircleInteractor circleInteractor;
    private CircleDetailPresenter circleDetailPresenter;
    private ApiCallback apiCallback;
    private Circle circle;


    @Before
    public void setCircleDetailPresenter() {
        MockitoAnnotations.initMocks(this);
        circleDetailPresenter = new CircleDetailPresenter(circleDetailView, circleInteractor);
        circle = new Circle();
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        circleDetailPresenter = new CircleDetailPresenter(circleDetailView, circleInteractor);
        verify(circleDetailView).setPresenter(circleDetailPresenter);
    }

    @Test
    public void loadCircleInfoFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(circle);
            return null;
        }).when(circleInteractor).readCircle(anyInt(), any(ApiCallback.class));
        circleDetailPresenter.getCircleInfo(1);
        verify(circleDetailView).showLoading();
        verify(circleDetailView).onCircleDataReceived(circle);
        verify(circleDetailView).hideLoading();
    }

    @Test
    public void errorLoadCircleInfoFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(circleInteractor).readCircle(anyInt(), any(ApiCallback.class));
        circleDetailPresenter.getCircleInfo(1);
        verify(circleDetailView).showLoading();
        verify(circleDetailView).showMessage(R.string.circle_get_fail);
        verify(circleDetailView).hideLoading();
    }
}
