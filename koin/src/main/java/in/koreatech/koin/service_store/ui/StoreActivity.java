package in.koreatech.koin.service_store.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.helpers.RecyclerClickListener;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.core.networks.entity.Store;
import in.koreatech.koin.core.networks.interactors.StoreRestInteractor;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_store.adapters.StoreRecyclerAdapter;
import in.koreatech.koin.service_store.contracts.StoreContract;
import in.koreatech.koin.service_store.presenters.StorePresenter;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class StoreActivity extends KoinNavigationDrawerActivity implements StoreContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = StoreActivity.class.getSimpleName();

    private Context mContext;
    private StoreRecyclerAdapter mStoreRecyclerAdapter;
    private static CustomProgressDialog mGenerateProgress;

    private String today;
    private StorePresenter mStorePresenter;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager
    private ArrayList<Store> mStoreArrayList; //store list
    private ArrayList<Store> mStoreAllArraylist; //All store list
    private int mStoreCategoryNumber; // 1 .치킨 2. 피자 3. 탕수육 4. 도시락 5. 족발 6. 중국집 7. 일반음식점 8. 미용실 9. 기타
    private Resources mResources;
    private String mCategoryCode[]; // 치킨(S005), 피자(S006), 탕수육(S007), 일반(S008), 족발(S003), 중국집(S004), 일반(S008), 미용실(S009), 기타(S000)
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
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.store_recyclerview)
    RecyclerView mStoreListRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity_main);
        ButterKnife.bind(this);
        mContext = this;
        init();
        //TODO:필터기능 추가
    }

    @Override
    public void onStart() {
        super.onStart();
        mStorePresenter.getStoreList();
    }

    @OnClick({R.id.store_category_chicken, R.id.store_category_pizza, R.id.store_category_sweet_pork, R.id.store_category_sweet_dosirak,
            R.id.store_category_sweet_pork_feet, R.id.store_category_chinese, R.id.store_category_normal, R.id.store_category_hair, R.id.store_category_etc,
            R.id.store_category_chicken_textview, R.id.store_category_pizza_textview, R.id.store_category_sweet_pork_textview, R.id.store_category_dosirak_textview,
            R.id.store_category_sweet_pork_feet_textview, R.id.store_category_chinese_textview, R.id.store_category_normal_textview,
            R.id.store_category_hair_textview, R.id.store_category_etc_textview}
    )
    public void storeCategoryOnCliked(View view) {
        initCateGoryTextColor();
        mStoreCategoryNumber = getCategoryPoistionNumber(view);
        if (!mStoreAllArraylist.isEmpty())
            sortStoreCategorize(mStoreCategoryNumber);
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
                textview.setTextColor(getResources().getColor(R.color.squash));
                return i + 1;
            }
        }
        return -1;

    }


    public void sortStoreCategorize(int position) {
        mGenerateProgress = new CustomProgressDialog(mContext, "로딩 중");
        mGenerateProgress.execute();
        mStoreArrayList.clear();

        if (position == 0)
            mStoreArrayList.addAll(mStoreAllArraylist);
        else {
            for (int a = 0; a < mStoreAllArraylist.size(); a++)
                if (isSameCategory(mStoreAllArraylist.get(a).category, mCategoryCode[position - 1]))
                    mStoreArrayList.add(mStoreAllArraylist.get(a));
        }
        mGenerateProgress.cancel(true);
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
        mResources = getResources();
        mCategoryCode = mResources.getStringArray(R.array.store_category_list_code);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mStoreCategoryNumber = 0; //메뉴 전체로 초기화
        mLayoutManager = new LinearLayoutManager(this);
        mStoreArrayList = new ArrayList<>();
        mStoreAllArraylist = new ArrayList<>();
        mStoreRecyclerAdapter = new StoreRecyclerAdapter(mContext, new ArrayList<Store>());
        mStoreListRecyclerView.setNestedScrollingEnabled(false);
        mStoreListRecyclerView.setHasFixedSize(false);
        mStoreListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        mStoreListRecyclerView.setLayoutManager(mLayoutManager);
        mStoreListRecyclerView.setAdapter(mStoreRecyclerAdapter);

        setPresenter(new StorePresenter(this, new StoreRestInteractor()));
    }

    @Override
    public void setPresenter(StorePresenter presenter) {
        this.mStorePresenter = presenter;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        ToastUtil.getInstance().makeShortToast(message);
    }

    @Override
    public void onStoreListDataReceived(ArrayList<Store> storeArrayList) {
        mStoreArrayList.clear();
        mStoreAllArraylist.clear();

        mStoreArrayList.addAll(storeArrayList);
        mStoreAllArraylist.addAll(storeArrayList);
        if (!mStoreArrayList.isEmpty())
            sortStoreCategorize(mStoreCategoryNumber);

        updateUserInterface();


        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateUserInterface() {
        mStoreRecyclerAdapter = new StoreRecyclerAdapter(mContext, mStoreArrayList);
        mStoreListRecyclerView.setAdapter(mStoreRecyclerAdapter);

    }

    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            goToStoreDetailActivity(mStoreArrayList.get(position).uid, mStoreArrayList.get(position).name);

        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
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
        mStorePresenter.getStoreList();
    }
}
