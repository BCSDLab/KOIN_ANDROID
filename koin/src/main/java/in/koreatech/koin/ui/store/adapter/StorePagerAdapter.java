package in.koreatech.koin.ui.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Store;

public class StorePagerAdapter extends PagerAdapter {

    private Context context;
    private List<Store> allStoreList;
    private String[] categoryCode;

    public StorePagerAdapter(Context context, List<Store> allStoreList, String[] categoryCode) {
        this.context = context;
        this.allStoreList = allStoreList;
        this.categoryCode = categoryCode;
    }

    @Override
    public int getCount() {
        return categoryCode.length + 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_main_pager_item, container, false);

        RecyclerView storeRecyclerView = view.findViewById(R.id.store_recycler_view);

        StoreRecyclerAdapter storeRecyclerAdapter = new StoreRecyclerAdapter(context, categorizeStore(position));
        storeRecyclerView.setAdapter(storeRecyclerAdapter);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        view.setTag(position);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
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
