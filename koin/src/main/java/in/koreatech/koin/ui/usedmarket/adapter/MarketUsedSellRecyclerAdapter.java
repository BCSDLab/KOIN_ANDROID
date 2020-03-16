package in.koreatech.koin.ui.usedmarket.adapter;


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
import in.koreatech.koin.data.network.entity.Item;

public class MarketUsedSellRecyclerAdapter extends RecyclerView.Adapter<MarketUsedSellRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater; //inflate 사용위한 inflater
    private ArrayList<Item> marketsellArrayList;
    private final RequestOptions glideOptions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.market_used_sell_item_imageview)
        ImageView imageViewItem; // 물품 썸내일
        @BindView(R.id.market_used_sell_title_textview)
        TextView textViewTitle; // 물품 제목
        @BindView(R.id.market_used_sell_nickname_textview)
        TextView textViewNickname; // 판매자 닉네임
        @BindView(R.id.market_used_sell_money_textview)
        TextView textViewMoney;
        @BindView(R.id.market_used_sell_lookup_number_textview)
        TextView textViewLookupNumber;
        @BindView(R.id.market_used_sell_comment_count_textview)
        TextView textViewCommentCount;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public MarketUsedSellRecyclerAdapter(Context context, ArrayList<Item> marketsellArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.marketsellArrayList = new ArrayList<>();
        this.marketsellArrayList = marketsellArrayList;

        glideOptions = new RequestOptions()
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
        return marketsellArrayList.size();
    }

    @AddTrace(name = "MarketUsedsellRecyclerAdapter_onBindViewHolder")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Item item = marketsellArrayList.get(position);
        StringBuilder title = new StringBuilder();

        Glide.with(context)
                .load(item.getThumbnail())
                .apply(glideOptions)
                .into(holder.imageViewItem);


        holder.textViewMoney.setText(changeMoneyFormat(Integer.toString(item.getPrice())) + "원");

        holder.textViewNickname.setText(item.getNickname());
        if (item.getState() == 0) {
//            title.append("(판매중)");
        } else if (item.getState() == 1) {
            title.append("(판매중지)");
        } else {
            title.append("(판매완료)");
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



