package in.koreatech.koin.ui.timetable.presenter;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.koreatech.koin.core.contract.BasePresenter;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.data.network.interactor.AppVersionInteractor;
import in.koreatech.koin.data.network.interactor.AppVersionRestInteractor;
import in.koreatech.koin.data.network.interactor.LectureInteractor;
import in.koreatech.koin.data.network.interactor.LectureRestInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableRestInteractor;
import in.koreatech.koin.util.SaveManager;


public class TimetableAnonymousPresenter implements BasePresenter {
    public static final String TAG = TimetableAnonymousPresenter.class.getName();
    public static final String TIMETABLE_SERVICE_CODE = "timetable";

    private AppVersionInteractor appVersionInteractor;
    private TimeTableInteractor timeTableInteractor;
    private LectureInteractor lectureInteractor;
    private TimetableAnonymousContract.View timeTableView;
    private int deleteId;

    public TimetableAnonymousPresenter(TimetableAnonymousContract.View timeTableView) {
        this.timeTableView = timeTableView;
        this.appVersionInteractor = new AppVersionRestInteractor();
        this.timeTableInteractor = new TimeTableRestInteractor();
        this.lectureInteractor = new LectureRestInteractor();
        //
    }

    final ApiCallback lectureApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Lecture> lecture = (ArrayList<Lecture>) object;
            timeTableView.showLecture(lecture);
            timeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showFailMessage("리스트를 받아오지 못했습니다.");
            timeTableView.hideLoading();
        }
    };


    final ApiCallback readTableVersionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            String versionCode = TimeTableSharedPreferencesHelper.getInstance().loadTimeTableVersion();
            String serverVersionCode = "";
            if (object == null) return;
            Version version = (Version) object;
            if (version.getVersion() != null) {
                serverVersionCode = version.getVersion();
            } else {
                timeTableView.hideLoading();
                return;
            }

            if (versionCode == null || !versionCode.equals(serverVersionCode)) {
                String[] timeStamp = serverVersionCode.split("_");
                StringBuilder timeStringBuilder = new StringBuilder();
                timeStringBuilder.append("강의가 업데이트 되었습니다.\n");
                timeStringBuilder.append(getDate(Long.parseLong(timeStamp[1])));
                timeTableView.showUpdateAlertDialog(timeStringBuilder.toString());
            }
            if (version.getVersion() != null) {
                TimeTableSharedPreferencesHelper.getInstance().saveTimeTableVersion(serverVersionCode);
                timeTableView.updateSemesterCode(serverVersionCode.split("_")[0]);
            }
            timeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.hideLoading();
        }
    };

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
        return date;
    }

    public void getLecture(String semester) {
        this.timeTableView.showLoading();
        this.lectureInteractor.readLecture(semester, lectureApiCallback);
    }

    public void addTimeTableItem(TimeTableItem timeTableItem, String semester) {
        this.timeTableView.showLoading();
        TimeTable savedTimeTable = new TimeTable();
        String timeTable = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable();
        if (timeTable != null) {
            try {
                savedTimeTable = SaveManager.loadTimeTable(timeTable);
            } catch (Exception e) {
                this.timeTableView.showFailSavedTimeTable();
            }
        }

        if (savedTimeTable != null && savedTimeTable.getTimeTableItems() != null) {
            int id = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTableBlockID();
            if (id == Integer.MAX_VALUE) id = -1;
            timeTableItem.setId(++id);
            TimeTableSharedPreferencesHelper.getInstance().saveTimeTableBlockID(id);
            savedTimeTable.addTimeTableItem(timeTableItem);
            TimeTableSharedPreferencesHelper.getInstance().saveTimeTable(SaveManager.saveTimeTable(savedTimeTable, semester));
        }


        this.timeTableView.showSuccessAddTimeTableItem(savedTimeTable);
        this.timeTableView.updateWidget();
        this.timeTableView.hideLoading();
    }

    public void getSavedTimeTableItem() {
        this.timeTableView.showLoading();
        String timeTable = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable();
        if (timeTable != null) {
            try {
                TimeTable savedTimeTable = SaveManager.loadTimeTable(timeTable);
                if (savedTimeTable != null) {
                    this.timeTableView.showSavedTimeTable(savedTimeTable);
                }
            } catch (Exception e) {
                Log.e(TAG, "getSavedTimeTableItem: ", e);
                this.timeTableView.showFailSavedTimeTable();
            }
        } else
            this.timeTableView.showFailSavedTimeTable();
        this.timeTableView.updateWidget();
        this.timeTableView.hideLoading();
    }

    public TimeTable getSavedTimeTable() {
        TimeTable savedTimeTable = new TimeTable();
        String timeTable = TimeTableSharedPreferencesHelper.getInstance().loadSaveTimeTable();
        if (timeTable != null) {
            try {
                savedTimeTable = SaveManager.loadTimeTable(timeTable);
            } catch (Exception e) {
                Log.e(TAG, "getSavedTimeTableItem: ", e);
            }
        }
        return savedTimeTable;
    }

    public void deleteItem(int id) {
        this.timeTableView.showLoading();
        deleteId = id;
        TimeTable timeTable = getSavedTimeTable();
        TimeTable saveTable = new TimeTable();
        if (timeTable == null) return;
        saveTable.setSemester(timeTable.semester);
        for (TimeTableItem timeTableItem : timeTable.getTimeTableItems()) {
            if (timeTableItem.getId() != id) {
                saveTable.addTimeTableItem(timeTableItem);
            }
        }
        TimeTableSharedPreferencesHelper.getInstance().saveTimeTable(SaveManager.saveTimeTable(saveTable, saveTable.semester));
        this.timeTableView.showDeleteSuccessTimeTableItem(deleteId);
        this.timeTableView.hideLoading();
    }


    public void getTimeTableVersion() {
        this.timeTableView.showLoading();
        appVersionInteractor.readAppVersion(TIMETABLE_SERVICE_CODE, readTableVersionApiCallback);
    }


}
