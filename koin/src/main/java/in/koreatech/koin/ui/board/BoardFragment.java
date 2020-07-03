package in.koreatech.koin.ui.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.swiperefreshbottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.data.network.response.ArticlePageResponse;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.board.adpater.BoardRecyclerAdapter;
import in.koreatech.koin.ui.board.presenter.BoardContract;
import in.koreatech.koin.ui.board.presenter.BoardPresenter;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.util.AuthorizeManager;
import in.koreatech.koin.util.NavigationManger;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class BoardFragment extends KoinBaseFragment implements BoardContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    public static final String TAG = "BoardFragment";
    public final String RES_CODE = "RES_CODE";
    private final String REQ_CODE_ARTICLE_EDIT = "REQ_CODE_ARTICLE_EDIT";
    private final String REQ_CODE_ARTICLE = "REQ_CODE_ARTICLE";
    private final String RES_CODE_ARTICLE_DELETED = "RES_CODE_ARTICLE_DELETED";
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appbarBase;
    @BindView(R.id.board_swiperefreshlayout)
    SwipeRefreshLayoutBottom boardSwipeRefreshLayout;
    @BindView(R.id.freeboard_recyclerview)
    RecyclerView boardListRecyclerView;
    @BindView(R.id.empty_board_list_frameLayout)
    FrameLayout mEmptyBoardListFrameLayout;
    private Context context;
    private int boardUid;
    private BoardRecyclerAdapter boardRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private ArrayList<Article> articleArrayList;
    private int pageNum;   //다음 호출할 페이지 인덱스
    private BoardPresenter boardPresenter;
    private final RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(null, null, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            if ((boardUid != ID_ANONYMOUS) && (UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.MEMBER))
                boardPresenter.getArticleGrant(articleArrayList.get(position).getArticleUid());
            else
                goToArticleActivity(articleArrayList.get(position).getArticleUid(), boardUid == ID_ANONYMOUS);
        }

        @Override
        public void onLongClick(View view, int position) {
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.pageNum = 1;
        if (boardPresenter != null)
            boardPresenter.getArticlePage(boardUid, this.pageNum);
        else {
            setPresenter(new BoardPresenter(this, new CommunityRestInteractor()));
            boardPresenter.getArticlePage(boardUid, this.pageNum);
        }
    }

    private void init() {
        boardUid = getArguments().getInt("BOARD_UID", ID_FREE);
        appbarBase.setTitleText((boardUid == ID_FREE) ? "자유게시판" : (boardUid == ID_RECRUIT) ? "취업게시판" : "익명게시판");
        boardSwipeRefreshLayout.setOnRefreshListener(this);

        layoutManager = new LinearLayoutManager(getContext());
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
        if (boardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = AuthorizeManager.getAuthorize(getContext());
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                AuthorizeManager.showLoginRequestDialog(getActivity());
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserNickName() == null) {
                AuthorizeManager.showNickNameRequestDialog(getActivity());
                return;
            }
        }
        Bundle bundle = new Bundle();
        getParentFragmentManager().setFragmentResultListener(REQ_CODE_ARTICLE_EDIT, this, this::onFragmentResult);
        bundle.putInt("BOARD_UID", boardUid);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_article_edit_action, bundle, NavigationManger.getNavigationAnimation());

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

    @Override
    public void onArticleGranDataReceived(Article article) {
        goToArticleActivity(article.getArticleUid(), article.isGrantEdit());
    }

    @Override
    public void goToArticleActivity(int articleUid, boolean isArticleGrantEdit) {
        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", boardUid);
        bundle.putInt("ARTICLE_UID", articleUid);
        bundle.putBoolean("ARTICLE_GRANT_EDIT", isArticleGrantEdit);
        getParentFragmentManager().setFragmentResultListener(REQ_CODE_ARTICLE, this, this::onFragmentResult);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_article_action, bundle, NavigationManger.getNavigationAnimation());

    }

    public void onFragmentResult(String key, Bundle bundle) {
        String resultCode = bundle.getString(RES_CODE);
        switch (key) {
            case REQ_CODE_ARTICLE_EDIT:
                onClickRefreshBoardList();
                break;
            case REQ_CODE_ARTICLE:
                if (RES_CODE_ARTICLE_DELETED.equals(resultCode)) {
                    onClickRefreshBoardList();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading() {
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        if (boardSwipeRefreshLayout.isRefreshing()) {
            boardSwipeRefreshLayout.setRefreshing(false);
        }
        ToastUtil.getInstance().makeShort(message);
    }
}
