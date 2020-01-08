package in.koreatech.koin.ui.navigation;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

import in.koreatech.koin.core.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.constant.AuthorizeConstant;

import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.core.toast.ToastUtil;

import static androidx.drawerlayout.widget.DrawerLayout.STATE_DRAGGING;

/**
 * Created by hyerim on 2018. 5. 31....
 * Edited by yunjae on 2018. 8. 27....
 */
public abstract class BaseNavigationActivity extends ActivityBase implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DrawerLayout.DrawerListener {
    private final String TAG = "BaseNavigationActivity";
    private final int LEFTNAVI = GravityCompat.START;
    private final int RIGHTNAVI = GravityCompat.END;
    private final String CURRENT_ID = "CURRENT_ID";
    private final String BEFORE_ID = "BEFORE_ID";
    private final String FONT_NAME = "fonts/notosanscjkkr_regular.otf";

    private Context context;

    private DrawerLayout drawerLayout;
    private NavigationView mLeftNavigationView;

    private long mPressTime = 0;
    private static int currentId = R.id.navi_item_home;
    private static int beforeId = R.id.navi_item_home;

    private InputMethodManager mInputMethodManager;
    private LinearLayout mOpenLeftNavigationDrawerOpenLinearLayout;
    private LinearLayout mOpenHomeLinearLayout;
    private LinearLayout mOpenSearchLinearLayout;


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
        mInputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

    }

    protected abstract Context setContext();

    @Override
    protected void onStart() {
        super.onStart();
        toggleIcon(currentId);
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
        drawerLayout.addDrawerListener(this);
        drawerLayout.setScrimColor(getResources().getColor(R.color.black__20));


        for (int id : getMenuId()) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(this);
                changeMenuFont(view, FONT_NAME);
            }
        }

        mLeftNavigationView = findViewById(getLeftNavigationDrawerID());
        mOpenLeftNavigationDrawerOpenLinearLayout = findViewById(getBottomNavigationCategoryID());
        mOpenHomeLinearLayout = findViewById(getBottomNavigationHomeID());
        mOpenSearchLinearLayout = findViewById(getBottomNavigationSearchID());
        mOpenLeftNavigationDrawerOpenLinearLayout.setOnClickListener(this);
        mOpenHomeLinearLayout.setOnClickListener(this);
        mOpenSearchLinearLayout.setOnClickListener(this);
        mLeftNavigationView.setNavigationItemSelectedListener(this);
        mLeftNavigationView.setOnClickListener(this);

        width = getResources().getDisplayMetrics().widthPixels * 667 / 1000;
        ViewGroup.LayoutParams params = mLeftNavigationView.getLayoutParams();
        params.width = width;
        mLeftNavigationView.setLayoutParams(params);
        toggleIcon(currentId);
    }

    public void toggleIcon(int id) {
        ImageView imageView;
        TextView textView;
        clearToggle();
        if (id == R.id.navi_item_home) {
            imageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_home_on);
            textView.setTextColor(getResources().getColor(R.color.light_navy));
        } else if (id == R.id.navi_item_search) {
            imageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
            imageView.setBackgroundResource(R.drawable.ic_search_menu_blue);
            textView.setTextColor(getResources().getColor(R.color.light_navy));
        } else {
            imageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_home);
            textView.setTextColor(getResources().getColor(R.color.black));
            imageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_myinfo);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void changeMenuFont(View view, String fontName) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(Typeface.createFromAsset(getAssets(), fontName));
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
        toggleIcon(currentId);
        selectNavigationItem(currentId);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        ImageView imageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
        TextView textView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
        imageView.setBackgroundResource(R.drawable.ic_bottom_category_on);
        textView.setTextColor(getResources().getColor(R.color.light_navy));
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        ImageView imageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
        TextView textView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
        imageView.setBackgroundResource(R.drawable.ic_bottom_category);
        textView.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState == STATE_DRAGGING) {
            ImageView imageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
            TextView textView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_category_on);
            textView.setTextColor(getResources().getColor(R.color.light_navy));
        } else {
            ImageView imageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
            TextView textView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_category);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
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
                String styledText = "<u><font color='#f7941e'>" + s + "</font></u>";
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
        } else if (itemId == R.id.navi_item_lostfound) {
            goToLostFoundActivity();
        } else if (itemId == R.id.navi_item_login) {
            onClickNavigationLogin();
        } else if (itemId == R.id.navi_item_kakao_talk) {
            onClickNavigationkakaoTalk();
            currentId = beforeId;
        } else if (itemId == R.id.navi_item_developer) {
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

        toggleIcon(currentId);

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
        if (i == getBottomNavigationCategoryID()) {
            toggleNavigationDrawer();
            return;
        }
        if (i == getBottomNavigationHomeID())
            i = R.id.navi_item_home;
        else if (i == getBottomNavigationSearchID())
            i = R.id.navi_item_search;
        else {
            selectNavigationItem(i);
            toggleNavigationDrawer();
        }
        callDrawerItem(i);
        toggleIcon(i);
    }

    private void toggleNavigationDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
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
            if (System.currentTimeMillis() > mPressTime + 2000) {
                if (currentId != R.id.navi_item_home)
                    callDrawerItem(R.id.navi_item_home);
                else {
                    mPressTime = System.currentTimeMillis();
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
            name = UserInfoSharedPreferencesHelper.getInstance().loadUser().userName;
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            name = UserInfoSharedPreferencesHelper.getInstance().loadUser().userName;
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
            if (FormValidatorUtil.validateStringIsEmpty(user.userNickName) || FormValidatorUtil.validateStringIsEmpty(user.userName)) {
                ToastUtil.getInstance().makeShort("해당 서비스를 이용하기 위해 사용자 정보를 입력해주세요.");
                goToUserInfoActivity(service);
                currentId = beforeId;
            } else {
                goToFreeBoardActivity();
            }

        } else if (service == R.string.navigation_item_recruit_board) {
            if (FormValidatorUtil.validateStringIsEmpty(user.userNickName) || FormValidatorUtil.validateStringIsEmpty(user.userName)) {
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