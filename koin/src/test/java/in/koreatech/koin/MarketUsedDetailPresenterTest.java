package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class MarketUsedDetailPresenterTest {
    @Mock
    MarketUsedInteractor marketUsedInteractor;
    @Mock
    MarketUsedDetailContract.View marketDetailView;

    private MarketUsedDetailPresenter marketUsedDetailPresenter;
    private ApiCallback apiCallback;
    private Comment comment;
    private String content;
    private int marketId;
    private Item item;

    @Before
    public void setupMarketUsedDetailPresenter() {
        MockitoAnnotations.initMocks(this);
        marketUsedDetailPresenter = new MarketUsedDetailPresenter(marketDetailView, marketUsedInteractor);
        comment = new Comment();
        comment.setCommentUid(55);
        content = "TEST CONTENT";
        marketId = 1234;
        item = new Item();
        item.setId(44);
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        marketUsedDetailPresenter = new MarketUsedDetailPresenter(marketDetailView, marketUsedInteractor);
        verify(marketDetailView).setPresenter(marketUsedDetailPresenter);
    }

    /* Read Market Detail */
    @Test
    public void loadMarketDetailFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.readMarketDetail(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).onMarketDataReceived(item);
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void errorMarketDetailFromServer_WrongObject_ShowsErrorToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.readMarketDetail(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketDataReceivedFail();
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void errorMarketDetailFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.readMarketDetail(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketDataReceivedFail();
        verify(marketDetailView).hideLoading();
    }

    /* Create Comment */
    @Test
    public void createCommentFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).createCommentDetail(anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailPresenter.createComment(marketId, content);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketCommentUpdate();
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void errorCreateCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).createCommentDetail(anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailPresenter.createComment(marketId, content);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketCommentUpdateFail();
        verify(marketDetailView).hideLoading();
    }

    /* Delete Comment */
    @Test
    public void deleteCommentFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).deleteCommentDetail(anyInt(), anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.deleteComment(comment, item);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketCommentDelete();
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void errorDeleteCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).deleteCommentDetail(anyInt(), anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.deleteComment(comment, item);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketCommentDeleteFail();
        verify(marketDetailView).hideLoading();
    }

    /* Edit Comment */
    @Test
    public void editCommentFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).editCommentDetail(anyInt(), anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailPresenter.editComment(comment, item, content);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketCommentEdit();
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void errorEditCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).editCommentDetail(anyInt(), anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailPresenter.editComment(comment, item, content);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketCommentEditFail();
        verify(marketDetailView).hideLoading();
    }

    /* Delete Item */
    @Test
    public void deleteItemFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).deleteMarketItem(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.deleteItem(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketItemDelete();
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void errorDeleteItemFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).deleteMarketItem(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.deleteItem(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showMarketItemDeleteFail();
        verify(marketDetailView).hideLoading();
    }

    /* Grant Check */
    @Test
    public void checkGrantedFromServer_PositiveGranted_LoadIntoView() {
        item.setGrantEdit(true);

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.checkGranted(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showGrantCheck(true);
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void checkGrantedFromServer_NegativeGranted_LoadIntoView() {
        item.setGrantEdit(false);

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.checkGranted(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showGrantCheck(false);
        verify(marketDetailView).hideLoading();
    }

    @Test
    public void errorCheckGrantedFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).readGrantedDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailPresenter.checkGranted(marketId);

        verify(marketDetailView).showLoading();
        verify(marketDetailView).showGrantCheck(false);
        verify(marketDetailView).hideLoading();
    }
}
