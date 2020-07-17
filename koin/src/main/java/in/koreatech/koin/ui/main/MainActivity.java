package in.koreatech.koin.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import com.airbnb.lottie.LottieAnimationView;

import java.nio.file.attribute.GroupPrincipal;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.util.NavigationManger;

import static android.view.View.GONE;


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
        LottieAnimationView categoryLottieAnimationView = findViewById(R.id.base_navigation_bar_bottom_category_lottie_imageview);
        ImageView categoryImageView = findViewById(R.id.base_navigation_bar_bottom_category_imageview);
        TextView categoryTextView = findViewById(R.id.base_navigation_bar_bottom_category_textview);
        if (isSelected) {
            setSelected(categoryLottieAnimationView, categoryImageView, categoryTextView);
        } else {
            setUnselected(categoryLottieAnimationView, categoryImageView, categoryTextView);
        }
    }

    private void setNavigationSearchButton(boolean isSelected) {
        LottieAnimationView searchLottieAnimationView = findViewById(R.id.base_navigation_bar_bottom_search_lottie_imageview);
        TextView searchTextView = findViewById(R.id.base_navigation_bar_bottom_search_textview);
        ImageView searchImageView = findViewById(R.id.base_navigation_bar_bottom_search_imageview);

        if (isSelected) {
            setSelected(searchLottieAnimationView, searchImageView, searchTextView);
        } else {
            setUnselected(searchLottieAnimationView, searchImageView, searchTextView);
        }
    }

    private void setNavigationHomeButton(boolean isSelected) {
        LottieAnimationView homeLottieAnimationView = findViewById(R.id.base_navigation_bar_bottom_home_lottie_imageview);
        ImageView homeImageView = findViewById(R.id.base_navigation_bar_bottom_home_imageview);
        TextView homeTextView = findViewById(R.id.base_navigation_bar_bottom_home_textview);
        if (isSelected) {
            setSelected(homeLottieAnimationView, homeImageView, homeTextView);
        } else {
            setUnselected(homeLottieAnimationView, homeImageView, homeTextView);
        }
    }

    private void setSelected(LottieAnimationView lottieAnimationView, ImageView imageView, TextView textView){
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        imageView.setVisibility(GONE);
        textView.setVisibility(GONE);
    }
    private void setUnselected(LottieAnimationView lottieAnimationView, ImageView imageView, TextView textView){
        lottieAnimationView.setVisibility(GONE);
        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
    }

}
