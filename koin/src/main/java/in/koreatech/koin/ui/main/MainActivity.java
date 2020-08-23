package in.koreatech.koin.ui.main;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.utils.StatusBarUtil;
import in.koreatech.koin.core.viewpager.ScaleViewPager;
import in.koreatech.koin.ui.main.enums.DiningKinds;

public class MainActivity extends ActivityBase {
    private static String TAG = MainActivity.class.getSimpleName();

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    MaterialCardView toolbar;
    @BindView(R.id.toolbar_layout)
    FrameLayout toolbarLayout;

    //버스
    @BindView(R.id.main_view_pager)
    ScaleViewPager busCardViewPager;

    //상점
    @BindView(R.id.recycler_view_store_category)
    RecyclerView recyclerViewStoreCategory;

    //학식
    @BindViews({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_special})
    List<TextView> textViewDiningKinds;

    @BindViews({R.id.text_view_card_dining_menu_0,
            R.id.text_view_card_dining_menu_1,
            R.id.text_view_card_dining_menu_2,
            R.id.text_view_card_dining_menu_3,
            R.id.text_view_card_dining_menu_4,
            R.id.text_view_card_dining_menu_5,
            R.id.text_view_card_dining_menu_6,
            R.id.text_view_card_dining_menu_7,
            R.id.text_view_card_dining_menu_8,
            R.id.text_view_card_dining_menu_9})
    List<TextView> textViewDiningMenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        unbinder = ButterKnife.bind(this);
        StatusBarUtil.applyTopPaddingStatusBarHeight(toolbarLayout, getResources());

        initBusPager();
        initStoreRecyclerView();
        initDining();
    }

    private void initBusPager() {
        busCardViewPager.setAdapter(new BusPagerAdapter());
        busCardViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        busCardViewPager.setOffscreenPageLimit(5);
    }

    private void initStoreRecyclerView() {
        recyclerViewStoreCategory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewStoreCategory.setAdapter(new StoreCategoryRecyclerAdapter());
    }

    private void initDining() {
        selectDiningKind(textViewDiningKinds.get(DiningKinds.KOREAN.getPosition()));
    }

    @OnClick({R.id.text_view_card_dining_korean, R.id.text_view_card_dining_onedish, R.id.text_view_card_dining_western, R.id.text_view_card_dining_special})
    void selectDiningKind(View view) {
        for (TextView textView : textViewDiningKinds) textView.setSelected(false);
        view.setSelected(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}