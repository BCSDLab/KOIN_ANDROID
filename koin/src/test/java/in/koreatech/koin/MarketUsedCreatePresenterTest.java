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
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedCreateContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedCreatePresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MarketUsedCreatePresenterTest {
    @Mock
    MarketUsedInteractor marketUsedInteractor;
    @Mock
    MarketUsedCreateContract.View marketCreateContractView;

    private MarketUsedCreatePresenter marketUsedCreatePresenter;
    private ApiCallback apiCallback;
    private MarketItem marketItem;
    private Item item;

    @Before
    public void setupMarketUsedCreatePresenter() {
        MockitoAnnotations.initMocks(this);
        marketUsedCreatePresenter = new MarketUsedCreatePresenter(marketCreateContractView, marketUsedInteractor);
        marketItem = new MarketItem();
        marketItem.setTitle("TEST TITLE");
        marketItem.setPhone("010-1234-5678");
        marketItem.setContent("TEST CONTENT");
        marketItem.setIsPhoneOpen(1);
        item = new Item();
        item.setUrl("https://img.youtube.com/vi/IBDtFdVEcnQ/default.jpg");
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        marketUsedCreatePresenter = new MarketUsedCreatePresenter(marketCreateContractView, marketUsedInteractor);
        verify(marketCreateContractView).setPresenter(marketUsedCreatePresenter);
    }

    /* Upload Thumbnail Image */
    @Test
    public void uploadThumbnailImageFromServerAndLoadIntoView() {
        File file = new File("");

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).uploadThumbnailImage(any(File.class), any(ApiCallback.class));

        marketUsedCreatePresenter.uploadThumbnailImage(file);

        verify(marketCreateContractView).showLoading();
        verify(marketCreateContractView).showImageUploadSuccess(item.getUrl());
        verify(marketCreateContractView).hideLoading();
    }

    @Test
    public void errorUploadThumbnailImageFromServer_WrongObject_ShowsErrorToastMessage() {
        File file = new File("");

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(marketItem);
            return null;
        }).when(marketUsedInteractor).uploadThumbnailImage(any(File.class), any(ApiCallback.class));

        marketUsedCreatePresenter.uploadThumbnailImage(file);

        verify(marketCreateContractView).showLoading();
        verify(marketCreateContractView).showImageUploadFail();
        verify(marketCreateContractView).hideLoading();
    }

    @Test
    public void errorUploadThumbnailImageFromServer_ShowsErrorToastMessage() {
        File file = new File("");
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).uploadThumbnailImage(any(File.class), any(ApiCallback.class));

        marketUsedCreatePresenter.uploadThumbnailImage(file);

        verify(marketCreateContractView).showLoading();
        verify(marketCreateContractView).showImageUploadFail();
        verify(marketCreateContractView).hideLoading();
    }

    /* Create Market Item */
    @Test
    public void createMarketItemFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).createMarketItem(any(MarketItem.class), any(ApiCallback.class));

        marketUsedCreatePresenter.createMarketItem(marketItem);

        verify(marketCreateContractView).showLoading();
        verify(marketCreateContractView).showMarketCreatedSuccess(item);
        verify(marketCreateContractView).hideLoading();
    }

    @Test
    public void errorCreateMarketItemFromServer_WrongObject_ShowsErrorToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(marketItem);
            return null;
        }).when(marketUsedInteractor).createMarketItem(any(MarketItem.class), any(ApiCallback.class));

        marketUsedCreatePresenter.createMarketItem(marketItem);

        verify(marketCreateContractView).showLoading();
        verify(marketCreateContractView).showMarketCreateFail();
        verify(marketCreateContractView).hideLoading();
    }

    @Test
    public void errorCreateMarketItemFromServer_EmptyPhone_ShowsErrorToastMessage() {
        marketItem.setPhone("");
        marketUsedCreatePresenter.createMarketItem(marketItem);
        verify(marketCreateContractView).showMessage(R.string.market_used_phone_check);
    }

    @Test
    public void errorCreateMarketItemFromServer_WrongPhone_ShowsErrorToastMessage() {
        String[] wrongPhones = {
                "",
                "a",
                "1",
                "1234567890",
                "01012345678",
                "010.1111.2222"
        };
        for(String wrongPhone : wrongPhones){
            marketItem.setPhone(wrongPhone);
            marketUsedCreatePresenter.createMarketItem(marketItem);
        }
        verify(marketCreateContractView, times(wrongPhones.length)).showMessage(R.string.market_used_phone_check);
    }

    @Test
    public void errorCreateMarketItemFromServer_NullTitle_ShowsErrorToastMessage() {
        marketItem.setTitle(null);
        marketUsedCreatePresenter.createMarketItem(marketItem);
        verify(marketCreateContractView).showMessage(R.string.market_used_title_check);
    }

    @Test
    public void errorCreateMarketItemFromServer_EmptyTitle_ShowsErrorToastMessage() {
        marketItem.setTitle("");
        marketUsedCreatePresenter.createMarketItem(marketItem);
        verify(marketCreateContractView).showMessage(R.string.market_used_title_check);
    }

    @Test
    public void errorCreateMarketItemFromServer_NullContent_ShowsErrorToastMessage() {
        marketItem.setContent(null);
        marketUsedCreatePresenter.createMarketItem(marketItem);
        verify(marketCreateContractView).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorCreateMarketItemFromServer_EmptyContent_ShowsErrorToastMessage() {
        String[] emptyHtmlContent = {
                "<u></u>",
                "<p> </p>",
                "<l> </l>",
                "<u></u>\n<l> </l>",
                "<u></u>\n\n\n\n<l> </l><b></b>"
        };

        for(String emptyContent : emptyHtmlContent) {
            marketItem.setContent(emptyContent);
            marketUsedCreatePresenter.createMarketItem(marketItem);
        }

        verify(marketCreateContractView, times(emptyHtmlContent.length)).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorCreateMarketItemFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).createMarketItem(any(MarketItem.class), any(ApiCallback.class));

        marketUsedCreatePresenter.createMarketItem(marketItem);

        verify(marketCreateContractView).showLoading();
        verify(marketCreateContractView).showMarketCreateFail();
        verify(marketCreateContractView).hideLoading();
    }
}