package in.koreatech.koin.ui.callvan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.CallvanRoom;
import in.koreatech.koin.data.network.interactor.CallvanRestInteractor;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.callvan.adapter.RoomRecyclerAdapter;
import in.koreatech.koin.ui.callvan.presenter.CallvanRoomContract;
import in.koreatech.koin.ui.callvan.presenter.CallvanRoomPresenter;


/**
 * Created by hyerim on 2018. 6. 17....
 */
public class RoomFragment extends CallvanBaseFragment implements CallvanRoomContract.View, RoomRecyclerAdapter.OnJoinButtonClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = RoomFragment.class.getSimpleName();

    private CallvanRoomPresenter mRoomPresenter;
    private Unbinder mUnbinder;
    private Context mContext;

    private boolean isJoinRoom = false;
    private CallvanRoom mJoinedRoom;
    private int mJoinedRoomUid;
    private ArrayList<CallvanRoom> mRoomArrayList = new ArrayList<>(); //DB의 Room 정보를 저장할 ArrayList

    /* View Component */
    private View mView;

    private RoomRecyclerAdapter mRoomRecyclerAdapter; // mRoomsRecyclerView에 데이터를 전해줄 RoomRecyclerAdapter
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager

    @BindView(R.id.room_frameLayout)
    ConstraintLayout mRoomLayout;
    @BindView(R.id.rooms_swiperefreshlayout)
    SwipeRefreshLayout mRoomSwiperefreshLayout;
    @BindView(R.id.rooms_recyclerview)
    RecyclerView mRoomRecyclerView; //CallvanSharingRoom의 List를 보여주는 RecyclerView

    @BindView(R.id.selected_start_place_layout)
    LinearLayout mSelectedStartPlaceLayout;
    @BindView(R.id.selected_start_place)
    TextView mSelectedStartPlaceTextView;
    @BindView(R.id.selected_end_place_layout)
    LinearLayout mSelectedEndPlaceLayout;
    @BindView(R.id.selected_end_place)
    TextView mSelectedEndPlaceTextView;

    @BindView(R.id.room_chat_frameLayout)
    ConstraintLayout mRoomChatLayout;
    @BindView(R.id.callvan_room_info_layout)
    LinearLayout mRoomInfoLayout;
    @BindView(R.id.info_starting_place_textview)
    TextView mInfoStartingPlaceTextView;
    @BindView(R.id.info_starting_data_and_time_textview)
    TextView mInfoStartingTimeTextView;
    @BindView(R.id.info_ending_place_textview)
    TextView mInfoEndingPlaceTextView;
    @BindView(R.id.info_people_textview)
    TextView mInfoPeopleCountTextView;
//    @BindView(R.id.room_info_exit_button)
//    RelativeLayout mInfoExitButton;
//    @BindView(R.id.info_refresh_button)
//    ImageView mInfoRefreshButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.callvan_fragment_room, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        mContext = this.getContext();

        initRoom();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        updateRoomView();

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

    private void initRoom() {
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRoomSwiperefreshLayout.setOnRefreshListener(this);

        mRoomRecyclerView.setHasFixedSize(true);
        mRoomRecyclerView.setLayoutManager(mLayoutManager); //layout 설정

        setPresenter(new CallvanRoomPresenter(this, new CallvanRestInteractor()));

    }

    @Override
    public void setPresenter(CallvanRoomPresenter presenter) {
        this.mRoomPresenter = presenter;
    }

    public void updateRoomView() {
        mJoinedRoomUid = UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid();
        Log.d(TAG, "join room uid" + mJoinedRoomUid);

        // 방에 참가중일 경우
        if (mJoinedRoomUid > 0) {
            mRoomLayout.setVisibility(View.INVISIBLE);
            mRoomInfoLayout.setVisibility(View.VISIBLE);

            mRoomPresenter.readRoom(mJoinedRoomUid);
        }
        // 방에 참가중이지 않을 경우
        else {
            mRoomLayout.setVisibility(View.VISIBLE);
            mRoomInfoLayout.setVisibility(View.INVISIBLE);

            updateRoomList();
        }
    }

    private void updateRoomList() {
        if (UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid() > 0) {
            return;
        }

        mRoomPresenter.readRoomList();
    }


    @Override
    public void onCallvanRoomListDataReceived(ArrayList<CallvanRoom> roomArrayList) {
        int scrollPosition = mRoomRecyclerView.computeVerticalScrollOffset();

        mRoomArrayList.clear();

        mRoomArrayList.addAll(roomArrayList);
        mRoomArrayList = sortRoomArrayList(mRoomArrayList);

        //필터 옵션이 default인 경우
        if (isFilterDefault()) {
            updateUserInterface(mRoomArrayList, scrollPosition);
        }
        //필터 옵션이 default 가 아닐 경우
        else {
            applySelectedFilter();  //recyclerView에 뿌릴 roomArrayList를 필터링 옵션에 따라 추림
            mRoomRecyclerView.scrollBy(0, scrollPosition);  //이전 스크롤 유지
        }
    }

    @Override
    public ArrayList<CallvanRoom> sortRoomArrayList(ArrayList<CallvanRoom> sortArrayList) {
        Collections.sort(sortArrayList, (room1, room2) -> room1.startingDate.compareTo(room2.startingDate));

        return sortArrayList;
    }

    @Override
    public void updateUserInterface(ArrayList<CallvanRoom> renderingList, int scrollPosition) {
        mRoomRecyclerAdapter = new RoomRecyclerAdapter(getContext(), renderingList);
        mRoomRecyclerAdapter.setCustomOnClickListener(this);
        mRoomRecyclerView.setAdapter(mRoomRecyclerAdapter); //adapter 설정
        mRoomRecyclerAdapter.notifyDataSetChanged();
        if (scrollPosition != -1) {
            mRoomRecyclerView.scrollBy(0, scrollPosition);  //이전 스크롤 위치를 유지
        }
    }

    @Override
    public void onCallvanRoomDataReceived(CallvanRoom room) {
        updateRoomInfo(room);
    }

    private void updateRoomInfo(CallvanRoom room) {
        if (room == null) {
            return;
        }

        mRoomLayout.setVisibility(View.INVISIBLE);
        mRoomInfoLayout.setVisibility(View.VISIBLE);

        mJoinedRoom = room;
        mInfoStartingPlaceTextView.setText(room.startingPlace);
        mInfoStartingTimeTextView.setText(room.startingDate);
        mInfoEndingPlaceTextView.setText(room.endingPlace);
        mInfoPeopleCountTextView.setText(room.currentPeople + "명/" + room.maximumPeople + "명");

        UserInfoSharedPreferencesHelper.getInstance().saveCallvanRoomUid(room.uid);
    }

    @Override
    public void onClickJoinButton(CallvanRoom room) {
        mRoomPresenter.updateIncreaseCurrentPeopleCount(room.uid);
    }

    @Override
    public void onCallvanRoomIncreasePeopleReceived(int roomUid) {
        mRoomPresenter.readRoom(roomUid);
    }

//    @OnClick(R.id.info_refresh_button)
//    public void onClickRoomInfoRefreshButton() {
//        mRoomPresenter.readRoom(mJoinedRoom.uid);
//    }

//    @OnClick(R.id.room_info_exit_button)
//    public void onClickRoomInfoExitButton() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.KAPDialog);
//        builder.setTitle("모임 나가기");
//        builder.setMessage("정말 모임에서 나가시겠습니까?");
//
//        builder.setPositiveButton("확인",
//                (dialog, which) -> exitJoinedRoom(mJoinedRoom.uid));
//        builder.setNegativeButton("취소",
//                (dialog, which) -> dialog.dismiss());
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void exitJoinedRoom(int roomUid) {
        mRoomPresenter.updateDecreaseCurrentPeopleCount(roomUid);
    }

    @Override
    public void onCallvanRoomDecreasePeopleReceived(boolean isSuccess) {
        if (isSuccess) {
            //방에서 나올 경우 이전에 설정해둔 필터를 지움
            UserInfoSharedPreferencesHelper.getInstance().removeCallvanRoomUid();
            mSelectedStartPlaceTextView.setText("출발지");
            mSelectedEndPlaceTextView.setText("목적지");
//            ((CallvanActivity) getActivity()).refreshRoomFragment();
        } else {
            showMessage("실패하였습니다.");
        }
    }

    @OnClick(R.id.selected_start_place_layout)
    public void onClickSelectedStartPlaceLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("출발지 필터 선택")
                .setItems(R.array.filter_place, (dialog, which) -> {
                    String[] list = getActivity().getResources().getStringArray(
                            R.array.filter_place);

                    if (which == 0) {
                        mSelectedStartPlaceTextView.setText("출발지");
                    } else {
                        mSelectedStartPlaceTextView.setText(list[which]);
                    }
                    applySelectedFilter();
                });
        builder.show();
    }

    @OnClick(R.id.selected_end_place_layout)
    public void onClickSelectedEndPlaceLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("목적지 필터 선택")
                .setItems(R.array.filter_place, (dialog, which) -> {
                    String[] list = getActivity().getResources().getStringArray(
                            R.array.filter_place);

                    if (which == 0) {
                        mSelectedEndPlaceTextView.setText("목적지");
                    } else {
                        mSelectedEndPlaceTextView.setText(list[which]);
                    }
                    applySelectedFilter();

                });
        builder.show();
    }

    private void applySelectedFilter() {
        ArrayList<CallvanRoom> filteredRoomArrayList = new ArrayList<>();

        String startFilterText = mSelectedStartPlaceTextView.getText().toString();
        String endFilterText = mSelectedEndPlaceTextView.getText().toString();

        if (startFilterText.compareTo(endFilterText) == 0) {
            ToastUtil.getInstance().makeShortToast("출발지와 목적지가 같습니다");
            mSelectedStartPlaceTextView.setText("출발지");
            mSelectedEndPlaceTextView.setText("목적지");
            updateUserInterface(mRoomArrayList, -1);
            return;
        }


        // 1) 출발지와 목적지가 둘 다 전체인 경우
        if (startFilterText.compareTo("출발지") == 0 && endFilterText.compareTo("목적지") == 0) {
            updateUserInterface(mRoomArrayList, -1);
        }
        // 2) 출발지만 설정된 경우
        else if (startFilterText.compareTo("출발지") != 0 && endFilterText.compareTo("목적지") == 0) {
            for (CallvanRoom r : mRoomArrayList) {
                if (r.startingPlace.compareTo(startFilterText) == 0) {
                    filteredRoomArrayList.add(r);
                }
            }
            updateUserInterface(filteredRoomArrayList, -1);
        }
        // 3) 목적지만 설정된 경우
        else if (startFilterText.compareTo("출발지") == 0 && endFilterText.compareTo("목적지") != 0) {
            for (CallvanRoom r : mRoomArrayList) {
                if (r.endingPlace.compareTo(endFilterText) == 0) {
                    filteredRoomArrayList.add(r);
                }
            }
            updateUserInterface(filteredRoomArrayList, -1);
        }
        // 4) 둘다 설정된 경우
        else if (startFilterText.compareTo("출발지") != 0 && endFilterText.compareTo("목적지") != 0) {
            for (CallvanRoom r : mRoomArrayList) {
                if (r.startingPlace.compareTo(startFilterText) == 0 && r.endingPlace.compareTo(
                        endFilterText) == 0) {
                    filteredRoomArrayList.add(r);
                }
            }
            updateUserInterface(filteredRoomArrayList, -1);
        }
    }

    //필터가 default인지 판별하는 메서드
    private boolean isFilterDefault() {
        return mSelectedStartPlaceTextView.getText().toString().compareTo("출발지") == 0
                && mSelectedEndPlaceTextView.getText().toString().compareTo("도착지") == 0;
    }

    @Override
    public void onRefresh() {
        // 새로고침 코드
        updateRoomView();

        mRoomSwiperefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShortToast(R.string.server_failed);
    }

}