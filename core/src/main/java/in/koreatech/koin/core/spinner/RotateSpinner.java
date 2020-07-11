package in.koreatech.koin.core.spinner;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;


public class RotateSpinner extends AppCompatSpinner {

    private OnSpinnerEventsListener listener;
    private boolean isOpenInitiated = false;

    public RotateSpinner(Context context) {
        super(context);
    }

    public RotateSpinner(Context context, int mode) {
        super(context, mode);
    }

    public RotateSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        isOpenInitiated = true;
        if (listener != null) {
            listener.onSpinnerStateChanged(this, true);
        }
        return super.performClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent();
        }
    }

    public void setSpinnerEventsListener(
            OnSpinnerEventsListener onSpinnerEventsListener) {
        listener = onSpinnerEventsListener;
    }

    public void performClosedEvent() {
        isOpenInitiated = false;
        if (listener != null) {
            listener.onSpinnerStateChanged(this, false);
        }
    }


    public boolean hasBeenOpened() {
        return isOpenInitiated;
    }


    public interface OnSpinnerEventsListener {
        void onSpinnerStateChanged(AppCompatSpinner spinner, boolean isExpanded);
    }


}
