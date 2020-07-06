package in.koreatech.koin.ui.userinfo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoContract;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoPresenter;
import in.koreatech.koin.util.AuthorizeManager;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.NavigationManger;
import in.koreatech.koin.util.SnackbarUtil;

public class UserInfoFragment extends KoinBaseFragment implements UserInfoContract.View {
    private final String TAG = "UserInfoFragment";
    /* View Component */
    @BindView(R.id.userinfo_scrollview)
    public ScrollView userinfoScrollview;
    //default data
    @BindView(R.id.userinfo_textview_id)
    public TextView userinfoTextviewId;
    @BindView(R.id.userinfo_textview_name)
    public TextView userinfoTextviewName;
    @BindView(R.id.userinfo_textview_nick_name)
    public TextView userinfoTextviewNickname;
    @BindView(R.id.userinfo_textview_anonymous_nick_name)
    public TextView userinfoTextviewAnonymousNickname;
    @BindView(R.id.userinfo_textview_phone_num)
    public TextView userinfoTextviewPhone;
    @BindView(R.id.userinfo_textview_gender)
    public TextView userinfoTextviewGender;
    //univ. data
    @BindView(R.id.userinfo_textview_student_id)
    public TextView userinfoTextviewStudentId;
    @BindView(R.id.userinfo_textview_major)
    public TextView userinfoTextviewMajor;
    private Context context;
    private User user;
    private int requiredService;
    private UserInfoPresenter userInfoPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_info, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        userInfoPresenter.getUserData();
    }

    @OnClick(R.id.koin_base_app_bar)
    public void onClickBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_userinfo_edited_action, null, NavigationManger.getNavigationAnimation());
    }

    private void init() {
        new UserInfoPresenter(this);
    }

    @Override
    public void setPresenter(UserInfoPresenter presenter) {
        this.userInfoPresenter = presenter;
    }

    @Override
    public void onUserDataReceived() {
        this.user = UserInfoSharedPreferencesHelper.getInstance().loadUser();

        userinfoTextviewId.setText(isNull(this.user.getUserId()) + "@koreatech.ac.kr");
        userinfoTextviewName.setText(isNull(this.user.getUserName()));
        userinfoTextviewNickname.setText(isNull(this.user.getUserNickName()));
        userinfoTextviewAnonymousNickname.setText(isNull(this.user.getAnonymousNickName()));
        userinfoTextviewPhone.setText(isNull(this.user.getPhoneNumber()));
        userinfoTextviewGender.setText(this.user.getGender() == 0 ? "남자" : "여자");

        userinfoTextviewStudentId.setText(isNull(this.user.getStudentId()));
        userinfoTextviewMajor.setText(isNull(this.user.getMajor()));

        checkRequiredInfo();
    }

    private String isNull(String str) {
        return FormValidatorUtil.validateStringIsEmpty(str) ? "-" : str;
    }

    @Override
    public void checkRequiredInfo() {
        this.requiredService = getArguments().getInt("CONDITION", -1);

        switch (this.requiredService) {
            case R.string.navigation_item_free_board:
            case R.string.navigation_item_recruit_board:
                String userName = AuthorizeManager.getUser(getContext()).getUserName();
                if (FormValidatorUtil.validateStringIsEmpty(userName) && userinfoTextviewName.getText().equals("-")) {
                    userinfoTextviewName.setText("이름을 입력해주세요");
//                    userinfoTextviewName.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if (userinfoTextviewNickname.getText().equals("-")) {
                    userinfoTextviewNickname.setText("닉네임을 입력해주세요");
//                    userinfoTextviewNickname.setTypeface(Typeface.DEFAULT_BOLD);
                }
                break;
            case R.string.navigation_item_callvan_sharing:
                if (userinfoTextviewName.getText().equals("-")) {
                    userinfoTextviewName.setText("이름을 입력해주세요");
//                    userinfoTextviewName.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if (userinfoTextviewPhone.getText().equals("-")) {
                    userinfoTextviewPhone.setText("전화 번호를 입력해주세요");
//                    userinfoTextviewPhone.setTypeface(Typeface.DEFAULT_BOLD);
                }
                break;
            default:
//                userinfoTextviewNickname.setTypeface(Typeface.DEFAULT);
//                userinfoTextviewPhone.setTypeface(Typeface.DEFAULT);
                break;
        }
    }


    @OnClick(R.id.userinfo_button_delete_user)
    public void onClickDeleteUserButton() {
        final int roomUid = UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid();

        SnackbarUtil.makeLongSnackbarActionYes(userinfoScrollview, "정말 탈퇴하시려구요? 한번 더 생각해보세요 :)", new Runnable() {
            @Override
            public void run() {
                userInfoPresenter.deleteUser(roomUid);
            }
        });
    }

    @OnClick(R.id.userinfo_button_logout_user)
    public void onClickLogoutButton() {
        NavigationManger.logout(getActivity());
    }

    @Override
    public void onDeleteUserReceived() {
        NavigationManger.logout(getActivity());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }
}

