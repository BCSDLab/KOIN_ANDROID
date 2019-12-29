package in.koreatech.koin.service_board.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.github.irshulx.models.Node;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.BaseActivity;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.ImageUtil;
import in.koreatech.koin.core.util.SnackbarUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_board.contracts.ArticleEditContract;
import in.koreatech.koin.service_board.presenters.ArticleEditPresenter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import top.defaults.colorpicker.ColorPickerPopup;


import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_RECRUIT;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class ArticleEditActivity extends KoinNavigationDrawerActivity implements ArticleEditContract.View, TextWatcher {
    private final static String TAG = ArticleEditActivity.class.getSimpleName();
    private final static int MAX_TITLE_LENGTH = 255;
    @BindView(R.id.koin_base_app_bar_dark)
    KoinBaseAppbarDark mKoinBaseAppBarDark;
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
    private GenerateProgressTask generateProgressTask;
    private int mBoardUid;
    private int mArticleUid;
    private String mTitleTemp;
    private String mPassword;
    private String mNickname;


    private boolean isEdit;
    private boolean isCreateBtnClicked;
    private boolean isUploadingImage;
    private InputMethodManager mInputMethodManager;

    @BindView(R.id.article_edittext_title)
    EditText mEditTextTitle;
    @BindView(R.id.article_editor_content)
    Editor articleEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit);
        ButterKnife.bind(this);
        this.mContext = this;
        initEditor();
        //isEdit = false로 게시물 작성을 기본값으로 함
        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        mBoardUid = getIntent().getIntExtra("BOARD_UID", 0);
        articleEditor.setEditorImageLayout(R.layout.rich_editor_image_layout);
        //게시물 수정인 경우
        if (isEdit) {
            mArticleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
            mEditTextTitle.setText(getIntent().getStringExtra("ARTICLE_TITLE"));
            mPassword = getIntent().getStringExtra("PASSWORD");
            mNickname = getIntent().getStringExtra("NICKNAME");

            mEditTextTitle.setSelection(mEditTextTitle.getText().length());
            articleEditor.render(renderHtmltoString(getIntent().getStringExtra("ARTICLE_CONTENT")));
            if (mBoardUid == ID_ANONYMOUS) {
                mArticleEdittextNickname.setText(mNickname);
                mArticleEdittextPassword.setText(mPassword);
                mArticleEdittextPassword.setFocusableInTouchMode(false);
                mArticleEdittextNickname.setFocusableInTouchMode(false);

            }
        }

        init();
    }

    public String renderHtmltoString(String url) {
        if (url == null) return "";
        return url.replace("<div>", " ").replace("<div/>", " ");
    }

    public void initEditor() {
        isUploadingImage = false;
        findViewById(R.id.action_h1).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.H1));
        findViewById(R.id.action_h2).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.H2));
        findViewById(R.id.action_h3).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.H3));
        findViewById(R.id.action_bold).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.BOLD));
        findViewById(R.id.action_Italic).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.ITALIC));
        findViewById(R.id.action_indent).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.INDENT));
        findViewById(R.id.action_outdent).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.OUTDENT));
        findViewById(R.id.action_bulleted).setOnClickListener(v -> articleEditor.insertList(false));
        findViewById(R.id.action_unordered_numbered).setOnClickListener(v -> articleEditor.insertList(true));
        findViewById(R.id.action_hr).setOnClickListener(v -> articleEditor.insertDivider());
        findViewById(R.id.action_insert_image).setOnClickListener(v -> {
            if (!isUploadingImage) articleEditor.openImagePicker();
            else
                ToastUtil.makeShortToast(mContext, R.string.uploading_image_now);
        });
        findViewById(R.id.action_insert_link).setOnClickListener(v -> {
            createURLCreateDialog();
        });
        findViewById(R.id.action_erase).setOnClickListener(v -> SnackbarUtil.makeLongSnackbarActionYes(articleEditor, getString(R.string.erase_button_pressed), this::eraseEditorText));
        findViewById(R.id.action_blockquote).   setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.BLOCKQUOTE));
        findViewById(R.id.action_color).setOnClickListener(view -> createColorPicker());
        articleEditor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap bitmap, String uuid) {
                View removeButton = null;
//                View layout = findViewByTag(articleEditor.getParentView(), uuid);
//                if (layout != null)
//                    removeButton = layout.findViewById(R.id.btn_remove);
//                if (removeButton != null)
//                    removeButton.setOnClickListener(view -> {
//                        Log.d(TAG, "이미지 업로드 취소");
//                        isUploadingImage = false;
//                    });
                ViewGroup viewGroup = articleEditor.getParentView().findViewWithTag(uuid);
                if (viewGroup != null) removeButton = viewGroup.findViewById(R.id.btn_remove);
                else Log.e(TAG, "뷰그룹 못찾음");
                if (removeButton != null) {
                    ToastUtil.makeShortToast(mContext, "삭제버튼 클릭");
                    isUploadingImage = false;
                }
                else Log.e(TAG, "버튼 못찾음");
                isUploadingImage = true;
                Observable.defer(() -> {
                    File imageFile = ImageUtil.changeBMPtoFILE(bitmap, uuid, 1, mContext);
                    return Observable.just(imageFile);
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe(imageFile -> {
                            if (isUploadingImage)
                                mArticleEditPresenter.uploadImage(imageFile, uuid);
                        }, throwable -> {
                            new Handler().post(() -> {
                                articleEditor.onImageUploadFailed(uuid);
                                ToastUtil.makeLongToast(mContext, R.string.fail_upload);
                                isUploadingImage = false;
                            });

                        });
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });
        articleEditor.render();
    }


    private void findImageViewGroupByTag(ViewGroup viewGroup, String tag, int id, View searchView) {

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view.getTag().equals(tag)) {
                ToastUtil.makeShortToast(this, "찾았습니다!!");
                Log.e(TAG, "findImageViewByTag: " + view.getId());
                searchView = view.findViewById(id);

            }
            if (view instanceof ViewGroup)
                findImageViewGroupByTag((ViewGroup) view, tag, id, searchView);

        }
    }

    private View findViewByTag(View view, String tag) {
        Stack<View> stack = new Stack<>();
        stack.push(view);

        while (!stack.isEmpty()) {
            view = stack.pop();
            if (view.findViewWithTag(tag) != null) {
                ToastUtil.makeShortToast(this, "찾았습니다!!");
                Log.e(TAG, "findImageViewByTag: " + view.getId());
                return view.findViewWithTag(tag);
            }
            if (view instanceof ViewGroup) {
                int count = ((ViewGroup) view).getChildCount();
                for (int i = 0; i < count; i++) {
                    View searchView = ((ViewGroup) view).getChildAt(i);
                    stack.push(searchView);

                }
            }
        }
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == articleEditor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                articleEditor.insertImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }
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

        SnackbarUtil.makeLongSnackbarActionYes(articleEditor, getString(R.string.back_button_pressed), this::finish);
    }

    public void init() {
        switch (mBoardUid) {
            case ID_FREE:
                mKoinBaseAppBarDark.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                mKoinBaseAppBarDark.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                mKoinBaseAppBarDark.setTitleText("익명게시판");
                break;
        }
        setVisiblityInput(mBoardUid);
        setPresenter(new ArticleEditPresenter(this, new CommunityRestInteractor()));

        mEditTextTitle.addTextChangedListener(this);

        isCreateBtnClicked = false;

        mInputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

    public void eraseEditorText() {
        articleEditor.clearAllContents();
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

    public String checkUrl(String url) {
        String patter = "^(http|https|ftp)://.*$";
        if (url.matches(patter)) {
            return url;
        } else {
            return "http://" + url;
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
                mEditTextTitle.requestFocus();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClickEditButton() {
        String spannableStringBuilder = articleEditor.getContentAsHTML();
        //blocking multi click
        if (isCreateBtnClicked) {
            return;
        }


        if (FormValidatorUtil.validateStringIsEmpty(mEditTextTitle.getText().toString())) {
            ToastUtil.makeShortToast(mContext, "제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(articleEditor.getContent().toString().trim())) {
            ToastUtil.makeShortToast(mContext, "내용을 입력하세요");
            return;
        }

        if (mBoardUid == ID_ANONYMOUS) {
            if (FormValidatorUtil.validateStringIsEmpty(mArticleEdittextNickname.getText().toString())) {
                ToastUtil.makeShortToast(mContext, "제목을 입력하세요");
                return;
            }
            if (FormValidatorUtil.validateStringIsEmpty(mArticleEdittextPassword.getText().toString())) {
                ToastUtil.makeShortToast(mContext, "내용을 입력하세요");
                return;
            }
        }

        if (isUploadingImage) {
            ToastUtil.makeShortToast(mContext, R.string.uploading_image_now);
            return;
        }

        switch (mBoardUid) {
            case ID_FREE: //자유게시판
            case ID_RECRUIT: //채용게시판
                if (isEdit) {
                    mArticleEditPresenter.updateArticle(new Article(mBoardUid, mArticleUid, mEditTextTitle.getText().toString().trim(), spannableStringBuilder));
                } else {
                    mArticleEditPresenter.createArticle(new Article(mBoardUid, mEditTextTitle.getText().toString().trim(), spannableStringBuilder));
                }
                break;
            case ID_ANONYMOUS: //익명게시판
                if (isEdit) {
                    mArticleEditPresenter.updateAnonymousArticle(mArticleUid, mEditTextTitle.getText().toString().trim(), spannableStringBuilder, mPassword);
                } else {
                    mArticleEditPresenter.createAnonymousArticle(mEditTextTitle.getText().toString().trim(), spannableStringBuilder, mArticleEdittextNickname.getText().toString().replace(" ", ""), mArticleEdittextPassword.getText().toString());
                }
                break;
            default:
                ToastUtil.makeShortToast(mContext, "잘못된 접근입니다.");
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
        if (generateProgressTask == null) {
            generateProgressTask = new GenerateProgressTask(this, "로딩 중");
            generateProgressTask.execute();
        }
    }

    @Override
    public void hideLoading() {
        if (generateProgressTask != null) {
            generateProgressTask.cancel(true);
            generateProgressTask = null;
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

    @Override
    public void showUploadImage(String url, String uploadImageId) {
        try {
            articleEditor.onImageUploadComplete(url, uploadImageId);
        } catch (NullPointerException e) {
            ToastUtil.makeShortToast(mContext, R.string.fail_upload);
        } finally {
            ToastUtil.makeShortToast(mContext, R.string.fail_upload);
        }
        isUploadingImage = false;
    }

    @Override
    public void showFailUploadImage(String uploadImageId) {
        articleEditor.onImageUploadFailed(uploadImageId);
        isUploadingImage = false;
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
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId())
            onClickEditButton();
    }

    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }

    public void createURLCreateDialog() {
        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(this);
        inputAlert.setTitle("링크 추가");
        final EditText userInput = new EditText(this);
        userInput.setHint("주소를 입력해주세요");
        userInput.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
        inputAlert.setView(userInput);
        inputAlert.setPositiveButton("삽입", (dialog, which) -> {
            String userInputValue = userInput.getText().toString();
            articleEditor.insertLink(checkUrl(userInputValue));
        });
        inputAlert.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();
    }

    public void createColorPicker() {
        new ColorPickerPopup.Builder(mContext)
                .initialColor(Color.RED) // 색초기화
                .enableAlpha(true)
                .okTitle("선택")
                .cancelTitle("취소")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        articleEditor.updateTextColor(colorHex(color));
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }
}
