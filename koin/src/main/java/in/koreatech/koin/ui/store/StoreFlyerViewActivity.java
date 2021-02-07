package in.koreatech.koin.ui.store;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.R;
import in.koreatech.koin.core.activity.ActivityBase;


/**
 * 전단지를 보여주는 화면
 */
public class StoreFlyerViewActivity extends ActivityBase {
    public static final String TAG = StoreFlyerViewActivity.class.getName();
    public static final float ZOOM_STANDARD = 1.1f; // 줌 기준
    // 스와이프 길이
    public static final int SWIPE_THRESHOLD = 200;
    public static final int SWIPE_VELOCITY_THRESHOLD = 200;
    @BindView(R.id.store_flyer_imageview)
    PhotoView flyerImageview;
    @BindView(R.id.current_page_text_view)
    TextView currentPageTextView;
    private int maxImageCount;
    private boolean isZoomed;
    private GestureDetector gestureDetector;
    private int currentPostition;
    private ArrayList<String> urls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_flyer_activity_view_layout);
        ButterKnife.bind(this);
        if (getIntent() == null) finish();
        currentPostition = getIntent().getIntExtra("POSITION", 0);
        urls = getIntent().getStringArrayListExtra("URLS");
        currentPageTextView.setText(getString(R.string.flyer_current_page_text, currentPostition + 1, urls.size()));
        maxImageCount = urls.size();
        init();
    }


    public void init() {
        isZoomed = false;
        Glide.with(this).load(urls.get(currentPostition)).into(flyerImageview);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return handleGestureEvent(e1, e2, velocityX, velocityY);
            }
        });

        /**
         * @see flyerImageview 를 줌 했을때 ZOOM_STANDARD 보다 클 경우 줌한다고 판단
         */
        flyerImageview.setOnScaleChangeListener((scaleFactor, focusX, focusY) -> {
            if (flyerImageview.getScale() > ZOOM_STANDARD) {
                isZoomed = true;
            } else {
                isZoomed = false;
            }
        });
    }

    /**
     * 왼쪽에서 오른쪽 또는 오른쪽에서 왼쪽으로 스와이프을 했을때 다음 전단지 또는 전 전단지로 이동한다.
     *
     * @param e1        event 1
     * @param e2        event 2
     * @param velocityX x좌표
     * @param velocityY y좌표
     * @return 값이 받아왔는지의 여부
     */
    private boolean handleGestureEvent(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSlide(true);
                    } else {
                        onSlide(false);
                    }
                    result = true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return result;
    }

    /**
     * 종료 버튼 누를 시 종료
     * @param view
     */
    @OnClick(R.id.store_flyer_close_imagebutton)
    public void onClickedCloseButton(View view){
        finish();
    }

    /**
     * 왼쪽에서 오르쪽으로 스와이프를 했을때는 전 전단지를 보여주고 반대일 경우에는 그다음 전단지를 보여준다.
     *
     * @param isLeftToRightSlide 왼쪽에서 오른쪽으로 스와이프를 하는지
     */
    public void onSlide(boolean isLeftToRightSlide) {
        if (!isLeftToRightSlide)
            currentPostition = ((currentPostition + 1) % maxImageCount);
        else {
            if (currentPostition - 1 < 0)
                currentPostition = maxImageCount;
            currentPostition = (currentPostition - 1);
        }
        currentPageTextView.setText(getString(R.string.flyer_current_page_text, currentPostition + 1, urls.size()));
        Glide.with(this).load(urls.get(currentPostition)).into(flyerImageview);
        isZoomed = false;
    }

    /**
     * 두 손가락으로 터치를 하고 있거나 줌 상태일때는 스와이프 기능을 막아준다.
     * 아닐경우에는 스와이프 검사를 해준다.
     *
     * @param ev 모션 이벤트
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        if (ev.getPointerCount() > 1 || isZoomed)
            return true;

        return gestureDetector.onTouchEvent(ev);
    }
}
