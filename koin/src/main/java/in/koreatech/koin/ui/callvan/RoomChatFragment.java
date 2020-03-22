package in.koreatech.koin.ui.callvan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.FirebaseDBConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.firebase.FirebaseDbManager;
import in.koreatech.koin.data.network.entity.Message;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.callvan.adapter.MessageRecyclerAdapter;
import in.koreatech.koin.ui.callvan.presenter.RoomChatContract;
import in.koreatech.koin.ui.callvan.presenter.CallvanRoomChatPresenter;

public class RoomChatFragment extends CallvanBaseFragment implements RoomChatContract.View {
    private final String TAG = "RoomChatFragment";

    private Unbinder unbinder;

    /* firebase */
    private String uid;
    private int roouid;
    private ArrayList<Message> messageListroouid = new ArrayList<>();
    private ArrayList<String> messageKeyList = new ArrayList<>();
    private DatabaseReference messageReference;
    private ValueEventListener messageListroouidener;

    /* View Component */
    private View mView;

    private MessageRecyclerAdapter messageRecyclerAdapter;

    //    @BindView(R.id.no_room_chat_frameLayout)
//    FrameLayout mNoRoomChatFrameLayout; //입장한 모임이 없을 경우 띄우는 화면
    @BindView(R.id.room_chat_frameLayout)
    FrameLayout roomChatFrameLayout; //입장한 모임이 있을 경우 띄우는 화면

    @BindView(R.id.room_chat_main_recyclerView)
    RecyclerView roomChatRecyclerView;

    @BindView(R.id.room_chat_main_input_text)
    EditText inputEditText;
    @BindView(R.id.room_chat_main_button)
    Button sendButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        roouid = UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid();
        if (roouid > 0) {
            uid = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUid();
            if (FormValidatorUtil.validateStringIsEmpty(uid)) {
                ToastUtil.getInstance().makeShort("채팅 정보를 일시적으로 불러올 수 없습니다");
                messageReference = null;
            } else {
                messageReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.ROOM_CHAT_MESSAGE).child(String.valueOf(roouid));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.callvan_fragment_room_chat, container, false);
        unbinder = ButterKnife.bind(this, mView);
        init();

        if (FormValidatorUtil.validateStringIsEmpty(uid)) {
//            mNoRoomChatFrameLayout.setVisibility(View.VISIBLE);
            roomChatFrameLayout.setVisibility(View.INVISIBLE);
        } else {
//            mNoRoomChatFrameLayout.setVisibility(View.INVISIBLE);
            roomChatFrameLayout.setVisibility(View.VISIBLE);
        }

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        uid = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUid();
        ValueEventListener messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageListroouid.clear();
                messageKeyList.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Message msg = Message.parseSnapshot(child);
                    if (msg.getDeleted() != null && !msg.getDeleted() && msg.getUid() != null) {
                        messageListroouid.add(msg);
                        messageKeyList.add(child.getKey());
                    }
                }

                updateUserInterface();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "load Message List:onCancelled", databaseError.toException());
            }
        };

        if (messageReference != null) {
            messageReference.addValueEventListener(messageListener);
        }
        messageListroouidener = messageListener;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (messageListroouidener != null) {
            if (messageReference != null) {
                messageReference.removeEventListener(messageListroouidener);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void init() {
        roomChatRecyclerView.setHasFixedSize(true);
        roomChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //layout 설정
    }

    @Override
    public void setPresenter(CallvanRoomChatPresenter presenter) {

    }

    @OnEditorAction(R.id.room_chat_main_input_text)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onClickSendButton();
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    @OnClick(R.id.room_chat_main_button)
    public void onClickSendButton() {
        if (!FormValidatorUtil.validateStringIsEmpty(inputEditText.getText().toString())) {
            String meesageBody = inputEditText.getText().toString();

            String userName = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserName();

            String messageKey = FirebaseDbManager.createRoomChatMessage(String.valueOf(roouid));

            FirebaseDbManager.updateMessage(uid, messageKey, userName, meesageBody.trim(), String.valueOf(roouid), false);

            inputEditText.setText(null); //전송 후 edit text 초기화
        }

        inputEditText.setText("");
    }

    @Override
    public void updateRoomChatView() {
        roouid = UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid();

        //참여한 방이 없을 경우
        if (roouid <= 0) {
//            mNoRoomChatFrameLayout.setVisibility(View.VISIBLE);
            roomChatFrameLayout.setVisibility(View.INVISIBLE);
        }
        //참여한 방이 있을 경우
        else {
            messageReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.ROOM_CHAT_MESSAGE).child(String.valueOf(roouid));
            if (messageListroouidener != null) {
                messageReference.removeEventListener(messageListroouidener);
                messageReference.addValueEventListener(messageListroouidener);
            }
//            mNoRoomChatFrameLayout.setVisibility(View.INVISIBLE);
            roomChatFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateUserInterface() {
        messageRecyclerAdapter = new MessageRecyclerAdapter(getActivity(), messageListroouid, messageKeyList);
        roomChatRecyclerView.setAdapter(messageRecyclerAdapter);
        roomChatRecyclerView.getLayoutManager().scrollToPosition(messageListroouid.size() - 1);
    }

}