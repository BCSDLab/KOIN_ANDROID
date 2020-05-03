package in.koreatech.koin.ui.lostfound;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.data.network.interactor.LostAndFoundRestInteractor;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.swiperefreshbottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.response.LostAndFoundPageResponse;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundMainContract;
import in.koreatech.koin.ui.lostfound.adapter.LostFoundMainActivityRecyclerviewAdapter;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundMainPresenter;

public class LostFoundMainActivity extends KoinNavigationDrawerActivity implements SwipeRefreshLayoutBottom.OnRefreshListener, LostFoundMainContract.View {
    public static final String TAG = "LostFoundMainActivity";
    public static final int LIMITITEM = 10;
    /* View Component */
    @BindView(R.id.lostfound_main_recyclerview)
    RecyclerView lostfoundMainRecyclerView;
    @BindView(R.id.lostfound_main_swiperefreshlayout)
    SwipeRefreshLayoutBottom lostfoundMainSwipeRefreshLayout;

    private LinearLayoutManager linearLayoutManager;
    private LostFoundMainActivityRecyclerviewAdapter lostFoundMainActivityRecyclerviewAdapter;
    private ArrayList<LostItem> lostItemArrayList;
    private LostFoundMainPresenter lostFoundMainPresenter;
    private Context context;
    private int currentPage;
    private int totalPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostfound_activity_main);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 뒤로가기를 눌렀을때 홈화면으로 간다.
     *
     * @param view 클릭한 view를 받는다.
     */
    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedKoinBaseAppbar(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId())
            callDrawerItem(R.id.navi_item_home);
    }

    public void init() {
        initVariable();
        initRecyclerview();
    }

    @Override
    protected void onStart() {
        super.onStart();
        totalPage = 1;
        currentPage = 0;
        getNextPage();
    }

    public void initVariable() {
        totalPage = 1;
        currentPage = 0;
        context = this;
        new LostFoundMainPresenter(this, new LostAndFoundRestInteractor());
        lostItemArrayList = new ArrayList<>();
        lostfoundMainSwipeRefreshLayout.setOnRefreshListener(this);
        lostFoundMainActivityRecyclerviewAdapter = new LostFoundMainActivityRecyclerviewAdapter(this, lostItemArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
    }

    /**
     * recyclerview 초기화
     */
    public void initRecyclerview() {
        lostfoundMainRecyclerView.setLayoutManager(linearLayoutManager);
        lostfoundMainRecyclerView.setAdapter(lostFoundMainActivityRecyclerviewAdapter);
        lostfoundMainRecyclerView.setHasFixedSize(true);
        lostfoundMainRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
    }

    /**
     * Swipe해서 Refresh 될때 실행
     */
    @Override
    public void onRefresh() {
        getNextPage();
        lostfoundMainSwipeRefreshLayout.setRefreshing(false);
    }


    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getBaseContext(), lostfoundMainRecyclerView, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            Intent intent = new Intent(context, LostFoundDetailActivity.class);
            intent.putExtra("ID", lostItemArrayList.get(position).getId());
            startActivity(intent);
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    /**
     * 전체 페이지를 검사해서 다음 페이지 리스트를 받아옵니다.
     */
    public void getNextPage() {
        if (currentPage == 0)
            lostItemArrayList.clear();
        if (currentPage < totalPage)
            lostFoundMainPresenter.getLostItem(++currentPage, LIMITITEM);
        else
            ToastUtil.getInstance().makeShort("마지막 페이지 입니다.");
    }

    @Override
    public void showLostAndFoundPageResponse(LostAndFoundPageResponse lostAndFoundPageResponse) {
        if (lostAndFoundPageResponse == null) return;
        totalPage = lostAndFoundPageResponse.totalPage;
        updateLostFoundItem(lostAndFoundPageResponse.lostItemArrayList);
    }

    public void updateLostFoundItem(ArrayList<LostItem> lostAndFoundArrayList) {
        if (lostAndFoundArrayList != null)
            lostItemArrayList.addAll(lostAndFoundArrayList);
        lostFoundMainActivityRecyclerviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            goToMainActivity();
        } else if (id == AppBarBase.getRightButtonId()) {
            onClickCreateButton();
        }
    }

    @Override
    public void setPresenter(LostFoundMainPresenter presenter) {
        lostFoundMainPresenter = presenter;
    }

    public void onClickCreateButton() {
        AuthorizeConstant authorize = getAuthority();
        if (authorize == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserNickName() == null) {
            showNickNameRequestDialog();
            return;
        }
        Intent intent = new Intent(this, LostFoundEditActivity.class);
        intent.putExtra("MODE", LostFoundEditActivity.CREATE_MODE);
        startActivity(intent);
    }
}
