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
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.board.presenter.ArticleEditContract;
import in.koreatech.koin.ui.board.presenter.ArticleEditPresenter;

import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constant.URLConstant.COMMUNITY.ID_RECRUIT;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class ArticleEditActivity extends KoinNavigationDrawerActivity implements ArticleEditContract.View, TextWatcher {
    private final static String TAG = ArticleEditActivity.class.getSimpleName();
    private final static int MAX_TITLE_LENGTH = 40;
    @BindView(R.id.koin_base_app_bar_dark)
    AppbarBase mAppBarBase;
    @BindView(R.id.title_nickname_border)
    LinearLayout mTitleNicknameBorder;
    @BindView(R.id.article_edittext_nickname)
    EditText mArticleEdittextNickname;
    @BindView(R.id.nickname_password_border)
    LinearLayout mNicknamePasswordBorder;
    @BindView(R.id.article_edittext_password)
    EditText mArticleEdittextPassword;
    @BindView(R.id.password_content_border)
    LinearLayout mPasswordContentBorder;
    private Context mContext;

    private ArticleEditPresenter mArticleEditPresenter;
    private CustomProgressDialog customProgressDialog;
    private int mBoardUid;
    private int mArticleUid;
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
        this.mContext = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //isEdit = false로 게시물 작성을 기본값으로 함
        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        mBoardUid = getIntent().getIntExtra("BOARD_UID", 0);

        //게시물 수정인 경우
        if (isEdit) {
            mArticleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
            mEditTextTitle.setText(getIntent().getStringExtra("ARTICLE_TITLE"));
            mPassword = getIntent().getStringExtra("PASSWORD");
            mNickname = getIntent().getStringExtra("NICKNAME");

            mEditTextTitle.setSelection(mEditTextTitle.getText().length());
            mEditTextContent.setText(Html.fromHtml(getIntent().getStringExtra("ARTICLE_CONTENT")));
            mEditTextContent.setSelection(mEditTextContent.getText().length());
            if (mBoardUid == ID_ANONYMOUS) {
                mArticleEdittextNickname.setText(mNickname);
                mArticleEdittextPassword.setText(mPassword);
                mArticleEdittextPassword.setFocusableInTouchMode(false);
                mArticleEdittextNickname.setFocusableInTouchMode(false);

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
        switch (mBoardUid) {
            case ID_FREE:
                mAppBarBase.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                mAppBarBase.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                mAppBarBase.setTitleText("익명게시판");
                break;
        }
        setVisiblityInput(mBoardUid);
        setPresenter(new ArticleEditPresenter(this, new CommunityRestInteractor()));

        mEditTextTitle.addTextChangedListener(this);

        isCreateBtnClicked = false;

        mInputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

    public void setVisiblityInput(int id) {
        if (id != ID_ANONYMOUS) {
            mTitleNicknameBorder.setVisibility(View.GONE);
            mArticleEdittextNickname.setVisibility(View.GONE);
            mNicknamePasswordBorder.setVisibility(View.GONE);
            mArticleEdittextPassword.setVisibility(View.GONE);
        } else {
            mTitleNicknameBorder.setVisibility(View.VISIBLE);
            mArticleEdittextNickname.setVisibility(View.VISIBLE);
            mNicknamePasswordBorder.setVisibility(View.VISIBLE);
            mArticleEdittextPassword.setVisibility(View.VISIBLE);
            mPasswordContentBorder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPresenter(ArticleEditPresenter presenter) {
        this.mArticleEditPresenter = presenter;
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
            ToastUtil.getInstance().makeShortToast("제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(mEditTextContent.getText().toString())) {
            ToastUtil.getInstance().makeShortToast("내용을 입력하세요");
            return;
        }

        if (mBoardUid == ID_ANONYMOUS) {
            if (FormValidatorUtil.validateStringIsEmpty(mArticleEdittextNickname.getText().toString())) {
                ToastUtil.getInstance().makeShortToast("제목을 입력하세요");
                return;
            }
            if (FormValidatorUtil.validateStringIsEmpty(mArticleEdittextPassword.getText().toString())) {
                ToastUtil.getInstance().makeShortToast("내용을 입력하세요");
                return;
            }
        }

        switch (mBoardUid) {
            case ID_FREE: //자유게시판
            case ID_RECRUIT: //취업게시판
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mEditTextContent.getText().toString());
                if (isEdit) {
                    mArticleEditPresenter.updateArticle(new Article(mBoardUid, mArticleUid, mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder)));
                } else {
                    mArticleEditPresenter.createArticle(new Article(mBoardUid, mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder)));
                }
                break;
            case ID_ANONYMOUS: //익명게시판
                spannableStringBuilder = new SpannableStringBuilder(mEditTextContent.getText().toString());
                if (isEdit) {
                    mArticleEditPresenter.updateAnonymousArticle(mArticleUid, mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder), mPassword);
                } else {
                    mArticleEditPresenter.createAnonymousArticle(mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder), mArticleEdittextNickname.getText().toString().replace(" ", ""), mArticleEdittextPassword.getText().toString());
                }
                break;
            default:
                ToastUtil.getInstance().makeShortToast("잘못된 접근입니다.");
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
            intent.putExtra("BOARD_UID", mBoardUid);
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
//                mArticleEditPresenter.createAnonymousArticle(mEditTextTitle.getText().toString().trim(), Html.toHtml(spannableStringBuilder), dialog.getNickName(), dialog.getPassword());
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
        if (id == AppbarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppbarBase.getRightButtonId())
            onClickEditButton();
    }


}
