package in.koreatech.koin.ui.usedmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.google.firebase.perf.metrics.AddTrace;

import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedContract;
import in.koreatech.koin.core.helper.RecyclerClickListener;
import in.koreatech.koin.core.helper.RecyclerViewClickListener;
import in.koreatech.koin.core.helper.swipeRefreshBottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.data.network.response.MarketPageResponse;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedPresenter;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedBuyRecyclerAdapter;

import java.util.ArrayList;

/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class MarketUsedBuyFragment extends MarketUsedBaseFragment implements MarketUsedContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    private final String TAG = "MarketUsedBuyFragment";


    private Unbinder unbinder;
    private MarketUsedContract.Presenter marketUsedPresenter;
    private ArrayList<Item> marketBuyArrayList = new ArrayList<>(); //DB의 중고장터 리스트를 저장할 ArrayList

    /* Adapter */
    private MarketUsedBuyRecyclerAdapter marketUsedBuyRecyclerAdapter;

    /* View Component */
    private View view;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager

    private final int BUYMARKETID = 1;
    private int position;
    private boolean grantCheck;
    private int currentPage;
    private int totalPage;
    private boolean isResume;

    @BindView(R.id.market_used_buy_recyclerview)
    RecyclerView marketBuyRecyclerView;
    @BindView(R.id.market_used_buy_swiperefreshlayout)
    SwipeRefreshLayoutBottom marketBuySwipeRefreshLayout;

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
        ((MarketUsedActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MarketUsedActivity) getActivity()).hideProgressDialog();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            int id = marketBuyArrayList.get(i).id;
            marketUsedPresenter.readDetailMarket(id);
        }
    }

    @Override
    public void setPresenter(MarketUsedContract.Presenter presenter) {
        this.marketUsedPresenter = presenter;
    }

    @Override
    public void onMarketDataReceived(Item item) {
        for (int i = 0; i < marketBuyArrayList.size(); i++) {
            int id = marketBuyArrayList.get(i).id;
            if (id == item.id)
                marketBuyArrayList.get(i).comments = item.comments;
        }
        updateUserInterface();
    }

    public void updateUserInterface() {

        marketUsedBuyRecyclerAdapter.notifyDataSetChanged();
    }

    //recyclerview item touch listener
    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), marketBuyRecyclerView, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int index) {
            marketUsedPresenter.readGrantedDetail(marketBuyArrayList.get(position).id);
            position = index;
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

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
        Intent intent = new Intent(getActivity(), MarketUsedBuyDetailActivity.class);
        intent.putExtra("ITEM_ID", marketBuyArrayList.get(this.position).id);
        intent.putExtra("MARKET_ID", BUYMARKETID);
        intent.putExtra("GRANT_CHECK", this.grantCheck);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isResume = true;
        currentPage = 1;
        onRefresh();
    }

}