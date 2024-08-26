package in.koreatech.koin.core.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.AttrRes;

import com.google.android.material.appbar.AppBarLayout;

import android.view.View;
import android.widget.TextView;


import android.util.AttributeSet;


import in.koreatech.koin.core.R;
import in.koreatech.koin.core.analytics.EventAction;
import in.koreatech.koin.core.analytics.EventLogger;
import in.koreatech.koin.core.constant.AnalyticsConstant;
import in.koreatech.koin.core.util.FontManager;


public class AppBarBase extends AppBarLayout {
    public AppBarLayout background;
    public TextView leftButton;
    public TextView rightButton;
    public TextView title;
    final Typeface textFont = FontManager.getTypeface(getContext(), FontManager.KoinFontType.PRETENDARD_MEDIUM);
    public OnClickListener onClickListener;

    public AppBarBase(Context context) {
        super(context);
        init();
    }

    public AppBarBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttribute(attrs, context);
    }

    public AppBarBase(Context context, AttributeSet attrs, int defStyle) {
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
        background = findViewById(R.id.base_appbar_dark);
        leftButton = findViewById(R.id.base_appbar_dark_left_button);
        rightButton = findViewById(R.id.base_appbar_dark_right_button);
        title = findViewById(R.id.base_appbar_dark_title);
        title.setTypeface(textFont);
        leftButton.setTypeface(textFont);
        rightButton.setTypeface(textFont);
    }

    public void getAttribute(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarBase);
        setTypeArray(typedArray);
    }

    public void getAttribute(AttributeSet attrs, Context context, @AttrRes int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarBase, defStyle, 0);
        setTypeArray(typedArray);

    }

    private void setTypeArray(TypedArray typedArray) {
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
        int leftButtonHeight = typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1);
        int rightButtonHeight = typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1);
        int leftButtonWidth = typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1);
        int rightButtonWidth = typedArray.getDimensionPixelSize(R.styleable.AppBarBase_leftButtonHeight, -1);

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
        if (leftButtonHeight != -1 || leftButtonWidth != -1) {
            leftButton.setHeight(leftButtonHeight);
            leftButton.setWidth(leftButtonWidth);
        }
        /* rightButton */
        rightButton.setTextColor(rightButtonColor);
        rightButton.setBackground(rightButtonBackground);
        rightButton.setText(rightButtonString);
        rightButton.setVisibility(rightButtonVisibility);
        if (leftButtonHeight != -1 || leftButtonWidth != -1) {
            rightButton.setHeight(rightButtonHeight);
            rightButton.setWidth(rightButtonWidth);
        }

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
