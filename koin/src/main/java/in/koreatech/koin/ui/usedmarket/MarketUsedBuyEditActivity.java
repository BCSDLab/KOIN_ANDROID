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

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.text.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;
import in.koreatech.koin.core.progressdialog.CustomProgressDialog;
import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditContract;
import in.koreatech.koin.data.network.entity.Item;
import in.koreatech.koin.data.network.entity.MarketItem;
import in.koreatech.koin.data.network.interactor.MarketUsedRestInteractor;
import in.koreatech.koin.ui.usedmarket.presenter.MarketUsedEditPresenter;
import in.koreatech.koin.core.toast.ToastUtil;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * This class edit item at MarketUsedBuyEdit
 * if the item edit success activity will be finished and go back to MarketUsedBuyDetailActivity
 *
 * @author Yunjae Na
 * @see MarketUsedBuyDetailActivity
 * @since 2018. 09.15
 */
public class MarketUsedBuyEditActivity extends KoinNavigationDrawerActivity implements MarketUsedEditContract.View {
    private final String TAG = "MarketUsedBuyEditActivity";
    private final int MAXTITLELENGTH = 39;
    private static final int MY_REQUEST_CODE = 100;
    private static final int REQUEST_GET_GALLERY = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_CROP_IMAGE = 3;
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
    private MarketUsedEditContract.Presenter mMarketUsedEditPresenter;
    private Uri currentPhotoPath;
    private File mImageFile;
    private CustomProgressDialog customProgressDialog;
    private boolean mIsTitlecheck;
    private boolean mIsPhoneCheck;
    private boolean mIsContentCheck;


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
    EditText mMarketBuyEditEditTextPhoneNum;


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.market_used_buy_edit_activity);
        ButterKnife.bind(this);

        this.marketId = getIntent().getIntExtra("MARKET_ID", -1);
        this.itemId = getIntent().getIntExtra("ITEM_ID", -1);
        this.title = getIntent().getStringExtra("MARKET_TITLE");
        content = getIntent().getStringExtra("MARKET_CONTENT");
        this.price = Integer.toString(getIntent().getIntExtra("MARKET_PRICE", 0));
        this.phoneNumber = getIntent().getStringExtra("MARKET_PHONE");
        this.isPhoneOpen = getIntent().getBooleanExtra("MARKET_PHONE_STATUS", false);
        this.itemState = getIntent().getIntExtra("MARKET_STATE", 0);
        this.thumbNail = getIntent().getStringExtra("MARKET_THUMBNAIL_URL");
        mImageFile = null;

        if (this.phoneNumber == null) {
            this.phoneNumber = UserInfoSharedPreferencesHelper.getInstance().loadUser().phoneNumber;
        }

        init();


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

    void init() {
        getPermisson();
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


        mMarketBuyEditEditTextPhoneNum.setFocusable(false);
        mMarketBuyEditEditTextPhoneNum.setClickable(false);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article_edited, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @OnClick(R.id.koin_base_app_bar_dark)
    public void onClickedBaseAppbar(View v) {
        int id = v.getId();
        if (id == AppbarBase.getLeftButtonId())
            finish();
        else if (id == AppbarBase.getRightButtonId())
            onClickEditButton();
    }


    @OnClick(R.id.market_used_buy_edit_thumbnail_change_button)
    void onThumbnailChangeButtonClick() {
        getPermisson();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (checkCameraPermisson() && checkStoragePermisson()) {
            builder.setMessage("이미지를 불러올 방법을 골라주세요.");
            builder.setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    File photoFile = null;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.

                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        Toast.makeText(context, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    if (photoFile != null) {
                        currentPhotoPath = FileProvider.getUriForFile(context,
                                "in.koreatech.koin.provider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoPath); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
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
            ToastUtil.getInstance().makeShort("기능 사용을 위한 권한 동의가 필요합니다.");

    }


    @OnCheckedChanged({R.id.market_used_buy_edit_is_phone_public_radiobutton, R.id.market_used_buy_edit_is_phone_private_radiobutton})
    public void onPhoneRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.market_used_buy_edit_is_phone_public_radiobutton:
                    setPhoneNumber();
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
        mMarketBuyEditEditTextPhoneNum.setTextIsSelectable(true);
        mMarketBuyEditEditTextPhoneNum.setClickable(true);
        if (this.phoneNumber == null)
            ToastUtil.getInstance().makeShort("휴대폰 번호를 기입해주세요");

        mMarketBuyEditEditTextPhoneNum.setText(this.phoneNumber);
    }

    /**
     * 번호 입력 Edittext 수정 못하도록 set
     */
    public void unSetPhoneNumber() {
        this.phoneNumber = mMarketBuyEditEditTextPhoneNum.getText().toString();
        mMarketBuyEditEditTextPhoneNum.setText(null);
        mMarketBuyEditEditTextPhoneNum.setTextIsSelectable(false);
        mMarketBuyEditEditTextPhoneNum.setClickable(false);
        hideKeyboard(this);
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_GET_GALLERY:
                //기존 갤러리에서 선택한 이미지 파일
                Uri getImagePath = data.getData();
                File oriFile = getImageFile(getImagePath);

                //이미지 편집을 위해 선택한 이미지를 저장할 파일
                currentPhotoPath = data.getData();

                File copyFile = new File(currentPhotoPath.getPath());

                //이미지 복사(이미지 편집시 원본 이미지가 변형되는것을 방지하기 위함)
                createTempFile(copyFile, oriFile);

                //이미지 편집 호출
                cropImage();

                break;

            case REQUEST_TAKE_PHOTO:
                cropImage();
                MediaScannerConnection.scanFile(this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                        new String[]{currentPhotoPath.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                break;

            case REQUEST_CROP_IMAGE:

                try {


                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentPhotoPath);
                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 500, 500);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    OutputStream os;
                    thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축
                    os = new FileOutputStream(mImageFile);
                    thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                    // Glide.with(context).asBitmap().load(bitmap).into(mMarketBuyEditThumbnailImageView);
                    mMarketUsedEditPresenter.uploadThumbnailImage(mImageFile);
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

        this.grantUriPermission("com.android.camera", currentPhotoPath,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(currentPhotoPath, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, currentPhotoPath,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ToastUtil.getInstance().makeShort("용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.");
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
            currentPhotoPath = FileProvider.getUriForFile(this,
                    "in.koreatech.koin.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoPath);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, currentPhotoPath,
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

        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if (cursor == null || cursor.getCount() < 1) {
            return null;
        }

        int idxColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        String path = cursor.getString(idxColumn);

        if (cursor != null) {
            cursor.close();
            cursor = null;
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
    public void onClickEditButton() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(mMarketBuyEditContent.getText().toString().trim());
        this.phoneNumber = mMarketBuyEditEditTextPhoneNum.getText().toString().trim();


        if (this.isPhoneOpen) {
            this.marketItem.isPhoneOpen = 1;
            this.marketItem.phone = this.phoneNumber;
        } else {
            this.marketItem.isPhoneOpen = 0;
            this.marketItem.phone = null;
        }

        this.marketItem.title = mMarketBuyEditTitleEditText.getText().toString().trim();
        this.marketItem.price = Integer.parseInt(this.price);
        this.marketItem.state = this.itemState;

        if (mMarketBuyEditContent.getText().toString().trim().length() != 0) {
            this.marketItem.content = Html.toHtml(spannableStringBuilder);
            this.marketItem.content = addImageSpan(this.marketItem.content, this.imageUrl);
            mIsContentCheck = true;
        } else {
            mIsContentCheck = false;
        }


        if (this.isPhoneOpen && (this.marketItem.phone.length() == 13)) {
            if (this.phoneNumber.charAt(3) == '-' && this.phoneNumber.charAt(8) == '-')
                mIsPhoneCheck = true;
            else
                mIsPhoneCheck = false;
        } else if (!this.isPhoneOpen)
            mIsPhoneCheck = true;
        else
            mIsPhoneCheck = false;

        if (this.marketItem.title.length() == 0)
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
        this.marketItem.type = this.marketId;
        if (mIsPhoneCheck && mIsTitlecheck && mIsContentCheck)
            mMarketUsedEditPresenter.editMarketContent(this.itemId, this.marketItem);

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
        finish();
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
        this.marketItem.thumbnail = url;
    }

    @Override
    public void showImageUploadFail() {
        ToastUtil.getInstance().makeShort("이미지의 크기가 너무 큽니다.");
    }

    @Override
    public void setPresenter(MarketUsedEditContract.Presenter presenter) {
        this.mMarketUsedEditPresenter = presenter;
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
