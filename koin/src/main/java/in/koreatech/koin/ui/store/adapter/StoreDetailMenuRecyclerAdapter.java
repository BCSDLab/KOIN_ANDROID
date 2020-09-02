package in.koreatech.koin.ui.store.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Store;


import java.util.ArrayList;

public class StoreDetailMenuRecyclerAdapter extends RecyclerView.Adapter<StoreDetailMenuRecyclerAdapter.ViewHolder> {

    private final String TAG = StoreDetailMenuRecyclerAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Store> storeMenuArrayList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.store_detail_menu_name_textview)
        TextView storeDetailMenuNameTextview;
        @BindView(R.id.store_detail_menu_price_textview)
        TextView storeDetailMenuPriceTextview;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public StoreDetailMenuRecyclerAdapter(Context context, ArrayList<Store> storeArrayList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.storeMenuArrayList = new ArrayList<>();
        this.storeMenuArrayList.addAll(storeArrayList);
    }

    public void setStoreMenuArrayList(ArrayList<Store> storeMenuArrayList) {
        this.storeMenuArrayList = storeMenuArrayList;
    }

    @NonNull
    @Override
    public StoreDetailMenuRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_detail_menu_list_item, parent, false);
        return new StoreDetailMenuRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreDetailMenuRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Store storeMenu = this.storeMenuArrayList.get(position);
        holder.storeDetailMenuNameTextview.setText(storeMenu.getName());
        holder.storeDetailMenuPriceTextview.setText(storeMenu.getDetail());


    }

    @Override
    public int getItemCount() {
        return this.storeMenuArrayList.size();
    }

}
