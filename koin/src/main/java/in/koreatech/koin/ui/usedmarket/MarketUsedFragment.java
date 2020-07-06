package in.koreatech.koin.ui.usedmarket;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedMainViewPagerAdapter;
import in.koreatech.koin.util.AuthorizeManager;
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.util.NavigationManger;

public class MarketUsedFragment extends KoinBaseFragment {
    private final String TAG = "MarketUsedFragment";
    /* View Component */
    @BindView(R.id.market_used_main_tabs)
    TabLayout tabLayout;
    @BindView(R.id.market_used_main_viewpager)
    ViewPager viewPager;
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseAppbar;
    private String tableLayoutFontName;
    private Context context;
    private FirebasePerformanceUtil firebasePerformanceUtil;
    private MarketUsedMainViewPagerAdapter mainViewPagerAdapter;
    private InputMethodManager inputMethodManager;
    private int pageSelected;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_used_fragment_main, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewPager.addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        firebasePerformanceUtil.stop();
        this.viewPager.setAdapter(null);
        super.onDestroy();
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId()) {
            createMarketItemFragmentMove(pageSelected);
        }
    }

    public void createMarketItemFragmentMove(int id) {
        if (AuthorizeManager.getAuthorize(getContext()) == AuthorizeConstant.ANONYMOUS) {
            AuthorizeManager.showLoginRequestDialog(getActivity());
            return;
        }
        if (AuthorizeManager.getNickName(getContext()) != null) {
            if (id == 0) {
                NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_sell_create_action,null, NavigationManger.getNavigationAnimation());
            } else if (id == 1) {
                NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_buy_create_action,null, NavigationManger.getNavigationAnimation());
            }
        } else {
            AuthorizeManager.showNickNameRequestDialog(getActivity());
        }

    }

    /**
     * Initialize MainViewPageAdapter ,TabLayout, ViewPager and InputMethodManger
     * Set TabCount to 2 (MarketUsed Sell, MarketUsed buy)
     * Set offScreenPageLimit to 2 (MarketUsed Sell, MarketUsed buy)
     * Hide keyboard using InputManager
     */
    private void init() {
        pageSelected = 0;
        mainViewPagerAdapter = new MarketUsedMainViewPagerAdapter(getChildFragmentManager(), 2);
        firebasePerformanceUtil = new FirebasePerformanceUtil("Market_used");
        firebasePerformanceUtil.start();
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mainViewPagerAdapter);
        viewPager.addOnPageChangeListener(mPageChangeListener);
        tabLayout.setupWithViewPager(viewPager);
        tableLayoutFontName = getResources().getString(R.string.font_kr_regular);
        changeFont(tabLayout.getChildAt(0), tableLayoutFontName);
        //hide keyboard
        inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().getApplicationContext().INPUT_METHOD_SERVICE);
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
                ((TextView) viewChild).setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontName));
            } else if (viewChild instanceof ViewGroup) {
                changeFont(viewChild, tableLayoutFontName);
            }
        }
    }
}

