package in.koreatech.koin.ui.board;

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
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.board.presenter.BoardContract;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.swiperefreshbottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.response.ArticlePageResponse;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.ui.board.presenter.BoardPresenter;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.board.adpater.BoardRecyclerAdapter;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class BoardActivity extends KoinNavigationDrawerActivity implements BoardContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    private final String TAG = BoardActivity.class.getSimpleName();
    private final int REQ_CODE_ARTICLE_EDIT = 1;
    private final int REQ_CODE_ARTICLE = 2;
    private final int RES_CODE_ARTICLE_DELETED = 1;

    private Context context;
    private int boardUid;

    private BoardRecyclerAdapter boardRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private ArrayList<Article> articleArrayList;
    private int pageNum;   //다음 호출할 페이지 인덱스

    private BoardPresenter boardPresenter;

    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appbarBase;
    @BindView(R.id.freeboard_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.board_swiperefreshlayout)
    SwipeRefreshLayoutBottom boardSwipeRefreshLayout;
    @BindView(R.id.freeboard_recyclerview)
    RecyclerView boardListRecyclerView;

    @BindView(R.id.empty_board_list_frameLayout)
    FrameLayout mEmptyBoardListFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        this.context = this;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.pageNum = 1;
        if (boardPresenter != null)
            boardPresenter.getArticlePage(boardUid, this.pageNum);
        else {
            setPresenter(new BoardPresenter(this, new CommunityRestInteractor()));
            boardPresenter.getArticlePage(boardUid, this.pageNum);
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
        boardUid = getIntent().getIntExtra("BOARD_UID", ID_FREE);
        appbarBase.setTitleText((boardUid == ID_FREE) ? "자유게시판" : (boardUid == ID_RECRUIT) ? "취업게시판" : "익명게시판");
        boardSwipeRefreshLayout.setOnRefreshListener(this);

        layoutManager = new LinearLayoutManager(this);
        articleArrayList = new ArrayList<>();

        boardRecyclerAdapter = new BoardRecyclerAdapter(articleArrayList);

        boardListRecyclerView.setHasFixedSize(true);
        boardListRecyclerView.setLayoutManager(layoutManager);
        boardListRecyclerView.setAdapter(boardRecyclerAdapter);
        boardListRecyclerView.addOnItemTouchListener(recyclerItemtouchListener);
        setPresenter(new BoardPresenter(this, new CommunityRestInteractor()));

        this.pageNum = 1;
    }

    @Override
    public void setPresenter(BoardPresenter presenter) {
        this.boardPresenter = presenter;
    }

    //리스트 추가 로드 기능 콜백
    @Override
    public void onRefresh() {
        boardPresenter.getArticlePage(boardUid, this.pageNum);
    }


    public void onClickRefreshBoardList() {
        boardListRecyclerView.stopScroll();
        layoutManager.scrollToPosition(0);
        this.pageNum = 1;
        //첫 페이지 다시 로드
        boardPresenter.getArticlePage(boardUid, this.pageNum);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickCreateButton();
    }

    public void onClickCreateButton() {
        //TODO:닉네임설정여부
        if (boardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = getAuthorize();
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        Intent intent = new Intent(context, ArticleEditActivity.class);
        intent.putExtra("BOARD_UID", boardUid);
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
        if (this.pageNum == 1) {   //refresh 버튼 시 초기화 하면 에러 발생 (recycler view bug)
            articleArrayList.clear();
        }

        updateUserInterface(articlePage.articleArrayList);

        if (boardSwipeRefreshLayout.isRefreshing()) {
            boardSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateUserInterface(ArrayList<Article> articleArrayList) {
        this.pageNum++;

        this.articleArrayList.addAll(articleArrayList);

        boardRecyclerAdapter.notifyDataSetChanged();

        if (articleArrayList.size() != 0) {
            mEmptyBoardListFrameLayout.setVisibility(View.GONE);
        } else {
            mEmptyBoardListFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {

            if ((boardUid != ID_ANONYMOUS) && (UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.MEMBER))
                boardPresenter.getArticleGrant(articleArrayList.get(position).articleUid);
            else
                goToArticleActivity(articleArrayList.get(position).articleUid, boardUid == ID_ANONYMOUS);

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
        intent.putExtra("BOARD_UID", boardUid);
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
       showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
       hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        if (boardSwipeRefreshLayout.isRefreshing()) {
            boardSwipeRefreshLayout.setRefreshing(false);
        }

        ToastUtil.getInstance().makeShort(message);
    }
}
