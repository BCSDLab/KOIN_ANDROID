package in.koreatech.koin.ui.search;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.swiperefreshbottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleRestInteractor;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.search.adapter.SearchResultAdapter;
import in.koreatech.koin.ui.search.presenter.SearchResultContract;
import in.koreatech.koin.ui.search.presenter.SearchResultPresenter;

public class SearchResultFragment extends KoinBaseFragment implements SwipeRefreshLayoutBottom.OnRefreshListener, SearchResultContract.View {
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
    private SearchResultPresenter searchResultPresenter;
    private LinearLayoutManager linearLayoutManager;
    private SearchResultAdapter searchResultAdapter;
    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), searchRecyclerview, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            ((SearchFragment) getParentFragment()).startFragmentByTableId(searchedArticles.get(position));
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

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
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
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
    public void setPresenter(SearchResultPresenter presenter) {
        this.searchResultPresenter = presenter;
    }
}
