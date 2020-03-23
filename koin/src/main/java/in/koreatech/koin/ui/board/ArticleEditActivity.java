package in.koreatech.koin.ui.board;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorControl;
import com.github.irshulx.models.EditorTextStyle;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.ui.board.presenter.ArticleEditContract;
import in.koreatech.koin.ui.board.presenter.ArticleEditPresenter;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.ImageUtil;
import in.koreatech.koin.util.SnackbarUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import top.defaults.colorpicker.ColorPickerPopup;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleEditActivity extends KoinEditorActivity implements ArticleEditContract.View, TextWatcher {
    private final static String TAG = "ArticleEditActivity";
    private final static int MAX_TITLE_LENGTH = 255;

    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseAppBarDark;
    @BindView(R.id.title_nickname_border)
    LinearLayout titleNicknameBorder;
    @BindView(R.id.article_edittext_nickname)
    EditText articleEdittextNickname;
    @BindView(R.id.nickname_password_border)
    LinearLayout nicknamePasswordBorder;
    @BindView(R.id.article_edittext_password)
    EditText articleEdittextPassword;
    @BindView(R.id.password_content_border)
    LinearLayout passwordContentBorder;
    @BindView(R.id.article_edittext_title)
    EditText editTextTitle;

    private Context context;
    private ArticleEditPresenter articleEditPresenter;
    private int boardUid;
    private int articleUid;
    private String titleTemp;
    private String password;
    private String nickname;
    private boolean isEdit;
    private boolean isCreateBtnClicked;
    private HashMap<String, Boolean> uploadImageHashMap;             // 갤러리에서 불러온 이미지의 HashMap
    private HashMap<String, Boolean> isUploadImageCompeleteHashMap;  // 압축 과정을 완료한 이미지의 HashMap
    private InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_article_edit);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        this.context = this;

        //isEdit = false로 게시물 작성을 기본값으로 함
        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        boardUid = getIntent().getIntExtra("BOARD_UID", 0);

        //게시물 수정인 경우
        if (isEdit) {
            articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
            editTextTitle.setText(getIntent().getStringExtra("ARTICLE_TITLE"));
            password = getIntent().getStringExtra("PASSWORD");
            nickname = getIntent().getStringExtra("NICKNAME");

            editTextTitle.setSelection(editTextTitle.getText().length());
            renderEditor(renderHtmltoString(getIntent().getStringExtra("ARTICLE_CONTENT")));
            if (boardUid == ID_ANONYMOUS) {
                articleEdittextNickname.setText(nickname);
                articleEdittextPassword.setText(password);
                articleEdittextPassword.setFocusableInTouchMode(false);
                articleEdittextNickname.setFocusableInTouchMode(false);
            }
        }

        init();
    }

    @Override
    protected int getRichEditorId() {
        return R.id.article_editor_content;
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    protected void successImageProcessing(File imageFile, String uuid) {
        articleEditPresenter.uploadImage(imageFile, uuid);
    }

    @Override
    public void onBackPressed() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        SnackbarUtil.makeLongSnackbarActionYes(view, getString(R.string.back_button_pressed), this::finish);
    }

    public void init() {
        switch (boardUid) {
            case ID_FREE:
                koinBaseAppBarDark.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                koinBaseAppBarDark.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                koinBaseAppBarDark.setTitleText("익명게시판");
                break;
        }
        setVisiblityInput(boardUid);
        setPresenter(new ArticleEditPresenter(this, new CommunityRestInteractor()));

        editTextTitle.addTextChangedListener(this);

        editTextTitle.setFocusableInTouchMode(true);
        editTextTitle.requestFocus();
        editTextTitle.setFocusable(true);

        isCreateBtnClicked = false;

        inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

    public void setVisiblityInput(int id) {
        if (id != ID_ANONYMOUS) {
            titleNicknameBorder.setVisibility(View.GONE);
            articleEdittextNickname.setVisibility(View.GONE);
            nicknamePasswordBorder.setVisibility(View.GONE);
            articleEdittextPassword.setVisibility(View.GONE);
        } else {
            titleNicknameBorder.setVisibility(View.VISIBLE);
            articleEdittextNickname.setVisibility(View.VISIBLE);
            nicknamePasswordBorder.setVisibility(View.VISIBLE);
            articleEdittextPassword.setVisibility(View.VISIBLE);
            passwordContentBorder.setVisibility(View.VISIBLE);
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
                editTextTitle.requestFocus();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClickEditButton() {
        String spannableStringBuilder = getContentAsHTML();
        //blocking multi click
        if (isCreateBtnClicked) {
            return;
        }

        if (!isImageAllUploaded()) {
            ToastUtil.getInstance().makeShort("이미지 업로드 중입니다.");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(editTextTitle.getText().toString())) {
            ToastUtil.getInstance().makeShort("제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(getContent())) {
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
            case ID_RECRUIT: //채용게시판
                if (isEdit) {
                    articleEditPresenter.updateArticle(new Article(boardUid, articleUid, editTextTitle.getText().toString().trim(), spannableStringBuilder));
                } else {
                    articleEditPresenter.createArticle(new Article(boardUid, editTextTitle.getText().toString().trim(), spannableStringBuilder));
                }
                break;
            case ID_ANONYMOUS: //익명게시판
                if (isEdit) {
                    articleEditPresenter.updateAnonymousArticle(articleUid, editTextTitle.getText().toString().trim(), spannableStringBuilder, password);
                } else {
                    articleEditPresenter.createAnonymousArticle(editTextTitle.getText().toString().trim(), spannableStringBuilder, articleEdittextNickname.getText().toString().replace(" ", ""), articleEdittextPassword.getText().toString());
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
        titleTemp = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > MAX_TITLE_LENGTH) {
            editTextTitle.setText(titleTemp);
            editTextTitle.setSelection(titleTemp.length() - 1);
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
            intent.putExtra("ARTICLE_UID", article.getArticleUid());
            intent.putExtra("ARTICLE_GRANT_EDIT", true);
            startActivity(intent);
        }

        finish();
    }

    @Override
    public void showUploadImage(String url, String uploadImageId) {
        onImageUploadComplete(url, uploadImageId);
    }

    @Override
    public void showFailUploadImage(String uploadImageId) {
        onImageUploadFailed(uploadImageId);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onKoinBaseAppbarClick(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickEditButton();
    }
}
