package in.koreatech.koin.ui.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Event;
import in.koreatech.koin.data.network.interactor.EventRestInteractor;
import in.koreatech.koin.ui.board.KoinEditorActivity;
import in.koreatech.koin.ui.board.KoinRichEditor;
import in.koreatech.koin.ui.event.adapter.EventCommentAdapter;
import in.koreatech.koin.ui.event.presenter.EventDetailContract;
import in.koreatech.koin.ui.event.presenter.EventDetailPresenter;
import in.koreatech.koin.ui.store.StoreDetailActivity;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.util.TimeUtil;

public class EventDetailActivity extends KoinEditorActivity implements EventDetailContract.View, EventCommentAdapter.OnCommentEditDeleteButtonClickListener {

    private EventDetailPresenter eventDetailPresenter;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;
    private EventCommentAdapter commentRecyclerAdapter;
    private Event eventDetail;
    private InputMethodManager commentInputManager;
    private boolean grantEdit;

    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseBar;
    @BindView(R.id.event_detail_scrollview)
    NestedScrollView scrollView;
    @BindView(R.id.event_detail_title_textview)
    TextView titleTextview;
    @BindView(R.id.event_detail_period_textview)
    TextView periodTextview;
    @BindView(R.id.event_detail_view_count_publisher_textview)
    TextView viewPublisherTextview;
    @BindView(R.id.event_detail_publish_date_textview)
    TextView createdAtTextview;
    @BindView(R.id.event_detail_content)
    KoinRichEditor contentEditor;
    @BindView(R.id.event_detail_reply_count_textview)
    TextView replyCountTextview;
    @BindView(R.id.event_detail_view_count_textview)
    TextView viewCountTextview;
    @BindView(R.id.event_detail_page_edit_button)
    Button editButton;
    @BindView(R.id.event_detail_page_delete_button)
    Button deleteButton;
    @BindView(R.id.event_detail_closedmark_textview)
    TextView closedMark;

    // Comment View Binding
    @BindView(R.id.event_comment_recyclerview)
    RecyclerView commentRecyclerview;
    @BindView(R.id.event_comment_content_edittext)
    EditText commentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.event_detail_activity);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        eventDetail = new Event();
        eventDetail.setId(getIntent().getIntExtra("ID", -1));
        grantEdit = getIntent().getBooleanExtra("GRANT_EDIT", false);

        if (eventDetail.getId() == -1) {
            ToastUtil.getInstance().makeShort("이벤트 정보를 불러오지 못했습니다.");
            finish();
        }

        init();
    }

    @Override
    protected int getRichEditorId() {
        return R.id.event_detail_content;
    }

    @Override
    protected boolean isEditable() {
        return false;
    }

    @Override
    protected void successImageProcessing(File imageFile, String uuid) {

    }

    private void init() {
        context = this;
        koinBaseBar.setTitleText("홍보게시판");
        setPresenter(new EventDetailPresenter(this, new EventRestInteractor()));
        layoutManager = new LinearLayoutManager(this);
        if (eventDetailPresenter != null) {
            eventDetailPresenter.getEventDetail(eventDetail.getId());
        }
        commentInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void updateView() {
        initRecyclerView();
        titleTextview.setText(eventDetail.getEventTitle());
        periodTextview.setText(eventDetail.getStartDate() + " ~ " + eventDetail.getEndDate());
        viewPublisherTextview.setText("조회 " + eventDetail.getHit() + " · " + eventDetail.getNickname());
        replyCountTextview.setText(eventDetail.getCommentCount() + "");
        viewCountTextview.setText(eventDetail.getHit() + "");
        createdAtTextview.setText(eventDetail.getCreatedAt());
        eraseEditorText();
        renderEditor(renderHtmltoString(eventDetail.getContent()));

        // 수정,삭제 권한이 있을 경우 [수정],[삭제] 버튼 시각화
        if (eventDetail.isGrantEdit()) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        // 익명이 아닐 경우 댓글 입력 활성화
        if (getAuthority() != AuthorizeConstant.ANONYMOUS) {
            commentEditText.setFocusable(true);
            commentEditText.setFocusableInTouchMode(true);
            commentEditText.setHint("댓글을 작성해주세요.");
        }

        checkEventClosed();
    }

    private void initRecyclerView() {
        commentRecyclerAdapter = new EventCommentAdapter(eventDetail.getComments(), eventDetail.getUserId());
        commentRecyclerAdapter.setButtonClickListener(this);
        commentRecyclerview.setHasFixedSize(true);
        commentRecyclerview.setLayoutManager(layoutManager);
        commentRecyclerview.setAdapter(commentRecyclerAdapter);
        commentRecyclerview.setNestedScrollingEnabled(false);
    }

    private void checkEventClosed() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date endDate = formatter.parse(eventDetail.getEndDate());
            Date currentDate = formatter.parse(TimeUtil.getDeviceCreatedDateOnlyString());

            if(endDate.getTime() - currentDate.getTime() <= 0)
                closedMark.setVisibility(View.VISIBLE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void onEventDataReceived(Event event) {
        eventDetail.setTitle(event.getTitle());
        eventDetail.setEventTitle(event.getEventTitle());
        eventDetail.setGrantEdit(grantEdit);
        eventDetail.setStartDate(event.getStartDate());
        eventDetail.setEndDate(event.getEndDate());
        eventDetail.setContent(event.getContent());
        eventDetail.setCommentCount(event.getCommentCount());
        eventDetail.setHit(event.getHit());
        eventDetail.setComments(event.getComments());  // 댓글 ArrayList
        eventDetail.setThumbnail(event.getThumbnail());
        eventDetail.setShopId(event.getShopId());
        eventDetail.setUserId(event.getUserId());
        eventDetail.setNickname(event.getNickname());
        eventDetail.setCreatedAt(event.getCreatedAt());

        updateView();
    }

    @Override
    public void onEventDeleteCompleted(boolean inSuccess) {
        finish();
    }

    @Override
    public void setPresenter(EventDetailPresenter presenter) {
        this.eventDetailPresenter = presenter;
    }

    @Override
    public void showMessage(String msg) {
        ToastUtil.getInstance().makeShort(msg);
    }

    /**
     * Comment를 추가했을 때 Comment 수와 Comment 리사이클러 뷰를 새로고침하고,
     * 키보드를 내리고, 댓글 입력창의 포커스를 없애는 메소드
     *
     * @param comment 새로 추가한 댓글
     */
    @Override
    public void onEventCommentReceived(Comment comment) {
        eventDetailPresenter.getEventDetail(eventDetail.getId());
        commentEditText.clearFocus();
        commentEditText.setText("");
        commentInputManager.hideSoftInputFromWindow(scrollView.getWindowToken(), 0);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public void onEventCommentDeleted(boolean isSuccess) {
        eventDetailPresenter.getEventDetail(eventDetail.getId());
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        }
    }

    /**
     * [목록으로] 버튼 클릭 메소드
     */
    @OnClick(R.id.event_detail_back_list_button)
    public void goBackListClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 해당 글의 [수정], [삭제] 버튼 클릭 리스너
     * @param view
     */
    @OnClick({R.id.event_detail_page_edit_button, R.id.event_detail_page_delete_button})
    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.event_detail_page_edit_button:
                onClickEditButton();
                break;
            case R.id.event_detail_page_delete_button:
                onClickDeleteButton();
                break;
        }
    }

    /**
     * [삭제] 버튼 클릭 시 해당 글을 삭제 시켜주는 메소드
     */
    private void onClickDeleteButton() {
        SnackbarUtil.makeLongSnackbarActionYes(scrollView, "게시글을 삭제할까요?\n댓글도 모두 사라집니다.", () -> eventDetailPresenter.deleteEvent(eventDetail.getId()));
    }

    /**
     * [수정] 버튼 클릭 시 EventCreateActivity 로 이동하여 글 수정
     */
    private void onClickEditButton() {
        Intent intent = new Intent(this, EventCreateActivity.class);
        intent.putExtra("ID", eventDetail.getId());
        intent.putExtra("IS_EDIT", eventDetail.isGrantEdit());
        intent.putExtra("TITLE", eventDetail.getTitle());
        intent.putExtra("EVENT_TITLE", eventDetail.getEventTitle());
        intent.putExtra("START_DATE", eventDetail.getStartDate());
        intent.putExtra("END_DATE", eventDetail.getEndDate());
        intent.putExtra("CONTENT", eventDetail.getContent());
        intent.putExtra("SHOP_ID", eventDetail.getShopId());
        startActivity(intent);

        finish();
    }

    /**
     * 댓글 입력란, [등록] 버튼, [취소] 버튼 클릭 리스너
     * @param view 지정한 View
     */
    @OnClick({R.id.event_comment_content_edittext, R.id.event_comment_create_button, R.id.event_comment_erase_button})
    public void onClickCommentView(View view) {
        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
        } else {
            switch (view.getId()) {
                case R.id.event_comment_create_button:
                    onClickCommentCreate();
                    break;
                case R.id.event_comment_erase_button:
                    onClickCommentErase();
                    break;
            }
        }
    }

    /**
     * 댓글의 [등록] 버튼 클릭했을 때 실행되는 메소드
     */
    public void onClickCommentCreate() {
        if (commentEditText.getText().toString().isEmpty()) {
            ToastUtil.getInstance().makeShort("댓글을 입력해주세요.");
        } else {
            Comment comment = new Comment();
            comment.setContent(commentEditText.getText().toString().trim());
            eventDetailPresenter.createComment(eventDetail.getId(), comment);
        }
    }

    /**
     * 댓글의 [취소] 버튼 클릭했을 때 실행되는 메소드
     */
    public void onClickCommentErase() {
        if (!commentEditText.getText().toString().isEmpty()) {
            SnackbarUtil.makeLongSnackbarActionYes(scrollView, "입력하신 댓글을 지우시겠습니까?", () -> commentEditText.setText(""));
        }
    }

    @Override
    public void onClickEditButton(Comment comment, String content) {
        if(content.isEmpty()) {
            ToastUtil.getInstance().makeShort("댓글을 입력해주세요.");
        } else {
            comment.setContent(content);
            eventDetailPresenter.updateComment(comment);
        }
    }

    @Override
    public void onClickDeleteButton(Comment comment) {
        SnackbarUtil.makeLongSnackbarActionYes(scrollView, "댓글을 삭제하시겠습니까?", () -> eventDetailPresenter.deleteComment(comment));
    }

    @OnClick(R.id.event_detail_order_button)
    public void onClickOrderButton(View view) {
        Intent intent = new Intent(context, StoreDetailActivity.class);
        intent.putExtra("STORE_UID", eventDetail.getShopId());
        startActivity(intent);
    }
}