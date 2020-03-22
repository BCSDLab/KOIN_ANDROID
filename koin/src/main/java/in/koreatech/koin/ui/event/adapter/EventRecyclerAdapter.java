package in.koreatech.koin.ui.event.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Event;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private ArrayList<Event> adArrayList;
    private Context context;
    private final RequestOptions glideOptions;

    public EventRecyclerAdapter(ArrayList<Event> adArrayList, Context context) {
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
        @BindView(R.id.event_recyclerview_item_food_imageview)
        ImageView adFoodImageview;
        @BindView(R.id.event_recyclerview_item_store_title_textview)
        TextView adTitleTextview;
        @BindView(R.id.event_recyclerview_item_event_contents_textview)
        TextView adContentsTextview;
        @BindView(R.id.event_recyclerview_item_period_textview)
        TextView adPeriodTextview;
        @BindView(R.id.event_recyclerview_item_publish_date_textview)
        TextView adPublishedDateTextview;
        @BindView(R.id.adviertising_recyclerview_margam_imageview)
        ImageView adMargamImageview;
        @BindView(R.id.event_recyclerview_item_margam_textview)
        TextView adMargamTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void onBind(Event ad) {

            if (ad.getEventTitle() == null)
                adTitleTextview.setText("-");
            else
                adTitleTextview.setText(ad.getTitle());

            if (ad.getContent() == null)
                adContentsTextview.setText("-");
            else
                adContentsTextview.setText(ad.getEventTitle());

            if (ad.getStartDate() == null && ad.getEndDate() == null)
                adPeriodTextview.setText("-");
            else
                adPeriodTextview.setText(ad.getStartDate() + "~" + ad.getEndDate());

            String[] publishedDate = ad.getPublishedDate().split(" ");

            if (ad.getPublishedDate() == null)
                adPublishedDateTextview.setText("-");
            else
                adPublishedDateTextview.setText(publishedDate[0]);

            Date date = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if ((date.compareTo(formatDate.parse(ad.getEndDate()))  == 1)){
                    adMargamImageview.setVisibility(View.VISIBLE);
                    adMargamTextview.setVisibility(View.VISIBLE);
                    adMargamImageview.setColorFilter(Color.parseColor("#8C8C8C"), PorterDuff.Mode.LIGHTEN);
                    adFoodImageview.setColorFilter(Color.parseColor("#8C8C8C"), PorterDuff.Mode.LIGHTEN);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public EventRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recyclerview_item, parent, false);
        return new EventRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.onBind(adArrayList.get(position));
        Event ad = adArrayList.get(position);

        Glide.with(context)
                .load(ad.getThumbnail())
                .apply(glideOptions)
                .into(holder.adFoodImageview);
    }

    @Override
    public int getItemCount() {
        return adArrayList.size();
    }


    public void setAdArrayList(ArrayList<Event> adArrayList) {
        this.adArrayList = adArrayList;
    }
}
