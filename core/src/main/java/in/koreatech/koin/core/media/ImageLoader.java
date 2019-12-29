package in.koreatech.koin.core.media;

import android.widget.ImageView;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public interface ImageLoader {
    void load(String url, ImageView imageView);
    void loadCircle(String url, ImageView imageView);
    void loadLocal(String path, ImageView imageView);
}