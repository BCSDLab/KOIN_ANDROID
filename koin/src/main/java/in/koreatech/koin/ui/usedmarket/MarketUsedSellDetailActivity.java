package in.koreatech.koin.ui.usedmarket;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedDetailCommentAdapter;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailPresenter;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;
import in.koreatech.koin.ui.userinfo.UserInfoEditedActivity;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;

public class MarketUsedSellDetailActivity extends KoinNavigationDrawerActivity implements MarketUsedDetailContract.View, MarketUsedDetailCommentAdapter.OnCommentRemoveButtonClickListener {
    private final String TAG = "MarketUsedSellDetailActivity";
    private static final int REQUEST_PHONE_CALL = 1;
    private Context context;
    private Item item; //품목 정보 저장
    private ArrayList<Comment> commentArrayList;
    private MarketUsedDetailPresenter marketDetailPresenter;

    private String prevCommentContent;
    private InputMethodManager inputMethodManager;
    private int mMarketID;

    @BindView(R.id.market_used_sell_nestedscrollview)
    NestedScrollView mMarketUsedSellNestedscrollview;
    @BindView(R.id.market_used_sell_detail_thumbnail_imageview)
    ImageView mMarketThumbnailImageview;
    @BindView(R.id.market_used_sell_detail_title_textview)
    TextView mMarketTitleTextview;
    @BindView(R.id.market_used_sell_detail_money_textview)
    TextView mMarketMoneyTextView;
    @BindView(R.id.market_used_sell_detail_nickname_textview)
    TextView mMarketNickNameTextView;
    @BindView(R.id.market_used_sell_detail_time_textview)
    TextView mMarketTimeTextView;
    @BindView(R.id.market_used_sell_detail_phone_textview)
    TextView mMarketPhoneTextView;
    @BindView(R.id.market_used_sell_comment_button)
    Button mMarketCommentButton;
    @BindView(R.id.market_used_sell_edit_button)
    Button mMarketEditButton;
    @BindView(R.id.market_used_sell_delete_button)
    Button mMarketDeleteButton;
    @BindView(R.id.market_used_sell_detail_content)
    TextView mMarketConetentTextView;
    @BindView(R.id.market_used_sell_detail_hit_count_textview)
    TextView mMarkethitCountTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_used_sell_activity_detail);
        ButterKnife.bind(this);
        this.context = this;
        this.item = new Item();
        mMarketID = 0;
        this.item.setId(getIntent().getIntExtra("ITEM_ID", 1));
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        checkRequiredInfo();
        this.marketDetailPresenter.checkGranted(this.item.getId());
        this.marketDetailPresenter.readMarketDetail(this.item.getId());


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
    public void onMarketDataReceived(Item item) {
        StringBuilder title = new StringBuilder();
        String styledText;
        this.item = item;
        this.commentArrayList.clear();
        this.commentArrayList.addAll(item.getComments());


        mMarketMoneyTextView.setText(changeMoneyFormat(Integer.toString(this.item.getPrice())) + "원");
        mMarketTimeTextView.setText(item.getCreatedAt());
        mMarketNickNameTextView.setText(item.getNickname());
        if (!item.isPhoneOpen() || item.getPhone() == null) {
            mMarketPhoneTextView.setText("비공개");
        } else
            mMarketPhoneTextView.setText(item.getPhone());


        Glide.with(this.context).load(item.getThumbnail()).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(mMarketThumbnailImageview);

        if (item.getState() == 0) {

        } else if (item.getState() == 1) {
            title.append("(판매중지)");
        } else {
            title.append("(판매완료)");
        }

        title.append(item.getTitle());
        if (item.getComments() != null && item.getComments().size() > 0) {
            styledText = title.toString() + "<font color='#175c8e'>(" + item.getComments().size() + ")</font>";
            mMarketTitleTextview.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE); // Title set
        } else
            mMarketTitleTextview.setText(title.toString());
        mMarkethitCountTextView.setText(item.getHit());
        if (item.getComments() != null && item.getComments().size() > 0) {
            styledText = "댓글 <font color='#175c8e'>" + item.getComments().size() + "</font>";
            mMarketCommentButton.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        }
//        mMarketCommentCountTextView.setText(Integer.toString(item.comments.size()));

        if (item.getContent() != null) {
            Spanned spannedValue = Html.fromHtml(item.getContent(), getImageHTML(), null);
            mMarketConetentTextView.setText(spannedValue);
        }

//        mMarketDetailCommentRecyclerAdapter.notifyDataSetChanged();
    }


    public Html.ImageGetter getImageHTML() {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                DownloadImageTask task = new DownloadImageTask();
                try {
                    return task.execute(new String[]{source}).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return imageGetter;
    }

    @Override
    public void setPresenter(MarketUsedDetailPresenter presenter) {
        this.marketDetailPresenter = presenter;
    }

    void init() {
        setPresenter(new MarketUsedDetailPresenter(this, new MarketUsedRestInteractor()));
        this.commentArrayList = new ArrayList<>();
        this.marketDetailPresenter.readMarketDetail(this.item.getId());
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            createActivityMove();
    }

    public void createActivityMove() {
        Intent intent = new Intent(this, MarketUsedSellCreateActivity.class);
        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        }
        if (getUser().getUserId() != null)
            startActivity(intent);
        else {
            ToastUtil.getInstance().makeLong("닉네임이 필요합니다.");
            intent = new Intent(this, UserInfoEditedActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.market_used_sell_comment_button)
    public void onClickedCommentButton() {
        Intent intent = new Intent(this, MarketUsedDetailCommentActivity.class);
        intent.putExtra("MARKET_ID", mMarketID);
        intent.putExtra("ITEM_ID", this.item.getId());
        intent.putExtra("ITEM_STATE", this.item.getState());
        intent.putExtra("ITEM_TITLE", this.item.getTitle());
        intent.putExtra("ITEM_NICKNAME", this.item.getNickname());
        intent.putExtra("ITEM_CREATE", this.item.getCreatedAt());
        intent.putExtra("ITEM_HIT", this.item.getHit());
        intent.putExtra("ITEM_CONTENT", this.item.getContent());
        intent.putExtra("ITEM_PRICE", this.item.getPrice());
        intent.putExtra("ITEM_PHONE", this.item.getPhone());
        intent.putExtra("ITEM_PHONE_STATUS", this.item.isPhoneOpen());
        intent.putExtra("ITEM_THUMBNAIL_URL", this.item.getThumbnail());
        startActivity(intent);
    }

    @OnClick(R.id.market_used_sell_delete_button)
    public void onClickedDeleteButton() {
        onClickMarketUsedItemRemoveButton();
    }

    @OnClick(R.id.market_used_sell_edit_button)
    public void onEditButton() {
        Intent intent = new Intent(this, MarketUsedSellEditActivity.class);
        intent.putExtra("MARKET_ID", this.item.getType());
        intent.putExtra("ITEM_ID", this.item.getId());
        intent.putExtra("MARKET_STATE", this.item.getState());
        intent.putExtra("MARKET_TITLE", this.item.getTitle());
        intent.putExtra("MARKET_CONTENT", this.item.getContent());
        intent.putExtra("MARKET_PRICE", this.item.getPrice());
        intent.putExtra("MARKET_PHONE", this.item.getPhone());
        intent.putExtra("MARKET_PHONE_STATUS", this.item.isPhoneOpen());
        intent.putExtra("MARKET_THUMBNAIL_URL", this.item.getThumbnail());
        startActivity(intent);
    }


    public void onClickMarketUsedItemRemoveButton() {
        SnackbarUtil.makeLongSnackbarActionYes(mMarketUsedSellNestedscrollview, "삭제하시겠습니까??", () -> {
            this.marketDetailPresenter.deleteItem(this.item.getId());
        });
    }

    public String changeMoneyFormat(String money) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String changedStirng = "";

        if (!TextUtils.isEmpty(money) && !money.equals(changedStirng)) {
            changedStirng = decimalFormat.format(Double.parseDouble(money.replaceAll(",", "")));
        }
        return changedStirng;
    }


    @Override
    public void showMarketCommentUpdate() {
        onRestart();
    }


    @Override
    public void onClickCommentRemoveButton(Comment comment) {
//        SnackbarUtil.makeLongSnackbarActionYes(mMarketCommentRecyclerVIew, "댓글을 삭제할까요?", () -> {
//            if (comment.grantDelete) {
//                this.marketDetailPresenter.deleteComment(comment, this.item);
//            }
//        });
    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
//        this.prevCommentContent = comment.content;
//        makeEditCommentDialog(comment);
    }

    public void makeEditCommentDialog(Comment comment) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit, null, false);

        final EditText editTextContent = dialogView.findViewById(R.id.dialog_edittext_comment_modify);
        editTextContent.setText(comment.getContent());
        editTextContent.setSelection(comment.getContent().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.KAPDialog);
        builder.setView(dialogView);
        builder.setPositiveButton("수정",
                (dialog, which) -> {
                    if (!FormValidatorUtil.validateStringIsEmpty(editTextContent.getText().toString())) {
                        comment.setContent(editTextContent.getText().toString().trim());
                        if (this.prevCommentContent.compareTo(comment.getContent()) != 0) {
                            this.marketDetailPresenter.editComment(comment, this.item, comment.getContent());
                        }
                    }
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showMarketDataReceivedFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
        finish();
    }

    @Override
    public void showMarketCommentDelete() {
        onRestart();
    }

    @Override
    public void showMarketCommentEdit() {
        onRestart();
    }

    @Override
    public void showMarketItemDelete() {
        ToastUtil.getInstance().makeShort("삭제되었습니다.");
        finish();
    }

    @Override
    public void showMarketCommentUpdateFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
    }

    @Override
    public void showMarketCommentDeleteFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
    }

    @Override
    public void showMarketCommentEditFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
    }

    @Override
    public void showMarketItemDeleteFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
    }

    @Override
    public void showGrantCheck(boolean isGranted) {
        if (isGranted) {
            mMarketEditButton.setVisibility(View.VISIBLE);
            mMarketDeleteButton.setVisibility(View.VISIBLE);
        } else {
            mMarketEditButton.setVisibility(View.INVISIBLE);
            mMarketDeleteButton.setVisibility(View.INVISIBLE);
        }
    }

//    public void checkRequiredInfo() {
//        String nickName;
//        AuthorizeConstant authorize = DefaultSharedPreferencesHelper.getInstance().checkAuthorize();
//        if (authorize == AuthorizeConstant.ANONYMOUS) {
//            mMarketCommentInputEditText.setFocusable(false);
//            mMarketCreateCommentButton.setEnabled(false);
//            mMarketCommentInputEditText.setText("로그인을 하셔야 입력할 수 있습니다.");
//            return;
//        } else {
//            nickName = DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName;
//        }
//
//        if (FormValidatorUtil.validateStringIsEmpty(nickName)) {
//            mMarketCommentInputEditText.setFocusable(false);
//            mMarketCreateCommentButton.setEnabled(false);
//            mMarketCommentInputEditText.setText("닉네임을 설정하지 않아 입력할 수 없습니다.");
//        } else {
//            mMarketCommentInputEditText.setFocusable(true);
//            mMarketCommentInputEditText.setFocusableInTouchMode(true);
//            mMarketCreateCommentButton.setEnabled(true);
//            mMarketCommentInputEditText.setText("");
//        }
//    }

//    @OnClick(R.id.market_used_sell_detail_comment_input_text)
//    public void onTouchMarketUsedCommentInputText() {
//        String nickName;
//        AuthorizeConstant authorize = DefaultSharedPreferencesHelper.getInstance().checkAuthorize();
//        if (authorize == AuthorizeConstant.ANONYMOUS) {
//            showLoginRequestDialog();
//            return;
//        } else {
//            nickName = DefaultSharedPreferencesHelper.getInstance().loadUser().userNickName;
//        }
//        if (FormValidatorUtil.validateStringIsEmpty(nickName)) {
//            showNickNameRequestDialog();
//        }
//    }

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

    public class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... urls) {
            for (String s : urls) {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL(s).openStream(), "src name");
                    DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                    int width = dm.widthPixels;
                    int height = dm.heightPixels;
                    Integer heigtbol = height / 3;
                    //TODO -> Change image size
                    drawable.setBounds(0, 0, drawable.getCurrent().getIntrinsicWidth(), drawable.getCurrent().getIntrinsicHeight());

                    return drawable;
                } catch (IOException exception) {
                    Log.v("IOException", exception.getMessage());
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
        }
    }
}

