package in.koreatech.koin.ui.timetable.presenter;

import androidx.annotation.StringRes;

import java.util.ArrayList;

import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.entity.TimeTable;

public interface TimetableContract {
    interface View extends BaseView<TimetablePresenter> {
        void showLoading();

        void hideLoading();

        void showLecture(ArrayList<Lecture> lecture);

        void showMessage(String message);

        void showMessage(@StringRes int  message);

        void showSuccessCreateTimeTable();

        void showFailCreateTimeTable();

        void showSuccessAddTimeTableItem(TimeTable timeTable);

        void showFailAddTimeTableItem();

        void showSuccessEditTimeTable();

        void showFailEditTimeTable();

        void showDeleteSuccessTimeTableItem(int id);

        void showFailDeleteTimeTableItem();

        void showDeleteSuccessTimeTableAllItem();

        void showFailDeleteTImeTableAllItem();

        void showSavedTimeTable(TimeTable timeTable);

        void showFailSavedTimeTable();

        void updateWidget();

        void showUpdateAlertDialog(String message);

        void updateSemesterCode(String semester);

        void getSemester(ArrayList<Semester> semesters);
    }
}
