package in.koreatech.koin.ui.timetable.presenter;

import java.util.ArrayList;
import in.koreatech.koin.core.contract.BaseView;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.entity.TimeTable;


public interface TimetableAnonymousContract{
    interface View extends BaseView<TimetableAnonymousPresenter> {
        void showLoading();

        void hideLoading();

        void showLecture(ArrayList<Lecture> lecture);

        void showFailMessage(String message);

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

        void showUpdateAlertDialog(String message);

        void updateSemesterCode(String semester);

        void updateWidget();

        void getSemester(ArrayList<Semester> semesters);
    }
}
