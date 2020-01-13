package in.koreatech.koin.ui.timetable;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.core.constant.AuthorizeConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.helper.RecyclerViewClickListener;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.core.helper.UserLockBottomSheetBehavior;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.util.LogUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.util.DepartmentCode;
import in.koreatech.koin.util.TimeDuplicateCheckUtil;
import in.koreatech.koin.ui.timetable.presenter.MajorDialogListener;
import in.koreatech.koin.ui.timetable.presenter.TimetableAnonymousContract;
import in.koreatech.koin.ui.timetable.adapter.TimetableRecyclerAdapter;
import in.koreatech.koin.ui.timetable.presenter.TimetableAnonymousPresenter;
import in.koreatech.koin.util.FileUtil;
import in.koreatech.koin.util.ScreenshotUtil;

import static in.koreatech.koin.util.LectureFilterUtil.getClikckedUtil;
import static in.koreatech.koin.util.LectureFilterUtil.getFilterUtil;
import static in.koreatech.koin.util.SeparateTime.getSpertateTimeToString;
import static in.koreatech.koin.util.TimeDuplicateCheckUtil.duplicateScheduleTostring;


public class TimetableAnonymousActivity extends KoinNavigationDrawerActivity implements TimetableAnonymousContract.View, TimetableSelectAnonymousMajorDialog.OnCLickedDialogItemListener, RecyclerViewClickListener {
    public static final String TAG = "TimetableAnonymousActivity";
    public static final int MY_REQUEST_CODE = 1;
    public static final int MAX_ITEM_LOAD = 40;
    public static final int LOAD_TIME_MS = 500;

    public static int select = -1;          //TimetableSelectAnonymousMajorDialog에서 선택했던것을 기억하는 변수
    @BindView(R.id.timetable_timetableview)
    TimetableView timetableView;
    @BindView(R.id.timetable_add_schedule_bottom_sheet)
    LinearLayout bottomsheet;
    @BindView(R.id.timetable_container_relativelayout)
    RelativeLayout timeTableContainerRelativeLayout;
    @BindView(R.id.timetable_search_recyclerview)
    RecyclerView timetableSearchRecyclerview;
    @BindView(R.id.timetable_add_schedule_bottom_sheet_center_textview)
    TextView timetableAddTopTextView;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet)
    LinearLayout detailBottomsheet;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_center_textview)
    TextView timetableDetailTopTextView;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_class_title_textview)
    TextView timetableDetailClassTitleTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_class_detail_textview)
    TextView timetableDetailinformationTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_left_textview)
    TextView timetableDetailLeftTextview;
    @BindView(R.id.timetable_detail_schedule_bottom_sheet_right_textview)
    TextView timetableDetailRightTextview;
    @BindView(R.id.timetable_add_schedule_search_edittext)
    TextView timetableAddScheduleSearchEdittext;
    @BindView(R.id.timetable_scrollview)
    ScrollView timeTableScrollview;
    @BindView(R.id.table_header)
    TableLayout timeTableHeader;

    private Context context;
    private UserLockBottomSheetBehavior bottomSheetBehavior;
    private UserLockBottomSheetBehavior bottomSheetDetailBehavior;
    private boolean isBottomSheetOpen;
    private boolean isBottomDetailSheetOpen;
    private TimetableAnonymousPresenter timetablePresenter;
    private int categoryNumber;

    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private ArrayList<TimeTableItem> timeTableArrayList;
    private ArrayList<TimeTableItem> timeTableEditArrayList;
    private ArrayList<Lecture> totalLectureArrayList;
    private ArrayList<Lecture> selectedLectureArrayList;
    private ArrayList<Lecture> selectedLectureSeperateArrayList;
    private int selectedLectureSeperateClickIndex = -1;

    private RecyclerView.ViewHolder viewHolder;
    private TimetableRecyclerAdapter timetableRecyclerAdapter;
    private TimetableSelectAnonymousMajorDialog timetableSelectAnonymousMajorDialog;
    private boolean isLoading;
    private DepartmentCode selectedDepartmentCode;
    private int blockId;

    private String semester = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_activity_main);
        context = this;
        ButterKnife.bind(this);
        init();
    }


    public void init() {
        isLoading = false;
        selectedDepartmentCode = DepartmentCode.DEPARTMENT_CODE_0;
        setPresenter(new TimetableAnonymousPresenter(this));
        categoryNumber = -1;
        select = -1;
        totalLectureArrayList = new ArrayList<>();
        selectedLectureArrayList = new ArrayList<>();
        timeTableArrayList = new ArrayList<>();
        selectedLectureSeperateArrayList = new ArrayList<>();
        bottomSheetBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(bottomsheet);
        bottomSheetDetailBehavior = (UserLockBottomSheetBehavior) BottomSheetBehavior.from(detailBottomsheet);
        bottomsheet.setNestedScrollingEnabled(false);
        closeBottomSearchSheetButton();
        isBottomSheetOpen = false;
        layoutManager = new LinearLayoutManager(this);
        initBottomSheet();
        initDetailBottomSheet();
        initSearchRecyclerview();
        initScrollListener();
        initSearchEditText();
        timetableView.setOnStickerSelectEventListener(this::onClickedSticker);
//        tempInitSticker();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkIsAnonoymous()) {
            goToTimetableActivty();
        }
        TimeTableSharedPreferencesHelper.getInstance().init(getApplicationContext());
        timetablePresenter.getTimeTableVersion();
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    public boolean checkIsAnonoymous() {
        return UserInfoSharedPreferencesHelper.getInstance().checkAuthorize() == AuthorizeConstant.ANONYMOUS;
    }

    public void askSaveToImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

    }


    public boolean checkStoragePermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED))
                return false;
            else
                return true;
        } else
            return true;
    }

    public void saveTimeTableViewInBMP(String semester) {
        File saveImageFile;
        removeAllCheckSticker();
//        timeTableScrollview.smoothScrollBy(0, 0);
        Bitmap bitmap = ScreenshotUtil.getInstance().takeTimeTableScreenShot(timeTableScrollview, timeTableHeader);
        try {
            if (bitmap != null) {
                String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
                String path = Environment.getExternalStorageDirectory().toString() + "/Pictures/" + semester + "_" + timeStamp + ".png";
                File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/");
                myDir.mkdirs();
                saveImageFile = FileUtil.getInstance().storeBitmap(bitmap, path);
                if (saveImageFile != null) {
                    Intent mediaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaIntent.setData(Uri.fromFile(saveImageFile));
                    sendBroadcast(mediaIntent);
                    Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "저장에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void initSearchEditText() {
        timetableAddScheduleSearchEdittext.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                onClickedSearchButton();
                return true;
            }
            return false;
        });
    }

    public void closeBottomSearchSheetButton() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        hideKeyboard(TimetableAnonymousActivity.this);
        clearFilterData();
    }

    @OnClick(R.id.timetable_add_schedule_bottom_sheet_left_textview)
    public void clickedSaveTimetable() {
        Handler handler = new Handler();
        Runnable runnable;
        if (checkStoragePermisson()) {
            runnable = () -> {
               showProgressDialog(R.string.saving);
            };
            handler.post(runnable);
            handler.postDelayed(() -> {
                saveTimeTableViewInBMP(semester);
                hideProgressDialog();
            }, 2000);

        } else {
            ToastUtil.getInstance().makeShort("권한이 필요합니다.");
            askSaveToImagePermission();
        }
    }

    public void initSearchRecyclerview() {
        timetableSearchRecyclerview.setLayoutManager(layoutManager);
        timetableRecyclerAdapter = new TimetableRecyclerAdapter(context, selectedLectureSeperateArrayList);
        timetableSearchRecyclerview.setHasFixedSize(true);
        timetableSearchRecyclerview.setNestedScrollingEnabled(false);
        timetableRecyclerAdapter.setRecyclerViewClickListener(this);
        timetableSearchRecyclerview.setAdapter(timetableRecyclerAdapter);
    }


    public void initBottomSheet() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                int height = bottomSheet.getHeight();
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        timetableView.setMarginBottom(bottomSheet.getHeight());
                        isBottomSheetOpen = true;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom(0);
                        isBottomSheetOpen = false;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float v) {
            }
        });
    }

    public void initDetailBottomSheet() {
        bottomSheetDetailBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        timetableView.setMarginBottom(bottomSheet.getHeight());
                        isBottomDetailSheetOpen = true;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        isBottomDetailSheetOpen = false;
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        timetableView.setMarginBottom(0);
                        isBottomDetailSheetOpen = false;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float v) {
            }
        });
    }


    @OnClick(R.id.timetable_add_schedule_bottom_sheet_center_textview)
    public void onClickedBottomSheetCenterTextview() {
        if (isBottomSheetOpen) {
            isBottomSheetOpen = false;
            timetableView.setMarginBottom(timetableAddTopTextView.getHeight());
            bottomSheetBehavior.setPeekHeight(timetableAddTopTextView.getHeight());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            isBottomSheetOpen = true;
            bottomSheetBehavior.setPeekHeight(0);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    @OnClick(R.id.timetable_detail_schedule_bottom_sheet_center_textview)
    public void onClickedDetailBottomSheetCenterTextview() {
        if (isBottomDetailSheetOpen) {
            isBottomDetailSheetOpen = false;
            timetableView.setMarginBottom(timetableDetailTopTextView.getHeight());
            bottomSheetDetailBehavior.setPeekHeight(timetableDetailTopTextView.getHeight());
            bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            isBottomDetailSheetOpen = true;
            bottomSheetDetailBehavior.setPeekHeight(0);
            bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }


    @Override
    public void onBackPressed() {

        if (isBottomSheetOpen)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if (isBottomDetailSheetOpen)
            bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }

    @OnClick(R.id.koin_base_appbar)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppbarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppbarBase.getRightButtonId()) {
            onAddClassButtonClicked();
        }
    }

    public void onClickedSticker(TimeTableItem timeTableItem) {
        if (!isBottomSheetOpen) {
            StringBuilder infoStringBuilder = new StringBuilder();
            bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            infoStringBuilder.append(getSpertateTimeToString(timeTableItem.getClassTime())).append("\n");
            if (timeTableItem.getDepartment() != null && !timeTableItem.getDepartment().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getDepartment()).append(" / ");
            else
                infoStringBuilder.append("-").append(" / ");
            if (timeTableItem.getCode() != null && !timeTableItem.getCode().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getCode()).append(" / ");
            else
                infoStringBuilder.append("-").append(" / ");
            if (timeTableItem.getGrades() != null && !timeTableItem.getGrades().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getGrades()).append("학점 / ");
            else
                infoStringBuilder.append("-").append("학점 / ");
            if (timeTableItem.getProfessor() != null && !timeTableItem.getProfessor().trim().equals(""))
                infoStringBuilder.append(timeTableItem.getProfessor()).append(" / ");
            else
                infoStringBuilder.append("-").append(" / ");
            timetableDetailClassTitleTextview.setText(timeTableItem.getClassTitle());
            timetableDetailinformationTextview.setText(infoStringBuilder.toString());
            blockId = timeTableItem.getId();

        }
    }

    // Bottom sheet 검색 창 불러오는 함수
    public void onAddClassButtonClicked() {
        clearFilterData();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // Bottom sheet 검색 확인 함수
    @OnClick(R.id.timetable_add_schedule_bottom_sheet_right_textview)
    public void onClickedBottomSheetRightButton() {
        clearFilterData();
        hideKeyboard(TimetableAnonymousActivity.this);
        timetableSearchRecyclerview.setVisibility(View.INVISIBLE);
        closeBottomSearchSheetButton();

    }

    @OnClick(R.id.timetable_add_category_imageview)
    public void onClickedBottomSheetCategoryButton() {
        timetableSearchRecyclerview.setVisibility(View.VISIBLE);
        timetableSelectAnonymousMajorDialog = new TimetableSelectAnonymousMajorDialog(this);
        timetableSelectAnonymousMajorDialog.setOnCLickedDialogItemListener(this);
        timetableSelectAnonymousMajorDialog.setMajorDialogListener(new MajorDialogListener() {
            @Override
            public void sendActivity(String major, int number) {
                setResult(major, number);
            }
        });
        timetableSelectAnonymousMajorDialog.show();
    }

    private void setResult(String major, int number) {
        if (number >= 0 && number < 10)
            select = number;
        Log.d("select", Integer.toString(select));
    }


    @OnClick(R.id.timetable_add_schedule_search_imageview)
    public void onClickedSearchButton() {
        hideKeyboard(TimetableAnonymousActivity.this);
        selectedLectureSeperateArrayList.clear();
        selectedLectureArrayList.clear();
        String keyword = timetableAddScheduleSearchEdittext.getText().toString().trim();
        selectedLectureArrayList.addAll(getFilterUtil(selectedDepartmentCode, keyword, totalLectureArrayList, timetableView.getAllTimeTableItems()));
        Log.d(TAG, "filter item :" + selectedLectureArrayList.size());
        timetableSearchRecyclerview.setVisibility(View.VISIBLE);
        if (selectedLectureArrayList.size() < MAX_ITEM_LOAD) {
            selectedLectureSeperateArrayList.addAll(selectedLectureArrayList);
        } else {
            for (int i = 0; i < MAX_ITEM_LOAD; i++)
                selectedLectureSeperateArrayList.add(selectedLectureArrayList.get(i));
        }
        updateSearchRectclerview();
    }


    @OnTextChanged(R.id.timetable_add_schedule_search_edittext)
    public void searchItemEditext(CharSequence text) {
        selectedLectureSeperateArrayList.clear();
        selectedLectureArrayList.clear();
        String keyword = text.toString().trim();
        selectedLectureArrayList.addAll(getFilterUtil(selectedDepartmentCode, keyword, totalLectureArrayList, timetableView.getAllTimeTableItems()));
        Log.d(TAG, "filter item :" + selectedLectureArrayList.size());
        timetableSearchRecyclerview.setVisibility(View.VISIBLE);
        if (selectedLectureArrayList.size() < MAX_ITEM_LOAD) {
            selectedLectureSeperateArrayList.addAll(selectedLectureArrayList);
        } else {
            for (int i = 0; i < MAX_ITEM_LOAD; i++)
                selectedLectureSeperateArrayList.add(selectedLectureArrayList.get(i));
        }
        updateSearchRectclerview();
    }

    public void clearFilterData() {
        removeAllCheckSticker();
        if (selectedLectureSeperateArrayList != null)
            selectedLectureSeperateArrayList.clear();
        if (selectedLectureArrayList != null)
            selectedLectureArrayList.clear();
        if (timetableRecyclerAdapter != null)
            timetableRecyclerAdapter.notifyDataSetChanged();
    }


    public void addTimeTableItemSelctedByRecyclerview(int position) {
        ArrayList<TimeTableItem> duplicateTimeTableItems;
        duplicateTimeTableItems = TimeDuplicateCheckUtil.checkDuplicateSchedule(timetableView.getAllTimeTableItems(), selectedLectureSeperateArrayList.get(position));
        if (duplicateTimeTableItems.isEmpty()) {
            selectedLectureSeperateClickIndex = position;
            timetablePresenter.addTimeTableItem(new TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
        } else {
            LogUtil.logdArrayList(duplicateTimeTableItems);
            showDuplicateDialog(position, duplicateTimeTableItems);
        }
    }

    public void showDuplicateDialog(int position, ArrayList<TimeTableItem> duplicateTimeTableItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("과목 중복 / 시간 중복");
        StringBuilder duplicateClassTitile = new StringBuilder();
        duplicateClassTitile.append(duplicateScheduleTostring(duplicateTimeTableItems));
        duplicateClassTitile.append("바꾸시겠습니까?");
        builder.setMessage(duplicateClassTitile.toString());
        builder.setPositiveButton("확인",
                (dialog, which) -> {
                    timetablePresenter.addTimeTableItem(new TimeTableItem(selectedLectureSeperateArrayList.get(position)), semester);
                    for (TimeTableItem timeTableItem : duplicateTimeTableItems) {
                        timetablePresenter.deleteItem(timeTableItem.getId());
                    }
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                });
        builder.show();
    }

    public void showAskDeleteTimeTableItemDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("해당 수업을 삭제하시겠습니까?");
        builder.setPositiveButton("확인",
                (dialog, which) -> {
                    timetablePresenter.deleteItem(index);
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                });
        builder.show();
    }

    public void removeAllCheckSticker() {
        timetableView.removeCheckAll();
        for (Lecture lecture : selectedLectureSeperateArrayList) {
            if (lecture != null)
                lecture.isItemClicked = false;
        }
    }

    @OnClick({R.id.timetable_detail_schedule_bottom_sheet_left_textview, R.id.timetable_detail_schedule_bottom_sheet_right_textview})
    public void onClickedDetailBottomSheet(View view) {
        switch (view.getId()) {
            case R.id.timetable_detail_schedule_bottom_sheet_left_textview:
                showAskDeleteTimeTableItemDialog(blockId);
                break;
            case R.id.timetable_detail_schedule_bottom_sheet_right_textview:
                bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                timetableSearchRecyclerview.setVisibility(View.GONE);
                isBottomDetailSheetOpen = false;
                break;
        }
    }

    @Override
    public void showLecture(ArrayList<Lecture> lecture) {
        if (lecture != null) {
            if (totalLectureArrayList != null) {
                totalLectureArrayList.clear();
                totalLectureArrayList.addAll(lecture);
            }

        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showFailMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    @Override
    public void showSuccessCreateTimeTable() {

    }

    @Override
    public void showSuccessAddTimeTableItem(TimeTable timeTable) {
        if (selectedLectureSeperateClickIndex != -1) {
            selectedLectureSeperateArrayList.get(selectedLectureSeperateClickIndex).isAddButtonClicked = true;
            timetableView.removeAll();
            for (TimeTableItem timeTableItem : timeTable.getTimeTableItems())
                timetableView.add(timeTableItem);
            updateSearchRectclerview();
        }
        selectedLectureSeperateClickIndex = -1;
    }

    @Override
    public void showFailAddTimeTableItem() {
        ToastUtil.getInstance().makeShort("인터넷 환경을 확인해주세요");
    }

    @Override
    public void showFailCreateTimeTable() {

    }

    @Override
    public void showSuccessEditTimeTable() {

    }

    @Override
    public void showFailEditTimeTable() {
        ToastUtil.getInstance().makeShort("인터넷 환경을 확인해주세요");
    }

    @Override
    public void showDeleteSuccessTimeTableItem(int id) {
        timetablePresenter.getSavedTimeTableItem();
        bottomSheetDetailBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        isBottomDetailSheetOpen = false;
    }

    public void updateSearchRectclerview() {
        getClikckedUtil(selectedLectureSeperateArrayList, timetableView.getAllTimeTableItems());
        timetableRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailDeleteTimeTableItem() {
        ToastUtil.getInstance().makeShort("인터넷 환경을 확인해주세요");
    }

    @Override
    public void showDeleteSuccessTimeTableAllItem() {

    }

    @Override
    public void showFailDeleteTImeTableAllItem() {

    }

    @Override
    public void showSavedTimeTable(TimeTable timeTable) {
        timetableView.removeAll();
        for (TimeTableItem timeTableItem : timeTable.getTimeTableItems()) {
            timeTableItem.isSavedAtServer = true;
            timetableView.add(timeTableItem);
        }
        updateSearchRectclerview();
    }

    @Override
    public void showFailSavedTimeTable() {

    }

    @Override
    public void setPresenter(TimetableAnonymousPresenter presenter) {
        this.timetablePresenter = presenter;
    }

    private void initScrollListener() {
        timetableSearchRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0) return;
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == selectedLectureSeperateArrayList.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        selectedLectureSeperateArrayList.add(null);
        Handler handler = new Handler();
        final Runnable r = () -> timetableRecyclerAdapter.notifyItemInserted(selectedLectureSeperateArrayList.size() - 1);
        handler.post(r);
        handler.postDelayed(() -> {
            selectedLectureSeperateArrayList.remove(selectedLectureSeperateArrayList.size() - 1);
            int scrollPosition = selectedLectureSeperateArrayList.size();
            timetableRecyclerAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = currentSize + MAX_ITEM_LOAD;
            while (currentSize - 1 < nextLimit) {
                if (currentSize - 1 == selectedLectureArrayList.size() - 1) {
                    return;
                }
                selectedLectureSeperateArrayList.add(selectedLectureArrayList.get(currentSize));
                Log.d(this.getClass().getName(), currentSize + "");
                currentSize++;
            }

            timetableRecyclerAdapter.notifyDataSetChanged();
            isLoading = false;
        }, LOAD_TIME_MS);


    }

    @Override
    public void onClickedItemList(DepartmentCode departmentCode) {
        new Handler().postDelayed(() -> {
            selectedDepartmentCode = departmentCode;
            isLoading = false;
            timetableAddScheduleSearchEdittext.setText("");
            onClickedSearchButton();
            timetableSearchRecyclerview.scrollToPosition(0);
        }, 200);


    }

    @Override
    public void onClick(View view, int position) {
        viewHolder = timetableSearchRecyclerview.findViewHolderForAdapterPosition(position);
        switch (view.getId()) {
            case R.id.timetable_recyclerview_item_relativelayout:
                removeAllCheckSticker();
                selectedLectureSeperateArrayList.get(position).isItemClicked = true;
                timetableView.addCheck(new TimeTableItem(selectedLectureArrayList.get(position)));
                timetableRecyclerAdapter.notifyDataSetChanged();
                break;
            case R.id.add_lecture_button:
                if (!selectedLectureSeperateArrayList.get(position).isAddButtonClicked)
                    addTimeTableItemSelctedByRecyclerview(position);
                else {
                    int index = timetableView.getSameIdWithLecture(selectedLectureSeperateArrayList.get(position));
                    if (index != -1)
                        timetablePresenter.deleteItem(index);
                }
                break;
            default:
                removeAllCheckSticker();
                break;
        }
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void showUpdateAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("업데이트");
        builder.setMessage(message);
        builder.setPositiveButton("확인",
                (dialog, which) -> {
                });
        builder.show();
    }

    @Override
    public void updateSemesterCode(String semester) {
        this.semester = semester;
        timetablePresenter.getLecture(semester);
        timetablePresenter.getSavedTimeTableItem();
    }

    @Override
    public void updateWidget() {
        Handler handler = new Handler();
        handler.postDelayed(this::saveWidgetImage, 2000);
    }

    public void saveWidgetImage() {
        Bitmap bitmap;
        timetableView.setCheckStickersVisibilty(false);
        bitmap = ScreenshotUtil.getInstance().takeTimeTableScreenShot(timeTableScrollview, timeTableHeader);
        timetableView.setCheckStickersVisibilty(true);
        if (bitmap == null) return;
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("timeTable", Context.MODE_PRIVATE);
        File saveImageFile = new File(directory, "timetable.png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(saveImageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TimeTableSharedPreferencesHelper.getInstance().saveLastPathTimetableImage(directory.getAbsolutePath());
        Intent intent = new Intent(this, TimetableWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        this.sendBroadcast(intent);
    }

}
