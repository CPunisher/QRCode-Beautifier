package com.cpunisher.qrcodebeautifier.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.*;

public class ExternalStorageHelper {

    public static boolean saveJpg(Bitmap bitmap, String name, Context context) throws IOException {
        boolean saved;
        OutputStream outputStream;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/" + References.IMAGE_FOLDER);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            outputStream = resolver.openOutputStream(imageUri);

            saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                    + File.separator + References.IMAGE_FOLDER;
            File file = new File(imagesDir);
            if (!file.exists()) file.mkdirs();

            File image = new File(imagesDir, name + ".jpg");
            Log.e(References.TAG, "Save to: " + image.getAbsolutePath());
            outputStream = new FileOutputStream(image);

            saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(image);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }
        outputStream.flush();
        outputStream.close();
        return saved;
    }
}
