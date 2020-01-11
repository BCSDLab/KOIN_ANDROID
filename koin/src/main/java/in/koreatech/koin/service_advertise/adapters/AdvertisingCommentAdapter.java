package in.koreatech.koin.service_advertise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.networks.entity.AdDetail;
import in.koreatech.koin.core.networks.entity.Comment;

/**
 * Created by hansol on 2020.1.9...
 */
public class AdvertisingCommentAdapter extends RecyclerView.Adapter<AdvertisingCommentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> adDetailComment;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.advertising_comment_item_nickname_textview)
        TextView nicknameTextview;
        @BindView(R.id.advertising_comment_item_time_textview)
        TextView timeTextview;
        @BindView(R.id.advertising_comment_item_contents_textview)
        TextView contentsTextview;
        @BindView(R.id.advertising_comment_item_fix_button)
        Button fitButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public AdvertisingCommentAdapter(Context context, ArrayList<Comment> adComment) {
        this.context = context;
        this.adDetailComment = adComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertising_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.nicknameTextview.setText(adDetailComment.get(0).authorNickname);
       holder.timeTextview.setText(adDetailComment.get(0).createDate);
       holder.contentsTextview.setText(adDetailComment.get(0).content);
       holder.fitButton.setOnClickListener(i->{
           //수정버튼 반영하기
       });

    }

    @Override
    public int getItemCount() {
        return 10;
    }


}
