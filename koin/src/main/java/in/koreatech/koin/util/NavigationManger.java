package in.koreatech.koin.util;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;

public class NavigationManger {
    public static final String TAG = "NavigationManger";
    public static final int FRAGMENT_CONTAINER_ID = R.id.nav_host_fragment;

    public static NavController getNavigationController(Activity activity) {
        return Navigation.findNavController(activity, FRAGMENT_CONTAINER_ID);
    }

    public static void goToHome(AppCompatActivity activity) {
        Fragment navHostFragment = activity.getSupportFragmentManager().findFragmentById(FRAGMENT_CONTAINER_ID).getChildFragmentManager().getFragments().get(0);
        if (navHostFragment instanceof KoinBaseFragment)
            ((KoinBaseFragment) navHostFragment).addFinishingBackground();
        NavigationManger.getNavigationController(activity).navigate(R.id.main_fragment, null, NavigationManger.getGoToHomeNavigationAnimation());
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
                .setEnterAnim(R.anim.trans_left_in)
                .setExitAnim(R.anim.trans_left_out)
                .setPopEnterAnim(R.anim.trans_right_in)
                .setPopExitAnim(R.anim.trans_right_out)
                .setPopUpTo(R.id.main_fragment, true)
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
}
