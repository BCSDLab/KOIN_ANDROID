package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.Semester;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.Version;
import in.koreatech.koin.data.network.interactor.AppVersionInteractor;
import in.koreatech.koin.data.network.interactor.LectureInteractor;
import in.koreatech.koin.data.network.interactor.TimeTableInteractor;
import in.koreatech.koin.ui.timetable.presenter.TimetableContract;
import in.koreatech.koin.ui.timetable.presenter.TimetablePresenter;
import in.koreatech.koin.util.SaveManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeTablePresenterTest {
    @Mock
    private AppVersionInteractor appVersionInteractor;
    @Mock
    private TimeTableInteractor timeTableInteractor;
    @Mock
    private LectureInteractor lectureInteractor;
    @Mock
    private TimetableContract.View timetableView;
    @Mock
    TimeTable timeTable;
    @Mock
    TimeTable.TimeTableItem timeTableItem;
    @Mock
    private Version version;
    private TimetablePresenter timetablePresenter;
    private ApiCallback apiCallback;
    private ArrayList<Lecture> lectures;
    private ArrayList<Semester> semesters;
    private String semester;

    @Before
    public void setTimeTableAnonymousPresenter() {
        MockitoAnnotations.initMocks(this);
        lectures = new ArrayList<>();
        semesters = new ArrayList<>();
        timetablePresenter = new TimetablePresenter(timetableView, timeTableInteractor, lectureInteractor, appVersionInteractor);
        semester = "2020";
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        timetablePresenter = new TimetablePresenter(timetableView, timeTableInteractor, lectureInteractor, appVersionInteractor);
        verify(timetableView).setPresenter(timetablePresenter);
    }

    @Test
    public void errorLoadLectureFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(lectureInteractor).readLecture(anyString(), any(ApiCallback.class));
        timetablePresenter.getLecture(semester);
        verify(timetableView).showLoading();
        verify(timetableView).showMessage(R.string.timetable_lecture_load_error_message);
        verify(timetableView).hideLoading();
    }

    @Test
    public void loadLectureFromServerLoadIntoView() {
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onSuccess(lectures);
                    return null;
                }
        ).when(lectureInteractor).readLecture(anyString(), any(ApiCallback.class));
        timetablePresenter.getLecture(semester);
        verify(timetableView).showLoading();
        verify(timetableView).showLecture(lectures);
        verify(timetableView).hideLoading();
    }

    @Test
    public void errorAddTimeTableItemAtServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(timeTableInteractor).createTimeTable(any(ApiCallback.class), any());
        timetablePresenter.addTimeTableItem(timeTableItem, semester);
        verify(timetableView).showLoading();
        verify(timetableView).showFailAddTimeTableItem();
        verify(timetableView).hideLoading();
    }

    @Test
    public void AddTimeTableItemAtServerLoadIntoView() {
        doAnswer(invocation -> {
                    assertEquals(invocation.getArgument(1), SaveManager.saveTimeTableItemAsJson(timeTableItem, semester));
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onSuccess(timeTable);
                    return null;
                }
        ).when(timeTableInteractor).createTimeTable(any(ApiCallback.class), any());
        timetablePresenter.addTimeTableItem(timeTableItem, semester);
        verify(timetableView).showLoading();
        verify(timetableView).showSuccessAddTimeTableItem(timeTable);
        verify(timetableView).updateWidget();
        verify(timetableView).hideLoading();
    }

    @Test
    public void errorDeleteItemFromServer_ShowsFailSavedTimeTable() {
        int id = 0;
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(timeTableInteractor).deleteTimeTable(any(ApiCallback.class), anyInt());
        timetablePresenter.deleteItem(id);
        verify(timetableView).showLoading();
        verify(timetableView).showFailSavedTimeTable();
        verify(timetableView).hideLoading();
    }

    @Test
    public void deleteItemFromServer_ShowsSuccessAddTimeTableItem() {
        int id = 0;
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onSuccess(timeTable);
                    return null;
                }
        ).when(timeTableInteractor).deleteTimeTable(any(ApiCallback.class), anyInt());
        timetablePresenter.deleteItem(id);
        verify(timetableView).showLoading();
        verify(timetableView).showDeleteSuccessTimeTableItem(id);
        verify(timetableView).hideLoading();
    }

    @Test
    public void errorGetTimeTableVersion_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(appVersionInteractor).readAppVersion(anyString(), any(ApiCallback.class));
        timetablePresenter.getTimeTableVersion();
        verify(timetableView).showLoading();
        verify(timetableView).showMessage(R.string.timetable_version_load_error_message);
        verify(timetableView).hideLoading();
    }

    @Test
    public void getTimeTableVersion_UpdateVersion_ShowsAlertDialog() {
        when(version.getVersion()).thenReturn("20201_1579774355");
        when(version.getLocalVersion()).thenReturn("20201_1579774350");
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onSuccess(version);
                    return null;
                }
        ).when(appVersionInteractor).readAppVersion(anyString(), any(ApiCallback.class));
        timetablePresenter.getTimeTableVersion();
        verify(timetableView).showLoading();
        verify(timetableView).showUpdateAlertDialog(getVersionDialogString(version.getVersion()));
        verify(timetableView).updateSemesterCode(version.getVersion().split("_")[0]);
        verify(timetableView).hideLoading();
    }

    @Test
    public void getTimeTableVersion_SameVersion_UpdateSemesterCode() {
        when(version.getVersion()).thenReturn("20201_1579774350");
        when(version.getLocalVersion()).thenReturn("20201_1579774350");
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(1);
                    apiCallback.onSuccess(version);
                    return null;
                }
        ).when(appVersionInteractor).readAppVersion(anyString(), any(ApiCallback.class));
        timetablePresenter.getTimeTableVersion();
        verify(timetableView).showLoading();
        verify(timetableView, never()).showUpdateAlertDialog(getVersionDialogString(version.getVersion()));
        verify(timetableView).updateSemesterCode(version.getVersion().split("_")[0]);
        verify(timetableView).hideLoading();
    }

    @Test
    public void errorLoadSemestersFromServer_ShowsToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onFailure(exception);
                    return null;
                }
        ).when(timeTableInteractor).readSemesters(any(ApiCallback.class));
        timetablePresenter.readSemesters();
        verify(timetableView).showLoading();
        verify(timetableView).showMessage(R.string.timetable_semester_load_error_message);
        verify(timetableView).hideLoading();
    }

    @Test
    public void loadSemestersFromServerLoadToView() {
        doAnswer(invocation -> {
                    apiCallback = invocation.getArgument(0);
                    apiCallback.onSuccess(semesters);
                    return null;
                }
        ).when(timeTableInteractor).readSemesters(any(ApiCallback.class));
        timetablePresenter.readSemesters();
        verify(timetableView).showLoading();
        verify(timetableView).getSemester(semesters);
        verify(timetableView).hideLoading();
    }


    private String getVersionDialogString(String serverVersionCode) {
        String[] timeStamp = serverVersionCode.split("_");
        StringBuilder timeStringBuilder = new StringBuilder();
        timeStringBuilder.append("강의가 업데이트 되었습니다.\n");
        timeStringBuilder.append(getDate(Long.parseLong(timeStamp[1])));
        return timeStringBuilder.toString();
    }

    private String getDate(long time) {
        java.text.SimpleDateFormat simple = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date result = new java.util.Date(time * 1000);
        return simple.format(result);
    }

}
