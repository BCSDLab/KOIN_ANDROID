package in.koreatech.koin.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        unbinder = ButterKnife.bind(this);
        StatusBarUtil.applyTopPaddingStatusBarHeight(toolbarLayuout, getResources());

        mainViewPager.setAdapter(new BusPagerAdapter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}