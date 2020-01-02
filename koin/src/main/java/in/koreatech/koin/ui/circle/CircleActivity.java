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
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.core.helper.RecyclerClickListener;
import in.koreatech.koin.core.helper.RecyclerViewClickListener;
import in.koreatech.koin.data.network.entity.Circle;
import in.koreatech.koin.data.network.interactor.CircleRestInteractor;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.circle.adapter.CircleRecyclerAdapter;
import in.koreatech.koin.ui.circle.presenter.CIrcleContract;
import in.koreatech.koin.ui.circle.presenter.CirclePresenter;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class CircleActivity extends KoinNavigationDrawerActivity implements CIrcleContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = CircleActivity.class.getSimpleName();
    public static final String SHARE_VIEW_NAME_APP_BAR = "KOIN_BASE_APPBAR";
    public static final String SHARE_VIEW_NAME_LOGO = "LOGO";
    public static final String SHARE_VIEW_DETAIL = "DETAIL";
    public static final String SHARE_VIEW_NAME = "NAME";
    private Context mContext;
    private CircleRecyclerAdapter mCircleRecyclerAdapyter;
    private static CustomProgressDialog mGenerateProgress;

    private CirclePresenter mCirlcePresenter;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager
    private ArrayList<Circle> mCirlceArrayList; //store list
    private ArrayList<Circle> mCircleAllArraylist; //All store list
    private int mCategorySelectedNumber;
    private Resources mResource;
    private String[] mCategoryCode;
    private RecyclerView.ViewHolder mViewHolder;


    /* View Component */
    @BindView(R.id.koin_base_app_bar_dark)
    AppbarBase mAppbarBase;
    @BindView(R.id.circle_recyclerview)
    RecyclerView mCircleListRecyclerView;
    ImageView mCircleItemLogoImageview;
    ImageView mCircleItemLogoBackgroundImageview;
    ImageView mCircleItemBackgroundImageview;
    TextView mCircleItemNameTextview;
    TextView mCircleItemDetailTextview;

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
        mContext = this;
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCirlcePresenter.getCirlceList(1);
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
        mResource = getResources();
        mCategorySelectedNumber = 0; //메뉴 전체로 초기화
        mLayoutManager = new LinearLayoutManager(this);
        mCirlceArrayList = new ArrayList<>();
        mCircleAllArraylist = new ArrayList<>();
        mCategoryCode = mResource.getStringArray(R.array.cirlce_category_list_code);
        mCircleRecyclerAdapyter = new CircleRecyclerAdapter(mContext, new ArrayList<>());
        setCategoryColor(0, CLIICKED);
        mCircleListRecyclerView.setHasFixedSize(true);
        mCircleListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        mCircleListRecyclerView.setLayoutManager(mLayoutManager);
        mCircleListRecyclerView.setAdapter(mCircleRecyclerAdapyter);
        mCircleListRecyclerView.setNestedScrollingEnabled(false);

        setPresenter(new CirclePresenter(this, new CircleRestInteractor()));
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppbarBase.getLeftButtonId()) {
            onBackPressed();
        }
    }

    @Override
    public void setPresenter(CirclePresenter presenter) {
        this.mCirlcePresenter = presenter;
    }


    @Override
    public void showLoading() {
        mGenerateProgress = new CustomProgressDialog(mContext, "로딩 중");
        mGenerateProgress.execute();
    }

    @OnClick({R.id.circle_all_linear_layout, R.id.circle_art_linear_layout, R.id.circle_show_linear_layout, R.id.circle_sport_linear_layout,
            R.id.circle_study_linear_layout, R.id.circle_religion_linear_layout, R.id.circle_service_linear_layout, R.id.circle_etc_linear_layout
    })
    public void onClickCircleCategory(View view) {
        mGenerateProgress = new CustomProgressDialog(mContext, "로딩 중");
        mGenerateProgress.execute();
        initCategoryColor();
        switch (view.getId()) {
            case R.id.circle_all_linear_layout:
                setCategoryColor(0, CLIICKED);
                mCategorySelectedNumber = 0;
                break;
            case R.id.circle_art_linear_layout:
                setCategoryColor(1, CLIICKED);
                mCategorySelectedNumber = 1;
                break;
            case R.id.circle_show_linear_layout:
                setCategoryColor(2, CLIICKED);
                mCategorySelectedNumber = 2;
                break;
            case R.id.circle_sport_linear_layout:
                setCategoryColor(3, CLIICKED);
                mCategorySelectedNumber = 3;
                break;
            case R.id.circle_study_linear_layout:
                setCategoryColor(4, CLIICKED);
                mCategorySelectedNumber = 4;
                break;
            case R.id.circle_religion_linear_layout:
                setCategoryColor(5, CLIICKED);
                mCategorySelectedNumber = 5;
                break;
            case R.id.circle_service_linear_layout:
                setCategoryColor(6, CLIICKED);
                mCategorySelectedNumber = 6;
                break;
            case R.id.circle_etc_linear_layout:
                setCategoryColor(7, CLIICKED);
                mCategorySelectedNumber = 7;
                break;
        }
        sortByCategory(mCategoryCode[mCategorySelectedNumber]);
    }

    public void sortByCategory(String categoryCode) {
        mCirlceArrayList.clear();

        if (categoryCode.equals(mCategoryCode[0]))
            mCirlceArrayList.addAll(mCircleAllArraylist);
        else {
            for (Circle item : mCircleAllArraylist) {
                if (item.category.equals(categoryCode))
                    mCirlceArrayList.add(item);
            }
        }
        updateUserInterface();
        mGenerateProgress.cancel(true);
    }

    public void initCategoryColor() {
        for (int i = 0; i < mCircleCategoryListId.length; i++) {
            setCategoryColor(i, UN_CLICKED);
        }
    }


    public void setCategoryColor(int index, int type) {

        ImageView icon = findViewById(mCircleCategoryListId[index]);
        TextView text = findViewById(mCircleCategoryTextviewListId[index]);
        text.setTextColor(getResources().getColor((type == UN_CLICKED) ? R.color.black : R.color.squash));
        icon.setImageDrawable(getResources().getDrawable(mCircleCategoryIconListId[index][type]));
    }

    @Override
    public void hideLoading() {
        mGenerateProgress.cancel(true);
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShortToast(message);
    }

    @Override
    public void onCircleListDataReceived(ArrayList<Circle> circleArrayList) {
        mCirlceArrayList.clear();
        mCircleAllArraylist.clear();

        mCirlceArrayList.addAll(circleArrayList);
        mCircleAllArraylist.addAll(circleArrayList);
        sortByCategory(mCategoryCode[mCategorySelectedNumber]);

        updateUserInterface();


    }

    @Override
    public void goToCircleDetailActivity(int circleId, String circleName) {
        getWindow().setExitTransition(new Explode());
        getWindow().setAllowEnterTransitionOverlap(true);
        Intent intent = new Intent(this, CircleDetailActivity.class);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this,
                        Pair.create(mAppbarBase, SHARE_VIEW_NAME_APP_BAR),
                        Pair.create(mCircleItemLogoImageview, SHARE_VIEW_NAME_LOGO),
                        Pair.create(mCircleItemNameTextview, SHARE_VIEW_NAME),
                        Pair.create(mCircleItemDetailTextview, SHARE_VIEW_DETAIL)
                );
        intent.putExtra("CIRCLE_ID", circleId);
        intent.putExtra("CIRCLE_NAME", circleName);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void updateUserInterface() {
        mCircleRecyclerAdapyter = new CircleRecyclerAdapter(mContext, mCirlceArrayList);
        mCircleListRecyclerView.setAdapter(mCircleRecyclerAdapyter);


    }

    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            mViewHolder = mCircleListRecyclerView.findViewHolderForAdapterPosition(position);
            mCircleItemLogoImageview = mViewHolder.itemView.findViewById(R.id.circle_item_logo_imageview);
            mCircleItemNameTextview = mViewHolder.itemView.findViewById(R.id.circle_item_name_textview);
            mCircleItemDetailTextview = mViewHolder.itemView.findViewById(R.id.circle_item_detail_textview);
            goToCircleDetailActivity(mCirlceArrayList.get(position).id, mCirlceArrayList.get(position).name);
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });

    @Override
    public void onRefresh() {
        mCirlcePresenter.getCirlceList(1);
    }
}