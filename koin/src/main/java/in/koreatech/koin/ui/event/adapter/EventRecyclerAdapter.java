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

    private ArrayList<Event> eventArrayList;
    private Context context;
    private final RequestOptions glideOptions;

    public EventRecyclerAdapter(ArrayList<Event> eventArrayList, Context context) {
        this.eventArrayList = eventArrayList;
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
        ImageView eventFoodImageview;
        @BindView(R.id.event_recyclerview_item_store_title_textview)
        TextView eventTitleTextview;
        @BindView(R.id.event_recyclerview_item_event_contents_textview)
        TextView eventContentsTextview;
        @BindView(R.id.event_recyclerview_item_period_textview)
        TextView eventPeriodTextview;
        @BindView(R.id.event_recyclerview_item_publish_date_textview)
        TextView eventPublishedDateTextview;
        @BindView(R.id.event_recyclerview_margam_imageview)
        ImageView eventMargamImageview;
        @BindView(R.id.event_recyclerview_item_margam_textview)
        TextView eventMargamTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void onBind(Event event) {

            if (event.getEventTitle() == null)
                eventTitleTextview.setText("-");
            else
                eventTitleTextview.setText(event.getTitle());

            if (event.getContent() == null)
                eventContentsTextview.setText("-");
            else
                eventContentsTextview.setText(event.getEventTitle());

            if (event.getStartDate() == null && event.getEndDate() == null)
                eventPeriodTextview.setText("-");
            else
                eventPeriodTextview.setText(event.getStartDate() + "~" + event.getEndDate());

            String[] publishedDate = event.getCreatedAt().split(" ");

            if (event.getCreatedAt() == null)
                eventPublishedDateTextview.setText("-");
            else
                eventPublishedDateTextview.setText(publishedDate[0]);

            Date date = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if ((date.compareTo(formatDate.parse(event.getEndDate()))  == 1)){
                    eventMargamImageview.setVisibility(View.VISIBLE);
                    eventMargamTextview.setVisibility(View.VISIBLE);
                    eventMargamImageview.setColorFilter(Color.parseColor("#8C8C8C"), PorterDuff.Mode.LIGHTEN);
                    eventFoodImageview.setColorFilter(Color.parseColor("#8C8C8C"), PorterDuff.Mode.LIGHTEN);
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
        holder.onBind(eventArrayList.get(position));
        Event event = eventArrayList.get(position);

        Glide.with(context)
                .load(event.getThumbnail())
                .apply(glideOptions)
                .into(holder.eventFoodImageview);
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }


    public void setAdArrayList(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }
}
