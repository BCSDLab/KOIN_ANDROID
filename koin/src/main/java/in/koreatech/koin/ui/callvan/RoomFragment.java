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

public class RoomFragment extends CallvanBaseFragment implements CallvanRoomContract.View, RoomRecyclerAdapter.OnJoinButtonClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "RoomFragment";

    private CallvanRoomPresenter roomPresenter;
    private Unbinder unbinder;
    private Context context;

    private boolean isJoinRoom = false;
    private CallvanRoom joinedRoom;
    private int joinedRoomUid;
    private ArrayList<CallvanRoom> roomArrayList = new ArrayList<>(); //DB의 Room 정보를 저장할 ArrayList

    /* View Component */
    private View view;

    private RoomRecyclerAdapter roomRecyclerAdapterview; // mRoomsRecyclerView에 데이터를 전해줄 RoomRecyclerAdapter
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager

    @BindView(R.id.room_frameLayout)
    ConstraintLayout roomLayout;
    @BindView(R.id.rooms_swiperefreshlayout)
    SwipeRefreshLayout roomSwiperefreshLayout;
    @BindView(R.id.rooms_recyclerview)
    RecyclerView roomRecyclerView; //CallvanSharingRoom의 List를 보여주는 RecyclerView

    @BindView(R.id.selected_start_place_layout)
    LinearLayout selectedStartPlaceLayout;
    @BindView(R.id.selected_start_place)
    TextView selectedStartPlaceTextView;
    @BindView(R.id.selected_end_place_layout)
    LinearLayout selectedEndPlaceLayout;
    @BindView(R.id.selected_end_place)
    TextView selectedEndPlaceTextView;

    @BindView(R.id.room_chat_frameLayout)
    ConstraintLayout roomChatLayout;
    @BindView(R.id.callvan_room_info_layout)
    LinearLayout roomInfoLayout;
    @BindView(R.id.info_starting_place_textview)
    TextView infoStartingPlaceTextView;
    @BindView(R.id.info_starting_data_and_time_textview)
    TextView infoStartingTimeTextView;
    @BindView(R.id.info_ending_place_textview)
    TextView infoEndingPlaceTextView;
    @BindView(R.id.info_people_textview)
    TextView infoPeopleCountTextView;

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
        view = inflater.inflate(R.layout.callvan_fragment_room, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = this.getContext();

        initRoom();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        updateRooview();

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

    private void initRoom() {
        layoutManager = new LinearLayoutManager(getActivity());

        roomSwiperefreshLayout.setOnRefreshListener(this);

        roomRecyclerView.setHasFixedSize(true);
        roomRecyclerView.setLayoutManager(layoutManager); //layout 설정

        setPresenter(new CallvanRoomPresenter(this, new CallvanRestInteractor()));

    }

    @Override
    public void setPresenter(CallvanRoomPresenter presenter) {
        this.roomPresenter = presenter;
    }

    public void updateRooview() {
        joinedRoomUid = UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid();
        Log.d(TAG, "join room uid" + joinedRoomUid);

        // 방에 참가중일 경우
        if (joinedRoomUid > 0) {
            roomLayout.setVisibility(View.INVISIBLE);
            roomInfoLayout.setVisibility(View.VISIBLE);

            roomPresenter.readRoom(joinedRoomUid);
        }
        // 방에 참가중이지 않을 경우
        else {
            roomLayout.setVisibility(View.VISIBLE);
            roomInfoLayout.setVisibility(View.INVISIBLE);

            updateRoomList();
        }
    }

    private void updateRoomList() {
        if (UserInfoSharedPreferencesHelper.getInstance().loadCallvanRoomUid() > 0) {
            return;
        }

        roomPresenter.readRoomList();
    }


    @Override
    public void onCallvanRoomListDataReceived(ArrayList<CallvanRoom> roomArrayList) {
        int scrollPosition = roomRecyclerView.computeVerticalScrollOffset();

        roomArrayList.clear();

        roomArrayList.addAll(roomArrayList);
        roomArrayList = sortRoomArrayList(roomArrayList);

        //필터 옵션이 default인 경우
        if (isFilterDefault()) {
            updateUserInterface(roomArrayList, scrollPosition);
        }
        //필터 옵션이 default 가 아닐 경우
        else {
            applySelectedFilter();  //recyclerView에 뿌릴 roomArrayList를 필터링 옵션에 따라 추림
            roomRecyclerView.scrollBy(0, scrollPosition);  //이전 스크롤 유지
        }
    }

    @Override
    public ArrayList<CallvanRoom> sortRoomArrayList(ArrayList<CallvanRoom> sortArrayList) {
        Collections.sort(sortArrayList, (room1, room2) -> room1.getStartingDate().compareTo(room2.getStartingDate()));

        return sortArrayList;
    }

    @Override
    public void updateUserInterface(ArrayList<CallvanRoom> renderingList, int scrollPosition) {
        roomRecyclerAdapterview = new RoomRecyclerAdapter(getContext(), renderingList);
        roomRecyclerAdapterview.setCustomOnClickListener(this);
        roomRecyclerView.setAdapter(roomRecyclerAdapterview); //adapter 설정
        roomRecyclerAdapterview.notifyDataSetChanged();
        if (scrollPosition != -1) {
            roomRecyclerView.scrollBy(0, scrollPosition);  //이전 스크롤 위치를 유지
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

        roomLayout.setVisibility(View.INVISIBLE);
        roomInfoLayout.setVisibility(View.VISIBLE);

        joinedRoom = room;
        infoStartingPlaceTextView.setText(room.getStartingPlace());
        infoStartingTimeTextView.setText(room.getStartingDate());
        infoEndingPlaceTextView.setText(room.getEndingPlace());
        infoPeopleCountTextView.setText(room.getCurrentPeople() + "명/" + room.getMaximumPeople() + "명");

        UserInfoSharedPreferencesHelper.getInstance().saveCallvanRoomUid(room.getUid());
    }

    @Override
    public void onClickJoinButton(CallvanRoom room) {
        roomPresenter.updateIncreaseCurrentPeopleCount(room.getUid());
    }

    @Override
    public void onCallvanRoomIncreasePeopleReceived(int roomUid) {
        roomPresenter.readRoom(roomUid);
    }

//    @OnClick(R.id.info_refresh_button)
//    public void onClickRoomInfoRefreshButton() {
//        roomPresenter.readRoom(joinedRoom.uid);
//    }

//    @OnClick(R.id.room_info_exit_button)
//    public void onClickRoomInfoExitButton() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.KAPDialog);
//        builder.setTitle("모임 나가기");
//        builder.setMessage("정말 모임에서 나가시겠습니까?");
//
//        builder.setPositiveButton("확인",
//                (dialog, which) -> exitJoinedRoom(joinedRoom.uid));
//        builder.setNegativeButton("취소",
//                (dialog, which) -> dialog.dismiss());
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void exitJoinedRoom(int roomUid) {
        roomPresenter.updateDecreaseCurrentPeopleCount(roomUid);
    }

    @Override
    public void onCallvanRoomDecreasePeopleReceived(boolean isSuccess) {
        if (isSuccess) {
            //방에서 나올 경우 이전에 설정해둔 필터를 지움
            UserInfoSharedPreferencesHelper.getInstance().removeCallvanRoomUid();
            selectedStartPlaceTextView.setText("출발지");
            selectedEndPlaceTextView.setText("목적지");
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
                        selectedStartPlaceTextView.setText("출발지");
                    } else {
                        selectedStartPlaceTextView.setText(list[which]);
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
                        selectedEndPlaceTextView.setText("목적지");
                    } else {
                        selectedEndPlaceTextView.setText(list[which]);
                    }
                    applySelectedFilter();

                });
        builder.show();
    }

    private void applySelectedFilter() {
        ArrayList<CallvanRoom> filteredRoomArrayList = new ArrayList<>();

        String startFilterText = selectedStartPlaceTextView.getText().toString();
        String endFilterText = selectedEndPlaceTextView.getText().toString();

        if (startFilterText.compareTo(endFilterText) == 0) {
            ToastUtil.getInstance().makeShort("출발지와 목적지가 같습니다");
            selectedStartPlaceTextView.setText("출발지");
            selectedEndPlaceTextView.setText("목적지");
            updateUserInterface(roomArrayList, -1);
            return;
        }


        // 1) 출발지와 목적지가 둘 다 전체인 경우
        if (startFilterText.compareTo("출발지") == 0 && endFilterText.compareTo("목적지") == 0) {
            updateUserInterface(roomArrayList, -1);
        }
        // 2) 출발지만 설정된 경우
        else if (startFilterText.compareTo("출발지") != 0 && endFilterText.compareTo("목적지") == 0) {
            for (CallvanRoom r : roomArrayList) {
                if (r.getStartingPlace().compareTo(startFilterText) == 0) {
                    filteredRoomArrayList.add(r);
                }
            }
            updateUserInterface(filteredRoomArrayList, -1);
        }
        // 3) 목적지만 설정된 경우
        else if (startFilterText.compareTo("출발지") == 0 && endFilterText.compareTo("목적지") != 0) {
            for (CallvanRoom r : roomArrayList) {
                if (r.getEndingPlace().compareTo(endFilterText) == 0) {
                    filteredRoomArrayList.add(r);
                }
            }
            updateUserInterface(filteredRoomArrayList, -1);
        }
        // 4) 둘다 설정된 경우
        else if (startFilterText.compareTo("출발지") != 0 && endFilterText.compareTo("목적지") != 0) {
            for (CallvanRoom r : roomArrayList) {
                if (r.getStartingPlace().compareTo(startFilterText) == 0 && r.getEndingPlace().compareTo(
                        endFilterText) == 0) {
                    filteredRoomArrayList.add(r);
                }
            }
            updateUserInterface(filteredRoomArrayList, -1);
        }
    }

    //필터가 default인지 판별하는 메서드
    private boolean isFilterDefault() {
        return selectedStartPlaceTextView.getText().toString().compareTo("출발지") == 0
                && selectedEndPlaceTextView.getText().toString().compareTo("도착지") == 0;
    }

    @Override
    public void onRefresh() {
        // 새로고침 코드
        updateRooview();

        roomSwiperefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
    }

}