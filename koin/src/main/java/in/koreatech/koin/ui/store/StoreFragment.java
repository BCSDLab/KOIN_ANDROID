package in.koreatech.koin.ui.store;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.StoreRestInteractor;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.store.adapter.StorePagerAdapter;
import in.koreatech.koin.ui.store.listener.OnPageSelectedListener;
import in.koreatech.koin.ui.store.presenter.StoreContract;
import in.koreatech.koin.ui.store.presenter.StorePresenter;
import in.koreatech.koin.util.NavigationManger;

public class StoreFragment extends KoinBaseFragment implements StoreContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = StoreFragment.class.getSimpleName();
    private final int[] CATEGORY_ID = {R.id.store_category_chicken, R.id.store_category_pizza, R.id.store_category_sweet_pork, R.id.store_category_sweet_dosirak,
            R.id.store_category_sweet_pork_feet, R.id.store_category_chinese, R.id.store_category_normal, R.id.store_category_hair, R.id.store_category_etc};
    private final int[] CATEGORY_TEXT_ID
            = {
            R.id.store_category_chicken_textview, R.id.store_category_pizza_textview, R.id.store_category_sweet_pork_textview, R.id.store_category_dosirak_textview,
            R.id.store_category_sweet_pork_feet_textview, R.id.store_category_chinese_textview, R.id.store_category_normal_textview,
            R.id.store_category_hair_textview, R.id.store_category_etc_textview
    };
    /* View Component */
    @BindView(R.id.store_swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.store_pager)
    ViewPager storePager;
    private Context context;
    private StorePagerAdapter storePagerAdapter;
    private String today;
    private StorePresenter storePresenter;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private ArrayList<Store> storeArrayList; //store list
    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            goToStoreDetailActivity(storeArrayList.get(position).getUid(), storeArrayList.get(position).getName());

        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });
    private ArrayList<Store> storeAllArraylist; //All store list
    private int storeCategoryNumber; // 1 .치킨 2. 피자 3. 탕수육 4. 도시락 5. 족발 6. 중국집 7. 일반음식점 8. 미용실 9. 기타
    private Resources resources;
    private String categoryCode[]; // 치킨(S005), 피자(S006), 탕수육(S007), 일반(S008), 족발(S003), 중국집(S004), 일반(S008), 미용실(S009), 기타(S000)
    private OnPageSelectedListener storePagerChangeListener = new OnPageSelectedListener() {
        @Override
        public void onPageSelected(int position) {
            resetPagerHeight(position);
            initCateGoryTextColor();
            if (position == 0) storeCategoryNumber = 0;
            else
                storeCategoryNumber = getCategoryPoistionNumber(getActivity().findViewById(CATEGORY_TEXT_ID[position - 1]));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_fragment_main, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.storePresenter.getStoreList();
    }

    @OnClick({R.id.store_category_chicken, R.id.store_category_pizza, R.id.store_category_sweet_pork, R.id.store_category_sweet_dosirak,
            R.id.store_category_sweet_pork_feet, R.id.store_category_chinese, R.id.store_category_normal, R.id.store_category_hair, R.id.store_category_etc,
            R.id.store_category_chicken_textview, R.id.store_category_pizza_textview, R.id.store_category_sweet_pork_textview, R.id.store_category_dosirak_textview,
            R.id.store_category_sweet_pork_feet_textview, R.id.store_category_chinese_textview, R.id.store_category_normal_textview,
            R.id.store_category_hair_textview, R.id.store_category_etc_textview}
    )
    public void storeCategoryOnCliked(View view) {
        int categoryPosition = getCategoryPoistionNumber(view);
        if (categoryPosition == storeCategoryNumber)
            storePager.setCurrentItem(0, true);
        else
            storePager.setCurrentItem(categoryPosition, true);
        //TODO("When clicked already selected position, Go to allList page(position 0)")
    }

    private void resetPagerHeight(int position) {
        View view = storePager.findViewWithTag(position);
        if (view != null) {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int height = view.getMeasuredHeight();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) storePager.getLayoutParams();
            layoutParams.height = height;
            Log.i("RecyclerHeight", String.valueOf(height));

            storePager.setLayoutParams(layoutParams);
        }
    }

    public void initCateGoryTextColor() {
        for (int id : CATEGORY_TEXT_ID) {
            TextView textView = getView().findViewById(id);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public int getCategoryPoistionNumber(View view) {
        for (int i = 0; i < CATEGORY_ID.length; i++) {
            if (CATEGORY_ID[i] == view.getId() || CATEGORY_TEXT_ID[i] == view.getId()) {
                TextView textview = getView().findViewById(CATEGORY_TEXT_ID[i]);
                textview.setTextColor(getResources().getColor(R.color.colorAccent));
                return i + 1;
            }
        }
        return -1;
    }

    private void init() {
        this.resources = getResources();
        categoryCode = this.resources.getStringArray(R.array.store_category_list_code);
        swipeRefreshLayout.setOnRefreshListener(this);
        this.storeCategoryNumber = 0; //메뉴 전체로 초기화
        this.layoutManager = new LinearLayoutManager(getContext());
        this.storeArrayList = new ArrayList<>();
        this.storeAllArraylist = new ArrayList<>();

        setPresenter(new StorePresenter(this, new StoreRestInteractor()));
    }

    @Override
    public void setPresenter(StorePresenter presenter) {
        this.storePresenter = presenter;
    }

    @Override
    public void showLoading() {
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void onStoreListDataReceived(ArrayList<Store> storeArrayList) {
        this.storeArrayList.clear();
        this.storeAllArraylist.clear();

        this.storeArrayList.addAll(storeArrayList);
        this.storeAllArraylist.addAll(storeArrayList);

        storePagerAdapter = new StorePagerAdapter(getActivity(), storeAllArraylist, categoryCode);
        storePager.setAdapter(storePagerAdapter);
        storePager.addOnPageChangeListener(storePagerChangeListener);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
    }

    @Override
    public void goToStoreDetailActivity(int storeUid, String storeName) {
        Bundle bundle = new Bundle();
        bundle.putInt("STORE_UID", storeUid);
        bundle.putString("STORE_NAME", storeName);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_item_store_detail_action, bundle, NavigationManger.getNavigationAnimation());
    }

    @Override
    public void onRefresh() {
        this.storePresenter.getStoreList();
    }
}
