package in.koreatech.koin.ui.usedmarket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedMainViewPagerAdapter;
import in.koreatech.koin.ui.userinfo.UserInfoEditedActivity;

public class MarketUsedActivity extends KoinNavigationDrawerActivity {
    private final String TAG = "MarketUsedActivity";
    private String tableLayoutFontName;
    private Context context;
    private FirebasePerformanceUtil firebasePerformanceUtil;

    /* View Component */
    @BindView(R.id.market_used_main_tabs)
    TabLayout tabLayout;
    @BindView(R.id.market_used_main_viewpager)
    ViewPager viewPager;
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseAppbar;

    private MarketUsedMainViewPagerAdapter mainViewPagerAdapter;
    private InputMethodManager inputMethodManager;
    private int pageSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_used_activity_main);
        ButterKnife.bind(this);
        context = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewPager.addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        firebasePerformanceUtil.stop();
        super.onDestroy();
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId()) {
            createActivityMove(pageSelected);
        }
    }

    public void createActivityMove(int id) {
        Intent intent = null;
        if (id == 0) {
            intent = new Intent(this, MarketUsedSellCreateActivity.class);
        } else if (id == 1) {
            intent = new Intent(this, MarketUsedBuyCreateActivity.class);
        }

        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        }
        if (getUser().userNickName != null)
            startActivity(intent);
        else {
            ToastUtil.getInstance().makeShort("닉네임이 필요합니다.");
            intent = new Intent(this, UserInfoEditedActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        callDrawerItem(R.id.navi_item_home);
    }

    /**
     * Initialize MainViewPageAdapter ,TabLayout, ViewPager and InputMethodManger
     * Set TabCount to 2 (MarketUsed Sell, MarketUsed buy)
     * Set offScreenPageLimit to 2 (MarketUsed Sell, MarketUsed buy)
     * Hide keyboard using InputManager
     */
    private void init() {
        pageSelected = 0;
        mainViewPagerAdapter = new MarketUsedMainViewPagerAdapter(getSupportFragmentManager(), 2);
        firebasePerformanceUtil = new FirebasePerformanceUtil("Market_used");
        firebasePerformanceUtil.start();
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mainViewPagerAdapter);
        viewPager.addOnPageChangeListener(mPageChangeListener);
        tabLayout.setupWithViewPager(viewPager);
        changeFont(tabLayout.getChildAt(0), tableLayoutFontName);
        tableLayoutFontName = getResources().getString(R.string.font_kr_regular);
        //hide keyboard
        inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }


    private void changeFont(View view, String fontName) {
        if (view == null) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View viewChild = viewGroup.getChildAt(i);
            if (viewChild instanceof TextView) {
                ((TextView) viewChild).setTypeface(Typeface.createFromAsset(getAssets(), fontName));
            } else if (viewChild instanceof ViewGroup) {
                changeFont(viewChild, tableLayoutFontName);
            }
        }
    }


    final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    pageSelected = 0;
                    break;
                case 1:
                    pageSelected = 1;
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    };
}

