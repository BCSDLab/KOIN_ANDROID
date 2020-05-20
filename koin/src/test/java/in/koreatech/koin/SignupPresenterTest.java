package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.ui.signup.presenter.SignupContract;
import in.koreatech.koin.ui.signup.presenter.SignupPresenter;
import retrofit2.HttpException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SignupPresenterTest {
    @Mock
    private SignupContract.View signupView;
    @Mock
    private UserInteractor userInteractor;
    @Mock
    HttpException httpException;
    private SignupPresenter signupPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setSignupPresenter() {
        MockitoAnnotations.initMocks(this);
        signupPresenter = new SignupPresenter(signupView, userInteractor);
        when(httpException.code()).thenReturn(409);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        signupPresenter = new SignupPresenter(signupView, userInteractor);
        verify(signupView).setPresenter(signupPresenter);
    }

    @Test
    public void errorSignup_NullID_ShowsToastMessage() {
        String id = null;
        String password = "a1234567!";
        signupPresenter.signUp(id, password);
        verify(signupView).showMessage(R.string.signup_id_input_warning);
    }

    @Test
    public void errorSignup_EmptyID_ShowsToastMessage() {
        String id = "";
        String password = "a1234567!";
        signupPresenter.signUp(id, password);
        verify(signupView).showMessage(R.string.signup_id_input_warning);
    }

    @Test
    public void errorSignup_NullPassword_ShowsToastMessage() {
        String id = "test";
        String password = null;
        signupPresenter.signUp(id, password);
        verify(signupView).showMessage(R.string.signup_password_input_warning);
    }

    @Test
    public void errorSignup_EmptyPassword_ShowsToastMessage() {
        String id = "test";
        String password = "";
        signupPresenter.signUp(id, password);
        verify(signupView).showMessage(R.string.signup_password_input_warning);
    }

    @Test
    public void errorSignup_WrongEmailFormat_ShowsToastMessage() {
        String[] wrongId = {"ID", "!id", "!?.id", "1234567891234", "worngid!@#$%#@", "!@#$%^&*()-="};
        String password = "a123456!";
        for (String s : wrongId) {
            signupPresenter.signUp(s, password);
        }
        verify(signupView, times(wrongId.length)).showMessage(R.string.signup_email_format_warning);
    }

    @Test
    public void errorSignup_WrongPasswordFormat_ShowsToastMessage() {
        String id = "id";
        String[] wrongPassword = {"1", "a", "!", "a123456", "123456", "123456!", "a123!", "a12345123451234512345!", "!@#$%^&*()_+=-.,!a1"};
        for (String s : wrongPassword) {
            signupPresenter.signUp(id, s);
        }
        verify(signupView, times(wrongPassword.length)).showMessage(R.string.signup_password_format_warning);
    }

    @Test
    public void signupFromServerGoToEmail() {
        String[] rightID = {"id", "a", "a1234", "12345a", "1234idtest"};
        String[] rightPassword = {"a12345!", "!@#$%^&*()_a1", "<>/!2asder!.a!a", "-123456u"};
        int totalTestCase = rightID.length * rightPassword.length;
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(new Object());
            return null;
        }).when(userInteractor).createToken(anyString(), anyString(), any(ApiCallback.class));
        for (int a = 0; a < rightID.length; a++) {
            for (int b = 0; b < rightPassword.length; b++) {
                signupPresenter.signUp(rightID[a], rightPassword[b]);
            }
        }

        verify(signupView, times(totalTestCase)).buttonClickBlock(true);
        verify(signupView, times(totalTestCase)).showProgress();
        verify(signupView, times(totalTestCase)).gotoEmail();
        verify(signupView, times(totalTestCase)).hideProgress();
        verify(signupView, times(totalTestCase)).buttonClickBlock(false);
    }

    @Test
    public void erroSignupFromServer_HttpException409_ShowsToastMessage() {

        String[] rightID = {"id", "a", "a1234", "12345a", "1234idtest"};
        String[] rightPassword = {"a12345!", "!@#$%^&*()_a1", "<>/!2asder!.a!a", "-123456u"};
        int totalTestCase = rightID.length * rightPassword.length;
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(httpException);
            return null;
        }).when(userInteractor).createToken(anyString(), anyString(), any(ApiCallback.class));
        for (int a = 0; a < rightID.length; a++) {
            for (int b = 0; b < rightPassword.length; b++) {
                signupPresenter.signUp(rightID[a], rightPassword[b]);
            }
        }

        verify(signupView, times(totalTestCase)).buttonClickBlock(true);
        verify(signupView, times(totalTestCase)).showProgress();
        verify(signupView, times(totalTestCase)).showMessage(R.string.email_already_send_or_email_requested);
        verify(signupView, times(totalTestCase)).hideProgress();
        verify(signupView, times(totalTestCase)).buttonClickBlock(false);
    }

    @Test
    public void erroSignupFromServer_Exception_ShowsToastMessage() {

        String[] rightID = {"id", "a", "a1234", "12345a", "1234idtest"};
        String[] rightPassword = {"a12345!", "!@#$%^&*()_a1", "<>/!2asder!.a!a", "-123456u"};
        int totalTestCase = rightID.length * rightPassword.length;
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(new Exception());
            return null;
        }).when(userInteractor).createToken(anyString(), anyString(), any(ApiCallback.class));
        for (int a = 0; a < rightID.length; a++) {
            for (int b = 0; b < rightPassword.length; b++) {
                signupPresenter.signUp(rightID[a], rightPassword[b]);
            }
        }

        verify(signupView, times(totalTestCase)).buttonClickBlock(true);
        verify(signupView, times(totalTestCase)).showProgress();
        verify(signupView, times(totalTestCase)).showMessage(R.string.error_network);
        verify(signupView, times(totalTestCase)).hideProgress();
        verify(signupView, times(totalTestCase)).buttonClickBlock(false);
    }


}
