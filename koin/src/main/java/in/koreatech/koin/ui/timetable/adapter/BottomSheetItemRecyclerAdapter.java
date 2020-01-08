package in.koreatech.koin.ui.timetable.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import in.koreatech.koin.core.R;

/**
 * Created by hyerim on 2018. 8. 4....
 */
public class BottomSheetItemRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BottomSheetItemRecyclerAdapter";

    private Context context;
    private ArrayList<String> mItemsArrayList;

    class ItemsViewHolder extends RecyclerView.ViewHolder {

        TextView mItemTextView;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            mItemTextView = itemView.findViewById(R.id.bottom_sheet_list_item_textview);
        }
    }

    public BottomSheetItemRecyclerAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.mItemsArrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_list_item, parent, false);
        return new ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String item = mItemsArrayList.get(position);

        BottomSheetItemRecyclerAdapter.ItemsViewHolder itemsViewHolder = (BottomSheetItemRecyclerAdapter.ItemsViewHolder) holder;
        itemsViewHolder.mItemTextView.setText(item);
    }

    @Override
    public int getItemCount() {
        return mItemsArrayList.size();
    }
}
