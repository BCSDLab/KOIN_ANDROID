package in.koreatech.koin.service_used_market.adapters;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.perf.metrics.AddTrace;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.entity.Item;

public class MarketUsedSellRecyclerAdapter extends RecyclerView.Adapter<MarketUsedSellRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<Item> mMarketsellArrayList;
    private final RequestOptions mGlideOptions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.market_used_sell_item_imageview)
        ImageView mImageViewItem; // 물품 썸내일
        @BindView(R.id.market_used_sell_title_textview)
        TextView mTextViewTitle; // 물품 제목
        @BindView(R.id.market_used_sell_nickname_textview)
        TextView mTextViewNickname; // 판매자 닉네임
        @BindView(R.id.market_used_sell_money_textview)
        TextView mTextViewMoney;
        @BindView(R.id.market_used_sell_lookup_number_textview)
        TextView mTextViewLookupNumber;
        @BindView(R.id.market_used_sell_comment_count_textview)
        TextView mTextViewCommentCount;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



    public MarketUsedSellRecyclerAdapter(Context context, ArrayList<Item> marketsellArrayList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mMarketsellArrayList = new ArrayList<>();
        this.mMarketsellArrayList = marketsellArrayList;

        mGlideOptions = new RequestOptions()
                .fitCenter()
                .override(100, 100)
                .error(R.drawable.img_noimage)
                .placeholder(R.color.white);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_used_sell_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mMarketsellArrayList.size();
    }

    @AddTrace(name = "MarketUsedsellRecyclerAdapter_onBindViewHolder")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Item item = mMarketsellArrayList.get(position);
        StringBuilder title = new StringBuilder();

        Glide.with(mContext)
                .load(item.thumbnail)
                .apply(mGlideOptions)
                .into(holder.mImageViewItem);


        holder.mTextViewMoney.setText(changeMoneyFormat(Integer.toString(item.price)) + "원");

        holder.mTextViewNickname.setText(item.nickname);
        if (item.state == 0) {
//            title.append("(판매중)");
        } else if (item.state == 1) {
            title.append("(판매중지)");
        } else {
            title.append("(판매완료)");
        }

        title.append(item.title);
        holder.mTextViewTitle.setText(title.toString());
        if(item.comments!=null && item.comments.size()>0) {
            if(item.comments.size()<100)
                holder.mTextViewCommentCount.setText("("+Integer.toString(item.comments.size())+")");
            else
                holder.mTextViewCommentCount.setText("99+");
        }

        holder.mTextViewLookupNumber.setText(item.hit);
    }

    public String changeMoneyFormat(String money) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String changedStirng = "";

        if (!TextUtils.isEmpty(money) && !money.equals(changedStirng)) {
            changedStirng = decimalFormat.format(Double.parseDouble(money.replaceAll(",", "")));
        }
        return changedStirng;
    }
}



