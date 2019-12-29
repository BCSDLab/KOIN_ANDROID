package in.koreatech.koin.service_callvan.adapters;

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
import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.networks.entity.Message;

/**
 * Created by hyerim on 2018. 6. 18....
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = MessageRecyclerAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_INCOMING = 0; //받은 메시지
    private static final int VIEW_TYPE_OUTGOING = 1; //보낸 메시지
    private static final int VIEW_TYPE_NOTICE = 2;

    private Context mContext;
    private ArrayList<Message> mMessageList; //Message List
    private ArrayList<String> mMessageKeyList;
    private String mUid; //사용자 uid

    //받은 메시지
    public class IncomingViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.message_user_name_incoming)
        TextView mUserName;
        @Nullable
        @BindView(R.id.message_body_incoming)
        TextView mMessageBody;
        @Nullable
        @BindView(R.id.create_time_incoming)
        TextView mCreateTime;

        public IncomingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //보낸 메시지
    public class OutgoingViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.message_user_name_outgoing)
        TextView mUserName;
        @Nullable
        @BindView(R.id.message_body_outgoing)
        TextView mMessageBody;
        @Nullable
        @BindView(R.id.create_time_outgoing)
        TextView mCreateTime;


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
        this.mContext = context;
        this.mMessageList = messageList;
        this.mMessageKeyList = messageKeyList;

        mUid = UserInfoSharedPreferencesHelper.getInstance().loadUser().uid;
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
        final Message message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_INCOMING:
                IncomingViewHolder incomingViewHolder = (IncomingViewHolder) holder;
                incomingViewHolder.mUserName.setText(message.userName);
                incomingViewHolder.mMessageBody.setText(message.message);
                incomingViewHolder.mCreateTime.setText(message.createDate);
                break;

            case VIEW_TYPE_OUTGOING:
                OutgoingViewHolder outgoingViewHolder = (OutgoingViewHolder) holder;
                outgoingViewHolder.mUserName.setText(message.userName);
                outgoingViewHolder.mMessageBody.setText(message.message);
                outgoingViewHolder.mCreateTime.setText(message.createDate);
                break;

            case VIEW_TYPE_NOTICE:
                NoticeMessageViewHolder noticeMessageViewHolder = (NoticeMessageViewHolder) holder;
                noticeMessageViewHolder.mRoomNoticeMessage.setText(message.message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return classifyViewType(position);
    }

    private int classifyViewType(int position) {
        if (mMessageList.get(position).isNotice != null && mMessageList.get(position).isNotice) {
            return VIEW_TYPE_NOTICE;
        } else if (mUid.equals(mMessageList.get(position).uid)) {
            return VIEW_TYPE_OUTGOING;
        } else {
            return VIEW_TYPE_INCOMING;
        }
    }
}