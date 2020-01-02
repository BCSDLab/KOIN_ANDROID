package in.koreatech.koin.ui.search.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.SearchedArticle;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private ArrayList<SearchedArticle> searchedArticles;
    private String keyword;

    public SearchResultAdapter(ArrayList<SearchedArticle> searchedArticles, String keyword) {
        this.searchedArticles = searchedArticles;
        this.keyword = keyword;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_recyclerview_item, parent, false);
        return new SearchResultAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchedArticle searchedArticle = searchedArticles.get(position);
        if (searchedArticle.getServiceName() != null)
            holder.searchResultServiceName.setText("[" + searchedArticle.getServiceName() + "]");
        if (searchedArticle.getTitle() != null) {
            String title = searchedArticle.getTitle();
            if (title.contains(keyword)) {
                try {
                    title = title.replaceAll(keyword, "<font color=\"#175c8e\">" + keyword + "</font>").replaceAll("\n", "");
                } catch (PatternSyntaxException e) {
                    title = title.replaceAll(String.format("\\%s",keyword), "<font color=\"#175c8e\">" + "\\" + keyword + "</font>").replaceAll("\n", "");
                }
            }
            holder.searchResultTitle.setText(Html.fromHtml(title));

        }
        if (searchedArticle.getNickname() != null)
            holder.searchResultNickname.setText(searchedArticle.getNickname());
        if (searchedArticle.getUpdatedAt() != null)
            holder.searchResultUpdated.setText(searchedArticle.getUpdatedAt());
        holder.searchResultHit.setText(String.valueOf(searchedArticle.getHit()));
    }

    @Override
    public int getItemCount() {
        return searchedArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.search_result_service_name)
        TextView searchResultServiceName;
        @BindView(R.id.search_result_title)
        TextView searchResultTitle;
        @BindView(R.id.search_result_hit)
        TextView searchResultHit;
        @BindView(R.id.search_result_nickname)
        TextView searchResultNickname;
        @BindView(R.id.search_result_updated)
        TextView searchResultUpdated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
