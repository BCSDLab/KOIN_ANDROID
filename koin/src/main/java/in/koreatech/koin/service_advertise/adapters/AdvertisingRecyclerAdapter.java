package in.koreatech.koin.service_advertise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.service_dining.adapters.DiningRecyclerAdapter;
import in.koreatech.koin.service_land.adapter.LandRecyclerAdapter;

/**
 * Created by hansol on 2020.1.1...
 */
public class AdvertisingRecyclerAdapter extends RecyclerView.Adapter<AdvertisingRecyclerAdapter.ViewHolder> {

    private ArrayList<Advertising> adArrayList;
    private Context context;

    public AdvertisingRecyclerAdapter(ArrayList<Advertising> adArrayList, Context context) {
        this.adArrayList = adArrayList;
        this.adArrayList.addAll(adArrayList);
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view; //onBindViewHolder 함수에서 아이템의 클릭리스너를 달기위한 view
        @BindView(R.id.advertising_name_textview)
        TextView textViewLandName;  //원룸의 이름을 표시할 TextView
        @BindView(R.id.advertising_month_fee_textview)
        TextView textViewMonthFee; //월세가격
        @BindView(R.id.advertising_charter_fee_textview)
        TextView textViewCharterFee; //전세가격

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        /**
         * 복덕방 아이템의 글자들을 설정하는 함수
         */
        void onBind(Advertising ad) {
            if (ad.getTestTitle() == null)
                textViewLandName.setText("-");
            else
                textViewLandName.setText(ad.getTestTitle());

            if (ad.getTestPrice() == null)
                textViewMonthFee.setText("-");
            else
                textViewMonthFee.setText(ad.getTestPrice());

//            if (ad.getCharterFee() == null)
//                textViewCharterFee.setText("X");
//            else
//                textViewCharterFee.setText(ad.getCharterFee());
        }

    }

    @NonNull
    @Override
    public AdvertisingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertising_recyclerview_item, parent, false);
        return new AdvertisingRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisingRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.onBind(adArrayList.get(position));
        Advertising ad = adArrayList.get(position);

        holder.view.setOnClickListener(i->{
            //홍보게시판 상세페이지로 이동 구현해요~
        });
    }

    @Override
    public int getItemCount() {
        return adArrayList.size();
    }
}
