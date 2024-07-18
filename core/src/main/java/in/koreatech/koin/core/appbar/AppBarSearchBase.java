package in.koreatech.koin.core.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.AttrRes;

import com.google.android.material.appbar.AppBarLayout;

import in.koreatech.koin.core.R;
import in.koreatech.koin.core.util.FontManager;

public class AppBarSearchBase extends AppBarLayout {
    public AppBarLayout background;
    public TextView leftButton;
    public TextView rightButton;
    public EditText title;
    public Button eraseButton;
    private SearchTextChange searchTextChange;
    private SearchEditorAction searchEditorAction;

    final Typeface textFont = FontManager.INSTANCE.getTypeface(getContext(), FontManager.KoinFontType.PRETENDARD_MEDIUM);
    public OnClickListener onClickListener;


    public AppBarSearchBase(Context context) {
        super(context);
        init();
    }

    public AppBarSearchBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttribute(attrs, context);
    }

    public AppBarSearchBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
        getAttribute(attrs, context, defStyle);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        if (onClickListener == null) return;
        this.onClickListener = onClickListener;
        background.setOnClickListener(onClickListener);
        leftButton.setOnClickListener(onClickListener);
        rightButton.setOnClickListener(onClickListener);
        title.setOnClickListener(onClickListener);
    }


    public void init() {
        View view = inflate(getContext(), R.layout.search_appbar_dark, null);
        addView(view);
        background = findViewById(R.id.base_appbar_dark);
        leftButton = findViewById(R.id.base_appbar_dark_left_button);
        rightButton = findViewById(R.id.base_appbar_dark_right_button);
        title = findViewById(R.id.base_appbar_dark_title);
        eraseButton = findViewById(R.id.base_appbar_dark_erase_button);

        title.setTypeface(textFont);
        leftButton.setTypeface(textFont);
        rightButton.setTypeface(textFont);
        title.getBackground().setColorFilter(getContext().getResources().getColor(R.color.blue1), PorterDuff.Mode.SRC_ATOP);
        eraseButton.setOnClickListener(v -> {
            title.getText().clear();
        });
    }

    public void getAttribute(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarBase);
        setTypeArray(typedArray);
    }

    public void getAttribute(AttributeSet attrs, Context context, @AttrRes int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarBase, defStyle, 0);
        setTypeArray(typedArray);
    }

    public void setTypeArray(TypedArray typedArray) {
        int backgroundColor = typedArray.getColor(R.styleable.AppBarBase_backgroundColor, getResources().getColor(R.color.colorPrimary));
        int titleTextColor = typedArray.getColor(R.styleable.AppBarBase_titleTextColor, getResources().getColor(R.color.white));
        String titleText = typedArray.getString(R.styleable.AppBarBase_titleText);
        int titleVisibility = typedArray.getInt(R.styleable.AppBarBase_titleVisibility, View.VISIBLE);
        Drawable leftButtonBackground = typedArray.getDrawable(R.styleable.AppBarBase_leftButtonBackground);
        int leftButtonColor = typedArray.getColor(R.styleable.AppBarBase_leftButtonTextColor, getResources().getColor(R.color.white));
        String leftButtonString = typedArray.getString(R.styleable.AppBarBase_leftButtonText);
        int leftButtonVisibility = typedArray.getInt(R.styleable.AppBarBase_leftButtonVisibility, View.VISIBLE);
        int rightButtonColor = typedArray.getColor(R.styleable.AppBarBase_rightButtonTextColor, getResources().getColor(R.color.white));
        Drawable rightButtonBackground = typedArray.getDrawable(R.styleable.AppBarBase_rightButtonBackground);
        String rightButtonString = typedArray.getString(R.styleable.AppBarBase_rightButtonText);
        int rightButtonVisibility = typedArray.getInt(R.styleable.AppBarBase_rightButtonVisibility, View.VISIBLE);
        /* background */
        background.setBackgroundColor(backgroundColor);
        /* title */
        title.setTextColor(titleTextColor);
        title.setText(titleText);
        title.setVisibility(titleVisibility);
        /* leftButton */
        leftButton.setTextColor(leftButtonColor);
        leftButton.setBackground(leftButtonBackground);
        leftButton.setText(leftButtonString);
        leftButton.setVisibility(leftButtonVisibility);
        /* rightButton */
        rightButton.setTextColor(rightButtonColor);
        rightButton.setBackground(rightButtonBackground);
        rightButton.setText(rightButtonString);
        rightButton.setVisibility(rightButtonVisibility);

        typedArray.recycle();
    }

    public static int getTitleId() {
        return R.id.base_appbar_dark_title;
    }

    public static int getLeftButtonId() {
        return R.id.base_appbar_dark_left_button;
    }

    public static int getRightButtonId() {
        return R.id.base_appbar_dark_right_button;
    }

    public void setBackgroundColor(int backgroundColor) {
        background.setBackgroundColor(backgroundColor);
    }

    public void setTitleTextColor(int titleTextColor) {
        title.setTextColor(titleTextColor);
    }

    public void setTitleText(String titleText) {
        title.setText(titleText);
    }

    public void setTitleVisibility(int titleVisibility) {
        title.setVisibility(titleVisibility);
    }

    public void setLeftButtonTextColor(int leftButtonColor) {
        rightButton.setTextColor(leftButtonColor);
    }

    public void setLeftButtonDrawalble(Drawable leftButtonBackground) {
        leftButton.setBackground(leftButtonBackground);
    }

    public void setLeftButtonText(String leftButtonString) {
        leftButton.setText(leftButtonString);
    }

    public void setLeftButtonVisibility(int leftButtonVisibility) {
        leftButton.setVisibility(leftButtonVisibility);
    }

    public void setRightButtonTextColor(int rightButtonColor) {
        rightButton.setTextColor(rightButtonColor);
    }

    public void setRightButtonDrawalble(Drawable rightButtonBackground) {
        rightButton.setBackground(rightButtonBackground);
    }

    public void setRightButtonText(String rightButtonString) {
        rightButton.setText(rightButtonString);
    }

    public void setRightButtonVisibility(int rightButtonVisibility) {
        rightButton.setVisibility(rightButtonVisibility);
    }

    public String getText() {
        return title.getText().toString();

    }

    public void setText(String string) {
        if (string != null)
            title.setText(string);

    }

    public void setOnTextChange(SearchTextChange searchTextChange) {
        this.searchTextChange = searchTextChange;
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchTextChange.getString(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setSearchEditorAction(SearchEditorAction searchEditorAction) {
        this.searchEditorAction = searchEditorAction;
        title.setOnEditorActionListener((v, actionId, event) -> this.searchEditorAction.onEditorAction(v, actionId, event));
    }

    public void clearText() {
        title.getText().clear();
    }

    public interface SearchTextChange {
        void getString(String text);
    }

    public interface SearchEditorAction {
        boolean onEditorAction(TextView v, int actionId, KeyEvent event);
    }
}