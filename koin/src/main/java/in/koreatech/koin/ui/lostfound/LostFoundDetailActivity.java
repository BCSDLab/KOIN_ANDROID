package in.koreatech.koin.ui.lostfound;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.core.constant.AuthorizeConstant;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.data.network.entity.LostItem;
import in.koreatech.koin.util.LoadImageFromUrl;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundDetailContract;
import in.koreatech.koin.ui.lostfound.presenter.LostFoundDetailPresenter;

/**
 * @author yunjaeNa
 * @since 2019-09-06
 * <p>
 * 분실물 게시판 상세화면
 */
public class LostFoundDetailActivity extends KoinNavigationDrawerActivity implements LostFoundDetailContract.View, Html.ImageGetter {

    @BindView(R.id.lostfound_detail_nestedscrollview)
    NestedScrollView lostfoundDetailNestedScrollview;
    @BindView(R.id.lostfound_detail_title_textview)
    TextView lostfoundDetailTitleTextview;
    @BindView(R.id.lostfound_detail_hit_count_textview)
    TextView lostfoundDetailHitCountTextview;
    @BindView(R.id.lostfound_detail_nickname_textview)
    TextView lostfoundDetailNicknameTextview;
    @BindView(R.id.lostfound_detail_time_textview)
    TextView lostfoundDetailTimeTextview;
    @BindView(R.id.lostfound_detail_comment_button)
    Button lostfoundDetailCommentButton;
    @BindView(R.id.lostfound_detail_edit_button)
    Button lostfoundDetailEditButton;
    @BindView(R.id.lostfound_detail_delete_button)
    Button lostfoundDetailDeleteButton;
    @BindView(R.id.lostfound_detail_lost_date_contents_textview)
    TextView lostfoundDetailLostDateContentTextview;
    @BindView(R.id.lostfound_lost_place_contents_textview)
    TextView lostfoundDetailLostPlaceContentTextview;
    @BindView(R.id.lostfound_detail_phone_num_textview)
    TextView lostfoundDetailLostPhoneNumberTextview;
    @BindView(R.id.lostfound_detail_content)
    TextView lostfoundDetailContentTextview;
    @BindView(R.id.lostfound_detail_lost_place_textview)
    TextView lostfoundDetailLostPlaceTextview;
    @BindView(R.id.lostfound_detail_lost_date_textview)
    TextView lostfoundDetailLostDateTextview;

    private CustomProgressDialog customProgressDialog;
    private LostFoundDetailContract.Presenter lostfoundDetailPresenter;
    private LostItem lostItem;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostfound_activity_detail);
        ButterKnife.bind(this);
        id = getIntent().getIntExtra("ID", -1);
        init();
    }

    public void init() {
        new LostFoundDetailPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (id != -1)
            lostfoundDetailPresenter.getLostFoundDetailById(id);
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
    public void updateLostDetailData(LostItem lostItem) {
        this.lostItem = lostItem;
        String commentCount = Integer.toString(lostItem.getCommentCount());
        String titleText = getColorSpannedString(lostItem.title, "#252525") + getColorSpannedString("(" + commentCount + ")", "#175c8e");
        String commentButtonText = getColorSpannedString("댓글", "#252525") + getColorSpannedString(commentCount, "#175c8e");
        Spanned spanned = Html.fromHtml(lostItem.getContent(), this, null);
        lostfoundDetailTitleTextview.setText(Html.fromHtml(titleText));
        lostfoundDetailCommentButton.setText(Html.fromHtml(commentButtonText));
        lostfoundDetailHitCountTextview.setText(Integer.toString(lostItem.getHit()));
        lostfoundDetailNicknameTextview.setText(checkNullString(lostItem.getNickname()));
        lostfoundDetailLostDateContentTextview.setText(checkNullString(lostItem.getDate()));
        lostfoundDetailLostPlaceContentTextview.setText(checkNullString(lostItem.getLocation()));
        lostfoundDetailLostPhoneNumberTextview.setText(checkNullString(lostItem.getPhone()));
        lostfoundDetailContentTextview.setText(spanned);
        lostfoundDetailTimeTextview.setText(checkNullString(lostItem.getCreatedAt()));
        setTextviewWithLostItemType(lostItem.getType());

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
     * 아이템 type에 따라서 습득 장소인지 분실 장소인지 Textview 글을 바꿔준다.
     *
     * @param type 0: 분실 1 : 습득
     */
    public void setTextviewWithLostItemType(int type) {
        if (type == 0) {
            lostfoundDetailLostDateTextview.setText("습득일");
            lostfoundDetailLostPlaceTextview.setText("습득장소");
        } else {
            lostfoundDetailLostDateTextview.setText("분실일");
            lostfoundDetailLostPlaceTextview.setText("분실장소");
        }
    }

    @Override
    public void showMessage(String message) {
        if (message != null)
            ToastUtil.getInstance().makeShort(message);
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

    @Override
    public void showGranted(boolean isGrant) {
        if (isGrant) {
            lostfoundDetailEditButton.setVisibility(View.VISIBLE);
            lostfoundDetailDeleteButton.setVisibility(View.VISIBLE);
        } else {
            lostfoundDetailEditButton.setVisibility(View.INVISIBLE);
            lostfoundDetailDeleteButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showSuccessDeleted() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void setPresenter(LostFoundDetailContract.Presenter presenter) {
        this.lostfoundDetailPresenter = presenter;
    }

    @OnClick({R.id.lostfound_detail_comment_button, R.id.lostfound_detail_edit_button, R.id.lostfound_detail_delete_button})
    public void onClickedButton(View view) {
        switch (view.getId()) {
            case R.id.lostfound_detail_comment_button:
                Intent commentIntent = new Intent(this, LostFoundCommentActivity.class);
                commentIntent.putExtra("ID", id);
                startActivity(commentIntent);
                break;
            case R.id.lostfound_detail_edit_button:
                if (lostItem == null) return;
                Intent lostFoundEditIntent = new Intent(this, LostFoundEditActivity.class);
                lostFoundEditIntent.putExtra("LOST_ITEM", lostItem);
                lostFoundEditIntent.putExtra("MODE", LostFoundEditActivity.EDIT_MODE);
                startActivity(lostFoundEditIntent);
                break;
            case R.id.lostfound_detail_delete_button:
                SnackbarUtil.makeLongSnackbarActionYes(lostfoundDetailNestedScrollview, "글을 삭제할까요?", () -> {
                    lostfoundDetailPresenter.removeItem(id);
                });
                break;
        }
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppbarBase.getLeftButtonId()) {
            finish();
        } else if (id == AppbarBase.getRightButtonId()) {
            onClickCreateButton();
        }
    }

    public void onClickCreateButton() {
        AuthorizeConstant authorize = getAuthority();
        if (authorize == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        } else if (authorize == AuthorizeConstant.MEMBER && UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName == null) {
            showNickNameRequestDialog();
            return;
        }
        Intent intent = new Intent(this, LostFoundEditActivity.class);
        intent.putExtra("MODE", LostFoundEditActivity.CREATE_MODE);
        startActivity(intent);
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.image_no_image);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new LoadImageFromUrl(lostfoundDetailContentTextview, this).execute(source, d);
        return d;
    }
}
