package in.koreatech.koin.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.util.NavigationManger;


/**
 * 로그인 후 처음 보여지는 화면
 */
public class MainActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        NavigationManger.getNavigationController(this).addOnDestinationChangedListener(this::handleBottomNavigation);
    }

    private void handleBottomNavigation(NavController navController, NavDestination navDestination, Bundle bundle) {
        initBottomNavigation();
        if (NavigationManger.getCurrentServiceLabel(navController).equals(getResources().getString(R.string.navigation_home))) {
            setNavigationHomeButton(true);
        } else if (NavigationManger.getCurrentServiceLabel(navController).equals(getResources().getString(R.string.navigation_drawer))) {
            setNavigationCategoryButton(true);
        } else if (NavigationManger.getCurrentServiceLabel(navController).equals(getResources().getString(R.string.navigation_item_search))) {
            setNavigationSearchButton(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.base_navigation_bar_bottom_home_linearlayout, R.id.base_navigation_bar_bottom_category_linearlayout, R.id.base_navigation_bar_bottom_search_linearlayout})
    public void onClickBottomNavigationBar(View view) {
        switch (view.getId()) {
            case R.id.base_navigation_bar_bottom_home_linearlayout:
                goToHome();
                break;
            case R.id.base_navigation_bar_bottom_category_linearlayout:
                handleNavigationDrawer();
                break;
            case R.id.base_navigation_bar_bottom_search_linearlayout:
                goToSearch();
                break;

        }
    }

    public void goToHome() {
        NavigationManger.goToHome(this);
    }

    public void goToSearch() {
        NavigationManger.getNavigationController(this).navigate(R.id.navi_search_action, null, NavigationManger.getNavigationAnimation());
    }


    public void handleNavigationDrawer() {
        Bundle bundle = new Bundle();
        if (NavigationManger.isDrawerOpen(this)) {
            NavigationManger.getNavigationController(this).popBackStack();
        } else {
            bundle.putString("CURRENT_SERVICE", NavigationManger.getCurrentServiceLabel(this));
            NavigationManger.getNavigationController(this).navigate(R.id.navi_navigation_drawer_action, bundle, NavigationManger.getNavigationDrawerOpenAnimation());
        }
    }

    private void initBottomNavigation() {
        setNavigationCategoryButton(false);
        setNavigationSearchButton(false);
        setNavigationHomeButton(false);
    }

    private void setNavigationCategoryButton(boolean isSelected) {
        ImageView imageView;
        TextView textView;
        if (isSelected) {
            imageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_category_on);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            imageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_category);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void setNavigationSearchButton(boolean isSelected) {
        ImageView imageView;
        TextView textView;
        if (isSelected) {
            imageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
            imageView.setBackgroundResource(R.drawable.ic_search_menu_blue);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            imageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
            imageView.setBackgroundResource(R.drawable.ic_search_menu);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void setNavigationHomeButton(boolean isSelected) {
        ImageView imageView;
        TextView textView;
        if (isSelected) {
            imageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_home_on);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            imageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
            textView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
            imageView.setBackgroundResource(R.drawable.ic_bottom_home);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }
}
