package in.koreatech.koin.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.koreatech.koin.R;
import in.koreatech.koin.core.recyclerview.RecyclerViewClickListener;

public class StoreCategoryRecyclerAdapter extends RecyclerView.Adapter<StoreCategoryRecyclerAdapter.ViewHolder> {

    private final String[] CATEGORY_TEXT_ID = {
            "치킨", "피자", "탕수육", "도시락", "족발", "중국집", "일반음식", "미용실", "기타"
    };
    private final int[] CATEGORY_IMAGE_ID = {
            R.drawable.ic_chicken, R.drawable.ic_pizza, R.drawable.ic_sweet_pork, R.drawable.ic_dosirak, R.drawable.ic_porkfeet, R.drawable.ic_chinese, R.drawable.ic_normal, R.drawable.ic_hair, R.drawable.ic_etc
    };
    private RecyclerViewClickListener recyclerViewClickListener = null;

    public void setRecyclerViewClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public StoreCategoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_store, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoreCategoryRecyclerAdapter.ViewHolder holder, int position) {
        holder.textView.setText(CATEGORY_TEXT_ID[position]);
        holder.imageView.setImageResource(CATEGORY_IMAGE_ID[position]);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewClickListener != null)
                    recyclerViewClickListener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CATEGORY_TEXT_ID.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_store_category);
            imageView = itemView.findViewById(R.id.image_view_store_category);
            container = itemView.findViewById(R.id.container);
        }
    }
}
