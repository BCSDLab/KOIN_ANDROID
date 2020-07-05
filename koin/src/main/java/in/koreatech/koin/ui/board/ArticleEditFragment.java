package in.koreatech.koin.ui.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorControl;
import com.github.irshulx.models.EditorTextStyle;
import com.yunjaena.imageloader.ImageFailedType;
import com.yunjaena.imageloader.ImageLoadListener;
import com.yunjaena.imageloader.ImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.ImageUtil;
import in.koreatech.koin.util.NavigationManger;
import in.koreatech.koin.util.SnackbarUtil;
import top.defaults.colorpicker.ColorPickerPopup;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleEditFragment extends KoinBaseFragment implements ArticleEditContract.View, TextWatcher, ImageLoadListener {
    private final static String TAG = "ArticleEditFragment";
    private final static int MAX_TITLE_LENGTH = 255;
    private final static int EDITOR_LEFT_PADDING = 0;       // Editor 내 EditText의 왼쪽 Padding 값
    private final static int EDITOR_TOP_PADDING = 10;       // Editor 내 EditText의 위쪽 Padding 값
    private final static int EDITOR_RIGHT_PADDING = 0;      // Editor 내 EditText의 오른쪽 Padding 값
    private final static int EDITOR_BOTTOM_PADDING = 10;    // Editor 내 EditText의 아래쪽 Padding 값

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
    @BindView(R.id.article_editor_content)
    KoinRichEditor articleEditor;

    private Context context;
    private ArticleEditPresenter articleEditPresenter;
    private int boardUid;
    private int articleUid;
    private String titleTemp;
    private String password;
    private String nickname;
    private boolean isEdit;
    private boolean isCreateBtnClicked;
    private InputMethodManager inputMethodManager;
    private HashMap<String, Boolean> uploadImageHashMap;             // 갤러리에서 불러온 이미지의 HashMap
    private HashMap<String, Thread> imageUploadThreadHashMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_edit, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        uploadImageHashMap = new HashMap<>();
        initEditor(view);

        //isEdit = false로 게시물 작성을 기본값으로 함
        isEdit = getArguments().getBoolean("IS_EDIT", false);
        boardUid = getArguments().getInt("BOARD_UID", 0);
        articleEditor.setEditorImageLayout(R.layout.rich_editor_image_layout);

        //게시물 수정인 경우
        if (isEdit) {
            articleUid = getArguments().getInt("ARTICLE_UID", 0);
            editTextTitle.setText(getArguments().getString("ARTICLE_TITLE"));
            password = getArguments().getString("PASSWORD");
            nickname = getArguments().getString("NICKNAME");

            editTextTitle.setSelection(editTextTitle.getText().length());
            articleEditor.render(renderHtmltoString(getArguments().getString("ARTICLE_CONTENT")));
            if (boardUid == ID_ANONYMOUS) {
                articleEdittextNickname.setText(nickname);
                articleEdittextPassword.setText(password);
                articleEdittextPassword.setFocusableInTouchMode(false);
                articleEdittextNickname.setFocusableInTouchMode(false);
            }
        }

        init();
        changeEditorChildViewPadding(articleEditor, EDITOR_LEFT_PADDING, EDITOR_TOP_PADDING, EDITOR_RIGHT_PADDING, EDITOR_BOTTOM_PADDING);
        return view;
    }

    public String renderHtmltoString(String url) {
        if (url == null) return "";
        return url.replace("<div>", "").replace("<div/>", "").replace("<img", "</p><img").replace("<p></p><img", "<img").replace(".jpg\\\"></p>", ".jpg\\\">")
                .replace(".png\\\"></p>", ".png\\\">");
    }

    /**
     * 리치 에디터의 클릭 리스너 초기화
     */
    public void initEditor(View view) {
        view.findViewById(R.id.action_h1).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.H1));
        view.findViewById(R.id.action_h2).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.H2));
        view.findViewById(R.id.action_bold).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.BOLD));
        view.findViewById(R.id.action_Italic).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.ITALIC));
        view.findViewById(R.id.action_h3).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.H3));
        view.findViewById(R.id.action_indent).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.INDENT));
        view.findViewById(R.id.action_outdent).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.OUTDENT));
        view.findViewById(R.id.action_bulleted).setOnClickListener(v -> articleEditor.insertList(false));
        view.findViewById(R.id.action_unordered_numbered).setOnClickListener(v -> articleEditor.insertList(true));
        view.findViewById(R.id.action_hr).setOnClickListener(v -> articleEditor.insertDivider());
        view.findViewById(R.id.action_insert_image).setOnClickListener(v -> {
            //articleEditor.openImagePicker();
            ImageLoader.with(getContext())
                    .setImageLoadListener(this)
                    .showGalleryOrCameraSelectDialog();
            // btn_remove 버튼을 클릭하여 갤러리에서 가져온 이미지를 삭제
            articleEditor.setOnCancelListener(imageView -> {
                String imageTag = ((EditorControl) ((RelativeLayout) imageView.getParent()).getTag()).path;  // btn_remove 버튼을 포함하고 있는 이미지의 tag를 저장
                uploadImageHashMap.remove(imageTag);
                articleEditor.onImageUploadFailed(imageTag);
                imageUploadThreadHashMap.get(imageTag).interrupt();
                imageUploadThreadHashMap.remove(imageTag);
                articleEditor.getParentView().removeView(((RelativeLayout) imageView.getParent()));
            });
        });
        view.findViewById(R.id.action_insert_link).setOnClickListener(v -> {
            createURLCreateDialog();
        });
        view.findViewById(R.id.action_erase).setOnClickListener(v -> SnackbarUtil.makeLongSnackbarActionYes(articleEditor, getString(R.string.erase_button_pressed), this::eraseEditorText));
        view.findViewById(R.id.action_blockquote).setOnClickListener(v -> articleEditor.updateTextStyle(EditorTextStyle.BLOCKQUOTE));
        view.findViewById(R.id.action_color).setOnClickListener(v -> createColorPicker());


        // 리치 에디터 폰트 설정
        articleEditor.setHeadingTypeface(getEditorTypeface());
        articleEditor.setContentTypeface(getEditorTypeface());

        articleEditor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                editText.setPadding(EDITOR_LEFT_PADDING, EDITOR_TOP_PADDING, EDITOR_RIGHT_PADDING, EDITOR_BOTTOM_PADDING);
            }

            // 갤러리에서 이미지를 선택하고 호출되는 insertImage() 메소드 안에서 인자값을 넘겨받는 메소드
            @Override
            public void onUpload(Bitmap bitmap, String uuid) {
                uploadImage(bitmap, uuid);
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });

        articleEditor.render();
    }

    public void uploadImage(Bitmap bitmap, String uuid) {
        Thread uploadThread = new Thread(() -> {
            try {
                uploadImageHashMap.put(uuid, false);
                File imageFile = ImageUtil.changeBMPtoFILE(bitmap, uuid, 1, context);
                if (uploadImageHashMap.containsKey(uuid)) {
                    articleEditPresenter.uploadImage(imageFile, uuid);
                }
            } catch (Exception exception) {
                articleEditor.onImageUploadFailed(uuid);
                uploadImageHashMap.remove(uuid);
                ToastUtil.getInstance().makeLong(R.string.fail_upload);
            }
        });
        imageUploadThreadHashMap.put(uuid, uploadThread);
        uploadThread.start();

    }

    public Map<Integer, String> getEditorTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/notosans_regular.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/notosanscjkkr_bold.otf");
        typefaceMap.put(Typeface.ITALIC, "fonts/notosans_medium.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/notosans_light.ttf");

        return typefaceMap;
    }

    /**
     * Editor 영역의 Padding 값을 설정하여 LineSpacing 값을 수정하는 메소드
     *
     * @param view   LinearLayout를 상속받은 Editor
     * @param left   EditText(한 줄)의 왼쪽 Padding
     * @param top    EditText(한 줄)의 위쪽 Padding
     * @param right  EditText(한 줄)의 오른쪽 Padding
     * @param bottom EditText(한 줄)의 아래쪽 Padding
     */
    public void changeEditorChildViewPadding(View view, int left, int top, int right, int bottom) {
        if (view == null)
            return;
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                changeEditorChildViewPadding(((ViewGroup) view).getChildAt((i)), left, top, right, bottom);
            }
        } else {
            view.setPadding(left, top, right, bottom);
        }
    }

    @Override
    public void onBackPressed() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        SnackbarUtil.makeLongSnackbarActionYes(articleEditor, getString(R.string.back_button_pressed), super::onBackPressed);
    }

    public void init() {
        imageUploadThreadHashMap = new HashMap<>();
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

        inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().getApplicationContext().INPUT_METHOD_SERVICE);
    }

    public void eraseEditorText() {
        articleEditor.clearAllContents();
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
        String spannableStringBuilder = articleEditor.getContentAsHTML();
        boolean imageUploadCheck = true;
        //blocking multi click
        if (isCreateBtnClicked) {
            return;
        }

        for (Boolean isImageUploading : uploadImageHashMap.values()) {
            if (!isImageUploading) {
                imageUploadCheck = false;
                break;
            }
        }

        if (!imageUploadCheck) {
            ToastUtil.getInstance().makeShort("이미지 업로드 중입니다.");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(editTextTitle.getText().toString())) {
            ToastUtil.getInstance().makeShort("제목을 입력하세요");
            return;
        }
        if (FormValidatorUtil.validateStringIsEmpty(articleEditor.getContent().toString().trim())) {
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
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
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
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        switch (article.getBoardUid()) {
            case ID_FREE:
                goToArticle(R.id.navi_free_article_action, article);
                break;
            case ID_RECRUIT:
                goToArticle(R.id.navi_recruit_article_action, article);
                break;
            case ID_ANONYMOUS:
                goToArticle(R.id.navi_anonymous_article_action, article);
                break;
        }


    }

    @Override
    public void showUploadImage(String url, String uploadImageId) {
        try {
            if (!uploadImageHashMap.containsKey(uploadImageId))
                return;
            uploadImageHashMap.put(uploadImageId, true);
            articleEditor.onImageUploadComplete(url, uploadImageId);
        } catch (Exception e) {
            //ToastUtil.getInstance().makeShort( R.string.fail_upload);
        }
    }

    private void goToArticle(int id, Article article) {
        if (!isEdit) {
            Bundle bundle = new Bundle();
            bundle.putInt("BOARD_UID", boardUid);
            bundle.putInt("ARTICLE_UID", article.getArticleUid());
            bundle.putBoolean("ARTICLE_GRANT_EDIT", true);
            NavigationManger.getNavigationController(getActivity()).popBackStack();
            NavigationManger.getNavigationController(getActivity()).navigate(id, bundle, NavigationManger.getNavigationAnimation());
        } else {
            NavigationManger.getNavigationController(getActivity()).popBackStack();
        }
    }

    @Override
    public void showFailUploadImage(String uploadImageId) {
        articleEditor.onImageUploadFailed(uploadImageId);

    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onKoinBaseAppbarClick(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickEditButton();
    }

    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }

    public void createURLCreateDialog() {
        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getContext());
        inputAlert.setTitle("링크 추가");
        final EditText userInput = new EditText(getContext());
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
        new ColorPickerPopup.Builder(context)
                .initialColor(Color.RED) // 색초기화
                .enableAlpha(true)
                .okTitle("선택")
                .cancelTitle("취소")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(getView(), new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        articleEditor.updateTextColor(colorHex(color));
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }

    @Override
    public void onImageAdded(List<Bitmap> bitmapList) {
        articleEditor.insertImage(bitmapList.get(0));
    }

    @Override
    public void onImageFailed(ImageFailedType failedType) {
        if (failedType != ImageFailedType.CANCEL)
            ToastUtil.getInstance().makeShort("이미지를 불러오지 못했습니다.");
    }

    @Override
    public void onDestroyView() {
        for (Map.Entry<String, Thread> elem : imageUploadThreadHashMap.entrySet()) {
            elem.getValue().interrupt();
        }
        super.onDestroyView();
    }
}
