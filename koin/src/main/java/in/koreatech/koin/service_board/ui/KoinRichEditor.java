package in.koreatech.koin.service_board.ui;

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

public class KoinRichEditor extends Editor {
    private EditorCore editorCore;
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
        view.findViewById(R.id.progress).setVisibility(View.GONE);
        view.findViewById(R.id.lblStatus).setVisibility(View.GONE);
        if (cancelClickListener != null) {
           view.findViewById(R.id.btn_remove).setOnClickListener(cancelClickListener);
        }
    }
}
