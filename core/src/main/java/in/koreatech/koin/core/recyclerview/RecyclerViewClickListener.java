package in.koreatech.koin.core.recyclerview;

import android.view.View;

public interface RecyclerViewClickListener {
    void onClick(View view, int position); //click event

    void onLongClick(View view, int position); //long click event
}
