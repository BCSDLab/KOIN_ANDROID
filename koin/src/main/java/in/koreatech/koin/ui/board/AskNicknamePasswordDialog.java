package in.koreatech.koin.ui.board;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.util.FilterUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import io.reactivex.annotations.NonNull;

public class AskNicknamePasswordDialog extends Dialog {

    @BindView(R.id.anonymous_article_dialog_title)
    TextView mAnonymousArticleDialogTitleTextview;
    @BindView(R.id.anonymous_article_dialog_content)
    TextView mAnonymousArticleDialogContentTextView;
    @BindView(R.id.anonymous_article_dialog_nickname_edittext)
    EditText mAnonymousArticleDialogNicknameEditText;
    @BindView(R.id.anonymous_article_dialog_password_edittext)
    TextView mAnonymousArticleDialogPasswordEditText;
    @BindView(R.id.anonymous_article_dialog_nickname_relativelayout)
    RelativeLayout mAnonymousArticleDialogNicknameRelativelayout;
    @BindView(R.id.anonymous_article_dialog_password_relativelayout)
    RelativeLayout mAnonymousArticleDialogPasswordRelativelayout;

    public static final int YES_NICKNAME = 0;
    public static final int NO_NICKNAME = 1;
    private View.OnClickListener onclickListener;
    private String mNickName;
    private String mPassword;
    private Context mContext;
    private int mType;
    private boolean isCancelled;

    public AskNicknamePasswordDialog(@NonNull Context context, String title, String content, int type) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.anonymous_article_dialog);
        ButterKnife.bind(this);
        mContext = context;
        mType = type;
        mAnonymousArticleDialogTitleTextview.setText(title);
        mAnonymousArticleDialogContentTextView.setText(content);
        mAnonymousArticleDialogPasswordEditText.setFilters(new InputFilter[]{new FilterUtil(FilterUtil.FILTER_PASSWORD), new InputFilter.LengthFilter(20)});
        mAnonymousArticleDialogNicknameEditText.setFilters(new InputFilter[]{new FilterUtil(FilterUtil.FILTER_E_N_H), new InputFilter.LengthFilter(20)});
        if (type == NO_NICKNAME)
            mAnonymousArticleDialogNicknameRelativelayout.setVisibility(View.GONE);
    }

    public void setOnclickListener(View.OnClickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    @OnClick(R.id.anonymous_article_dialog_confirm_textview)
    public void anonymousArticleDialogConfirmTextview() {
        if (mType != NO_NICKNAME) {
            if (mAnonymousArticleDialogNicknameEditText.getText().toString().isEmpty() || mAnonymousArticleDialogPasswordEditText.getText().toString().isEmpty()) {
                ToastUtil.getInstance().makeShortToast("닉네임 또는 비밀번호를 입력해주세요");
                return;
            } else {
                mNickName = mAnonymousArticleDialogNicknameEditText.getText().toString();
                mPassword = mAnonymousArticleDialogPasswordEditText.getText().toString();
                dismiss();
            }
        } else {
            if (mAnonymousArticleDialogPasswordEditText.toString().isEmpty()) {
                ToastUtil.getInstance().makeShortToast("비밀번호를 입력해주세요");
                return;
            } else {
                mPassword = mAnonymousArticleDialogPasswordEditText.getText().toString();
                dismiss();
            }
            isCancelled = false;
        }

    }

    @OnClick(R.id.anonymous_article_dialog_cancel_textview)
    public void anonymousArticleDialogCancelTextview() {
        isCancelled = true;
        dismiss();
    }

    public String getNickName() {
        return mNickName;
    }

    public String getPassword() {
        return mPassword;
    }

    public boolean isCancelled() {
        return isCancelled;
    }


}
