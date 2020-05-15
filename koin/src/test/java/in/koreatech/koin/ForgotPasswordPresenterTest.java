package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.ui.forgotpassword.presenter.ForgotPasswordContract;
import in.koreatech.koin.ui.forgotpassword.presenter.ForgotPasswordPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class ForgotPasswordPresenterTest {

    @Mock
    ForgotPasswordContract.View forgotPasswordView;
    @Mock
    UserInteractor userInteractor;
    private ForgotPasswordPresenter forgotPasswordPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setupForgotPasswordPresenter() {
        MockitoAnnotations.initMocks(this);
        forgotPasswordPresenter = new ForgotPasswordPresenter(forgotPasswordView, userInteractor);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        forgotPasswordPresenter = new ForgotPasswordPresenter(forgotPasswordView, userInteractor);
        verify(forgotPasswordView).setPresenter(forgotPasswordPresenter);
    }

    @Test
    public void errorFindPassword_EmptyId_ShowsToastMessage() {
        forgotPasswordPresenter.findPassword("");
        verify(forgotPasswordView).showMessage(R.string.email_empty_string_warning);
    }

    @Test
    public void errorFindPassword_NullId_ShowsToastMessage() {
        forgotPasswordPresenter.findPassword(null);
        verify(forgotPasswordView).showMessage(R.string.email_empty_string_warning);
    }

    @Test
    public void errorFindPasswordFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(userInteractor).createFindPassword(anyString(), any(ApiCallback.class));
        forgotPasswordPresenter.findPassword("id");
        verify(forgotPasswordView).showMessage(exception.getMessage());
        verify(forgotPasswordView).hideProgress();
    }

    @Test
    public void findPasswordFromServer_GotoEmail() {
        DefaultResponse defaultResponse = new DefaultResponse();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(defaultResponse);
            return null;
        }).when(userInteractor).createFindPassword(anyString(), any(ApiCallback.class));
        forgotPasswordPresenter.findPassword("id");
        verify(forgotPasswordView).goToEmail();
        verify(forgotPasswordView).hideProgress();
    }


}
