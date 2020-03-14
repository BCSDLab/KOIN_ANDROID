package in.koreatech.koin.ui.userinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoContract;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoPresenter;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.ui.login.LoginActivity;

public class UserInfoActivity extends KoinNavigationDrawerActivity implements UserInfoContract.View {
    private final String TAG = "UserInfoActivity";
    private Context context;

    private User user;
    private int requiredService;
    private UserInfoPresenter userInfoPresenter;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        this.context = this;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        userInfoPresenter.getUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_info, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @OnClick(R.id.koin_base_app_bar)
    public void onClickBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            startActivity(new Intent(this, UserInfoEditedActivity.class));
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
        Intent intent = getIntent();
        this.requiredService = intent.getIntExtra("CONDITION", -1);

        switch (this.requiredService) {
            case R.string.navigation_item_free_board:
            case R.string.navigation_item_recruit_board:
                String userName = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserName();
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

    @Override
    public void onBackPressed() {
        setLastNavigationItem();
        finish();
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
        onClickNavigationLogout();
    }

    @Override
    public void onDeleteUserReceived() {
        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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

