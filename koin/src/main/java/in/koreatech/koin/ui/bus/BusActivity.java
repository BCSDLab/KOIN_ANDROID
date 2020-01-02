package in.koreatech.koin.ui.bus;

import android.content.Context;
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
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.ui.bus.adpater.BusMainViewPagerAdapter;

/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class BusActivity extends KoinNavigationDrawerActivity {
    private final String TAG = BusActivity.class.getSimpleName();
    private final String TABLAYOUT_FONT_NAME = "fonts/notosanscjkkr_regular.otf";
    private Context mContext;
    private FirebasePerformanceUtil mFirebasePerformanceUtil;

    /* View Component */
    @BindView(R.id.koin_base_appbar)
    AppbarBase mKoinBaseAppbar;
    @BindView(R.id.bus_main_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.bus_main_viewpager)
    ViewPager mViewPager;

    private BusMainViewPagerAdapter mMainViewPagerAdapter;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_activity_main);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewPager.addOnPageChangeListener(mPageChangeListener);
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
        mFirebasePerformanceUtil.stop();
        super.onDestroy();
    }


    private void init() {

        mMainViewPagerAdapter = new BusMainViewPagerAdapter(getSupportFragmentManager(), 3);
        mFirebasePerformanceUtil = new FirebasePerformanceUtil("Bus_Activity");
        mFirebasePerformanceUtil.start();
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mMainViewPagerAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabLayout.setupWithViewPager(mViewPager);
        changeFont(mTabLayout.getChildAt(0), TABLAYOUT_FONT_NAME);

        //hide keyboard
        mInputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

    final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mInputMethodManager.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
        }
    };

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view)
    {
        int id = view.getId();
        if (id == AppbarBase.getLeftButtonId())
            onBackPressed();
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
            } else if(viewChild instanceof ViewGroup) {
                changeFont(viewChild, TABLAYOUT_FONT_NAME);
            }
        }
    }


}


