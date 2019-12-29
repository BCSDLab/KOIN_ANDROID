package in.koreatech.koin.service_lostfound.ui;

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
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.helpers.RecyclerClickListener;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.core.helpers.swipeRefreshBottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.core.networks.entity.LostItem;
import in.koreatech.koin.core.networks.responses.LostAndFoundPageResponse;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_board.ui.ArticleEditActivity;
import in.koreatech.koin.service_lostfound.Contracts.LostFoundMainContract;
import in.koreatech.koin.service_lostfound.adapter.LostFoundMainActivityRecyclerviewAdapter;
import in.koreatech.koin.service_lostfound.presenters.LostFoundMainPresenter;

import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_ANONYMOUS;

/**
 * @author yunjaeNa
 * @since 2019-09-14
 */
public class LostFoundMainActivity extends KoinNavigationDrawerActivity implements SwipeRefreshLayoutBottom.OnRefreshListener, LostFoundMainContract.View {
    public static final String TAG = LostFoundMainActivity.class.getSimpleName();
    public static final int LIMITITEM = 10;
    /* View Component */
    @BindView(R.id.lostfound_main_recyclerview)
    RecyclerView lostfoundMainRecyclerView;
    @BindView(R.id.lostfound_main_swiperefreshlayout)
    SwipeRefreshLayoutBottom lostfoundMainSwipeRefreshLayout;

    private LinearLayoutManager linearLayoutManager;
    private GenerateProgressTask generateProgressTask;
    private LostFoundMainActivityRecyclerviewAdapter lostFoundMainActivityRecyclerviewAdapter;
    private ArrayList<LostItem> lostItemArrayList;
    private LostFoundMainContract.Presenter lostFoundMainPresenter;
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
        if (id == KoinBaseAppbarDark.getLeftButtonId())
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
        new LostFoundMainPresenter(this);
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
            intent.putExtra("ID", lostItemArrayList.get(position).id);
            startActivity(intent);
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

    @Override
    public void showLoading() {
        if (generateProgressTask == null) {
            generateProgressTask = new GenerateProgressTask(this, "로딩 중");
            generateProgressTask.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (generateProgressTask != null) {
            generateProgressTask.cancel(true);
            generateProgressTask = null;
        }
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
            ToastUtil.makeShortToast(this, "마지막 페이지 입니다.");
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
        ToastUtil.makeShortToast(this, message);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId()) {
            goToMainActivity();
        } else if (id == KoinBaseAppbarDark.getRightButtonId()) {
            onClickCreateButton();
        }
    }

    @Override
    public void setPresenter(LostFoundMainContract.Presenter presenter) {
        lostFoundMainPresenter = presenter;
    }

    public void onClickCreateButton() {
        AuthorizeConstant authorize = getAuthority();
        if (authorize == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        } else if (authorize == AuthorizeConstant.MEMBER && DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
            showNickNameRequestDialog();
            return;
        }
        Intent intent = new Intent(this, LostFoundEditActivity.class);
        intent.putExtra("MODE", LostFoundEditActivity.CREATE_MODE);
        startActivity(intent);
    }
}
