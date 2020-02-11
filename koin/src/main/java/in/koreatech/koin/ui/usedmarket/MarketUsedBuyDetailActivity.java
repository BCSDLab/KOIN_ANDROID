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
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.constant.AuthorizeConstant;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailContract;
import in.koreatech.koin.data.network.entity.Comment;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.util.FormValidatorUtil;
import in.koreatech.koin.util.SnackbarUtil;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedDetailCommentAdapter;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailPresenter;
import in.koreatech.koin.ui.login.LoginActivity;
import in.koreatech.koin.ui.userinfo.UserInfoActivity;
import in.koreatech.koin.ui.userinfo.UserInfoEditedActivity;

public class MarketUsedBuyDetailActivity extends KoinNavigationDrawerActivity implements MarketUsedDetailContract.View, MarketUsedDetailCommentAdapter.OnCommentRemoveButtonClickListener {
    private final String TAG = "MarketUsedBuyDetailActivity";
    private static final int REQUEST_PHONE_CALL = 1;

    private Context context;
    private MarketUsedDetailCommentAdapter narketDetailCommentRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager; // RecyclerView LayoutManager

    private Item item; //품목 정보 저장
    private boolean grantedCheck; // 글쓴이 인지 확인
    private ArrayList<Comment> commentArrayList;
    private MarketUsedDetailPresenter marketDetailPresenter;

    private String prevCommentContent;
    private InputMethodManager inputMethodManager;
    private int marketID;

    @BindView(R.id.market_used_buy_nestedscrollview)
    NestedScrollView marketUsedbuyNestedscrollview;
    @BindView(R.id.market_used_buy_detail_thumbnail_imageview)
    ImageView marketThumbnailImageview;
    @BindView(R.id.market_used_buy_detail_title_textview)
    TextView marketTitleTextview;
    @BindView(R.id.market_used_buy_detail_money_textview)
    TextView marketMoneyTextView;
    @BindView(R.id.market_used_buy_detail_nickname_textview)
    TextView marketNickNameTextView;
    @BindView(R.id.market_used_buy_detail_time_textview)
    TextView marketTimeTextView;
    @BindView(R.id.market_used_buy_detail_phone_textview)
    TextView marketPhoneTextView;
    @BindView(R.id.market_used_buy_comment_button)
    Button marketCommentButton;
    @BindView(R.id.market_used_buy_edit_button)
    Button marketEditButton;
    @BindView(R.id.market_used_buy_delete_button)
    Button marketDeleteButton;
    @BindView(R.id.market_used_buy_detail_content)
    TextView marketConetentTextView;
    @BindView(R.id.market_used_buy_detail_hit_count_textview)
    TextView markethitCountTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_used_buy_activity_detail);
        ButterKnife.bind(this);
        this.context = this;
        item = new Item();
        marketID = getIntent().getIntExtra("MARKET_ID", -1);
        item.id = getIntent().getIntExtra("ITEM_ID", -1);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        marketDetailPresenter.readMarketDetail(item.id);


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
    public void onMarketDataReceived(Item item) {
        StringBuilder title = new StringBuilder();
        String styledText;
        this.item = item;
        commentArrayList.clear();
        commentArrayList.addAll(item.comments);


        marketMoneyTextView.setText(changeMoneyFormat(Integer.toString(item.price)) + "원");
        marketTimeTextView.setText(item.createdAt);
        marketNickNameTextView.setText(item.nickname);
        if (!item.isPhoneOpen || item.phone == null) {
            marketPhoneTextView.setText("비공개");
//            mMarketCallButton.setVisibility(View.GONE);
//            mMarketMessageButton.setVisibility(View.GONE);
        } else
            marketPhoneTextView.setText(item.phone);


        Glide.with(context).load(item.thumbnail).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(marketThumbnailImageview);

        if (item.state == 0) {

        } else if (item.state == 1) {
            title.append("(구매중지)");
        } else {
            title.append("(구매완료)");
        }

        title.append(item.title);
        if (item.comments != null && item.comments.size() > 0) {
            styledText = title.toString() + "<font color='#175c8e'>(" + item.comments.size() + ")</font>";
            marketTitleTextview.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE); // Title set
        } else
            marketTitleTextview.setText(title.toString());
        markethitCountTextView.setText(item.hit);
        if (item.comments != null && item.comments.size() > 0) {
            styledText = "댓글 <font color='#175c8e'>" + item.comments.size() + "</font>";
            marketCommentButton.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        }
//        mMarketCommentCountTextView.setText(Integer.toString(item.comments.size()));

        if (item.content != null) {
            Spanned spannedValue = Html.fromHtml(item.content, getImageHTML(), null);
            marketConetentTextView.setText(spannedValue);
        }

//        narketDetailCommentRecyclerAdapter.notifyDataSetChanged();
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
        commentArrayList = new ArrayList<>();
        marketDetailPresenter.readMarketDetail(item.id);


//        narketDetailCommentRecyclerAdapter = new MarketUsedDetailCommentAdapter(this, commentArrayList);
//        layoutManager = new LinearLayoutManager(this);
//        mMarketCommentRecyclerVIew.setHasFixedSize(true);
//        mMarketCommentRecyclerVIew.setLayoutManager(layoutManager); //layout 설정
//        mMarketCommentRecyclerVIew.setAdapter(narketDetailCommentRecyclerAdapter); //adapter 설정
//
//        narketDetailCommentRecyclerAdapter.setCustomOnClickListener(this);
//        checkRequiredInfo();
    }


//    @OnClick(R.id.market_used_buy_detail_call_button)
//    void onCallButtonClick() {
//        Intent phoneIntent = new Intent(Intent.ACTION_VIEW);
//        phoneIntent.setData(Uri.parse("tel:" + item.phone));
//        startActivity(phoneIntent);
//
//    }
//
//    @OnClick(R.id.market_used_buy_detail_message_button)
//    void onMessageButtonClick() {
//        Intent messageIntent = new Intent(Intent.ACTION_VIEW);
//        messageIntent.setData(Uri.parse("sms:" + item.phone));
//        startActivity(messageIntent);
//    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            createActivityMove();
    }

    public void createActivityMove() {
        Intent intent = new Intent(this, MarketUsedBuyCreateActivity.class);
        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        }
        if (getUser().userNickName != null)
            startActivity(intent);
        else {
            ToastUtil.getInstance().makeShort("닉네임이 필요합니다.");
            intent = new Intent(this, UserInfoEditedActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.market_used_buy_comment_button)
    public void onClickedCommentButton() {
        Intent intent = new Intent(this, MarketUsedDetailCommentActivity.class);
        intent.putExtra("MARKET_ID", marketID);
        intent.putExtra("ITEM_ID", item.id);
        intent.putExtra("ITEM_STATE", item.state);
        intent.putExtra("ITEM_TITLE", item.title);
        intent.putExtra("ITEM_NICKNAME", item.nickname);
        intent.putExtra("ITEM_CREATE", item.createdAt);
        intent.putExtra("ITEM_HIT", item.hit);
        intent.putExtra("ITEM_CONTENT", item.content);
        intent.putExtra("ITEM_PRICE", item.price);
        intent.putExtra("ITEM_PHONE", item.phone);
        intent.putExtra("ITEM_PHONE_STATUS", item.isPhoneOpen);
        intent.putExtra("ITEM_THUMBNAIL_URL", item.thumbnail);
        startActivity(intent);
    }

    @OnClick(R.id.market_used_buy_delete_button)
    public void onClickedDeleteButton() {
        onClickMarketUsedItemRemoveButton();
    }

    @OnClick(R.id.market_used_buy_edit_button)
    public void onEditButton() {
        Intent intent = new Intent(this, MarketUsedBuyEditActivity.class);
        intent.putExtra("MARKET_ID", marketID);
        intent.putExtra("ITEM_ID", item.id);
        intent.putExtra("MARKET_STATE", item.state);
        intent.putExtra("MARKET_TITLE", item.title);
        intent.putExtra("MARKET_CONTENT", item.content);
        intent.putExtra("MARKET_PRICE", item.price);
        intent.putExtra("MARKET_PHONE", item.phone);
        intent.putExtra("MARKET_PHONE_STATUS", item.isPhoneOpen);
        intent.putExtra("MARKET_THUMBNAIL_URL", item.thumbnail);
        startActivity(intent);
    }


    public void onClickMarketUsedItemRemoveButton() {
        SnackbarUtil.makeLongSnackbarActionYes(marketUsedbuyNestedscrollview, "삭제하시겠습니까??", () -> {
            marketDetailPresenter.deleteItem(item.id);
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
//        mMarketCommentInputEditText.setText("");
//        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(mMarketCommentInputEditText.getWindowToken(), 0);
        onRestart();
    }

//    @OnClick(R.id.market_used_buy_detail_create_comment_button)
//    void onCreateCommentButton() {
//        String comment = mMarketCommentInputEditText.getText().toString().trim();
//        if (!comment.isEmpty())
//            marketDetailPresenter.createComment(item.id, mMarketCommentInputEditText.getText().toString());
//        else
//            ToastUtil.makeShort(context, "댓글을 입력해주세요.");
//
//    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
//        SnackbarUtil.makeLongSnackbarActionYes(mMarketCommentRecyclerVIew, "댓글을 삭제할까요?", () -> {
//            if (comment.grantDelete) {
//                marketDetailPresenter.deleteComment(comment, item);
//            }
//        });
    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
//        prevCommentContent = comment.content;
//        makeEditCommentDialog(comment);
    }

    public void makeEditCommentDialog(Comment comment) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit, null, false);

        final EditText editTextContent = dialogView.findViewById(R.id.dialog_edittext_comment_modify);
        editTextContent.setText(comment.content);
        editTextContent.setSelection(comment.content.length());

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.KAPDialog);
        builder.setView(dialogView);
        builder.setPositiveButton("수정",
                (dialog, which) -> {
                    if (!FormValidatorUtil.validateStringIsEmpty(editTextContent.getText().toString())) {
                        comment.content = editTextContent.getText().toString().trim();
                        if (prevCommentContent.compareTo(comment.content) != 0) {
                            marketDetailPresenter.editComment(comment, item, comment.content);
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
            marketEditButton.setVisibility(View.VISIBLE);
            marketDeleteButton.setVisibility(View.VISIBLE);
        } else {
            marketEditButton.setVisibility(View.INVISIBLE);
            marketDeleteButton.setVisibility(View.INVISIBLE);
        }
    }

//    public void checkRequiredInfo() {
//        String nickName;
//        AuthorizeConstant authorize = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
//        if (authorize == AuthorizeConstant.ANONYMOUS) {
//            mMarketCommentInputEditText.setFocusable(false);
//            mMarketCreateCommentButton.setEnabled(false);
//            mMarketCommentInputEditText.setText("로그인을 하셔야 입력할 수 있습니다.");
//            return;
//        } else {
//            nickName = UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName;
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

//    @OnClick(R.id.market_used_buy_detail_comment_input_text)
//    public void onTouchMarketUsedCommentInputText() {
//        String nickName;
//        AuthorizeConstant authorize = UserInfoSharedPreferencesHelper.getInstance().checkAuthorize();
//        if (authorize == AuthorizeConstant.ANONYMOUS) {
//            showLoginRequestDialog();
//            return;
//        } else {
//            nickName = UserInfoSharedPreferencesHelper.getInstance().loadUser().userNickName;
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

