package in.koreatech.koin.ui.board;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import com.github.irshulx.Editor;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Article;
import in.koreatech.koin.data.network.interactor.CommunityRestInteractor;
import in.koreatech.koin.ui.board.presenter.ArticleContract;
import in.koreatech.koin.ui.board.presenter.ArticlePresenter;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.util.AuthorizeManager;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.NavigationManger;
import in.koreatech.koin.util.SnackbarUtil;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleFragment extends KoinBaseFragment implements ArticleContract.View {
    private final static int EDITOR_LEFT_PADDING = 0;       // Editor 내 EditText의 왼쪽 Padding 값
    private final static int EDITOR_TOP_PADDING = 10;       // Editor 내 EditText의 위쪽 Padding 값
    private final static int EDITOR_RIGHT_PADDING = 0;      // Editor 내 EditText의 오른쪽 Padding 값
    private final static int EDITOR_BOTTOM_PADDING = 10;    // Editor 내 EditText의 아래쪽 Padding 값
    public final String RES_CODE = "RES_CODE";
    private final String TAG = "ArticleFragment";
    private final String REQ_CODE_ARTICLE_EDIT = "REQ_CODE_ARTICLE_EDIT";
    private final String REQ_CODE_ARTICLE = "REQ_CODE_ARTICLE";
    private final String RES_CODE_ARTICLE_DELETED = "RES_CODE_ARTICLE_DELETED";
    @BindView(R.id.article_title)
    TextView textViewTitle;
    @BindView(R.id.koin_base_app_bar_dark)
    AppBarBase koinBaseAppbarDark;
    @BindView(R.id.article_view_count)
    TextView textViewViewCount;
    @BindView(R.id.article_content)
    Editor editorContent;

    @BindView(R.id.article_writer)
    TextView textViewWriter;
    @BindView(R.id.article_create_date)
    TextView textViewCreateDate;
    @BindView(R.id.article_scrollview)
    NestedScrollView nestedScrollView;
    @BindView(R.id.article_comment_write_button)
    Button buttonCommentWrite;
    @BindView(R.id.article_delete_button)
    Button articleDeleteButton;
    @BindView(R.id.article_edit_button)
    Button articleEditButton;
    @BindView(R.id.article_edittext_password)
    EditText articlePasswordEdittext;

    private Context context;
    private InputMethodManager inputMethodManager;
    private ArticlePresenter articlePresenter;
    private Article article;
    private String articleCommentCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        this.article = new Article();
        this.article.setBoardUid(getArguments().getInt("BOARD_UID", 0));
        this.article.setArticleUid(getArguments().getInt("ARTICLE_UID", 0));
        this.article.setGrantEdit(getArguments().getBoolean("ARTICLE_GRANT_EDIT", false));

        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.article.getBoardUid() == ID_ANONYMOUS) {
            articlePresenter.getAnonymousArticle(this.article.getArticleUid());
            return;
        }
        articlePresenter.getArticle(this.article.getArticleUid());
        articlePresenter.checkGranted(this.article.getArticleUid());
    }


    private void init() {
        switch (this.article.getBoardUid()) {
            case ID_FREE:
                koinBaseAppbarDark.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                koinBaseAppbarDark.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                koinBaseAppbarDark.setTitleText("익명게시판");
                visibleAnonymousBoard();
                break;
            default:
                koinBaseAppbarDark.setTitleText("게시판");
                break;
        }

        setPresenter(new ArticlePresenter(this, new CommunityRestInteractor()));

        //hide keyboard
        inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().getApplicationContext().INPUT_METHOD_SERVICE);
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
    public void showLoading() {
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.getInstance().makeShort(message);
    }

    /**
     * 서버로부터 받은 게시글 데이터를 저장하는 메소드
     *
     * @param article 게시글 파라미터
     */
    @Override
    public void onArticleDataReceived(Article article) {
        if (this.article.getBoardUid() != ID_ANONYMOUS)
            checkRequiredInfo();
        this.article.setTitle(article.getTitle());
        this.article.setAuthorNickname(article.getAuthorNickname());
        this.article.setArticleUid(article.getArticleUid());
        this.article.setCreateDate(article.getCreateDate().substring(0, 10) + " " + article.getCreateDate().substring(11, 16));
        this.article.setUpdateDate(article.getUpdateDate().substring(0, 10) + " " + article.getUpdateDate().substring(11, 16));
        this.article.setHitCount(article.getHitCount());
        this.article.setContent(article.getContent());
        this.article.setTag(article.getTag());
        this.articleCommentCount = String.valueOf(article.getCommentCount());
        updateUserInterface();

    }

    /**
     *
     */
    @Override
    public void checkRequiredInfo() {
        String nickName;
        AuthorizeConstant authorize = AuthorizeManager.getAuthorize(getContext());
        if (authorize == AuthorizeConstant.ANONYMOUS) {
            return;
        } else {
            nickName = AuthorizeManager.getUser(getContext()).getUserNickName();
        }
        if (FormValidatorUtil.validateStringIsEmpty(nickName)) {
        } else {
        }
    }

    @Override
    public void updateUserInterface() {
        StringBuilder title;
        StringBuilder commentButtonText;
        title = new StringBuilder("<font color='black'>" + this.article.getTitle() + " " + "</font>");
        commentButtonText = new StringBuilder("<font color='black'>댓글</font>");
        if (this.articleCommentCount != null && Integer.parseInt(this.articleCommentCount) > 0) {
            title.append("<font color='#175c8e'>" + "(").append(this.articleCommentCount).append(")").append("</font>");
            commentButtonText.append("<font color='#175c8e'>").append(" ").append(this.articleCommentCount).append("</font>");
        }
        textViewTitle.setText(Html.fromHtml(title.toString()), TextView.BufferType.SPANNABLE);
        buttonCommentWrite.setText(Html.fromHtml(commentButtonText.toString()), TextView.BufferType.SPANNABLE);
        textViewWriter.setText(this.article.getAuthorNickname());
        textViewCreateDate.setText(Html.fromHtml(this.article.getCreateDate()));
        textViewViewCount.setText(String.valueOf(this.article.getHitCount()));
        editorContent.setDividerLayout(R.layout.tmpl_divider_layout);
        editorContent.setEditorImageLayout(R.layout.rich_editor_image_layout);
        editorContent.setListItemLayout(R.layout.tmpl_list_item);
        editorContent.clearAllContents();

        // 리치 에디터 폰트 설정
        editorContent.setHeadingTypeface(getEditorTypeface());
        editorContent.setContentTypeface(getEditorTypeface());
        editorContent.render(renderHtmltoString(this.article.getContent()));

        changeEditorChildViewSetting(editorContent, EDITOR_LEFT_PADDING, EDITOR_TOP_PADDING, EDITOR_RIGHT_PADDING, EDITOR_BOTTOM_PADDING);
//        mCommentRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Editor 영역의 Padding 값을 설정하여 LineSpacing 값을 수정하는 메소드
     *
     * @param view   Editor extends LinearLayout
     * @param left   EditText(한 줄)의 왼쪽 Padding
     * @param top    EditText(한 줄)의 위쪽 Padding
     * @param right  EditText(한 줄)의 오른쪽 Padding
     * @param bottom EditText(한 줄)의 아래쪽 Padding
     */
    public void changeEditorChildViewSetting(View view, int left, int top, int right, int bottom) {
        // TODO: TextView일때 Copy가 가능하도록 구현(한줄씩만 Copy가 됨)
        if (view == null)
            return;
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                changeEditorChildViewSetting(((ViewGroup) view).getChildAt((i)), left, top, right, bottom);
            }
        } else {
            view.setPadding(left, top, right, bottom);
            if (view instanceof TextView) {
                ((TextView) view).setTextIsSelectable(true);
            }
        }
    }

    public Map<Integer, String> getEditorTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/notosans_regular.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/notosanscjkkr_bold.otf");
        typefaceMap.put(Typeface.ITALIC, "fonts/notosans_medium.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/notosans_light.ttf");

        return typefaceMap;
    }

    @Override
    public void onArticleDeleteCompleteReceived(boolean isSuccess) {
        Bundle result = new Bundle();
        result.putString(RES_CODE, RES_CODE_ARTICLE_DELETED);
        getParentFragmentManager().setFragmentResult(REQ_CODE_ARTICLE, result);
        NavigationManger.getNavigationController(getActivity()).popBackStack();
    }


    public String renderHtmltoString(String url) {
        if (url == null) return "";
        String str = url.replace("<div>", "").replace("<div/>", "").replace("<img", "</p><img").replace("<p></p><img", "<img").replace(".jpg\\\"></p>", ".jpg\\\">")
                .replace(".png\\\"></p>", ".png\\\">");
        Log.d("render : ", str);
        return str;
    }

    public void onClickEditButton() {
        String password = articlePasswordEdittext.getText().toString();
        if (this.article.getBoardUid() == ID_ANONYMOUS) {
            if (!password.isEmpty())
                articlePresenter.checkAnonymousAdjustGranted(this.article.getArticleUid(), password);
            else
                ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요");
            return;
        }

        switch (this.article.getBoardUid()) {
            case ID_FREE:
                goToArticleEdit(true, R.id.navi_free_article_edit_action);
                break;
            case ID_RECRUIT:
                goToArticleEdit(true, R.id.navi_recruit_article_edit_action);
                break;
            case ID_ANONYMOUS:
                goToArticleEdit(true, R.id.navi_anonymous_article_edit_action);
                break;
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
        if (this.article.getBoardUid() != ID_ANONYMOUS)
            SnackbarUtil.makeLongSnackbarActionYes(nestedScrollView, "게시글을 삭제할까요?\n댓글도 모두 사라집니다.", () -> articlePresenter.deleteArticle(this.article.getArticleUid()));
        else if (this.article.getBoardUid() == ID_ANONYMOUS) {
            String password = articlePasswordEdittext.getText().toString();
            if (!password.isEmpty())
                articlePresenter.checkAnonymousDeleteGranted(this.article.getArticleUid(), password);
            else
                ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요");
        }
    }

    @Override
    public void showEditAndDeleteMenu() {
        this.article.setGrantEdit(true);
        showEditAndDeleteButton(this.article.isGrantEdit());
    }

    @Override
    public void hideEditAndDeleteMenu() {
        this.article.setGrantEdit(false);
        showEditAndDeleteButton(this.article.isGrantEdit());
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


    @Override
    public void showErrorDeleteContent() {
        ToastUtil.getInstance().makeShort("삭제에 실패했습니다.");
    }

    @Override
    public void showSuccessDeleteContent() {
        ToastUtil.getInstance().makeShort("삭제되었습니다.");
        Bundle result = new Bundle();
        result.putString(RES_CODE, RES_CODE_ARTICLE_DELETED);
        getParentFragmentManager().setFragmentResult(REQ_CODE_ARTICLE, result);
        NavigationManger.getNavigationController(getActivity()).popBackStack();
    }

    @Override
    public void showErrorAdjustGrantedContent() {
        ToastUtil.getInstance().makeShort("비밀번호가 틀렸습니다.");
    }

    @Override
    public void showSuccessAdjustGrantedContent() {
        goToAnonymousArticleEdit(R.id.navi_anonymous_article_edit_action);
    }

    @Override
    public void showErrorGrantedDeleteContent() {
        ToastUtil.getInstance().makeShort("게시물을 수정하거나 삭제할 권한이 없습니다.");
    }

    @Override
    public void showSuccessGrantedDeleteContent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String password = articlePasswordEdittext.getText().toString();
        builder.setTitle("알림")
                .setMessage("게시글을 삭제할까요?\n댓글도 모두 사라집니다.")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    articlePresenter.deleteAnoymousArticle(this.article.getArticleUid(), password);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }


    public void onClickCreateButton() {
        if (this.article.getBoardUid() != ID_ANONYMOUS) {
            AuthorizeConstant authorize = AuthorizeManager.getAuthorize(getContext());
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                AuthorizeManager.showLoginRequestDialog(getActivity());
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && AuthorizeManager.getUser(getContext()).getUserNickName() == null) {
                AuthorizeManager.showNickNameRequestDialog(getActivity());
                return;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putInt("BOARD_UID", this.article.getBoardUid());

        switch (this.article.getBoardUid()) {
            case ID_FREE:
                goToArticleEdit(false, R.id.navi_free_article_edit_action);
                break;
            case ID_RECRUIT:
                goToArticleEdit(false, R.id.navi_recruit_article_edit_action);
                break;
            case ID_ANONYMOUS:
                goToArticleEdit(false, R.id.navi_anonymous_article_edit_action);
                break;
        }

    }

    @OnClick(R.id.article_comment_write_button)
    public void onClickedCommentWriteButton() {
        switch (this.article.getBoardUid()) {
            case ID_FREE:
                goToComment(R.id.navi_free_article_comment_action);
                break;
            case ID_RECRUIT:
                goToComment(R.id.navi_recruit_article_comment_action);
                break;
            case ID_ANONYMOUS:
                goToComment(R.id.navi_anonymous_article_comment_action);
                break;
        }
    }

    private void goToArticleEdit(boolean isEdit, int id) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_EDIT", isEdit);
        bundle.putInt("ARTICLE_UID", this.article.getArticleUid());
        bundle.putInt("BOARD_UID", this.article.getBoardUid());
        bundle.putString("ARTICLE_TITLE", this.article.getTitle());
        bundle.putString("ARTICLE_CONTENT", this.article.getContent());
        NavigationManger.getNavigationController(getActivity()).navigate(id, bundle, NavigationManger.getNavigationAnimation());
    }

    private void goToAnonymousArticleEdit(int id) {
        String password = articlePasswordEdittext.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_EDIT", true);
        bundle.putInt("ARTICLE_UID", this.article.getArticleUid());
        bundle.putInt("BOARD_UID", this.article.getBoardUid());
        bundle.putString("ARTICLE_TITLE", this.article.getTitle());
        bundle.putString("ARTICLE_CONTENT", this.article.getContent());
        bundle.putString("NICKNAME", this.article.getAuthorNickname());
        bundle.putString("PASSWORD", password);
        NavigationManger.getNavigationController(getActivity()).navigate(id, bundle, NavigationManger.getNavigationAnimation());
    }

    private void goToComment(int id) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_EDIT", true);
        bundle.putInt("ARTICLE_UID", this.article.getArticleUid());
        bundle.putInt("BOARD_UID", this.article.getBoardUid());
        bundle.putString("ARTICLE_TITLE", this.article.getTitle());
        bundle.putString("ARTICLE_CONTENT", this.article.getContent());
        bundle.putString("NICKNAME", this.article.getAuthorNickname());
        NavigationManger.getNavigationController(getActivity()).navigate(id, bundle, NavigationManger.getNavigationAnimation());
    }
}
