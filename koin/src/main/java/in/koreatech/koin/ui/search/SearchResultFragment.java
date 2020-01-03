package in.koreatech.koin.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.WebViewActivity;

import in.koreatech.koin.core.helper.RecyclerClickListener;
import in.koreatech.koin.core.helper.RecyclerViewClickListener;
import in.koreatech.koin.core.helper.swipeRefreshBottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleRestInteractor;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.board.ArticleActivity;
import in.koreatech.koin.ui.lostfound.LostFoundDetailActivity;
import in.koreatech.koin.ui.search.adapter.SearchResultAdapter;
import in.koreatech.koin.ui.search.presenter.SearchResultContract;
import in.koreatech.koin.ui.search.presenter.SearchResultPresenter;
import in.koreatech.koin.ui.usedmarket.MarketUsedBuyDetailActivity;
import in.koreatech.koin.ui.usedmarket.MarketUsedSellDetailActivity;

import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class SearchResultFragment extends Fragment implements SwipeRefreshLayoutBottom.OnRefreshListener, SearchResultContract.View {
    @BindView(R.id.search_recyclerview)
    RecyclerView searchRecyclerview;
    @BindView(R.id.search_swipe_refresh_layout)
    SwipeRefreshLayoutBottom swipeRefreshLayoutBottom;
    @BindView(R.id.search_result_textview)
    TextView searchResultTextview;

    private int currentPage;
    private int totalPage;
    private String currentText;
    private SearchedArticle searchedArticle;
    private ArrayList<SearchedArticle> searchedArticles;
    private SearchResultContract.Presenter searchResultPresenter;
    private LinearLayoutManager linearLayoutManager;
    private SearchResultAdapter searchResultAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        currentPage = 1;
        searchedArticles = new ArrayList<>();
        new SearchResultPresenter(this, new SearchArticleRestInteractor());
        swipeRefreshLayoutBottom.setOnRefreshListener(this);
        if (getArguments() != null) {
            searchedArticle = (SearchedArticle) getArguments().getSerializable("SEARCH_ITEMS");
            if (searchedArticle != null && searchedArticle.getSearchedArticles() != null) {
                totalPage = searchedArticle.getTotalPage();
                currentText = getArguments().getString("CURRENT_TEXT");
                searchedArticles.addAll(searchedArticle.getSearchedArticles());
                searchResultTextview.setText(Html.fromHtml("<font><b>'" + currentText + "'</b></font> 에 대한 결과입니다."));
            }
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        searchResultAdapter = new SearchResultAdapter(searchedArticles, currentText);
        searchRecyclerview.setLayoutManager(linearLayoutManager);
        searchRecyclerview.setAdapter(searchResultAdapter);
        searchRecyclerview.addOnItemTouchListener(recyclerItemtouchListener);
    }

    @Override
    public void onRefresh() {
        if (totalPage >= currentPage + 1) {
            searchResultPresenter.getArticleSearched(currentText, currentPage + 1);
        } else
            ToastUtil.getInstance().makeShort("마지막 검색 결과입니다.");
        swipeRefreshLayoutBottom.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        ((SearchActivity)getActivity()).showProgressDialog("로딩 중");
    }

    @Override
    public void hideLoading() {
        ((SearchActivity)getActivity()).hideLoading();
    }

    @Override
    public void showSearchedArticle(SearchedArticle searchedArticle) {
        if (searchedArticle.getSearchedArticles() != null) {
            currentPage++;
            searchedArticles.addAll(searchedArticle.getSearchedArticles());
            searchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
        this.searchResultPresenter = presenter;
    }

    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), searchRecyclerview, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {

            startActivityByTableId(searchedArticles.get(position));
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

    private void startActivityByTableId(SearchedArticle searchedArticleItem) {
        if (searchedArticleItem == null) return;
        Intent intent;
        switch (searchedArticleItem.getTableId()) {
            case 5: // 자유게시판
                intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("BOARD_UID", ID_FREE);
                intent.putExtra("ARTICLE_UID", searchedArticleItem.getId());
                break;
            case 6: // 자유게시판
                intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("BOARD_UID", ID_RECRUIT);
                intent.putExtra("ARTICLE_UID", searchedArticleItem.getId());
                break;
            case 7: // 취업게시판
                intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("BOARD_UID", ID_ANONYMOUS);
                intent.putExtra("ARTICLE_UID", searchedArticleItem.getId());
                break;
            case 9: // 분실물
                intent = new Intent(getActivity(), LostFoundDetailActivity.class);
                intent.putExtra("ID", searchedArticleItem.getId());
                break;
            case 10: // 중고장터
                if (searchedArticleItem.getPermalink().contains("buy")) {
                    intent = new Intent(getActivity(), MarketUsedBuyDetailActivity.class);
                    intent.putExtra("MARKET_ID", 1);
                } else {
                    intent = new Intent(getActivity(), MarketUsedSellDetailActivity.class);
                    intent.putExtra("MARKET_ID", 0);
                }

                intent.putExtra("ITEM_ID", searchedArticleItem.getId());
                break;
            default:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", searchedArticleItem.getServiceName());
                intent.putExtra("url", searchedArticleItem.getPermalink());
                break;
        }

        getActivity().startActivity(intent);

    }

}
