package in.koreatech.koin.ui.bus;

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
import in.koreatech.koin.util.FirebasePerformanceUtil;
import in.koreatech.koin.ui.bus.adpater.BusMainViewPagerAdapter;

public class BusActivity extends KoinNavigationDrawerActivity {
    private final String TAG = "BusActivity";
    private final String TABLAYOUT_fontName = "fonts/notosanscjkkr_regular.otf";
    private Context context;
    private FirebasePerformanceUtil firebasePerformanceUtil;

    public int departureState = 0; // 0 : 한기대 1 : 야우리 2 : 천안역
    public int arrivalState = 1; // 0 : 한기대 1 : 야우리 2 : 천안역

    /* View Component */
    @BindView(R.id.koin_base_appbar)
    AppBarBase koinBaseAppbar;
    @BindView(R.id.bus_main_tabs)
    TabLayout tabLayout;
    @BindView(R.id.bus_main_viewpager)
    ViewPager viewPager;

    private BusMainViewPagerAdapter mainViewPagerAdapter;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_activity_main);
        ButterKnife.bind(this);
        this.context = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.viewPager.addOnPageChangeListener(mPageChangeListener);
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
        this.firebasePerformanceUtil.stop();
        super.onDestroy();
    }


    private void init() {

        this.mainViewPagerAdapter = new BusMainViewPagerAdapter(getSupportFragmentManager(), 3);
        this.firebasePerformanceUtil = new FirebasePerformanceUtil("Bus_Activity");
        this.firebasePerformanceUtil.start();
        this.viewPager.setOffscreenPageLimit(3);
        this.viewPager.setAdapter(this.mainViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(mPageChangeListener);
        this.tabLayout.setupWithViewPager(this.viewPager);
        changeFont(this.tabLayout.getChildAt(0), TABLAYOUT_fontName);

        //hide keyboard
        this.inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            departureState = bundle.getInt("departure", 0);
            arrivalState = bundle.getInt("arrival", 1);
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
            inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    };

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        }else if(id == AppBarBase.getRightButtonId()){
            toggleNavigationDrawer();
        }
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
                changeFont(viewChild, TABLAYOUT_fontName);
            }
        }
    }


}


