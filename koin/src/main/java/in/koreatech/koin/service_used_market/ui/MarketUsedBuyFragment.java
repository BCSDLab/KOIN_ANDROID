package in.koreatech.koin.service_used_market.ui;

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
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.service_used_market.contracts.MarketUsedContract;
import in.koreatech.koin.core.helpers.RecyclerClickListener;
import in.koreatech.koin.core.helpers.RecyclerViewClickListener;
import in.koreatech.koin.core.helpers.swipeRefreshBottom.SwipeRefreshLayoutBottom;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.interactors.MarketUsedRestInteractor;
import in.koreatech.koin.core.networks.responses.MarketPageResponse;
import in.koreatech.koin.service_used_market.presenters.MarketUsedPresenter;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_used_market.adapters.MarketUsedBuyRecyclerAdapter;

import java.util.ArrayList;

/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class MarketUsedBuyFragment extends MarketUsedBaseFragment implements MarketUsedContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    private final String TAG = MarketUsedBuyFragment.class.getSimpleName();


    private Unbinder mUnbinder;
    private MarketUsedContract.Presenter mMarketUsedPresenter;
    private ArrayList<Item> mMarketBuyArrayList = new ArrayList<>(); //DB의 중고장터 리스트를 저장할 ArrayList

    /* Adapter */
    private MarketUsedBuyRecyclerAdapter mMarketUsedBuyRecyclerAdapter;

    /* View Component */
    private View mView;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager

    private final int BUYMARKETID = 1;
    private int mTotalItemCount;
    private int mPosition;
    private boolean mGrantCheck;
    private int mCurrentPage;
    private int mTotalPage;
    private boolean mIsResume;
    private GenerateProgressTask generateProgressTask;

    @BindView(R.id.market_used_buy_recyclerview)
    RecyclerView mMarketBuyRecyclerView;
    @BindView(R.id.market_used_buy_swiperefreshlayout)
    SwipeRefreshLayoutBottom mMarketBuySwipeRefreshLayout;

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
        mView = inflater.inflate(R.layout.market_used_fragment_buy, container, false);
        mUnbinder = ButterKnife.bind(this, mView);

        init();

        return mView;
    }

    @Override
    public void showLoading() {
        if (generateProgressTask == null) {
            generateProgressTask = new GenerateProgressTask(getContext(), "로딩 중");
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
        mTotalPage = marketPageResponses.totalPage;
        if (mCurrentPage == 1)
            mMarketBuyArrayList.clear();
        mMarketBuyArrayList.addAll(marketPageResponses.marketArrayList);

        if (mMarketBuyArrayList == null)
            return;
        for (int i = 0; i < mMarketBuyArrayList.size(); i++) {
            int id = mMarketBuyArrayList.get(i).id;
            mMarketUsedPresenter.readDetailMarket(id);
        }
    }

    @Override
    public void setPresenter(MarketUsedContract.Presenter presenter) {
        this.mMarketUsedPresenter = presenter;
    }

    @Override
    public void onMarketDataReceived(Item item) {
        for (int i = 0; i < mMarketBuyArrayList.size(); i++) {
            int id = mMarketBuyArrayList.get(i).id;
            if (id == item.id)
                mMarketBuyArrayList.get(i).comments = item.comments;
        }
        updateUserInterface();
    }

    public void updateUserInterface() {

        mMarketUsedBuyRecyclerAdapter.notifyDataSetChanged();
    }

    //recyclerview item touch listener
    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), mMarketBuyRecyclerView, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            mMarketUsedPresenter.readGrantedDetail(mMarketBuyArrayList.get(position).id);
            mPosition = position;
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

    @Override
    public void onRefresh() {
        // 새로고침 코드

        if ((mCurrentPage != mTotalPage) && !mIsResume) {
            mCurrentPage++;
            mMarketUsedPresenter.readMarket(BUYMARKETID, mCurrentPage);
        } else if (!mIsResume)
            ToastUtil.makeLongToast(getActivity(), R.string.market_used_list_last_page);
        else
            mMarketUsedPresenter.readMarket(BUYMARKETID, mCurrentPage);

        mIsResume = false;
        updateUserInterface();
        mMarketBuySwipeRefreshLayout.setRefreshing(false);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showMarketDataReceivedFail() {
        ToastUtil.makeShortToast(getActivity(), R.string.market_used_get_list_fail);
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
        mCurrentPage = 1;
        mIsResume = false;
        mMarketUsedBuyRecyclerAdapter = new MarketUsedBuyRecyclerAdapter(getActivity(), mMarketBuyArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mMarketBuySwipeRefreshLayout.setOnRefreshListener(this);
        mMarketBuyRecyclerView.setHasFixedSize(true);
        mMarketBuyRecyclerView.setLayoutManager(mLayoutManager); //layout 설정
        mMarketBuyRecyclerView.addOnItemTouchListener(recyclerItemtouchListener); //itemTouchListner 설정
        mMarketBuyRecyclerView.setAdapter(mMarketUsedBuyRecyclerAdapter); //adapter 설정
        setPresenter(new MarketUsedPresenter(this, new MarketUsedRestInteractor()));
        mMarketUsedPresenter.readMarket(BUYMARKETID, mCurrentPage);

    }

    @Override
    public void onGrantedDataReceived(boolean granted) {
        mGrantCheck = granted;
        Intent intent = new Intent(getActivity(), MarketUsedBuyDetailActivity.class);
        intent.putExtra("ITEM_ID", mMarketBuyArrayList.get(mPosition).id);
        intent.putExtra("MARKET_ID", BUYMARKETID);
        intent.putExtra("GRANT_CHECK", mGrantCheck);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsResume = true;
        mCurrentPage = 1;
        onRefresh();
    }

}