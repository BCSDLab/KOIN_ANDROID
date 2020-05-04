package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MarketUsedEditPresenterTest {
    @Mock
    MarketUsedInteractor marketUsedInteractor;
    @Mock
    MarketUsedEditContract.View marketUsedEditView;

    private MarketUsedEditPresenter marketUsedEditPresenter;
    private ApiCallback apiCallback;
    private MarketItem marketItem;
    private Item item;
    private int marketId;

    @Before
    public void setupMarketUsedEditPresenter() {
        MockitoAnnotations.initMocks(this);
        marketUsedEditPresenter = new MarketUsedEditPresenter(marketUsedEditView, marketUsedInteractor);
        marketItem = new MarketItem();
        marketItem.setTitle("TEST TITLE");
        marketItem.setPhone("010-1234-5678");
        marketItem.setContent("TEST CONTENT");
        marketItem.setIsPhoneOpen(1);
        item = new Item();
        item.setUrl("https://img.youtube.com/vi/IBDtFdVEcnQ/default.jpg");
        marketId = 5555;
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        marketUsedEditPresenter = new MarketUsedEditPresenter(marketUsedEditView, marketUsedInteractor);
        verify(marketUsedEditView).setPresenter(marketUsedEditPresenter);
    }

    @Test
    public void editMarketContentFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).editCotentEdit(anyInt(), any(MarketItem.class), any(ApiCallback.class));

        marketUsedEditPresenter.editMarketContent(marketId, marketItem);

        verify(marketUsedEditView).showLoading();
        verify(marketUsedEditView).showUpdateSuccess();
        verify(marketUsedEditView).hideLoading();
    }

    @Test
    public void errorEditMarketContent_WrongPhone_ShowsErrorToastMessage() {
        String[] wrongPhones = {
                "",
                "a",
                "1",
                "1234567890",
                "01012345678",
                "010.1111.2222"
        };

        for (String wrongPhone : wrongPhones) {
            marketItem.setPhone(wrongPhone);
            marketUsedEditPresenter.editMarketContent(marketId, marketItem);
        }

        verify(marketUsedEditView, times(wrongPhones.length)).showMessage(R.string.market_used_phone_check);
    }

    @Test
    public void errorEditMarketContent_NullTitle_ShowsErrorToastMessage() {
        marketItem.setTitle(null);

        marketUsedEditPresenter.editMarketContent(marketId, marketItem);

        verify(marketUsedEditView).showMessage(R.string.market_used_title_check);
    }

    @Test
    public void errorEditMarketContent_EmptyTitle_ShowsErrorToastMessage() {
        marketItem.setTitle("");

        marketUsedEditPresenter.editMarketContent(marketId, marketItem);

        verify(marketUsedEditView).showMessage(R.string.market_used_title_check);
    }

    @Test
    public void errorEditMarketContent_NullContent_ShowsErrorToastMessage() {
        marketItem.setContent(null);

        marketUsedEditPresenter.editMarketContent(marketId, marketItem);

        verify(marketUsedEditView).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorEditMarketContent_EmptyContent_ShowsErrorToastMessage() {
        String[] emptyHtmlContent = {
                "<u></u>",
                "<p> </p>",
                "<l> </l>",
                "<u></u>\n<l> </l>",
                "<u></u>\n\n\n\n<l> </l><b></b>"
        };

        for (String emptyContent : emptyHtmlContent) {
            marketItem.setContent(emptyContent);
            marketUsedEditPresenter.editMarketContent(marketId, marketItem);
        }

        verify(marketUsedEditView, times(emptyHtmlContent.length)).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorEditMarketContent_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).editCotentEdit(anyInt(), any(MarketItem.class), any(ApiCallback.class));

        marketUsedEditPresenter.editMarketContent(marketId, marketItem);

        verify(marketUsedEditView).showLoading();
        verify(marketUsedEditView).showUpdateFail();
        verify(marketUsedEditView).hideLoading();
    }

    @Test
    public void uploadThumbnailImageFromServerAndLoadIntoView() {
        File file = new File("");

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).uploadThumbnailImage(any(File.class), any(ApiCallback.class));

        marketUsedEditPresenter.uploadThumbnailImage(file);

        verify(marketUsedEditView).showLoading();
        verify(marketUsedEditView).showImageUploadSuccess(item.getUrl());
        verify(marketUsedEditView).hideLoading();
    }

    @Test
    public void errorUploadThumbnailImage_NullUrl_HideLoading() {
        File file = new File("");
        item.setUrl(null);

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).uploadThumbnailImage(any(File.class), any(ApiCallback.class));

        marketUsedEditPresenter.uploadThumbnailImage(file);

        verify(marketUsedEditView).showLoading();
        verify(marketUsedEditView).hideLoading();
    }

    @Test
    public void errorUploadThumbnailImage_ShowsErrorToastMessage() {
        File file = new File("");
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).uploadThumbnailImage(any(File.class), any(ApiCallback.class));

        marketUsedEditPresenter.uploadThumbnailImage(file);

        verify(marketUsedEditView).showLoading();
        verify(marketUsedEditView).showImageUploadFail();
        verify(marketUsedEditView).hideLoading();
    }
}