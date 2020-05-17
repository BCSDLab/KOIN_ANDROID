package in.koreatech.koin.ui.timetable.presenter;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
import in.koreatech.koin.util.SaveManager;


public class TimetableAnonymousPresenter {
    public static final String TAG = "TimetableAnonymous";
    public static final String TIMETABLE_SERVICE_CODE = "timetable";

    private AppVersionInteractor appVersionInteractor;
    private TimeTableInteractor timeTableInteractor;
    private LectureInteractor lectureInteractor;
    private TimetableAnonymousContract.View timeTableView;

    public TimetableAnonymousPresenter(TimetableAnonymousContract.View timeTableView, TimeTableInteractor timeTableInteractor, LectureInteractor lectureInteractor, AppVersionInteractor appVersionInteractor) {
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
                timeTableView.showUpdateAlertDialog(serverVersionCode);
            }

            timeTableView.updateSemesterCode(serverVersionCode.split("_")[0]);
            timeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.hideLoading();
            timeTableView.showMessage(R.string.timetable_version_load_error_message);
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

    final ApiCallback readTimeTableFromLocalApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof TimeTable) {
                timeTableView.showSavedTimeTable((TimeTable) object);
                timeTableView.updateWidget();
            } else {
                timeTableView.showFailSavedTimeTable();
            }

            timeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showFailSavedTimeTable();
            timeTableView.hideLoading();
        }
    };

    final ApiCallback editTimeTableFromLocalApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof TimeTable) {
                timeTableView.showSuccessAddTimeTableItem((TimeTable) object);
                timeTableView.updateWidget();
            } else {
                timeTableView.showFailSavedTimeTable();
            }
            timeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showFailSavedTimeTable();
            timeTableView.hideLoading();
        }
    };

    final ApiCallback deleteTimeTableFromLocalApiCallback = new ApiCallback() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof TimeTable) {
                timeTableView.showSuccessAddTimeTableItem((TimeTable) object);
                timeTableView.showDeleteSuccessTimeTableItem();
                timeTableView.updateWidget();
            } else {
                timeTableView.showFailSavedTimeTable();
            }
            timeTableView.hideLoading();
        }

        @Override
        public void onFailure(Throwable throwable) {
            timeTableView.showFailSavedTimeTable();
            timeTableView.hideLoading();
        }
    };

    public void getLecture(String semester) {
        timeTableView.showLoading();
        lectureInteractor.readLecture(semester, lectureApiCallback);
    }

    public void addTimeTableItem(TimeTable.TimeTableItem timeTableItem, String semester) {
        timeTableView.showLoading();
        timeTableInteractor.editTimeTableItemAtLocal(timeTableItem, semester, editTimeTableFromLocalApiCallback);
    }

    public void getSavedTimeTableItem(String semester) {
        timeTableView.showLoading();
        timeTableInteractor.readTimeTableFromLocal(semester, readTimeTableFromLocalApiCallback);
    }

    public void deleteItem(String semester, int id) {
        timeTableView.showLoading();
        timeTableInteractor.deleteTimeTableItemAtLocal(semester, id, deleteTimeTableFromLocalApiCallback);
    }


    public void getTimeTableVersion() {
        timeTableView.showLoading();
        this.appVersionInteractor.readAppVersion(TIMETABLE_SERVICE_CODE, readTableVersionApiCallback);
    }

    public void readSemesters() {
        timeTableView.showLoading();
        timeTableInteractor.readSemesters(readSemestersApiCallback);
    }
}
