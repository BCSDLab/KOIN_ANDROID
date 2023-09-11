package in.koreatech.koin.util;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    private static FileUtil instance;

    private FileUtil() {
    }

    public static FileUtil getInstance() {
        if (instance == null) {
            synchronized (FileUtil.class) {
                if (instance == null) {
                    instance = new FileUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Stores the given {@link Bitmap} to a path on the device.
     *
     * @param context  Android Specific context
     * @param bitmap   The {@link Bitmap} that needs to be stored
     * @param fileName The file name in which the bitmap is going to be stored.
     *
     * @return true if bitmap saved successfully
     */
    public boolean storeBitmap(Context context, Bitmap bitmap, String fileName) {
        // Add a specific media item.
        ContentResolver contentResolver = context.getContentResolver();

        // Find all audio files on the primary external storage device.
        Uri imageCollection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageCollection = MediaStore.Images.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues newImage = new ContentValues();
        newImage.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            newImage.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        Uri imageToSaveUri = contentResolver.insert(imageCollection, newImage);

        try {
            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(imageToSaveUri, "w", null);

            if (pdf == null) {
                return false;
            } else {
                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    newImage.clear();
                    newImage.put(MediaStore.Images.Media.IS_PENDING, 0);
                    contentResolver.update(imageToSaveUri, newImage, null, null);
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}