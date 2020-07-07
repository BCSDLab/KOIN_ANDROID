package in.koreatech.koin.ui.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Store;

public class StorePagerAdapter extends RecyclerView.Adapter<StorePagerAdapter.StorePagerViewHolder> {

    private List<Store> allStoreList;
    private String[] categoryCode;

    public StorePagerAdapter(List<Store> allStoreList, String[] categoryCode) {
        this.allStoreList = allStoreList;
        this.categoryCode = categoryCode;
    }

    @NonNull
    @Override
    public StorePagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StorePagerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.store_main_pager_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StorePagerViewHolder holder, int position) {
        StoreRecyclerAdapter storeRecyclerAdapter = new StoreRecyclerAdapter(holder.context, categorizeStore(position));
        holder.storeRecyclerView.setAdapter(storeRecyclerAdapter);
    }



    @Override
    public int getItemCount() {
        return categoryCode.length + 1;
    }

    public static class StorePagerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.store_recycler_view)
        public RecyclerView storeRecyclerView;

        public Context context;

        public StorePagerViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);

            storeRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public ArrayList<Store> categorizeStore(int position) {
        ArrayList<Store> storeList = new ArrayList<>();
        if (position == 0)
            storeList.addAll(this.allStoreList);
        else {
            for (int a = 0; a < this.allStoreList.size(); a++)
                if (isSameCategory(this.allStoreList.get(a).getCategory(), categoryCode[position - 1]))
                    storeList.add(this.allStoreList.get(a));
        }

        return storeList;
    }

    public boolean isSameCategory(String first, String second) {
        if (first.equals("S001") && second.equals("S000"))
            return true;
        return first.equals(second);
    }

}
