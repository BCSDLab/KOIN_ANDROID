package in.koreatech.koin.ui.board.adpater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Article;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class BoardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = BoardRecyclerAdapter.class.getSimpleName();

    private final ArrayList<Article> mArticleArrayList; //article list

    //자유게시판, 익명게시판, 취업게시판
    class BoardViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.board_create_date)
        TextView mBoardCreateDate;

        @Nullable
        @BindView(R.id.board_view_count)
        TextView mBoardViewCount;

        @Nullable
        @BindView(R.id.board_title)
        TextView mBoardTitle;

        @Nullable
        @BindView(R.id.board_author)
        TextView mBoardWriter;

        @Nullable
        @BindView(R.id.board_comment_count)
        TextView mBoardCommentCount;

        public BoardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public BoardRecyclerAdapter(ArrayList<Article> articleArrayList) {
        this.mArticleArrayList = articleArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_list_item, parent, false);
        return new BoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Article article = mArticleArrayList.get(position);

        BoardViewHolder boardViewHolder = (BoardViewHolder) holder;

        Objects.requireNonNull(boardViewHolder.mBoardCreateDate).setText(article.createDate.substring(0, 10));
        Objects.requireNonNull(boardViewHolder.mBoardViewCount).setText(String.valueOf(article.hitCount));
        if (article.commentCount > 0) { //when board has comments
            if(article.commentCount<1000)
            Objects.requireNonNull(boardViewHolder.mBoardCommentCount).setText("("+article.commentCount + ")");
            else
                Objects.requireNonNull(boardViewHolder.mBoardCommentCount).setText("(999+)");
        } else {    //no comments in board
            Objects.requireNonNull(boardViewHolder.mBoardCommentCount).setText("");
        }
        Objects.requireNonNull(boardViewHolder.mBoardTitle).setText(article.title);
        Objects.requireNonNull(boardViewHolder.mBoardWriter).setText(article.authorNickname);

    }

    @Override
    public int getItemCount() {
        return mArticleArrayList.size();
    }


}
