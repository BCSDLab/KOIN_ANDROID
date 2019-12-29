package in.koreatech.koin.core.bases;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.AttrRes;
import com.google.android.material.appbar.AppBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import android.util.AttributeSet;


import in.koreatech.koin.core.R;

/**
 * Created by yunjae on 2019. 3. 17....
 *
 */
public class KoinBaseAppbarDark extends AppBarLayout {
    public AppBarLayout background;
    public TextView leftButton;
    public TextView rightButton;
    public TextView title;

    final Typeface textFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/notosans_medium.ttf");
    public OnClickListener onClickListener;


    public KoinBaseAppbarDark(Context context) {
        super(context);
        init();
    }

    public KoinBaseAppbarDark(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttribute(attrs, context);
    }

    public KoinBaseAppbarDark(Context context, AttributeSet attrs, int defStyle) {
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
        View view = inflate(getContext(), R.layout.base_appbar_dark, null);
        addView(view);
        background = (AppBarLayout) findViewById(R.id.base_appbar_dark);
        leftButton = (TextView) findViewById(R.id.base_appbar_dark_left_button);
        rightButton = (TextView) findViewById(R.id.base_appbar_dark_right_button);
        title = (TextView) findViewById(R.id.base_appbar_dark_title);

        title.setTypeface(textFont);
        leftButton.setTypeface(textFont);
        rightButton.setTypeface(textFont);
    }

    public void getAttribute(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KoinBaseAppbarDark);
        setTypeArray(typedArray);
    }

    public void getAttribute(AttributeSet attrs, Context context, @AttrRes int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KoinBaseAppbarDark, defStyle, 0);
        setTypeArray(typedArray);
    }

    public void setTypeArray(TypedArray typedArray) {
        int backgroundColor = typedArray.getColor(R.styleable.KoinBaseAppbarDark_background_color, getResources().getColor(R.color.light_navy));
        int titleTextColor = typedArray.getColor(R.styleable.KoinBaseAppbarDark_title_text_color, getResources().getColor(R.color.white));
        String titleText = typedArray.getString(R.styleable.KoinBaseAppbarDark_title_text);
        int titleVisibility = typedArray.getInt(R.styleable.KoinBaseAppbarDark_title_visibility, View.VISIBLE);
        Drawable leftButtonBackground = typedArray.getDrawable(R.styleable.KoinBaseAppbarDark_left_button_background);
        int leftButtonColor = typedArray.getColor(R.styleable.KoinBaseAppbarDark_left_button_text_color, getResources().getColor(R.color.white));
        String leftButtonString = typedArray.getString(R.styleable.KoinBaseAppbarDark_left_button_text);
        int leftButtonVisibility = typedArray.getInt(R.styleable.KoinBaseAppbarDark_left_button_visibility, View.VISIBLE);
        int rightButtonColor = typedArray.getColor(R.styleable.KoinBaseAppbarDark_right_button_text_color, getResources().getColor(R.color.white));
        Drawable rightButtonBackground = typedArray.getDrawable(R.styleable.KoinBaseAppbarDark_right_button_background);
        String rightButtonString = typedArray.getString(R.styleable.KoinBaseAppbarDark_right_button_text);
        int rightButtonVisibility = typedArray.getInt(R.styleable.KoinBaseAppbarDark_right_button_visibility, View.VISIBLE);
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
}
