package in.koreatech.koin;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorTextStyle;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.koreatech.koin.service_board.ui.KoinRichEditor;
import top.defaults.colorpicker.ColorPickerPopup;

public class KoinEditorActivity extends KoinNavigationDrawerActivity {
    @BindView(R.id.article_editor_content)
    KoinRichEditor richEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_edit);
        ButterKnife.bind(this);
    }

    private void init() {
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
        findViewById(R.id.action_insert_link).setOnClickListener(v -> {
            createURLCreateDialog();
        });
        findViewById(R.id.action_insert_image).setOnClickListener(v -> {
            richEditor.openImagePicker();
        });
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

}
