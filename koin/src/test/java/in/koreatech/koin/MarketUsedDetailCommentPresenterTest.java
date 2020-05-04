package in.koreatech.koin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import in.koreatech.koin.core.network.ApiCallback;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedInteractor;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedCommentContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailCommentPresenter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class MarketUsedDetailCommentPresenterTest {
    @Mock
    MarketUsedInteractor marketUsedInteractor;
    @Mock
    MarketUsedCommentContract.View marketCreateCommentContractView;

    private MarketUsedDetailCommentPresenter marketUsedDetailCommentPresenter;
    private ApiCallback apiCallback;
    private Comment comment;
    private String content;
    private Item item;
    private int marketId;

    @Before
    public void setupMarketUsedDetailCommentPresenter() {
        MockitoAnnotations.initMocks(this);
        marketUsedDetailCommentPresenter = new MarketUsedDetailCommentPresenter(marketCreateCommentContractView, marketUsedInteractor);
        comment = new Comment();
        comment.setCommentUid(123);
        comment.setContent("TEST COMMENT CONTENT");
        content = "TEST CONTENT";
        item = new Item();
        item.setId(111);
        marketId = 7777;
    }

    @Test
    public void createPresenter_setsThePresenterToView() {
        marketUsedDetailCommentPresenter = new MarketUsedDetailCommentPresenter(marketCreateCommentContractView, marketUsedInteractor);
        verify(marketCreateCommentContractView).setPresenter(marketUsedDetailCommentPresenter);
    }

    /* Read Market Detail */
    @Test
    public void loadMarketDetailFromServerAndLoadIntoView() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(item);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.readMarketDetail(marketId);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).onMarketDataReceived(item);
        verify(marketCreateCommentContractView).hideLoading();
    }

    @Test
    public void errorMarketDetailFromServer_WrongObject_ShowsErrorToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.readMarketDetail(marketId);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketDataReceivedFail();
        verify(marketCreateCommentContractView).hideLoading();
    }

    @Test
    public void errorMarketDetailFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(1);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).readMarketDetail(anyInt(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.readMarketDetail(marketId);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketDataReceivedFail();
        verify(marketCreateCommentContractView).hideLoading();
    }

    /* Create Comment */
    @Test
    public void createCommentFromServer_ShowsSuccessToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).createCommentDetail(anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.createComment(marketId, content);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketCommentUpdate();
        verify(marketCreateCommentContractView).hideLoading();
    }

    @Test
    public void errorCreateCommentFromServer_NullContent_ShowsErrorToastMessage() {
        content = null;
        marketUsedDetailCommentPresenter.createComment(marketId, content);
        verify(marketCreateCommentContractView).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorCreateCommentFromServer_EmptyContent_ShowsErrorToastMessage() {
        content = "";
        marketUsedDetailCommentPresenter.createComment(marketId, content);
        verify(marketCreateCommentContractView).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorCreateCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).createCommentDetail(anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.createComment(marketId, content);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketCommentUpdateFail();
        verify(marketCreateCommentContractView).hideLoading();
    }

    /* Delete Comment */
    @Test
    public void deleteCommentFromServer_ShowsSuccessToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).deleteCommentDetail(anyInt(), anyInt(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.deleteComment(comment, item);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketCommentDelete();
        verify(marketCreateCommentContractView).hideLoading();
    }

    @Test
    public void errorDeleteCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(2);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).deleteCommentDetail(anyInt(), anyInt(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.deleteComment(comment, item);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketCommentDeleteFail();
        verify(marketCreateCommentContractView).hideLoading();
    }

    /* Edit Comment */
    @Test
    public void editCommentFromServer_ShowsSuccessToastMessage() {
        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onSuccess(comment);
            return null;
        }).when(marketUsedInteractor).editCommentDetail(anyInt(), anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.editComment(comment, item, content);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketCommentEdit();
        verify(marketCreateCommentContractView).hideLoading();
    }

    @Test
    public void errorEditCommentFromServer_NullCommentContent_ShowsErrorToastMessage() {
        comment.setContent(null);
        marketUsedDetailCommentPresenter.editComment(comment, item, content);
        verify(marketCreateCommentContractView).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorEditCommentFromServer_EmptyCommentContent_ShowsErrorToastMessage() {
        comment.setContent("");
        marketUsedDetailCommentPresenter.editComment(comment, item, content);
        verify(marketCreateCommentContractView).showMessage(R.string.market_used_content_check);
    }

    @Test
    public void errorEditCommentFromServer_ShowsErrorToastMessage() {
        Exception exception = new Exception();

        doAnswer(invocation -> {
            apiCallback = invocation.getArgument(3);
            apiCallback.onFailure(exception);
            return null;
        }).when(marketUsedInteractor).editCommentDetail(anyInt(), anyInt(), anyString(), any(ApiCallback.class));

        marketUsedDetailCommentPresenter.editComment(comment, item, content);

        verify(marketCreateCommentContractView).showLoading();
        verify(marketCreateCommentContractView).showMarketCommentEditFail();
        verify(marketCreateCommentContractView).hideLoading();
    }
}
