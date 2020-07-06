package in.koreatech.koin.ui.usedmarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import in.koreatech.koin.util.AuthorizeManager;
import in.koreatech.koin.util.NavigationManger;

/**
 * This class create item at MarketUsed Buy
 * if the item create success activity will be finished and go back to MarketUsedBuyFragment
 */
public class MarketUsedBuyCreateFragment extends KoinBaseFragment implements MarketUsedCreateContract.View, ImageLoadListener {
    private static final int BUY_CODE = 1;
    private final String TAG = "MarketUsedBuyEditFragment";
    private final int MAXTITLELENGTH = 39;
    @BindView(R.id.market_used_buy_create_thumbnail_imageview)
    ImageView marketBuyCreateThumbnailImageView;
    @BindView(R.id.market_used_buy_create_thumbnail_change_button)
    Button marketBuyCreateThumbnailChangeButton;
    @BindView(R.id.market_used_buy_create_title_textview)
    EditText marketBuyCreateTitleEditText;
    @BindView(R.id.market_used_buy_create_money_edittext)
    EditText marketBuyCreateMoneyEditText;
    @BindView(R.id.market_used_buy_create_phone_status_radiobutton_group)
    RadioGroup marketBuyCreatePhoneStatusRadioButtonGroup;
    @BindView(R.id.market_used_buy_create_is_phone_public_radiobutton)
    RadioButton marketBuyCreateIsPhonePublicRadioButton;
    @BindView(R.id.market_used_buy_create_is_phone_private_radiobutton)
    RadioButton marketBuyCreateIsPhonePrivateRadioButton;
    @BindView(R.id.market_used_buy_create_edittext_phone_num)
    EditText marketBuyCreateEditTextPhoneNum;
    @BindView(R.id.market_used_buy_create_buying_status_radiobutton_group)
    RadioGroup marketBuyCreateBuyingStatusRadioButtonGroup;
    @BindView(R.id.market_used_buy_create_is_buying_radiobutton)
    RadioButton marketBuyCreateIsBuyingRadioButton;
    @BindView(R.id.market_used_buy_create_is_stop_buying_radiobutton)
    RadioButton marketBuyCreateIsStopBuyingRadioButton;
    @BindView(R.id.market_used_buy_create_is_complete_buying_radiobutton)
    RadioButton marketBuyCreateIsCompleteBuyingRadioButton;
    @BindView(R.id.market_used_buy_create_content)
    EditText marketBuyCreateContent;
    private Context context;
    private String price;
    private String phoneNumber;
    private boolean isPhoneOpen;
    private int itemState;
    private MarketItem marketItem;
    private MarketUsedCreatePresenter marketUsedCreatePresenter;
    private boolean isTitlecheck;
    private boolean isPhoneCheck;
    private boolean isContentCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_used_buy_create_fragment, container, false);
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
     * mPhone, mIsTitleCheck, isPhoneCheck, isPhoneOpen, isContentCheck false로 초기화
     * itemState 0 구매중으로 초기화 price 0원으로 초기화
     * thumbnail 변경을 위한 camera 권한 체크
     * 가격을 입력시 ',' event 처리를 위해 moneyEditTextChangedListener() 호출
     * 사용자 번호가 등록이 되어있을경우 사용자 번호로 set 아닐경우 unset
     *
     * @see UserInfoSharedPreferencesHelper
     */
    void init() {

        isTitlecheck = false;
        isPhoneCheck = false;
        isPhoneOpen = false;
        isContentCheck = false;
        phoneNumber = AuthorizeManager.getPhoneNumber(getContext());
        itemState = 0;
        price = "0";

        moneyEditTextChangedListener();
        setPresenter(new MarketUsedCreatePresenter(this, new MarketUsedRestInteractor()));

        marketItem = new MarketItem();

        InputFilter[] inputArray = new InputFilter[1];
        inputArray[0] = new InputFilter.LengthFilter(MAXTITLELENGTH);
        marketBuyCreateTitleEditText.setFilters(inputArray);
        marketBuyCreateMoneyEditText.setText(price);
        if (phoneNumber != null) {
            isPhoneOpen = true;
            marketBuyCreateIsPhonePublicRadioButton.setChecked(true);
            setPhoneNumber();
        } else {
            isPhoneOpen = false;
            marketBuyCreateEditTextPhoneNum.setText(null);
            marketBuyCreateEditTextPhoneNum.setEnabled(false);                      //핸드폰번호텍스트 비활성화
            marketBuyCreateEditTextPhoneNum.setTextIsSelectable(false);
            marketBuyCreateEditTextPhoneNum.setClickable(false);
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


    @OnClick(R.id.market_used_buy_create_thumbnail_change_button)
    void onThumbnailChangeButtonClick() {
        ImageLoader.with(getContext())
                .setImageLoadListener(this)
                .setChangeImageSize(true)
                .setImageHeight(500)
                .setImageWidth(500)
                .showGalleryOrCameraSelectDialog();
    }


    @OnCheckedChanged({R.id.market_used_buy_create_is_phone_public_radiobutton, R.id.market_used_buy_create_is_phone_private_radiobutton})
    public void onPhoneRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_buy_create_is_phone_public_radiobutton:
                    setPhoneNumber();
                    marketBuyCreateEditTextPhoneNum.setFocusableInTouchMode(true);
                    marketBuyCreateEditTextPhoneNum.requestFocus();                                                     //포커스 부여
                    marketBuyCreateEditTextPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());     //자동으로 '-' 생성
                    isPhoneOpen = true;
                    break;
                case R.id.market_used_buy_create_is_phone_private_radiobutton:
                    unSetPhoneNumber();
                    isPhoneOpen = false;
                    break;
            }
        }
    }


    @OnCheckedChanged({R.id.market_used_buy_create_is_buying_radiobutton, R.id.market_used_buy_create_is_stop_buying_radiobutton, R.id.market_used_buy_create_is_complete_buying_radiobutton})
    public void onStateRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_buy_create_is_buying_radiobutton:
                    itemState = 0;
                    break;
                case R.id.market_used_buy_create_is_stop_buying_radiobutton:
                    itemState = 1;
                    break;
                case R.id.market_used_buy_create_is_complete_buying_radiobutton:
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
        if (phoneNumber == null)
            ToastUtil.getInstance().makeShort("휴대폰 번호를 기입해주세요");
        marketBuyCreateEditTextPhoneNum.setEnabled(true);                               //핸드폰번호텍스트 활성화
        marketBuyCreateEditTextPhoneNum.setTextIsSelectable(true);
        marketBuyCreateEditTextPhoneNum.setClickable(true);
        marketBuyCreateEditTextPhoneNum.setText(phoneNumber);
    }

    /**
     * 번호 입력 Edittext 수정 못하도록 set
     */

    public void unSetPhoneNumber() {
        phoneNumber = marketBuyCreateEditTextPhoneNum.getText().toString();
        marketBuyCreateEditTextPhoneNum.setText(null);
        marketBuyCreateEditTextPhoneNum.setEnabled(false);                              //핸드폰번호텍스트 비활성화
        marketBuyCreateEditTextPhoneNum.setTextIsSelectable(false);
        marketBuyCreateEditTextPhoneNum.setClickable(false);
        hideKeyboard(getActivity());
    }

    /**
     * 키보드를 사라지게 하는 함수
     *
     * @param activity
     */
    public void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(marketBuyCreateEditTextPhoneNum.getWindowToken(), 0);
    }

    /**
     * 숫자가 처음에 0원 일 경우 0을 지우고 입력한 숫자로 set
     * 변경된 숫자는 changeMoneyFormatStringWithComma() method를 통해서 ','를 붙여줌
     * 숫자는 매 변경시 changeMoneyFormatStringWithoutComma() method를 통해서 ','를 제외하고 값 저장
     */
    public void moneyEditTextChangedListener() {
        marketBuyCreateMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!text.equals(changeMoneyFormatToStringWithComma(price))) {
                    if (text.length() == 1 && text.charAt(0) == '0')
                        marketBuyCreateMoneyEditText.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!marketBuyCreateMoneyEditText.getText().toString().equals(changeMoneyFormatToStringWithComma(price))) {

                    if (marketBuyCreateMoneyEditText.getText().toString().length() != 0) {
                        price = changeMoneyFormatToStringWithoutComma(marketBuyCreateMoneyEditText.getText().toString());
                        marketBuyCreateMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    } else {
                        price = "0";
                        marketBuyCreateMoneyEditText.setText(changeMoneyFormatToStringWithComma(price));
                    }

                    price = changeMoneyFormatToStringWithoutComma((marketBuyCreateMoneyEditText.getText().toString()));
                    marketBuyCreateMoneyEditText.setSelection(marketBuyCreateMoneyEditText.getText().length());
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

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(marketBuyCreateContent.getText().toString().trim());
        phoneNumber = marketBuyCreateEditTextPhoneNum.getText().toString().trim();

        if (isPhoneOpen) {
            marketItem.setIsPhoneOpen(1);
            marketItem.setPhone(phoneNumber);
        } else {
            marketItem.setIsPhoneOpen(0);
            marketItem.setPhone(null);
        }
        marketItem.setType(BUY_CODE);
        marketItem.setTitle(marketBuyCreateTitleEditText.getText().toString().trim());
        marketItem.setPrice(Integer.parseInt(price));

        if (marketBuyCreateContent.getText().toString().trim().length() != 0) {
            marketItem.setContent(Html.toHtml(spannableStringBuilder).trim());
            isContentCheck = true;
        } else {
            isContentCheck = false;
        }

        marketItem.setState(itemState);
        if (isPhoneOpen && (marketItem.getPhone().trim().length() == 13)) {
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
        else
            isTitlecheck = true;


        if (isContentCheck && isTitlecheck && !isPhoneCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_phone_check);
        if (!isTitlecheck && isContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_check);
        if (!isContentCheck && isTitlecheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_content_check);
        if (!isTitlecheck && !isContentCheck)
            ToastUtil.getInstance().makeShort(R.string.market_used_title_content_check);

        if (isPhoneCheck && isTitlecheck && isContentCheck)
            marketUsedCreatePresenter.createMarketItem(marketItem);
    }

    @Override
    public void showMarketCreatedSuccess(Item item) {
        Bundle bundle = new Bundle();
        bundle.putInt("MARKET_ID", BUY_CODE);
        bundle.putInt("ITEM_ID", item.getId());
        bundle.putBoolean("GRANT_CHECK", true);
        NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_buy_detail_action, bundle, NavigationManger.getNavigationOptionAnimation().setPopUpTo(R.id.market_used_buy_create_fragment, true).build());
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
        marketBuyCreateThumbnailImageView.setVisibility(View.VISIBLE);
        Glide.with(context).asBitmap().load(url).into(marketBuyCreateThumbnailImageView);
        marketItem.setThumbnail(url);
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
