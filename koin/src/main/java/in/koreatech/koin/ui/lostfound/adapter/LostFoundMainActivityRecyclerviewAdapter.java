package in.koreatech.koin.ui.lostfound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.LostItem;


public class LostFoundMainActivityRecyclerviewAdapter extends RecyclerView.Adapter<LostFoundMainActivityRecyclerviewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LostItem> lostItemArrayList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lostfound_create_title_textview)
        TextView lostfoundCreateTitleTextview;
        @BindView(R.id.lostfound_create_comment_count_textview)
        TextView lostfoundCreateCommentCountTextview;
        @BindView(R.id.lostfound_create_lookup_number_textview)
        TextView lostfoundCreateLookupNumberTextview;
        @BindView(R.id.lostfound_create_nickname_textview)
        TextView lostfoundFoundCreateNicknameTextview;
        @BindView(R.id.lostfound_create_date_textview)
        TextView lostfoundCreateDateTextview;
        @BindView(R.id.lostfound_create_lost_info_korean_textview)
        TextView lostfoundCreateLostInfoKoreanTextview;
        @BindView(R.id.lostfound_create_lost_info_date_textview)
        TextView lostfoundCreateLostInfoDateTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public LostFoundMainActivityRecyclerviewAdapter(Context context, ArrayList<LostItem> lostFoundArrayList) {
        this.context = context;
        this.lostItemArrayList = lostFoundArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lostfound_list_item, parent, false);
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
        LostItem lostItem = lostItemArrayList.get(position);
        if (lostItem == null) return;
        if (lostItem.getTitle() != null)
            holder.lostfoundCreateTitleTextview.setText(lostItem.getTitle());
        holder.lostfoundCreateCommentCountTextview.setText(getCommentCountString(lostItem.getCommentCount()));
        holder.lostfoundCreateLookupNumberTextview.setText(Integer.toString(lostItem.getHit()));
        if (lostItem.getNickname() != null)
            holder.lostfoundFoundCreateNicknameTextview.setText(lostItem.getNickname());
        if (lostItem.getDate() != null)
            holder.lostfoundCreateLostInfoDateTextview.setText(lostItem.getDate());
        if (lostItem.getType() == 0) {
            holder.lostfoundCreateLostInfoKoreanTextview.setText("습득일");
        } else if (lostItem.getType() == 1) {
            holder.lostfoundCreateLostInfoKoreanTextview.setText("분실일");
        }
        if (lostItem.getCreatedAt() != null) {
            holder.lostfoundCreateDateTextview.setText(lostItem.getCreatedAt());
        }
        if (lostItem.getDate() != null) {
            holder.lostfoundCreateLostInfoDateTextview.setText(lostItem.getDate());
        } else {
            holder.lostfoundCreateLostInfoDateTextview.setVisibility(View.GONE);
            holder.lostfoundCreateLostInfoKoreanTextview.setVisibility(View.GONE);
        }
    }

    /**
     * 1000 개 이상일시 999+으로 변환
     *
     * @param count 댓글 수를 입력을 받는다.
     * @return 댓글 수를 String으로 변형해서 반환해준다.
     */
    public String getCommentCountString(int count) {
        if (count < 0)
            return "(0)";
        if (count >= 1000)
            return "(999+)";
        return "(" + count + ")";
    }

    @Override
    public int getItemCount() {
        return lostItemArrayList.size();
    }
}
