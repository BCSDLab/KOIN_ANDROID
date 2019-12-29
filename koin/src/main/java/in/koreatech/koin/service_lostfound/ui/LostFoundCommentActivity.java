package in.koreatech.koin.service_lostfound.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.asynctasks.GenerateProgressTask;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.core.helpers.DefaultSharedPreferencesHelper;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.entity.LostItem;
import in.koreatech.koin.core.util.SnackbarUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_lostfound.Contracts.LostFoundCommentContract;
import in.koreatech.koin.service_lostfound.adapter.LostFoundCommentRecyclerviewAdapter;
import in.koreatech.koin.service_lostfound.presenters.LostFoundCommentPresenter;


public class LostFoundCommentActivity extends KoinNavigationDrawerActivity implements LostFoundCommentContract.View, LostFoundCommentRecyclerviewAdapter.OnCommentRemoveButtonClickListener {

    @BindView(R.id.lostfound_comment_title)
    TextView lostfoundCommentTitleTextView;
    @BindView(R.id.lostfound_comment_view_count_korean)
    TextView lostfoundCommentViewCountKoreanTextView;
    @BindView(R.id.lostfound_comment_view_count)
    TextView lostfoundCommentViewCountTextView;
    @BindView(R.id.lostfound_comment_writer)
    TextView lostfoundCommentWriterTextView;
    @BindView(R.id.lostfound_comment_create_date)
    TextView lostfoundCommentCreateDateTextView;
    @BindView(R.id.lostfound_comment_content_recyclerview)
    RecyclerView lostfoundCommentContentRecyclerview;
    @BindView(R.id.lostfound_comment_nickname_textview)
    TextView lostfoundCommentNicknameTextview;
    @BindView(R.id.lostfound_comment_content_edittext)
    EditText lostfoundCommentContentEdittext;
    @BindView(R.id.lostfound_comment_cancel_button)
    Button lostfoundCommentCancelButton;
    @BindView(R.id.lostfound_comment_register_button)
    Button lostfoundCommentRegisterButton;

    private LostFoundCommentContract.Presenter lostFoundCommentPresenter;
    private GenerateProgressTask generateProgressTask;
    private LostFoundCommentRecyclerviewAdapter commentRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Comment> commentArrayList;
    private int id;
    private int editCommentId;
    private Context context;
    private boolean isEditComment;
    private boolean isEditPossible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostfound_comment_activity);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("ID", -1);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String nickname = getNickname();
        AuthorizeConstant authorizeConstant = getAuthority();
        if (nickname.isEmpty() || authorizeConstant == AuthorizeConstant.ANONYMOUS) {
            lostfoundCommentContentEdittext.setFocusable(false);
            lostfoundCommentContentEdittext.setClickable(false);
            isEditPossible = false;
        } else {
            lostfoundCommentNicknameTextview.setText(nickname);
            lostfoundCommentContentEdittext.setFocusableInTouchMode(true);
            isEditPossible = true;
        }
        if (lostFoundCommentPresenter == null)
            new LostFoundCommentPresenter(this);
        if (id != -1)
            lostFoundCommentPresenter.getLostItem(id);
    }


    public void init() {
        isEditComment = false;
        isEditPossible = true;
        layoutManager = new LinearLayoutManager(this);
        commentArrayList = new ArrayList<>();
        initRecyclerView();
    }

    public void initRecyclerView() {
        commentRecyclerAdapter = new LostFoundCommentRecyclerviewAdapter(this, commentArrayList);
        commentRecyclerAdapter.setCustomOnClickListener(this);
        lostfoundCommentContentRecyclerview.setHasFixedSize(true);
        lostfoundCommentContentRecyclerview.setLayoutManager(layoutManager);
        lostfoundCommentContentRecyclerview.setAdapter(commentRecyclerAdapter);
        lostfoundCommentContentRecyclerview.setNestedScrollingEnabled(false);
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId())
            onClickCreateButton();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onClickCreateButton() {
        AuthorizeConstant authorize = getAuthority();
        if (authorize == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        } else if (authorize == AuthorizeConstant.MEMBER && DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
            showNickNameRequestDialog();
            return;
        }
        Intent intent = new Intent(this, LostFoundEditActivity.class);
        intent.putExtra("MODE", LostFoundEditActivity.CREATE_MODE);
        startActivity(intent);
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
    public void showSuccessLostItem(LostItem lostItem) {
        String commentCount = Integer.toString(lostItem.getCommentCount());
        String titleText = getColorSpannedString(lostItem.title, "#252525") + getColorSpannedString("(" + commentCount + ")", "#175c8e");
        lostfoundCommentContentEdittext.getText().clear();
        lostfoundCommentTitleTextView.setText(Html.fromHtml(titleText));
        lostfoundCommentViewCountTextView.setText(Integer.toString(lostItem.getHit()));
        lostfoundCommentWriterTextView.setText(checkNullString(lostItem.getNickname()));
        lostfoundCommentCreateDateTextView.setText(checkNullString(lostItem.getDate()));
        updateCommentRecyclerView(lostItem.comments);
        isEditComment = false;
    }

    /**
     * Spanned text를 반환한다.
     *
     * @param text      변환하고 싶은 text
     * @param colorCode 색 code
     * @return spanned 된 text를 반환
     */
    public String getColorSpannedString(String text, String colorCode) {
        return "<font color=" + colorCode + ">" + text + "</font>";
    }

    /**
     * 값이 없는 경우 "-" 문자로 바꿔서 반환한다.
     *
     * @param string 검사하고 싶은 String
     * @return 검사후 결과 값 반환
     */
    public String checkNullString(String string) {
        if (string == null) {
            return "-";
        } else {
            return string;
        }
    }

    public void updateCommentRecyclerView(ArrayList<Comment> comments) {
        if (comments == null) return;
        commentArrayList.clear();
        commentArrayList.addAll(comments);
        commentRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSuccessUpdateComment(Comment comment) {
        isEditComment = false;
        lostfoundCommentContentEdittext.getText().clear();
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.makeShortToast(this, message);
    }

    @Override
    public void setPresenter(LostFoundCommentContract.Presenter presenter) {
        this.lostFoundCommentPresenter = presenter;
    }

    public String getNickname() {
        String nickname = "";
        try {
            nickname = DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName;
        } catch (NullPointerException e) {
            DefaultSharedPreferencesHelper.getInstance().init(getApplicationContext());
            if (DefaultSharedPreferencesHelper.getInstance().loadUser() != null)
                nickname = DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName;
        }
        if (nickname == null) nickname = "";
        return nickname;
    }

    @OnClick({R.id.lostfound_comment_content_linearlayout, R.id.lostfound_comment_content_edittext, R.id.lostfound_comment_cancel_button, R.id.lostfound_comment_register_button})
    public void onClicked(View view) {
        String nickname = getNickname();
        AuthorizeConstant authorizeConstant = getAuthority();
        if (!isEditPossible) {
            if (authorizeConstant == AuthorizeConstant.ANONYMOUS) {
                showLoginRequestDialog();
                return;
            } else if (nickname.isEmpty()) {
                showNickNameRequestDialog();
                return;
            }
        }

        switch (view.getId()) {
            case R.id.lostfound_comment_content_linearlayout:
                lostfoundCommentContentEdittext.setFocusableInTouchMode(true);
                lostfoundCommentContentEdittext.requestFocus();
                break;
            case R.id.lostfound_comment_content_edittext:
                break;
            case R.id.lostfound_comment_cancel_button:
                onClickedCancelButton();
                break;
            case R.id.lostfound_comment_register_button:
                onClickedCommentRegisterButton();
                break;
        }

    }


    public void onClickedCancelButton() {
        lostfoundCommentContentEdittext.setText("");
        isEditComment = false;
    }

    public void onClickedCommentRegisterButton() {
        String commentContent = lostfoundCommentContentEdittext.getText().toString();
        if (commentContent.isEmpty()) {
            ToastUtil.makeShortToast(this, "내용을 입력해주세요.");
            return;
        }
        if (!isEditComment) {
            lostFoundCommentPresenter.createComment(id, commentContent);
        } else {
            lostFoundCommentPresenter.updateComment(id, editCommentId, commentContent);

        }

    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
        SnackbarUtil.makeLongSnackbarActionYes(lostfoundCommentContentRecyclerview, "댓글을 삭제할까요?", () -> {
            if (comment.grantDelete) {
                lostFoundCommentPresenter.deleteComment(id, comment.commentUid);
            }
        });
    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
        isEditComment = true;
        lostfoundCommentContentEdittext.setText(comment.content);
        editCommentId = comment.commentUid;
    }
}
