package in.koreatech.koin.ui.bus;

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
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.ui.bus.adpater.BusMainViewPagerAdapter;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;

public class BusViewPagerFragment extends KoinBaseFragment {
    private final String TAG = "BusViewPagerFragment";
    private final String TABLAYOUT_fontName = "fonts/notosanscjkkr_regular.otf";
    /* View Component */
    @BindView(R.id.koin_base_appbar)
    AppBarBase koinBaseAppbar;
    @BindView(R.id.bus_main_tabs)
    TabLayout tabLayout;
    @BindView(R.id.bus_main_viewpager)
    ViewPager viewPager;
    private Context context;
    private BusMainViewPagerAdapter mainViewPagerAdapter;
    private InputMethodManager inputMethodManager;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_fragment_view_pager, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.viewPager.addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewPager.setAdapter(null);
    }

    private void init() {
        this.mainViewPagerAdapter = new BusMainViewPagerAdapter(getChildFragmentManager(), 3);
        this.viewPager.setOffscreenPageLimit(3);
        this.viewPager.setAdapter(this.mainViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(mPageChangeListener);
        this.tabLayout.setupWithViewPager(this.viewPager);
        changeFont(this.tabLayout.getChildAt(0), TABLAYOUT_fontName);

        //hide keyboard
        this.inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().getApplicationContext().INPUT_METHOD_SERVICE);
    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId())
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
                ((TextView) viewChild).setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontName));
            } else if (viewChild instanceof ViewGroup) {
                changeFont(viewChild, TABLAYOUT_fontName);
            }
        }
    }
}


