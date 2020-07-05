package in.koreatech.koin.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

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
                break;

        }
    }

    public void goToHome() {
        NavigationManger.goToHome(this);
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
}
