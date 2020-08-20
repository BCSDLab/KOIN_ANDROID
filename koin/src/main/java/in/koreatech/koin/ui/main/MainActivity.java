package in.koreatech.koin.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.utils.StatusBarUtil;
import in.koreatech.koin.core.viewpager.ScaleViewPager;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;

public class MainActivity extends ActivityBase {

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    MaterialCardView toolbar;
    @BindView(R.id.toolbar_layout)
    FrameLayout toolbarLayuout;

    @BindView(R.id.main_view_pager)
    ScaleViewPager mainViewPager;

    @BindView(R.id.recycler_view_store_category)
    RecyclerView recyclerViewStoreCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        unbinder = ButterKnife.bind(this);
        StatusBarUtil.applyTopPaddingStatusBarHeight(toolbarLayuout, getResources());

        initBusPager();
        initStoreRecyclerView();
    }

    private void initBusPager() {
        mainViewPager.setAdapter(new BusPagerAdapter());
        mainViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        mainViewPager.setOffscreenPageLimit(5);
    }

    private void initStoreRecyclerView() {
        recyclerViewStoreCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewStoreCategory.setAdapter(new StoreCategoryRecyclerAdapter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}