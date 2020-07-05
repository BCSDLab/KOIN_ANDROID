package in.koreatech.koin.ui.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import in.koreatech.koin.R;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;

public abstract class BaseNavigationFragment extends KoinBaseFragment {
    private final String TAG = "BaseNavigationFragment";
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = setContext();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    protected void callDrawerItem(String tag) {
        if (getCurrentService().equals(tag))
            return;

        if (isServiceSame(R.string.navigation_item_store, tag)) {
            goToStoreFragment();
        } else if (isServiceSame(R.string.navigation_item_dining, tag)) {
            goToDiningFragment();
        } else if (isServiceSame(R.string.navigation_item_bus, tag)) {
            goToBusFragment();
        } else if (isServiceSame(R.string.navigation_item_circle, tag)) {
            goToCircleFragment();
        } else if (isServiceSame(R.string.navigation_item_free_board, tag)) {
            goToFreeBoardFragment();
        } else if (isServiceSame(R.string.navigation_item_recruit_board, tag)) {
            goToRecruitBoardFragment();
        } else if (isServiceSame(R.string.navigation_item_anonymous_board, tag)) {
            goToAnonymousBoardFragment();
        } else if (isServiceSame(R.string.navigation_item_usedmarket, tag)) {
            goToMarketFragment();
        } else if (isServiceSame(R.string.navigation_item_lostfound, tag)) {
            goToLostFoundFragment();
        } else if (isServiceSame(R.string.navigation_item_login, tag)) {
            onClickNavigationLogin();
        } else if (isServiceSame(R.string.navigation_item_kakao_talk, tag)) {
            onClickNavigationkakaoTalk();
        } else if (isServiceSame(R.string.navigation_item_developer, tag)) {
            onClickNavigationDeveloper();
        } else if (isServiceSame(R.string.navigation_item_user_info, tag)) {
            goToUserInfo();
        } else if (isServiceSame(R.string.navigation_item_timetable, tag)) {
            goToTimetableFragment();
        } else if (isServiceSame(R.string.navigation_item_land, tag)) {
            goToLandFragment();
        } else {
            ToastUtil.getInstance().makeShort("서비스예정입니다");
        }
    }

    private boolean isServiceSame(@StringRes int tag, String currentService) {
        return getResources().getString(tag).equals(currentService);
    }


    protected abstract Context setContext();

    protected abstract String getCurrentService();

    /*
     * Left Navigation Menu
     * */
    protected abstract void goToStoreFragment();

    protected abstract void goToDiningFragment();

    protected abstract void goToBusFragment();

    protected abstract void goToFreeBoardFragment();

    protected abstract void goToRecruitBoardFragment();

    protected abstract void goToAnonymousBoardFragment();

    ;

    protected abstract void goToTimetableFragment();

    protected abstract void goToLandFragment();

    protected abstract void goToAnonymousTimeTableFragment();

    protected abstract void goToMarketFragment();

    protected abstract void goToCircleFragment();

    protected abstract void goToLostFoundFragment();

    protected abstract void goToTimeTableFragment();

    protected abstract void goToUserInfo();

    /*
     * Right Navigation Menu
     * */
    protected abstract void goToUserInfoFragment(int service);

    protected abstract void onClickNavigationkakaoTalk();

    protected abstract void onClickNavigationDeveloper();

    protected abstract void onClickNavigationLogout();

    protected abstract void onClickNavigationLogin();

    protected abstract void showLoginRequestDialog();

    protected abstract void showNickNameRequestDialog();
}
