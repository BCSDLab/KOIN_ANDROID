package in.koreatech.koin.ui.board.adpater;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.ui.board.adpater.holder.CommentViewHolderFactory;

import static in.koreatech.koin.ui.board.adpater.holder.CommentViewHolderFactory.VIEW_TYPE_COMMENT;
import static in.koreatech.koin.ui.board.adpater.holder.CommentViewHolderFactory.VIEW_TYPE_COMMENT_BOARD_AUTHOR;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "CommentRecyclerAdapter";

    private static final String EDITED = "(edited)"; //수정했을 경우 표시
    private Context context;

    private OnCommentRemoveButtonClickListener mOnCommentRemoveButtonClickListener;
    private Article article;   //parent board
    private ArrayList<Comment> commentArrayList; //comment list

    public CommentRecyclerAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resId = CommentViewHolderFactory.getItemLayoutId(viewType);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
        RecyclerView.ViewHolder viewHolder = CommentViewHolderFactory.getViewHolder(viewType, itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Comment comment = commentArrayList.get(position);

        String createDate = comment.getCreateDate().substring(0, comment.getCreateDate().length() - 5);
        String updateDate = comment.getCreateDate().substring(0, comment.getCreateDate().length() - 5);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_COMMENT:
                CommentViewHolderFactory.CommentViewHolder commentViewHolder = (CommentViewHolderFactory.CommentViewHolder) holder;
                Objects.requireNonNull(commentViewHolder.commentWriter).setText(comment.getAuthorNickname());

                if (createDate.equals(updateDate)) { //not edited
                    Objects.requireNonNull(commentViewHolder.commentContent).setText(comment.getContent());
                } else {    //edited
                    SpannableStringBuilder spannableTextEdited = new SpannableStringBuilder(comment.getContent() + EDITED);
                    spannableTextEdited.setSpan(new ForegroundColorSpan(ResourcesCompat.getColor(context.getResources(), R.color.gray4, null)), spannableTextEdited.length() - EDITED.length(), spannableTextEdited.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    Objects.requireNonNull(commentViewHolder.commentContent).setText(spannableTextEdited);
                }
                Objects.requireNonNull(commentViewHolder.commentCreateDate).setText(comment.getCreateDate().substring(0, 10) + " " + comment.getCreateDate().substring(11, 16));

                if (comment.isGrantEdit()) {
                    Objects.requireNonNull(commentViewHolder.commentModify).setTag(position);
                    commentViewHolder.commentModify.setOnClickListener(this);
                    Objects.requireNonNull(commentViewHolder.commentModify).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentViewHolder.commentModify).setVisibility(View.GONE);
                }

                if (comment.isGrantDelete()) {
                    Objects.requireNonNull(commentViewHolder.commentRemove).setTag(position);
                    commentViewHolder.commentRemove.setOnClickListener(this);
                    Objects.requireNonNull(commentViewHolder.commentRemove).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentViewHolder.commentRemove).setVisibility(View.GONE);
                }
                break;

            case VIEW_TYPE_COMMENT_BOARD_AUTHOR:
                CommentViewHolderFactory.CommentBoardAuthorViewHolder commentBoardAuthorViewHolder = (CommentViewHolderFactory.CommentBoardAuthorViewHolder) holder;
                Objects.requireNonNull(commentBoardAuthorViewHolder.commentWriter).setText(comment.getAuthorNickname());

                createDate = comment.getCreateDate().substring(0, comment.getCreateDate().length() - 5);
                updateDate = comment.getUpdateDate().substring(0, comment.getUpdateDate().length() - 5);

                if (createDate.equals(updateDate)) { //not edited
                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentContent).setText(comment.getContent());
                } else {    //edited
                    SpannableStringBuilder spannableText = new SpannableStringBuilder(comment.getContent() + EDITED);
                    spannableText.setSpan(new ForegroundColorSpan(ResourcesCompat.getColor(context.getResources(), R.color.gray4, null)), comment.getContent().length(), comment.getContent().length() + EDITED.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentContent).setText(spannableText);
                }
                Objects.requireNonNull(commentBoardAuthorViewHolder.commentCreateDate).setText(comment.getCreateDate().substring(0, 10) + " " + comment.getCreateDate().substring(11, 16));

                if (comment.isGrantEdit()) {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentModify).setTag(position);
                    commentBoardAuthorViewHolder.commentModify.setOnClickListener(this);
                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentModify).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentModify).setVisibility(View.GONE);
                }

                if (comment.isGrantDelete()) {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentRemove).setTag(position);
                    commentBoardAuthorViewHolder.commentRemove.setOnClickListener(this);
                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentRemove).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.commentRemove).setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    @Override
    public void onClick(View v) {
        Comment comment = commentArrayList.get((int) v.getTag());
        switch (v.getId()) {
            case R.id.comment_remove:
            case R.id.comment_board_author_remove:
                mOnCommentRemoveButtonClickListener.onClickCommentRemoveButton(comment);
                break;
            case R.id.comment_edit:
            case R.id.comment_board_author_edit:
                mOnCommentRemoveButtonClickListener.onClickCommentModifyButton(comment);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return classifyViewType(position);
    }

    private int classifyViewType(int position) {
//        if (commentArrayList.get(position).authorUid != Integer.parseInt(article.authorUid)) { //다를 경우
        return VIEW_TYPE_COMMENT;
//        } else {
//            return VIEW_TYPE_COMMENT_BOARD_AUTHOR;
//        }
    }

    public void setCustomOnClickListener(OnCommentRemoveButtonClickListener callback) {
        this.mOnCommentRemoveButtonClickListener = callback;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    //onClick interface
    public interface OnCommentRemoveButtonClickListener {
        void onClickCommentRemoveButton(Comment comment);

        void onClickCommentModifyButton(Comment comment);
    }
}