package in.koreatech.koin.ui.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.board.presenter.ArticleContract;
import in.koreatech.koin.ui.board.presenter.ArticlePresenter;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class ArticleActivity extends KoinNavigationDrawerActivity implements ArticleContract.View {
    private final String TAG = "ArticleActivity";
    private final int REQ_CODE_ARTICLE_EDIT = 1;
    private final int REQ_CODE_ARTICLE = 2;
    private final int RES_CODE_ARTICLE_DELETED = 1;
    private final static int EDITOR_LEFT_PADDING = 0;       // Editor 내 EditText의 왼쪽 Padding 값
    private final static int EDITOR_TOP_PADDING = 10;       // Editor 내 EditText의 위쪽 Padding 값
    private final static int EDITOR_RIGHT_PADDING = 0;      // Editor 내 EditText의 오른쪽 Padding 값
    private final static int EDITOR_BOTTOM_PADDING = 10;    // Editor 내 EditText의 아래쪽 Padding 값
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        this.context = this;
        this.article = new Article();
        this.article.boardUid = getIntent().getIntExtra("BOARD_UID", 0);
        this.article.articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
        this.article.isGrantEdit = getIntent().getBooleanExtra("ARTICLE_GRANT_EDIT", false);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.article.boardUid == ID_ANONYMOUS) {
            articlePresenter.getAnonymousArticle(this.article.articleUid);
            return;
        }
        articlePresenter.getArticle(this.article.articleUid);
        articlePresenter.checkGranted(this.article.articleUid);
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
        switch (this.article.boardUid) {
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

//        mLayoutManager = new LinearLayoutManager(this);
//        mCommentArrayList = new ArrayList<>();
//
//        mCommentRecyclerAdapter = new CommentRecyclerAdapter(this.context, mCommentArrayList);
//        mCommentRecyclerAdapter.setCustomOnClickListener(this);
//
//        mCommentRecyclerView.setHasFixedSize(true);
//        mCommentRecyclerView.setLayoutManager(mLayoutManager);
//        mCommentRecyclerView.setAdapter(mCommentRecyclerAdapter);

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

//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            case R.id.menu_article_refresh:
//                if (this.article.boardUid != ID_ANONYMOUS) {
//                    articlePresenter.getArticle(this.article.articleUid);
//                    articlePresenter.checkGranted(this.article.articleUid);
//                } else {
//                    articlePresenter.getAnonymousArticle(this.article.articleUid);
//                }
//                return true;
//            case R.id.menu_article_edited:
//                onClickEditButton();
//                return true;
//            case R.id.menu_article_deleted:
//                onClickRemoveButton();
//                return true;
//            case R.id.menu_article_solved:
//                onClickSolveButton();
//                return true;
//            case R.id.menu_article_unsolved:
//                onClickUnsolveButton();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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
        if (this.article.boardUid != ID_ANONYMOUS)
            checkRequiredInfo();

        this.article.title = article.title;
        this.article.authorNickname = article.authorNickname;
        this.article.authorUid = article.authorUid;
        this.article.createDate = article.createDate.substring(0, 10) + " " + article.createDate.substring(11, 16);
        this.article.updateDate = article.updateDate.substring(0, 10) + " " + article.updateDate.substring(11, 16);
        this.article.hitCount = article.hitCount;
        this.article.content = article.content;
        this.article.tag = article.tag;
        this.articleCommentCount = String.valueOf(article.commentCount);

//        mCommentRecyclerAdapter.setArticle(this.article);
//
//        mCommentArrayList.clear();
//
//        if (article.commentCount > 0) {
//            if (this.article.boardUid == ID_ANONYMOUS) {
//                for (int i = 0; i < article.commentArrayList.size(); i++) {
//                    article.commentArrayList.get(i).grantEdit = true;
//                    article.commentArrayList.get(i).grantDelete = true;
//                }
//            }
//            mCommentArrayList.addAll(article.commentArrayList);
//        }

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
        title = new StringBuilder("<font color='black'>" + this.article.title + " " + "</font>");
        commentButtonText = new StringBuilder("<font color='black'>댓글</font>");
        if (this.articleCommentCount != null && Integer.parseInt(this.articleCommentCount) > 0) {
            title.append("<font color='#175c8e'>" + "(").append(this.articleCommentCount).append(")").append("</font>");
            commentButtonText.append("<font color='#175c8e'>").append(" ").append(this.articleCommentCount).append("</font>");
        }
        textViewTitle.setText(Html.fromHtml(title.toString()), TextView.BufferType.SPANNABLE);
        buttonCommentWrite.setText(Html.fromHtml(commentButtonText.toString()), TextView.BufferType.SPANNABLE);
        textViewWriter.setText(this.article.authorNickname);
        textViewCreateDate.setText(Html.fromHtml(this.article.createDate));
        textViewViewCount.setText(String.valueOf(this.article.hitCount));
        editorContent.setDividerLayout(R.layout.tmpl_divider_layout);
        editorContent.setEditorImageLayout(R.layout.rich_editor_image_layout);
        editorContent.setListItemLayout(R.layout.tmpl_list_item);
        editorContent.clearAllContents();

        // 리치 에디터 폰트 설정
        editorContent.setHeadingTypeface(getEditorTypeface());
        editorContent.setContentTypeface(getEditorTypeface());
        editorContent.render(renderHtmltoString(this.article.content));

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
        setResult(RES_CODE_ARTICLE_DELETED);
        finish();

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
        if (this.article.boardUid == ID_ANONYMOUS) {
            if (!password.isEmpty())
                articlePresenter.checkAnonymousAdjustGranted(this.article.articleUid, password);
            else
                ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요");
            return;
        }

        Intent intent = new Intent(this, ArticleEditActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", this.article.articleUid);
        intent.putExtra("BOARD_UID", this.article.boardUid);
        intent.putExtra("ARTICLE_TITLE", this.article.title);
        intent.putExtra("ARTICLE_CONTENT", this.article.content);
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

//    @Override
//    public void onClickCommentRemoveButton(final Comment comment) {
//        if (this.article.boardUid == ID_ANONYMOUS) {
////            deleteCommentDialog(comment);
//            return;
//        }
//        SnackbarUtil.makeLongSnackbarActionYes(mCommentRecyclerView, "댓글을 삭제할까요?", () -> {
//            if (comment.grantDelete) {
//                articlePresenter.deleteComment(this.article.articleUid, comment.commentUid);
//            }
//        });
//    }
//
//    @Override
//    public void onClickCommentModifyButton(Comment comment) {
//        mPrevCommentContent = comment.content;
//        if (this.article.boardUid == ID_ANONYMOUS) {
////            adjustCommentDialog(comment);
//            return;
//        }
//        makeEditCommentDialog(comment);
//    }

    public void onClickRemoveButton() {
        if (this.article.boardUid != ID_ANONYMOUS)
            SnackbarUtil.makeLongSnackbarActionYes(nestedScrollView, "게시글을 삭제할까요?\n댓글도 모두 사라집니다.", () -> articlePresenter.deleteArticle(this.article.articleUid));
        else if (this.article.boardUid == ID_ANONYMOUS) {
            String password = articlePasswordEdittext.getText().toString();
            if (!password.isEmpty())
                articlePresenter.checkAnonymousDeleteGranted(this.article.articleUid, password);
            else
                ToastUtil.getInstance().makeShort("비밀번호를 입력해주세요");

        }

    }

//    @Override
//    public void makeEditCommentDialog(Comment comment) {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit, null, false);
//
//        final EditText editTextContent = dialogView.findViewById(R.id.dialog_edittext_comment_modify);
//        editTextContent.setText(comment.content);
//        editTextContent.setSelection(comment.content.length());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.KAPDialog);
//        builder.setView(dialogView);
//        builder.setPositiveButton("수정",
//                (dialog, which) -> {
//                    if (!FormValidatorUtil.validateStringIsEmpty(editTextContent.getText().toString())) {
//                        comment.content = editTextContent.getText().toString().trim();
//                        if (mPrevCommentContent.compareTo(comment.content) != 0) {
//                            articlePresenter.updateComment(this.article.articleUid, new Comment(comment.commentUid, comment.content));
//                        }
//                    }
//                });
//        builder.setNegativeButton("취소",
//                (dialog, which) -> {
//                });
//
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    @Override
    public void showEditAndDeleteMenu() {
        this.article.isGrantEdit = true;
        showEditAndDeleteButton(this.article.isGrantEdit);
    }

    @Override
    public void hideEditAndDeleteMenu() {
        this.article.isGrantEdit = false;
        showEditAndDeleteButton(this.article.isGrantEdit);
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

    //    @Override
//    public void showErrorDeleteComment() {
//        ToastUtil.getInstance().makeShort( "삭제에 실패했습니다.");
//    }
//
//    @Override
//    public void showSuccessDeleteComment() {
//        ToastUtil.getInstance().makeShort( "삭제되었습니다.");
//    }
//
//    @Override
//    public void showErrorAdjustComment() {
//        ToastUtil.getInstance().makeShort( "수정에 실패했습니다.");
//    }
//
//    @Override
    public void showSuccessAdjustComment() {
        ToastUtil.getInstance().makeShort("수정되었습니다.");
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
        intent.putExtra("ARTICLE_UID", this.article.articleUid);
        intent.putExtra("BOARD_UID", this.article.boardUid);
        intent.putExtra("ARTICLE_TITLE", this.article.title);
        intent.putExtra("ARTICLE_CONTENT", this.article.content);
        intent.putExtra("NICKNAME", this.article.authorNickname);
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
                    articlePresenter.deleteAnoymousArticle(this.article.articleUid, password);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

//    @Override
//    public void showErrorGrantedDeleteComment() {
//        ToastUtil.getInstance().makeShort( "비밀번호가 틀렸습니다.");
//    }
//
//    @Override
//    public void showSuccessGrantedDeleteComment() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("알림")
//                .setMessage("정말로 삭제하시겠습니까?")
//                .setCancelable(false)
//                .setPositiveButton("확인", (dialog, whichButton) -> {
//                    articlePresenter.deleteAnonymousComment(this.article.articleUid, mCommentUId, mCommentPassword);
//                })
//                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
//        AlertDialog dialog = builder.create();    // 알림창 객체 생성
//        dialog.show();    // 알림창 띄우기
//    }
//
//    @Override
//    public void showErrorGrantedAdjustComment() {
//        ToastUtil.getInstance().makeShort( "비밀번호가 틀렸습니다.");
//    }
//
//    @Override
//    public void showSuccessGrantedAdjustComment() {
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit, null, false);
//
//        final EditText editTextContent = dialogView.findViewById(R.id.dialog_edittext_comment_modify);
//        editTextContent.setText(mComment.content);
//        editTextContent.setSelection(mComment.content.length());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.KAPDialog);
//        builder.setView(dialogView);
//        builder.setPositiveButton("수정",
//                (dialog, which) -> {
//                    if (!FormValidatorUtil.validateStringIsEmpty(editTextContent.getText().toString())) {
//                        mComment.content = editTextContent.getText().toString().trim();
//                        if (mPrevCommentContent.compareTo(mComment.content) != 0) {
//                            articlePresenter.updateAnonymousComment(this.article.articleUid, new Comment(mComment.commentUid, mComment.content, mCommentPassword));
//                        }
//                    }
//                });
//        builder.setNegativeButton("취소",
//                (dialog, which) -> {
//                });
//
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    public void onClickCreateButton() {
        if (this.article.boardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = getAuthorize();
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        Intent intent = new Intent(this.context, ArticleEditActivity.class);
        intent.putExtra("BOARD_UID", this.article.boardUid);
        startActivityForResult(intent, REQ_CODE_ARTICLE_EDIT);

    }

    @OnClick(R.id.article_comment_write_button)
    public void onClickedCommentWriteButton() {
        Intent intent = new Intent(this, ArticleCommentActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", this.article.articleUid);
        intent.putExtra("BOARD_UID", this.article.boardUid);
        intent.putExtra("ARTICLE_TITLE", this.article.title);
        intent.putExtra("ARTICLE_CONTENT", this.article.content);
        intent.putExtra("NICKNAME", this.article.authorNickname);
        startActivity(intent);
    }


//    public void createCommentCreateDialog() {
//        AskNicknamePasswordDialog dialog = new AskNicknamePasswordDialog(this, "알림", "닉네임과 비밀번호를 입력해주세요", AskNicknamePasswordDialog.YES_NICKNAME);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (!FormValidatorUtil.validateStringIsEmpty(mEditTextComment.getText().toString())) {
//                articlePresenter.createAnonymousComment(this.article.articleUid, mEditTextComment.getText().toString(), dialog.getNickName(), dialog.getPassword());
//                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
//            }
//            mEditTextComment.setText("");
//        });
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Window window = dialog.getWindow();
//        int x = (int) (size.x * 0.8f);
//        int y = (int) (size.y * 0.4f);
//        window.setLayout(x, y);
//    }
//
//    public void deleteCommentDialog(Comment comment) {
//        AskNicknamePasswordDialog dialog = new AskNicknamePasswordDialog(this, "알림", "비밀번호를 입력해주세요", AskNicknamePasswordDialog.NO_NICKNAME);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (dialog.isCancelled()) return;
//            mCommentUId = comment.commentUid;
//            mCommentPassword = dialog.getPassword();
//            articlePresenter.checkAnonymousCommentDeleteGranted(comment.commentUid, dialog.getPassword());
//            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
//        });
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Window window = dialog.getWindow();
//        int x = (int) (size.x * 0.8f);
//        int y = (int) (size.y * 0.4f);
//        window.setLayout(x, y);
//    }
//
//    public void adjustCommentDialog(Comment comment) {
//        AskNicknamePasswordDialog dialog = new AskNicknamePasswordDialog(this, "알림", "비밀번호를 입력해주세요", AskNicknamePasswordDialog.NO_NICKNAME);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (dialog.isCancelled()) return;
//            mCommentUId = comment.commentUid;
//            mCommentPassword = dialog.getPassword();
//            mComment = comment;
//            articlePresenter.checkAnonymousCommentAdjustGranted(comment.commentUid, dialog.getPassword());
//            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
//        });
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Window window = dialog.getWindow();
//        int x = (int) (size.x * 0.8f);
//        int y = (int) (size.y * 0.4f);
//        window.setLayout(x, y);
//    }
//
//    public void deleteContentDialog() {
//        AskNicknamePasswordDialog dialog = new AskNicknamePasswordDialog(this, "알림", "비밀번호를 입력해주세요", AskNicknamePasswordDialog.NO_NICKNAME);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (dialog.isCancelled()) return;
//            mContentPassword = dialog.getPassword();
//            articlePresenter.checkAnonymousDeleteGranted(this.article.articleUid, mContentPassword);
//            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
//        });
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Window window = dialog.getWindow();
//        int x = (int) (size.x * 0.8f);
//        int y = (int) (size.y * 0.4f);
//        window.setLayout(x, y);
//    }
//
//    public void adjustContentDialog() {
//        AskNicknamePasswordDialog dialog = new AskNicknamePasswordDialog(this, "알림", "비밀번호를 입력해주세요", AskNicknamePasswordDialog.NO_NICKNAME);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (dialog.isCancelled()) return;
//            mContentPassword = dialog.getPassword();
//            articlePresenter.checkAnonymousAdjustGranted(this.article.articleUid, mContentPassword);
//            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
//        });
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Window window = dialog.getWindow();
//        int x = (int) (size.x * 0.8f);
//        int y = (int) (size.y * 0.4f);
//        window.setLayout(x, y);
//    }
}
