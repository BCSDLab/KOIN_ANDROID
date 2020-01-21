package in.koreatech.koin.ui.lostfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Comment;

/**
 * 댓글 recyclerview item
 */
public class LostFoundCommentRecyclerviewAdapter extends RecyclerView.Adapter<LostFoundCommentRecyclerviewAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<Comment> lostItemCommentArrayList;
    private LostFoundCommentRecyclerviewAdapter.OnCommentRemoveButtonClickListener onCommentRemoveButtonClickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lostfound_comment_writer)
        TextView lostFoundCommentWriter;
        @BindView(R.id.lostfound_comment_create_date)
        TextView lostFoundCommentCreateDate;
        @BindView(R.id.lostfound_comment_content)
        TextView lostFoundCommentContent;
        @BindView(R.id.lostfound_comment_remove)
        Button lostFoundCommentRemoveButton;
        @BindView(R.id.lostfound_comment_edit)
        Button lostFoundCommentEditButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public LostFoundCommentRecyclerviewAdapter(Context context, ArrayList<Comment> lostItemCommentArrayList) {
        this.context = context;
        this.lostItemCommentArrayList = lostItemCommentArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lostfound_comment_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * ViewHolder을 통해서 view를 바인딩 해준다.
     *
     * @param holder   ViewHolder을 받아온다.
     * @param position 현재 표시해주는 위치를 받아온다.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = lostItemCommentArrayList.get(position);
        if (comment.authorNickname != null)
            holder.lostFoundCommentWriter.setText(comment.authorNickname);
        if (comment.createDate != null)
            holder.lostFoundCommentCreateDate.setText(comment.createDate);
        if (comment.content != null)
            holder.lostFoundCommentContent.setText(comment.content);
        if (comment.grantDelete) {
            holder.lostFoundCommentRemoveButton.setVisibility(View.VISIBLE);
            Objects.requireNonNull(holder.lostFoundCommentRemoveButton).setTag(position);
            holder.lostFoundCommentRemoveButton.setOnClickListener(this);
        } else {
            holder.lostFoundCommentRemoveButton.setVisibility(View.INVISIBLE);
        }
        if (comment.grantEdit) {
            holder.lostFoundCommentEditButton.setVisibility(View.VISIBLE);
            Objects.requireNonNull(holder.lostFoundCommentEditButton).setTag(position);
            holder.lostFoundCommentEditButton.setOnClickListener(this);
        } else {
            holder.lostFoundCommentEditButton.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        Comment comment = lostItemCommentArrayList.get((int) view.getTag());
        switch (view.getId()) {
            case R.id.lostfound_comment_remove:
                if (onCommentRemoveButtonClickListener != null)
                    onCommentRemoveButtonClickListener.onClickCommentRemoveButton(comment);
                break;
            case R.id.lostfound_comment_edit:
                if (onCommentRemoveButtonClickListener != null)
                    onCommentRemoveButtonClickListener.onClickCommentModifyButton(comment);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lostItemCommentArrayList.size();
    }

    public void setCustomOnClickListener(LostFoundCommentRecyclerviewAdapter.OnCommentRemoveButtonClickListener callback) {
        this.onCommentRemoveButtonClickListener = callback;
    }

    //onClick interface
    public interface OnCommentRemoveButtonClickListener {
        void onClickCommentRemoveButton(Comment comment);

        void onClickCommentModifyButton(Comment comment);
    }
}
