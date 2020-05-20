package in.koreatech.koin.ui.circle;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.transition.Explode;
import android.util.Pair;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleRestInteractor;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.circle.adapter.CircleRecyclerAdapter;
import in.koreatech.koin.ui.circle.presenter.CircleContract;
import in.koreatech.koin.ui.circle.presenter.CirclePresenter;

public class CircleActivity extends KoinNavigationDrawerActivity implements CircleContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "CircleActivity";
    public static final String SHARE_VIEW_NAME_APP_BAR = "KOIN_BASE_APPBAR";
    public static final String SHARE_VIEW_NAME_LOGO = "LOGO";
    public static final String SHARE_VIEW_DETAIL = "DETAIL";
    public static final String SHARE_VIEW_NAME = "NAME";
    private Context context;
    private CircleRecyclerAdapter circleRecyclerAdapytercontext;

    private CirclePresenter cirlcePresenter;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private ArrayList<Circle> cirlceArrayList; //store list
    private ArrayList<Circle> circleAllArraylist; //All store list
    private int categorySelectedNumber;
    private Resources resource;
    private String[] categoryCode;
    private RecyclerView.ViewHolder viewHolder;


    /* View Component */
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appbarBase;
    @BindView(R.id.circle_recyclerview)
    RecyclerView circleListRecyclerView;
    ImageView circleItemLogoImageview;
    ImageView circleItemLogoBackgroundImageview;
    ImageView circleItemBackgroundImageview;
    TextView circleItemNameTextview;
    TextView circleItemDetailTextview;

    private final int[] mCircleCategoryListId = {
            R.id.circle_all_imageview,
            R.id.circle_art_imageview,
            R.id.circle_show_imageview,
            R.id.circle_sport_imageview,
            R.id.circle_study_imageview,
            R.id.circle_religion_imageview,
            R.id.circle_service_imageview,
            R.id.circle_etc_imageview,
    };

    private final int[] mCircleCategoryTextviewListId = {
            R.id.circle_all_textview,
            R.id.circle_art_textview,
            R.id.circle_show_textview,
            R.id.circle_sport_textview,
            R.id.circle_study_textview,
            R.id.circle_religion_textview,
            R.id.circle_service_textview,
            R.id.circle_etc_textview,
    };
    private final int[][] mCircleCategoryIconListId = {
            {R.drawable.ic_club_all, R.drawable.ic_club_all_on},
            {R.drawable.ic_club_art, R.drawable.ic_club_art_on},
            {R.drawable.ic_club_show, R.drawable.ic_club_show_on},
            {R.drawable.ic_club_sport, R.drawable.ic_club_sport_on},
            {R.drawable.ic_club_study, R.drawable.ic_club_study_on},
            {R.drawable.ic_club_religion, R.drawable.ic_club_religion_on},
            {R.drawable.ic_club_service, R.drawable.ic_club_service_on},
            {R.drawable.ic_club_etc, R.drawable.ic_club_etc_on},
    };

    private final int UN_CLICKED = 0;
    private final int CLIICKED = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.circle_activity_main);
        ButterKnife.bind(this);
        context = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.cirlcePresenter.getCircleList(1);
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
        resource = getResources();
        this.categorySelectedNumber = 0; //메뉴 전체로 초기화
        layoutManager = new LinearLayoutManager(this);
        cirlceArrayList = new ArrayList<>();
        circleAllArraylist = new ArrayList<>();
        categoryCode = resource.getStringArray(R.array.cirlce_category_list_code);
        circleRecyclerAdapytercontext = new CircleRecyclerAdapter(context, new ArrayList<>());
        setCategoryColor(0, CLIICKED);
        circleListRecyclerView.setHasFixedSize(true);
        circleListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        circleListRecyclerView.setLayoutManager(layoutManager);
        circleListRecyclerView.setAdapter(circleRecyclerAdapytercontext);
        circleListRecyclerView.setNestedScrollingEnabled(false);

        setPresenter(new CirclePresenter(this, new CircleRestInteractor()));
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        }
    }

    @Override
    public void setPresenter(CirclePresenter presenter) {
        this.cirlcePresenter = presenter;
    }


    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @OnClick({R.id.circle_all_linear_layout, R.id.circle_art_linear_layout, R.id.circle_show_linear_layout, R.id.circle_sport_linear_layout,
            R.id.circle_study_linear_layout, R.id.circle_religion_linear_layout, R.id.circle_service_linear_layout, R.id.circle_etc_linear_layout
    })
    public void onClickCircleCategory(View view) {
        showProgressDialog(R.string.loading);
        initCategoryColor();
        switch (view.getId()) {
            case R.id.circle_all_linear_layout:
                setCategoryColor(0, CLIICKED);
                this.categorySelectedNumber = 0;
                break;
            case R.id.circle_art_linear_layout:
                setCategoryColor(1, CLIICKED);
                this.categorySelectedNumber = 1;
                break;
            case R.id.circle_show_linear_layout:
                setCategoryColor(2, CLIICKED);
                this.categorySelectedNumber = 2;
                break;
            case R.id.circle_sport_linear_layout:
                setCategoryColor(3, CLIICKED);
                this.categorySelectedNumber = 3;
                break;
            case R.id.circle_study_linear_layout:
                setCategoryColor(4, CLIICKED);
                this.categorySelectedNumber = 4;
                break;
            case R.id.circle_religion_linear_layout:
                setCategoryColor(5, CLIICKED);
                this.categorySelectedNumber = 5;
                break;
            case R.id.circle_service_linear_layout:
                setCategoryColor(6, CLIICKED);
                this.categorySelectedNumber = 6;
                break;
            case R.id.circle_etc_linear_layout:
                setCategoryColor(7, CLIICKED);
                this.categorySelectedNumber = 7;
                break;
        }
        sortByCategory(categoryCode[this.categorySelectedNumber]);
    }

    public void sortByCategory(String categoryCode) {
        cirlceArrayList.clear();

        if (categoryCode.equals(this.categoryCode[0]))
            cirlceArrayList.addAll(circleAllArraylist);
        else {
            for (Circle item : circleAllArraylist) {
                if (item.getCategory().equals(categoryCode))
                    cirlceArrayList.add(item);
            }
        }
        updateUserInterface();
        hideProgressDialog();
    }

    public void initCategoryColor() {
        for (int i = 0; i < mCircleCategoryListId.length; i++) {
            setCategoryColor(i, UN_CLICKED);
        }
    }


    public void setCategoryColor(int index, int type) {

        ImageView icon = findViewById(mCircleCategoryListId[index]);
        TextView text = findViewById(mCircleCategoryTextviewListId[index]);
        text.setTextColor(getResources().getColor((type == UN_CLICKED) ? R.color.black : R.color.colorAccent));
        icon.setImageDrawable(getResources().getDrawable(mCircleCategoryIconListId[index][type]));
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void showMessage(int message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void onCircleListDataReceived(ArrayList<Circle> circleArrayList) {
        cirlceArrayList.clear();
        circleAllArraylist.clear();

        cirlceArrayList.addAll(circleArrayList);
        circleAllArraylist.addAll(circleArrayList);
        sortByCategory(categoryCode[this.categorySelectedNumber]);

        updateUserInterface();


    }

    @Override
    public void goToCircleDetailActivity(int circleId, String circleName) {
        getWindow().setExitTransition(new Explode());
        getWindow().setAllowEnterTransitionOverlap(true);
        Intent intent = new Intent(this, CircleDetailActivity.class);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this,
                        Pair.create(appbarBase, SHARE_VIEW_NAME_APP_BAR),
                        Pair.create(circleItemLogoImageview, SHARE_VIEW_NAME_LOGO),
                        Pair.create(circleItemNameTextview, SHARE_VIEW_NAME),
                        Pair.create(circleItemDetailTextview, SHARE_VIEW_DETAIL)
                );
        intent.putExtra("CIRCLE_ID", circleId);
        intent.putExtra("CIRCLE_NAME", circleName);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void updateUserInterface() {
        circleRecyclerAdapytercontext = new CircleRecyclerAdapter(context, cirlceArrayList);
        circleListRecyclerView.setAdapter(circleRecyclerAdapytercontext);


    }

    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            viewHolder = circleListRecyclerView.findViewHolderForAdapterPosition(position);
            circleItemLogoImageview = viewHolder.itemView.findViewById(R.id.circle_item_logo_imageview);
            circleItemNameTextview = viewHolder.itemView.findViewById(R.id.circle_item_name_textview);
            circleItemDetailTextview = viewHolder.itemView.findViewById(R.id.circle_item_detail_textview);
            goToCircleDetailActivity(cirlceArrayList.get(position).getId(), cirlceArrayList.get(position).getName());
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });

    @Override
    public void onRefresh() {
        this.cirlcePresenter.getCircleList(1);
    }
}