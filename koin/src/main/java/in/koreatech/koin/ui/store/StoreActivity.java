package in.koreatech.koin.ui.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.Injection;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter;
import in.koreatech.koin.ui.store.presenter.StoreContract;
import in.koreatech.koin.ui.store.presenter.StorePresenter;
import in.koreatech.koin.util.FirebaseEventUtil;
import in.koreatech.koin.util.RxEditTextUtil;
import in.koreatech.koin.util.schedulers.SchedulerProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class StoreActivity extends KoinNavigationDrawerActivity implements StoreContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "StoreActivity";
    private final int ALL_CATEGORY = -1;
    private final int[] CATEGORY_ID = {
            R.id.store_category_chicken, R.id.store_category_pizza, R.id.store_category_sweet_pork, R.id.store_category_sweet_dosirak,
            R.id.store_category_sweet_pork_feet, R.id.store_category_chinese, R.id.store_category_normal, R.id.store_category_hair, R.id.store_category_etc
    };
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
    @BindView(R.id.category_constraint_layout)
    ConstraintLayout categoryConstraintLayout;
    @BindView(R.id.not_found_constraint_layout)
    ConstraintLayout notFoundConstraintLayout;
    @BindView(R.id.search_editText)
    EditText searchEditText;
    @BindView(R.id.search_image_view)
    ImageView searchImageView;


    private CompositeDisposable compositeDisposable;
    private Context context;
    private StoreRecyclerAdapter storeRecyclerAdapter;
    private boolean isSearchMode;
    private StorePresenter storePresenter;
    private ArrayList<Store> storeArrayList;
    private final RecyclerClickListener recyclerItemTouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            goToStoreDetailActivity(storeArrayList.get(position).getUid(), storeArrayList.get(position).getName(),storeArrayList.get(position).getCategory());
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });
    private int storeCategoryNumber; // -1. 전체 0 .치킨 1. 피자 2. 탕수육 3. 도시락 4. 족발 5. 중국집 6. 일반음식점 7. 미용실 8. 기타
    private String[] categoryCode; // 치킨(S005), 피자(S006), 탕수육(S007), 일반(S008), 족발(S003), 중국집(S004), 일반(S008), 미용실(S009), 기타(S000)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity_main);
        ButterKnife.bind(this);
        context = this;
        init();
        getCurrentCategoryStoreItem();
        FirebaseEventUtil.getInstance(this).startTrackStoreCallTime();
    }

    private void init() {
        isSearchMode = false;
        compositeDisposable = new CompositeDisposable();
        initRecyclerView();
        setPresenter(new StorePresenter(this, Injection.provideStoreRepository(), SchedulerProvider.getInstance()));
        categoryCode = getResources().getStringArray(R.array.store_category_list_code);
        swipeRefreshLayout.setOnRefreshListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.storeCategoryNumber = ALL_CATEGORY;

        if (bundle != null) {
            int category = bundle.getInt("store_category", ALL_CATEGORY); //메뉴 초기화
            storeCategoryOnClicked(findViewById(CATEGORY_TEXT_ID[category]));
        }

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                isSearchMode = true;
                categoryConstraintLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                showKeyboard(this);
                storePresenter.getStoreList();
            }
        });

        searchImageView.setOnClickListener(v -> searchEditText.setText(""));

        compositeDisposable.add(
                RxEditTextUtil.getObservable(searchEditText)
                        .debounce(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(charSequence -> {
                            if (!searchEditText.hasFocus()) {
                                return;
                            }
                            if (charSequence.length() == 0 && isSearchMode) {
                                searchImageView.setBackground(getResources().getDrawable(R.drawable.ic_search));
                                searchImageView.getLayoutParams().width = dpToPx(24);
                                searchImageView.getLayoutParams().height = dpToPx(24);
                                storePresenter.getStoreList();
                            } else {
                                searchImageView.setBackground(getResources().getDrawable(R.drawable.ic_search_close));
                                searchImageView.getLayoutParams().width = dpToPx(16);
                                searchImageView.getLayoutParams().height = dpToPx(16);
                                storePresenter.getStoreListWithStoreName(charSequence.toString());
                            }
                        }));
    }

    private void initRecyclerView() {
        this.storeArrayList = new ArrayList<>();
        this.storeRecyclerAdapter = new StoreRecyclerAdapter(context, storeArrayList);
        storeListRecyclerView.setNestedScrollingEnabled(false);
        storeListRecyclerView.setHasFixedSize(false);
        storeListRecyclerView.addOnItemTouchListener(recyclerItemTouchListener);
        storeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storeListRecyclerView.setAdapter(this.storeRecyclerAdapter);
    }


    @Override
    public void setPresenter(StorePresenter presenter) {
        this.storePresenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R.id.store_category_chicken, R.id.store_category_pizza, R.id.store_category_sweet_pork, R.id.store_category_sweet_dosirak,
            R.id.store_category_sweet_pork_feet, R.id.store_category_chinese, R.id.store_category_normal, R.id.store_category_hair, R.id.store_category_etc,
            R.id.store_category_chicken_textview, R.id.store_category_pizza_textview, R.id.store_category_sweet_pork_textview, R.id.store_category_dosirak_textview,
            R.id.store_category_sweet_pork_feet_textview, R.id.store_category_chinese_textview, R.id.store_category_normal_textview,
            R.id.store_category_hair_textview, R.id.store_category_etc_textview}
    )
    public void storeCategoryOnClicked(View view) {
        initCateGoryTextColor();
        int selectCategoryNumber = getCategoryPositionNumber(view);
        if (selectCategoryNumber == storeCategoryNumber) {
            storeCategoryNumber = ALL_CATEGORY;
        } else {
            this.storeCategoryNumber = selectCategoryNumber;
            TextView textview = findViewById(CATEGORY_TEXT_ID[this.storeCategoryNumber]);
            textview.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        getCurrentCategoryStoreItem();
    }

    public void initCateGoryTextColor() {
        for (int id : CATEGORY_TEXT_ID) {
            TextView textView = findViewById(id);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }


    public int getCategoryPositionNumber(View view) {
        for (int i = 0; i < CATEGORY_ID.length; i++) {
            if (CATEGORY_ID[i] == view.getId() || CATEGORY_TEXT_ID[i] == view.getId()) {
                return i;
            }
        }
        return ALL_CATEGORY;

    }

    @Override
    public void showLoading() {
        if (!isSearchMode)
            showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        if (!isSearchMode)
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
    public void showMessage(int message) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void onStoreListDataReceived(List<Store> storeArrayList) {
        this.storeArrayList.clear();
        this.storeArrayList.addAll(storeArrayList);

        updateUserInterface();
    }

    private void getCurrentCategoryStoreItem() {
        if (this.storeCategoryNumber == ALL_CATEGORY) {
            storePresenter.getStoreList();
            return;
        }
        // 기타 => 콜벤 포함
        if (categoryCode[storeCategoryNumber].equals("S000")) {
            storePresenter.getStoreListWithCategory("S000", "S001");
            return;
        }

        storePresenter.getStoreListWithCategory(categoryCode[storeCategoryNumber]);

    }

    @Override
    public void updateUserInterface() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (storeArrayList.isEmpty() && !searchEditText.getText().toString().isEmpty()) {
            storeListRecyclerView.setVisibility(View.GONE);
            notFoundConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            storeListRecyclerView.setVisibility(View.VISIBLE);
            notFoundConstraintLayout.setVisibility(View.GONE);
        }

        storeRecyclerAdapter.notifyDataSetChanged();
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

    public void goToStoreDetailActivity(int storeUid, String storeName, String category) {
        Intent intent = new Intent(this, StoreDetailActivity.class);
        intent.putExtra("STORE_UID", storeUid);
        intent.putExtra("STORE_NAME", storeName);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        storePresenter.refresh();
        getCurrentCategoryStoreItem();
    }

    @Override
    public void onBackPressed() {
        if (isSearchMode) {
            isSearchMode = false;
            hideKeyboard(this);
            searchEditText.setText("");
            searchEditText.clearFocus();
            categoryConstraintLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(true);
            getCurrentCategoryStoreItem();
        } else {
            super.onBackPressed();
        }
    }

    private void hideKeyboard(Activity activity) {
        View view = this.getCurrentFocus();
        if (view == null)
            return;
        InputMethodManager im = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(Activity activity) {
        View view = this.getCurrentFocus();
        if (view == null)
            return;
        InputMethodManager im = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

    private int dpToPx(float dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return px;
    }

    @Override
    protected void onDestroy() {
        storePresenter.unSubscribe();
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
