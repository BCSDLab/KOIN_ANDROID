package in.koreatech.koin.service_store.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

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
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_store.adapters.StoreDetailFlyerRecyclerAdapter;
import in.koreatech.koin.service_store.adapters.StoreDetailMenuRecyclerAdapter;
import in.koreatech.koin.service_store.contracts.StoreDetailContract;
import in.koreatech.koin.service_store.presenters.StoreDetailPresenter;

public class StoreDetailActivity extends KoinNavigationDrawerActivity implements StoreDetailContract.View {
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;  //User Permission Request Code

    private Context mContext;
    private StoreDetailPresenter mStoreDetailPresenter;
    private CustomProgressDialog customProgressDialog;

    private Store mStore;

    /* View Component */
    @BindView(R.id.store_detail_title_textview)
    TextView mTitleTextView;
    @BindView(R.id.store_detail_phone_textview)
    TextView mPhoneTextView;
    @BindView(R.id.store_detail_time_textview)
    TextView mTimeTextView;
    @BindView(R.id.store_detail_address_textview)
    TextView mAddressTextview;
    @BindView(R.id.store_detail_etc_textview)
    TextView  mEtcTextview;
    @BindView(R.id.store_detail_deliver_textview)
    TextView mDeliverTextview;


    //배달/카드/계좌이체 여부
    @BindView(R.id.store_detail_is_delivery_textview)
    TextView mIsDeliveryTextView;
    @BindView(R.id.store_detail_is_card_textview)
    TextView mIsCardTextView;
    @BindView(R.id.store_detail_is_bank_textview)
    TextView mIsBankTextView;

    // 메뉴 리스트
    @BindView(R.id.store_detail_recyclerview)
    RecyclerView mMenuListRecyclerView;
    @BindView(R.id.store_detail_flyer_recyclerview)
    RecyclerView flyerListRecyclerView;

    private RecyclerView.LayoutManager mMenuLayoutManager; // Menu RecyclerView LayoutManager
    private RecyclerView.LayoutManager mFlyerLayoutManger; // Flyer RecycerView LayoutManger
    private ArrayList<Store> mStoreMenuArrayList; //store menu list
    private ArrayList<String> storeFlyerArrayList;
    private StoreDetailMenuRecyclerAdapter mStoreDetailMenuRecyclerAdapter;
    private StoreDetailFlyerRecyclerAdapter storeDetailFlyerRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity_detail);
        ButterKnife.bind(this);
        this.mContext = this;

        init();
    }

    private void init() {
        mStore = new Store();
        mStore.uid = getIntent().getIntExtra("STORE_UID", 0);
        mStore.name = getIntent().getStringExtra("STORE_NAME");

        setPresenter(new StoreDetailPresenter(this, new StoreRestInteractor()));
        mStoreDetailPresenter.getStore(mStore.uid);

        mMenuLayoutManager = new LinearLayoutManager(this);
        mFlyerLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mStoreMenuArrayList = new ArrayList<>();
        storeFlyerArrayList = new ArrayList<>();
        mStoreDetailMenuRecyclerAdapter = new StoreDetailMenuRecyclerAdapter(mContext, mStoreMenuArrayList);
        storeDetailFlyerRecyclerAdapter = new StoreDetailFlyerRecyclerAdapter(mContext, storeFlyerArrayList);

        mMenuListRecyclerView.setHasFixedSize(true);
        mMenuListRecyclerView.setLayoutManager(mMenuLayoutManager);
        mMenuListRecyclerView.setAdapter(mStoreDetailMenuRecyclerAdapter);

        flyerListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        flyerListRecyclerView.setHasFixedSize(true);
        flyerListRecyclerView.setLayoutManager(mFlyerLayoutManger);
        flyerListRecyclerView.setAdapter(storeDetailFlyerRecyclerAdapter);
    }

    @Override
    public void setPresenter(StoreDetailPresenter presenter) {
        this.mStoreDetailPresenter = presenter;
    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
    }

    @Override
    public void onStoreDataReceived(Store store) {
        StringBuilder stringBuilder;
        mStore = new Store();
        mStore = store;
        mStoreMenuArrayList.clear();
        storeFlyerArrayList.clear();
        if (mStore.imageUrls != null)
            storeFlyerArrayList.addAll(mStore.imageUrls);
        storeDetailFlyerRecyclerAdapter.notifyDataSetChanged();
        if (!store.menus.isEmpty()) {
            for (int a = 0; a < store.menus.size(); a++) {
                Store storeItemDetail = new Store();
                storeItemDetail.name = store.menus.get(a).name;
                stringBuilder = new StringBuilder();
                for (int b = 0; b < store.menus.get(a).priceType.size(); b++) {
                    storeItemDetail.size = store.menus.get(a).priceType.get(b).size;
                    storeItemDetail.price = store.menus.get(a).priceType.get(b).price;
                    if (!stringBuilder.toString().equals("")) stringBuilder.append("\n");
                    if (!storeItemDetail.size.equals("기본")) {
                        stringBuilder.append(storeItemDetail.size).append(": ").append(changeMoneyFormat(storeItemDetail.price)).append("원");
                    } else {
                        stringBuilder.append(changeMoneyFormat(storeItemDetail.price)).append("원");
                    }
                }
                storeItemDetail.detail = stringBuilder.toString();
                mStoreMenuArrayList.add(storeItemDetail);
            }
            mStoreDetailMenuRecyclerAdapter.setStoreMenuArrayList(mStoreMenuArrayList);
            mStoreDetailMenuRecyclerAdapter.notifyDataSetChanged();
        }


        updateUserInterface();
        onResume();

    }


    @Override
    protected void onResume() {
        super.onResume();
        mStoreDetailMenuRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void updateUserInterface() {
        mTitleTextView.setText(mStore.name);
        mPhoneTextView.setText(Objects.requireNonNull(mStore.phone,"-"));
        mAddressTextview.setText(Objects.requireNonNull(mStore.address,"-"));
        mEtcTextview.setText(Objects.requireNonNull(mStore.description,"-"));

        if(mStore.deliveryPrice >= 0)
        mDeliverTextview.setText(mStore.deliveryPrice + "원");
        else
            mDeliverTextview.setText("-");

        if (!FormValidatorUtil.validateStringIsEmpty(mStore.openTime)) {
            mTimeTextView.setText(mStore.openTime + " ~ " + mStore.closeTime);
        } else {
            mTimeTextView.setText("-");
        }
        if (!mStore.isDeliveryOk)
            mIsDeliveryTextView.setVisibility(View.GONE);
        if (!mStore.isCardOk)
            mIsCardTextView.setVisibility(View.GONE);
        if (!mStore.isBankOk)
            mIsBankTextView.setVisibility(View.GONE);

//        setTextviewTextWithBackground(mIsDeliveryTextView, "#배달", mStore.isDeliveryOk);
//        setTextviewTextWithBackground(mIsCardTextView, "#카드", mStore.isCardOk);
//        setTextviewTextWithBackground(mIsBankTextView, "#계좌이체", mStore.isBankOk);

    }

//    private void setTextviewTextWithBackground(TextView textView, String firstText, boolean isOk) {
//        if (isOk) {
//            textView.setText(firstText + "가능");
//            textView.setBackground(getResources().getDrawable(R.drawable.button_rect_accent_radius_25dp));
//            textView.setTextColor(getResources().getColor(R.color.white));
//        } else {
//            textView.setText(firstText + "불가능");
//            textView.setBackground(getResources().getDrawable(R.drawable.button_rect_mono_radius_25dp));
//            textView.setTextColor(getResources().getColor(R.color.colorSecondaryText));
//        }
//        textView.setPadding(20, 0, 20, 0);
//    }

    @OnClick(R.id.store_detail_call_button)
    public void onClickedStoreCallButton() {
        requestPermission();

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.KAPDialog));

        builder.setMessage(mStore.name + "\n\n" + mStore.phone);

        builder.setPositiveButton("통화",
                (dialog, which) -> {
                    onClickCallButton();    //call intent 수행
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onClickCallButton() {
        String callNumber = mStore.phone;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber))); //콜 인텐트 수행
        }
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShortToast(message);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(getDrawerLayoutID());
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            finish();
    }

    @Override
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(mContext), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {//해당 권한이 없을 경우 실행
            //설명 dialog 표시
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {//사용자가 권한 거부시 재 요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {//최초 권한 요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
    }

    public String changeMoneyFormat(String money) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String changedStirng = "";

        if (!TextUtils.isEmpty(money) && !money.equals(changedStirng)) {
            changedStirng = decimalFormat.format(Double.parseDouble(money.replaceAll(",", "")));
        }
        return changedStirng;
    }

    @Override
    public void showLoading() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(this, "로딩 중");
            customProgressDialog.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (customProgressDialog != null) {
            customProgressDialog.cancel(true);
            customProgressDialog = null;
        }
    }

    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
           Intent intent = new Intent(mContext, StoreFlyerViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
           intent.putExtra("POSITION",position);
           intent.putStringArrayListExtra("URLS",mStore.imageUrls);
           startActivity(intent);
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });
}
