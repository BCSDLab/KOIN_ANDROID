package in.koreatech.koin.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author yunjaeNa
 * @since 2019-09-07
 *
 * Img 태그 주소를 통해 이미지를 받아서 textview 에다가 넣어준다.
 */
public class LoadImageFromUrl extends AsyncTask<Object, Void, Bitmap> {
    public static final String TAG = "LoadImageFromUrl";
    private LevelListDrawable drawable;
    private WeakReference<Context> context;
    private WeakReference<TextView> textView;

    public LoadImageFromUrl(TextView textView, Context context) {
        this.context = new WeakReference<>(context);
        this.textView = new WeakReference<>(textView);
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        String source = (String) params[0];
        drawable = (LevelListDrawable) params[1];
        Log.d(TAG, "doInBackground " + source);
        try {
            InputStream is = new URL(source).openStream();
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "onPostExecute drawable " + drawable);
        Log.d(TAG, "onPostExecute bitmap " + bitmap);
        if (bitmap != null) {
            BitmapDrawable d = new BitmapDrawable(context.get().getResources(), bitmap);
            int width = textView.get().getWidth() < bitmap.getWidth() ? textView.get().getWidth() : bitmap.getWidth();
            int height = bitmap.getHeight() * width / bitmap.getWidth();
            drawable.addLevel(1, 1, d);
            drawable.setBounds(0, 0, width,height);
            drawable.setLevel(1);
            CharSequence t = textView.get().getText();
            textView.get().setText(t);
        }
    }
}
