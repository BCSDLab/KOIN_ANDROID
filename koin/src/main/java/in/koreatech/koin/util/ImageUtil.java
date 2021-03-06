package in.koreatech.koin.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
    public static final String TAG = ImageUtil.class.getName();
    public static byte[] reduceSize(Bitmap bitmap, int maxSize) {
        int quality = 100;
        int redeceQuality = 2;
        while (true) {
            if (quality <= 0) {
                return null;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            byte[] bitmapdata = bos.toByteArray();
            if (bitmapdata.length /1000000.0 > maxSize) quality -= redeceQuality;
            else return bitmapdata;
        }
    }

    public static File changeBMPtoFILE(Bitmap bitmap, String fileName, int maxSize, Context context) throws IOException {
        File file = new File(context.getCacheDir(), fileName + ".jpg");
        file.createNewFile();

        byte[] bitmapdata = reduceSize(bitmap,maxSize);
        if(bitmapdata == null) return  null;
        Log.d(TAG, "size: " + bitmapdata.length/1000000.0 );
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
