package in.koreatech.koin.ui.navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;

public class KoinNavigationDrawer extends ActivityBase implements View.OnClickListener {
    private LinearLayout categoryLinearLayout;
    private FragmentTransaction fragmentTransaction;
    private Fragment navigationFragment;
    private boolean isDrawerOpen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        init();
    }

    private void init() {
        isDrawerOpen = false;
        categoryLinearLayout = findViewById(R.id.base_navigation_bar_bottom_category_linearlayout);
        navigationFragment = new KoinNavigationDrawerFragment();
        categoryLinearLayout.setOnClickListener(this);
        navigationControl();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_navigation_bar_bottom_category_linearlayout:
                navigationControl();
                break;
        }
    }

    public void openNavigationDrawer() {
        isDrawerOpen = true;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_start_left, R.anim.slide_finsh_left);
        fragmentTransaction.replace(R.id.navigation_drawer_frame_layout, navigationFragment);
        fragmentTransaction.commit();
    }

    public void closeNavigationDrawer() {
        isDrawerOpen = false;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_start_right, R.anim.slide_finsh_right);
        fragmentTransaction.remove(navigationFragment);
        fragmentTransaction.commit();
    }

    public void navigationControl() {
        if (isDrawerOpen) {
            closeNavigationDrawer();
        } else {
            openNavigationDrawer();
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen)
            closeNavigationDrawer();
    }

    public boolean isDrawerOpen() {
        return isDrawerOpen;
    }
}
