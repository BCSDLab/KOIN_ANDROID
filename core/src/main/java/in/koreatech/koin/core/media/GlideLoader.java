package in.koreatech.koin.core.media;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class GlideLoader implements ImageLoader {

    @Override
    public void load(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @Override
    public void loadCircle(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url)
//                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    @Override
    public void loadLocal(String path, ImageView imageView) {
        Glide.with(imageView.getContext()).load(new File(path)).into(imageView);
    }


}
