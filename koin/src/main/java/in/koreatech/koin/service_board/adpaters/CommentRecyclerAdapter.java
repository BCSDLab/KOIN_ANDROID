package in.koreatech.koin.service_board.adpaters;

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
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.service_board.viewholders.CommentViewHolderFactory;

import static in.koreatech.koin.service_board.viewholders.CommentViewHolderFactory.VIEW_TYPE_COMMENT;
import static in.koreatech.koin.service_board.viewholders.CommentViewHolderFactory.VIEW_TYPE_COMMENT_BOARD_AUTHOR;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class  CommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String TAG = CommentRecyclerAdapter.class.getSimpleName();

    private static final String EDITED = "(edited)"; //수정했을 경우 표시
    private Context mContext;

    private OnCommentRemoveButtonClickListener mOnCommentRemoveButtonClickListener;
    private Article mArticle;   //parent board
    private ArrayList<Comment> mCommentArrayList; //comment list

    public CommentRecyclerAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.mContext = context;
        this.mCommentArrayList = commentArrayList;
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
        final Comment comment = mCommentArrayList.get(position);

        String createDate = comment.createDate.substring(0, comment.createDate.length() - 5);
        String updateDate = comment.updateDate.substring(0, comment.updateDate.length() - 5);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_COMMENT:
                CommentViewHolderFactory.CommentViewHolder commentViewHolder = (CommentViewHolderFactory.CommentViewHolder) holder;
                Objects.requireNonNull(commentViewHolder.mCommentWriter).setText(comment.authorNickname);

                if (createDate.equals(updateDate)) { //not edited
                    Objects.requireNonNull(commentViewHolder.mCommentContent).setText(comment.content);
                } else {    //edited
                    SpannableStringBuilder spannableTextEdited = new SpannableStringBuilder(comment.content + EDITED);
                    spannableTextEdited.setSpan(new ForegroundColorSpan(ResourcesCompat.getColor(mContext.getResources(), R.color.gray1, null)), spannableTextEdited.length() - EDITED.length(), spannableTextEdited.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    Objects.requireNonNull(commentViewHolder.mCommentContent).setText(spannableTextEdited);
                }
                Objects.requireNonNull(commentViewHolder.mCommentCreateDate).setText(comment.createDate.substring(0, 10) + " " + comment.createDate.substring(11, 16));

                if (comment.grantEdit) {
                    Objects.requireNonNull(commentViewHolder.mCommentModify).setTag(position);
                    commentViewHolder.mCommentModify.setOnClickListener(this);
                    Objects.requireNonNull(commentViewHolder.mCommentModify).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentViewHolder.mCommentModify).setVisibility(View.GONE);
                }

                if (comment.grantDelete) {
                    Objects.requireNonNull(commentViewHolder.mCommentRemove).setTag(position);
                    commentViewHolder.mCommentRemove.setOnClickListener(this);
                    Objects.requireNonNull(commentViewHolder.mCommentRemove).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentViewHolder.mCommentRemove).setVisibility(View.GONE);
                }
                break;

            case VIEW_TYPE_COMMENT_BOARD_AUTHOR:
                CommentViewHolderFactory.CommentBoardAuthorViewHolder commentBoardAuthorViewHolder = (CommentViewHolderFactory.CommentBoardAuthorViewHolder) holder;
                Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentWriter).setText(comment.authorNickname);

                createDate = comment.createDate.substring(0, comment.createDate.length() - 5);
                updateDate = comment.updateDate.substring(0, comment.updateDate.length() - 5);

                if (createDate.equals(updateDate)) { //not edited
                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentContent).setText(comment.content);
                } else {    //edited
                    SpannableStringBuilder spannableText = new SpannableStringBuilder(comment.content + EDITED);
                    spannableText.setSpan(new ForegroundColorSpan(ResourcesCompat.getColor(mContext.getResources(), R.color.gray1, null)), comment.content.length(), comment.content.length() + EDITED.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentContent).setText(spannableText);
                }
                Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentCreateDate).setText(comment.createDate.substring(0, 10) + " " + comment.createDate.substring(11, 16));

                if (comment.grantEdit) {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentModify).setTag(position);
                    commentBoardAuthorViewHolder.mCommentModify.setOnClickListener(this);
                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentModify).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentModify).setVisibility(View.GONE);
                }

                if (comment.grantDelete) {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentRemove).setTag(position);
                    commentBoardAuthorViewHolder.mCommentRemove.setOnClickListener(this);
                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentRemove).setVisibility(View.VISIBLE);
                } else {
                    Objects.requireNonNull(commentBoardAuthorViewHolder.mCommentRemove).setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mCommentArrayList.size();
    }

    @Override
    public void onClick(View v) {
        Comment comment = mCommentArrayList.get((int) v.getTag());
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
//        if (mCommentArrayList.get(position).authorUid != Integer.parseInt(mArticle.authorUid)) { //다를 경우
        return VIEW_TYPE_COMMENT;
//        } else {
//            return VIEW_TYPE_COMMENT_BOARD_AUTHOR;
//        }
    }

    public void setCustomOnClickListener(OnCommentRemoveButtonClickListener callback) {
        this.mOnCommentRemoveButtonClickListener = callback;
    }

    public void setArticle(Article article) {
        mArticle = article;
    }

    //onClick interface
    public interface OnCommentRemoveButtonClickListener {
        void onClickCommentRemoveButton(Comment comment);

        void onClickCommentModifyButton(Comment comment);
    }
}