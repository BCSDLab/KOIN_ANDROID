package in.koreatech.koin.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.constant.AuthorizeConstant;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.core.toast.ToastUtil;

import static androidx.drawerlayout.widget.DrawerLayout.STATE_DRAGGING;

public abstract class BaseNavigationActivity extends ActivityBase implements View.OnClickListener {
    public static final int NAVIGATION_OPEN_REQUEST = 1000;
    public static final String IS_NAVIGATION_CLICKED = "IS_NAVIGATION_CLICKED";
    public static final String NAVIGATION_ID = "NAVIGATION_ID";
    private final String TAG = "BaseNavigationActivity";
    private Context context;
    private long pressTime = 0;
    private LinearLayout openLeftNavigationDrawerOpenLinearLayout;
    private LinearLayout openHomeLinearLayout;
    private LinearLayout openSearchLinearLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = setContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        toggleIcon(NavigationManager.getInstance().getCurrentService());
        this.openHomeLinearLayout = findViewById(getBottomNavigationHomeID());
        this.openSearchLinearLayout = findViewById(getBottomNavigationSearchID());
        this.openLeftNavigationDrawerOpenLinearLayout = findViewById(getBottomNavigationCategoryID());
        this.openLeftNavigationDrawerOpenLinearLayout.setOnClickListener(this);
        this.openHomeLinearLayout.setOnClickListener(this);
        this.openSearchLinearLayout.setOnClickListener(this);
    }

    protected abstract Context setContext();


    public void toggleIcon(int id) {
        ImageView imageView;
        TextView textView;
        clearToggle();
        if (id == R.id.navi_item_home) {
            imageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_home_on);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (id == R.id.navi_item_search) {
            imageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
            imageView.setBackgroundResource(R.drawable.ic_search_menu_blue);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            imageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_home);
            textView.setTextColor(getResources().getColor(R.color.black));
            imageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
            imageView.setBackgroundResource(R.drawable.ic_search_menu);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public void clearToggle() {
        ImageView imageView;
        TextView textView;
        imageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
        textView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
        imageView.setBackgroundResource(R.drawable.ic_bottom_home);
        textView.setTextColor(getResources().getColor(R.color.black));
        imageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);
        textView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
        imageView.setBackgroundResource(R.drawable.ic_search_menu);
        textView.setTextColor(getResources().getColor(R.color.black));
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

    protected abstract int[] getMenuId();

    protected abstract int[] getMenuTextviewId();

    protected abstract int getBottomNavigationCategoryID();

    protected abstract int getBottomNavigationHomeID();

    protected abstract int getBottomNavigationSearchID();

    protected abstract int getDrawerLayoutID();

    protected abstract int getLeftNavigationDrawerID();


    // TODO -> 익명게시판 수정 후 주석 제거
    public void callDrawerItem(int itemId) {
        if (!NavigationManager.getInstance().goToService(itemId))
            return;

        if (itemId == R.id.navi_item_home) {
            goToMainActivity();
        } else if (itemId == R.id.navi_item_store) {
            goToStoreActivity();
        } else if (itemId == R.id.navi_item_dining) {
            goToDiningActivity();
        } else if (itemId == R.id.navi_item_bus) {
            goToBusActivity();
        } else if (itemId == R.id.navi_item_cirlce) {
            goToCircleActivity();
        } else if (itemId == R.id.navi_item_free_board) {
            goToFreeBoardActivity();
        } else if (itemId == R.id.navi_item_recruit_board) {
            goToRecruitBoardActivity();
        } else if (itemId == R.id.navi_item_anonymous_board) {
            goToAnonymousBoardActivity();
//        } else if (itemId == R.id.navi_item_callvansharing) {
//            onClickNavigationCallvanshring();
        } else if (itemId == R.id.navi_item_usedmarket) {
            goToMarketActivity();
        } else if (itemId == R.id.navi_item_lostfound) {
            goToLostFoundActivity();
        } else if (itemId == R.id.navi_item_login) {
            onClickNavigationLogin();
        } else if (itemId == R.id.navi_item_kakao_talk) {
            onClickNavigationkakaoTalk();
            NavigationManager.getInstance().goToBeforeService();
        } else if (itemId == R.id.navi_item_developer) {
            onClickNavigationDeveloper();
            NavigationManager.getInstance().goToBeforeService();
        } else if (itemId == R.id.navi_item_user_info) {
            onClickNavigationUserInfo();
        } else if (itemId == R.id.navi_item_timetable) {
            onClickTimeTable();
        } else if (itemId == R.id.navi_item_land) {
            goToLandActivity();
        } else if (itemId == R.id.navi_item_myinfo) {
            onClickNavigationUserInfo();
        } else if (itemId == R.id.navi_item_search) {
            goToSearchActivity();
        } else {
            ToastUtil.getInstance().makeShort("서비스예정입니다");
        }
    }

    @Override
    public void onClick(View v) {
        int servicedId = v.getId();
        if (servicedId == getBottomNavigationCategoryID()) {
            openNavigationDrawer();
            return;
        }
        if (servicedId == getBottomNavigationHomeID())
            servicedId = R.id.navi_item_home;
        else if (servicedId == getBottomNavigationSearchID())
            servicedId = R.id.navi_item_search;

        callDrawerItem(servicedId);
        toggleIcon(servicedId);
    }

    public void openNavigationDrawer() {
        startActivityForResult(new Intent(this, KoinNavigationDrawer.class), NAVIGATION_OPEN_REQUEST);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NAVIGATION_OPEN_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                boolean isNavigationClicked = data.getBooleanExtra(IS_NAVIGATION_CLICKED, false);
                if (isNavigationClicked) {
                    int serviceId = data.getIntExtra(NAVIGATION_ID, -1);
                    if (serviceId != -1)
                        callDrawerItem(serviceId);
                }
            }
        }

    }


    private void onClickNavigationUserInfo() {
        checkUserInfoEnough(R.string.navigation_item_user_info);
    }

    public void onClickTimeTable() {
        if (getAuthorize() != AuthorizeConstant.ANONYMOUS)
            goToTimetableActivty();
        else
            goToAnonymousTimeTableActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer_right_open, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > pressTime + 2000) {
            if (NavigationManager.getInstance().getCurrentService() != R.id.navi_item_home)
                callDrawerItem(R.id.navi_item_home);
            else {
                pressTime = System.currentTimeMillis();
                ToastUtil.getInstance().makeShort("뒤로가기 버튼을 한 번 더 누르면 종료됩니다.");
            }
        } else {
            finishAffinity();
        }
    }

    private AuthorizeConstant getAuthorize() {
        AuthorizeConstant authorizeConstant;
        try {
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        }
        return authorizeConstant;
    }

    private String getName() {
        String name;
        try {
            name = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserName();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            name = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserName();
        }
        return (name != null) ? name : "";
    }

    public void checkUserInfoEnough(int service) {
        if (getAuthorize() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            NavigationManager.getInstance().goToBeforeService();
            return;
        }
        User user = UserInfoSharedPreferencesHelper.getInstance().loadUser();

        //TODO:홈 만들 경우 유저 이름 체크 홈으로 이동

        if (service == R.string.home || service == R.string.navigation_item_free_board) {
            if (FormValidatorUtil.validateStringIsEmpty(user.getUserNickName()) || FormValidatorUtil.validateStringIsEmpty(user.getUserName())) {
                ToastUtil.getInstance().makeShort("해당 서비스를 이용하기 위해 사용자 정보를 입력해주세요.");
                goToUserInfoActivity(service);
                NavigationManager.getInstance().goToBeforeService();
            } else {
                goToFreeBoardActivity();
            }

        } else if (service == R.string.navigation_item_recruit_board) {
            if (FormValidatorUtil.validateStringIsEmpty(user.getUserNickName()) || FormValidatorUtil.validateStringIsEmpty(user.getUserName())) {
                ToastUtil.getInstance().makeShort("해당 서비스를 이용하기 위해 사용자 정보를 입력해주세요.");
                goToUserInfoActivity(service);
                NavigationManager.getInstance().goToBeforeService();
            } else {
                goToRecruitBoardActivity();
            }
        } else if (service == R.string.navigation_item_anonymous_board) {
            goToAnonymousBoardActivity();
        }
        //콜밴쉐어링 hold
//        else if (service == R.string.navigation_item_callvan_sharing) {
//            if (FormValidatorUtil.validateStringIsEmpty(user.userName) || FormValidatorUtil.validateStringIsEmpty(user.phoneNumber)) {
//                ToastUtil.makeLong(context, "해당 서비스를 이용하기 위해 사용자 정보를 입력해주세요.");
//                goToUserInfoActivity(service);
//                currentId = beforeId;
//            } else {
//                goToCallvanActivity();
//            }
//        }
        else if (service == R.string.navigation_item_usedmarket) {
            goToMarketActivity();
        } else if (service == R.string.navigation_item_user_info) {
            goToUserInfoActivity(0);
        } else if (service == R.string.navigation_item_timetable) {
            goToTimetableActivty();
        }
    }


    /*
     * Left Navigation Menu
     * */
    protected abstract void goToMainActivity();

    protected abstract void goToStoreActivity();

    protected abstract void goToDiningActivity();

    protected abstract void goToBusActivity();

    protected abstract void goToFreeBoardActivity();

    protected abstract void goToRecruitBoardActivity();

    protected abstract void goToAnonymousBoardActivity();

    protected abstract void goToCallvanActivity();

    protected abstract void goToTimetableActivty();

    protected abstract void goToLandActivity();

    protected abstract void goToAnonymousTimeTableActivity();

    protected abstract void goToMarketActivity();

    protected abstract void goToCircleActivity();

    protected abstract void goToLostFoundActivity();

    protected abstract void goToSearchActivity();


    /*
     * Right Navigation Menu
     * */
    protected abstract void goToUserInfoActivity(int service);

    protected abstract void onClickNavigationkakaoTalk();

    protected abstract void onClickNavigationDeveloper();

    protected abstract void onClickNavigationLogout();

    protected abstract void onClickNavigationLogin();

    protected abstract void showLoginRequestDialog();

    protected abstract void showNickNameRequestDialog();

}