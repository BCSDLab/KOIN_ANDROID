package in.koreatech.koin.ui.event;

public class EventDetailActivityextends extends KoinEditorActivity implements AdDetailContract.View {

    private AdDetailPresenter adDetailPresenter;
    private Context context;
    private RecyclerView.LayoutManager layoutManager;
    private AdvertisingCommentAdapter commentRecyclerAdapter;
    private GenerateProgressTask adDetailGenerateProgress;
    private AdDetail adDetailInfo;
    private InputMethodManager commentInputManager;

    @BindView(R.id.advertising_detail_scrollview)
    NestedScrollView scrollView;
    @BindView(R.id.advertising_detail_title_textview)
    TextView titleTextview;
    @BindView(R.id.advertising_detail_period_textview)
    TextView periodTextview;
    @BindView(R.id.advertising_detail_view_count_publisher_textview)
    TextView viewPublisherTextview;
    @BindView(R.id.advertising_detail_publish_date_textview)
    TextView publishDateTextview;
    @BindView(R.id.advertising_detail_reply_count_textview)
    TextView replyCountTextview;
    @BindView(R.id.advertising_detail_view_count_textview)
    TextView viewCountTextview;
    @BindView(R.id.advertising_detail_page_edit_button)
    Button editButton;
    @BindView(R.id.advertising_detail_page_delete_button)
    Button deleteButton;

    // Comment View Binding
    @BindView(R.id.advertising_comment_recyclerview)
    RecyclerView detailRecyclerview;
    @BindView(R.id.advertising_comment_content_edittext)
    EditText commentEditText;
    @BindView(R.id.advertising_comment_create_button)
    Button commentCreateButton;
    @BindView(R.id.advertising_comment_erase_button)
    Button commentDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.advertising_detail_page_activity);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        adDetailInfo = new AdDetail();
        adDetailInfo.id = getIntent().getIntExtra("ID", -1);
        adDetailInfo.grantEdit = getIntent().getBooleanExtra("GRANT_EDIT", false);

        if (adDetailInfo.id == -1) {
            ToastUtil.makeShortToast(context, "이벤트 정보를 불러오지 못했습니다.");
            finish();
        }

        init();
    }

    @Override
    protected int getRichEditorId() {
        return R.id.advertising_detail_contents;
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
        setPresenter(new AdDetailPresenter(this, new AdDetailRestInterator()));
        layoutManager = new LinearLayoutManager(this);
        if (adDetailPresenter != null) {
            adDetailPresenter.getAdDetailInfo(adDetailInfo.id);
        }
        commentInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void initView() {
        commentRecyclerAdapter = new AdvertisingCommentAdapter(context, adDetailInfo.comments);
        detailRecyclerview.setHasFixedSize(true);
        detailRecyclerview.setLayoutManager(layoutManager);
        detailRecyclerview.setAdapter(commentRecyclerAdapter);
        detailRecyclerview.setNestedScrollingEnabled(false);
        titleTextview.setText(adDetailInfo.eventTitle);
        periodTextview.setText(adDetailInfo.startDate + " ~ " + adDetailInfo.endDate);
        viewPublisherTextview.setText("조회 " + adDetailInfo.getHit() + " · " + adDetailInfo.getNickname());
        renderEditor(renderHtmltoString(adDetailInfo.content));
        replyCountTextview.setText(adDetailInfo.comentCount + "");
        viewCountTextview.setText(adDetailInfo.hit + "");
        publishDateTextview.setText(adDetailInfo.createdAt);

        if (adDetailInfo.grantEdit) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        if (getAuthority() != AuthorizeConstant.ANONYMOUS) {
            commentEditText.setFocusable(true);
            commentEditText.setFocusableInTouchMode(true);
            commentEditText.setHint("댓글을 작성해주세요.");
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
        adDetailGenerateProgress = new GenerateProgressTask(this, "로딩 중...");
        adDetailGenerateProgress.execute();
    }

    @Override
    public void hideLoading() {
        adDetailGenerateProgress.cancel(true);
    }

    @Override
    public void onAdDetailDataReceived(AdDetail adDetail) {
        adDetailInfo.title = adDetail.title;
        adDetailInfo.eventTitle = adDetail.eventTitle;
        adDetailInfo.startDate = adDetail.startDate;
        adDetailInfo.endDate = adDetail.endDate;
        adDetailInfo.content = adDetail.content;
        adDetailInfo.comentCount = adDetail.comentCount;
        adDetailInfo.hit = adDetail.hit;
        adDetailInfo.comments = adDetail.comments; //댓글 ArrayList
        adDetailInfo.thumbnail = adDetail.thumbnail;
        adDetailInfo.shopId = adDetail.shopId;
        adDetailInfo.userId = adDetail.userId;
        adDetailInfo.nickname = adDetail.nickname;
        adDetailInfo.createdAt = adDetail.createdAt;

        initView();
    }

    @Override
    public void onAdDetailDeleteCompleted(boolean inSuccess) {
        finish();
    }

    @Override
    public void setPresenter(AdDetailPresenter presenter) {
        this.adDetailPresenter = presenter;
    }

    @Override
    public void showMessage(String msg) {
        ToastUtil.makeShortToast(context, msg);
    }

    /**
     * Comment를 추가했을 때 Comment 수와 Comment 리사이클러 뷰를 새로고침하고,
     * 키보드를 내리면서 댓글 입력창의 포커스를 없애는 메소드
     *
     * @param comment 새로 추가한 댓글
     */
    @Override
    public void onAdDetailCommentReceived(Comment comment) {
        adDetailInfo.comments.add(comment);
        commentRecyclerAdapter.notifyDataSetChanged();
        replyCountTextview.setText(Integer.toString(adDetailInfo.comments.size()));
        commentEditText.clearFocus();
        commentEditText.setText("");
        commentInputManager.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
    }

    @Override
    public void onAdDetailCommentDeleted(boolean isSuccess) {
        Intent intent = new Intent(this, AdvertisingDetailActivity.class);
        intent.putExtra("ID", adDetailInfo.id);
        intent.putExtra("GRANT_EDIT", adDetailInfo.grantEdit);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId()) {
            onBackPressed();
        }
    }

    // [목록으로] 버튼 클릭 메소드
    @OnClick(R.id.advertising_detail_back_list_button)
    public void goBackListClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick({R.id.advertising_detail_page_edit_button, R.id.advertising_detail_page_delete_button})
    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.advertising_detail_page_edit_button:
                onClickEditButton();
                break;
            case R.id.advertising_detail_page_delete_button:
                onClickDeleteButton();
                break;
        }
    }

    @OnClick({R.id.advertising_comment_content_edittext, R.id.advertising_comment_create_button, R.id.advertising_comment_erase_button})
    public void onClickCommentView(View view) {
        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
        } else {
            switch (view.getId()) {
                case R.id.advertising_comment_content_edittext:
                    scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    break;
                case R.id.advertising_comment_create_button:
                    onClickCommentCreate();
                    break;
                case R.id.advertising_comment_erase_button:
                    onClickCommentErase();
                    break;
            }
        }
    }

    public void onClickCommentCreate() {
        if (commentEditText.getText().toString().isEmpty()) {
            ToastUtil.makeShortToast(this, "댓글을 입력해주세요.");
        } else {
            Comment comment = new Comment();
            comment.content = commentEditText.getText().toString();
            adDetailPresenter.createComment(adDetailInfo.id, comment);
        }
    }

    public void onClickCommentErase() {
        if (!commentEditText.getText().toString().isEmpty()) {
            SnackbarUtil.makeLongSnackbarActionYes(scrollView, "입력한 댓글을 지우시겠습니까?", () -> commentEditText.setText(""));
        }
    }

    // [삭제] 버튼 클릭 시 해당 글을 삭제 시켜주는 메소드
    private void onClickDeleteButton() {
        SnackbarUtil.makeLongSnackbarActionYes(scrollView, "게시글을 삭제할까요?\n댓글도 모두 사라집니다.", () -> adDetailPresenter.deleteAdDetail(adDetailInfo.id));
    }

    // [수정] 버튼 클릭 시 AdvertisingCreateActivity 로 이동하여 글 수정
    private void onClickEditButton() {
        Intent intent = new Intent(this, AdvertisingCreateActivity.class);
        intent.putExtra("ID", adDetailInfo.id);
        intent.putExtra("IS_EDIT", adDetailInfo.grantEdit);
        intent.putExtra("TITLE", adDetailInfo.title);
        intent.putExtra("EVENT_TITLE", adDetailInfo.eventTitle);
        intent.putExtra("START_DATE", adDetailInfo.startDate);
        intent.putExtra("END_DATE", adDetailInfo.endDate);
        intent.putExtra("CONTENT", adDetailInfo.content);
        intent.putExtra("SHOP_ID", adDetailInfo.shopId);
        startActivity(intent);

        finish();
    }
}