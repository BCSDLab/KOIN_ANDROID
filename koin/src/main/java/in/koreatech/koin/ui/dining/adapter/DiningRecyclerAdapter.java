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

/**
 * Created by hyerim on 2018. 6. 21....
 * Edited by yunjae on 2018. 8. 26.... checkDiningString  null일경우 0으로 change
 */
public class DiningRecyclerAdapter extends RecyclerView.Adapter<DiningRecyclerAdapter.ViewHolder> {
    private final String TAG = DiningRecyclerAdapter.class.getSimpleName();

    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<Dining> mDiningArrayList; //식단 정보 담긴 식당 List
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dining_item_title)
        TextView mTextViewTitle; // 식당이름(한식, 양식, 2캠퍼스 등)
        @BindView(R.id.dining_item_info)
        TextView mTextViewInfo; // 정보(칼로리)
        @BindView(R.id.dining_item_menu)
        TextView mTextViewMenu; // 메뉴리스트
        @BindView(R.id.dining_item_price)
        TextView mTextViewPrice; // 정보(카드가격, 현금가격)
        @BindView(R.id.dining_divider)
        LinearLayout mDiningDivider;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DiningRecyclerAdapter(Context context, ArrayList<Dining> diningArrayList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDiningArrayList = new ArrayList<>();
        this.mDiningArrayList.addAll(diningArrayList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dining_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dining dining = mDiningArrayList.get(position);
        holder.mTextViewTitle.setText(dining.place);        // 식당이름(한식, 양식, 2캠퍼스 등)

        StringBuilder sb = new StringBuilder();
        dining = checkDiningString(dining);
        sb.append(dining.kcal).append("kcal");
        holder.mTextViewInfo.setText(sb.toString());        //식단 정보 (칼로리 정보)

        sb = new StringBuilder();
        sb.append("캐시비 ")
                .append(dining.priceCard).append("원 / 현금 ")
                .append(dining.priceCash).append("원");
        holder.mTextViewPrice.setText(sb.toString());       //가격 정보

        sb = new StringBuilder();
        for (String menuItem : dining.menu) {
            sb.append(menuItem).append("\n");
        }

        if(!dining.place.equals("능수관"))
            holder.mDiningDivider.setBackgroundColor(mContext.getResources().getColor(R.color.light_navy));
        else
            holder.mDiningDivider.setBackgroundColor(mContext.getResources().getColor(R.color.squash));

        holder.mTextViewMenu.setText(sb.toString());        //메뉴리스트
    }

    @Override
    public int getItemCount() {
        return mDiningArrayList.size();
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

