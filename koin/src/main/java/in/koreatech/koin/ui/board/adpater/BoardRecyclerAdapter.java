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
    private static final String TAG = "BoardRecyclerAdapter";

    private final ArrayList<Article> articleArrayList; //article list

    //자유게시판, 익명게시판, 취업게시판
    class BoardViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.board_create_date)
        TextView boardCreateDate;

        @Nullable
        @BindView(R.id.board_view_count)
        TextView boardViewCount;

        @Nullable
        @BindView(R.id.board_title)
        TextView boardTitle;

        @Nullable
        @BindView(R.id.board_author)
        TextView boardWriter;

        @Nullable
        @BindView(R.id.board_comment_count)
        TextView boardCommentCount;

        public BoardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public BoardRecyclerAdapter(ArrayList<Article> articleArrayList) {
        this.articleArrayList = articleArrayList;
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
        final Article article = articleArrayList.get(position);

        BoardViewHolder boardViewHolder = (BoardViewHolder) holder;

        Objects.requireNonNull(boardViewHolder.boardCreateDate).setText(article.createDate.substring(0, 10));
        Objects.requireNonNull(boardViewHolder.boardViewCount).setText(String.valueOf(article.hitCount));
        if (article.commentCount > 0) { //when board has comments
            if (article.commentCount < 1000)
                Objects.requireNonNull(boardViewHolder.boardCommentCount).setText("(" + article.commentCount + ")");
            else
                Objects.requireNonNull(boardViewHolder.boardCommentCount).setText("(999+)");
        } else {    //no comments in board
            Objects.requireNonNull(boardViewHolder.boardCommentCount).setText("");
        }
        Objects.requireNonNull(boardViewHolder.boardTitle).setText(article.title);
        Objects.requireNonNull(boardViewHolder.boardWriter).setText(article.authorNickname);

    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }


}
