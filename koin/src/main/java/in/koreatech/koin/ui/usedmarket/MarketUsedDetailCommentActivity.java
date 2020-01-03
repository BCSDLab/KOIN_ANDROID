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
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.core.constant.AuthorizeConstant;
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
    AppbarBase mAppBarBase;
    @BindView(R.id.market_used_comment_title)
    TextView mMarketUsedCommentTitle;
    @BindView(R.id.market_used_comment_view_count_korean)
    TextView mMarketUsedCommentViewCountKorean;
    @BindView(R.id.market_used_comment_view_count)
    TextView mMarketUsedCommentViewCount;
    @BindView(R.id.market_used_comment_writer)
    TextView mMarketUsedCommentWriter;
    @BindView(R.id.market_used_comment_create_date)
    TextView mMarketUsedCommentCreateDate;
    @BindView(R.id.market_used_comment_content_recyclerview)
    RecyclerView mMarketUsedCommentContentRecyclerview;
    @BindView(R.id.market_used_comment_nickname_edittext)
    EditText mMarketUsedCommentNicknameEdittext;
    @BindView(R.id.market_used_comment_nickname_linearlayout)
    LinearLayout mMarketUsedCommentNicknameLinearlayout;
    @BindView(R.id.market_used_comment_content_edittext)
    EditText mMarketUsedCommentContentEdittext;
    @BindView(R.id.market_used_comment_content_linearlayout)
    LinearLayout mMarketUsedCommentContentLinearlayout;
    @BindView(R.id.market_used_comment_cancel_button)
    Button mMarketusedCommentCancelButton;
    @BindView(R.id.market_used_comment_register_button)
    Button mMarketusedCommentRegisterButton;
    @BindView(R.id.market_used_comment_scrollview)
    NestedScrollView mMarketusedCommentScrollview;
    @BindView(R.id.market_used_comment_comment_edit_border)
    LinearLayout mMarketUsedCommentLinearlayout;
    @BindView(R.id.aritcle_comment_anonymous_cancel_delete_edit_linearlayout)
    LinearLayout mMarketUsedCommentAnoymousCancelDeleteEditLinearlayout;
    @BindView(R.id.market_used_comment_password_linearlayout)
    LinearLayout mAritcleCommentPasswordLinearlayout;
    @BindView(R.id.market_used_comment_cancel_register_linearlayout)
    LinearLayout mAritcleCommentCancelRegisterLayout;
    @BindView(R.id.market_used_comment_password_edittext)
    EditText mMarketUsedCommentPasswordEdittext;


    private MarketUsedDetailCommentAdapter mMarketDetailCommentRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager
    private Item mItem; //품목 정보 저장
    private int mMarketID;
    private boolean mGrantedCheck; // 글쓴이 인지 확인
    private boolean mIsEditPossible;
    private boolean mIsEditComment;
    private ArrayList<Comment> mCommentArrayList;
    private MarketUsedCommentContract.Presenter mMarketUsedDetatailCommentPresenter;
    private Context mContext;
    private Comment mSelectedComment;
    private CustomProgressDialog customProgressDialog;

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
        mMarketUsedCommentNicknameEdittext.setFocusable(false);
        mMarketUsedCommentNicknameEdittext.setClickable(false);
        if (mItem.id != -1)
            mMarketUsedDetatailCommentPresenter.readMarketDetail(mItem.id);
        if (getUser() != null && getUser().userNickName != null) {
            mMarketUsedCommentNicknameEdittext.setText(getUser().userNickName);
            mMarketUsedCommentContentEdittext.setFocusableInTouchMode(true);
            mIsEditPossible = true;
            mMarketUsedCommentNicknameEdittext.setText(getUser().userNickName);
        } else {
            mMarketUsedCommentContentEdittext.setClickable(false);
            mMarketUsedCommentContentEdittext.setFocusable(false);
            mIsEditPossible = false;
        }
    }

    public void init() {
        mItem = new Item();
        mContext = this;
        mCommentArrayList = new ArrayList<>();
        mAritcleCommentPasswordLinearlayout.setVisibility(View.GONE);
        mItem.id = getIntent().getIntExtra("ITEM_ID", -1);
        mMarketID = getIntent().getIntExtra("MARKET_ID", 0);
        if (mMarketID == 0)
            mAppBarBase.setTitleText("팝니다");
        else
            mAppBarBase.setTitleText("삽니다");
        mMarketUsedDetatailCommentPresenter = new MarketUsedDetailCommentPresenter(this, new MarketUsedRestInteractor());
        mMarketDetailCommentRecyclerAdapter = new MarketUsedDetailCommentAdapter(this, mCommentArrayList);
        mLayoutManager = new LinearLayoutManager(this);
        mMarketUsedCommentContentRecyclerview.setHasFixedSize(true);
        mMarketUsedCommentContentRecyclerview.setLayoutManager(mLayoutManager); //layout 설정
        mMarketUsedCommentContentRecyclerview.setAdapter(mMarketDetailCommentRecyclerAdapter); //adapter 설정
        mMarketDetailCommentRecyclerAdapter.setCustomOnClickListener(this);
    }

    @Override
    public void onMarketDataReceived(Item item) {
        String styledText;
        mItem = item;
        mCommentArrayList.clear();
        if (item.comments != null && item.comments.size() > 0) {
            styledText = item.title + "<font color='#175c8e'>(" + item.comments.size() + ")</font>";
            mMarketUsedCommentTitle.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE); // Title set
        } else
            mMarketUsedCommentTitle.setText(item.title);
        mMarketUsedCommentViewCount.setText(item.hit);
        mMarketUsedCommentWriter.setText(item.nickname);
        mMarketUsedCommentCreateDate.setText(item.createdAt);
        mCommentArrayList.addAll(item.comments);
        mMarketDetailCommentRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void showLoading() {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(this, "로딩 중");
            customProgressDialog.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (customProgressDialog != null) {
            customProgressDialog.cancel(true);
            customProgressDialog = null;
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppbarBase.getLeftButtonId())
            finish();
        else if (id == AppbarBase.getRightButtonId())
            createActivityMove();

    }

    public void createActivityMove() {
        Intent intent;
        if (mMarketID == 0)
            intent = new Intent(this, MarketUsedSellCreateActivity.class);
        else
            intent = new Intent(this, MarketUsedBuyCreateActivity.class);

        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        }
        if (getUser().userNickName != null)
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
        if (mItem.id != -1)
            mMarketUsedDetatailCommentPresenter.readMarketDetail(mItem.id);
    }

    @Override
    public void showMarketCommentUpdateFail() {
        ToastUtil.getInstance().makeShort("댓글이 등록되지 않았습니다.");
    }

    @Override
    public void showMarketCommentDelete() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 성공하였습니다.");
        if (mItem.id != -1)
            mMarketUsedDetatailCommentPresenter.readMarketDetail(mItem.id);
    }

    @Override
    public void showMarketCommentDeleteFail() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 실패하였습니다.");
    }

    @Override
    public void showMarketCommentEdit() {
        ToastUtil.getInstance().makeShort("댓글 수정에 성공하였습니다.");
        onClickedCancelButton();
        if (mItem.id != -1)
            mMarketUsedDetatailCommentPresenter.readMarketDetail(mItem.id);
    }

    @Override
    public void showMarketCommentEditFail() {
        ToastUtil.getInstance().makeShort("댓글 수정에 실패하였습니다.");
    }


    @Override
    public void setPresenter(MarketUsedCommentContract.Presenter presenter) {
        mMarketUsedDetatailCommentPresenter = presenter;
    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
        SnackbarUtil.makeLongSnackbarActionYes(mMarketUsedCommentContentRecyclerview, "삭제하시겠습니까?", () ->
                mMarketUsedDetatailCommentPresenter.deleteComment(comment, mItem));
    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
        mIsEditComment = true;
        mMarketUsedCommentContentEdittext.setText(comment.content);
        mSelectedComment = comment;
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
        String nickname = (getUser() != null) ? getUser().userNickName : null;
        AuthorizeConstant authorizeConstant = getAuthority();
        if (!mIsEditPossible) {
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
                mMarketUsedCommentContentEdittext.requestFocus();
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
        mMarketUsedCommentContentEdittext.setText("");
        mIsEditComment = false;
    }

    public void onClickedCommentRegisterButton() {
        String commentContent = mMarketUsedCommentContentEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.getInstance().makeShort("내용을 입력해주세요.");
            return;
        }
        if (!mIsEditComment) {
            mMarketUsedDetatailCommentPresenter.createComment(mItem.id, commentContent);
        } else {
            mSelectedComment.content = commentContent;
            mMarketUsedDetatailCommentPresenter.editComment(mSelectedComment, mItem, mSelectedComment.content);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
