package in.koreatech.koin.util;


import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
     * @param bitmap   The {@link Bitmap} that needs to be stored
     * @param filePath The path in which the bitmap is going to be stored.
     */
    public File storeBitmap(Bitmap bitmap, String filePath) {
        File imageFile = new File(filePath);
        imageFile.getParentFile().mkdirs();
        try {
            OutputStream fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();
            return imageFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}