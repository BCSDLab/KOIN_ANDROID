package in.koreatech.koin.service_used_market.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.text.*;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import com.bumptech.glide.Glide;

import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.bases.KoinBaseAppbarDark;
import in.koreatech.koin.service_used_market.contracts.MarketUsedCreateContract;
import in.koreatech.koin.core.helpers.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.core.networks.entity.Item;
import in.koreatech.koin.core.networks.entity.MarketItem;
import in.koreatech.koin.core.networks.interactors.MarketUsedRestInteractor;
import in.koreatech.koin.service_used_market.presenters.MarketUsedCreatePresenter;
import in.koreatech.koin.core.util.ToastUtil;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class create item at MarketUsed Sell
 * if the item create success activity will be finished and go back to MarketUsedSellFragment
 *
 * @author Yunjae Na
 * @see MarketUsedSellFragment
 * @since 2018. 09.15
 */
public class MarketUsedSellCreateActivity extends ActivityBase implements MarketUsedCreateContract.View {
    private final String TAG = MarketUsedSellEditActivity.class.getSimpleName();
    private final int MAXTITLELENGTH = 39;
    private static final int MY_REQUEST_CODE = 100;
    private static final int REQUEST_GET_GALLERY = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_CROP_IMAGE = 3;
    private static final int SELL_CODE = 0;

    private Context mContext;

    private int mId;
    private String mTitle;
    private String mContent;
    private String mPrice;
    private String mPhoneNumber;
    private boolean mIsPhoneOpen;
    private int mItemState;
    private String mThumbNail;
    private ArrayList<String> mImageUrl;

    private MarketItem mMarketItem;
    private MarketUsedCreateContract.Presenter mMarketUsedCreatePresenter;
    private Uri mCurrentPhotoPath;
    private File mImageFile;
    private CustomProgressDialog customProgressDialog;

    private boolean mIsTitlecheck;
    private boolean mIsPhoneCheck;
    private boolean mIsContentCheck;

    @BindView(R.id.market_used_sell_create_thumbnail_imageview)
    ImageView mMarketSellCreateThumbnailImageView;
    @BindView(R.id.market_used_sell_create_thumbnail_change_button)
    Button mMarketSellCreateThumbnailChangeButton;
    @BindView(R.id.market_used_sell_create_title_textview)
    EditText mMarketSellCreateTitleEditText;
    @BindView(R.id.market_used_sell_create_money_edittext)
    EditText mMarketSellCreateMoneyEditText;

    @BindView(R.id.market_used_sell_create_phone_status_radiobutton_group)
    RadioGroup mMarketSellCreatePhoneStatusRadioButtonGroup;
    @BindView(R.id.market_used_sell_create_is_phone_public_radiobutton)
    RadioButton mMarketSellCreateIsPhonePublicRadioButton;
    @BindView(R.id.market_used_sell_create_is_phone_private_radiobutton)
    RadioButton mMarketSellCreateIsPhonePrivateRadioButton;

    @BindView(R.id.market_used_sell_create_edittext_phone_num)
    EditText mMarketSellCreateEditTextPhoneNum;


    @BindView(R.id.market_used_sell_create_selling_status_radiobutton_group)
    RadioGroup mMarketSellCreateSellingStatusRadioButtonGroup;
    @BindView(R.id.market_used_sell_create_is_selling_radiobutton)
    RadioButton mMarketSellCreateIsSellingRadioButton;
    @BindView(R.id.market_used_sell_create_is_stop_selling_radiobutton)
    RadioButton mMarketSellCreateIsStopSellingRadioButton;
    @BindView(R.id.market_used_sell_create_is_complete_selling_radiobutton)
    RadioButton mMarketSellCreateIsCompleteSellingRadioButton;

    @BindView(R.id.market_used_sell_create_content)
    EditText mMarketSellCreateContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.market_used_sell_create_activity);
        ButterKnife.bind(this);
        mPhoneNumber = UserInfoSharedPreferencesHelper.getInstance().loadUser().phoneNumber;
        init();


    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onRestart() {
        super.onRestart();
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

    /**
     * mPhone, mIsTitleCheck, mIsPhoneCheck, mIsPhoneOpen, mIsContentCheck false로 초기화
     * mItemState 0 구매중으로 초기화 mPrice 0원으로 초기화
     * thumbnail 변경을 위한 camera 권한 체크
     * 가격을 입력시 ',' event 처리를 위해 moneyEditTextChangedListener() 호출
     * 사용자 번호가 등록이 되어있을경우 사용자 번호로 set 아닐경우 unset
     *
     * @see UserInfoSharedPreferencesHelper
     */
    void init() {

        mIsTitlecheck = false;
        mIsPhoneCheck = false;
        mIsPhoneOpen = false;
        mIsContentCheck = false;
        mItemState = 0;
        mPrice = "0";

        getPermisson();
        moneyEditTextChangedListener();
        setPresenter(new MarketUsedCreatePresenter(this, new MarketUsedRestInteractor()));


        mMarketItem = new MarketItem();

        InputFilter[] inputArray = new InputFilter[1];
        inputArray[0] = new InputFilter.LengthFilter(MAXTITLELENGTH);
        mMarketSellCreateTitleEditText.setFilters(inputArray);
        mMarketSellCreateMoneyEditText.setText(mPrice);
        if (mPhoneNumber != null) {
            mIsPhoneOpen = true;
            mMarketSellCreateIsPhonePublicRadioButton.setChecked(true);
            setPhoneNumber();
        } else {
            mIsPhoneOpen = false;
            mMarketSellCreateEditTextPhoneNum.setText(null);
            mMarketSellCreateEditTextPhoneNum.setTextIsSelectable(false);
            mMarketSellCreateEditTextPhoneNum.setClickable(false);
        }


    }

    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == KoinBaseAppbarDark.getLeftButtonId())
            finish();
        else if (id == KoinBaseAppbarDark.getRightButtonId())
            onClickEditButton();
    }


    @OnClick(R.id.market_used_sell_create_thumbnail_change_button)
    void onThumbnailChangeButtonClick() {
        getPermisson();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (checkCameraPermisson() && checkStoragePermisson()) {
            builder.setMessage("이미지를 불러올 방법을 골라주세요.");
            builder.setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    File photoFile = null;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.

                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        Toast.makeText(mContext, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    if (photoFile != null) {
                        mCurrentPhotoPath = FileProvider.getUriForFile(mContext,
                                "in.koreatech.koin.provider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                    }

                }
            });

            builder.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_PICK);
                    mediaScanIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    mediaScanIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(mediaScanIntent, REQUEST_GET_GALLERY);
                }
            });
            builder.setNeutralButton("취소", null);
            builder.create().show();
        } else
            ToastUtil.getInstance().makeShortToast("기능 사용을 위한 권한 동의가 필요합니다.");

    }


    @OnCheckedChanged({R.id.market_used_sell_create_is_phone_public_radiobutton, R.id.market_used_sell_create_is_phone_private_radiobutton})
    public void onPhoneRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_sell_create_is_phone_public_radiobutton:
                    setPhoneNumber();
                    mIsPhoneOpen = true;
                    break;
                case R.id.market_used_sell_create_is_phone_private_radiobutton:
                    unSetPhoneNumber();
                    mIsPhoneOpen = false;
                    break;
            }
        }
    }


    @OnCheckedChanged({R.id.market_used_sell_create_is_selling_radiobutton, R.id.market_used_sell_create_is_stop_selling_radiobutton, R.id.market_used_sell_create_is_complete_selling_radiobutton})
    public void onStateRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_sell_create_is_selling_radiobutton:
                    mItemState = 0;
                    break;
                case R.id.market_used_sell_create_is_stop_selling_radiobutton:
                    mItemState = 1;
                    break;
                case R.id.market_used_sell_create_is_complete_selling_radiobutton:
                    mItemState = 2;
                    break;
            }
        }
    }

    /**
     * 번호가 없을시 Toast message
     * 번호 입력 Edittext 수정가능하도록 set
     */
    public void setPhoneNumber() {
        mMarketSellCreateEditTextPhoneNum.setTextIsSelectable(true);
        mMarketSellCreateEditTextPhoneNum.setClickable(true);
        if (mPhoneNumber == null) {
            ToastUtil.getInstance().makeShortToast("휴대폰 번호를 기입해주세요");
            return;
        }
        mMarketSellCreateEditTextPhoneNum.setText(mPhoneNumber);
    }

    /**
     * 번호 입력 Edittext 수정 못하도록 set
     */

    public void unSetPhoneNumber() {
        mPhoneNumber = mMarketSellCreateEditTextPhoneNum.getText().toString();
        mMarketSellCreateEditTextPhoneNum.setText(null);
        mMarketSellCreateEditTextPhoneNum.setTextIsSelectable(false);
        mMarketSellCreateEditTextPhoneNum.setClickable(false);
        hideKeyboard(this);
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * 숫자가 처음에 0원 일 경우 0을 지우고 입력한 숫자로 set
     * 변경된 숫자는 changeMoneyFormatStringWithComma() method를 통해서 ','를 붙여줌
     * 숫자는 매 변경시 changeMoneyFormatStringWithoutComma() method를 통해서 ','를 제외하고 값 저장
     */
    public void moneyEditTextChangedListener() {
        mMarketSellCreateMoneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (!text.equals(changeMoneyFormatToStringWithComma(mPrice))) {
                    if (text.length() == 1 && text.charAt(0) == '0')
                        mMarketSellCreateMoneyEditText.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mMarketSellCreateMoneyEditText.getText().toString().equals(changeMoneyFormatToStringWithComma(mPrice))) {

                    if (mMarketSellCreateMoneyEditText.getText().toString().length() != 0) {
                        mPrice = changeMoneyFormatToStringWithoutComma(mMarketSellCreateMoneyEditText.getText().toString());
                        mMarketSellCreateMoneyEditText.setText(changeMoneyFormatToStringWithComma(mPrice));
                    } else {
                        mPrice = "0";
                        mMarketSellCreateMoneyEditText.setText(changeMoneyFormatToStringWithComma(mPrice));
                    }

                    mPrice = changeMoneyFormatToStringWithoutComma((mMarketSellCreateMoneyEditText.getText().toString()));
                    mMarketSellCreateMoneyEditText.setSelection(mMarketSellCreateMoneyEditText.getText().length());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_GET_GALLERY:
                //기존 갤러리에서 선택한 이미지 파일
                Uri getImagePath = data.getData();
                File oriFile = getImageFile(getImagePath);

                //이미지 편집을 위해 선택한 이미지를 저장할 파일
                mCurrentPhotoPath = data.getData();

                File copyFile = new File(mCurrentPhotoPath.getPath());

                //이미지 복사(이미지 편집시 원본 이미지가 변형되는것을 방지하기 위함)
                createTempFile(copyFile, oriFile);

                //이미지 편집 호출
                cropImage();

                break;

            case REQUEST_TAKE_PHOTO:
                cropImage();
                MediaScannerConnection.scanFile(this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                        new String[]{mCurrentPhotoPath.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                break;

            case REQUEST_CROP_IMAGE:

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCurrentPhotoPath);
                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 500, 500);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    OutputStream os;
                    thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축
                    os = new FileOutputStream(mImageFile);
                    thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                    // Glide.with(mContext).asBitmap().load(bitmap).into(mMarketSellEditThumbnailImageView);
                    mMarketUsedCreatePresenter.uploadThumbnailImage(mImageFile);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }


                break;

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "koin" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures/koin/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    /**
     * 파일 복사
     *
     * @param oriFile  : 복사할 File
     * @param copyFile : 복사될 File
     * @return
     */
    public boolean createTempFile(File copyFile, File oriFile) {
        boolean result = true;
        try {
            InputStream inputStream = new FileInputStream(oriFile);
            OutputStream outputStream = new FileOutputStream(copyFile);

            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            result = false;
        } catch (IOException e) {
            result = false;
        }
        return result;
    }


    /**
     * 이미지 편집
     */
    private void cropImage() {

        this.grantUriPermission("com.android.camera", mCurrentPhotoPath,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mCurrentPhotoPath, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, mCurrentPhotoPath,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ToastUtil.getInstance().makeShortToast("용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/Pictures/koin");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            mImageFile = tempFile;
            mCurrentPhotoPath = FileProvider.getUriForFile(this,
                    "in.koreatech.koin.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, mCurrentPhotoPath,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, REQUEST_CROP_IMAGE);


        }
    }

    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     *
     * @param uri
     * @return
     */
    public File getImageFile(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if (mCursor == null || mCursor.getCount() < 1) {
            return null;
        }

        int idxColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(idxColumn);

        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        return new File(path);

    }


    public void getPermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE);
        }

    }

    public boolean checkCameraPermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                return false;
            else
                return true;
        } else
            return true;
    }

    public boolean checkStoragePermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED))
                return false;
            else
                return true;
        } else
            return true;
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

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mMarketSellCreateContent.getText().toString().trim());
        mPhoneNumber = mMarketSellCreateEditTextPhoneNum.getText().toString().trim();

        if (mIsPhoneOpen) {
            mMarketItem.isPhoneOpen = 1;
            mMarketItem.phone = mPhoneNumber;
        } else {
            mMarketItem.isPhoneOpen = 0;
            mMarketItem.phone = null;
        }
        mMarketItem.type = SELL_CODE;
        mMarketItem.title = mMarketSellCreateTitleEditText.getText().toString().trim();
        mMarketItem.price = Integer.parseInt(mPrice);

        if (mMarketSellCreateContent.getText().toString().trim().length() != 0) {
            mMarketItem.content = Html.toHtml(spannableStringBuilder).trim();
            mIsContentCheck = true;
        } else {
            mIsContentCheck = false;
        }

        mMarketItem.state = mItemState;
        if (mIsPhoneOpen && (mMarketItem.phone.length() == 13)) {
            if (mPhoneNumber.charAt(3) == '-' && mPhoneNumber.charAt(8) == '-')
                mIsPhoneCheck = true;
            else
                mIsPhoneCheck = false;

        } else if (!mIsPhoneOpen)
            mIsPhoneCheck = true;
        else
            mIsPhoneCheck = false;

        if (mMarketItem.title.length() == 0)
            mIsTitlecheck = false;
        else
            mIsTitlecheck = true;


        if (mIsContentCheck && mIsTitlecheck && !mIsPhoneCheck)
            ToastUtil.getInstance().makeShortToast(R.string.market_used_phone_check);
        if (!mIsTitlecheck && mIsContentCheck)
            ToastUtil.getInstance().makeShortToast(R.string.market_used_title_check);
        if (!mIsContentCheck && mIsTitlecheck)
            ToastUtil.getInstance().makeShortToast(R.string.market_used_content_check);
        if (!mIsTitlecheck && !mIsContentCheck)
            ToastUtil.getInstance().makeShortToast(R.string.market_used_title_content_check);

        if (mIsPhoneCheck && mIsTitlecheck && mIsContentCheck)
            mMarketUsedCreatePresenter.createMarketItem(mMarketItem);
    }

    @Override
    public void showMarketCreatedSuccess(Item item) {
        Intent intent = new Intent(this, MarketUsedSellDetailActivity.class);
        intent.putExtra("MARKET_ID", SELL_CODE);
        intent.putExtra("ITEM_ID", item.id);
        intent.putExtra("GRANT_CHECK", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMarketCreatefFail() {
        ToastUtil.getInstance().makeShortToast(R.string.server_failed);
    }

    @Override
    public void setPresenter(MarketUsedCreateContract.Presenter presenter) {
        this.mMarketUsedCreatePresenter = presenter;
    }


    @Override
    public void showImageUploadSuccess(String url) {
        mMarketSellCreateThumbnailImageView.setVisibility(View.VISIBLE);
        Glide.with(mContext).asBitmap().load(url).into(mMarketSellCreateThumbnailImageView);
        mMarketItem.thumbnail = url;
    }

    @Override
    public void showImageUploadFail() {

    }
}
