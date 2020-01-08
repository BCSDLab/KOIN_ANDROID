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
    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<Comment> mMarketCommentArrayList;

    private OnCommentRemoveButtonClickListener mOnCommentRemoveButtonClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.market_used_comment_writer)
        TextView mMarketCommentWriterTextView;
        @BindView(R.id.market_used_comment_remove)
        TextView mMarketCommentRemoveTextView;
        @BindView(R.id.market_used_comment_edit)
        TextView mMarketCommentEditTextView;
        @BindView(R.id.market_used_comment_create_date)
        TextView mMarketCommentCreateDateTextView;
        @BindView(R.id.market_used_comment_content)
        TextView mMarketCommentConetentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public MarketUsedDetailCommentAdapter(Context context, ArrayList<Comment> mMarketCommentArrayList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mMarketCommentArrayList = new ArrayList<>();
        this.mMarketCommentArrayList = mMarketCommentArrayList;
    }


    @NonNull
    @Override
    public MarketUsedDetailCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_used_comment_list_item, parent, false);
        return new MarketUsedDetailCommentAdapter.ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mMarketCommentArrayList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull MarketUsedDetailCommentAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Comment comment = mMarketCommentArrayList.get(position);
        holder.mMarketCommentWriterTextView.setText(comment.authorNickname);
        holder.mMarketCommentCreateDateTextView.setText(comment.updateDate);
        holder.mMarketCommentConetentTextView.setText(comment.content);
        if (!comment.grantEdit)
            holder.mMarketCommentEditTextView.setVisibility(View.INVISIBLE);
        else {
            holder.mMarketCommentEditTextView.setOnClickListener(this);
            Objects.requireNonNull(holder.mMarketCommentEditTextView).setTag(position);
        }

        if (!comment.grantDelete)
            holder.mMarketCommentRemoveTextView.setVisibility(View.INVISIBLE);
        else {
            holder.mMarketCommentRemoveTextView.setOnClickListener(this);
            Objects.requireNonNull(holder.mMarketCommentRemoveTextView).setTag(position);
        }


    }

    @Override
    public void onClick(View v) {
        Comment comment = mMarketCommentArrayList.get((int) v.getTag());
        switch (v.getId()) {
            case R.id.market_used_comment_edit:
                mOnCommentRemoveButtonClickListener.onClickCommentModifyButton(comment);
                break;
            case R.id.market_used_comment_remove:
                mOnCommentRemoveButtonClickListener.onClickCommentRemoveButton(comment);
                break;
            default:
                break;
        }


    }

    public void setCustomOnClickListener(OnCommentRemoveButtonClickListener callback) {
        this.mOnCommentRemoveButtonClickListener = callback;
    }

    //onClick interface
    public interface OnCommentRemoveButtonClickListener {
        void onClickCommentRemoveButton(Comment comment);

        void onClickCommentModifyButton(Comment comment);
    }
}
