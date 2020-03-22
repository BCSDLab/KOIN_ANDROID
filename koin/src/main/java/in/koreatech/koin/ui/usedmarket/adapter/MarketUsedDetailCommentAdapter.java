package in.koreatech.koin.ui.usedmarket.adapter;

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
import in.koreatech.koin.data.network.entity.Comment;

import java.util.ArrayList;
import java.util.Objects;

public class MarketUsedDetailCommentAdapter extends RecyclerView.Adapter<MarketUsedDetailCommentAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private LayoutInflater layoutInflater; //inflate 사용위한 inflater
    private ArrayList<Comment> marketCommentArrayList;

    private OnCommentRemoveButtonClickListener onCommentRemoveButtonClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.market_used_comment_writer)
        TextView marketCommentWriterTextView;
        @BindView(R.id.market_used_comment_remove)
        TextView marketCommentRemoveTextView;
        @BindView(R.id.market_used_comment_edit)
        TextView marketCommentEditTextView;
        @BindView(R.id.market_used_comment_create_date)
        TextView marketCommentCreateDateTextView;
        @BindView(R.id.market_used_comment_content)
        TextView marketCommentConetentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public MarketUsedDetailCommentAdapter(Context context, ArrayList<Comment> marketCommentArrayList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.marketCommentArrayList = new ArrayList<>();
        this.marketCommentArrayList = marketCommentArrayList;
    }


    @NonNull
    @Override
    public MarketUsedDetailCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_used_comment_list_item, parent, false);
        return new MarketUsedDetailCommentAdapter.ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return marketCommentArrayList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull MarketUsedDetailCommentAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Comment comment = marketCommentArrayList.get(position);
        holder.marketCommentWriterTextView.setText(comment.getAuthorNickname());
        holder.marketCommentCreateDateTextView.setText(comment.getUpdateDate());
        holder.marketCommentConetentTextView.setText(comment.getContent());
        if (!comment.isGrantEdit())
            holder.marketCommentEditTextView.setVisibility(View.INVISIBLE);
        else {
            holder.marketCommentEditTextView.setOnClickListener(this);
            Objects.requireNonNull(holder.marketCommentEditTextView).setTag(position);
        }

        if (!comment.isGrantDelete())
            holder.marketCommentRemoveTextView.setVisibility(View.INVISIBLE);
        else {
            holder.marketCommentRemoveTextView.setOnClickListener(this);
            Objects.requireNonNull(holder.marketCommentRemoveTextView).setTag(position);
        }


    }

    @Override
    public void onClick(View v) {
        Comment comment = marketCommentArrayList.get((int) v.getTag());
        switch (v.getId()) {
            case R.id.market_used_comment_edit:
                onCommentRemoveButtonClickListener.onClickCommentModifyButton(comment);
                break;
            case R.id.market_used_comment_remove:
                onCommentRemoveButtonClickListener.onClickCommentRemoveButton(comment);
                break;
            default:
                break;
        }


    }

    public void setCustomOnClickListener(OnCommentRemoveButtonClickListener callback) {
        this.onCommentRemoveButtonClickListener = callback;
    }

    //onClick interface
    public interface OnCommentRemoveButtonClickListener {
        void onClickCommentRemoveButton(Comment comment);

        void onClickCommentModifyButton(Comment comment);
    }
}
