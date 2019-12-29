package in.koreatech.koin.service_board.viewholders;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;

/**
 * Created by hyerim on 2018. 7. 27....
 */
public class CommentViewHolderFactory {
    public static final int VIEW_TYPE_COMMENT = 0; //일반 댓글
    public static final int VIEW_TYPE_COMMENT_BOARD_AUTHOR = 1; //게시글 작성자 댓글

    public static int getItemLayoutId(int type) {
        int resId = 0;
        if (type == VIEW_TYPE_COMMENT) {
            resId = R.layout.board_comment_list_item;
        } else if (type == VIEW_TYPE_COMMENT_BOARD_AUTHOR) {
            resId = R.layout.board_comment_board_author_list_item;
        }
        return resId;
    }

    public static RecyclerView.ViewHolder getViewHolder(int type, View itemView) {
        RecyclerView.ViewHolder viewHolder = null;
        if (type == VIEW_TYPE_COMMENT) {
            viewHolder = new CommentViewHolder(itemView);
        } else if (type == VIEW_TYPE_COMMENT_BOARD_AUTHOR) {
            viewHolder = new CommentBoardAuthorViewHolder(itemView);
        }
        return viewHolder;
    }

    public static RecyclerView.ViewHolder getChangedViewHolder(RecyclerView.ViewHolder holder, int type) {
        if (type == VIEW_TYPE_COMMENT) {
            return (CommentViewHolder) holder;
        } else if (type == VIEW_TYPE_COMMENT_BOARD_AUTHOR) {
            return (CommentBoardAuthorViewHolder) holder;
        } else {
            return (CommentViewHolder) holder;
        }
    }

    //일반 유저
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.comment_writer)
        public
        TextView mCommentWriter;

        @Nullable
        @BindView(R.id.comment_content)
        public
        TextView mCommentContent;

        @Nullable
        @BindView(R.id.comment_create_date)
        public
        TextView mCommentCreateDate;

        @Nullable
        @BindView(R.id.comment_remove)
        public
        TextView mCommentRemove;

        @Nullable
        @BindView(R.id.comment_edit)
        public
        TextView mCommentModify;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //글 작성자 유저
    public static class CommentBoardAuthorViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.comment_board_author_writer)
        public
        TextView mCommentWriter;

        @Nullable
        @BindView(R.id.comment_board_author_content)
        public
        TextView mCommentContent;

        @Nullable
        @BindView(R.id.comment_board_author_create_date)
        public
        TextView mCommentCreateDate;

        @Nullable
        @BindView(R.id.comment_board_author_remove)
        public
        TextView mCommentRemove;

        @Nullable
        @BindView(R.id.comment_board_author_edit)
        public
        TextView mCommentModify;

        public CommentBoardAuthorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
