package in.koreatech.koin.core.helper;

import android.view.View;

/**
 * Created by hyerim on 2018. 6. 4....
 */
public interface RecyclerViewClickListener {
    void onClick(View view, int position); //click event

    void onLongClick(View view, int position); //long click event
}
