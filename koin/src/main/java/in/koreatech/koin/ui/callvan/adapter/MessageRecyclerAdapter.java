package in.koreatech.koin.ui.callvan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.Message;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageRecyclerAdapter";
    private static final int VIEW_TYPE_INCOMING = 0; //받은 메시지
    private static final int VIEW_TYPE_OUTGOING = 1; //보낸 메시지
    private static final int VIEW_TYPE_NOTICE = 2;

    private Context context;
    private ArrayList<Message> messageList; //Message List
    private ArrayList<String> messageKeyList;
    private String uid; //사용자 uid

    //받은 메시지
    public class IncomingViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.message_user_name_incoming)
        TextView userName;
        @Nullable
        @BindView(R.id.message_body_incoming)
        TextView messageBody;
        @Nullable
        @BindView(R.id.create_time_incoming)
        TextView createTime;

        public IncomingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //보낸 메시지
    public class OutgoingViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.message_user_name_outgoing)
        TextView userName;
        @Nullable
        @BindView(R.id.message_body_outgoing)
        TextView messageBody;
        @Nullable
        @BindView(R.id.create_time_outgoing)
        TextView createTime;


        public OutgoingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //입/퇴장 메시지
    public class NoticeMessageViewHolder extends RecyclerView.ViewHolder {
        TextView mRoomNoticeMessage;

        public NoticeMessageViewHolder(View itemView) {
            super(itemView);
            mRoomNoticeMessage = itemView.findViewById(R.id.message_body_notice);
        }
    }

    public MessageRecyclerAdapter(Context context, ArrayList<Message> messageList, ArrayList<String> messageKeyList) {
        this.context = context;
        this.messageList = messageList;
        this.messageKeyList = messageKeyList;

        uid = UserInfoSharedPreferencesHelper.getInstance().loadUser().uid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_INCOMING: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item_incoming_message, parent, false);
                return new IncomingViewHolder(itemView);
            }
            case VIEW_TYPE_OUTGOING: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item_outgoing_message, parent, false);
                return new OutgoingViewHolder(itemView);
            }
            default: {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item_notice_message, parent, false);
                return new NoticeMessageViewHolder(itemView);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Message message = messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_INCOMING:
                IncomingViewHolder incomingViewHolder = (IncomingViewHolder) holder;
                incomingViewHolder.userName.setText(message.userName);
                incomingViewHolder.messageBody.setText(message.message);
                incomingViewHolder.createTime.setText(message.createDate);
                break;

            case VIEW_TYPE_OUTGOING:
                OutgoingViewHolder outgoingViewHolder = (OutgoingViewHolder) holder;
                outgoingViewHolder.userName.setText(message.userName);
                outgoingViewHolder.messageBody.setText(message.message);
                outgoingViewHolder.createTime.setText(message.createDate);
                break;

            case VIEW_TYPE_NOTICE:
                NoticeMessageViewHolder noticeMessageViewHolder = (NoticeMessageViewHolder) holder;
                noticeMessageViewHolder.mRoomNoticeMessage.setText(message.message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return classifyViewType(position);
    }

    private int classifyViewType(int position) {
        if (messageList.get(position).isNotice != null && messageList.get(position).isNotice) {
            return VIEW_TYPE_NOTICE;
        } else if (uid.equals(messageList.get(position).uid)) {
            return VIEW_TYPE_OUTGOING;
        } else {
            return VIEW_TYPE_INCOMING;
        }
    }
}