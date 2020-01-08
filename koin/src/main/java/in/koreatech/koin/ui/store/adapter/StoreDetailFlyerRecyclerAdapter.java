package in.koreatech.koin.ui.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;

public class StoreDetailFlyerRecyclerAdapter extends RecyclerView.Adapter<StoreDetailFlyerRecyclerAdapter.ViewHolder> {

    private final String TAG = StoreDetailFlyerRecyclerAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> storeFlyerArrayList;
    private final RequestOptions glideOptions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.store_flyer_imageview)
        ImageView flyerImageview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public StoreDetailFlyerRecyclerAdapter(Context context, ArrayList<String> storeFlyerArrayList) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.storeFlyerArrayList = storeFlyerArrayList;
        glideOptions = new RequestOptions()
                .fitCenter()
                .override(300, 300)
                .error(R.drawable.image_no_image)
                .placeholder(R.color.white);
    }


    @NonNull
    @Override
    public StoreDetailFlyerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_flyer_list_item, parent, false);
        return new StoreDetailFlyerRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreDetailFlyerRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String url = storeFlyerArrayList.get(position);
        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(holder.flyerImageview);
    }

    @Override
    public int getItemCount() {
        return storeFlyerArrayList.size();
    }

}
