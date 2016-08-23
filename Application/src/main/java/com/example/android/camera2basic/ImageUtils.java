package com.example.android.camera2basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by cliqers on 22/8/2016.
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    public static Uri saveImageToLocal(final Context context, final Bitmap bitmap) {

        Log.d(TAG, "saveImageToLocal");

        File mediaStorageDir = getMediaStorage(context);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "mediaStorageDir is null la bodohl");
                return null;
            }
        }

        Bitmap out = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), false);

        String timeStamp = getSimpleTimestamp();
        File dir = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(dir);
            out.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            bitmap.recycle();
            out.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(dir);
    }

    public static File getMediaStorage(final Context context) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), context.getResources().getString(R.string.app_name)
        );
    }

    public static String getSimpleTimestamp() {
        return new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
    }

    public static Bitmap combineBitmaps(final Context context, List<Bitmap> bitmaps) {

        Log.d(TAG, "combineBitmaps");

        if (bitmaps == null || bitmaps.size() == 0) {
            return null;
        }
        int width = 0;
        int height = 0;


        for (Bitmap bitmap : bitmaps) {
            height += bitmap.getHeight();
            if (bitmap.getWidth() > width) {
                width = bitmap.getWidth();
            }
        }

        Bitmap cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Paint myPaint = new Paint();
        myPaint.setColor(context.getResources().getColor(android.R.color.white));
        Canvas comboImage = new Canvas(cs);
        comboImage.drawPaint(myPaint);
        int positionY = 0;

        for (Bitmap bitmap : bitmaps) {
            comboImage.drawBitmap(bitmap, 0, positionY, null);
            positionY += bitmap.getHeight();

        }
        return cs;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
