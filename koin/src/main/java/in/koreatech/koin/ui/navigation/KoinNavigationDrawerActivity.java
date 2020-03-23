package in.koreatech.koin.ui.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.WebViewActivity;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.User;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.board.BoardActivity;
import in.koreatech.koin.ui.bus.BusActivity;
import in.koreatech.koin.ui.callvan.CallvanActivity;
import in.koreatech.koin.ui.circle.CircleActivity;
import in.koreatech.koin.ui.dining.DiningActivity;
import in.koreatech.koin.ui.event.EventActivity;
import in.koreatech.koin.ui.lostfound.LostFoundMainActivity;
import in.koreatech.koin.ui.land.LandActivity;
import in.koreatech.koin.ui.search.SearchActivity;
import in.koreatech.koin.ui.timetable.TimetableActivity;
import in.koreatech.koin.ui.timetable.TimetableAnonymousActivity;
import in.koreatech.koin.ui.usedmarket.MarketUsedActivity;
import in.koreatech.koin.ui.store.StoreActivity;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

/**
 * koin enterprise 앱에서 navigation drawer가 사용되는 화면들의 기본 acitivity
 */
public class KoinNavigationDrawerActivity extends BaseNavigationActivity {
    //시간표,복덕방,분실물
    private final int[] mMenuId = {
            R.id.navi_item_myinfo,
            R.id.navi_item_store, R.id.navi_item_bus,
            R.id.navi_item_dining, R.id.navi_item_cirlce,
            R.id.navi_item_timetable, R.id.navi_item_anonymous_board,
            R.id.navi_item_free_board, R.id.navi_item_recruit_board, R.id.navi_item_event_board,
            R.id.navi_item_land, R.id.navi_item_lostfound,
            R.id.navi_item_usedmarket, R.id.navi_item_kakao_talk,
            R.id.navi_item_version_info, R.id.navi_item_developer}; //닉네임 레이아웃 추가

    private final int[] mMenuTextviewId = {
            R.id.navi_item_myinfo_textview,
            R.id.navi_item_store_textview, R.id.navi_item_bus_textview,
            R.id.navi_item_dining_textview, R.id.navi_item_cirlce_textview,
            R.id.navi_item_timetable_textview, R.id.navi_item_anonymous_board_textview,
            R.id.navi_item_free_board_textview, R.id.navi_item_recruit_board_textview, R.id.navi_item_event_textview,
            R.id.navi_item_land_textview, R.id.navi_item_lostfound_textview,
            R.id.navi_item_usedmarket_textview, R.id.navi_item_kakao_talk_textview,
            R.id.navi_item_version_info_textview, R.id.navi_item_developer_textview
    };

    @Override
    protected Context setContext() {
        return getApplicationContext();
    }

    @Override
    protected int getDrawerLayoutID() {
        return R.id.drawer_layout;
    }

    @Override
    protected int getLeftNavigationDrawerID() {
        return R.id.left_nav_view;
    }

    @Override
    protected int[] getMenuId() {
        return mMenuId;
    }

    @Override
    protected int[] getMenuTextviewId() {
        return mMenuTextviewId;
    }

    @Override
    protected int getBottomNavigationCategoryID() {
        return R.id.base_navigation_bar_bottom_category_linearlayout;
    }

    @Override
    protected int getBottomNavigationHomeID() {
        return R.id.base_navigation_bar_bottom_home_linearlayout;
    }

    @Override
    protected int getBottomNavigationSearchID() {
        return R.id.base_navigation_bar_bottom_search_linearlayout;
    }

    /**
     * left navigation drawer 서비스 메뉴 호출
     */

    public AuthorizeConstant getAuthority() {

        AuthorizeConstant authorizeConstant;
        try {
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        }
        return authorizeConstant;
    }

    public User getUser() {
        User user;
        try {
            user = UserInfoSharedPreferencesHelper.getInstance().loadUser();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            user = UserInfoSharedPreferencesHelper.getInstance().loadUser();
        }
        return user;
    }

    @Override
    protected void goToMainActivity() {
        goToActivityFinish(new Intent(this, MainActivity.class));
    }

    @Override
    protected void goToSearchActivity() {
        goToActivityFinish(new Intent(this, SearchActivity.class));
    }

    @Override
    protected void goToStoreActivity() {
        goToActivityFinish(new Intent(this, StoreActivity.class));
    }

    @Override
    protected void goToDiningActivity() {
        goToActivityFinish(new Intent(this, DiningActivity.class));
    }

    @Override
    protected void goToBusActivity() {
        goToActivityFinish(new Intent(this, BusActivity.class));
    }

    @Override
    protected void goToTimetableActivty() {
        goToActivityFinish(new Intent(this, TimetableActivity.class));
    }

    @Override
    protected void goToLandActivity() {
        goToActivityFinish(new Intent(this, LandActivity.class));
    }

    protected void goToLostFoundActivity() {
        goToActivityFinish(new Intent(this, LostFoundMainActivity.class));
    }

    @Override
    protected void goToAnonymousTimeTableActivity() {
        goToActivityFinish(new Intent(this, TimetableAnonymousActivity.class));
    }

    @Override
    protected void goToFreeBoardActivity() {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("BOARD_UID", ID_FREE);
        goToActivityFinish(intent);
    }

    @Override
    protected void goToRecruitBoardActivity() {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("BOARD_UID", ID_RECRUIT);
        goToActivityFinish(intent);
    }

    @Override
    protected void goToAnonymousBoardActivity() {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("BOARD_UID", ID_ANONYMOUS);
        goToActivityFinish(intent);
    }

    @Override
    protected void goToCallvanActivity() {
        goToActivityFinish(new Intent(this, CallvanActivity.class));
    }

    @Override
    protected void goToMarketActivity() {
        goToActivityFinish(new Intent(this, MarketUsedActivity.class));
    }

    @Override
    protected void goToCircleActivity() {
        goToActivityFinish(new Intent(this, CircleActivity.class));
    }

    @Override
    protected void goToEventActivity() {
        goToActivityFinish(new Intent(this, EventActivity.class));
    }

    /**
     * right navigation drawer 서비스 메뉴 호출
     */

    @Override
    protected void goToUserInfoActivity(int service) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("CONDITION", service);
        startActivity(intent);
    }

    @Override
    protected void onClickNavigationkakaoTalk() {
        boolean isKaKaoTalkInstalled = false;

        try {   //카카오톡 APP 설치 유무
            getPackageManager().getPackageInfo("com.kakao.talk", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) { //카카오톡 APP이 있을 경우
            e.printStackTrace();
            isKaKaoTalkInstalled = true;
        }

        if (isKaKaoTalkInstalled) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://goto.kakao.com/@bcsdlab")));
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("kakaoplus://plusfriend/friend/@bcsdlab")));
        }
    }

    @Override
    protected void onClickNavigationDeveloper() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title", "BCSD 홈페이지");
        intent.putExtra("url", "https://bcsdlab.com/");
        startActivity(intent);
    }

    /**
     * 로그아웃 메소드
     * 콜밴쉐어링 방 참여 중일 경우 로그아웃 불가
     * 아닐 경우 기기에 저장된 사용자 정보 삭제 후 로그아웃
     */
    @Override
    protected void onClickNavigationLogout() {
        //TODO:API 오류 복구 후 제거
        UserInfoSharedPreferencesHelper.getInstance().removeCallvanRoomUid();
        int roomUid = UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid();

        if (roomUid > 0) {
            ToastUtil.getInstance().makeLong("콜밴쉐어링 방에 참여중일땐 로그아웃하실 수 없습니다");
        } else {
            UserInfoSharedPreferencesHelper.getInstance().clear();
            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
            ToastUtil.getInstance().makeShort("로그아웃 되었습니다.");
            overridePendingTransition(R.anim.fade, R.anim.hold);

        }
    }

    @Override
    protected void onClickNavigationLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("FIRST_LOGIN", false);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    @Override
    protected void showLoginRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원 전용 서비스")
                .setMessage("로그인이 필요한 서비스입니다.\n로그인 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("FIRST_LOGIN", false);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> {
                    setLastNavigationItem();
                    dialog.cancel();
                });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    @Override
    public void showNickNameRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("닉네임이 필요합니다.")
                .setMessage("닉네임이 필요한 서비스입니다.\n닉네임을 추가 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    startActivity(new Intent(this, UserInfoActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    /**
     * 서비스 간 이동 시 slide animation을 적용시켜 이동하는 메소드
     *
     * @param intent
     */
    private void goToActivityFinish(Intent intent) {
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        finish();
    }
}
