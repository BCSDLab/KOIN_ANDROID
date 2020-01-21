package in.koreatech.koin.ui.dining.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Dining;

public class DiningRecyclerAdapter extends RecyclerView.Adapter<DiningRecyclerAdapter.ViewHolder> {
    private final String TAG = "DiningRecyclerAdapter";

    private LayoutInflater layoutInflater; //inflate 사용위한 inflater
    private ArrayList<Dining> diningArrayList; //식단 정보 담긴 식당 List
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dining_item_title)
        TextView textViewTitle; // 식당이름(한식, 양식, 2캠퍼스 등)
        @BindView(R.id.dining_item_info)
        TextView textViewInfo; // 정보(칼로리)
        @BindView(R.id.dining_item_menu)
        TextView textViewMenu; // 메뉴리스트
        @BindView(R.id.dining_item_price)
        TextView textViewPrice; // 정보(카드가격, 현금가격)
        @BindView(R.id.dining_divider)
        LinearLayout diningDivider;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DiningRecyclerAdapter(Context context, ArrayList<Dining> diningArrayList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.diningArrayList = new ArrayList<>();
        this.diningArrayList.addAll(diningArrayList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dining_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dining dining = diningArrayList.get(position);
        holder.textViewTitle.setText(dining.place);        // 식당이름(한식, 양식, 2캠퍼스 등)

        StringBuilder sb = new StringBuilder();
        dining = checkDiningString(dining);
        sb.append(dining.kcal).append("kcal");
        holder.textViewInfo.setText(sb.toString());        //식단 정보 (칼로리 정보)

        sb = new StringBuilder();
        sb.append("캐시비 ")
                .append(dining.priceCard).append("원 / 현금 ")
                .append(dining.priceCash).append("원");
        holder.textViewPrice.setText(sb.toString());       //가격 정보

        sb = new StringBuilder();
        for (String menuItem : dining.menu) {
            sb.append(menuItem).append("\n");
        }

        if (!dining.place.equals("능수관"))
            holder.diningDivider.setBackgroundColor(context.getResources().getColor(R.color.light_navy));
        else
            holder.diningDivider.setBackgroundColor(context.getResources().getColor(R.color.squash));

        holder.textViewMenu.setText(sb.toString());        //메뉴리스트
    }

    @Override
    public int getItemCount() {
        return diningArrayList.size();
    }

    public Dining checkDiningString(Dining dining) {
        if (dining.kcal == null)
            dining.kcal = 0;
        if (dining.priceCard == null)
            dining.priceCard = 0;
        if (dining.priceCash == null)
            dining.priceCash = 0;

        return dining;
    }
}

