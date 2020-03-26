package in.koreatech.koin.ui.event.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Comment;

public class EventCommentAdapter extends RecyclerView.Adapter<EventCommentAdapter.ViewHolder> {
    private ArrayList<Comment> eventDetailComment;
    private String nickName; // 홍보글의 점주 닉네임과 댓글 닉네임을 비교하여 점주 마크를 표시


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopperTextView; // [점주] 마크
        TextView nicknameTextview;
        TextView timeTextview;
        TextView contentsTextview;
        Button fixButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopperTextView = (TextView)itemView.findViewById(R.id.event_comment_shopper_textview);
            nicknameTextview = (TextView)itemView.findViewById(R.id.event_comment_item_nickname_textview);
            timeTextview = (TextView) itemView.findViewById(R.id.event_comment_item_time_textview);
            contentsTextview = (TextView) itemView.findViewById(R.id.event_comment_item_contents_textview);
            fixButton = (Button)itemView.findViewById(R.id.event_comment_item_fix_button);
        }
    }

    public EventCommentAdapter(ArrayList<Comment> adComment, String nickName) {
        this.eventDetailComment = adComment;
        this.nickName = nickName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_detail_comment_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String commentNickName = eventDetailComment.get(position).getAuthorNickname();
        if(nickName.equals(commentNickName)){
            holder.shopperTextView.setVisibility(View.VISIBLE);
        }
        holder.nicknameTextview.setText(commentNickName);
        holder.timeTextview.setText(eventDetailComment.get(position).getCreateDate());
        holder.contentsTextview.setText(eventDetailComment.get(position).getContent());
//       holder.fitButton.setOnClickListener(i->{
//           // 수정버튼 반영하기
//       });
    }

    @Override
    public int getItemCount() {
        return eventDetailComment.size();
    }
}