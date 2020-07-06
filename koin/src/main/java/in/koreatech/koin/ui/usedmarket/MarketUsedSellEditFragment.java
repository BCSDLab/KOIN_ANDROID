package in.koreatech.koin.ui.usedmarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yunjaena.imageloader.ImageFailedType;
import com.yunjaena.imageloader.ImageLoadListener;
import com.yunjaena.imageloader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditPresenter;
import in.koreatech.koin.util.AuthorizeManager;

/**
 * This class edit item at MarketUsedSellEdit
 * if the item edit success activity will be finished and go back to MarketUsedSellDetailFragment
 */
public class MarketUsedSellEditFragment extends KoinBaseFragment implements MarketUsedEditContract.View, ImageLoadListener {
    private static final int MY_REQUEST_CODE = 100;
    private static final int REQUEST_GET_GALLERY = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_CROP_IMAGE = 3;
    private final String TAG = "MarketUsedSellEditFragment";
    private final int MAXTITLELENGTH = 39;
    @BindView(R.id.market_used_sell_edit_thumbnail_imageview)
    ImageView marketSellEditThumbnailImageView;
    @BindView(R.id.market_used_sell_edit_thumbnail_change_button)
    Button marketSellEditThumbnailChangeButton;
    @BindView(R.id.market_used_sell_edit_title_textview)
    EditText marketSellEditTitleEditText;
    @BindView(R.id.market_used_sell_edit_money_edittext)
    EditText marketSellEditMoneyEditText;
    @BindView(R.id.market_used_sell_edit_phone_status_radiobutton_group)
    RadioGroup marketSellEditPhoneStatusRadioButtonGroup;
    @BindView(R.id.market_used_sell_edit_is_phone_public_radiobutton)
    RadioButton marketSellEditIsPhonePublicRadioButton;
    @BindView(R.id.market_used_sell_edit_is_phone_private_radiobutton)
    RadioButton marketSellEditIsPhonePrivateRadioButton;
    @BindView(R.id.market_used_sell_edit_edittext_phone_num)
    EditText marketSellEditEditTextPhoneNum;
    @BindView(R.id.market_used_sell_edit_selling_status_radiobutton_group)
    RadioGroup marketSellEditSellingStatusRadioButtonGroup;
    @BindView(R.id.market_used_sell_edit_is_selling_radiobutton)
    RadioButton marketSellEditIsSellingRadioButton;
    @BindView(R.id.market_used_sell_edit_is_stop_selling_radiobutton)
    RadioButton marketSellEditIsStopSellingRadioButton;
    @BindView(R.id.market_used_sell_edit_is_complete_selling_radiobutton)
    RadioButton marketSellEditIsCompleteSellingRadioButton;
    @BindView(R.id.market_used_sell_edit_content)
    EditText marketSellEditContent;
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
    private boolean isTitlecheck;
    private boolean isPhoneCheck;
    private boolean isContentCheck;

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
        imageUrl = new ArrayList<>();
        marketItem = new MarketItem();
        setPresenter(new MarketUsedEditPresenter(this, new MarketUsedRestInteractor()));

        isTitlecheck = false;
        isPhoneCheck = false;
        isContentCheck = false;
        marketSellEditThumbnailImageView.setVisibility(View.VISIBLE);
        Glide.with(context).load(thumbNail).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(marketSellEditThumbnailImageView);

        marketSellEditTitleEditText.setText(title);
        marketSellEditMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));


        marketSellEditEditTextPhoneNum.setFocusable(false);


        if (isPhoneOpen) {
            marketSellEditPhoneStatusRadioButtonGroup.clearCheck();
            marketSellEditIsPhonePublicRadioButton.setChecked(true);
            setPhoneNumber();
        }


        marketSellEditSellingStatusRadioButtonGroup.clearCheck();
        if (itemState == 0)
            marketSellEditIsSellingRadioButton.setChecked(true);
        else if (itemState == 1)
            marketSellEditIsStopSellingRadioButton.setChecked(true);
        else
            marketSellEditIsCompleteSellingRadioButton.setChecked(true);

        if (content != null) {
            Spanned spannedValue = Html.fromHtml(content, getImageHTML(), null);
            marketSellEditContent.setText(spannedValue);
        }

        moneyEditTextChangedListener();
        contentEditTextChangedListener();


        InputFilter[] inputArray = new InputFilter[1];
        inputArray[0] = new InputFilter.LengthFilter(MAXTITLELENGTH);
        marketSellEditTitleEditText.setFilters(inputArray);

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


    @OnClick(R.id.market_used_sell_edit_thumbnail_change_button)
    void onThumbnailChangeButtonClick() {
        ImageLoader.with(getContext())
                .setImageLoadListener(this)
                .setChangeImageSize(true)
                .setImageHeight(500)
                .setImageWidth(500)
                .showGalleryOrCameraSelectDialog();
    }


    @OnCheckedChanged({R.id.market_used_sell_edit_is_phone_public_radiobutton, R.id.market_used_sell_edit_is_phone_private_radiobutton})
    public void onPhoneRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_sell_edit_is_phone_public_radiobutton:
                    setPhoneNumber();
                    marketSellEditEditTextPhoneNum.setFocusableInTouchMode(true);
                    marketSellEditEditTextPhoneNum.requestFocus();                                                      //포커스 부여
                    marketSellEditEditTextPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());     //자동으로 '-' 생성
                    isPhoneOpen = true;
                    break;
                case R.id.market_used_sell_edit_is_phone_private_radiobutton:
                    unSetPhoneNumber();
                    isPhoneOpen = false;
                    break;
            }
        }
    }


    @OnCheckedChanged({R.id.market_used_sell_edit_is_selling_radiobutton, R.id.market_used_sell_edit_is_stop_selling_radiobutton, R.id.market_used_sell_edit_is_complete_selling_radiobutton})
    public void onStateRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_sell_edit_is_selling_radiobutton:
                    itemState = 0;
                    break;
                case R.id.market_used_sell_edit_is_stop_selling_radiobutton:
                    itemState = 1;
                    break;
                case R.id.market_used_sell_edit_is_complete_selling_radiobutton:
                    itemState = 2;
                    break;
            }
        }
    }

    /**
     * 번호가 없을시 Toast message
     * 번호 입력 Edittext 수정가능하도록 set
     */
    public void setPhoneNumber() {
        marketSellEditEditTextPhoneNum.setTextIsSelectable(true);
        marketSellEditEditTextPhoneNum.setClickable(true);
        marketSellEditEditTextPhoneNum.setEnabled(true);
        if (phoneNumber == null) {
            ToastUtil.getInstance().makeShort("휴대폰 번호를 기입해주세요");
            return;
        }
        marketSellEditEditTextPhoneNum.setText(phoneNumber);
    }

    /**
     * 번호 입력 Edittext 수정 못하도록 set
     */
    public void unSetPhoneNumber() {
        phoneNumber = marketSellEditEditTextPhoneNum.getText().toString();
        marketSellEditEditTextPhoneNum.setText(null);
        marketSellEditEditTextPhoneNum.setTextIsSelectable(false);
        marketSellEditEditTextPhoneNum.setEnabled(false);                                   ////핸드폰번호텍스트 비활성화
        marketSellEditEditTextPhoneNum.setClickable(false);
        hideKeyboard(getActivity());
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(marketSellEditEditTextPhoneNum.getWindowToken(), 0);
    }

    //TODO -> content edit
    public void contentEditTextChangedListener() {
        marketSellEditContent.addTextChangedListener(new TextWatcher() {
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
        if (!content.isEmpty() && !imageUrl.isEmpty()) {
            number = getCharNumber(content, start, (char) 65532);
            imageUrl.remove(number);
        }
    }


    public void moneyEditTextChangedListener() {
        marketSellEditMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!text.equals(changeMoneyFormatToStringWithComma(price))) {
                    if (text.length() == 1 && text.charAt(0) == '0')
                        marketSellEditMoneyEditText.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!marketSellEditMoneyEditText.getText().toString().equals(changeMoneyFormatToStringWithComma(price))) {

                    if (marketSellEditMoneyEditText.getText().toString().length() != 0) {
                        price = changeMoneyFormatToStringWithoutComma(marketSellEditMoneyEditText.getText().toString());
                        marketSellEditMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    } else {
                        price = "0";
                        marketSellEditMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    }


                    price = changeMoneyFormatToStringWithoutComma((marketSellEditMoneyEditText.getText().toString()));
                    marketSellEditMoneyEditText.setSelection(marketSellEditMoneyEditText.getText().length());
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
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(marketSellEditContent.getText().toString().trim());
        phoneNumber = marketSellEditEditTextPhoneNum.getText().toString().trim();


        if (isPhoneOpen) {
            marketItem.setIsPhoneOpen(1);
            marketItem.setPhone(phoneNumber);
        } else {
            marketItem.setIsPhoneOpen(0);
            marketItem.setPhone(null);
        }

        marketItem.setTitle(marketSellEditTitleEditText.getText().toString().trim());
        marketItem.setPrice(Integer.parseInt(price));
        marketItem.setState(itemState);

        if (marketSellEditContent.getText().toString().trim().length() != 0) {
            marketItem.setContent(Html.toHtml(spannableStringBuilder));
            marketItem.setContent(addImageSpan(marketItem.getContent(), imageUrl));
            isContentCheck = true;
        } else {
            isContentCheck = false;
        }


        if (isPhoneOpen && (marketItem.getPhone().length() == 13)) {
            if (phoneNumber.charAt(3) == '-' && phoneNumber.charAt(8) == '-')
                isPhoneCheck = true;
            else
                isPhoneCheck = false;

        } else if (!isPhoneOpen)
            isPhoneCheck = true;
        else
            isPhoneCheck = false;

        if (marketItem.getTitle().length() == 0)
            isTitlecheck = false;
        else {
            isTitlecheck = true;
        }

        if (isContentCheck && isTitlecheck && !isPhoneCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_phone_check);
        if (!isTitlecheck && isContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_check);
        if (!isContentCheck && isTitlecheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_content_check);
        if (!isTitlecheck && !isContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_content_check);
        marketItem.setType(marketId);
        if (isPhoneCheck && isTitlecheck && isContentCheck)
            marketUsedEditPresenter.editMarketContent(itemId, marketItem);

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
        marketSellEditThumbnailImageView.setVisibility(View.VISIBLE);
        Glide.with(context).load(url).apply(new RequestOptions()
                .placeholder(R.drawable.img_noimage_big)
                .error(R.drawable.img_noimage_big)        //Error상황에서 보여진다.
        ).into(marketSellEditThumbnailImageView);
        marketItem.setThumbnail(url);
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
