package in.koreatech.koin.ui.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;


public class KoinNavigationDrawer extends ActivityBase implements View.OnClickListener {
    private LinearLayout categoryLinearLayout;
    private LinearLayout searchLinearLayout;
    private LinearLayout homeLinearLayout;
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
        homeLinearLayout = findViewById(R.id.base_navigation_bar_bottom_home_linearlayout);
        searchLinearLayout = findViewById(R.id.base_navigation_bar_bottom_search_linearlayout);
        navigationFragment = new KoinNavigationDrawerFragment();
        categoryLinearLayout.setOnClickListener(this);
        homeLinearLayout.setOnClickListener(this);
        searchLinearLayout.setOnClickListener(this);
        ImageView categoryImageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
        TextView categoryTextView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
        categoryImageView.setBackgroundResource(R.drawable.ic_bottom_category_on);
        categoryTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        navigationControl();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_navigation_bar_bottom_category_linearlayout:
                navigationControl();
                break;
            case R.id.base_navigation_bar_bottom_home_linearlayout:
                closeNavigationDrawer();
                goToService(R.id.navi_item_home);
                break;
            case R.id.base_navigation_bar_bottom_search_linearlayout:
                closeNavigationDrawer();
                goToService(R.id.navi_item_search);
                break;
        }
    }

    public void openNavigationDrawer() {
        if (isDrawerOpen())
            return;
        isDrawerOpen = true;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_start_left, R.anim.slide_finsh_left);
        fragmentTransaction.replace(R.id.navigation_drawer_frame_layout, navigationFragment);
        fragmentTransaction.commit();
    }

    public void closeNavigationDrawer() {
        if (!isDrawerOpen())
            return;
        isDrawerOpen = false;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_start_right, R.anim.slide_finsh_right);
        fragmentTransaction.remove(navigationFragment);
        fragmentTransaction.commit();
    }

    public void navigationControl() {
        if (isDrawerOpen()) {
            closeNavigationDrawer();
        } else {
            openNavigationDrawer();
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen())
            closeNavigationDrawer();
    }

    public boolean isDrawerOpen() {
        return isDrawerOpen;
    }

    public void goToService(@IdRes int selectItemId) {
        Intent intent = new Intent();
        intent.putExtra(BaseNavigationActivity.IS_NAVIGATION_CLICKED, true);
        intent.putExtra(BaseNavigationActivity.NAVIGATION_ID, selectItemId);
        setResult(Activity.RESULT_OK, intent);
    }
}
