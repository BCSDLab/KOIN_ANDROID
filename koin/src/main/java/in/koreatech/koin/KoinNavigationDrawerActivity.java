package in.koreatech.koin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import in.koreatech.koin.core.bases.BaseNavigationActivity;
import in.koreatech.koin.core.activity.WebViewActivity;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.networks.entity.User;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_board.ui.BoardActivity;
import in.koreatech.koin.service_bus.ui.BusActivity;
import in.koreatech.koin.service_callvan.ui.CallvanActivity;
import in.koreatech.koin.service_circle.ui.CircleActivity;
import in.koreatech.koin.service_dining.ui.DiningActivity;
import in.koreatech.koin.service_lostfound.ui.LostFoundMainActivity;
import in.koreatech.koin.service_land.ui.LandActivity;
import in.koreatech.koin.service_timetable.ui.TimetableActivity;
import in.koreatech.koin.service_timetable.ui.TimetableAnonymousActivity;
import in.koreatech.koin.service_used_market.ui.MarketUsedActivity;
import in.koreatech.koin.service_store.ui.StoreActivity;
import in.koreatech.koin.ui.LoginActivity;
import in.koreatech.koin.ui.MainActivity;
import in.koreatech.koin.ui.UserInfoActivity;

import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_RECRUIT;

/**
 * koin enterprise 앱에서 navigation drawer가 사용되는 화면들의 기본 acitivity
 * <p>
 * Created by hyerim on 2018. 7. 26....
 * Edited by yunjae on 2018. 8 .27....
 */
public class KoinNavigationDrawerActivity extends BaseNavigationActivity {

    private final int[] mMenuId = {
            R.id.navi_item_store, R.id.navi_item_bus,
            R.id.navi_item_dining, R.id.navi_item_cirlce,
            R.id.navi_item_timetable, R.id.navi_item_anonymous_board,
            R.id.navi_item_free_board, R.id.navi_item_recruit_board,
            R.id.navi_item_land, R.id.navi_item_lostfound
            , R.id.navi_item_usedmarket, R.id.navi_item_kakao_talk,
            R.id.navi_item_version_info, R.id.navi_item_developer};

    private final int[] mMenuTextviewId = {
            R.id.navi_item_store_textview, R.id.navi_item_bus_textview,
            R.id.navi_item_dining_textview, R.id.navi_item_cirlce_textview,
            R.id.navi_item_timetable_textview, R.id.navi_item_anonymous_board_textview,
            R.id.navi_item_free_board_textview, R.id.navi_item_recruit_board_textview,
            R.id.navi_item_land_textview, R.id.navi_item_lostfound_textview
            , R.id.navi_item_usedmarket_textview, R.id.navi_item_kakao_talk_textview,
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
    protected int getBottomNavigationMyInfoID() {
        return R.id.base_navigation_bar_bottom_myinfo_linearlayout;
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
            ToastUtil.getInstance().makeLongToast("콜밴쉐어링 방에 참여중일땐 로그아웃하실 수 없습니다");
        } else {
            UserInfoSharedPreferencesHelper.getInstance().clear();
            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
            ToastUtil.getInstance().makeShortToast("로그아웃 되었습니다.");
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
