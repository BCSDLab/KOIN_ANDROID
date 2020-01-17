package in.koreatech.koin.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.ui.search.adapter.SearchRecentAdapter;
import in.koreatech.koin.ui.search.presenter.SearchRecentContract;
import in.koreatech.koin.ui.search.presenter.SearchRecentPresenter;


public class SearchRecentFragment extends Fragment implements SearchRecentContract.View {

    @BindView(R.id.search_recent_recyclerview)
    RecyclerView searchRecentRecyclerview;

    private SearchRecentAdapter searchRecentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> recentSearchData;
    private SearchRecentContract.Presenter searchRecentPresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_recentresult_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        recentSearchData = new ArrayList<>();
        new SearchRecentPresenter(this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        searchRecentAdapter = new SearchRecentAdapter(recentSearchData);
        searchRecentRecyclerview.setLayoutManager(linearLayoutManager);
        searchRecentRecyclerview.setAdapter(searchRecentAdapter);
        searchRecentRecyclerview.addOnItemTouchListener(recyclerItemtouchListener);
        searchRecentPresenter.getRecentData();
    }

    @Override
    public void showLoading() {
        ((SearchActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((SearchActivity) getActivity()).hideLoading();
    }

    @Override
    public void showSearchedArticle(ArrayList<String> recentSearchData) {
        if (recentSearchData == null) return;
        this.recentSearchData.clear();
        this.recentSearchData.addAll(recentSearchData);
        searchRecentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void setPresenter(SearchRecentContract.Presenter presenter) {
        this.searchRecentPresenter = presenter;
    }

    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), searchRecentRecyclerview, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            ((SearchActivity) getActivity()).setSearchText(recentSearchData.get(position));
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });
}
