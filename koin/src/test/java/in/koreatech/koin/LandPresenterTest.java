package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Land;
import in.koreatech.koin.data.network.interactor.LandInteractor;
import in.koreatech.koin.ui.land.presenter.LandContract;
import in.koreatech.koin.ui.land.presenter.LandPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class LandPresenterTest {
    @Mock
    LandContract.View landView;
    @Mock
    LandInteractor landInteractor;

    private LandPresenter landPresenter;
    private ApiCallback apiCallback;
    private Land land;
    private ArrayList<Land> landArrayList;

    @Before
    public void setupLandPresenter() {
        MockitoAnnotations.initMocks(this);
        landPresenter = new LandPresenter(landView, landInteractor);
        land = new Land();
        landArrayList = new ArrayList<>();
        land.setLands(landArrayList);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        landPresenter = new LandPresenter(landView, landInteractor);
        verify(landView).setPresenter(landPresenter);
    }

    @Test
    public void loadLandListFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(land);
            return null;
        }).when(landInteractor).readLandList(any(ApiCallback.class));

        landPresenter.getLandList();

        verify(landView).showLoading();
        verify(landView).onLandListDataReceived(landArrayList);
        verify(landView).hideLoading();
    }

    @Test
    public void errorLandListFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(landInteractor).readLandList(any(ApiCallback.class));

        landPresenter.getLandList();

        verify(landView).showLoading();
        verify(landView).showMessage("원룸 리스트를 받아오지 못했습니다");
        verify(landView).hideLoading();
    }
}
