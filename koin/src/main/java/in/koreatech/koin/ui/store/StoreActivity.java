package in.koreatech.koin.ui.store;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter;
import in.koreatech.koin.ui.store.presenter.StoreContract;
import in.koreatech.koin.ui.store.presenter.StorePresenter;

public class StoreActivity extends KoinNavigationDrawerActivity implements StoreContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = StoreActivity.class.getSimpleName();
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
    @BindView(R.id.store_recyclerview)
    RecyclerView storeListRecyclerView;
    private Context context;
    private StoreRecyclerAdapter storeRecyclerAdapter;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity_main);
        ButterKnife.bind(this);
        context = this;
        init();
        //TODO:필터기능 추가
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
        initCateGoryTextColor();
        this.storeCategoryNumber = getCategoryPoistionNumber(view);
        if (!this.storeAllArraylist.isEmpty())
            sortStoreCategorize(this.storeCategoryNumber);
    }

    public void initCateGoryTextColor() {
        for (int id : CATEGORY_TEXT_ID) {
            TextView textView = findViewById(id);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public int getCategoryPoistionNumber(View view) {
        for (int i = 0; i < CATEGORY_ID.length; i++) {
            if (CATEGORY_ID[i] == view.getId() || CATEGORY_TEXT_ID[i] == view.getId()) {
                TextView textview = findViewById(CATEGORY_TEXT_ID[i]);
                textview.setTextColor(getResources().getColor(R.color.colorAccent));
                return i + 1;
            }
        }
        return -1;

    }

    public void sortStoreCategorize(int position) {
        this.storeArrayList.clear();
        if (position == 0)
            this.storeArrayList.addAll(this.storeAllArraylist);
        else {
            for (int a = 0; a < this.storeAllArraylist.size(); a++)
                if (isSameCategory(this.storeAllArraylist.get(a).getCategory(), categoryCode[position - 1]))
                    this.storeArrayList.add(this.storeAllArraylist.get(a));
        }
        updateUserInterface();

    }

    public boolean isSameCategory(String first, String second) {
        if (first.equals("S001") && second.equals("S000"))
            return true;
        return first.equals(second);
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

    private void init() {
        this.resources = getResources();
        categoryCode = this.resources.getStringArray(R.array.store_category_list_code);
        swipeRefreshLayout.setOnRefreshListener(this);
        this.storeCategoryNumber = 0; //메뉴 전체로 초기화
        this.layoutManager = new LinearLayoutManager(this);
        this.storeArrayList = new ArrayList<>();
        this.storeAllArraylist = new ArrayList<>();
        this.storeRecyclerAdapter = new StoreRecyclerAdapter(context, new ArrayList<Store>());
        storeListRecyclerView.setNestedScrollingEnabled(false);
        storeListRecyclerView.setHasFixedSize(false);
        storeListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        storeListRecyclerView.setLayoutManager(this.layoutManager);
        storeListRecyclerView.setAdapter(this.storeRecyclerAdapter);

        setPresenter(new StorePresenter(this, new StoreRestInteractor()));
    }

    @Override
    public void setPresenter(StorePresenter presenter) {
        this.storePresenter = presenter;
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
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
        if (!this.storeArrayList.isEmpty())
            sortStoreCategorize(this.storeCategoryNumber);

        updateUserInterface();


        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateUserInterface() {
        this.storeRecyclerAdapter = new StoreRecyclerAdapter(context, this.storeArrayList);
        storeListRecyclerView.setAdapter(this.storeRecyclerAdapter);

    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        } else if (id == AppBarBase.getRightButtonId()) {
            toggleNavigationDrawer();
        }
    }

    @Override
    public void goToStoreDetailActivity(int storeUid, String storeName) {
        Intent intent = new Intent(this, StoreDetailActivity.class);
        intent.putExtra("STORE_UID", storeUid);
        intent.putExtra("STORE_NAME", storeName);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        this.storePresenter.getStoreList();
    }
}
