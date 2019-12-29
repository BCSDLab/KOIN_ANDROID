package in.koreatech.koin.service_used_market.ui;

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
import in.koreatech.koin.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.core.constants.AuthorizeConstant;
import in.koreatech.koin.service_used_market.contracts.MarketUsedDetailContract;
import in.koreatech.koin.core.networks.entity.Comment;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.interactors.MarketUsedRestInteractor;
import in.koreatech.koin.core.util.FormValidatorUtil;
import in.koreatech.koin.core.util.SnackbarUtil;
import in.koreatech.koin.core.util.ToastUtil;
import in.koreatech.koin.service_used_market.adapters.MarketUsedDetailCommentAdapter;
import in.koreatech.koin.service_used_market.presenters.MarketUsedDetailPresenter;
import in.koreatech.koin.ui.LoginActivity;
import in.koreatech.koin.ui.UserInfoActivity;
import in.koreatech.koin.ui.UserInfoEditedActivity;


/**
 * @author yunjae na
 * @since 2018.09.16
 */
public class MarketUsedSellDetailActivity extends KoinNavigationDrawerActivity implements MarketUsedDetailContract.View, MarketUsedDetailCommentAdapter.OnCommentRemoveButtonClickListener {
    private final String TAG = MarketUsedSellDetailActivity.class.getSimpleName();
    private static final int REQUEST_PHONE_CALL = 1;
    private static CustomProgressDialog mGenerateProgress;


    private Context mContext;

    private MarketUsedDetailCommentAdapter mMarketDetailCommentRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager

    private Item mItem; //품목 정보 저장
    private boolean mGrantedCheck; // 글쓴이 인지 확인
    private ArrayList<Comment> mCommentArrayList;
    private MarketUsedDetailContract.Presenter mMarketDetailPresenter;

    private String mPrevCommentContent;
    private InputMethodManager mInputMethodManager;
    private int mMarketID;

    //    @BindView(R.id.market_used_sell_detail_scrollview)
//    ScrollView mScrollView;
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
    //    @BindView(R.id.market_used_sell_detail_is_selling_textview)
//    TextView mMarketIsSellingTextView;
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

    private CustomProgressDialog customProgressDialog;
//    @BindView(R.id.market_used_sell_detail_comment_count_textview)
//    TextView mMarketCommentCountTextView;
//
//    @BindView(R.id.market_used_sell_detail_comment_recyclerview)
//    RecyclerView mMarketCommentRecyclerVIew;
//    @BindView(R.id.market_used_sell_detail_comment_input_text)
//    EditText mMarketCommentInputEditText;
//
//
//    @BindView(R.id.market_used_sell_detail_create_comment_button)
//    Button mMarketCreateCommentButton;
//    @BindView(R.id.market_used_sell_detail_call_button)
//    Button mMarketCallButton;
//    @BindView(R.id.market_used_sell_detail_message_button)
//    Button mMarketMessageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_used_sell_activity_detail);
        ButterKnife.bind(this);
        this.mContext = this;
        mItem = new Item();
        mMarketID = 0;
        mItem.id = getIntent().getIntExtra("ITEM_ID", 1);
        mGrantedCheck = getIntent().getBooleanExtra("GRANT_CHECK", false);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        checkRequiredInfo();


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
        mMarketDetailPresenter.readMarketDetail(mItem.id);
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
    public void onMarketDataReceived(Item item) {
        StringBuilder title = new StringBuilder();
        String styledText;
        mItem = item;
        mCommentArrayList.clear();
        mCommentArrayList.addAll(item.comments);


        mMarketMoneyTextView.setText(changeMoneyFormat(Integer.toString(mItem.price)) + "원");
        mMarketTimeTextView.setText(item.createdAt);
        mMarketNickNameTextView.setText(item.nickname);
        if (!item.isPhoneOpen || item.phone == null) {
            mMarketPhoneTextView.setText("비공개");
//            mMarketCallButton.setVisibility(View.GONE);
//            mMarketMessageButton.setVisibility(View.GONE);
        } else
            mMarketPhoneTextView.setText(item.phone);


        Glide.with(mContext).load(item.thumbnail).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(mMarketThumbnailImageview);

        if (item.state == 0) {

        } else if (item.state == 1) {
            title.append("(판매중지)");
        } else {
            title.append("(판매완료)");
        }

        title.append(item.title);
        if (item.comments != null && item.comments.size() > 0) {
            styledText = title.toString() + "<font color='#175c8e'>(" + item.comments.size() + ")</font>";
            mMarketTitleTextview.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE); // Title set
        } else
            mMarketTitleTextview.setText(title.toString());
        mMarkethitCountTextView.setText(item.hit);
        if (item.comments != null && item.comments.size() > 0) {
            styledText = "댓글 <font color='#175c8e'>" + item.comments.size() + "</font>";
            mMarketCommentButton.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        }
//        mMarketCommentCountTextView.setText(Integer.toString(item.comments.size()));

        if (item.content != null) {
            Spanned spannedValue = Html.fromHtml(item.content, getImageHTML(), null);
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
    public void setPresenter(MarketUsedDetailContract.Presenter presenter) {
        this.mMarketDetailPresenter = presenter;
    }

    void init() {
        setPresenter(new MarketUsedDetailPresenter(this, new MarketUsedRestInteractor()));
        mCommentArrayList = new ArrayList<>();
        mMarketDetailPresenter.readMarketDetail(mItem.id);

        if (mGrantedCheck) {
            mMarketEditButton.setVisibility(View.VISIBLE);
            mMarketDeleteButton.setVisibility(View.VISIBLE);
        } else {
            mMarketEditButton.setVisibility(View.INVISIBLE);
            mMarketDeleteButton.setVisibility(View.INVISIBLE);
        }


//        mMarketDetailCommentRecyclerAdapter = new MarketUsedDetailCommentAdapter(this, mCommentArrayList);
//        mLayoutManager = new LinearLayoutManager(this);
//        mMarketCommentRecyclerVIew.setHasFixedSize(true);
//        mMarketCommentRecyclerVIew.setLayoutManager(mLayoutManager); //layout 설정
//        mMarketCommentRecyclerVIew.setAdapter(mMarketDetailCommentRecyclerAdapter); //adapter 설정
//
//        mMarketDetailCommentRecyclerAdapter.setCustomOnClickListener(this);
//        checkRequiredInfo();
    }


//    @OnClick(R.id.market_used_sell_detail_call_button)
//    void onCallButtonClick() {
//        Intent phoneIntent = new Intent(Intent.ACTION_VIEW);
//        phoneIntent.setData(Uri.parse("tel:" + mItem.phone));
//        startActivity(phoneIntent);
//
//    }
//
//    @OnClick(R.id.market_used_sell_detail_message_button)
//    void onMessageButtonClick() {
//        Intent messageIntent = new Intent(Intent.ACTION_VIEW);
//        messageIntent.setData(Uri.parse("sms:" + mItem.phone));
//        startActivity(messageIntent);
//    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            onBackPressed();
        else if (id == KoinBaseAppbarDark.getRightButtonId())
            createActivityMove();
    }

    public void createActivityMove() {
        Intent intent = new Intent(this, MarketUsedSellCreateActivity.class);
        if (getAuthority() == AuthorizeConstant.ANONYMOUS) {
            showLoginRequestDialog();
            return;
        }
        if (getUser().userNickName != null)
            startActivity(intent);
        else {
            ToastUtil.getInstance().makeShortToast("닉네임이 필요합니다.");
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
        intent.putExtra("ITEM_ID", mItem.id);
        intent.putExtra("ITEM_STATE", mItem.state);
        intent.putExtra("ITEM_TITLE", mItem.title);
        intent.putExtra("ITEM_NICKNAME", mItem.nickname);
        intent.putExtra("ITEM_CREATE", mItem.createdAt);
        intent.putExtra("ITEM_HIT", mItem.hit);
        intent.putExtra("ITEM_CONTENT", mItem.content);
        intent.putExtra("ITEM_PRICE", mItem.price);
        intent.putExtra("ITEM_PHONE", mItem.phone);
        intent.putExtra("ITEM_PHONE_STATUS", mItem.isPhoneOpen);
        intent.putExtra("ITEM_THUMBNAIL_URL", mItem.thumbnail);
        startActivity(intent);
    }

    @OnClick(R.id.market_used_sell_delete_button)
    public void onClickedDeleteButton() {
        onClickMarketUsedItemRemoveButton();
    }

    @OnClick(R.id.market_used_sell_edit_button)
    public void onEditButton() {
        Intent intent = new Intent(this, MarketUsedSellEditActivity.class);
        intent.putExtra("MARKET_ID", mItem.type);
        intent.putExtra("ITEM_ID", mItem.id);
        intent.putExtra("MARKET_STATE", mItem.state);
        intent.putExtra("MARKET_TITLE", mItem.title);
        intent.putExtra("MARKET_CONTENT", mItem.content);
        intent.putExtra("MARKET_PRICE", mItem.price);
        intent.putExtra("MARKET_PHONE", mItem.phone);
        intent.putExtra("MARKET_PHONE_STATUS", mItem.isPhoneOpen);
        intent.putExtra("MARKET_THUMBNAIL_URL", mItem.thumbnail);
        startActivity(intent);
    }


    public void onClickMarketUsedItemRemoveButton() {
        SnackbarUtil.makeLongSnackbarActionYes(mMarketUsedSellNestedscrollview, "삭제하시겠습니까??", () -> {
            mMarketDetailPresenter.deleteItem(mItem.id);
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
//        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mInputMethodManager.hideSoftInputFromWindow(mMarketCommentInputEditText.getWindowToken(), 0);
        onRestart();
    }

//    @OnClick(R.id.market_used_sell_detail_create_comment_button)
//    void onCreateCommentButton() {
//        String comment = mMarketCommentInputEditText.getText().toString().trim();
//        if (!comment.isEmpty())
//            mMarketDetailPresenter.createComment(mItem.id, mMarketCommentInputEditText.getText().toString());
//        else
//            ToastUtil.makeShortToast(mContext, "댓글을 입력해주세요.");
//
//    }

    @Override
    public void onClickCommentRemoveButton(Comment comment) {
//        SnackbarUtil.makeLongSnackbarActionYes(mMarketCommentRecyclerVIew, "댓글을 삭제할까요?", () -> {
//            if (comment.grantDelete) {
//                mMarketDetailPresenter.deleteComment(comment, mItem);
//            }
//        });
    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {
//        mPrevCommentContent = comment.content;
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
                        if (mPrevCommentContent.compareTo(comment.content) != 0) {
                            mMarketDetailPresenter.editComment(comment, mItem, comment.content);
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
        ToastUtil.getInstance().makeShortToast(R.string.server_failed);
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
        ToastUtil.getInstance().makeShortToast("삭제되었습니다.");
        finish();
    }

    @Override
    public void showMarketCommentUpdateFail() {
        ToastUtil.getInstance().makeShortToast(R.string.server_failed);
    }

    @Override
    public void showMarketCommentDeleteFail() {
        ToastUtil.getInstance().makeShortToast(R.string.server_failed);
    }

    @Override
    public void showMarketCommentEditFail() {
        ToastUtil.getInstance().makeShortToast(R.string.server_failed);
    }

    @Override
    public void showMarketItemDeleteFail() {
        ToastUtil.getInstance().makeShortToast(R.string.server_failed);
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

//    @OnClick(R.id.market_used_sell_detail_comment_input_text)
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

