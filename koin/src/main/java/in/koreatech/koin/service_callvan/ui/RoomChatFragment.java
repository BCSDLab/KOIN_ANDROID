package in.koreatech.koin.service_callvan.ui;

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
import in.koreatech.koin.core.constants.FirebaseDBConstant;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.networks.FirebaseDbManager;
import in.koreatech.koin.core.networks.entity.Message;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_callvan.adapters.MessageRecyclerAdapter;
import in.koreatech.koin.service_callvan.contracts.RoomChatContract;
import in.koreatech.koin.service_callvan.presenters.CallvanRoomChatPresenter;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public class RoomChatFragment extends CallvanBaseFragment implements RoomChatContract.View{
    private final String TAG = RoomChatFragment.class.getSimpleName();

    private Unbinder mUnbinder;

    /* firebase */
    private String mUid;
    private int mRoomUid;
    private ArrayList<Message> mMessageList = new ArrayList<>();
    private ArrayList<String> mMessageKeyList = new ArrayList<>();
    private DatabaseReference mMessageReference;
    private ValueEventListener mMessageListener;

    /* View Component */
    private View mView;

    private MessageRecyclerAdapter mMessageRecyclerAdapter;

//    @BindView(R.id.no_room_chat_frameLayout)
//    FrameLayout mNoRoomChatFrameLayout; //입장한 모임이 없을 경우 띄우는 화면
    @BindView(R.id.room_chat_frameLayout)
    FrameLayout mRoomChatFrameLayout; //입장한 모임이 있을 경우 띄우는 화면

    @BindView(R.id.room_chat_main_recyclerView)
    RecyclerView mRoomChatRecyclerView;

    @BindView(R.id.room_chat_main_input_text)
    EditText mInputEditText;
    @BindView(R.id.room_chat_main_button)
    Button mSendButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRoomUid = DefaultSharedPreferencesHelper.getInstance().loadCallvanRoomUid();
        if (mRoomUid > 0) {
            mUid = DefaultSharedPreferencesHelper.getInstance().loadUser().uid;
            if (FormValidatorUtil.validateStringIsEmpty(mUid)) {
                ToastUtil.makeShortToast(getActivity(), "채팅 정보를 일시적으로 불러올 수 없습니다");
                mMessageReference = null;
            } else {
                mMessageReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.ROOM_CHAT_MESSAGE).child(String.valueOf(mRoomUid));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.callvan_fragment_room_chat, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        init();

        if (FormValidatorUtil.validateStringIsEmpty(mUid)) {
//            mNoRoomChatFrameLayout.setVisibility(View.VISIBLE);
            mRoomChatFrameLayout.setVisibility(View.INVISIBLE);
        } else {
//            mNoRoomChatFrameLayout.setVisibility(View.INVISIBLE);
            mRoomChatFrameLayout.setVisibility(View.VISIBLE);
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

        mUid = DefaultSharedPreferencesHelper.getInstance().loadUser().uid;
        ValueEventListener messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMessageList.clear();
                mMessageKeyList.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Message msg = Message.parseSnapshot(child);
                    if (msg.isDeleted != null && !msg.isDeleted && msg.uid != null) {
                        mMessageList.add(msg);
                        mMessageKeyList.add(child.getKey());
                    }
                }

                updateUserInterface();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "load Message List:onCancelled", databaseError.toException());
            }
        };

        if (mMessageReference != null) {
            mMessageReference.addValueEventListener(messageListener);
        }
        mMessageListener = messageListener;
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
        if (mMessageListener != null) {
            if (mMessageReference != null) {
                mMessageReference.removeEventListener(mMessageListener);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
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
        mRoomChatRecyclerView.setHasFixedSize(true);
        mRoomChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //layout 설정
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
        if (!FormValidatorUtil.validateStringIsEmpty(mInputEditText.getText().toString())) {
            String meesageBody = mInputEditText.getText().toString();

            String userName = DefaultSharedPreferencesHelper.getInstance().loadUser().userName;

            String messageKey = FirebaseDbManager.createRoomChatMessage(String.valueOf(mRoomUid));

            FirebaseDbManager.updateMessage(mUid, messageKey, userName, meesageBody.trim(), String.valueOf(mRoomUid), false);

            mInputEditText.setText(null); //전송 후 edit text 초기화
        }

        mInputEditText.setText("");
    }

    @Override
    public void updateRoomChatView() {
        mRoomUid = DefaultSharedPreferencesHelper.getInstance().loadCallvanRoomUid();

        //참여한 방이 없을 경우
        if (mRoomUid <= 0) {
//            mNoRoomChatFrameLayout.setVisibility(View.VISIBLE);
            mRoomChatFrameLayout.setVisibility(View.INVISIBLE);
        }
        //참여한 방이 있을 경우
        else {
            mMessageReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDBConstant.getBaseChannel()).child(FirebaseDBConstant.CALL_VAN_SHARING).child(FirebaseDBConstant.ROOM_CHAT_MESSAGE).child(String.valueOf(mRoomUid));
            if (mMessageListener != null) {
                mMessageReference.removeEventListener(mMessageListener);
                mMessageReference.addValueEventListener(mMessageListener);
            }
//            mNoRoomChatFrameLayout.setVisibility(View.INVISIBLE);
            mRoomChatFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateUserInterface() {
        mMessageRecyclerAdapter = new MessageRecyclerAdapter(getActivity(), mMessageList, mMessageKeyList);
        mRoomChatRecyclerView.setAdapter(mMessageRecyclerAdapter);
        mRoomChatRecyclerView.getLayoutManager().scrollToPosition(mMessageList.size() - 1);
    }

}