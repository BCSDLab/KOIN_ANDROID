package in.koreatech.koin.ui.circle.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Circle;

/**
 * Created by hyerim on 2018. 8. 12....
 */
public class CircleRecyclerAdapter extends RecyclerView.Adapter<CircleRecyclerAdapter.ViewHolder> {
    private final String TAG = CircleRecyclerAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<Circle> mCirlceArrayList;
    private final RequestOptions mGlideOptions;
    private final Resources mResource;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.circle_item_background_imageview)
        ImageView mCircleItemBackgroundImageview;
        @BindView(R.id.circle_item_logo_background_imageview)
        ImageView mCircleItemLogoBackgroundImageview;
        @BindView(R.id.circle_item_logo_background_border_imageview)
        ImageView mCircleItemLogoBackgroundBorderImageview;
        @BindView(R.id.circle_item_logo_imageview)
        ImageView mCircleItemLogoImageview;
        @BindView(R.id.circle_item_name_textview)
        TextView mCircleItemNameTextview;
        @BindView(R.id.circle_item_detail_textview)
        TextView mCircleItemDetailTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CircleRecyclerAdapter(Context context, ArrayList<Circle> circleArrayList) {
        this.mContext = context;
        this.mResource = context.getResources();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mCirlceArrayList = new ArrayList<>();
        this.mCirlceArrayList.addAll(circleArrayList);
        mGlideOptions = new RequestOptions()
                .fitCenter()
                .override(300, 300)
                .error(R.drawable.image_no_image)
                .placeholder(R.color.white);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Circle circle = mCirlceArrayList.get(position);
        Glide.with(mContext).load(new ColorDrawable(mContext.getResources().getColor(R.color.white))).apply(RequestOptions.circleCropTransform()).into(holder.mCircleItemLogoBackgroundImageview);
        Glide.with(mContext).load(new ColorDrawable(mContext.getResources().getColor(R.color.cloudy_blue))).apply(RequestOptions.circleCropTransform()).into(holder.mCircleItemLogoBackgroundBorderImageview);

        if (circle.name != null)
            holder.mCircleItemNameTextview.setText(circle.name);
        if (circle.lineDescription != null)
            holder.mCircleItemDetailTextview.setText(circle.lineDescription);
        if (circle.logoUrl != null) {
            Glide.with(mContext)
                    .load(circle.logoUrl)
                    .apply(mGlideOptions)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mCircleItemLogoImageview);
        } else {
            Glide.with(mContext).load(new ColorDrawable(mContext.getResources().getColor(R.color.gray1))).apply(RequestOptions.circleCropTransform()).into(holder.mCircleItemLogoImageview);
        }
        if (circle.backgroundImgUrl != null) {
            Glide.with(mContext)
                    .load(circle.backgroundImgUrl)
                    .apply(mGlideOptions)
                    .into(holder.mCircleItemBackgroundImageview);
        } else {
            holder.mCircleItemBackgroundImageview.setBackgroundColor(mResource.getColor(R.color.light_navy));
        }

    }

    @Override
    public int getItemCount() {
        return mCirlceArrayList.size();
    }

}
