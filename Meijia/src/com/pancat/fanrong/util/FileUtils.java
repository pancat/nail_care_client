package com.pancat.fanrong.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.pancat.fanrong.MainApplication;

public class FileUtils {
	
	public static Integer writeDataToFile(String filename, byte[] data) throws IOException {
		Context context = MainApplication.getAppContext();
		FileOutputStream output = context.openFileOutput(filename,
				Context.MODE_PRIVATE);
		output.write(data);
		output.close();
		return 0;
	}
	
	public static String getFilenameFromUrl(String url)
	{
		String suffix = "";
		int dotPos = url.lastIndexOf('.');
		if (dotPos >= 0)
		{
			suffix = url.substring(dotPos, url.length());
		}
		String name = StringUtils.hashStringWithMD5(url);
		name = (name == null? suffix : name + suffix);
		
		return name.length() > 0 ? name : null;
	}
	
	public static boolean isFileExist(String filename)
	{
		Context context = MainApplication.getAppContext();
		File file = context.getFileStreamPath(filename);
		return file.exists();
	}

	public static byte[] readDataFromFile(String filename) throws IOException {
		Context context = MainApplication.getAppContext();
		FileInputStream fileStream = context.openFileInput(filename);
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		while (fileStream.read(buffer) != -1) {
			byteStream.write(buffer);
		}
		fileStream.close();
		byte[] data = byteStream.toByteArray();
		byteStream.close();
		return data;
	}
	
	/**
	 * 将bitmap对象的左上角和右上角设置为圆角
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
            .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 10;
        // final Rect topRightRect = new Rect(bitmap.getWidth()/2, 0, bitmap.getWidth(), bitmap.getHeight()/2);
        final Rect bottomRect = new Rect(0, bitmap.getHeight()/2, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // Fill in upper right corner
        //    canvas.drawRect(topRightRect, paint);
        // Fill in bottom corners
        canvas.drawRect(bottomRect, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
	}
	
}
