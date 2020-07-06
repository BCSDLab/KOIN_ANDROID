package in.koreatech.koin.ui.usedmarket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.usedmarket.adapter.MarketUsedDetailCommentAdapter;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedDetailPresenter;
import in.koreatech.koin.util.AuthorizeManager;
import in.koreatech.koin.util.NavigationManger;
import in.koreatech.koin.util.SnackbarUtil;

public class MarketUsedBuyDetailFragment extends KoinBaseFragment implements MarketUsedDetailContract.View, MarketUsedDetailCommentAdapter.OnCommentRemoveButtonClickListener {
    private static final int REQUEST_PHONE_CALL = 1;
    private final String TAG = "MarketUsedBuyDetailFragment";
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_used_buy_fragment_detail, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        item = new Item();
        marketID = getArguments().getInt("MARKET_ID", -1);
        item.setId(getArguments().getInt("ITEM_ID", -1));
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        marketDetailPresenter.readMarketDetail(item.getId());
        this.marketDetailPresenter.checkGranted(this.item.getId());

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
    public void onMarketDataReceived(Item item) {
        StringBuilder title = new StringBuilder();
        String styledText;
        this.item = item;
        commentArrayList.clear();
        commentArrayList.addAll(item.getComments());


        marketMoneyTextView.setText(changeMoneyFormat(Integer.toString(item.getPrice())) + "원");
        marketTimeTextView.setText(item.getCreatedAt());
        marketNickNameTextView.setText(item.getNickname());
        if (!item.isPhoneOpen() || item.getPhone() == null) {
            marketPhoneTextView.setText("비공개");
        } else
            marketPhoneTextView.setText(item.getPhone());


        Glide.with(context).load(item.getThumbnail()).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(marketThumbnailImageview);

        if (item.getState() == 0) {

        } else if (item.getState() == 1) {
            title.append("(구매중지)");
        } else {
            title.append("(구매완료)");
        }

        title.append(item.getTitle());
        if (item.getComments() != null && item.getComments().size() > 0) {
            styledText = title.toString() + "<font color='#175c8e'>(" + item.getComments().size() + ")</font>";
            marketTitleTextview.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE); // Title set
        } else
            marketTitleTextview.setText(title.toString());
        markethitCountTextView.setText(item.getHit());
        if (item.getComments() != null && item.getComments().size() > 0) {
            styledText = "댓글 <font color='#175c8e'>" + item.getComments().size() + "</font>";
            marketCommentButton.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        }

        if (item.getContent() != null) {
            Spanned spannedValue = Html.fromHtml(item.getContent(), getImageHTML(), null);
            marketConetentTextView.setText(spannedValue);
        }
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
        marketDetailPresenter.readMarketDetail(item.getId());
    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            createFragmentMove();
    }

    public void createFragmentMove() {
        if (AuthorizeManager.getAuthorize(getContext()) == AuthorizeConstant.ANONYMOUS) {
            AuthorizeManager.showLoginRequestDialog(getActivity());
            return;
        }
        if (AuthorizeManager.getNickName(getContext()) != null)
            NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_buy_create_action, null, NavigationManger.getNavigationAnimation());
        else {
            AuthorizeManager.showNickNameRequestDialog(getActivity());
        }
    }

    @OnClick(R.id.market_used_buy_comment_button)
    public void onClickedCommentButton() {
        Bundle bundle = new Bundle();
        bundle.putInt("MARKET_ID", marketID);
        bundle.putInt("ITEM_ID", item.getId());
        bundle.putInt("ITEM_STATE", item.getState());
        bundle.putString("ITEM_NICKNAME", item.getNickname());
        bundle.putString("ITEM_CREATE", item.getCreatedAt());
        bundle.putString("ITEM_HIT", item.getHit());
        bundle.putString("ITEM_CONTENT", item.getContent());
        bundle.putInt("ITEM_PRICE", item.getPrice());
        bundle.putString("ITEM_PHONE", item.getPhone());
        bundle.putBoolean("ITEM_PHONE_STATUS", item.isPhoneOpen());
        bundle.putString("ITEM_THUMBNAIL_URL", item.getThumbnail());
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_comment_action, bundle, NavigationManger.getNavigationAnimation());
    }

    @OnClick(R.id.market_used_buy_delete_button)
    public void onClickedDeleteButton() {
        onClickMarketUsedItemRemoveButton();
    }

    @OnClick(R.id.market_used_buy_edit_button)
    public void onEditButton() {
        Bundle bundle = new Bundle();
        bundle.putInt("MARKET_ID", marketID);
        bundle.putInt("ITEM_ID", item.getId());
        bundle.putInt("MARKET_STATE", item.getState());
        bundle.putString("MARKET_TITLE", item.getTitle());
        bundle.putString("MARKET_CONTENT", item.getContent());
        bundle.putInt("MARKET_PRICE", item.getPrice());
        bundle.putString("MARKET_PHONE", item.getPhone());
        bundle.putBoolean("MARKET_PHONE_STATUS", item.isPhoneOpen());
        bundle.putString("MARKET_THUMBNAIL_URL", item.getThumbnail());
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_buy_edit_action, bundle, NavigationManger.getNavigationAnimation());
    }


    public void onClickMarketUsedItemRemoveButton() {
        SnackbarUtil.makeLongSnackbarActionYes(getView(), "삭제하시겠습니까??", () -> {
            marketDetailPresenter.deleteItem(item.getId());
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

    }


    @Override
    public void onClickCommentRemoveButton(Comment comment) {

    }

    @Override
    public void onClickCommentModifyButton(Comment comment) {

    }

    @Override
    public void showMarketDataReceivedFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
        onBackPressed();
    }

    @Override
    public void showMarketCommentDelete() {
    }

    @Override
    public void showMarketCommentEdit() {

    }

    @Override
    public void showMarketItemDelete() {
        ToastUtil.getInstance().makeShort("삭제되었습니다.");
        onBackPressed();
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

    public class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... urls) {
            for (String s : urls) {
                try {
                    Drawable drawable = Drawable.createFromStream(new URL(s).openStream(), "src name");
                    DisplayMetrics dm = getContext().getApplicationContext().getResources().getDisplayMetrics();
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

