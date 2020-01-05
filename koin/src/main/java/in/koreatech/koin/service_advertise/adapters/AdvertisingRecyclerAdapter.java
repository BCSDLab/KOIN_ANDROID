package in.koreatech.koin.service_advertise.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.entity.Advertising;
import in.koreatech.koin.core.networks.entity.BokdukRoom;
import in.koreatech.koin.service_advertise.ui.AdvertisingDetailActivity;
import in.koreatech.koin.service_dining.adapters.DiningRecyclerAdapter;
import in.koreatech.koin.service_land.adapter.LandRecyclerAdapter;

/**
 * Created by hansol on 2020.1.1...
 */
public class AdvertisingRecyclerAdapter extends RecyclerView.Adapter<AdvertisingRecyclerAdapter.ViewHolder> {

    private ArrayList<Advertising> adArrayList;
    private Context context;
    private final RequestOptions glideOptions;

    public AdvertisingRecyclerAdapter(ArrayList<Advertising> adArrayList, Context context) {
        this.adArrayList = adArrayList;
        this.context = context;

        glideOptions = new RequestOptions()
                .fitCenter()
                .override(158, 106)
                .error(R.drawable.img_noimage)
                .placeholder(R.color.white);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view; //onBindViewHolder 함수에서 아이템의 클릭리스너를 달기위한 view
        private Context context;
        @BindView(R.id.advertising_recyclerview_item_food_imageview)
        ImageView adFoodImageview;
        @BindView(R.id.advertising_recyclerview_item_store_title_textview)
        TextView adTitleTextview;
        @BindView(R.id.advertising_recyclerview_item_event_contents_textview)
        TextView adContentsTextview;
        @BindView(R.id.advertising_recyclerview_item_period_textview)
        TextView adPeriodTextview;
        @BindView(R.id.advertising_recyclerview_item_publish_date_textview)
        TextView adPublishedDateTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void onBind(Advertising ad) {

            if (ad.getEventTitle() == null)
                adTitleTextview.setText("-");
            else
                adTitleTextview.setText(ad.getEventTitle());

            if (ad.getContent() == null)
                adContentsTextview.setText("-");
            else
                adContentsTextview.setText(ad.getContent());

            if (ad.getStartDate() == null && ad.getEndDate() == null)
                adPeriodTextview.setText("-");
            else
                adPeriodTextview.setText(ad.startDate+"~"+ad.getEndDate());

            String[] publishedDate = ad.getPublishedDate().split(" ");

            if(ad.getPublishedDate() == null)
                adPublishedDateTextview.setText("-");
            else
                adPublishedDateTextview.setText(publishedDate[0]);

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

        Glide.with(context)
                .load(ad.thumbnail)
                .apply(glideOptions)
                .into(holder.adFoodImageview);

            holder.view.setOnClickListener(i->{
                Intent intent = new Intent(context, AdvertisingDetailActivity.class);
                intent.putExtra("ID",ad.getId());
                context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return adArrayList.size();
    }
}
