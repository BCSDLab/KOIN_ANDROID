package in.koreatech.core.ui.view.recyclerview;

import android.view.View;

public interface RecyclerViewClickListener {
    void onClick(View view, int position); //click event

    void onLongClick(View view, int position); //long click event
}
