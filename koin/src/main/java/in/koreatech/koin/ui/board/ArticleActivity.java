package in.koreatech.koin.ui.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.board.presenter.ArticleContract;
import in.koreatech.koin.ui.board.presenter.ArticlePresenter;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleActivity extends KoinNavigationDrawerActivity implements ArticleContract.View {
    private final String TAG = "ArticleActivity";
    private final int REQ_CODE_ARTICLE_EDIT = 1;
    private final int REQ_CODE_ARTICLE = 2;
    private final int RES_CODE_ARTICLE_DELETED = 1;
    private Context context;

    private InputMethodManager inputMethodManager;


    private ArticlePresenter articlePresenter;

    private Article article;
    private String articleCommentCount;

    @BindView(R.id.article_title)
    TextView mTextViewTitle;
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appbarBase;
    @BindView(R.id.article_view_count)
    TextView mTextViewViewCount;
    @BindView(R.id.article_content)
    TextView mTextViewContent;

    @BindView(R.id.article_writer)
    TextView mTextViewWriter;
    @BindView(R.id.article_create_date)
    TextView mTextViewCreateDate;
    @BindView(R.id.article_scrollview)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.article_comment_write_button)
    Button mButtonCommentWrite;
    @BindView(R.id.article_delete_button)
    Button articleDeleteButton;
    @BindView(R.id.article_edit_button)
    Button articleEditButton;
    @BindView(R.id.article_edittext_password)
    EditText articlePasswordEdittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        this.context = this;

        article = new Article();
        article.boardUid = getIntent().getIntExtra("BOARD_UID", 0);
        article.articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
        article.isGrantEdit = getIntent().getBooleanExtra("ARTICLE_GRANT_EDIT", false);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (article.boardUid == ID_ANONYMOUS) {
            articlePresenter.getAnonymousArticle(article.articleUid);
            return;
        }
        articlePresenter.getArticle(article.articleUid);
        articlePresenter.checkGranted(article.articleUid);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (article.boardUid == ID_ANONYMOUS) {
            articlePresenter.getAnonymousArticle(article.articleUid);
            return;
        }
        articlePresenter.getArticle(article.articleUid);
        articlePresenter.checkGranted(article.articleUid);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void init() {
        switch (article.boardUid) {
            case ID_FREE:
                appbarBase.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                appbarBase.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                appbarBase.setTitleText("익명게시판");
                visibleAnonymousBoard();
                break;
            default:
                appbarBase.setTitleText("게시판");
                break;
        }

        setPresenter(new ArticlePresenter(this, new CommunityRestInteractor()));

        //hide keyboard
        inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

    public void visibleAnonymousBoard() {
        articlePasswordEdittext.setVisibility(View.VISIBLE);
        articleDeleteButton.setVisibility(View.VISIBLE);
        articleEditButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(ArticlePresenter presenter) {
        this.articlePresenter = presenter;
    }

    @Override
    public void onBackPressed() {
        finish();
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
    public void showMessage(String message) {

    }

    /**
     * 서버로부터 받은 게시글 데이터를 저장하는 메소드
     *
     * @param article 게시글 파라미터
     */
    @Override
    public void onArticleDataReceived(Article article) {
        if (article.boardUid != ID_ANONYMOUS)
            checkRequiredInfo();

        this.article.title = article.title;
        this.article.authorNickname = article.authorNickname;
        this.article.authorUid = article.authorUid;
        this.article.createDate = article.createDate.substring(0, 10) + " " + article.createDate.substring(11, 16);
        this.article.updateDate = article.updateDate.substring(0, 10) + " " + article.updateDate.substring(11, 16);
        this.article.hitCount = article.hitCount;
        this.article.content = article.content;
        this.article.tag = article.tag;
        articleCommentCount = String.valueOf(article.commentCount);

        updateUserInterface();

    }

    /**
     *
     */
    @Override
    public void checkRequiredInfo() {
        String nickName;
        AuthorizeConstant authorize = getAuthorize();
        if (authorize == AuthorizeConstant.ANONYMOUS) {
            return;
        } else {
            nickName = UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName;
        }
        if (FormValidatorUtil.validateStringIsEmpty(nickName)) {
        } else {
        }
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

    @Override
    public void updateUserInterface() {
        StringBuilder title;
        StringBuilder commentButtonText;
        title = new StringBuilder("<font color='black'>" + article.title + " " + "</font>");
        commentButtonText = new StringBuilder("<font color='black'>댓글</font>");
        if (articleCommentCount != null && Integer.parseInt(articleCommentCount) > 0) {
            title.append("<font color='#175c8e'>" + "(").append(articleCommentCount).append(")").append("</font>");
            commentButtonText.append("<font color='#175c8e'>").append(" ").append(articleCommentCount).append("</font>");
        }
        mTextViewTitle.setText(Html.fromHtml(title.toString()), TextView.BufferType.SPANNABLE);
        mButtonCommentWrite.setText(Html.fromHtml(commentButtonText.toString()), TextView.BufferType.SPANNABLE);
        mTextViewWriter.setText(article.authorNickname);
        mTextViewCreateDate.setText(Html.fromHtml(article.createDate));
        mTextViewViewCount.setText(String.valueOf(article.hitCount));
        mTextViewContent.setText(Html.fromHtml(article.content));

//        commentRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onArticleDeleteCompleteReceived(boolean isSuccess) {
        setResult(RES_CODE_ARTICLE_DELETED);
        finish();

    }

    public void onClickEditButton() {
        String password = articlePasswordEdittext.getText().toString();
        if (article.boardUid == ID_ANONYMOUS) {
            if (!password.isEmpty())
                articlePresenter.checkAnonymousAdjustGranted(article.articleUid, password);
            else
                ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요");
            return;
        }

        Intent intent = new Intent(this, ArticleEditActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", article.articleUid);
        intent.putExtra("BOARD_UID", article.boardUid);
        intent.putExtra("ARTICLE_TITLE", article.title);
        intent.putExtra("ARTICLE_CONTENT", article.content);
        startActivity(intent);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickCreateButton();

    }

    @OnClick({R.id.article_delete_button, R.id.article_edit_button})
    public void onClickedButton(View v) {
        switch (v.getId()) {
            case R.id.article_edit_button:
                onClickEditButton();
                break;
            case R.id.article_delete_button:
                onClickRemoveButton();
                break;
        }
    }

    public void onClickRemoveButton() {
        if (article.boardUid != ID_ANONYMOUS)
            SnackbarUtil.makeLongSnackbarActionYes(mNestedScrollView, "게시글을 삭제할까요?\n댓글도 모두 사라집니다.", () -> articlePresenter.deleteArticle(article.articleUid));
        else if (article.boardUid == ID_ANONYMOUS) {
            String password = articlePasswordEdittext.getText().toString();
            if (!password.isEmpty())
                articlePresenter.checkAnonymousDeleteGranted(article.articleUid, password);
            else
                ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요");

        }

    }

    @Override
    public void showEditAndDeleteMenu() {
        article.isGrantEdit = true;
        showEditAndDeleteButton(article.isGrantEdit);
    }

    @Override
    public void hideEditAndDeleteMenu() {
        article.isGrantEdit = false;
        showEditAndDeleteButton(article.isGrantEdit);
    }

    public void showEditAndDeleteButton(boolean isGrant) {
        if (isGrant) {
            articleEditButton.setVisibility(View.VISIBLE);
            articleDeleteButton.setVisibility(View.VISIBLE);
        } else {
            articleEditButton.setVisibility(View.INVISIBLE);
            articleDeleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickSolveButton() {
        //TODO:문의사항 게시판 생길 때 수정
    }

    public void onClickUnsolveButton() {
        //TODO:문의사항 게시판 생길 때 수정
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

    @Override
    public void showErrorDeleteContent() {
        ToastUtil.getInstance().makeShort("삭제에 실패했습니다.");
    }

    @Override
    public void showSuccessDeleteContent() {
        ToastUtil.getInstance().makeShort("삭제되었습니다.");
        setResult(RES_CODE_ARTICLE_DELETED);
        finish();
    }

    @Override
    public void showErrorAdjustGrantedContent() {
        ToastUtil.getInstance().makeShort("비밀번호가 틀렸습니다.");
    }

    @Override
    public void showSuccessAdjustGrantedContent() {
        String password = articlePasswordEdittext.getText().toString();
        Intent intent = new Intent(this, ArticleEditActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", article.articleUid);
        intent.putExtra("BOARD_UID", article.boardUid);
        intent.putExtra("ARTICLE_TITLE", article.title);
        intent.putExtra("ARTICLE_CONTENT", article.content);
        intent.putExtra("NICKNAME", article.authorNickname);
        intent.putExtra("PASSWORD", password);
        startActivity(intent);
    }

    @Override
    public void showErrorGrantedDeleteContent() {
        ToastUtil.getInstance().makeShort("게시물을 수정하거나 삭제할 권한이 없습니다.");
    }

    @Override
    public void showSuccessGrantedDeleteContent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String password = articlePasswordEdittext.getText().toString();
        builder.setTitle("알림")
                .setMessage("게시글을 삭제할까요?\n댓글도 모두 사라집니다.")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    articlePresenter.deleteAnoymousArticle(article.articleUid, password);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
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

    @OnClick(R.id.article_comment_write_button)
    public void onClickedCommentWriteButton() {
        Intent intent = new Intent(this, ArticleCommentActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", article.articleUid);
        intent.putExtra("BOARD_UID", article.boardUid);
        intent.putExtra("ARTICLE_TITLE", article.title);
        intent.putExtra("ARTICLE_CONTENT", article.content);
        intent.putExtra("NICKNAME", article.authorNickname);
        startActivity(intent);
    }
}
