package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.network.interactor.UserInteractor;
import in.koreatech.koin.data.network.response.UserInfoEditResponse;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoEditContract;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoEditPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserInfoEditPresenterTest {
    @Mock
    private UserInfoEditContract.View userInfoEditView;
    @Mock
    private UserInteractor userInteractor;
    @Mock
    private User user;
    private UserInfoEditResponse userInfoEditResponse;
    private UserInfoEditPresenter userInfoEditPresenter;
    private ApiCallback apiCallback;

    @Before
    public void setUserEditInfoPresenter() {
        MockitoAnnotations.initMocks(this);
        userInfoEditPresenter = new UserInfoEditPresenter(userInfoEditView, userInteractor);
        userInfoEditResponse = new UserInfoEditResponse();
        userInfoEditResponse.success = "success";
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        userInfoEditPresenter = new UserInfoEditPresenter(userInfoEditView, userInteractor);
        verify(userInfoEditView).setPresenter(userInfoEditPresenter);
    }

    @Test
    public void errorUserNickNameCheck_WrongNickNameFormat_ShowsToastMessage() {
        String[] wrongNickName = {"꒰⑅ᵕ༚ᵕ꒱", "123456789123", "( ღ'ᴗ'ღ )", "⭐", "꒰◍ॢ•ᴗ•◍ॢ꒱", "⭐1!"};
        for (String nickname : wrongNickName) {
            userInfoEditPresenter.getUserCheckNickName(nickname);
        }
        verify(userInfoEditView, times(wrongNickName.length)).showMessage(R.string.user_info_check_nickname_warning);
    }

    @Test
    public void errorUserNickNameCheckFromServerShowCheckNickNameFail() {
        Exception exception = new Exception();
        String[] rightNickname = {"a", "123456789", "a!abc", "12Bsd", "t!./,><", "@1!"};
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(userInteractor).readCheckUserNickName(anyString(), any(ApiCallback.class));
        for (String nickname : rightNickname) {
            userInfoEditPresenter.getUserCheckNickName(nickname);
        }
        verify(userInfoEditView, times(rightNickname.length)).showCheckNickNameFail();
    }

    @Test
    public void userNickNameCheckFromServerShowCheckNickNameSuccess() {
        String[] rightNickname = {"a", "123456789", "a!abc", "12Bsd", "t!./,><", "@1!"};
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onSuccess(userInfoEditResponse);
                    return null;
                }
        ).when(userInteractor).readCheckUserNickName(anyString(), any(ApiCallback.class));

        for (String nickname : rightNickname) {
            userInfoEditPresenter.getUserCheckNickName(nickname);
        }
        verify(userInfoEditView, times(rightNickname.length)).showCheckNickNameSuccess();

    }

    @Test
    public void updateUserInfoFromServeLoadIntoView(){
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onSuccess(user);
            return null;
        }).when(userInteractor).updateUser(any(ApiCallback.class),any(User.class));
        userInfoEditPresenter.updateUserInfo(user);
        verify(userInfoEditView).showConfirm();
    }

    @Test
    public void errorUpdateUserInfoFromServe_ShowsConfirmFail(){
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(0);
            apiCallback.onFailure(exception);
            return null;
        }).when(userInteractor).updateUser(any(ApiCallback.class),any(User.class));
        userInfoEditPresenter.updateUserInfo(user);
        verify(userInfoEditView).showConfirmFail();
    }



}
