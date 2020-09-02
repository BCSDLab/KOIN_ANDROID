package in.koreatech.koin.ui.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorCore;
import com.github.irshulx.models.EditorControl;
import com.github.irshulx.models.EditorType;

import in.koreatech.koin.R;

/**
 *  기존 Rich Editor에서 이미지를 지우는 기능을 하는
 *  btn_remove의 클릭 리스너를 만들기 위해 Rich Editor를 상속 받은 클래스
 */
public class KoinRichEditor extends Editor {
    private OnClickListener cancelClickListener;

    public KoinRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnCancelListener(OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
    }

    @Override
    public void insertImage(Bitmap bitmap) {
        View view = getImageExtensions().insertImage(bitmap, null, -1, null, true);
        if (cancelClickListener != null) {
           view.findViewById(R.id.btn_remove).setOnClickListener(cancelClickListener);
        }
    }
}
