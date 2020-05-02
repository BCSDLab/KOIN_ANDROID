package in.koreatech.koin.ui.timetable.presenter;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import android.text.format.DateFormat;

import in.koreatech.koin.R;
import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.data.network.interactor.AppVersionInteractor;
import in.koreatech.koin.data.network.interactor.AppVersionRestInteractor;
import in.koreatech.koin.data.network.interactor.LectureInteractor;
import in.koreatech.koin.data.network.interactor.LectureRestInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableRestInteractor;
import in.koreatech.koin.data.sharedpreference.TimeTableSharedPreferencesHelper;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SaveManager;

public class TimetablePresenter {
    public static final String TAG = "TimetablePresenter";
    public static final String TIMETABLE_SERVICE_CODE = "timetable";

    private AppVersionInteractor appVersionInteractor;
    private TimeTableInteractor timeTableInteractor;
    private LectureInteractor lectureInteractor;
    private TimetableContract.View timeTableView;
    private int deleteId;

    public TimetablePresenter(TimetableContract.View timeTableView, TimeTableInteractor timeTableInteractor, LectureInteractor lectureInteractor, AppVersionInteractor appVersionInteractor) {
        this.timeTableView = timeTableView;
        this.appVersionInteractor = appVersionInteractor;
        this.timeTableInteractor = timeTableInteractor;
        this.lectureInteractor = lectureInteractor;
        timeTableView.setPresenter(this);
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
            timeTableView.showMessage(R.string.timetable_lecture_load_error_message);
            timeTableView.hideLoading();
        }
    };

    final ApiCallback addTimetableApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (!(object instanceof TimeTable))
                return;
            TimeTable timeTable = (TimeTable) object;
            timeTableView.showSuccessAddTimeTableItem(timeTable);
            timeTableView.updateWidget();
            timeTableView.hideLoading();

        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showFailAddTimeTableItem();
            timeTableView.hideLoading();
        }
    };

    final ApiCallback getSavedTimetableApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (!(object instanceof TimeTable))
                return;
            TimeTable timeTable = (TimeTable) object;
            timeTableView.showSavedTimeTable(timeTable);
            timeTableView.updateWidget();
            timeTableView.hideLoading();

        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showFailSavedTimeTable();
            timeTableView.hideLoading();
        }
    };

    final ApiCallback deleteTimetableItemApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            timeTableView.showDeleteSuccessTimeTableItem(deleteId);
            timeTableView.hideLoading();

        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showFailSavedTimeTable();
            timeTableView.hideLoading();
        }
    };

    final ApiCallback readTableVersionApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            String versionCode = "";
            String serverVersionCode = "";
            Version version;
            if (object instanceof Version) {
                version = (Version) object;
                serverVersionCode = version.getVersion();
                versionCode = version.getLocalVersion();
            }

            if (!versionCode.equals(serverVersionCode)) {
                String[] timeStamp = serverVersionCode.split("_");
                StringBuilder timeStringBuilder = new StringBuilder();
                timeStringBuilder.append("강의가 업데이트 되었습니다.\n");
                timeStringBuilder.append(getDate(Long.parseLong(timeStamp[1])));
                timeTableView.showUpdateAlertDialog(timeStringBuilder.toString());
            }

            timeTableView.updateSemesterCode(serverVersionCode.split("_")[0]);
            timeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showMessage(R.string.timetable_version_load_error_message);
            timeTableView.hideLoading();
        }
    };

    final ApiCallback readSemestersApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            ArrayList<Semester> semesters = (ArrayList<Semester>) object;
            timeTableView.getSemester(semesters);
            timeTableView.hideLoading();

        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showMessage(R.string.timetable_semester_load_error_message);
            timeTableView.hideLoading();
        }
    };

    private String getDate(long time) {
        java.text.SimpleDateFormat simple = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        java.util.Date result = new  java.util.Date(time * 1000);
        return simple.format(result);
    }

    public void getLecture(String semester) {
        timeTableView.showLoading();
        this.lectureInteractor.readLecture(semester, lectureApiCallback);
    }

    public void addTimeTableItem(TimeTable.TimeTableItem timeTableItem, String semester) {
        this.timeTableView.showLoading();
        JsonObject timetableJson = SaveManager.saveTimeTableItemAsJson(timeTableItem, semester);
        this.timeTableInteractor.createTimeTable(addTimetableApiCallback, timetableJson);
    }

    public void getSavedTimeTableItem(String semester) {
        this.timeTableView.showLoading();
        this.timeTableInteractor.readTimeTable(getSavedTimetableApiCallback, semester);
    }

    public void deleteItem(int id) {
        this.timeTableView.showLoading();
        deleteId = id;
        this.timeTableInteractor.deleteTimeTable(deleteTimetableItemApiCallback, id);
    }

    public void getTimeTableVersion() {
        this.timeTableView.showLoading();
        this.appVersionInteractor.readAppVersion(TIMETABLE_SERVICE_CODE, readTableVersionApiCallback);
    }

    public void readSemesters() {
        this.timeTableView.showLoading();
        this.timeTableInteractor.readSemesters(readSemestersApiCallback);
    }


}
