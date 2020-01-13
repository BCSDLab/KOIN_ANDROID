package in.koreatech.koin.ui.board;

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
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.board.adpater.CommentRecyclerAdapter;
import in.koreatech.koin.ui.board.presenter.ArticleCommentContract;
import in.koreatech.koin.ui.board.presenter.ArticleCommentPresenter;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;

import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleCommentActivity extends KoinNavigationDrawerActivity implements ArticleCommentContract.View, CommentRecyclerAdapter.OnCommentRemoveButtonClickListener {

    @BindView(R.id.koin_base_app_bar_dark)
    AppbarBase appBarBase;
    @BindView(R.id.article_comment_title)
    TextView articleCommentTitle;
    @BindView(R.id.article_comment_view_count_korean)
    TextView articleCommentViewCountKorean;
    @BindView(R.id.article_comment_view_count)
    TextView articleCommentViewCount;
    @BindView(R.id.article_comment_writer)
    TextView articleCommentWriter;
    @BindView(R.id.article_comment_create_date)
    TextView articleCommentCreateDate;
    @BindView(R.id.article_comment_content_recyclerview)
    RecyclerView articleCommentContentRecyclerview;
    @BindView(R.id.article_comment_nickname_edittext)
    EditText articleCommentNicknameEdittext;
    @BindView(R.id.article_comment_nickname_linearlayout)
    LinearLayout articleCommentNicknameLinearlayout;
    @BindView(R.id.article_comment_content_edittext)
    EditText articleCommentContentEdittext;
    @BindView(R.id.article_comment_content_linearlayout)
    LinearLayout articleCommentContentLinearlayout;
    @BindView(R.id.article_comment_cancel_button)
    Button articleCommentCancelButton;
    @BindView(R.id.article_comment_register_button)
    Button articleCommentRegisterButton;
    @BindView(R.id.article_comment_scrollview)
    NestedScrollView articleCommentScrollview;
    @BindView(R.id.article_comment_comment_edit_border)
    LinearLayout articleCommentLinearlayout;
    @BindView(R.id.aritcle_comment_anonymous_cancel_delete_edit_linearlayout)
    LinearLayout articleCommentAnoymousCancelDeleteEditLinearlayout;
    @BindView(R.id.article_comment_password_linearlayout)
    LinearLayout aritcleCommentPasswordLinearlayout;
    @BindView(R.id.article_comment_cancel_register_linearlayout)
    LinearLayout aritcleCommentCancelRegisterLayout;
    @BindView(R.id.article_comment_password_edittext)
    EditText articleCommentPasswordEdittext;


    private final int REQ_CODE_ARTICLE_EDIT = 1;
    private CommentRecyclerAdapter commentRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Comment> commentArrayList;
    private Article article;
    private Context context;
    private ArticleCommentPresenter articleCommentPresenter;
    private String articleCommentCount;
    private boolean mIsEditComment;
    private Comment mSelectedComment;
    private String commentPassword;
    private boolean mIsEditPossible;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);
        context = this;
        ButterKnife.bind(this);
        this.article = new Article();
        this.article.boardUid = getIntent().getIntExtra("BOARD_UID", 0);
        this.article.articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String nickname = getNickname();
        AuthorizeConstant authorizeConstant = getAuthorize();
        if (this.article.boardUid == ID_FREE || this.article.boardUid == ID_RECRUIT)
            articleCommentPresenter.getArticle(article.articleUid);
        else
            articleCommentPresenter.getAnonymousArticle(article.articleUid);

        if (this.article != null && (this.article.boardUid == ID_FREE || this.article.boardUid == ID_RECRUIT) && (nickname.isEmpty() || authorizeConstant == AuthorizeConstant.ANONYMOUS)) {
            articleCommentContentEdittext.setFocusable(false);
            articleCommentContentEdittext.setClickable(false);
            mIsEditPossible = false;
        } else {
            articleCommentContentEdittext.setFocusableInTouchMode(true);
            mIsEditPossible = true;
        }


    }

    public void init() {
        mIsEditComment = false;
        mIsEditPossible = true;
        setPresenter(new ArticleCommentPresenter(this, new CommunityRestInteractor()));
        layoutManager = new LinearLayoutManager(this);
        commentArrayList = new ArrayList<>();
        commentRecyclerAdapter = new CommentRecyclerAdapter(context, commentArrayList);
        commentRecyclerAdapter.setCustomOnClickListener(this);
        articleCommentContentRecyclerview.setHasFixedSize(true);
        articleCommentContentRecyclerview.setLayoutManager(layoutManager);
        articleCommentContentRecyclerview.setAdapter(commentRecyclerAdapter);
        articleCommentContentRecyclerview.setNestedScrollingEnabled(false);
        articleCommentContentRecyclerview.setHasFixedSize(false);

        switch (article.boardUid) {
            case ID_FREE:
                appBarBase.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                appBarBase.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                appBarBase.setTitleText("익명게시판");
                break;
            default:
                appBarBase.setTitleText("게시판");
                break;
        }

        setVisibility(article.boardUid);

        if (article.boardUid != ID_ANONYMOUS) {
            articleCommentNicknameEdittext.setFocusable(false);
            articleCommentNicknameEdittext.setClickable(false);
        }

    }

    public void setVisibility(int boardUid) {
        if (boardUid != ID_ANONYMOUS) {
            articleCommentAnoymousCancelDeleteEditLinearlayout.setVisibility(View.GONE);
            aritcleCommentPasswordLinearlayout.setVisibility(View.GONE);
            aritcleCommentCancelRegisterLayout.setVisibility(View.VISIBLE);
        } else {
            articleCommentAnoymousCancelDeleteEditLinearlayout.setVisibility(View.VISIBLE);
            aritcleCommentPasswordLinearlayout.setVisibility(View.VISIBLE);
            aritcleCommentCancelRegisterLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppbarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppbarBase.getRightButtonId())
            onClickCreateButton();

    }

    public void onClickCreateButton() {
        if (article.boardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = getAuthorize();
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        Intent intent = new Intent(context, ArticleEditActivity.class);
        intent.putExtra("BOARD_UID", article.boardUid);
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
                articleCommentContentEdittext.setFocusableInTouchMode(true);
                articleCommentContentEdittext.requestFocus();
                break;
            case R.id.article_comment_nickname_linearlayout:
                articleCommentNicknameEdittext.setFocusableInTouchMode(true);
                articleCommentNicknameEdittext.requestFocus();
                break;
            case R.id.article_comment_cancel_button:
                onClickedCancelButton();
                break;
            case R.id.article_comment_register_button:
                onClickedCommentRegisterButton();
                break;
            case R.id.article_comment_password_linearlayout:
                articleCommentPasswordEdittext.setFocusableInTouchMode(true);
                articleCommentPasswordEdittext.requestFocus();
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
        articleCommentContentEdittext.setText("");
    }

    public void onClickedAnonymousCommentCancelButton() {
        articleCommentNicknameEdittext.setText("");
        articleCommentContentEdittext.setText("");
        articleCommentPasswordEdittext.setText("");
        articleCommentNicknameEdittext.setFocusable(true);
        articleCommentNicknameEdittext.setClickable(true);
        articleCommentNicknameEdittext.setFocusableInTouchMode(true);
        mIsEditComment = false;
    }

    public void onClickedAnonymousCommentDeleteButton() {
        if (!mIsEditComment)
            return;

        commentPassword = articleCommentPasswordEdittext.getText().toString();
        if (!commentPassword.isEmpty())
            articleCommentPresenter.checkAnonymousCommentDeleteGranted(mSelectedComment.commentUid, commentPassword);
        else
            ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요");
    }

    public void onClickedAnonymousRegisterButton() {
        String commentContent = articleCommentContentEdittext.getText().toString();
        String password = articleCommentPasswordEdittext.getText().toString();
        String nickname = articleCommentNicknameEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.getInstance().makeShort("내용을 입력해주세요.");
            return;
        }
        if (password.isEmpty()) {
            ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요.");
            return;
        }
        if (nickname.isEmpty()) {
            ToastUtil.getInstance().makeShort("닉네임을 입력해주세요.");
            return;
        }
        if (!mIsEditComment) {
            articleCommentPresenter.createAnonymousComment(article.articleUid, commentContent, nickname, password);
        } else {
            mSelectedComment.content = commentContent;
            mSelectedComment.password = password;
            articleCommentPresenter.checkAnonymousCommentAdjustGranted(mSelectedComment.commentUid, password);
        }
    }

    public void onClickedCommentRegisterButton() {
        String commentContent = articleCommentContentEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.getInstance().makeShort("내용을 입력해주세요.");
            return;
        }
        if (!mIsEditComment) {
            articleCommentPresenter.createComment(article.articleUid, commentContent);
        } else {
            mSelectedComment.content = commentContent;
            articleCommentPresenter.updateComment(article.articleUid, mSelectedComment);
        }

    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
        if (article.boardUid == ID_ANONYMOUS) {
//          deleteCommentDialog(comment);
            return;
        }

        SnackbarUtil.makeLongSnackbarActionYes(articleCommentContentRecyclerview, "댓글을 삭제할까요?", () -> {
            if (comment.grantDelete) {
                articleCommentPresenter.deleteComment(article.articleUid, comment.commentUid);
            }
        });
    }

    @Override
    public void showErrorDeleteComment() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 실패하였습니다.");
    }

    @Override
    public void showSuccessDeleteComment() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 성공하였습니다.");
        articleCommentContentEdittext.setText("");
        mIsEditComment = false;
    }

    @Override
    public void showErrorGrantedDeleteComment() {
        ToastUtil.getInstance().makeShort("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessGrantedDeleteComment() {
        SnackbarUtil.makeLongSnackbarActionYes(articleCommentContentRecyclerview, "삭제하시겠습니까?", () ->
                articleCommentPresenter.deleteAnonymousComment(article.articleUid, mSelectedComment.commentUid, commentPassword));
    }


    @Override
    public void showErrorGrantedDeleteContent() {
        ToastUtil.getInstance().makeShort("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessCreateComment() {
        ToastUtil.getInstance().makeShort("댓글이 등록되었습니다.");
        articleCommentContentEdittext.setText("");
        mIsEditComment = false;
    }

    @Override
    public void showErrorCreateComment() {
        ToastUtil.getInstance().makeShort("댓글이 등록되지 않았습니다.");
    }

    @Override
    public void showErrorDeleteAnonymousComment() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 실패하였습니다.");
    }

    @Override
    public void showSuccessAnonymousDeleteComment() {
        ToastUtil.getInstance().makeShort("댓글 삭제에 성공하였습니다.");
        onClickedAnonymousCommentCancelButton();

    }

    @Override
    public void showSuccessCreateAnonymousComment() {
        ToastUtil.getInstance().makeShort("댓글이 등록되었습니다.");
        onClickedAnonymousCommentCancelButton();
        mIsEditComment = false;
    }

    @Override
    public void showErrorCreateAnonymousComment() {
        ToastUtil.getInstance().makeShort("댓글이 등록되지 않았습니다.");
    }

    @Override
    public void showErrorUpdateAnonymousComment() {
        ToastUtil.getInstance().makeShort("댓글 수정에 실패하였습니다.");
    }

    @Override
    public void showSuccessUpdateAnonymousComment() {
        ToastUtil.getInstance().makeShort("댓글 수정에 성공하였습니다.");
        onClickedAnonymousCommentCancelButton();
    }

    @Override
    public void showSuccessGrantedDeleteContent() {

    }

    @Override
    public void showErrorGrantedAdjustComment() {
        ToastUtil.getInstance().makeShort("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessGrantedAdjustComment() {
        if (mSelectedComment.password != null && !mSelectedComment.password.isEmpty())
            articleCommentPresenter.updateAnonymousComment(this.article.articleUid, mSelectedComment);
        else
            ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요.");

    }

    @Override
    public void showErrorEditComment() {
        ToastUtil.getInstance().makeShort("댓글이 수정되지 않았습니다.");
    }

    @Override
    public void showSuccessEditComment() {
        ToastUtil.getInstance().makeShort("댓글이 수정되었습니다.");
        articleCommentContentEdittext.setText("");
        mIsEditComment = false;
    }

    @Override
    public void onArticleDataReceived(Article article) {
        this.article.title = article.title;
        this.article.authorNickname = article.authorNickname;
        this.article.authorUid = article.authorUid;
        this.article.createDate = article.createDate.substring(0, 10) + " " + article.createDate.substring(11, 16);
        this.article.updateDate = article.updateDate.substring(0, 10) + " " + article.updateDate.substring(11, 16);
        this.article.hitCount = article.hitCount;
        this.article.content = article.content;
        this.article.tag = article.tag;
        this.articleCommentCount = String.valueOf(this.article.commentCount);

        commentRecyclerAdapter.setArticle(this.article);

        commentArrayList.clear();

        if (this.article.commentCount > 0) {
            if (this.article.boardUid == ID_ANONYMOUS) {
                for (int i = 0; i < article.commentArrayList.size(); i++) {
                    this.article.commentArrayList.get(i).grantEdit = true;
                    this.article.commentArrayList.get(i).grantDelete = false;
                }
            }
            commentArrayList.addAll(this.article.commentArrayList);
        }
        if (this.article.boardUid != ID_ANONYMOUS) {
            articleCommentNicknameEdittext.setText(getNickname());
        }


        updateUserInterface();
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
        title = new StringBuilder("<font color='black'>" + article.title + " " + "</font>");
        commentButtonText = new StringBuilder("<font color='black'>댓글</font>");
        if (articleCommentViewCount != null && Integer.parseInt(articleCommentCount) > 0) {
            title.append("<font color='#175c8e'>" + "(").append(articleCommentCount).append(")").append("</font>");
            commentButtonText.append("<font color='#175c8e'>").append(" ").append(articleCommentCount).append("</font>");
        }
        articleCommentTitle.setText(Html.fromHtml(title.toString()), TextView.BufferType.SPANNABLE);
        articleCommentWriter.setText(this.article.authorNickname);
        articleCommentCreateDate.setText(Html.fromHtml(this.article.createDate));
        articleCommentViewCount.setText(String.valueOf(this.article.hitCount));
        if (commentArrayList.size() == 0)
            articleCommentLinearlayout.setVisibility(View.GONE);
        else
            articleCommentLinearlayout.setVisibility(View.VISIBLE);
        commentRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
        mIsEditComment = true;
        mSelectedComment = comment;
        articleCommentContentEdittext.setText(comment.content);
        if (this.article.boardUid == ID_ANONYMOUS) {
            articleCommentNicknameEdittext.setText(comment.authorNickname);
            articleCommentNicknameEdittext.setFocusable(false);
            articleCommentNicknameEdittext.setClickable(false);
        }
    }

    @Override
    public void setPresenter(ArticleCommentPresenter presenter) {
        this.articleCommentPresenter = presenter;
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
