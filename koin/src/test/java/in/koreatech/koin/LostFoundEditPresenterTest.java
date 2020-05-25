package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Land;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.data.network.interactor.LostAndFoundInteractor;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundEditContract;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundEditPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LostFoundEditPresenterTest {
    @Mock
    LostFoundEditContract.View lostFoundEditView;
    @Mock
    LostAndFoundInteractor lostFoundInteractor;

    private LostFoundEditPresenter lostFoundEditPresenter;
    private ApiCallback apiCallback;
    private LostItem lostItem;
    private int testId;

    @Before
    public void setupLostFoundEditPresenter() {
        MockitoAnnotations.initMocks(this);
        lostFoundEditPresenter = new LostFoundEditPresenter(lostFoundEditView, lostFoundInteractor);
        lostItem = new LostItem();
        lostItem.setTitle("TEST TITLE");
        lostItem.setPhoneOpen(true);
        lostItem.setPhone("010-1234-5678");
        lostItem.setContent("TEST CONTENT");
        testId = 1234;
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        lostFoundEditPresenter = new LostFoundEditPresenter(lostFoundEditView, lostFoundInteractor);
        verify(lostFoundEditView).setPresenter(lostFoundEditPresenter);
    }

    /* Create LostAndFound */
    @Test
    public void createLostItemFromServerAndShowsSuccessCreate() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).createLostAndFoundItem(any(LostItem.class), any(ApiCallback.class));

        lostFoundEditPresenter.createLostItem(lostItem);

        verify(lostFoundEditView).showLoading();
        verify(lostFoundEditView).showSuccessCreate(lostItem);
        verify(lostFoundEditView).hideLoading();
    }

    @Test
    public void errorCreateLostItemFromServer_WrongObject_ShowsErrorToastMessage() {
        Land land = new Land();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(land);
            return null;
        }).when(lostFoundInteractor).createLostAndFoundItem(any(LostItem.class), any(ApiCallback.class));

        lostFoundEditPresenter.createLostItem(lostItem);

        verify(lostFoundEditView).showLoading();
        verify(lostFoundEditView).showMessage(R.string.error_network);
        verify(lostFoundEditView).hideLoading();
    }

    @Test
    public void errorCreateLostItemFromServer_NullTitle_ShowsErrorToastMessage() {
        lostItem.setTitle(null);
        lostFoundEditPresenter.createLostItem(lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_title_check);
    }

    @Test
    public void errorCreateLostItemFromServer_EmptyTitle_ShowsErrorToastMessage() {
        lostItem.setTitle("");
        lostFoundEditPresenter.createLostItem(lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_title_check);
    }

    @Test
    public void errorCreateLostItemFromServer_NullPhone_ShowsErrorToastMessage() {
        lostItem.setPhone(null);
        lostFoundEditPresenter.createLostItem(lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_phone_check);
    }

    @Test
    public void errorCreateLostItemFromServer_WrongPhone_ShowsErrorToastMessage() {
        String[] wrongPhoneList = { "", "a", "1", "12345678", "01012345678", "010.1234.5678", "tel://010-1234-5678" };

        for (String wrongPhone : wrongPhoneList) {
            lostItem.setPhone(wrongPhone);
            lostFoundEditPresenter.createLostItem(lostItem);
        }

        verify(lostFoundEditView, times(wrongPhoneList.length)).showMessage(R.string.lost_and_found_phone_check);
    }

    @Test
    public void errorCreateLostItemFromServer_NullContent_ShowsErrorToastMessage() {
        lostItem.setContent(null);
        lostFoundEditPresenter.createLostItem(lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_content_check);
    }

    @Test
    public void errorCreateLostItemFromServer_EmptyContent_ShowsErrorToastMessage() {
        String[] emptyHtmlContent = { "<u></u>", "<p> </p>", "<l> </l>", "<u></u>\n<l> </l>", "<u></u>\n\n\n\n<l> </l><b></b>" };

        for (String emptyContent : emptyHtmlContent) {
            lostItem.setContent(emptyContent);
            lostFoundEditPresenter.createLostItem(lostItem);
        }

        verify(lostFoundEditView, times(emptyHtmlContent.length)).showMessage(R.string.lost_and_found_content_check);
    }

    /* Update LostAndFound */
    @Test
    public void updateLostItemFromServerAndShowSuccessUpdate() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(lostItem);
            return null;
        }).when(lostFoundInteractor).editCotentEdit(anyInt(), any(LostItem.class), any(ApiCallback.class));

        lostFoundEditPresenter.updateLostItem(testId, lostItem);

        verify(lostFoundEditView).showLoading();
        verify(lostFoundEditView).showSuccessUpdate(lostItem);
        verify(lostFoundEditView).hideLoading();
    }

    @Test
    public void errorUpdateLostItemFromServer_WrongObject_ShowsErrorToastMessage() {
        Land land = new Land();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(land);
            return null;
        }).when(lostFoundInteractor).editCotentEdit(anyInt(), any(LostItem.class), any(ApiCallback.class));

        lostFoundEditPresenter.updateLostItem(testId, lostItem);

        verify(lostFoundEditView).showLoading();
        verify(lostFoundEditView).showMessage(R.string.error_network);
        verify(lostFoundEditView).hideLoading();
    }

    @Test
    public void errorUpdateLostItemFromServer_NullTitle_ShowsErrorToastMessage() {
        lostItem.setTitle(null);
        lostFoundEditPresenter.updateLostItem(testId, lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_title_check);
    }

    @Test
    public void errorUpdateLostItemFromServer_EmptyTitle_ShowsErrorToastMessage() {
        lostItem.setTitle("");
        lostFoundEditPresenter.updateLostItem(testId, lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_title_check);
    }

    @Test
    public void errorUpdateLostItemFromServer_NullPhone_ShowsErrorToastMessage() {
        lostItem.setPhone(null);
        lostFoundEditPresenter.updateLostItem(testId, lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_phone_check);
    }

    @Test
    public void errorUpdateLostItemFromServer_WrongPhone_ShowsErrorToastMessage() {
        String[] wrongPhoneList = {
                "",
                "a",
                "1",
                "12345678",
                "01012345678",
                "010.1234.5678",
                "tel://010-1234-5678"
        };

        for (String wrongPhone : wrongPhoneList) {
            lostItem.setPhone(wrongPhone);
            lostFoundEditPresenter.updateLostItem(testId, lostItem);
        }

        verify(lostFoundEditView, times(wrongPhoneList.length)).showMessage(R.string.lost_and_found_phone_check);
    }

    @Test
    public void errorUpdateLostItemFromServer_NullContent_ShowsErrorToastMessage() {
        lostItem.setContent(null);
        lostFoundEditPresenter.updateLostItem(testId, lostItem);
        verify(lostFoundEditView).showMessage(R.string.lost_and_found_content_check);
    }

    @Test
    public void errorUpdateLostItemFromServer_EmptyContent_ShowsErrorToastMessage() {
        String[] emptyHtmlContent = { "<u></u>", "<p> </p>", "<l> </l>", "<u></u>\n<l> </l>", "<u></u>\n\n\n\n<l> </l><b></b>" };

        for (String emptyContent : emptyHtmlContent) {
            lostItem.setContent(emptyContent);
            lostFoundEditPresenter.updateLostItem(testId, lostItem);
        }

        verify(lostFoundEditView, times(emptyHtmlContent.length)).showMessage(R.string.lost_and_found_content_check);
    }
}