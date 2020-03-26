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
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.constant.AuthorizeConstant;
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

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleCommentActivity extends KoinNavigationDrawerActivity implements ArticleCommentContract.View, CommentRecyclerAdapter.OnCommentRemoveButtonClickListener {

    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appBarBase;
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
    private Context context;
    private ArticleCommentPresenter articleCommentPresenter;
    private Article article;
    private String articleCommentCount;
    private boolean isEditComment;
    private Comment selectedComment;
    private String commentPassword;
    private boolean iIsEditPossible;
    private int boardUid;
    private int articleUid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);
        context = this;
        ButterKnife.bind(this);
        boardUid = getIntent().getIntExtra("BOARD_UID", 0);
        articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String nickname = getNickname();
        AuthorizeConstant authorizeConstant = getAuthorize();
        if (boardUid == ID_FREE || boardUid == ID_RECRUIT)
            articleCommentPresenter.getArticle(articleUid);
        else
            articleCommentPresenter.getAnonymousArticle(articleUid);

        if ((boardUid == ID_FREE || boardUid == ID_RECRUIT) && (nickname.isEmpty() || authorizeConstant == AuthorizeConstant.ANONYMOUS)) {
            articleCommentContentEdittext.setFocusable(false);
            articleCommentContentEdittext.setClickable(false);
            iIsEditPossible = false;
        } else {
            articleCommentContentEdittext.setFocusableInTouchMode(true);
            iIsEditPossible = true;
        }


    }

    public void init() {
        isEditComment = false;
        iIsEditPossible = true;
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

        switch (boardUid) {
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

        setVisibility(boardUid);

        if (boardUid != ID_ANONYMOUS) {
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
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickCreateButton();

    }

    public void onClickCreateButton() {
        if (boardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = getAuthorize();
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserNickName() == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        Intent intent = new Intent(context, ArticleEditActivity.class);
        intent.putExtra("BOARD_UID", boardUid);
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
        if (!iIsEditPossible) {
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
        isEditComment = false;
    }

    public void onClickedAnonymousCommentCancelButton() {
        articleCommentNicknameEdittext.setText("");
        articleCommentContentEdittext.setText("");
        articleCommentPasswordEdittext.setText("");
        articleCommentNicknameEdittext.setFocusable(true);
        articleCommentNicknameEdittext.setClickable(true);
        articleCommentNicknameEdittext.setFocusableInTouchMode(true);
        isEditComment = false;
    }

    public void onClickedAnonymousCommentDeleteButton() {
        if (!isEditComment)
            return;

        commentPassword = articleCommentPasswordEdittext.getText().toString();
        if (!commentPassword.isEmpty())
            articleCommentPresenter.checkAnonymousCommentDeleteGranted(selectedComment.getCommentUid(), commentPassword);
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
        if (!isEditComment) {
            articleCommentPresenter.createAnonymousComment(articleUid, commentContent, nickname, password);
        } else {
            selectedComment.setContent(commentContent);
            selectedComment.setPassword(password);
            articleCommentPresenter.checkAnonymousCommentAdjustGranted(selectedComment.getCommentUid(), password);
        }
    }

    public void onClickedCommentRegisterButton() {
        String commentContent = articleCommentContentEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.getInstance().makeShort("내용을 입력해주세요.");
            return;
        }
        if (!isEditComment) {
            articleCommentPresenter.createComment(articleUid, commentContent);
        } else {
            selectedComment.setContent(commentContent);
            articleCommentPresenter.updateComment(articleUid, selectedComment);
        }

    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
        if (boardUid == ID_ANONYMOUS) {
//          deleteCommentDialog(comment);
            return;
        }

        SnackbarUtil.makeLongSnackbarActionYes(articleCommentContentRecyclerview, "댓글을 삭제할까요?", () -> {
            if (comment.isGrantEdit()) {
                articleCommentPresenter.deleteComment(articleUid, comment.getCommentUid());
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
        isEditComment = false;
    }

    @Override
    public void showErrorGrantedDeleteComment() {
        ToastUtil.getInstance().makeShort("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessGrantedDeleteComment() {
        SnackbarUtil.makeLongSnackbarActionYes(articleCommentContentRecyclerview, "삭제하시겠습니까?", () ->
                articleCommentPresenter.deleteAnonymousComment(articleUid, selectedComment.getCommentUid(), commentPassword));
    }


    @Override
    public void showErrorGrantedDeleteContent() {
        ToastUtil.getInstance().makeShort("비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void showSuccessCreateComment() {
        ToastUtil.getInstance().makeShort("댓글이 등록되었습니다.");
        articleCommentContentEdittext.setText("");
        isEditComment = false;
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
        isEditComment = false;
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
        if (selectedComment.getPassword() != null && !selectedComment.getPassword().isEmpty())
            articleCommentPresenter.updateAnonymousComment(articleUid, selectedComment);
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
        isEditComment = false;
    }

    @Override
    public void onArticleDataReceived(Article article) {
        this.article = article;
        this.article.setCreateDate(article.getCreateDate().substring(0, 10) + " " + article.getCreateDate().substring(11, 16));
        this.article.setUpdateDate(article.getUpdateDate().substring(0, 10) + " " + article.getUpdateDate().substring(11, 16));;
        this.articleCommentCount = String.valueOf(article.getCommentCount());
        commentRecyclerAdapter.setArticle(this.article);
        commentArrayList.clear();

        if (this.article.getCommentCount() > 0) {
            if (this.article.getBoardUid() == ID_ANONYMOUS) {
                for (int i = 0; i < article.getCommentArrayList().size(); i++) {
                    this.article.getCommentArrayList().get(i).setGrantEdit(true);
                    this.article.getCommentArrayList().get(i).setGrantDelete(false);
                }
            }
            commentArrayList.addAll(article.getCommentArrayList());
        }
        if (boardUid != ID_ANONYMOUS) {
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
        title = new StringBuilder("<font color='black'>" + article.getTitle() + " " + "</font>");
        commentButtonText = new StringBuilder("<font color='black'>댓글</font>");
        if (articleCommentViewCount != null && Integer.parseInt(articleCommentCount) > 0) {
            title.append("<font color='#175c8e'>" + "(").append(articleCommentCount).append(")").append("</font>");
            commentButtonText.append("<font color='#175c8e'>").append(" ").append(articleCommentCount).append("</font>");
        }
        articleCommentTitle.setText(Html.fromHtml(title.toString()), TextView.BufferType.SPANNABLE);
        articleCommentWriter.setText(this.article.getAuthorNickname());
        articleCommentCreateDate.setText(Html.fromHtml(this.article.getCreateDate()));
        articleCommentViewCount.setText(String.valueOf(this.article.getHitCount()));
        if (commentArrayList.size() == 0)
            articleCommentLinearlayout.setVisibility(View.GONE);
        else
            articleCommentLinearlayout.setVisibility(View.VISIBLE);
        commentRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
        isEditComment = true;
        selectedComment = comment;
        articleCommentContentEdittext.setText(comment.getContent());
        if (this.article.getBoardUid() == ID_ANONYMOUS) {
            articleCommentNicknameEdittext.setText(comment.getAuthorUid());
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
            nickname = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserNickName();
        } catch (NullPointerException e) {
            UserInfoSharedPreferencesHelper.getInstance().init(getApplicationContext());
            if (UserInfoSharedPreferencesHelper.getInstance().loadUser() != null)
                nickname = UserInfoSharedPreferencesHelper.getInstance().loadUser().getUserNickName();
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
