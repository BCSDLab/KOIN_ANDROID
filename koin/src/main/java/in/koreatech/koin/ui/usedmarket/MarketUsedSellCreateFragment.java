package in.koreatech.koin.ui.usedmarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.yunjaena.imageloader.ImageFailedType;
import com.yunjaena.imageloader.ImageLoadListener;
import com.yunjaena.imageloader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedCreateContract;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedCreatePresenter;
import in.koreatech.koin.util.NavigationManger;

/**
 * This class create item at MarketUsed Sell
 * if the item create success activity will be finished and go back to MarketUsedSellFragment
 */
public class MarketUsedSellCreateFragment extends KoinBaseFragment implements MarketUsedCreateContract.View, ImageLoadListener {
    private static final int SELL_CODE = 0;
    private final String TAG = "MarketUsedSellEditFragment";
    private final int MAXTITLELENGTH = 39;
    @BindView(R.id.market_used_sell_create_thumbnail_imageview)
    ImageView marketSellCreateThumbnailImageView;
    @BindView(R.id.market_used_sell_create_thumbnail_change_button)
    Button marketSellCreateThumbnailChangeButton;
    @BindView(R.id.market_used_sell_create_title_textview)
    EditText marketSellCreateTitleEditText;
    @BindView(R.id.market_used_sell_create_money_edittext)
    EditText marketSellCreateMoneyEditText;
    @BindView(R.id.market_used_sell_create_phone_status_radiobutton_group)
    RadioGroup marketSellCreatePhoneStatusRadioButtonGroup;
    @BindView(R.id.market_used_sell_create_is_phone_public_radiobutton)
    RadioButton marketSellCreateIsPhonePublicRadioButton;
    @BindView(R.id.market_used_sell_create_is_phone_private_radiobutton)
    RadioButton marketSellCreateIsPhonePrivateRadioButton;
    @BindView(R.id.market_used_sell_create_edittext_phone_num)
    EditText marketSellCreateEditTextPhoneNum;
    @BindView(R.id.market_used_sell_create_selling_status_radiobutton_group)
    RadioGroup marketSellCreateSellingStatusRadioButtonGroup;
    @BindView(R.id.market_used_sell_create_is_selling_radiobutton)
    RadioButton marketSellCreateIsSellingRadioButton;
    @BindView(R.id.market_used_sell_create_is_stop_selling_radiobutton)
    RadioButton marketSellCreateIsStopSellingRadioButton;
    @BindView(R.id.market_used_sell_create_is_complete_selling_radiobutton)
    RadioButton marketSellCreateIsCompleteSellingRadioButton;
    @BindView(R.id.market_used_sell_create_content)
    EditText marketSellCreateContent;
    private Context context;
    private String price;
    private String phoneNumber;
    private boolean isPhoneOpen;
    private int itemState;
    private MarketItem marketItem;
    private MarketUsedCreatePresenter marketUsedCreatePresenter;
    private Uri currentPhotoPath;
    private File imageFile;
    private boolean isTitlecheck;
    private boolean isPhoneCheck;
    private boolean isContentCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_used_sell_create_fragment, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
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

    /**
     * mPhone, mIsTitleCheck, this.isPhoneCheck, this.isPhoneOpen, this.isContentCheck false로 초기화
     * this.itemState 0 구매중으로 초기화 this.price 0원으로 초기화
     * thumbnail 변경을 위한 camera 권한 체크
     * 가격을 입력시 ',' event 처리를 위해 moneyEditTextChangedListener() 호출
     * 사용자 번호가 등록이 되어있을경우 사용자 번호로 set 아닐경우 unset
     *
     * @see UserInfoSharedPreferencesHelper
     */
    void init() {

        this.isTitlecheck = false;
        this.isPhoneCheck = false;
        this.isPhoneOpen = false;
        this.isContentCheck = false;
        this.itemState = 0;
        this.price = "0";

        moneyEditTextChangedListener();
        setPresenter(new MarketUsedCreatePresenter(this, new MarketUsedRestInteractor()));


        this.marketItem = new MarketItem();

        InputFilter[] inputArray = new InputFilter[1];
        inputArray[0] = new InputFilter.LengthFilter(MAXTITLELENGTH);
        marketSellCreateTitleEditText.setFilters(inputArray);
        marketSellCreateMoneyEditText.setText(this.price);
        if (this.phoneNumber != null) {
            this.isPhoneOpen = true;
            marketSellCreateIsPhonePublicRadioButton.setChecked(true);
            setPhoneNumber();
        } else {
            this.isPhoneOpen = false;
            marketSellCreateEditTextPhoneNum.setText(null);
            marketSellCreateEditTextPhoneNum.setEnabled(false);                             //핸드폰번호 터치 불가능
            marketSellCreateEditTextPhoneNum.setTextIsSelectable(false);
            marketSellCreateEditTextPhoneNum.setClickable(false);
        }


    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppBarBase.getLeftButtonId())
            onBackPressed();
        else if (id == AppBarBase.getRightButtonId())
            onClickEditButton();
    }


    @OnClick(R.id.market_used_sell_create_thumbnail_change_button)
    void onThumbnailChangeButtonClick() {
        ImageLoader.with(getContext())
                .setImageLoadListener(this)
                .setChangeImageSize(true)
                .setImageHeight(500)
                .setImageWidth(500)
                .showGalleryOrCameraSelectDialog();
    }


    @OnCheckedChanged({R.id.market_used_sell_create_is_phone_public_radiobutton, R.id.market_used_sell_create_is_phone_private_radiobutton})
    public void onPhoneRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_sell_create_is_phone_public_radiobutton:
                    setPhoneNumber();
                    marketSellCreateEditTextPhoneNum.setFocusableInTouchMode(true);
                    marketSellCreateEditTextPhoneNum.requestFocus();                                                        //포커스 부여
                    marketSellCreateEditTextPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());     //자동으로 '-' 생성
                    this.isPhoneOpen = true;
                    break;
                case R.id.market_used_sell_create_is_phone_private_radiobutton:
                    unSetPhoneNumber();
                    this.isPhoneOpen = false;
                    break;
            }
        }
    }


    @OnCheckedChanged({R.id.market_used_sell_create_is_selling_radiobutton, R.id.market_used_sell_create_is_stop_selling_radiobutton, R.id.market_used_sell_create_is_complete_selling_radiobutton})
    public void onStateRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_sell_create_is_selling_radiobutton:
                    this.itemState = 0;
                    break;
                case R.id.market_used_sell_create_is_stop_selling_radiobutton:
                    this.itemState = 1;
                    break;
                case R.id.market_used_sell_create_is_complete_selling_radiobutton:
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
        marketSellCreateEditTextPhoneNum.setTextIsSelectable(true);
        marketSellCreateEditTextPhoneNum.setClickable(true);
        marketSellCreateEditTextPhoneNum.setEnabled(true);                              //핸드폰번호텍스트 활성화
        if (this.phoneNumber == null) {
            ToastUtil.getInstance().makeShort("휴대폰 번호를 기입해주세요");
            return;
        }
        marketSellCreateEditTextPhoneNum.setText(this.phoneNumber);
    }

    /**
     * 번호 입력 Edittext 수정 못하도록 set
     */

    public void unSetPhoneNumber() {
        this.phoneNumber = marketSellCreateEditTextPhoneNum.getText().toString();
        marketSellCreateEditTextPhoneNum.setText(null);
        marketSellCreateEditTextPhoneNum.setEnabled(false);                             //핸드폰번호텍스트 비활성화
        marketSellCreateEditTextPhoneNum.setTextIsSelectable(false);
        marketSellCreateEditTextPhoneNum.setClickable(false);
        hideKeyboard(getActivity());
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(marketSellCreateEditTextPhoneNum.getWindowToken(), 0);
    }

    /**
     * 숫자가 처음에 0원 일 경우 0을 지우고 입력한 숫자로 set
     * 변경된 숫자는 changeMoneyFormatStringWithComma() method를 통해서 ','를 붙여줌
     * 숫자는 매 변경시 changeMoneyFormatStringWithoutComma() method를 통해서 ','를 제외하고 값 저장
     */
    public void moneyEditTextChangedListener() {
        marketSellCreateMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!text.equals(changeMoneyFormatToStringWithComma(price))) {
                    if (text.length() == 1 && text.charAt(0) == '0')
                        marketSellCreateMoneyEditText.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!marketSellCreateMoneyEditText.getText().toString().equals(changeMoneyFormatToStringWithComma(price))) {

                    if (marketSellCreateMoneyEditText.getText().toString().length() != 0) {
                        price = changeMoneyFormatToStringWithoutComma(marketSellCreateMoneyEditText.getText().toString());
                        marketSellCreateMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    } else {
                        price = "0";
                        marketSellCreateMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    }

                    price = changeMoneyFormatToStringWithoutComma((marketSellCreateMoneyEditText.getText().toString()));
                    marketSellCreateMoneyEditText.setSelection(marketSellCreateMoneyEditText.getText().length());
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

    /**
     * Edit 메뉴를 클릭시 title, content, phone 형식을 검사
     * state를 확인해서 item create
     */
    public void onClickEditButton() {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(marketSellCreateContent.getText().toString().trim());
        this.phoneNumber = marketSellCreateEditTextPhoneNum.getText().toString().trim();

        if (this.isPhoneOpen) {
            this.marketItem.setIsPhoneOpen(1);
            this.marketItem.setPhone(this.phoneNumber);
        } else {
            this.marketItem.setIsPhoneOpen(0);
            this.marketItem.setPhone(null);
        }
        this.marketItem.setType(SELL_CODE);
        this.marketItem.setTitle(marketSellCreateTitleEditText.getText().toString().trim());
        this.marketItem.setPrice(Integer.parseInt(this.price));

        if (marketSellCreateContent.getText().toString().trim().length() != 0) {
            this.marketItem.setContent(Html.toHtml(spannableStringBuilder).trim());
            this.isContentCheck = true;
        } else {
            this.isContentCheck = false;
        }

        this.marketItem.setState(this.itemState);
        if (this.isPhoneOpen && (this.marketItem.getPhone().length() == 13)) {
            if (this.phoneNumber.charAt(3) == '-' && this.phoneNumber.charAt(8) == '-')
                this.isPhoneCheck = true;
            else
                this.isPhoneCheck = false;

        } else if (!this.isPhoneOpen)
            this.isPhoneCheck = true;
        else
            this.isPhoneCheck = false;

        if (this.marketItem.getTitle().length() == 0)
            this.isTitlecheck = false;
        else
            this.isTitlecheck = true;


        if (this.isContentCheck && this.isTitlecheck && !this.isPhoneCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_phone_check);
        if (!this.isTitlecheck && this.isContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_check);
        if (!this.isContentCheck && this.isTitlecheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_content_check);
        if (!this.isTitlecheck && !this.isContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_content_check);

        if (this.isPhoneCheck && this.isTitlecheck && this.isContentCheck)
            this.marketUsedCreatePresenter.createMarketItem(this.marketItem);
    }

    @Override
    public void showMarketCreatedSuccess(Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("MARKET_ID", SELL_CODE);
        bundle.putInt("ITEM_ID", item.getId());
        bundle.putBoolean("GRANT_CHECK", true);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_sell_detail_action, bundle, NavigationManger.getNavigationOptionAnimation().setPopUpTo(R.id.market_used_sell_create_fragment, true).build());
    }

    @Override
    public void showMarketCreatefFail() {
        ToastUtil.getInstance().makeShort(R.string.server_failed);
    }

    @Override
    public void setPresenter(MarketUsedCreatePresenter presenter) {
        this.marketUsedCreatePresenter = presenter;
    }


    @Override
    public void showImageUploadSuccess(String url) {
        marketSellCreateThumbnailImageView.setVisibility(View.VISIBLE);
        Glide.with(context).asBitmap().load(url).into(marketSellCreateThumbnailImageView);
        this.marketItem.setThumbnail(url);
    }

    @Override
    public void showImageUploadFail() {

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
            marketUsedCreatePresenter.uploadThumbnailImage(imageFile);
        } catch (Exception e) {
            ToastUtil.getInstance().makeLong(R.string.fail_upload);
        }
    }

    @Override
    public void onImageFailed(ImageFailedType failedType) {
        if (failedType != ImageFailedType.CANCEL)
            ToastUtil.getInstance().makeShort(R.string.market_used_image_upload_failed);
    }
}
