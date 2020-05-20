package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleInteractor;
import in.koreatech.koin.ui.circle.presenter.CircleContract;
import in.koreatech.koin.ui.circle.presenter.CirclePresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class CirclePresenterTest {
    @Mock
    private CircleContract.View circleView;
    @Mock
    private CircleInteractor circleInteractor;
    private CirclePresenter circlePresenter;
    private ApiCallback apiCallback;
    private  Circle circle;
    private ArrayList<Circle> circleList;


    @Before
    public void setCirclePresenter(){
        MockitoAnnotations.initMocks(this);
        circlePresenter = new CirclePresenter(circleView, circleInteractor);
        circle = new Circle();
        ArrayList<Circle> circles = new ArrayList<>();
        circles.add(circle);
        circle.setTotalPage(4);
        circle.setCircles(circles);
        circleList = new ArrayList<>();
        circleList.add(circle);
    }

    @Test
    public void createPresenter_setsThePresenterToView(){
        circlePresenter = new CirclePresenter(circleView , circleInteractor);
        verify(circleView).setPresenter(circlePresenter);
    }

    @Test
    public void loadCircleListFromServerLoadIntoView(){
        ArrayList<Circle> circleArrayList = new ArrayList<>();
        for(int i = 0 ; i < circle.getTotalPage() ; i++) {
            circleArrayList.addAll(circle.getCircles());
        }

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(circle);
            return null;
        }).when(circleInteractor).readCircleList(anyInt(), any(ApiCallback.class));
        circlePresenter.getCircleList(1);
        verify(circleView).showLoading();
        verify(circleView).onCircleListDataReceived(circleArrayList);
        verify(circleView).hideLoading();
    }

    @Test
    public void errorLoadCircleListFromServer_ShowsToastMessage(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(circleInteractor).readCircleList(anyInt(), any(ApiCallback.class));
        circlePresenter.getCircleList(1);
        verify(circleView).showLoading();
        verify(circleView).showMessage(R.string.circle_get_list_fail);
        verify(circleView).hideLoading();
    }



}
