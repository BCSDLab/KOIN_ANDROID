package in.koreatech.koin.ui.usedmarket.adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.perf.metrics.AddTrace;

import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Item;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class MarketUsedBuyRecyclerAdapter extends RecyclerView.Adapter<MarketUsedBuyRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater; //inflate 사용위한 inflater
    private ArrayList<Item> marketBuyArrayList;
    private final RequestOptions glideOptions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.market_used_buy_item_imageview)
        ImageView imageViewItem; // 물품 썸내일
        @BindView(R.id.market_used_buy_title_textview)
        TextView textViewTitle; // 물품 제목
        @BindView(R.id.market_used_buy_nickname_textview)
        TextView textViewNickname; // 판매자 닉네임
        @BindView(R.id.market_used_buy_money_textview)
        TextView textViewMoney;
        @BindView(R.id.market_used_buy_lookup_number_textview)
        TextView textViewLookupNumber;
        @BindView(R.id.market_used_buy_comment_count_textview)
        TextView textViewCommentCount;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public MarketUsedBuyRecyclerAdapter(Context context, ArrayList<Item> marketBuyArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.marketBuyArrayList = new ArrayList<>();
        this.marketBuyArrayList = marketBuyArrayList;

        glideOptions = new RequestOptions()
                .fitCenter()
                .override(100, 100)
                .error(R.drawable.img_noimage)
                .placeholder(R.color.white);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_used_buy_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return marketBuyArrayList.size();
    }

    @AddTrace(name = "MarketUsedBuyRecyclerAdapter_onBindViewHolder")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Item item = marketBuyArrayList.get(position);
        StringBuilder title = new StringBuilder();

        Glide.with(context)
                .load(item.getThumbnail())
                .apply(glideOptions)
                .into(holder.imageViewItem);


        holder.textViewMoney.setText(changeMoneyFormat(Integer.toString(item.getPrice())) + "원");

        holder.textViewNickname.setText(item.getNickname());
        if (item.getState() == 0) {
//            title.append("(구매중)");
        } else if (item.getState() == 1) {
            title.append("(구매중지)");
        } else {
            title.append("(구매완료)");
        }

        title.append(item.getTitle());
        holder.textViewTitle.setText(title.toString());
        if (item.getComments() != null && item.getComments().size() > 0) {
            if (item.getComments().size() < 100)
                holder.textViewCommentCount.setText("(" + Integer.toString(item.getComments().size()) + ")");
            else
                holder.textViewCommentCount.setText("99+");
        }

        holder.textViewLookupNumber.setText(item.getHit());
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



