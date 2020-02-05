package in.koreatech.koin.service_board.ui;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorContent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.networks.entity.Article;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.interactors.CommunityRestInteractor;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.SnackbarUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_board.contracts.ArticleContract;
import in.koreatech.koin.service_board.presenters.ArticlePresenter;
import in.koreatech.koin.ui.LoginActivity;
import in.koreatech.koin.ui.UserInfoActivity;

import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.core.constants.URLConstant.COMMUNITY.ID_RECRUIT;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public class ArticleActivity extends KoinNavigationDrawerActivity implements ArticleContract.View {
    private final String TAG = ArticleActivity.class.getSimpleName();
    private final int REQ_CODE_ARTICLE_EDIT = 1;
    private final int REQ_CODE_ARTICLE = 2;
    private final int RES_CODE_ARTICLE_DELETED = 1;
    private final static int EDITOR_LEFT_PADDING = 0;       // Editor 내 EditText의 왼쪽 Padding 값
    private final static int EDITOR_TOP_PADDING = 10;       // Editor 내 EditText의 위쪽 Padding 값
    private final static int EDITOR_RIGHT_PADDING = 0;      // Editor 내 EditText의 오른쪽 Padding 값
    private final static int EDITOR_BOTTOM_PADDING = 10;    // Editor 내 EditText의 아래쪽 Padding 값
    private Context mContext;

    private InputMethodManager mInputMethodManager;
    private GenerateProgressTask generateProgressTask;

//    private CommentRecyclerAdapter mCommentRecyclerAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//    private ArrayList<Comment> mCommentArrayList;


    private ArticlePresenter mArticlePresenter;

    private Article mArticle;
    private String mArticleCommentCount;

    private String mPrevCommentContent;
    private int mCommentUId;
    private String mContentPassword;
    private String mCommentPassword;
    private Comment mComment;

    @BindView(R.id.article_title)
    TextView mTextViewTitle;
    @BindView(R.id.koin_base_app_bar_dark)
    KoinBaseAppbarDark mKoinBaseAppbarDark;
    @BindView(R.id.article_view_count)
    TextView mTextViewViewCount;
    @BindView(R.id.article_content)
    Editor mEditorContent;

    @BindView(R.id.article_writer)
    TextView mTextViewWriter;
    @BindView(R.id.article_create_date)
    TextView mTextViewCreateDate;
    @BindView(R.id.article_scrollview)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.article_comment_write_button)
    Button mButtonCommentWrite;
    @BindView(R.id.article_delete_button)
    Button mArticleDeleteButton;
    @BindView(R.id.article_edit_button)
    Button mArticleEditButton;
    @BindView(R.id.article_edittext_password)
    EditText mArticlePasswordEdittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        this.mContext = this;

        mArticle = new Article();
        mArticle.boardUid = getIntent().getIntExtra("BOARD_UID", 0);
        mArticle.articleUid = getIntent().getIntExtra("ARTICLE_UID", 0);
        mArticle.isGrantEdit = getIntent().getBooleanExtra("ARTICLE_GRANT_EDIT", false);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mArticle.boardUid == ID_ANONYMOUS) {
            mArticlePresenter.getAnonymousArticle(mArticle.articleUid);
            return;
        }
        mArticlePresenter.getArticle(mArticle.articleUid);
        mArticlePresenter.checkGranted(mArticle.articleUid);
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
        switch (mArticle.boardUid) {
            case ID_FREE:
                mKoinBaseAppbarDark.setTitleText("자유게시판");
                break;
            case ID_RECRUIT:
                mKoinBaseAppbarDark.setTitleText("취업게시판");
                break;
            case ID_ANONYMOUS:
                mKoinBaseAppbarDark.setTitleText("익명게시판");
                visibleAnonymousBoard();
                break;
            default:
                mKoinBaseAppbarDark.setTitleText("게시판");
                break;
        }

//        mLayoutManager = new LinearLayoutManager(this);
//        mCommentArrayList = new ArrayList<>();
//
//        mCommentRecyclerAdapter = new CommentRecyclerAdapter(mContext, mCommentArrayList);
//        mCommentRecyclerAdapter.setCustomOnClickListener(this);
//
//        mCommentRecyclerView.setHasFixedSize(true);
//        mCommentRecyclerView.setLayoutManager(mLayoutManager);
//        mCommentRecyclerView.setAdapter(mCommentRecyclerAdapter);

        setPresenter(new ArticlePresenter(this, new CommunityRestInteractor()));

        //hide keyboard
        mInputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
    }

    public void visibleAnonymousBoard() {
        mArticlePasswordEdittext.setVisibility(View.VISIBLE);
        mArticleDeleteButton.setVisibility(View.VISIBLE);
        mArticleEditButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(ArticlePresenter presenter) {
        this.mArticlePresenter = presenter;
    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            case R.id.menu_article_refresh:
//                if (mArticle.boardUid != ID_ANONYMOUS) {
//                    mArticlePresenter.getArticle(mArticle.articleUid);
//                    mArticlePresenter.checkGranted(mArticle.articleUid);
//                } else {
//                    mArticlePresenter.getAnonymousArticle(mArticle.articleUid);
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

    }

    /**
     * 서버로부터 받은 게시글 데이터를 저장하는 메소드
     *
     * @param article 게시글 파라미터
     */
    @Override
    public void onArticleDataReceived(Article article) {
        if (mArticle.boardUid != ID_ANONYMOUS)
            checkRequiredInfo();

        mArticle.title = article.title;
        mArticle.authorNickname = article.authorNickname;
        mArticle.authorUid = article.authorUid;
        mArticle.createDate = article.createDate.substring(0, 10) + " " + article.createDate.substring(11, 16);
        mArticle.updateDate = article.updateDate.substring(0, 10) + " " + article.updateDate.substring(11, 16);
        mArticle.hitCount = article.hitCount;
        mArticle.content = article.content;
        mArticle.tag = article.tag;
        mArticleCommentCount = String.valueOf(article.commentCount);

//        mCommentRecyclerAdapter.setArticle(mArticle);
//
//        mCommentArrayList.clear();
//
//        if (article.commentCount > 0) {
//            if (mArticle.boardUid == ID_ANONYMOUS) {
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
            nickName = DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName;
        }
        if (FormValidatorUtil.validateStringIsEmpty(nickName)) {
        } else {
        }
    }

    public AuthorizeConstant getAuthorize() {
        AuthorizeConstant authorizeConstant;
        try {
            authorizeConstant = DefaultSharedPreferencesHelper.getInstance().checkAuthorize();
        } catch (NullPointerException e) {
            DefaultSharedPreferencesHelper.getInstance().init(getApplicationContext());
            authorizeConstant = DefaultSharedPreferencesHelper.getInstance().checkAuthorize();
        }
        return authorizeConstant;
    }

    @Override
    public void updateUserInterface() {
        StringBuilder title;
        StringBuilder commentButtonText;
        title = new StringBuilder("<font color='black'>" + mArticle.title + " " + "</font>");
        commentButtonText = new StringBuilder("<font color='black'>댓글</font>");
        if (mArticleCommentCount != null && Integer.parseInt(mArticleCommentCount) > 0) {
            title.append("<font color='#175c8e'>" + "(").append(mArticleCommentCount).append(")").append("</font>");
            commentButtonText.append("<font color='#175c8e'>").append(" ").append(mArticleCommentCount).append("</font>");
        }
        mTextViewTitle.setText(Html.fromHtml(title.toString()), TextView.BufferType.SPANNABLE);
        mButtonCommentWrite.setText(Html.fromHtml(commentButtonText.toString()), TextView.BufferType.SPANNABLE);
        mTextViewWriter.setText(mArticle.authorNickname);
        mTextViewCreateDate.setText(Html.fromHtml(mArticle.createDate));
        mTextViewViewCount.setText(String.valueOf(mArticle.hitCount));
        mEditorContent.setDividerLayout(R.layout.tmpl_divider_layout);
        mEditorContent.setEditorImageLayout(R.layout.rich_editor_image_layout);
        mEditorContent.setListItemLayout(R.layout.tmpl_list_item);
        mEditorContent.clearAllContents();

        // 리치 에디터 폰트 설정
        mEditorContent.setHeadingTypeface(getEditorTypeface());
        mEditorContent.setContentTypeface(getEditorTypeface());
        mEditorContent.render(renderHtmltoString(mArticle.content));

        changeEditorChildViewSetting(mEditorContent, EDITOR_LEFT_PADDING, EDITOR_TOP_PADDING, EDITOR_RIGHT_PADDING, EDITOR_BOTTOM_PADDING);
//        mCommentRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * Editor 영역의 Padding 값을 설정하여 LineSpacing 값을 수정하는 메소드
     * @param view Editor extends LinearLayout
     * @param left EditText(한 줄)의 왼쪽 Padding
     * @param top EditText(한 줄)의 위쪽 Padding
     * @param right EditText(한 줄)의 오른쪽 Padding
     * @param bottom EditText(한 줄)의 아래쪽 Padding
     */
    public void changeEditorChildViewSetting(View view,  int left, int top, int right, int bottom) {
        // TODO: TextView일때 Copy가 가능하도록 구현(한줄씩만 Copy가 됨)
        if (view == null)
            return;
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                changeEditorChildViewSetting(((ViewGroup) view).getChildAt((i)), left, top, right, bottom);
            }
        } else {
            view.setPadding(left, top, right, bottom);
            if(view instanceof TextView) {
                ((TextView) view).setTextIsSelectable(true);
            }
        }
    }

    public Map<Integer, String> getEditorTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL,"fonts/notosans_regular.ttf");
        typefaceMap.put(Typeface.BOLD,"fonts/notosanscjkkr_bold.otf");
        typefaceMap.put(Typeface.ITALIC,"fonts/notosans_medium.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC,"fonts/notosans_light.ttf");

        return typefaceMap;
    }

    @Override
    public void onArticleDeleteCompleteReceived(boolean isSuccess) {
        setResult(RES_CODE_ARTICLE_DELETED);
        finish();

    }


    public String renderHtmltoString(String url) {
        if (url == null) return "";
        String str = url.replace("<div>", "").replace("<div/>", "").replace("<img", "</p><img").replace("<p></p><img","<img").replace(".jpg\\\"></p>",".jpg\\\">")
                .replace(".png\\\"></p>",".png\\\">");
        Log.d("render : ", str);
        return str;
    }

    public void onClickEditButton() {
        String password = mArticlePasswordEdittext.getText().toString();
        if (mArticle.boardUid == ID_ANONYMOUS) {
            if (!password.isEmpty())
                mArticlePresenter.checkAnonymousAdjustGranted(mArticle.articleUid, password);
            else
                ToastUtil.makeShortToast(mContext, "비밀번호를 입력해주세요");
            return;
        }

        Intent intent = new Intent(this, ArticleEditActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", mArticle.articleUid);
        intent.putExtra("BOARD_UID", mArticle.boardUid);
        intent.putExtra("ARTICLE_TITLE", mArticle.title);
        intent.putExtra("ARTICLE_CONTENT", mArticle.content);
        startActivity(intent);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId())
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
//        if (mArticle.boardUid == ID_ANONYMOUS) {
////            deleteCommentDialog(comment);
//            return;
//        }
//        SnackbarUtil.makeLongSnackbarActionYes(mCommentRecyclerView, "댓글을 삭제할까요?", () -> {
//            if (comment.grantDelete) {
//                mArticlePresenter.deleteComment(mArticle.articleUid, comment.commentUid);
//            }
//        });
//    }
//
//    @Override
//    public void onClickCommentModifyButton(Comment comment) {
//        mPrevCommentContent = comment.content;
//        if (mArticle.boardUid == ID_ANONYMOUS) {
////            adjustCommentDialog(comment);
//            return;
//        }
//        makeEditCommentDialog(comment);
//    }

    public void onClickRemoveButton() {
        if (mArticle.boardUid != ID_ANONYMOUS)
            SnackbarUtil.makeLongSnackbarActionYes(mNestedScrollView, "게시글을 삭제할까요?\n댓글도 모두 사라집니다.", () -> mArticlePresenter.deleteArticle(mArticle.articleUid));
        else if (mArticle.boardUid == ID_ANONYMOUS) {
            String password = mArticlePasswordEdittext.getText().toString();
            if (!password.isEmpty())
                mArticlePresenter.checkAnonymousDeleteGranted(mArticle.articleUid, password);
            else
                ToastUtil.makeShortToast(mContext, "비밀번호를 입력해주세요");

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
//                            mArticlePresenter.updateComment(mArticle.articleUid, new Comment(comment.commentUid, comment.content));
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
        mArticle.isGrantEdit = true;
        showEditAndDeleteButton(mArticle.isGrantEdit);
    }

    @Override
    public void hideEditAndDeleteMenu() {
        mArticle.isGrantEdit = false;
        showEditAndDeleteButton(mArticle.isGrantEdit);
    }

    public void showEditAndDeleteButton(boolean isGrant) {
        if (isGrant) {
            mArticleEditButton.setVisibility(View.VISIBLE);
            mArticleDeleteButton.setVisibility(View.VISIBLE);
        } else {
            mArticleEditButton.setVisibility(View.INVISIBLE);
            mArticleDeleteButton.setVisibility(View.INVISIBLE);
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
//        ToastUtil.makeShortToast(mContext, "삭제에 실패했습니다.");
//    }
//
//    @Override
//    public void showSuccessDeleteComment() {
//        ToastUtil.makeShortToast(mContext, "삭제되었습니다.");
//    }
//
//    @Override
//    public void showErrorAdjustComment() {
//        ToastUtil.makeShortToast(mContext, "수정에 실패했습니다.");
//    }
//
//    @Override
    public void showSuccessAdjustComment() {
        ToastUtil.makeShortToast(mContext, "수정되었습니다.");
    }

    @Override
    public void showErrorDeleteContent() {
        ToastUtil.makeShortToast(mContext, "삭제에 실패했습니다.");
    }

    @Override
    public void showSuccessDeleteContent() {
        ToastUtil.makeShortToast(mContext, "삭제되었습니다.");
        setResult(RES_CODE_ARTICLE_DELETED);
        finish();
    }

    @Override
    public void showErrorAdjustGrantedContent() {
        ToastUtil.makeShortToast(mContext, "비밀번호가 틀렸습니다.");
    }

    @Override
    public void showSuccessAdjustGrantedContent() {
        String password = mArticlePasswordEdittext.getText().toString();
        Intent intent = new Intent(this, ArticleEditActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", mArticle.articleUid);
        intent.putExtra("BOARD_UID", mArticle.boardUid);
        intent.putExtra("ARTICLE_TITLE", mArticle.title);
        intent.putExtra("ARTICLE_CONTENT", mArticle.content);
        intent.putExtra("NICKNAME", mArticle.authorNickname);
        intent.putExtra("PASSWORD", password);
        startActivity(intent);
    }

    @Override
    public void showErrorGrantedDeleteContent() {
        ToastUtil.makeShortToast(mContext, "게시물을 수정하거나 삭제할 권한이 없습니다.");
    }

    @Override
    public void showSuccessGrantedDeleteContent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String password = mArticlePasswordEdittext.getText().toString();
        builder.setTitle("알림")
                .setMessage("게시글을 삭제할까요?\n댓글도 모두 사라집니다.")
                .setCancelable(false)
                .setPositiveButton("확인", (dialog, whichButton) -> {
                    mArticlePresenter.deleteAnoymousArticle(mArticle.articleUid, password);
                })
                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

//    @Override
//    public void showErrorGrantedDeleteComment() {
//        ToastUtil.makeShortToast(mContext, "비밀번호가 틀렸습니다.");
//    }
//
//    @Override
//    public void showSuccessGrantedDeleteComment() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("알림")
//                .setMessage("정말로 삭제하시겠습니까?")
//                .setCancelable(false)
//                .setPositiveButton("확인", (dialog, whichButton) -> {
//                    mArticlePresenter.deleteAnonymousComment(mArticle.articleUid, mCommentUId, mCommentPassword);
//                })
//                .setNegativeButton("취소", (dialog, whichButton) -> dialog.cancel());
//        AlertDialog dialog = builder.create();    // 알림창 객체 생성
//        dialog.show();    // 알림창 띄우기
//    }
//
//    @Override
//    public void showErrorGrantedAdjustComment() {
//        ToastUtil.makeShortToast(mContext, "비밀번호가 틀렸습니다.");
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
//                            mArticlePresenter.updateAnonymousComment(mArticle.articleUid, new Comment(mComment.commentUid, mComment.content, mCommentPassword));
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
        if (mArticle.boardUid != ID_ANONYMOUS) {
            AuthorizeConstant authorize = getAuthorize();
            if (authorize == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (authorize == AuthorizeConstant.MEMBER && DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
                showNickNameRequestDialog();
                return;
            }
        }
        Intent intent = new Intent(mContext, ArticleEditActivity.class);
        intent.putExtra("BOARD_UID", mArticle.boardUid);
        startActivityForResult(intent, REQ_CODE_ARTICLE_EDIT);

    }

    @OnClick(R.id.article_comment_write_button)
    public void onClickedCommentWriteButton() {
        Intent intent = new Intent(this, ArticleCommentActivity.class);
        intent.putExtra("IS_EDIT", true);
        intent.putExtra("ARTICLE_UID", mArticle.articleUid);
        intent.putExtra("BOARD_UID", mArticle.boardUid);
        intent.putExtra("ARTICLE_TITLE", mArticle.title);
        intent.putExtra("ARTICLE_CONTENT", mArticle.content);
        intent.putExtra("NICKNAME", mArticle.authorNickname);
        startActivity(intent);
    }


//    public void createCommentCreateDialog() {
//        AskNicknamePasswordDialog dialog = new AskNicknamePasswordDialog(this, "알림", "닉네임과 비밀번호를 입력해주세요", AskNicknamePasswordDialog.YES_NICKNAME);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        dialog.show();
//        dialog.setOnDismissListener(dialog1 -> {
//            if (!FormValidatorUtil.validateStringIsEmpty(mEditTextComment.getText().toString())) {
//                mArticlePresenter.createAnonymousComment(mArticle.articleUid, mEditTextComment.getText().toString(), dialog.getNickName(), dialog.getPassword());
//                mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
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
//            mArticlePresenter.checkAnonymousCommentDeleteGranted(comment.commentUid, dialog.getPassword());
//            mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
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
//            mArticlePresenter.checkAnonymousCommentAdjustGranted(comment.commentUid, dialog.getPassword());
//            mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
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
//            mArticlePresenter.checkAnonymousDeleteGranted(mArticle.articleUid, mContentPassword);
//            mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
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
//            mArticlePresenter.checkAnonymousAdjustGranted(mArticle.articleUid, mContentPassword);
//            mInputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), 0);
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