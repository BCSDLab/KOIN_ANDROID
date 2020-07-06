package in.koreatech.koin.ui.usedmarket;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunjaena.imageloader.ImageFailedType;
import com.yunjaena.imageloader.ImageLoadListener;
import com.yunjaena.imageloader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.MarketItem;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditPresenter;
import in.koreatech.koin.util.AuthorizeManager;

/**
 * This class edit item at MarketUsedBuyEdit
 * if the item edit success activity will be finished and go back to MarketUsedBuyDetailFragment
 */
public class MarketUsedBuyEditFragment extends KoinBaseFragment implements MarketUsedEditContract.View , ImageLoadListener {
    private static final int MY_REQUEST_CODE = 100;
    private static final int REQUEST_GET_GALLERY = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_CROP_IMAGE = 3;
    private final String TAG = "MarketUsedBuyEditFragment";
    private final int MAXTITLELENGTH = 39;
    @BindView(R.id.market_used_buy_edit_thumbnail_imageview)
    ImageView mMarketBuyEditThumbnailImageView;
    @BindView(R.id.market_used_buy_edit_thumbnail_change_button)
    Button mMarketBuyEditThumbnailChangeButton;
    @BindView(R.id.market_used_buy_edit_title_textview)
    EditText mMarketBuyEditTitleEditText;
    @BindView(R.id.market_used_buy_edit_money_edittext)
    EditText mMarketBuyEditMoneyEditText;
    @BindView(R.id.market_used_buy_edit_phone_status_radiobutton_group)
    RadioGroup mMarketBuyEditPhoneStatusRadioButtonGroup;
    @BindView(R.id.market_used_buy_edit_is_phone_public_radiobutton)
    RadioButton mMarketBuyEditIsPhonePublicRadioButton;
    @BindView(R.id.market_used_buy_edit_is_phone_private_radiobutton)
    RadioButton mMarketBuyEditIsPhonePrivateRadioButton;
    @BindView(R.id.market_used_buy_edit_edittext_phone_num)
    EditText marketBuyEditEditTextPhoneNum;
    @BindView(R.id.market_used_buy_edit_buying_status_radiobutton_group)
    RadioGroup mMarketBuyEditBuyingStatusRadioButtonGroup;
    @BindView(R.id.market_used_buy_edit_is_buying_radiobutton)
    RadioButton mMarketBuyEditIsBuyingRadioButton;
    @BindView(R.id.market_used_buy_edit_is_stop_buying_radiobutton)
    RadioButton mMarketBuyEditIsStopBuyingRadioButton;
    @BindView(R.id.market_used_buy_edit_is_complete_buying_radiobutton)
    RadioButton mMarketBuyEditIsCompleteBuyingRadioButton;
    @BindView(R.id.market_used_buy_edit_content)
    EditText mMarketBuyEditContent;
    private Context context;
    private int marketId;
    private int itemId;
    private String title;
    private String content;
    private String price;
    private String phoneNumber;
    private boolean isPhoneOpen;
    private int itemState;
    private String thumbNail;
    private ArrayList<String> imageUrl;
    private MarketItem marketItem;
    private MarketUsedEditPresenter marketUsedEditPresenter;
    private Uri currentPhotoPath;
    private File mImageFile;
    private CustomProgressDialog customProgressDialog;
    private boolean mIsTitlecheck;
    private boolean mIsPhoneCheck;
    private boolean mIsContentCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_used_sell_edit_fragment, container, false);
        ButterKnife.bind(this, view);
        this.context = getContext();
        marketId = getArguments().getInt("MARKET_ID", -1);
        itemId = getArguments().getInt("ITEM_ID", -1);
        title = getArguments().getString("MARKET_TITLE");
        content = getArguments().getString("MARKET_CONTENT");
        price = Integer.toString(getArguments().getInt("MARKET_PRICE", 0));
        phoneNumber = getArguments().getString("MARKET_PHONE");
        isPhoneOpen = getArguments().getBoolean("MARKET_PHONE_STATUS", false);
        itemState = getArguments().getInt("MARKET_STATE", 0);
        thumbNail = getArguments().getString("MARKET_THUMBNAIL_URL");

        if (phoneNumber == null) {
            phoneNumber = AuthorizeManager.getPhoneNumber(getContext());
        }

        init();
        return view;
    }

    @Override
    public void showLoading() {
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    void init() {
        this.imageUrl = new ArrayList<>();
        this.marketItem = new MarketItem();
        setPresenter(new MarketUsedEditPresenter(this, new MarketUsedRestInteractor()));

        mIsTitlecheck = false;
        mIsPhoneCheck = false;
        mIsContentCheck = false;
        mMarketBuyEditThumbnailImageView.setVisibility(View.VISIBLE);
        Glide.with(context).load(this.thumbNail).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(mMarketBuyEditThumbnailImageView);

        mMarketBuyEditTitleEditText.setText(this.title);
        mMarketBuyEditMoneyEditText.setText(changeMoneyFormatToStringWithComma(this.price));


        marketBuyEditEditTextPhoneNum.setFocusable(false);
        marketBuyEditEditTextPhoneNum.setClickable(false);


        if (this.isPhoneOpen) {
            mMarketBuyEditPhoneStatusRadioButtonGroup.clearCheck();
            mMarketBuyEditIsPhonePublicRadioButton.setChecked(true);
            setPhoneNumber();
        }


        mMarketBuyEditBuyingStatusRadioButtonGroup.clearCheck();
        if (this.itemState == 0)
            mMarketBuyEditIsBuyingRadioButton.setChecked(true);
        else if (this.itemState == 1)
            mMarketBuyEditIsStopBuyingRadioButton.setChecked(true);
        else
            mMarketBuyEditIsCompleteBuyingRadioButton.setChecked(true);

        if (content != null) {
            Spanned spannedValue = Html.fromHtml(content, getImageHTML(), null);
            mMarketBuyEditContent.setText(spannedValue);
        }

        moneyEditTextChangedListener();
        contentEditTextChangedListener();


        InputFilter[] inputArray = new InputFilter[1];
        inputArray[0] = new InputFilter.LengthFilter(MAXTITLELENGTH);
        mMarketBuyEditTitleEditText.setFilters(inputArray);

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


    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickEditButton();
    }


    @OnClick(R.id.market_used_buy_edit_thumbnail_change_button)
    void onThumbnailChangeButtonClick() {
        ImageLoader.with(getContext())
                .setImageLoadListener(this)
                .setChangeImageSize(true)
                .setImageHeight(500)
                .setImageWidth(500)
                .showGalleryOrCameraSelectDialog();
    }

    @OnCheckedChanged({R.id.market_used_buy_edit_is_phone_public_radiobutton, R.id.market_used_buy_edit_is_phone_private_radiobutton})
    public void onPhoneRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_buy_edit_is_phone_public_radiobutton:
                    setPhoneNumber();
                    marketBuyEditEditTextPhoneNum.setFocusableInTouchMode(true);
                    marketBuyEditEditTextPhoneNum.requestFocus();                                                     //포커스를 부여
                    marketBuyEditEditTextPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());     //자동으로 '-' 생성

                    this.isPhoneOpen = true;
                    break;
                case R.id.market_used_buy_edit_is_phone_private_radiobutton:
                    unSetPhoneNumber();
                    this.isPhoneOpen = false;
                    break;
            }
        }
    }


    @OnCheckedChanged({R.id.market_used_buy_edit_is_buying_radiobutton, R.id.market_used_buy_edit_is_stop_buying_radiobutton, R.id.market_used_buy_edit_is_complete_buying_radiobutton})
    public void onStateRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_buy_edit_is_buying_radiobutton:
                    this.itemState = 0;
                    break;
                case R.id.market_used_buy_edit_is_stop_buying_radiobutton:
                    this.itemState = 1;
                    break;
                case R.id.market_used_buy_edit_is_complete_buying_radiobutton:
                    this.itemState = 2;
                    break;
            }
        }
    }

    /**
     * 번호가 없을시 Toast message
     * 번호 입력 Edittext 수정가능하도록 set
     */
    public void setPhoneNumber() {
        marketBuyEditEditTextPhoneNum.setEnabled(true);                                     //핸드폰번호텍스트 활성화
        marketBuyEditEditTextPhoneNum.setTextIsSelectable(true);
        marketBuyEditEditTextPhoneNum.setClickable(true);
        if (this.phoneNumber == null)
            ToastUtil.getInstance().makeShort("휴대폰 번호를 기입해주세요");

        marketBuyEditEditTextPhoneNum.setText(this.phoneNumber);
    }

    /**
     * 번호 입력 Edittext 수정 못하도록 set
     */
    public void unSetPhoneNumber() {
        this.phoneNumber = marketBuyEditEditTextPhoneNum.getText().toString();
        marketBuyEditEditTextPhoneNum.setText(null);
        marketBuyEditEditTextPhoneNum.setEnabled(false);                                //핸드폰번호텍스트 비활성화
        marketBuyEditEditTextPhoneNum.setTextIsSelectable(false);
        marketBuyEditEditTextPhoneNum.setClickable(false);
        hideKeyboard(getActivity());
    }

    /**
     * 키보드를 사라지게 하는 함수
     *
     * @param activity
     */
    public void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(marketBuyEditEditTextPhoneNum.getWindowToken(), 0);
    }

    //TODO -> content edit
    public void contentEditTextChangedListener() {
        mMarketBuyEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(content)) {
                    content = s.toString();
                    int number = getCharNumber(content, (char) 65532);
                    if (number < imageUrl.size())
                        deleteImageUrlAtArrayList(content, start);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }


        });
    }


    public int getCharNumber(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

    public int getCharNumber(String str, int start, char c) {
        int count = 0;
        for (int i = 0; i < start; i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;

    }

    public void deleteImageUrlAtArrayList(String content, int start) {
        int number;
        if (!content.isEmpty() && !this.imageUrl.isEmpty()) {
            number = getCharNumber(content, start, (char) 65532);
            this.imageUrl.remove(number);
        }
    }


    public void moneyEditTextChangedListener() {
        mMarketBuyEditMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!text.equals(changeMoneyFormatToStringWithComma(price))) {
                    if (text.length() == 1 && text.charAt(0) == '0')
                        mMarketBuyEditMoneyEditText.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mMarketBuyEditMoneyEditText.getText().toString().equals(changeMoneyFormatToStringWithComma(price))) {

                    if (mMarketBuyEditMoneyEditText.getText().toString().length() != 0) {
                        price = changeMoneyFormatToStringWithoutComma(mMarketBuyEditMoneyEditText.getText().toString());
                        mMarketBuyEditMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    } else {
                        price = "0";
                        mMarketBuyEditMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    }


                    price = changeMoneyFormatToStringWithoutComma((mMarketBuyEditMoneyEditText.getText().toString()));
                    mMarketBuyEditMoneyEditText.setSelection(mMarketBuyEditMoneyEditText.getText().length());
                }
            }
        });
    }

    /**
     * @param money money를 인자로 받음
     * @return 1000단위로 comma를 넣어서 반환
     */
    public String changeMoneyFormatToStringWithComma(String money) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String changedStirng = "";

        if (!TextUtils.isEmpty(money) && !money.equals(changedStirng)) {
            changedStirng = decimalFormat.format(Double.parseDouble(money.replaceAll(",", "")));
        }
        return changedStirng;
    }

    /**
     * @param money comma가 있는 money를 인자로 받음
     * @return comma를 제외하고 반환
     */
    public String changeMoneyFormatToStringWithoutComma(String money) {
        StringTokenizer tokenizer = new StringTokenizer(money, ",");
        String formatChangedMoney = "";

        for (int i = 1; tokenizer.hasMoreElements(); i++) {
            formatChangedMoney += tokenizer.nextToken();
        }
        return formatChangedMoney;
    }
    /**
     * Edit 메뉴를 클릭시 title, content, phone 형식을 검사
     * state를 확인해서 item edit
     */
    public void onClickEditButton() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mMarketBuyEditContent.getText().toString().trim());
        this.phoneNumber = marketBuyEditEditTextPhoneNum.getText().toString().trim();


        if (this.isPhoneOpen) {
            this.marketItem.setIsPhoneOpen(1);
            this.marketItem.setPhone(this.phoneNumber);
        } else {
            this.marketItem.setIsPhoneOpen(0);
            this.marketItem.setPhone(null);
        }

        this.marketItem.setTitle(mMarketBuyEditTitleEditText.getText().toString().trim());
        this.marketItem.setPrice(Integer.parseInt(this.price));
        this.marketItem.setState(this.itemState);

        if (mMarketBuyEditContent.getText().toString().trim().length() != 0) {
            this.marketItem.setContent(Html.toHtml(spannableStringBuilder));
            this.marketItem.setContent(addImageSpan(this.marketItem.getContent(), this.imageUrl));
            mIsContentCheck = true;
        } else {
            mIsContentCheck = false;
        }


        if (this.isPhoneOpen && (this.marketItem.getPhone().length() == 13)) {
            if (this.phoneNumber.charAt(3) == '-' && this.phoneNumber.charAt(8) == '-')
                mIsPhoneCheck = true;
            else
                mIsPhoneCheck = false;
        } else if (!this.isPhoneOpen)
            mIsPhoneCheck = true;
        else
            mIsPhoneCheck = false;

        if (this.marketItem.getTitle().length() == 0)
            mIsTitlecheck = false;
        else {
            mIsTitlecheck = true;
        }

        if (mIsContentCheck && mIsTitlecheck && !mIsPhoneCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_phone_check);
        if (!mIsTitlecheck && mIsContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_check);
        if (!mIsContentCheck && mIsTitlecheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_content_check);
        if (!mIsTitlecheck && !mIsContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_content_check);
        this.marketItem.setType(this.marketId);
        if (mIsPhoneCheck && mIsTitlecheck && mIsContentCheck)
            marketUsedEditPresenter.editMarketContent(this.itemId, this.marketItem);

    }

    public String addImageSpan(String content, ArrayList<String> imageUrl) {
        if (content.contains("&#65532;")) {
            for (int i = 0; i < imageUrl.size(); i++) {
                String imageUrlSpan = "<img src=\"" + imageUrl.get(i) + "\">";
                content = content.replaceFirst("&#65532;", imageUrlSpan);
            }
            return content;
        }
        return content;
    }

    @Override
    public void showUpdateSuccess() {
        onBackPressed();
    }

    @Override
    public void showUpdateFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
    }

    @Override
    public void showImageUploadSuccess(String url) {
        mMarketBuyEditThumbnailImageView.setVisibility(View.VISIBLE);
        Glide.with(context).load(url).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(mMarketBuyEditThumbnailImageView);
        this.marketItem.setThumbnail(url);
    }

    @Override
    public void showImageUploadFail() {
        ToastUtil.getInstance().makeShort("이미지의 크기가 너무 큽니다.");
    }

    @Override
    public void setPresenter(MarketUsedEditPresenter presenter) {
        this.marketUsedEditPresenter = presenter;
    }

    @Override
    public void onImageAdded(List<Bitmap> bitmapList) {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "koin" + timeStamp + ".jpg";
        try {
            File imageFile = new File(context.getCacheDir(), imageFileName);
            imageFile.createNewFile();
            Bitmap bitmap = bitmapList.get(0);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            marketUsedEditPresenter.uploadThumbnailImage(imageFile);
        } catch (Exception e) {
            ToastUtil.getInstance().makeLong(R.string.fail_upload);
        }
    }

    @Override
    public void onImageFailed(ImageFailedType failedType) {
        if (failedType != ImageFailedType.CANCEL)
            ToastUtil.getInstance().makeShort(R.string.market_used_image_upload_failed);
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
                    imageUrl.add(s);
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
