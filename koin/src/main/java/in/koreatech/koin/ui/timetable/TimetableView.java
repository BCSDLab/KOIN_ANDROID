package in.koreatech.koin.ui.timetable;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.koreatech.koin.R;
import in.koreatech.koin.data.network.entity.Lecture;
import in.koreatech.koin.data.network.entity.TimeTable;
import in.koreatech.koin.data.network.entity.TimeTable.TimeTableItem;
import in.koreatech.koin.util.CustomTypefaceSpan;
import in.koreatech.koin.util.SaveManager;
import in.koreatech.koin.util.SeparateTime;
import in.koreatech.koin.util.Time;

import static in.koreatech.koin.util.LectureFilterUtil.CheckClikckedUtil;
import static java.lang.String.format;

public class TimetableView extends LinearLayout {
    private static final int DEFAULT_ROW_COUNT = 12;
    private static final int DEFAULT_COLUMN_COUNT = 6;
    private static final int DEFAULT_CELL_HEIGHT_DP = 50;
    private static final int DEFAULT_SIDE_CELL_WIDTH_DP = 60;
    private static final int DEFAULT_START_TIME = 9;

    private static final int DEFAULT_SIDE_HEADER_FONT_SIZE_DP = 9;
    private static final int DEFAULT_HEADER_FONT_SIZE_DP = 11;
    private static final int DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP = 9;
    private static final int DEFAULT_STICKER_FONT_SIZE_DP = 9;
    private static final int DEFAULT_MARGINE_BOTTOM_DP = 0;

    private static final String CLASS_TITLE_fontName = "fonts/notosanscjkkr_medium.otf";
    private static final String CLASS_LECTURE_fontName = "fonts/notosans_regular.ttf";

    private int rowCount;
    private int columnCount;
    private int cellHeight;
    private int sideCellWidth;
    private int headerHeight;
    private String[] headerTitle;
    private String[] stickerColors;
    private int startTime;
    private int headerHighlightColor;
    private int marginBottom;
    private boolean isTimebarActivate;

    private RelativeLayout stickerBox;
    private String now;
    Date date;

    TableLayout tableHeader;
    TableLayout tableBox;
    NestedScrollView scrollView;


    private Context context;


    TimeTable timeTable = new TimeTable();
    TimeTable checkStickers = new TimeTable();
    ArrayList<String> titleArrayList = new ArrayList<>();
    ViewGroup.MarginLayoutParams timebarMarginParams;
    private int stickerCount = -1;
    private int stickerCheckCount = -1;

    private OnStickerSelectedListener stickerSelectedListener = null;

    public TimetableView(Context context) {
        super(context, null);
        this.context = context;
    }

    public TimetableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimetableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimetableView);
        rowCount = a.getInt(R.styleable.TimetableView_rowCount, DEFAULT_ROW_COUNT) - 1;
        columnCount = a.getInt(R.styleable.TimetableView_columnCount, DEFAULT_COLUMN_COUNT);
        cellHeight = a.getDimensionPixelSize(R.styleable.TimetableView_cellHeight, dp2Px(DEFAULT_CELL_HEIGHT_DP));
        sideCellWidth = a.getDimensionPixelSize(R.styleable.TimetableView_sideCellWidth, dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP));
        int titlesId = a.getResourceId(R.styleable.TimetableView_headerTitle, R.array.default_headerTitle);
        headerTitle = a.getResources().getStringArray(titlesId);
        int colorsId = a.getResourceId(R.styleable.TimetableView_stickerColors, R.array.default_sticker_color);
        stickerColors = a.getResources().getStringArray(colorsId);
        startTime = a.getInt(R.styleable.TimetableView_startTime, DEFAULT_START_TIME);
        headerHighlightColor = a.getColor(R.styleable.TimetableView_headerHighlightColor, getResources().getColor(R.color.default_headerHighlightColor));
        headerHeight = a.getDimensionPixelOffset(R.styleable.TimetableView_headerHeight, dp2Px(DEFAULT_CELL_HEIGHT_DP));
        marginBottom = a.getDimensionPixelSize(R.styleable.TimetableView_marginBottom, DEFAULT_MARGINE_BOTTOM_DP);

        a.recycle();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_timetable, this, false);
        addView(view);
        stickerBox = view.findViewById(R.id.sticker_box);
        tableHeader = view.findViewById(R.id.table_header);
        tableBox = view.findViewById(R.id.table_box);
        scrollView = view.findViewById(R.id.timetable_scrollview);
        createTable();


    }

    public void setScrollView(int x, int y) {
        scrollView.smoothScrollBy(x, y);
        scrollView.scrollTo(x, y);
        scrollView.pageScroll(View.FOCUS_UP);
    }

    public void setOnStickerSelectEventListener(OnStickerSelectedListener listener) {
        stickerSelectedListener = listener;
    }

    /**
     * get all timeTableItems TimetableView has.
     */
    public ArrayList<TimeTableItem> getAllTimeTableItems() {

        return timeTable.getTimeTableItems();
    }

    /**
     * Used in Edit mode, To check a invalidate timeTableItem.
     */
    public ArrayList<TimeTableItem> getAllTimeTableItemsInStickersExceptIdx(int idx) {

        return new ArrayList<>(timeTable.getTimeTableItems());
    }

    public int getSameIdWithLecture(Lecture lecture) {
        for (int i = 0; i < timeTable.getTimeTableItems().size(); i++) {
            TimeTableItem timeTableItem = timeTable.getTimeTableItems().get(i);
            if (CheckClikckedUtil(lecture, timeTableItem))
                return timeTableItem.getId();
        }
        return -1;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public ArrayList<TimeTableItem> getSameTitleTimeTableItems(TimeTableItem timeTableItem) {
        ArrayList<TimeTableItem> timeTableItems = new ArrayList<>();
        for (TimeTableItem searchTimeTableItem : timeTable.getTimeTableItems()) {
            if (searchTimeTableItem.classTitle.equals(timeTableItem.getClassTitle()))
                timeTableItems.add(searchTimeTableItem);
        }
        return timeTableItems;
    }

    public void add(TimeTableItem timeTableItem) {
        add(timeTableItem, -1);
    }

    private void add(final TimeTableItem timeTableItem, int specIdx) {
        TextView tv;
        int id = timeTableItem.getId();
        for (SeparateTime separateTime : SeparateTime.getSeparateTimes(timeTableItem.getClassTime())) {
            tv = new TextView(context);
            RelativeLayout.LayoutParams param = createStickerParam(separateTime);
            tv.setLayoutParams(param);
            tv.setPadding(dp2Px(4), dp2Px(8), dp2Px(4), 0);
            tv.setIncludeFontPadding(false);
            String firstWord = stringNullToNotValid(timeTableItem.getClassTitle()) + "\n\n";
            String secondWord = stringNullToNotValid(timeTableItem.getLectureClass()) + " " + stringNullToNotValid(timeTableItem.getProfessor());
            Spannable spannable = new SpannableString(firstWord + secondWord);
            spannable.setSpan(new CustomTypefaceSpan(CLASS_TITLE_fontName, Typeface.DEFAULT, 9, context), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new CustomTypefaceSpan(CLASS_LECTURE_fontName, Typeface.DEFAULT, 9, context), firstWord.length(), firstWord.length() + secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(spannable);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STICKER_FONT_SIZE_DP);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setOnClickListener(v -> {
                if (stickerSelectedListener != null)
                    stickerSelectedListener.OnStickerSelected(timeTableItem);
            });
            timeTableItem.setId(id);
            timeTableItem.addStickerTextView(tv);
            stickerBox.addView(tv);
        }
        timeTable.addTimeTableItem(timeTableItem);
        setStickerColor();
    }

    public String stringNullToNotValid(String string) {
        return (string == null) ? "미정" : string;
    }

    public void makeStickerTextView() {

    }

    public void addCheck(TimeTableItem timeTableItems) {
        addCheck(timeTableItems, -1);
    }

    private void addCheck(final TimeTableItem timeTableItem, int specIdx) {
        TextView tv;
        final int id = timeTableItem.getId();
        for (SeparateTime separateTime : SeparateTime.getSeparateTimes(timeTableItem.getClassTime())) {
            tv = new TextView(context);
            RelativeLayout.LayoutParams param = createStickerParam(separateTime);
            tv.setLayoutParams(param);
            timeTableItem.addStickerTextView(tv);
            stickerBox.addView(tv);
        }
        checkStickers.addTimeTableItem(timeTableItem);
        setCheckStickerColor();
    }

    public String createSaveData(String semester) {
        removeCheckAll();
        return SaveManager.saveTimeTable(timeTable, semester);
    }

    public void load(String data) {
        removeAll();
        int maxKey = 0;
        TimeTable timeTable = SaveManager.loadTimeTable(data);
        for (TimeTableItem timeTableItem : timeTable.getTimeTableItems())
            add(timeTableItem);
        setStickerColor();
    }

    public void removeAll() {
        for (TimeTableItem timeTableItem : timeTable.getTimeTableItems())
            for (TextView tv : timeTableItem.getStickerTextview()) {
                stickerBox.removeView(tv);
            }
        timeTable = new TimeTable();
        titleArrayList.clear();
    }

    public void edit(int index, TimeTableItem timeTableItem) {
        remove(index);
        add(timeTableItem);
    }


    public void remove(int id) {
        int index = -1;
        for (int i = 0; i < timeTable.getTimeTableItems().size(); i++) {
            if (timeTable.getTimeTableItems().get(i).getId() == id)
                index = i;
        }
        if (index == -1)
            return;
        for (TextView textView : timeTable.getTimeTableItems().get(index).getStickerTextview())
            stickerBox.removeView(textView);

        timeTable.getTimeTableItems().remove(index);
    }

    public void removeCheckAll() {
        for (TimeTableItem timeTableItem : checkStickers.getTimeTableItems())
            for (TextView tv : timeTableItem.getStickerTextview()) {
                stickerBox.removeView(tv);
            }
        checkStickers = new TimeTable();

    }

    public void setCheckStickersVisibilty(boolean isVisible) {
        int visibility = View.VISIBLE;
        if (!isVisible) visibility = View.GONE;
        for (TimeTableItem timeTableItem : checkStickers.getTimeTableItems())
            for (TextView tv : timeTableItem.getStickerTextview()) {
                tv.setVisibility(visibility);
            }
    }

    public void setHeaderHighlight(int idx) {
        TableRow row = (TableRow) tableHeader.getChildAt(0);
        TextView tx = (TextView) row.getChildAt(idx);
        tx.setTextColor(Color.parseColor("#FFFFFF"));
        tx.setIncludeFontPadding(false);
        tx.setBackgroundColor(headerHighlightColor);
        tx.setTypeface(null, Typeface.BOLD);
        tx.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP);
    }

    private void setStickerColor() {
        GradientDrawable gradientDrawable;
        int size = timeTable.getTimeTableItems().size();
        int colorSize = stickerColors.length;
        int strokeWidth = dp2Px(1);
        int strokeColor = getResources().getColor(R.color.white2);

        for (int i = 0; i < size; i++) {
            for (TextView v : timeTable.getTimeTableItems().get(i).getStickerTextview()) {
                gradientDrawable = new GradientDrawable();
                int fillColor = Color.parseColor(stickerColors[i % (colorSize)]);
                gradientDrawable.setColor(fillColor);
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                gradientDrawable.setStroke(strokeWidth, strokeColor);
                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gradientDrawable});
                v.setBackground(layerDrawable);
            }
        }
    }

    private void setCheckStickerColor() {
        int size = checkStickers.getTimeTableItems().size();
        for (TimeTableItem timeTableItem : checkStickers.getTimeTableItems()) {
            for (TextView v : timeTableItem.getStickerTextview()) {
                v.setBackground(getResources().getDrawable(R.drawable.timetable_rectangle_red));
            }
        }

    }

    private void createTable() {
        createTableHeader();
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);
            LinearLayout block = new LinearLayout(context);
            LinearLayout block2 = new LinearLayout(context);
            LinearLayout block3 = new LinearLayout(context);
            tableRow.setLayoutParams(createTableLayoutParam());
            setBlockLinearLayout(block, sideCellWidth, cellHeight, LinearLayout.VERTICAL);
            setBlockLinearLayout(block2, sideCellWidth, cellHeight / 2, LinearLayout.HORIZONTAL);
            setBlockLinearLayout(block3, sideCellWidth, cellHeight / 2, LinearLayout.HORIZONTAL);

            for (int k = 0; k < columnCount; k++) {
                TextView tv = new TextView(context);
                TextView topTimeCodeTextview = new TextView(context);
                TextView topTimeTextview = new TextView(context);
                TextView bottomTimeCodeTextview = new TextView(context);
                TextView bottomTimeTextview = new TextView(context);
                tv.setLayoutParams(createTableRowParam(cellHeight));
                topTimeCodeTextview.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight / 2));
                topTimeTextview.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight / 2));
                bottomTimeCodeTextview.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight / 2));
                bottomTimeTextview.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight / 2));

                if (k == 0) {
                    String timeCode = format("%02dA", i + 1);
                    String timeBottomCode = format("%02dB", i + 1);
                    String time = format("%02d:%02d", Integer.parseInt(getHeaderTime(i)), 0);
                    String timeHalf = format("%02d:%02d", Integer.parseInt(getHeaderTime(i)), 30);
                    setBlockLinearLayoutTextView(topTimeCodeTextview, timeCode, R.color.colorHeaderText, DEFAULT_SIDE_HEADER_FONT_SIZE_DP, Color.TRANSPARENT);
                    block2.addView(topTimeCodeTextview);
                    setBlockLinearLayoutTextView(topTimeTextview, time, R.color.colorHeaderText, DEFAULT_SIDE_HEADER_FONT_SIZE_DP, Color.TRANSPARENT);
                    block2.addView(topTimeTextview);
                    setBlockLinearLayoutTextView(bottomTimeCodeTextview, timeBottomCode, R.color.colorHeaderText, DEFAULT_SIDE_HEADER_FONT_SIZE_DP, Color.TRANSPARENT);
                    block3.addView(bottomTimeCodeTextview);
                    setBlockLinearLayoutTextView(bottomTimeTextview, timeHalf, R.color.colorHeaderText, DEFAULT_SIDE_HEADER_FONT_SIZE_DP, Color.TRANSPARENT);
                    block3.addView(bottomTimeTextview);
                    block.addView(block2);
                    block.addView(block3);
                    block.setBackground(getResources().getDrawable(R.drawable.bg_white_timetable_block));
                    tableRow.addView(block);
                } else {
                    tv.setText("");
                    tv.setGravity(Gravity.RIGHT);
                    tv.setBackground(getResources().getDrawable(R.drawable.timetable_item_rect));
                    tableRow.addView(tv);
                }

            }
            tableBox.addView(tableRow);
        }
        setMarginBottom();

    }

    public String getNow() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        return simpleDateFormat.format(date);
    }

    public void setMarginBottom() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tableBox.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, marginBottom);
        tableBox.setLayoutParams(layoutParams);
    }

    public void setMarginBottom(int dp) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tableBox.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, dp);
        tableBox.setLayoutParams(layoutParams);
    }

    private void createTableHeader() {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(createTableLayoutParam());

        for (int i = 0; i < columnCount; i++) {
            TextView tv = new TextView(context);
            if (i == 0) {
                tv.setLayoutParams(createTableRowParam(sideCellWidth, headerHeight));
            } else {
                tv.setLayoutParams(createTableRowParam(headerHeight));
            }
            tv.setTextColor(getResources().getColor(R.color.gray12));
            tv.setBackgroundColor(getResources().getColor(R.color.white3));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_FONT_SIZE_DP);
            tv.setText(headerTitle[i]);
            tv.setIncludeFontPadding(false);
            tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/notosans_regular.ttf"));
            tv.setGravity(Gravity.CENTER);

            tableRow.addView(tv);
        }
        tableHeader.addView(tableRow);
    }

    private RelativeLayout.LayoutParams createStickerParam(SeparateTime separateTime) {
        int cell_w = calCellWidth();

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(cell_w, calStickerHeightPx(separateTime));
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.setMargins(sideCellWidth + cell_w * separateTime.getDay(), calStickerTopPxByTime(separateTime.getStartTime()), 0, 0);

        return param;
    }

    private RelativeLayout.LayoutParams createCheckStickerParam(SeparateTime separateTime) {
        int cell_w = calCellWidth();

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(cell_w, calStickerHeightPx(separateTime));
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.setMargins(sideCellWidth + cell_w * separateTime.getDay(), calStickerTopPxByTime(separateTime.getStartTime()), 0, 0);

        return param;
    }

    private int calCellWidth() {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cell_w = (size.x - getPaddingLeft() - getPaddingRight() - sideCellWidth) / (columnCount - 1);
        return cell_w;
    }

    private int calStickerHeightPx(SeparateTime separateTime) {
        int startTopPx = calStickerTopPxByTime(new Time(separateTime.getStartTimeHour(), separateTime.getStartTimeMinute()));
        int endTopPx = calStickerTopPxByTime(new Time(separateTime.getEndTimeHour(), separateTime.getEndTimeMinute()));
        int d = endTopPx - startTopPx;

        return d;
    }

    private int calStickerTopPxByTime(Time time) {
        int topPx = (time.getHour() - startTime) * cellHeight + (int) ((time.getMinute() / 60.0f) * cellHeight);
        return topPx;
    }

    private TableLayout.LayoutParams createTableLayoutParam() {
        return new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    }

    private TableRow.LayoutParams createTableRowParam(int h_px) {
        return new TableRow.LayoutParams(calCellWidth(), h_px);
    }

    private TableRow.LayoutParams createTableRowParam(int w_px, int h_px) {
        return new TableRow.LayoutParams(w_px, h_px);
    }

    private LinearLayout.LayoutParams createLinearLayoutParam() {
        return new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    }

    private LinearLayout.LayoutParams createLinearRowParam(int h_px) {
        return new LinearLayout.LayoutParams(calCellWidth(), h_px);
    }

    private LinearLayout.LayoutParams createLinearRowParam(int w_px, int h_px) {
        return new LinearLayout.LayoutParams(w_px, h_px);
    }

    private void setBlockLinearLayout(LinearLayout linearLayout, int width, int height, int orientation) {
        linearLayout.setLayoutParams(createTableLayoutParam());
        linearLayout.setLayoutParams(createTableRowParam(width, height));
        linearLayout.setOrientation(orientation);
    }

    private void setBlockLinearLayoutTextView(TextView textView, String text, int textColorId, int textSize, int color) {
        textView.setText(text);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/notosans_regular.ttf"));
        textView.setTextColor(getResources().getColor(textColorId));
        textView.setIncludeFontPadding(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        textView.setBackgroundColor(getResources().getColor(R.color.white));
        textView.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        textView.setLayoutParams(createTableRowParam(sideCellWidth / 2, cellHeight / 2));
        textView.setBackgroundColor(color);
    }

    private String getHeaderTime(int i) {
        int p = (startTime + i) % 24;
        int res = p <= 12 ? p : p - 12;
        return p + "";
    }

    static private int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void onCreateByBuilder(Builder builder) {
        this.rowCount = builder.rowCount;
        this.columnCount = builder.columnCount;
        this.cellHeight = builder.cellHeight;
        this.sideCellWidth = builder.sideCellWidth;
        this.headerTitle = builder.headerTitle;
        this.stickerColors = builder.stickerColors;
        this.startTime = builder.startTime;
        this.headerHighlightColor = builder.headerHighlightColor;

        init();
    }

    public static int getCellHeight() {
        return dp2Px(DEFAULT_CELL_HEIGHT_DP);
    }

    public interface OnStickerSelectedListener {
        void OnStickerSelected(TimeTableItem timeTableItem);
    }

    static class Builder {
        private Context context;
        private int rowCount;
        private int columnCount;
        private int cellHeight;
        private int sideCellWidth;
        private String[] headerTitle;
        private String[] stickerColors;
        private int startTime;
        private int headerHighlightColor;

        public Builder(Context context) {
            this.context = context;
            rowCount = DEFAULT_ROW_COUNT;
            columnCount = DEFAULT_COLUMN_COUNT;
            cellHeight = dp2Px(DEFAULT_CELL_HEIGHT_DP);
            sideCellWidth = dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP);
            headerTitle = context.getResources().getStringArray(R.array.default_headerTitle);
            stickerColors = context.getResources().getStringArray(R.array.default_sticker_color);
            startTime = DEFAULT_START_TIME;
            headerHighlightColor = context.getResources().getColor(R.color.default_headerHighlightColor);
        }


        public Builder setRowCount(int n) {
            this.rowCount = n;
            return this;
        }

        public Builder setColumnCount(int n) {
            this.columnCount = n;
            return this;
        }

        public Builder setCellHeight(int dp) {
            this.cellHeight = dp2Px(dp);
            return this;
        }

        public Builder setSideCellWidth(int dp) {
            this.sideCellWidth = dp2Px(dp);
            return this;
        }

        public Builder setHeaderTitle(String[] titles) {
            this.headerTitle = titles;
            return this;
        }

        public Builder setStickerColors(String[] colors) {
            this.stickerColors = colors;
            return this;
        }

        public Builder setStartTime(int t) {
            this.startTime = t;
            return this;
        }

        public Builder setHeaderHighlightColor(int c) {
            this.headerHighlightColor = c;
            return this;
        }

        public TimetableView build() {
            TimetableView timetableView = new TimetableView(context);
            timetableView.onCreateByBuilder(this);
            return timetableView;
        }
    }
}
