package in.koreatech.koin.util;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import in.koreatech.koin.R;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.login.LoginActivity;

public class NavigationManger {
    public static final String TAG = "NavigationManger";
    public static final int FRAGMENT_CONTAINER_ID = R.id.nav_host_fragment;

    public static NavController getNavigationController(Activity activity) {
        return Navigation.findNavController(activity, FRAGMENT_CONTAINER_ID);
    }

    public static void goToHome(Activity activity) {
        if (getCurrentServiceLabel(activity).equals(activity.getResources().getString(R.string.navigation_home)))
            return;
        NavigationManger.getNavigationController(activity).navigate(R.id.navi_main_action, null, NavigationManger.getGoToHomeNavigationAnimation());
    }

    public static void goToUserInfo(Activity activity){
        if (getCurrentServiceLabel(activity).equals(activity.getResources().getString(R.string.navigation_item_user_info)))
            return;
        NavigationManger.getNavigationController(activity).navigate(R.id.navi_userinfo_action, null, NavigationManger.getNavigationAnimation());
    }

    public static void logout(Activity activity) {
        UserInfoSharedPreferencesHelper.getInstance().clear();
        activity.finishAffinity();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        ToastUtil.getInstance().makeShort("로그아웃 되었습니다.");
        activity.overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    public static NavOptions getNavigationAnimation() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.trans_left_in)
                .setExitAnim(R.anim.trans_left_out)
                .setPopEnterAnim(R.anim.trans_right_in)
                .setPopExitAnim(R.anim.trans_right_out)
                .build();
    }

    public static NavOptions getNavigationDrawerServiceSelectAnimation() {
        return new NavOptions.Builder()
                .setPopEnterAnim(R.anim.trans_right_in)
                .setPopExitAnim(R.anim.trans_right_out)
                .setPopUpTo(R.id.main_fragment, false)
                .build();
    }

    public static NavOptions getNavigationDrawerOpenAnimation() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.trans_right_in)
                .setExitAnim(R.anim.no_animation)
                .setPopEnterAnim(R.anim.no_animation)
                .setPopExitAnim(R.anim.trans_left_out)
                .build();
    }


    public static NavOptions getGoToHomeNavigationAnimation() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.trans_left_in)
                .setExitAnim(R.anim.trans_left_out)
                .setPopEnterAnim(R.anim.trans_right_in)
                .setPopExitAnim(R.anim.trans_right_out)
                .setPopUpTo(R.id.main_fragment, true)
                .build();
    }

    public static boolean isDrawerOpen(Activity activity) {
        return getCurrentServiceLabel(activity).equals(activity.getString(R.string.navigation_drawer));
    }

    public static String getCurrentServiceLabel(Activity activity) {
        return NavigationManger.getNavigationController(activity).getCurrentDestination().getLabel().toString();
    }

    public static String getCurrentServiceLabel(NavController navController) {
        return navController.getCurrentDestination().getLabel().toString();
    }
}
