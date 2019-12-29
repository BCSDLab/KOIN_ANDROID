package in.koreatech.koin.service_board.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.service_board.contracts.BoardContract;
import in.koreatech.koin.core.helpers.RecyclerClickListener;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.core.helpers.swipeRefreshBottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.responses.ArticlePageResponse;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
import in.koreatech.koin.service_board.presenters.BoardPresenter;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_board.adpaters.BoardRecyclerAdapter;

import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_RECRUIT;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class BoardActivity extends KoinNavigationDrawerActivity implements BoardContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    private final String TAG = BoardActivity.class.getSimpleName();
    private final int REQ_CODE_ARTICLE_EDIT = 1;
    private final int REQ_CODE_ARTICLE = 2;
    private final int RES_CODE_ARTICLE_DELETED = 1;

    private CustomProgressDialog customProgressDialog;
    private Context mContext;
    private int mBoardUid;
    private String mUserNickname;

    private BoardRecyclerAdapter mBoardRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager
    private ArrayList<Article> mArticleArrayList;
    private int mPageNum;   //다음 호출할 페이지 인덱스

    private BoardPresenter mBoardPresenter;

    @BindView(R.id.koin_base_app_bar_dark)
    KoinBaseAppbarDark mKoinBaseAppbarDark;
    @BindView(R.id.freeboard_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.board_swiperefreshlayout)
    SwipeRefreshLayoutBottom mBoardSwipeRefreshLayout;
    @BindView(R.id.freeboard_recyclerview)
    RecyclerView mBoardListRecyclerView;

    @BindView(R.id.empty_board_list_frameLayout)
    FrameLayout mEmptyBoardListFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        this.mContext = this;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPageNum = 1;
        if (mBoardPresenter != null)
            mBoardPresenter.getArticlePage(mBoardUid, mPageNum);
        else {
            setPresenter(new BoardPresenter(this, new CommunityRestInteractor()));
            mBoardPresenter.getArticlePage(mBoardUid, mPageNum);
        }
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
    protected void onStop() {
        super.onStop();
    }

    private void init() {
        mBoardUid = getIntent().getIntExtra("BOARD_UID", ID_FREE);
        mKoinBaseAppbarDark.setTitleText((mBoardUid == ID_FREE) ? "자유게시판" : (mBoardUid == ID_RECRUIT) ? "취업게시판" : "익명게시판");
        mBoardSwipeRefreshLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(this);
        mArticleArrayList = new ArrayList<>();

        mBoardRecyclerAdapter = new BoardRecyclerAdapter(mArticleArrayList);

        mBoardListRecyclerView.setHasFixedSize(true);
        mBoardListRecyclerView.setLayoutManager(mLayoutManager);
        mBoardListRecyclerView.setAdapter(mBoardRecyclerAdapter);
        mBoardListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        setPresenter(new BoardPresenter(this, new CommunityRestInteractor()));

        mPageNum = 1;
    }

    @Override
    public void setPresenter(BoardPresenter presenter) {
        this.mBoardPresenter = presenter;
    }

    //리스트 추가 로드 기능 콜백
    @Override
    public void onRefresh() {
        mBoardPresenter.getArticlePage(mBoardUid, mPageNum);
    }


    public void onClickRefreshBoardList() {
        mBoardListRecyclerView.stopScroll();
        mLayoutManager.scrollToPosition(0);
        mPageNum = 1;
        //첫 페이지 다시 로드
        mBoardPresenter.getArticlePage(mBoardUid, mPageNum);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId())
            onClickCreateButton();
    }

    public void onClickCreateButton() {
        //TODO:닉네임설정여부
        if (mBoardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = getAuthorize();
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        Intent intent = new Intent(mContext, ArticleEditActivity.class);
        intent.putExtra("BOARD_UID", mBoardUid);
        startActivityForResult(intent, REQ_CODE_ARTICLE_EDIT);

    }

    public AuthorizeConstant getAuthorize() {
        AuthorizeConstant authorizeConstant;
        try {
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        }
        return authorizeConstant;
    }

    @Override
    public void onBoardListDataReceived(ArticlePageResponse articlePage) {
        if (mPageNum == 1) {   //refresh 버튼 시 초기화 하면 에러 발생 (recycler view bug)
            mArticleArrayList.clear();
        }

        updateUserInterface(articlePage.articleArrayList);

        if (mBoardSwipeRefreshLayout.isRefreshing()) {
            mBoardSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateUserInterface(ArrayList<Article> articleArrayList) {
        mPageNum++;

        mArticleArrayList.addAll(articleArrayList);

        mBoardRecyclerAdapter.notifyDataSetChanged();

        if (mArticleArrayList.size() != 0) {
            mEmptyBoardListFrameLayout.setVisibility(View.GONE);
        } else {
            mEmptyBoardListFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {

            if ((mBoardUid != ID_ANONYMOUS) && (UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.MEMBER))
                mBoardPresenter.getArticleGrant(mArticleArrayList.get(position).articleUid);
            else
                goToArticleActivity(mArticleArrayList.get(position).articleUid, mBoardUid == ID_ANONYMOUS);

        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });

    @Override
    public void onArticleGranDataReceived(Article article) {
        goToArticleActivity(article.articleUid, article.isGrantEdit);
    }

    @Override
    public void goToArticleActivity(int articleUid, boolean isArticleGrantEdit) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("BOARD_UID", mBoardUid);
        intent.putExtra("ARTICLE_UID", articleUid);
        intent.putExtra("ARTICLE_GRANT_EDIT", isArticleGrantEdit);
        startActivityForResult(intent, REQ_CODE_ARTICLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_ARTICLE_EDIT:
                onClickRefreshBoardList();
                break;
            case REQ_CODE_ARTICLE:
                if (RES_CODE_ARTICLE_DELETED == resultCode) {
                    onClickRefreshBoardList();
                }
                break;
            default:
                break;
        }
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

    @Override
    public void showMessage(String message) {
        if (mBoardSwipeRefreshLayout.isRefreshing()) {
            mBoardSwipeRefreshLayout.setRefreshing(false);
        }

        ToastUtil.getInstance().makeShortToast(message);
    }
}
