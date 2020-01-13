package in.koreatech.koin.ui.store;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.core.helper.RecyclerClickListener;
import in.koreatech.koin.core.helper.RecyclerViewClickListener;
import in.koreatech.koin.data.network.entity.Store;
import in.koreatech.koin.data.network.interactor.StoreRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.store.adapter.StoreDetailFlyerRecyclerAdapter;
import in.koreatech.koin.ui.store.adapter.StoreDetailMenuRecyclerAdapter;
import in.koreatech.koin.ui.store.presenter.StoreDetailContract;
import in.koreatech.koin.ui.store.presenter.StoreDetailPresenter;

public class StoreDetailActivity extends KoinNavigationDrawerActivity implements StoreDetailContract.View {
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;  //User Permission Request Code

    private Context context;
    private StoreDetailPresenter storeDetailPresenter;

    private Store store;

    /* View Component */
    @BindView(R.id.store_detail_title_textview)
    TextView titleTextView;
    @BindView(R.id.store_detail_phone_textview)
    TextView phoneTextView;
    @BindView(R.id.store_detail_time_textview)
    TextView timeTextView;
    @BindView(R.id.store_detail_address_textview)
    TextView addressTextview;
    @BindView(R.id.store_detail_etc_textview)
    TextView etcTextview;
    @BindView(R.id.store_detail_deliver_textview)
    TextView deliverTextview;
    @BindView(R.id.store_detail_call_button)
    LinearLayout storeDetailCallButton;


    //배달/카드/계좌이체 여부
    @BindView(R.id.store_detail_is_delivery_textview)
    TextView isDeliveryTextView;
    @BindView(R.id.store_detail_is_card_textview)
    TextView isCardTextView;
    @BindView(R.id.store_detail_is_bank_textview)
    TextView isBankTextView;

    // 메뉴 리스트
    @BindView(R.id.store_detail_recyclerview)
    RecyclerView menuListRecyclerView;
    @BindView(R.id.store_detail_flyer_recyclerview)
    RecyclerView flyerListRecyclerView;

    private RecyclerView.LayoutManager menuLayoutManager; // Menu RecyclerView LayoutManager
    private RecyclerView.LayoutManager flyerLayoutManger; // Flyer RecycerView LayoutManger
    private ArrayList<Store> storeMenuArrayList; //store menu list
    private ArrayList<String> storeFlyerArrayList;
    private StoreDetailMenuRecyclerAdapter storeDetailMenuRecyclerAdapter;
    private StoreDetailFlyerRecyclerAdapter storeDetailFlyerRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity_detail);
        ButterKnife.bind(this);
        this.context = this;

        init();
    }

    private void init() {
        this.store = new Store();
        this.store.uid = getIntent().getIntExtra("STORE_UID", 0);
        this.store.name = getIntent().getStringExtra("STORE_NAME");

        setPresenter(new StoreDetailPresenter(this, new StoreRestInteractor()));
        this.storeDetailPresenter.getStore(this.store.uid);

        menuLayoutManager = new LinearLayoutManager(this);
        flyerLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.storeMenuArrayList = new ArrayList<>();
        storeFlyerArrayList = new ArrayList<>();
        this.storeDetailMenuRecyclerAdapter = new StoreDetailMenuRecyclerAdapter(this.context, this.storeMenuArrayList);
        storeDetailFlyerRecyclerAdapter = new StoreDetailFlyerRecyclerAdapter(this.context, storeFlyerArrayList);

        menuListRecyclerView.setHasFixedSize(true);
        menuListRecyclerView.setLayoutManager(menuLayoutManager);
        menuListRecyclerView.setAdapter(this.storeDetailMenuRecyclerAdapter);

        flyerListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        flyerListRecyclerView.setHasFixedSize(true);
        flyerListRecyclerView.setLayoutManager(flyerLayoutManger);
        flyerListRecyclerView.setAdapter(storeDetailFlyerRecyclerAdapter);
    }

    @Override
    public void setPresenter(StoreDetailPresenter presenter) {
        this.storeDetailPresenter = presenter;
    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppbarBase.getLeftButtonId())
            onBackPressed();
    }

    @Override
    public void onStoreDataReceived(Store store) {
        StringBuilder stringBuilder;
        this.store = new Store();
        this.store = store;
        this.storeMenuArrayList.clear();
        storeFlyerArrayList.clear();
        if (this.store.imageUrls != null)
            storeFlyerArrayList.addAll(this.store.imageUrls);
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
                this.storeMenuArrayList.add(storeItemDetail);
            }
            this.storeDetailMenuRecyclerAdapter.setStoreMenuArrayList(this.storeMenuArrayList);
            this.storeDetailMenuRecyclerAdapter.notifyDataSetChanged();
        }


        updateUserInterface();
        onResume();

    }


    @Override
    protected void onResume() {
        super.onResume();
        this.storeDetailMenuRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void updateUserInterface() {
        //상점 전화번호,주소,기타정보가 NULL일시에 - 표시를 한다
        titleTextView.setText(this.store.name);
        if (this.store.phone == null) {
            phoneTextView.setText("-");
            storeDetailCallButton.setVisibility(View.INVISIBLE);
        }
        if (this.store.address == null) {
            addressTextview.setText("-");
        }
        if (this.store.description == null) {
            etcTextview.setText("-");
        }
        /*
        phoneTextView.setText(Objects.requireNonNull(this.store.phone,"-"));
        addressTextview.setText(Objects.requireNonNullElse(this.store.address,"-"));
        etcTextview.setText(Objects.requireNonNull(this.store.description,"-"));
         */
        if (this.store.deliveryPrice >= 0)
            deliverTextview.setText(this.store.deliveryPrice + "원");
        else
            deliverTextview.setText("-");

        if (!FormValidatorUtil.validateStringIsEmpty(this.store.openTime)) {
            timeTextView.setText(this.store.openTime + " ~ " + this.store.closeTime);
        } else {
            timeTextView.setText("-");
        }
        if (!this.store.isDeliveryOk)
            isDeliveryTextView.setVisibility(View.GONE);
        if (!this.store.isCardOk)
            isCardTextView.setVisibility(View.GONE);
        if (!this.store.isBankOk)
            isBankTextView.setVisibility(View.GONE);

//        setTextviewTextWithBackground(isDeliveryTextView, "#배달", this.store.isDeliveryOk);
//        setTextviewTextWithBackground(isCardTextView, "#카드", this.store.isCardOk);
//        setTextviewTextWithBackground(isBankTextView, "#계좌이체", this.store.isBankOk);

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
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this.context, R.style.KAPDialog));

        builder.setMessage(this.store.name + "\n\n" + this.store.phone);

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
        String callNumber = this.store.phone;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber))); //콜 인텐트 수행
        }
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
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
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this.context), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {//해당 권한이 없을 경우 실행
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
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            Intent intent = new Intent(context, StoreFlyerViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("POSITION", position);
            intent.putStringArrayListExtra("URLS", store.imageUrls);
            startActivity(intent);
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });
}
