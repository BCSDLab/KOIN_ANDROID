package in.koreatech.koin.service_search.ui;

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
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.helpers.RecyclerClickListener;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.service_search.adapters.SearchRecentAdapter;
import in.koreatech.koin.service_search.contracts.SearchRecentContract;
import in.koreatech.koin.service_search.presenters.SearchRecentPresenter;


public class SearchRecentFragment extends Fragment implements SearchRecentContract.View {

    @BindView(R.id.search_recent_recyclerview)
    RecyclerView searchRecentRecyclerview;

    private SearchRecentAdapter searchRecentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> recentSearchData;
    private SearchRecentContract.Presenter searchRecentPresenter;
    private GenerateProgressTask generateProgress;

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
        new SearchRecentPresenter(this, getActivity().getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        searchRecentAdapter = new SearchRecentAdapter(recentSearchData);
        searchRecentRecyclerview.setLayoutManager(linearLayoutManager);
        searchRecentRecyclerview.setAdapter(searchRecentAdapter);
        searchRecentRecyclerview.addOnItemTouchListener(recyclerItemtouchListener);
        searchRecentPresenter.getRecentData();
    }

    @Override
    public void showLoading() {
        if (generateProgress == null) {
            generateProgress = new GenerateProgressTask(getContext(), "로딩 중");
            generateProgress.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (generateProgress != null) {
            generateProgress.cancel(true);
            generateProgress = null;
        }
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
