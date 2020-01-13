package in.koreatech.koin.ui.callvan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.koreatech.koin.R;
import in.koreatech.koin.ui.callvan.presenter.CompanyContract;
import in.koreatech.koin.core.helper.RecyclerClickListener;
import in.koreatech.koin.core.helper.RecyclerViewClickListener;
import in.koreatech.koin.data.network.entity.Company;
import in.koreatech.koin.data.network.interactor.CallvanRestInteractor;
import in.koreatech.koin.ui.callvan.presenter.CompanyPresenter;
import in.koreatech.koin.ui.callvan.adapter.CompanyRecyclerAdapter;

/**
 * Created by hyerim on 2018. 6. 17....
 */
public class CompanyFragment extends CallvanBaseFragment implements CompanyContract.View, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "CallvanBaseFragment";
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;  //User Permission Request Code

    private Unbinder unbinder;
    private CompanyPresenter companyPresenter;
    private ArrayList<Company> companyArrayList = new ArrayList<>();    //DB의 연락처 정보를 저장할 ArrayList

    /* Adapter */
    private CompanyRecyclerAdapter companyRecyclerAdapter; // contactRecyclerView에 데이터를 전해줄 ContactRecyclerAdapter

    /* View Component */
    private View view;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager

    @BindView(R.id.company_recyclerview)
    RecyclerView contactRecyclerView; //Contact fragment의 contact List를 보여주는 RecyclerView
    @BindView(R.id.company_swiperefreshlayout)
    SwipeRefreshLayout contactSwipeRefreshLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermission(); //사용자에게 권한을 요청 (Call intent)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.callvan_fragment_company, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        UserCallStateCheck();
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


    private void init() {
        companyRecyclerAdapter = new CompanyRecyclerAdapter(getActivity(), companyArrayList);
        layoutManager = new LinearLayoutManager(getActivity());

        contactSwipeRefreshLayout.setOnRefreshListener(this);

        contactRecyclerView.setHasFixedSize(true);
        contactRecyclerView.setLayoutManager(layoutManager); //layout 설정
        contactRecyclerView.setAdapter(companyRecyclerAdapter); //adapter 설정
        contactRecyclerView.addOnItemTouchListener(recyclerItemtouchListener); //itemTouchListner 설정

        setPresenter(new CompanyPresenter(this, new CallvanRestInteractor()));

        companyPresenter.getCompanyList();
    }

    @Override
    public void setPresenter(CompanyPresenter presenter) {
        this.companyPresenter = presenter;
    }

    @Override
    public void onCallvanCompaniesDataReceived(ArrayList<Company> companyArrayList) {
        companyArrayList.clear();

        companyArrayList.addAll(companyArrayList);

        if (!companyArrayList.isEmpty()) {
            sortCompanyListDescendingByCallCount(companyArrayList); //contactArrayList를 Call Count 순으로 정렬한다
            updateUserInterface();
        }
    }


    /* contactList를 call count로 내림차순 정렬한다 */
    @Override
    public ArrayList<Company> sortCompanyListDescendingByCallCount(ArrayList<Company> companyArrayList) {
        Collections.sort(companyArrayList, (prev, next) -> {
            long prevCallCount = prev.callCount;
            long nextCallCount = next.callCount;

            //내림차순으로 정렬
            return Long.compare(nextCallCount, prevCallCount);
        });

        return companyArrayList;
    }


    /* 유저인터페이스를 업데이트 하는 메서드 */
    @Override
    public void updateUserInterface() {
        companyRecyclerAdapter.notifyDataSetChanged();
    }

    //recyclerview item touch listener
    private RecyclerClickListener recyclerItemtouchListener = new RecyclerClickListener(getActivity(), contactRecyclerView, new RecyclerViewClickListener() {
        @Override
        public void onClick(View view, final int position) {
            showContactDialog(position);
        }

        @Override
        public void onLongClick(View view, int position) {
        }

    });

    @Override
    public void showContactDialog(final int clickedPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.KAPDialog);

        builder.setMessage(companyArrayList.get(clickedPosition).name + "\n\n" + companyArrayList.get(clickedPosition).phone);

        builder.setPositiveButton("통화",
                (dialog, which) -> {
                    onClickCallButton(clickedPosition);    //call intent 수행
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClickCallButton(int clickedPosition) {
        String callNumber = companyArrayList.get(clickedPosition).phone; //선택된 Row의 contact의 callvanPhoneNum 값을 가져옴
        startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + callNumber)));

    }

    /* 사용자가 전화를 했는지를 판별하는 메서드  */
    @Override
    public void UserCallStateCheck() {

    }

    @Override
    public void onCallvanCompanyDataReceived(Company company) {
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
    public void onRefresh() {
        // 새로고침 코드
        companyPresenter.getCompanyList();
        contactSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {//해당 권한이 없을 경우 실행
            //설명 dialog 표시
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {//사용자가 권한 거부시 재 요청
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {//최초 권한 요청
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
    }
}
