package in.koreatech.koin.ui.usedmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedCommentContract;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedDetailCommentAdapter;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailCommentPresenter;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;
import in.koreatech.koin.ui.userinfo.UserInfoEditedActivity;


public class MarketUsedDetailCommentActivity extends KoinNavigationDrawerActivity implements MarketUsedCommentContract.View, MarketUsedDetailCommentAdapter.OnCommentRemoveButtonClickListener {

    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appBarBase;
    @BindView(R.id.market_used_comment_title)
    TextView marketUsedCommentTitle;
    @BindView(R.id.market_used_comment_view_count_korean)
    TextView marketUsedCommentViewCountKorean;
    @BindView(R.id.market_used_comment_view_count)
    TextView marketUsedCommentViewCount;
    @BindView(R.id.market_used_comment_writer)
    TextView marketUsedCommentWriter;
    @BindView(R.id.market_used_comment_create_date)
    TextView marketUsedCommentCreateDate;
    @BindView(R.id.market_used_comment_content_recyclerview)
    RecyclerView marketUsedCommentContentRecyclerview;
    @BindView(R.id.market_used_comment_nickname_edittext)
    EditText marketUsedCommentNicknameEdittext;
    @BindView(R.id.market_used_comment_nickname_linearlayout)
    LinearLayout marketUsedCommentNicknameLinearlayout;
    @BindView(R.id.market_used_comment_content_edittext)
    EditText marketUsedCommentContentEdittext;
    @BindView(R.id.market_used_comment_content_linearlayout)
    LinearLayout marketUsedCommentContentLinearlayout;
    @BindView(R.id.market_used_comment_cancel_button)
    Button marketusedCommentCancelButton;
    @BindView(R.id.market_used_comment_register_button)
    Button marketusedCommentRegisterButton;
    @BindView(R.id.market_used_comment_scrollview)
    NestedScrollView marketusedCommentScrollview;
    @BindView(R.id.market_used_comment_comment_edit_border)
    LinearLayout marketUsedCommentLinearlayout;
    @BindView(R.id.aritcle_comment_anonymous_cancel_delete_edit_linearlayout)
    LinearLayout marketUsedCommentAnoymousCancelDeleteEditLinearlayout;
    @BindView(R.id.market_used_comment_password_linearlayout)
    LinearLayout aritcleCommentPasswordLinearlayout;
    @BindView(R.id.market_used_comment_cancel_register_linearlayout)
    LinearLayout aritcleCommentCancelRegisterLayout;
    @BindView(R.id.market_used_comment_password_edittext)
    EditText marketUsedCommentPasswordEdittext;


    private MarketUsedDetailCommentAdapter marketDetailCommentRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager
    private Item item; //품목 정보 저장
    private int marketID;
    private boolean isEditPossible;
    private boolean isEditComment;
    private ArrayList<Comment> commentArrayList;
    private MarketUsedDetailCommentPresenter marketUsedDetatailCommentPresenter;
    private Context context;
    private Comment SelectedComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_used_comment_activity);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        marketUsedCommentNicknameEdittext.setFocusable(false);
        marketUsedCommentNicknameEdittext.setClickable(false);
        if (item.getId() != -1)
            marketUsedDetatailCommentPresenter.readMarketDetail(item.getId());
        if (getUser() != null && getUser().getUserNickName() != null) {
            marketUsedCommentNicknameEdittext.setText(getUser().getUserNickName());
            marketUsedCommentContentEdittext.setFocusableInTouchMode(true);
            isEditPossible = true;
            marketUsedCommentNicknameEdittext.setText(getUser().getUserNickName());
        } else {
            marketUsedCommentContentEdittext.setClickable(false);
            marketUsedCommentContentEdittext.setFocusable(false);
            isEditPossible = false;
        }
    }

    public void init() {
        item = new Item();
        context = this;
        commentArrayList = new ArrayList<>();
        aritcleCommentPasswordLinearlayout.setVisibility(View.GONE);
        item.setId(getIntent().getIntExtra("ITEM_ID", -1));
        marketID = getIntent().getIntExtra("MARKET_ID", 0);
        if (marketID == 0)
            appBarBase.setTitleText("팝니다");
        else
            appBarBase.setTitleText("삽니다");
        marketUsedDetatailCommentPresenter = new MarketUsedDetailCommentPresenter(this, new MarketUsedRestInteractor());
        this.marketDetailCommentRecyclerAdapter = new MarketUsedDetailCommentAdapter(this, commentArrayList);
        layoutManager = new LinearLayoutManager(this);
        marketUsedCommentContentRecyclerview.setHasFixedSize(true);
        marketUsedCommentContentRecyclerview.setLayoutManager(layoutManager); //layout 설정
        marketUsedCommentContentRecyclerview.setAdapter(this.marketDetailCommentRecyclerAdapter); //adapter 설정
        this.marketDetailCommentRecyclerAdapter.setCustomOnClickListener(this);
    }

    @Override
    public void onMarketDataReceived(Item item) {
        String styledText;
        item = item;
        commentArrayList.clear();
        if (item.getComments() != null && item.getComments().size() > 0) {
            styledText = item.getTitle() + "<font color='#175c8e'>(" + item.getComments().size() + ")</font>";
            marketUsedCommentTitle.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE); // Title set
        } else
            marketUsedCommentTitle.setText(item.getTitle());
        marketUsedCommentViewCount.setText(item.getHit());
        marketUsedCommentWriter.setText(item.getNickname());
        marketUsedCommentCreateDate.setText(item.getCreatedAt());
        commentArrayList.addAll(item.getComments());
        this.marketDetailCommentRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            finish();
        else if (id == AppBarBase.getRightButtonId())
            createActivityMove();

    }

    public void createActivityMove() {
        Intent intent;
        if (marketID == 0)
            intent = new Intent(this, MarketUsedSellCreateActivity.class);
        else
            intent = new Intent(this, MarketUsedBuyCreateActivity.class);

        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        }
        if (getUser().getUserNickName() != null)
            startActivity(intent);
        else {
            ToastUtil.getInstance().makeShort("닉네임이 필요합니다.");
            intent = new Intent(this, UserInfoEditedActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void showMarketDataReceivedFail() {

    }

    @Override
    public void showMarketCommentUpdate() {
        ToastUtil.getInstance().makeShort("댓글이 등록되었습니다.");
        onClickedCancelButton();
        if (item.getId() != -1)
            marketUsedDetatailCommentPresenter.readMarketDetail(item.getId());
    }

    @Override
    public void showMarketCommentUpdateFail() {
        ToastUtil.getInstance().makeShort("댓글이 등록되지 않았습니다.");
    }

    @Override
    public void showMarketCommentDelete() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 성공하였습니다.");
        if (item.getId() != -1)
            marketUsedDetatailCommentPresenter.readMarketDetail(item.getId());
    }

    @Override
    public void showMarketCommentDeleteFail() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 실패하였습니다.");
    }

    @Override
    public void showMarketCommentEdit() {
        ToastUtil.getInstance().makeShort("댓글 수정에 성공하였습니다.");
        onClickedCancelButton();
        if (item.getId() != -1)
            marketUsedDetatailCommentPresenter.readMarketDetail(item.getId());
    }

    @Override
    public void showMarketCommentEditFail() {
        ToastUtil.getInstance().makeShort("댓글 수정에 실패하였습니다.");
    }


    @Override
    public void setPresenter(MarketUsedDetailCommentPresenter presenter) {
        marketUsedDetatailCommentPresenter = presenter;
    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
        SnackbarUtil.makeLongSnackbarActionYes(marketUsedCommentContentRecyclerview, "삭제하시겠습니까?", () ->
                marketUsedDetatailCommentPresenter.deleteComment(comment, item));
    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
        this.isEditComment = true;
        marketUsedCommentContentEdittext.setText(comment.getContent());
        this.SelectedComment = comment;
    }

    public void showNickNameRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("닉네임이 필요합니다.")
                .setMessage("닉네임이 필요한 서비스입니다.\n닉네임을 추가 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    startActivity(new Intent(this, UserInfoActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    @OnClick({R.id.market_used_comment_nickname_edittext, R.id.market_used_comment_nickname_linearlayout,
            R.id.market_used_comment_content_edittext, R.id.market_used_comment_content_linearlayout,
            R.id.market_used_comment_cancel_button, R.id.market_used_comment_register_button})
    public void onViewClicked(View view) {
        String nickname = (getUser() != null) ? getUser().getUserNickName() : null;
        AuthorizeConstant authorizeConstant = getAuthority();
        if (!isEditPossible) {
            if (authorizeConstant == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (nickname == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        switch (view.getId()) {
            case R.id.market_used_comment_nickname_edittext:
                break;
            case R.id.market_used_comment_nickname_linearlayout:
                break;
            case R.id.market_used_comment_content_edittext:
                break;
            case R.id.market_used_comment_content_linearlayout:
                marketUsedCommentContentEdittext.requestFocus();
                break;
            case R.id.market_used_comment_cancel_button:
                onClickedCancelButton();
                break;
            case R.id.market_used_comment_register_button:
                onClickedCommentRegisterButton();
                break;
        }
    }

    public void onClickedCancelButton() {
        marketUsedCommentContentEdittext.setText("");
        this.isEditComment = false;
    }

    public void onClickedCommentRegisterButton() {
        String commentContent = marketUsedCommentContentEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.getInstance().makeShort("내용을 입력해주세요.");
            return;
        }
        if (!this.isEditComment) {
            marketUsedDetatailCommentPresenter.createComment(item.getId(), commentContent);
        } else {
            this.SelectedComment.setContent(commentContent);
            marketUsedDetatailCommentPresenter.editComment(this.SelectedComment, item, this.SelectedComment.getContent());
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
