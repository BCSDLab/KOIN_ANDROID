package in.koreatech.koin.ui.usedmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.perf.metrics.AddTrace;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerClickListener;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;
import in.koreatech.koin.core.swiperefreshbottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.data.network.response.MarketPageResponse;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedBuyRecyclerAdapter;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedPresenter;
import in.koreatech.koin.util.NavigationManger;

public class MarketUsedBuyFragment extends KoinBaseFragment implements MarketUsedContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    private final String TAG = "MarketUsedBuyFragment";
    private final int BUYMARKETID = 1;
    @BindView(R.id.market_used_buy_recyclerview)
    RecyclerView marketBuyRecyclerView;
    @BindView(R.id.market_used_buy_swiperefreshlayout)
    SwipeRefreshLayoutBottom marketBuySwipeRefreshLayout;
    private Unbinder unbinder;
    private MarketUsedPresenter marketUsedPresenter;
    private ArrayList<Item> marketBuyArrayList = new ArrayList<>(); //DB의 중고장터 리스트를 저장할 ArrayList
    /* Adapter */
    private MarketUsedBuyRecyclerAdapter marketUsedBuyRecyclerAdapter;
    /* View Component */
    private View view;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private int position;
    private boolean grantCheck;
    private int currentPage;
    private int totalPage;
    private boolean isResume;
    //recyclerview item touch listener
    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), marketBuyRecyclerView, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int index) {
            marketUsedPresenter.readGrantedDetail(marketBuyArrayList.get(position).getId());
            position = index;
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.market_used_fragment_buy, container, false);
        this.unbinder = ButterKnife.bind(this, this.view);

        init();

        return this.view;
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @AddTrace(name = "MarketData_Received_Buy")
    @Override
    public void onMarketDataReceived(MarketPageResponse marketPageResponses) {
        this.totalPage = marketPageResponses.totalPage;
        if (currentPage == 1)
            marketBuyArrayList.clear();
        marketBuyArrayList.addAll(marketPageResponses.marketArrayList);

        if (marketBuyArrayList == null)
            return;
        for (int i = 0; i < marketBuyArrayList.size(); i++) {
            int id = marketBuyArrayList.get(i).getId();
            marketUsedPresenter.readDetailMarket(id);
        }
    }

    @Override
    public void setPresenter(MarketUsedPresenter presenter) {
        this.marketUsedPresenter = presenter;
    }

    @Override
    public void onMarketDataReceived(Item item) {
        for (int i = 0; i < marketBuyArrayList.size(); i++) {
            int id = marketBuyArrayList.get(i).getId();
            if (id == item.getId())
                marketBuyArrayList.get(i).setComments(item.getComments());
        }
        updateUserInterface();
    }

    public void updateUserInterface() {

        marketUsedBuyRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        // 새로고침 코드

        if ((currentPage != this.totalPage) && !this.isResume) {
            currentPage++;
            marketUsedPresenter.readMarket(BUYMARKETID, currentPage);
        } else if (!this.isResume)
            ToastUtil.getInstance().makeShort(R.string.market_used_list_last_page);
        else
            marketUsedPresenter.readMarket(BUYMARKETID, currentPage);

        this.isResume = false;
        updateUserInterface();
        marketBuySwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    @Override
    public void showMarketDataReceivedFail() {
        ToastUtil.getInstance().makeShort(R.string.market_used_get_list_fail);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void init() {
        currentPage = 1;
        this.isResume = false;
        marketUsedBuyRecyclerAdapter = new MarketUsedBuyRecyclerAdapter(getActivity(), marketBuyArrayList);
        this.layoutManager = new LinearLayoutManager(getActivity());
        marketBuySwipeRefreshLayout.setOnRefreshListener(this);
        marketBuyRecyclerView.setHasFixedSize(true);
        marketBuyRecyclerView.setLayoutManager(this.layoutManager); //layout 설정
        marketBuyRecyclerView.addOnItemTouchListener(recyclerItemtouchListener); //itemTouchListner 설정
        marketBuyRecyclerView.setAdapter(marketUsedBuyRecyclerAdapter); //adapter 설정
        setPresenter(new MarketUsedPresenter(this, new MarketUsedRestInteractor()));
        marketUsedPresenter.readMarket(BUYMARKETID, currentPage);

    }

    @Override
    public void onGrantedDataReceived(boolean granted) {
        this.grantCheck = granted;
        Bundle bundle = new Bundle();
        bundle.putInt("ITEM_ID", marketBuyArrayList.get(this.position).getId());
        bundle.putInt("MARKET_ID", BUYMARKETID);
        bundle.putBoolean("GRANT_CHECK", this.grantCheck);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_buy_detail_action, bundle, NavigationManger.getNavigationAnimation());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isResume = true;
        currentPage = 1;
        onRefresh();
    }

}