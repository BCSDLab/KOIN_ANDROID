package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.CallvanInteractor;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoContract;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class UserInfoPresenterTest {

    @Mock
    private UserInfoContract.View userInfoView;
    @Mock
    private UserInteractor userInteractor;
    @Mock
    private CallvanInteractor callvanInteractor;
    private UserInfoPresenter userInfoPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setUserInfoPresenter() {
        MockitoAnnotations.initMocks(this);
        userInfoPresenter = new UserInfoPresenter(userInfoView, userInteractor, callvanInteractor);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        userInfoPresenter = new UserInfoPresenter(userInfoView, userInteractor, callvanInteractor);
        verify(userInfoView).setPresenter(userInfoPresenter);
    }

    @Test
    public void deleteUserFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(new DefaultResponse());
            return null;
        }).when(userInteractor).deleteUser(any(ApiCallback.class));
        userInfoPresenter.deleteUser();
        verify(userInfoView).onDeleteUserReceived();
    }

    @Test
    public void errorDeleteUserFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(userInteractor).deleteUser(any(ApiCallback.class));
        userInfoPresenter.deleteUser();
        verify(userInfoView).showMessage(exception.getMessage());
    }

    @Test
    public void loadUserDataFromServerLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(new DefaultResponse());
            return null;
        }).when(userInteractor).readUser(any(ApiCallback.class));
        userInfoPresenter.getUserData();
        verify(userInfoView).onUserDataReceived();
    }

    @Test
    public void errorLoadUserDataFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(userInteractor).readUser(any(ApiCallback.class));
        userInfoPresenter.getUserData();
        verify(userInfoView).showMessage(exception.getMessage());
    }

}
