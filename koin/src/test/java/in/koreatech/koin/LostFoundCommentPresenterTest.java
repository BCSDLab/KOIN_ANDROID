package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.data.network.response.DefaultResponse;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundCommentContract;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundCommentPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LostFoundCommentPresenterTest {
    @Mock
    LostFoundCommentContract.View lostFoundCommentView;
    @Mock
    LostAndFoundInteractor lostFoundInteractor;

    private LostFoundCommentPresenter lostFoundCommentPresenter;
    private ApiCallback apiCallback;
    private LostItem lostItem;
    private int testCommentId;
    private String testComment;
    private int testId;

    @Before
    public void setupLostFoundCommentPresenter() {
        MockitoAnnotations.initMocks(this);
        lostFoundCommentPresenter = new LostFoundCommentPresenter(lostFoundCommentView, lostFoundInteractor);
        lostItem = new LostItem();
        testComment = "TEST COMMENT";
        testCommentId = 1111;
        testId = 2222;
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        lostFoundCommentPresenter = new LostFoundCommentPresenter(lostFoundCommentView, lostFoundInteractor);
        verify(lostFoundCommentView).setPresenter(lostFoundCommentPresenter);
    }

    /* Create LostAndFound Comment */
    @Test
    public void createCommentFromServer_ShowsSuccessToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).createCommentDetail(anyInt(), anyString(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));

        lostFoundCommentPresenter.createComment(testId, testComment);

        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_created);
        verify(lostFoundCommentView, times(2)).showLoading();
        verify(lostFoundCommentView).showSuccessLostItem(lostItem);
        verify(lostFoundCommentView, times(2)).hideLoading();
    }

    @Test
    public void errorCreateCommentFromServer_NullComment_ShowsErrorToastMessage() {
        testComment = null;

        lostFoundCommentPresenter.createComment(testId, testComment);
        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_content_check);
    }

    @Test
    public void errorCreateCommentFromServer_EmptyComment_ShowsErrorToastMessage() {
        testComment = "";

        lostFoundCommentPresenter.createComment(testId, testComment);
        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_content_check);
    }

    @Test
    public void errorCreateCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostFoundInteractor).createCommentDetail(anyInt(), anyString(), any(ApiCallback.class));

        lostFoundCommentPresenter.createComment(testId, testComment);

        verify(lostFoundCommentView).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.error_network);
        verify(lostFoundCommentView).hideLoading();
    }

    /* Update LostAndFound Comment */
    @Test
    public void updateCommentFromServer_ShowsSuccessToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).editCommentDetail(anyInt(), anyInt(), anyString(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));

        lostFoundCommentPresenter.updateComment(testId, testCommentId, testComment);

        verify(lostFoundCommentView, times(2)).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_edited);
        verify(lostFoundCommentView).showSuccessLostItem(lostItem);
        verify(lostFoundCommentView, times(2)).hideLoading();
    }

    @Test
    public void errorUpdateCommentFromServer_NullContent_ShowsErrorToastMessage() {
        testComment = null;

        lostFoundCommentPresenter.updateComment(testId, testCommentId, testComment);
        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_content_check);
    }

    @Test
    public void errorUpdateCommentFromServer_EmptyContent_ShowsErrorToastMessage() {
        testComment = "";

        lostFoundCommentPresenter.updateComment(testId, testCommentId, testComment);
        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_content_check);
    }

    @Test
    public void errorUpdateCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostFoundInteractor).editCommentDetail(anyInt(), anyInt(), anyString(), any(ApiCallback.class));

        lostFoundCommentPresenter.updateComment(testId, testCommentId, testComment);

        verify(lostFoundCommentView).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.error_network);
        verify(lostFoundCommentView).hideLoading();
    }

    /* Delete LostAndFound Comment */
    @Test
    public void deleteCommentFromServer_ShowsSuccessMessage() {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.success = true;

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(defaultResponse);
            return null;
        }).when(lostFoundInteractor).deleteCommentDetail(anyInt(), anyInt(), any(ApiCallback.class));

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));


        lostFoundCommentPresenter.deleteComment(testId, testCommentId);

        verify(lostFoundCommentView, times(2)).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_deleted);
        verify(lostFoundCommentView).showSuccessLostItem(lostItem);
        verify(lostFoundCommentView, times(2)).hideLoading();
    }

    @Test
    public void errorDeleteCommentFromServer_FalseResponse_ShowsErrorToastMessage() {
        DefaultResponse defaultResponse = new DefaultResponse();
        defaultResponse.success = false;

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(defaultResponse);
            return null;
        }).when(lostFoundInteractor).deleteCommentDetail(anyInt(), anyInt(), any(ApiCallback.class));

        lostFoundCommentPresenter.deleteComment(testId, testCommentId);

        verify(lostFoundCommentView).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.lost_and_found_delete_fail);
        verify(lostFoundCommentView).hideLoading();
    }

    @Test
    public void errorDeleteCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostFoundInteractor).deleteCommentDetail(anyInt(), anyInt(), any(ApiCallback.class));

        lostFoundCommentPresenter.deleteComment(testId, testCommentId);

        verify(lostFoundCommentView).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.error_network);
        verify(lostFoundCommentView).hideLoading();
    }

    /* Error To Get LostAndFoundItem */
    @Test
    public void errorLoadLostFound_WrongObject_ShowsErrorToastMessage() {
        DefaultResponse defaultResponse = new DefaultResponse();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(defaultResponse);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));

        lostFoundCommentPresenter.getLostItem(testId);

        verify(lostFoundCommentView).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.error_network);
        verify(lostFoundCommentView).hideLoading();
    }

    @Test
    public void errorLoadLostFound_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(lostFoundInteractor).readLostAndFoundDetail(anyInt(), any(ApiCallback.class));

        lostFoundCommentPresenter.getLostItem(testId);

        verify(lostFoundCommentView).showLoading();
        verify(lostFoundCommentView).showMessage(R.string.error_network);
        verify(lostFoundCommentView).hideLoading();
    }
}
