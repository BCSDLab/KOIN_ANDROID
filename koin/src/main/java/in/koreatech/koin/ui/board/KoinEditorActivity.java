package in.koreatech.koin.ui.board;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorControl;
import com.github.irshulx.models.EditorTextStyle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import in.koreatech.koin.R;
import in.koreatech.koin.core.toast.ToastUtil;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.util.ImageUtil;
import in.koreatech.koin.util.SnackbarUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import top.defaults.colorpicker.ColorPickerPopup;

public abstract class KoinEditorActivity extends KoinNavigationDrawerActivity {
    private Context context;
    private KoinRichEditor richEditor;

    private HashMap<String, Boolean> uploadImageHashMap;             // 갤러리에서 불러온 이미지의 HashMap
    private HashMap<String, Boolean> isUploadImageCompeleteHashMap;  // 압축 과정을 완료한 이미지의 HashMap
    private ArrayList<String> uploadedImageUrlList; // 업로드 완료된 이미지들의 URL 목록

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        richEditor = findViewById(getRichEditorId());
        uploadImageHashMap = new HashMap<>();
        isUploadImageCompeleteHashMap = new HashMap<>();
        uploadedImageUrlList = new ArrayList<>();

        richEditor.setEditorImageLayout(R.layout.rich_editor_image_layout);

        init();
    }

    abstract protected int getRichEditorId();

    abstract protected boolean isEditable();

    abstract protected void successImageProcessing(File imageFile, String uuid);

    private void init() {
        if (isEditable()) {
            findViewById(R.id.action_h1).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.H1));
            findViewById(R.id.action_h2).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.H2));
            findViewById(R.id.action_h3).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.H3));
            findViewById(R.id.action_bold).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.BOLD));
            findViewById(R.id.action_Italic).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.ITALIC));
            findViewById(R.id.action_indent).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.INDENT));
            findViewById(R.id.action_outdent).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.OUTDENT));
            findViewById(R.id.action_bulleted).setOnClickListener(v -> richEditor.insertList(false));
            findViewById(R.id.action_unordered_numbered).setOnClickListener(v -> richEditor.insertList(true));
            findViewById(R.id.action_hr).setOnClickListener(v -> richEditor.insertDivider());
            findViewById(R.id.action_blockquote).setOnClickListener(v -> richEditor.updateTextStyle(EditorTextStyle.BLOCKQUOTE));
            findViewById(R.id.action_color).setOnClickListener(v -> createColorPicker());
            findViewById(R.id.action_erase).setOnClickListener(v -> SnackbarUtil.makeLongSnackbarActionYes(richEditor, getString(R.string.erase_button_pressed), this::eraseEditorText));
            findViewById(R.id.action_insert_link).setOnClickListener(v -> {
                createURLCreateDialog();
            });
            findViewById(R.id.action_insert_image).setOnClickListener(v -> {
                richEditor.openImagePicker();

                richEditor.setOnCancelListener(view -> {
                    String imageTag = ((EditorControl) ((RelativeLayout) view.getParent()).getTag()).path;  // "url" 값 반환
                    Log.d("Editor", imageTag);
                    uploadedImageUrlList.remove(imageTag);
                    uploadImageHashMap.put(imageTag, false);
                    richEditor.onImageUploadFailed(imageTag);
                    isUploadImageCompeleteHashMap.remove(imageTag);
                    richEditor.getParentView().removeView(((RelativeLayout) view.getParent()));
                });
            });

            richEditor.setEditorListener(new EditorListener() {
                @Override
                public void onTextChanged(EditText editText, Editable text) {
                    // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
                }

                // 갤러리에서 이미지를 선택하고 호출되는 insertImage() 메소드 안에서 인자값을 넘겨받는 메소드
                @Override
                public void onUpload(Bitmap bitmap, String uuid) {
                    uploadImageHashMap.put(uuid, true);
                    isUploadImageCompeleteHashMap.put(uuid, false);
                    Observable.defer(() -> {
                        File imageFile = ImageUtil.changeBMPtoFILE(bitmap, uuid, 1, context);
                        return Observable.just(imageFile);
                    })
                            .subscribeOn(Schedulers.io())
                            .subscribe(imageFile -> {
                                if (uploadImageHashMap.containsKey(uuid) && uploadImageHashMap.get(uuid)) {
                                    successImageProcessing(imageFile, uuid);
                                    isUploadImageCompeleteHashMap.put(uuid, true);
                                }
                            }, throwable -> {
                                new Handler().post(() -> {
                                    richEditor.onImageUploadFailed(uuid);
                                    ToastUtil.getInstance().makeLong(R.string.fail_upload);
                                    isUploadImageCompeleteHashMap.put(uuid, true);
                                });
                            });
                }

                @Override
                public View onRenderMacro(String name, Map<String, Object> props, int index) {
                    return null;
                }
            });
        }

        renderEditor();
    }

    public void onImageUploadComplete(String url, String uploadImageId) {
        try {
            uploadedImageUrlList.add(url);
            richEditor.onImageUploadComplete(url, uploadImageId);
        } catch (Exception e) {
            //ToastUtil.makeShortToast(mContext, R.string.fail_upload);
        }
    }

    public void onImageUploadFailed(String uploadImageId) {
        richEditor.onImageUploadFailed(uploadImageId);

    }

    public String getContent() {
        return richEditor.getContent().toString().trim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == richEditor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                richEditor.insertImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }
    }

    public void eraseEditorText() {
        richEditor.clearAllContents();
    }

    private void createColorPicker() {
        new ColorPickerPopup.Builder(this)
                .initialColor(Color.RED) // 색 초기화
                .enableAlpha(true)
                .okTitle("선택")
                .cancelTitle("취소")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        richEditor.updateTextColor(colorHex(color));
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {
                    }
                });
    }

    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }

    private void createURLCreateDialog() {
        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(this);
        inputAlert.setTitle("링크 추가");
        final EditText userInput = new EditText(this);
        userInput.setHint("주소를 입력해주세요.");
        userInput.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
        inputAlert.setView(userInput);
        inputAlert.setPositiveButton("삽입", (dialog, which) -> {
            String userInputValue = userInput.getText().toString();
            richEditor.insertLink(checkUrl(userInputValue));
        });
        inputAlert.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();
    }

    private String checkUrl(String url) {
        String patter = "^(http|https|ftp)://.*$";
        if (url.matches(patter)) {
            return url;
        } else {
            return "http://" + url;
        }
    }

    public String renderHtmltoString(String url) {
        if (url == null) return "";

        return url.replace("<div>", " ").replace("<div/>", " ").replace("<p><img", "<img");
    }

    public void renderEditor(String content) {
        richEditor.render(content);
    }

    public void renderEditor() {
        richEditor.render();
    }

    public String getContentAsHTML() {
        return richEditor.getContentAsHTML();
    }

    public boolean isImageAllUploaded() {
        for (Boolean isImageUploading : isUploadImageCompeleteHashMap.values()) {
            if (!isImageUploading) {
                return false;
            }
        }
        return true;
    }

    /**
     * 업로드된 이미지들 중 썸네일 이미지를 반환하는 메소드
     * @return 업로드된 이미지 리스트 중 첫번째 이미지
     */
    public String getThumbnail() {
        String html = richEditor.getContentAsHTML();
        Document doc = Jsoup.parseBodyFragment(html);
        Elements img = doc.getElementsByTag("img");
        String thumbnail = null;
        if(img.size() > 0) {
            thumbnail = img.get(0).attr("src");
        }

        return thumbnail;
    }
}
