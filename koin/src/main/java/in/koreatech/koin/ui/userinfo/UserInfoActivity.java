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
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoContract;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.ui.userinfo.presenter.UserInfoPresenter;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.ui.login.LoginActivity;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class UserInfoActivity extends KoinNavigationDrawerActivity implements UserInfoContract.View {
    private final String TAG = "UserInfoActivity";
    private Context mContext;

    private User mUser;
    private int mRequiredService;
    private UserInfoContract.Presenter mUserInfoPresenter;

    /* View Component */
    @BindView(R.id.userinfo_scrollview)
    public ScrollView mUserinfoScrollview;

    //default data
    @BindView(R.id.userinfo_textview_id)
    public TextView mUserinfoTextviewId;
    @BindView(R.id.userinfo_textview_name)
    public TextView mUserinfoTextviewName;
    @BindView(R.id.userinfo_textview_nick_name)
    public TextView mUserinfoTextviewNickname;
    @BindView(R.id.userinfo_textview_anonymous_nick_name)
    public TextView mUserinfoTextviewAnonymousNickname;
    @BindView(R.id.userinfo_textview_phone_num)
    public TextView mUserinfoTextviewPhone;
    @BindView(R.id.userinfo_textview_gender)
    public TextView mUserinfoTextviewGender;

    //univ. data
    @BindView(R.id.userinfo_textview_student_id)
    public TextView mUserinfoTextviewStudentId;
    @BindView(R.id.userinfo_textview_major)
    public TextView mUserinfoTextviewMajor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        this.mContext = this;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mUserInfoPresenter.getUserData();
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
        if (id == AppbarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppbarBase.getRightButtonId())
            startActivity(new Intent(this, UserInfoEditedActivity.class));
    }

    private void init() {
        new UserInfoPresenter(this);
    }

    @Override
    public void setPresenter(UserInfoContract.Presenter presenter) {
        this.mUserInfoPresenter = presenter;
    }

    @Override
    public void onUserDataReceived() {
        mUser = UserInfoSharedPreferencesHelper.getInstance().loadUser();

        mUserinfoTextviewId.setText(isNull(mUser.userId) + "@koreatech.ac.kr");
        mUserinfoTextviewName.setText(isNull(mUser.userName));
        mUserinfoTextviewNickname.setText(isNull(mUser.userNickName));
        mUserinfoTextviewAnonymousNickname.setText(isNull(mUser.anonymousNickName));
        mUserinfoTextviewPhone.setText(isNull(mUser.phoneNumber));
        mUserinfoTextviewGender.setText(mUser.gender == 0 ? "남자" : "여자");

        mUserinfoTextviewStudentId.setText(isNull(mUser.studentId));
        mUserinfoTextviewMajor.setText(isNull(mUser.major));

        checkRequiredInfo();
    }

    private String isNull(String str) {
        return FormValidatorUtil.validateStringIsEmpty(str) ? "-" : str;
    }

    @Override
    public void checkRequiredInfo() {
        Intent intent = getIntent();
        mRequiredService = intent.getIntExtra("CONDITION", -1);

        switch (mRequiredService) {
            case R.string.navigation_item_free_board:
            case R.string.navigation_item_recruit_board:
                String userName = UserInfoSharedPreferencesHelper.getInstance().loadUser().userName;
                if (FormValidatorUtil.validateStringIsEmpty(userName) && mUserinfoTextviewName.getText().equals("-")) {
                    mUserinfoTextviewName.setText("이름을 입력해주세요");
//                    mUserinfoTextviewName.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if (mUserinfoTextviewNickname.getText().equals("-")) {
                    mUserinfoTextviewNickname.setText("닉네임을 입력해주세요");
//                    mUserinfoTextviewNickname.setTypeface(Typeface.DEFAULT_BOLD);
                }
                break;
            case R.string.navigation_item_callvan_sharing:
                if (mUserinfoTextviewName.getText().equals("-")) {
                    mUserinfoTextviewName.setText("이름을 입력해주세요");
//                    mUserinfoTextviewName.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if (mUserinfoTextviewPhone.getText().equals("-")) {
                    mUserinfoTextviewPhone.setText("전화 번호를 입력해주세요");
//                    mUserinfoTextviewPhone.setTypeface(Typeface.DEFAULT_BOLD);
                }
                break;
            default:
//                mUserinfoTextviewNickname.setTypeface(Typeface.DEFAULT);
//                mUserinfoTextviewPhone.setTypeface(Typeface.DEFAULT);
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

        SnackbarUtil.makeLongSnackbarActionYes(mUserinfoScrollview, "정말 탈퇴하시려구요? 한번 더 생각해보세요 :)", new Runnable() {
            @Override
            public void run() {
                mUserInfoPresenter.deleteUser(roomUid);
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

