package in.koreatech.koin.service_board.ui;

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
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
import in.koreatech.koin.core.util.SnackbarUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_board.adpaters.CommentRecyclerAdapter;
import in.koreatech.koin.service_board.contracts.ArticleCommentContract;
import in.koreatech.koin.service_board.presenters.ArticleCommentPresenter;
import in.koreatech.koin.ui.LoginActivity;
import in.koreatech.koin.ui.UserInfoActivity;

import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleCommentActivity extends KoinNavigationDrawerActivity implements ArticleCommentContract.View, CommentRecyclerAdapter.OnCommentRemoveButtonClickListener {

    @BindView(R.id.koin_base_app_bar_dark)
    KoinBaseAppbarDark mKoinBaseAppBarDark;
    @BindView(R.id.article_comment_title)
    TextView mArticleCommentTitle;
    @BindView(R.id.article_comment_view_count_korean)
    TextView mArticleCommentViewCountKorean;
    @BindView(R.id.article_comment_view_count)
    TextView mArticleCommentViewCount;
    @BindView(R.id.article_comment_writer)
    TextView mArticleCommentWriter;
    @BindView(R.id.article_comment_create_date)
    TextView mArticleCommentCreateDate;
    @BindView(R.id.article_comment_content_recyclerview)
    RecyclerView mArticleCommentContentRecyclerview;
    @BindView(R.id.article_comment_nickname_edittext)
    EditText mArticleCommentNicknameEdittext;
    @BindView(R.id.article_comment_nickname_linearlayout)
    LinearLayout mArticleCommentNicknameLinearlayout;
    @BindView(R.id.article_comment_content_edittext)
    EditText mArticleCommentContentEdittext;
    @BindView(R.id.article_comment_content_linearlayout)
    LinearLayout mArticleCommentContentLinearlayout;
    @BindView(R.id.article_comment_cancel_button)
    Button articleCommentCancelButton;
    @BindView(R.id.article_comment_register_button)
    Button articleCommentRegisterButton;
    @BindView(R.id.article_comment_scrollview)
    NestedScrollView articleCommentScrollview;
    @BindView(R.id.article_comment_comment_edit_border)
    LinearLayout mArticleCommentLinearlayout;
    @BindView(R.id.aritcle_comment_anonymous_cancel_delete_edit_linearlayout)
    LinearLayout mArticleCommentAnoymousCancelDeleteEditLinearlayout;
    @BindView(R.id.article_comment_password_linearlayout)
    LinearLayout mAritcleCommentPasswordLinearlayout;
    @BindView(R.id.article_comment_cancel_register_linearlayout)
    LinearLayout mAritcleCommentCancelRegisterLayout;
    @BindView(R.id.article_comment_password_edittext)
    EditText mArticleCommentPasswordEdittext;


    private final int REQ_CODE_ARTICLE_EDIT = 1;
    private CustomProgressDialog customProgressDialog;
    private CommentRecyclerAdapter mCommentRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Comment> mCommentArrayList;
    private Article mArticle;
    private Context mContext;
    private ArticleCommentPresenter mArticleCommentPresenter;
    private String mArticleCommentCount;
    private boolean mIsEditComment;
    private Comment mSelectedComment;
    private String mCommentPassword;
    private boolean mIsEditPossible;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);
        mContext = this;
        ButterKnife.bind(this);
        mArticle = new Article();
        mArticle.boardUid = getIntent().getIntExtra("BOARD_UID", 0);
        mArticle.articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String nickname = getNickname();
        AuthorizeConstant authorizeConstant = getAuthorize();
        if (mArticle.boardUid == ID_FREE || mArticle.boardUid == ID_RECRUIT)
            mArticleCommentPresenter.getArticle(mArticle.articleUid);
        else
            mArticleCommentPresenter.getAnonymousArticle(mArticle.articleUid);

        if (mArticle != null && (mArticle.boardUid == ID_FREE || mArticle.boardUid == ID_RECRUIT) && (nickname.isEmpty() || authorizeConstant == AuthorizeConstant.ANONYMOUS)) {
            mArticleCommentContentEdittext.setFocusable(false);
            mArticleCommentContentEdittext.setClickable(false);
            mIsEditPossible = false;
        } else {
            mArticleCommentContentEdittext.setFocusableInTouchMode(true);
            mIsEditPossible = true;
        }


    }

    public void init() {
        mIsEditComment = false;
        mIsEditPossible = true;
        setPresenter(new ArticleCommentPresenter(this, new CommunityRestInteractor()));
        mLayoutManager = new LinearLayoutManager(this);
        mCommentArrayList = new ArrayList<>();
        mCommentRecyclerAdapter = new CommentRecyclerAdapter(mContext, mCommentArrayList);
        mCommentRecyclerAdapter.setCustomOnClickListener(this);
        mArticleCommentContentRecyclerview.setHasFixedSize(true);
        mArticleCommentContentRecyclerview.setLayoutManager(mLayoutManager);
        mArticleCommentContentRecyclerview.setAdapter(mCommentRecyclerAdapter);
        mArticleCommentContentRecyclerview.setNestedScrollingEnabled(false);
        mArticleCommentContentRecyclerview.setHasFixedSize(false);

        switch (mArticle.boardUid) {
            case ID_FREE:
                mKoinBaseAppBarDark.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                mKoinBaseAppBarDark.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                mKoinBaseAppBarDark.setTitleText("익명게시판");
                break;
            default:
                mKoinBaseAppBarDark.setTitleText("게시판");
                break;
        }

        setVisibility(mArticle.boardUid);

        if (mArticle.boardUid != ID_ANONYMOUS) {
            mArticleCommentNicknameEdittext.setFocusable(false);
            mArticleCommentNicknameEdittext.setClickable(false);
        }

    }

    public void setVisibility(int boardUid) {
        if (boardUid != ID_ANONYMOUS) {
            mArticleCommentAnoymousCancelDeleteEditLinearlayout.setVisibility(View.GONE);
            mAritcleCommentPasswordLinearlayout.setVisibility(View.GONE);
            mAritcleCommentCancelRegisterLayout.setVisibility(View.VISIBLE);
        } else {
            mArticleCommentAnoymousCancelDeleteEditLinearlayout.setVisibility(View.VISIBLE);
            mAritcleCommentPasswordLinearlayout.setVisibility(View.VISIBLE);
            mAritcleCommentCancelRegisterLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId())
            onClickCreateButton();

    }

    public void onClickCreateButton() {
        if (mArticle.boardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = getAuthorize();
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        Intent intent = new Intent(mContext, ArticleEditActivity.class);
        intent.putExtra("BOARD_UID", mArticle.boardUid);
        startActivityForResult(intent, REQ_CODE_ARTICLE_EDIT);

    }

    public AuthorizeConstant getAuthorize() {
        AuthorizeConstant authorizeConstant;
        try {
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            authorizeConstant = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
        }
        return authorizeConstant;
    }

    @OnClick({R.id.article_comment_content_linearlayout, R.id.article_comment_nickname_linearlayout,
            R.id.article_comment_cancel_button, R.id.article_comment_register_button,
            R.id.article_comment_password_linearlayout, R.id.article_comment_anonymous_cancel_button,
            R.id.article_comment_anonymous_delete_button, R.id.article_comment_anonymous_register_button
    })
    public void onViewClicked(View view) {
        String nickname = getNickname();
        AuthorizeConstant authorizeConstant = getAuthorize();
        if (!mIsEditPossible) {
            if (authorizeConstant == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (nickname.isEmpty()) {
                showNickNameRequestDialog();
                return;
            }

        }
        switch (view.getId()) {
            case R.id.article_comment_content_linearlayout:
                mArticleCommentContentEdittext.setFocusableInTouchMode(true);
                mArticleCommentContentEdittext.requestFocus();
                break;
            case R.id.article_comment_nickname_linearlayout:
                mArticleCommentNicknameEdittext.setFocusableInTouchMode(true);
                mArticleCommentNicknameEdittext.requestFocus();
                break;
            case R.id.article_comment_cancel_button:
                onClickedCancelButton();
                break;
            case R.id.article_comment_register_button:
                onClickedCommentRegisterButton();
                break;
            case R.id.article_comment_password_linearlayout:
                mArticleCommentPasswordEdittext.setFocusableInTouchMode(true);
                mArticleCommentPasswordEdittext.requestFocus();
                break;
            case R.id.article_comment_anonymous_cancel_button:
                onClickedAnonymousCommentCancelButton();
                break;
            case R.id.article_comment_anonymous_delete_button:
                onClickedAnonymousCommentDeleteButton();
                break;
            case R.id.article_comment_anonymous_register_button:
                onClickedAnonymousRegisterButton();
                break;
        }
    }

    public void onClickedCancelButton() {
        mArticleCommentContentEdittext.setText("");
    }

    public void onClickedAnonymousCommentCancelButton() {
        mArticleCommentNicknameEdittext.setText("");
        mArticleCommentContentEdittext.setText("");
        mArticleCommentPasswordEdittext.setText("");
        mArticleCommentNicknameEdittext.setFocusable(true);
        mArticleCommentNicknameEdittext.setClickable(true);
        mArticleCommentNicknameEdittext.setFocusableInTouchMode(true);
        mIsEditComment = false;
    }

    public void onClickedAnonymousCommentDeleteButton() {
        if (!mIsEditComment)
            return;

        mCommentPassword = mArticleCommentPasswordEdittext.getText().toString();
        if (!mCommentPassword.isEmpty())
            mArticleCommentPresenter.checkAnonymousCommentDeleteGranted(mSelectedComment.commentUid, mCommentPassword);
        else
            ToastUtil.getInstance().makeShortToast("비밀번호를 입력해주세요");
    }

    public void onClickedAnonymousRegisterButton() {
        String commentContent = mArticleCommentContentEdittext.getText().toString();
        String password = mArticleCommentPasswordEdittext.getText().toString();
        String nickname = mArticleCommentNicknameEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.getInstance().makeShortToast("내용을 입력해주세요.");
            return;
        }
        if (password.isEmpty()) {
            ToastUtil.getInstance().makeShortToast("비밀번호를 입력해주세요.");
            return;
        }
        if (nickname.isEmpty()) {
            ToastUtil.getInstance().makeShortToast("닉네임을 입력해주세요.");
            return;
        }
        if (!mIsEditComment) {
            mArticleCommentPresenter.createAnonymousComment(mArticle.articleUid, commentContent, nickname, password);
        } else {
            mSelectedComment.content = commentContent;
            mSelectedComment.password = password;
            mArticleCommentPresenter.checkAnonymousCommentAdjustGranted(mSelectedComment.commentUid, password);
        }
    }

    public void onClickedCommentRegisterButton() {
        String commentContent = mArticleCommentContentEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.getInstance().makeShortToast("내용을 입력해주세요.");
            return;
        }
        if (!mIsEditComment) {
            mArticleCommentPresenter.createComment(mArticle.articleUid, commentContent);
        } else {
            mSelectedComment.content = commentContent;
            mArticleCommentPresenter.updateComment(mArticle.articleUid, mSelectedComment);
        }

    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
        if (mArticle.boardUid == ID_ANONYMOUS) {
//          deleteCommentDialog(comment);
            return;
        }

        SnackbarUtil.makeLongSnackbarActionYes(mArticleCommentContentRecyclerview, "댓글을 삭제할까요?", () -> {
            if (comment.grantDelete) {
                mArticleCommentPresenter.deleteComment(mArticle.articleUid, comment.commentUid);
            }
        });
    }

    @Override
    public void showErrorDeleteComment() {
        ToastUtil.getInstance().makeShortToast("댓글 삭제에 실패하였습니다.");
    }

    @Override
    public void showSuccessDeleteComment() {
        ToastUtil.getInstance().makeShortToast("댓글 삭제에 성공하였습니다.");
        mArticleCommentContentEdittext.setText("");
        mIsEditComment = false;
    }

    @Override
    public void showErrorGrantedDeleteComment() {
        ToastUtil.getInstance().makeShortToast( "비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessGrantedDeleteComment() {
        SnackbarUtil.makeLongSnackbarActionYes(mArticleCommentContentRecyclerview, "삭제하시겠습니까?", () ->
                mArticleCommentPresenter.deleteAnonymousComment(mArticle.articleUid, mSelectedComment.commentUid, mCommentPassword));
    }


    @Override
    public void showErrorGrantedDeleteContent() {
        ToastUtil.getInstance().makeShortToast("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessCreateComment() {
        ToastUtil.getInstance().makeShortToast("댓글이 등록되었습니다.");
        mArticleCommentContentEdittext.setText("");
        mIsEditComment = false;
    }

    @Override
    public void showErrorCreateComment() {
        ToastUtil.getInstance().makeShortToast("댓글이 등록되지 않았습니다.");
    }

    @Override
    public void showErrorDeleteAnonymousComment() {
        ToastUtil.getInstance().makeShortToast( "댓글 삭제에 실패하였습니다.");
    }

    @Override
    public void showSuccessAnonymousDeleteComment() {
        ToastUtil.getInstance().makeShortToast( "댓글 삭제에 성공하였습니다.");
        onClickedAnonymousCommentCancelButton();

    }

    @Override
    public void showSuccessCreateAnonymousComment() {
        ToastUtil.getInstance().makeShortToast( "댓글이 등록되었습니다.");
        onClickedAnonymousCommentCancelButton();
        mIsEditComment = false;
    }

    @Override
    public void showErrorCreateAnonymousComment() {
        ToastUtil.getInstance().makeShortToast("댓글이 등록되지 않았습니다.");
    }

    @Override
    public void showErrorUpdateAnonymousComment() {
        ToastUtil.getInstance().makeShortToast("댓글 수정에 실패하였습니다.");
    }

    @Override
    public void showSuccessUpdateAnonymousComment() {
        ToastUtil.getInstance().makeShortToast( "댓글 수정에 성공하였습니다.");
        onClickedAnonymousCommentCancelButton();
    }

    @Override
    public void showSuccessGrantedDeleteContent() {

    }

    @Override
    public void showErrorGrantedAdjustComment() {
        ToastUtil.getInstance().makeShortToast("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessGrantedAdjustComment() {
        if (mSelectedComment.password != null && !mSelectedComment.password.isEmpty())
            mArticleCommentPresenter.updateAnonymousComment(mArticle.articleUid, mSelectedComment);
        else
            ToastUtil.getInstance().makeShortToast("비밀번호를 입력해주세요.");

    }

    @Override
    public void showErrorEditComment() {
        ToastUtil.getInstance().makeShortToast("댓글이 수정되지 않았습니다.");
    }

    @Override
    public void showSuccessEditComment() {
        ToastUtil.getInstance().makeShortToast("댓글이 수정되었습니다.");
        mArticleCommentContentEdittext.setText("");
        mIsEditComment = false;
    }

    @Override
    public void onArticleDataReceived(Article article) {
        mArticle.title = article.title;
        mArticle.authorNickname = article.authorNickname;
        mArticle.authorUid = article.authorUid;
        mArticle.createDate = article.createDate.substring(0, 10) + " " + article.createDate.substring(11, 16);
        mArticle.updateDate = article.updateDate.substring(0, 10) + " " + article.updateDate.substring(11, 16);
        mArticle.hitCount = article.hitCount;
        mArticle.content = article.content;
        mArticle.tag = article.tag;
        mArticleCommentCount = String.valueOf(article.commentCount);

        mCommentRecyclerAdapter.setArticle(mArticle);

        mCommentArrayList.clear();

        if (article.commentCount > 0) {
            if (mArticle.boardUid == ID_ANONYMOUS) {
                for (int i = 0; i < article.commentArrayList.size(); i++) {
                    article.commentArrayList.get(i).grantEdit = true;
                    article.commentArrayList.get(i).grantDelete = false;
                }
            }
            mCommentArrayList.addAll(article.commentArrayList);
        }
        if (mArticle.boardUid != ID_ANONYMOUS) {
            mArticleCommentNicknameEdittext.setText(getNickname());
        }


        updateUserInterface();
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void updateUserInterface() {
        StringBuilder title;
        StringBuilder commentButtonText;
        title = new StringBuilder("<font color='black'>" + mArticle.title + " " + "</font>");
        commentButtonText = new StringBuilder("<font color='black'>댓글</font>");
        if (mArticleCommentViewCount != null && Integer.parseInt(mArticleCommentCount) > 0) {
            title.append("<font color='#175c8e'>" + "(").append(mArticleCommentCount).append(")").append("</font>");
            commentButtonText.append("<font color='#175c8e'>").append(" ").append(mArticleCommentCount).append("</font>");
        }
        mArticleCommentTitle.setText(Html.fromHtml(title.toString()), TextView.BufferType.SPANNABLE);
        mArticleCommentWriter.setText(mArticle.authorNickname);
        mArticleCommentCreateDate.setText(Html.fromHtml(mArticle.createDate));
        mArticleCommentViewCount.setText(String.valueOf(mArticle.hitCount));
        if (mCommentArrayList.size() == 0)
            mArticleCommentLinearlayout.setVisibility(View.GONE);
        else
            mArticleCommentLinearlayout.setVisibility(View.VISIBLE);
        mCommentRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
        mIsEditComment = true;
        mSelectedComment = comment;
        mArticleCommentContentEdittext.setText(comment.content);
        if (mArticle.boardUid == ID_ANONYMOUS) {
            mArticleCommentNicknameEdittext.setText(comment.authorNickname);
            mArticleCommentNicknameEdittext.setFocusable(false);
            mArticleCommentNicknameEdittext.setClickable(false);
        }
    }

    @Override
    public void setPresenter(ArticleCommentPresenter presenter) {
        this.mArticleCommentPresenter = presenter;
    }

    public String getNickname() {
        String nickname = "";
        try {
            nickname = UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName;
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            if (UserInfoSharedPreferencesHelper.getInstance().loadUser() != null)
                nickname = UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName;
        }
        if (nickname == null) nickname = "";
        return nickname;
    }

    public void showLoginRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원 전용 서비스")
                .setMessage("로그인이 필요한 서비스입니다.\n로그인 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("FIRST_LOGIN", false);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
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
}
