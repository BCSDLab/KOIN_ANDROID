package in.koreatech.koin.ui.navigation;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.util.FormValidatorUtil;

public abstract class BaseNavigationActivity extends ActivityBase implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static int currentId = R.id.navi_item_home;
    private static int beforeId = R.id.navi_item_home;
    private final String TAG = "BaseNavigationActivity";
    private final int LEFTNAVI = GravityCompat.END;
    private final int RIGHTNAVI = GravityCompat.START;
    private final String CURRENT_ID = "CURRENT_ID";
    private final String BEFORE_ID = "BEFORE_ID";
    private final String fontName = "fonts/notosanscjkkr_regular.otf";
    private Context context;
    private DrawerLayout drawerLayout;
    private NavigationView leftNavigationView;
    private long pressTime = 0;
    private InputMethodManager inputMethodManager;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_ID, currentId);
        outState.putInt(BEFORE_ID, beforeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = setContext();
        this.inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

    }

    protected abstract Context setContext();

    @Override
    protected void onStart() {
        super.onStart();
        setLeftNavigationDrawerName();
        selectNavigationItem(currentId);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentId = savedInstanceState.getInt(CURRENT_ID);
            beforeId = savedInstanceState.getInt(BEFORE_ID);
        }
        int width;
        drawerLayout = findViewById(getDrawerLayoutID());
        drawerLayout.setScrimColor(getResources().getColor(R.color.black_alpha20));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (slideOffset < 0.5f) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(BaseNavigationActivity.this, R.color.colorPrimary));
                        getWindow().getDecorView().setSystemUiVisibility(0);
                    } else
                        getWindow().setStatusBarColor(ContextCompat.getColor(BaseNavigationActivity.this, R.color.colorPrimary));
                } else {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(BaseNavigationActivity.this, R.color.background));
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else
                        getWindow().setStatusBarColor(ContextCompat.getColor(BaseNavigationActivity.this, R.color.gray8));
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        for (int id : getMenuId()) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(this);
                changeMenuFont(view, fontName);
            }
        }
        View leftArrowButton = findViewById(getLeftArrowButtonId());
        leftArrowButton.setOnClickListener((view) -> toggleNavigationDrawer());                       //왼쪽화살표  클릭리스너 등록
        View logoImageView = findViewById(getLogoId());
        logoImageView.setOnClickListener(this);                                                       //로고 클릭리스너 등록

        this.leftNavigationView = findViewById(getLeftNavigationDrawerID());
        this.leftNavigationView.setNavigationItemSelectedListener(this);
        this.leftNavigationView.setOnClickListener(this);

        width = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = this.leftNavigationView.getLayoutParams();
        params.width = width;
        this.leftNavigationView.setLayoutParams(params);

    }


    private void changeMenuFont(View view, String fontName) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(Typeface.createFromAsset(getAssets(), fontName));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setLeftNavigationDrawerName();
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    public void setLastNavigationItem() {
        currentId = beforeId;
        Log.d(TAG, "beforeID: " + getResources().getResourceEntryName(currentId));
        Log.d(TAG, "beforeID: " + getResources().getResourceEntryName(beforeId));
        selectNavigationItem(currentId);
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


    protected abstract int getDrawerLayoutID();

    protected abstract int getLeftNavigationDrawerID();

    protected abstract int getLeftArrowButtonId();

    protected abstract int getLogoId();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int itemId = item.getItemId();
        return goToNavigationItem(itemId);
    }

    private boolean goToNavigationItem(int itemId) {
        if (itemId == getLeftNavigationDrawerID()) {
            drawerLayout.closeDrawer(LEFTNAVI);
            return true;
        }

        callDrawerItem(itemId);


        drawerLayout.closeDrawer(LEFTNAVI);


        return true;
    }

    private void selectNavigationItem(int itemId) {
        if (itemId == R.id.navi_item_user_info || itemId == R.id.navi_item_version_info || itemId == R.id.navi_item_developer)
            return;
        for (int id : getMenuTextviewId()) {
            TextView textView = findViewById(id);
            String s = textView.getText().toString();
            textView.setText(s);
        }
        for (int i = 0; i < getMenuId().length; i++) {
            if (getMenuId()[i] == itemId) {
                TextView textView = findViewById(getMenuTextviewId()[i]);
                String s = textView.getText().toString();
                String styledText = "<font color='#f7941e'>" + s + "</font>";
                textView.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);       //#f7941e
            }
        }
    }

    // TODO -> 익명게시판 수정 후 주석 제거
    public void callDrawerItem(int itemId) {

        if (itemId == currentId) {
            selectNavigationItem(currentId);
            return;
        }

        beforeId = currentId;
        currentId = itemId;


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
        } else if (itemId == R.id.navi_item_login) {
            onClickNavigationLogin();
        } else if (itemId == R.id.navi_item_kakao_talk) {
            onClickNavigationkakaoTalk();
            currentId = beforeId;
        } else if (itemId == R.id.bcsd_logo) {
            onClickNavigationDeveloper();
            currentId = beforeId;
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
            currentId = beforeId;
        }

        selectNavigationItem(currentId);

    }

    public void callDrawerItem(int itemId, Bundle bundle) {
        if (itemId == currentId) {
            selectNavigationItem(currentId);
            return;
        }

        beforeId = currentId;
        currentId = itemId;

        if (itemId == R.id.navi_item_store) {
            goToStoreActivity(bundle);
        } else if (itemId == R.id.navi_item_bus) {
            goToBusActivity(bundle);
        }

        selectNavigationItem(currentId);
    }

    public void setLeftNavigationDrawerName() {
        TextView mNameTextview = findViewById(R.id.base_naviagtion_drawer_nickname_textview);
        if (getAuthorize() == AuthorizeConstant.ANONYMOUS) // 비로그인일때 회원 정보 수정 비활성화
        {
            mNameTextview.setText("익명");
        } else {
            mNameTextview.setText(!getName().equals("") ? getName() : "익명");
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        selectNavigationItem(i);
        toggleNavigationDrawer();
        callDrawerItem(i);
    }

    protected void toggleNavigationDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
        else
            drawerLayout.openDrawer(GravityCompat.END);
    }

    private void onClickNavigationFreeBoard() {
        checkUserInfoEnough(R.string.navigation_item_free_board);
    }

    private void onClickNavigationRecruitBoard() {
        checkUserInfoEnough(R.string.navigation_item_recruit_board);
    }

    private void onClickNavigationAnonymousBoard() {
        checkUserInfoEnough(R.string.navigation_item_anonymous_board);
    }

    private void onClickNavigationCallvanshring() {
        checkUserInfoEnough(R.string.navigation_item_callvan_sharing);
    }

    private void onClickNavigationUsedmarket() {
        checkUserInfoEnough(R.string.navigation_item_usedmarket);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_openRight) {
            drawerLayout.openDrawer(RIGHTNAVI);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(getDrawerLayoutID());

        if (drawer.isDrawerOpen(LEFTNAVI)) {
            drawer.closeDrawer(LEFTNAVI);
        } else if (drawer.isDrawerOpen(RIGHTNAVI)) {
            drawer.closeDrawer(RIGHTNAVI);
        } else {
            if (System.currentTimeMillis() > pressTime + 2000) {
                if (currentId != R.id.navi_item_home)
                    callDrawerItem(R.id.navi_item_home);
                else {
                    pressTime = System.currentTimeMillis();
                    ToastUtil.getInstance().makeShort("뒤로가기 버튼을 한 번 더 누르면 종료됩니다.");
                }
            } else {
                finishAffinity();
            }
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
            currentId = beforeId;
            return;
        }
        User user = UserInfoSharedPreferencesHelper.getInstance().loadUser();

        //TODO:홈 만들 경우 유저 이름 체크 홈으로 이동

        if (service == R.string.home || service == R.string.navigation_item_free_board) {
            if (FormValidatorUtil.validateStringIsEmpty(user.getUserNickName()) || FormValidatorUtil.validateStringIsEmpty(user.getUserName())) {
                ToastUtil.getInstance().makeShort("해당 서비스를 이용하기 위해 사용자 정보를 입력해주세요.");
                goToUserInfoActivity(service);
                currentId = beforeId;
            } else {
                goToFreeBoardActivity();
            }

        } else if (service == R.string.navigation_item_recruit_board) {
            if (FormValidatorUtil.validateStringIsEmpty(user.getUserNickName()) || FormValidatorUtil.validateStringIsEmpty(user.getUserName())) {
                ToastUtil.getInstance().makeShort("해당 서비스를 이용하기 위해 사용자 정보를 입력해주세요.");
                goToUserInfoActivity(service);
                currentId = beforeId;
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

    protected abstract void goToStoreActivity(Bundle bundle);

    protected abstract void goToBusActivity(Bundle bundle);

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

    protected void onClickNavigationLogout() {
        currentId = R.id.navi_item_home;
        beforeId = R.id.navi_item_home;
    }

    protected abstract void onClickNavigationLogin();

    protected abstract void showLoginRequestDialog();

    protected abstract void showNickNameRequestDialog();

}