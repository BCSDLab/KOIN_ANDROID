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
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedSellRecyclerAdapter;

import java.util.ArrayList;


/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class MarketUsedSellFragment extends MarketUsedBaseFragment implements MarketUsedContract.View, SwipeRefreshLayoutBottom.OnRefreshListener {
    private final String TAG = MarketUsedSellFragment.class.getSimpleName();


    private Unbinder mUnbinder;
    private MarketUsedContract.Presenter mMarketUsedPresenter;
    private ArrayList<Item> mMarketSellArrayList = new ArrayList<>(); //DB의 중고장터 리스트를 저장할 ArrayList
    private CustomProgressDialog customProgressDialog;
    /* Adapter */
    private MarketUsedSellRecyclerAdapter mMarketUsedSellRecyclerAdapter;

    /* View Component */
    private View mView;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager

    private final int SELLMARKETID = 0;
    private int mPostion;
    private boolean mGrantCheck;
    private int mCurrentPage;
    private int mTotalPage;
    private boolean mIsResume;


    @BindView(R.id.market_used_sell_recyclerview)
    RecyclerView mMarketSellRecyclerView;
    @BindView(R.id.market_used_sell_swiperefreshlayout)
    SwipeRefreshLayoutBottom mMarketSellSwipeRefreshLayout;

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
        mView = inflater.inflate(R.layout.market_used_fragment_sell, container, false);
        mUnbinder = ButterKnife.bind(this, mView);

        init();

        return mView;
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

    @AddTrace(name = "MarketData_Received_Sell")
    @Override
    public void onMarketDataReceived(MarketPageResponse marketPageResponses) {

        mTotalPage = marketPageResponses.totalPage;
        if (mCurrentPage == 1)
            mMarketSellArrayList.clear();
        mMarketSellArrayList.addAll(marketPageResponses.marketArrayList);

        if (mMarketSellArrayList == null)
            return;
        for (int i = 0; i < mMarketSellArrayList.size(); i++) {
            int id = mMarketSellArrayList.get(i).id;
            mMarketUsedPresenter.readDetailMarket(id);
        }


    }

    @Override
    public void setPresenter(MarketUsedContract.Presenter presenter) {
        this.mMarketUsedPresenter = presenter;
    }

    @Override
    public void showLoading() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(getContext(), "로딩 중");
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

    public void updateUserInterface() {

        mMarketUsedSellRecyclerAdapter.notifyDataSetChanged();
    }

    //recyclerview item touch listener
    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), mMarketSellRecyclerView, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            mMarketUsedPresenter.readGrantedDetail(mMarketSellArrayList.get(position).id);
            mPostion = position;

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
            mMarketUsedPresenter.readMarket(SELLMARKETID, mCurrentPage);
        } else if (!mIsResume)
            ToastUtil.getInstance().makeShortToast(R.string.market_used_list_last_page);
        else
            mMarketUsedPresenter.readMarket(SELLMARKETID, mCurrentPage);

        mIsResume = false;
        updateUserInterface();
        mMarketSellSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void showMarketDataReceivedFail() {
        ToastUtil.getInstance().makeShortToast(R.string.market_used_get_list_fail);
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
        mMarketUsedSellRecyclerAdapter = new MarketUsedSellRecyclerAdapter(getActivity(), mMarketSellArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mMarketSellSwipeRefreshLayout.setOnRefreshListener(this);
        mMarketSellRecyclerView.setHasFixedSize(true);
        mMarketSellRecyclerView.setLayoutManager(mLayoutManager); //layout 설정
        mMarketSellRecyclerView.addOnItemTouchListener(recyclerItemtouchListener); //itemTouchListner 설정
        mMarketSellRecyclerView.setAdapter(mMarketUsedSellRecyclerAdapter); //adapter 설정
        setPresenter(new MarketUsedPresenter(this, new MarketUsedRestInteractor()));

        mMarketUsedPresenter.readMarket(SELLMARKETID, mCurrentPage);


    }

    @Override
    public void onGrantedDataReceived(boolean granted) {
        mGrantCheck = granted;
        Intent intent = new Intent(getActivity(), MarketUsedSellDetailActivity.class);
        intent.putExtra("ITEM_ID", mMarketSellArrayList.get(mPostion).id);
        intent.putExtra("MARKET_ID", SELLMARKETID);
        intent.putExtra("GRANT_CHECK", mGrantCheck);
        startActivity(intent);
    }

    @Override
    public void onMarketDataReceived(Item item) {
        for (int i = 0; i < mMarketSellArrayList.size(); i++) {
            int id = mMarketSellArrayList.get(i).id;
            if (id == item.id)
                mMarketSellArrayList.get(i).comments = item.comments;
        }
        updateUserInterface();
    }


    @Override
    public void onResume() {
        super.onResume();
        mIsResume = true;
        mCurrentPage = 1;
        onRefresh();
    }
}
