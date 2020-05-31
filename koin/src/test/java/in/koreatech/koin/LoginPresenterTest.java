package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Auth;
import in.koreatech.koin.data.network.interactor.TokenSessionInteractor;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.ui.login.presenter.LoginContract;
import in.koreatech.koin.ui.login.presenter.LoginPresenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoginPresenterTest {
    @Mock
    private LoginContract.View loginView;
    @Mock
    private UserInteractor userInteractor;
    @Mock
    private TokenSessionInteractor tokenSessionInteractor;
    @Mock
    private Auth auth;
    private LoginPresenter loginPresenter;
    private ApiCallback apiCallback;


    @Before
    public void setLoginPresenter() {
        MockitoAnnotations.initMocks(this);
        loginPresenter = new LoginPresenter(loginView, userInteractor, tokenSessionInteractor);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        loginPresenter = new LoginPresenter(loginView, userInteractor, tokenSessionInteractor);
        verify(loginView).setPresenter(loginPresenter);
    }

    @Test
    public void errorLogin_EmptyId_ShowsToastMessage() {
        String id = "";
        String password = "password";
        loginPresenter.login(id, password, false);
        verify(loginView).showMessage(R.string.login_email_password_empty_string_warning);
    }

    @Test
    public void errorLogin_NullId_ShowsToastMessage() {
        String id = null;
        String password = "password";
        loginPresenter.login(id, password, false);
        verify(loginView).showMessage(R.string.login_email_password_empty_string_warning);
    }

    @Test
    public void errorLogin_EmptyPassword_ShowsToastMessage() {
        String id = "id";
        String password = "";
        loginPresenter.login(id, password, false);
        verify(loginView).showMessage(R.string.login_email_password_empty_string_warning);
    }

    @Test
    public void errorLogin_NullPassword_ShowsToastMessage() {
        String id = "id";
        String password = null;
        loginPresenter.login(id, password, false);
        verify(loginView).showMessage(R.string.login_email_password_empty_string_warning);
    }

    @Test
    public void errorLoginFromServer_ShowsToastMessage(){
        Exception exception = new Exception();
        String id = "id";
        String password = "password";


        doAnswer(invocation -> {
            assertEquals(invocation.getArgument(1), password);
            apiCallback = invocation.getArgument(3);
            apiCallback.onFailure(exception);
            return null;
        }).when(userInteractor).readToken(anyString(), anyString(), anyBoolean(), any(ApiCallback.class));

        loginPresenter.login(id, password, false);
        verify(loginView).showProgress();
        verify(loginView).hideProgress();
        verify(loginView).showMessage(R.string.login_failed);
    }


    @Test
    public void loginFromServer_GotoMain() {
        String id = "id";
        String password = "password";
        String sha256Password = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        doAnswer(invocation -> {
            assertEquals(invocation.getArgument(1), password);
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(auth);
            return null;
        }).when(userInteractor).readToken(anyString(), anyString(), anyBoolean(), any(ApiCallback.class));

        doAnswer(invocation -> {
            assertEquals(invocation.getArgument(1), sha256Password);
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(auth);
            return null;
        }).when(tokenSessionInteractor).saveSession(any(), anyString(), any(ApiCallback.class));

        loginPresenter.login(id, password, false);
        verify(loginView).showProgress();
        verify(loginView).gotoMain();
        verify(loginView, times(2)).hideProgress();
    }

}
