package in.koreatech.koin.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;

public class SearchRecentAdapter extends RecyclerView.Adapter<SearchRecentAdapter.ViewHolder> {
    private ArrayList<String> titles;

    public SearchRecentAdapter(ArrayList<String> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recentlist_recyclerview_item, parent, false);
        return new SearchRecentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = titles.get(position);
        if (title != null)
            holder.searchRecentTextview.setText(title);

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recent_search_textview)
        TextView searchRecentTextview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
