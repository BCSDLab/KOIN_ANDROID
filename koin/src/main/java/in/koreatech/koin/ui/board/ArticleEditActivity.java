package in.koreatech.koin.ui.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.board.presenter.ArticleEditContract;
import in.koreatech.koin.ui.board.presenter.ArticleEditPresenter;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class ArticleEditActivity extends KoinNavigationDrawerActivity implements ArticleEditContract.View, TextWatcher {
    private final static String TAG = "ArticleEditActivity";
    private final static int MAX_TITLE_LENGTH = 40;
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase appBarBase;
    @BindView(R.id.title_nickname_border)
    LinearLayout mTitleNicknameBorder;
    @BindView(R.id.article_edittext_nickname)
    EditText articleEdittextNickname;
    @BindView(R.id.nickname_password_border)
    LinearLayout mNicknamePasswordBorder;
    @BindView(R.id.article_edittext_password)
    EditText articleEdittextPassword;
    @BindView(R.id.password_content_border)
    LinearLayout mPasswordContentBorder;
    private Context context;

    private ArticleEditPresenter articleEditPresenter;
    private int boardUid;
    private int articleUid;
    private String mTitleTemp;
    private String mPassword;
    private String mNickname;


    private boolean isEdit;
    private boolean isCreateBtnClicked;
    private InputMethodManager mInputMethodManager;

    @BindView(R.id.article_edittext_title)
    EditText mEditTextTitle;
    @BindView(R.id.article_edittext_content)
    EditText mEditTextContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit);
        ButterKnife.bind(this);
        this.context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //isEdit = false로 게시물 작성을 기본값으로 함
        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        boardUid = getIntent().getIntExtra("BOARD_UID", 0);

        //게시물 수정인 경우
        if (isEdit) {
            articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
            mEditTextTitle.setText(getIntent().getStringExtra("ARTICLE_TITLE"));
            mPassword = getIntent().getStringExtra("PASSWORD");
            mNickname = getIntent().getStringExtra("NICKNAME");

            mEditTextTitle.setSelection(mEditTextTitle.getText().length());
            mEditTextContent.setText(Html.fromHtml(getIntent().getStringExtra("ARTICLE_CONTENT")));
            mEditTextContent.setSelection(mEditTextContent.getText().length());
            if (boardUid == ID_ANONYMOUS) {
                articleEdittextNickname.setText(mNickname);
                articleEdittextPassword.setText(mPassword);
                articleEdittextPassword.setFocusableInTouchMode(false);
                articleEdittextNickname.setFocusableInTouchMode(false);

            }
        }

        init();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        SnackbarUtil.makeLongSnackbarActionYes(mEditTextContent, getString(R.string.back_button_pressed), this::finish);
    }

    public void init() {
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
        }
        setVisiblityInput(boardUid);
        setPresenter(new ArticleEditPresenter(this, new CommunityRestInteractor()));

        mEditTextTitle.addTextChangedListener(this);

        isCreateBtnClicked = false;

        mInputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

    public void setVisiblityInput(int id) {
        if (id != ID_ANONYMOUS) {
            mTitleNicknameBorder.setVisibility(View.GONE);
            articleEdittextNickname.setVisibility(View.GONE);
            mNicknamePasswordBorder.setVisibility(View.GONE);
            articleEdittextPassword.setVisibility(View.GONE);
        } else {
            mTitleNicknameBorder.setVisibility(View.VISIBLE);
            articleEdittextNickname.setVisibility(View.VISIBLE);
            mNicknamePasswordBorder.setVisibility(View.VISIBLE);
            articleEdittextPassword.setVisibility(View.VISIBLE);
            mPasswordContentBorder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPresenter(ArticleEditPresenter presenter) {
        this.articleEditPresenter = presenter;
    }

    @OnEditorAction(R.id.article_edittext_title)
    public boolean onEditorAction(EditText v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.article_edittext_title:
                mEditTextContent.requestFocus();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClickEditButton() {
        //blocking multi click
        if (isCreateBtnClicked) {
            return;
        }

        if (FormValidatorUtil.validateStringIsEmpty(mEditTextTitle.getText().toString())) {
            ToastUtil.getInstance().makeShort("제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(mEditTextContent.getText().toString())) {
            ToastUtil.getInstance().makeShort("내용을 입력하세요");
            return;
        }

        if (boardUid == ID_ANONYMOUS) {
            if (FormValidatorUtil.validateStringIsEmpty(articleEdittextNickname.getText().toString())) {
                ToastUtil.getInstance().makeShort("제목을 입력하세요");
                return;
            }
            if (FormValidatorUtil.validateStringIsEmpty(articleEdittextPassword.getText().toString())) {
                ToastUtil.getInstance().makeShort("내용을 입력하세요");
                return;
            }
        }

        switch (boardUid) {
            case ID_FREE: //자유게시판
            case ID_RECRUIT: //취업게시판
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mEditTextContent.getText().toString());
                if (isEdit) {
                    articleEditPresenter.updateArticle(new Article(boardUid, articleUid, mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder)));
                } else {
                    articleEditPresenter.createArticle(new Article(boardUid, mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder)));
                }
                break;
            case ID_ANONYMOUS: //익명게시판
                spannableStringBuilder = new SpannableStringBuilder(mEditTextContent.getText().toString());
                if (isEdit) {
                    articleEditPresenter.updateAnonymousArticle(articleUid, mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder), mPassword);
                } else {
                    articleEditPresenter.createAnonymousArticle(mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder), articleEdittextNickname.getText().toString().replace(" ", ""), articleEdittextPassword.getText().toString());
                }
                break;
            default:
                ToastUtil.getInstance().makeShort("잘못된 접근입니다.");
                break;

        }

        isCreateBtnClicked = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mTitleTemp = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > MAX_TITLE_LENGTH) {
            mEditTextTitle.setText(mTitleTemp);
            mEditTextTitle.setSelection(mTitleTemp.length() - 1);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

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
        isCreateBtnClicked = false;
    }

    @Override
    public void onArticleDataReceived(Article article) {
        isCreateBtnClicked = false;
        goToArticleActivity(article);
    }

    @Override
    public void goToArticleActivity(Article article) {
        if (!isEdit) {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("BOARD_UID", boardUid);
            intent.putExtra("ARTICLE_UID", article.articleUid);
            intent.putExtra("ARTICLE_GRANT_EDIT", true);
            startActivity(intent);
        }

        finish();
    }

//    public void createContentCreateDialog() {
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mEditTextContent.getText().toString());
//        AskNicknamePasswordDialog dialog = new AskNicknamePasswordDialog(this, "알림", "닉네임과 비밀번호를 입력해주세요", AskNicknamePasswordDialog.YES_NICKNAME);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (!dialog.isCancelled()) {
//                articleEditPresenter.createAnonymousArticle(mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder), dialog.getNickName(), dialog.getPassword());
//                mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
//            }
//        });
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Window window = dialog.getWindow();
//        int x = (int) (size.x * 0.8f);
//        int y = (int) (size.y * 0.4f);
//        window.setLayout(x, y);
//    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onKoinBaseAppbarClick(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickEditButton();
    }


}
