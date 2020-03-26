package in.koreatech.koin.ui.event.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Comment;

public class EventCommentAdapter extends RecyclerView.Adapter<EventCommentAdapter.ViewHolder> {
    private ArrayList<Comment> eventCommentList;
    private int userId; // 홍보글의 점주 닉네임과 댓글 닉네임을 비교하여 점주 마크를 표시


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopperTextView; // [점주] 마크
        TextView nicknameTextview;
        TextView timeTextview;
        TextView contentsTextview;
        Button fixButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopperTextView = (TextView)itemView.findViewById(R.id.event_comment_shopper_textview);
            nicknameTextview = (TextView)itemView.findViewById(R.id.event_comment_item_nickname_textview);
            timeTextview = (TextView) itemView.findViewById(R.id.event_comment_item_time_textview);
            contentsTextview = (TextView) itemView.findViewById(R.id.event_comment_item_contents_textview);
            fixButton = (Button)itemView.findViewById(R.id.event_comment_item_fix_button);
            deleteButton = (Button)itemView.findViewById(R.id.event_comment_item_delete_button);
        }
    }

    public EventCommentAdapter(ArrayList<Comment> eventComments, int userId) {
        this.eventCommentList = eventComments;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_detail_comment_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = eventCommentList.get(position);
        // [점주] 마크 Visibility 설정
        if(userId == comment.getAuthorUid()){
            holder.shopperTextView.setVisibility(View.VISIBLE);
        }
        // [수정] 버튼 Visibility 설정
        if(comment.isGrantEdit()) {
            holder.fixButton.setVisibility(View.VISIBLE);
        }
        // [삭제] 버튼 Visibility 설정
        if(comment.isGrantDelete())
            holder.deleteButton.setVisibility(View.VISIBLE);

        holder.nicknameTextview.setText(comment.getAuthorNickname());
        holder.timeTextview.setText(comment.getUpdateDate());
        holder.contentsTextview.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return eventCommentList.size();
    }
}