package com.yiju.ClassClockRoom.util.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import net.bither.util.NativeUtil;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class ImageUtil {

	// 放大缩小图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
	}

	/**
	 * 等比缩小宽度
	 * 
	 * @param bitmap b
	 * @param widthX w
	 * @param heightY h
	 * @return b
	 */
	public static Bitmap zoomBitmapXY(Bitmap bitmap, int widthX, int heightY) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth;
		float scaleHeight;
		if (width >= height) {
			scaleWidth = (float) heightY / height;
			scaleHeight = (float) heightY / height;
		} else {
			scaleWidth = (float) widthX / width;
			scaleHeight = (float) widthX / width;
		}
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
	}

	public static File zoomFileXY(File file, int widthX, int heightY) {
		Bitmap bitmap = getRotaDiskBitmap(file.getAbsolutePath());
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int diffWidth = Math.abs(width - widthX);
		int diffHeight = Math.abs(height - heightY);
		float scale;
		Matrix matrix = new Matrix();
		if (diffWidth < diffHeight) {
			scale = (float) widthX / width;
		} else {
			scale = (float) heightY / height;
		}
		matrix.postScale(scale, scale);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		try {
			return saveBitmap(newBmp, "temp.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将Drawable转化为Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * Bitmap to Byte[]
	 * 
	 * @param bitmap b
	 * @return byte[]
	 */
	public static byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * Byte[] to Bitmap
	 * 
	 * @param b b
	 * @return bm
	 */
	public static Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * Bitmap 压缩
	 * 
	 * @param bitmap bm
	 * @return bm
	 */
	public static Bitmap getBitmapCompress(Bitmap bitmap, int zoom, int widthX,
			int heightY) {
		ByteArrayOutputStream out = null;
		ByteArrayInputStream input = null;
		Bitmap reBitmap;
		BitmapFactory.Options opts;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth;
		float scaleHeight;
		if (width >= height) {
			scaleWidth = (float) heightY / height;
			scaleHeight = (float) heightY / height;
		} else {
			scaleWidth = (float) widthX / width;
			scaleHeight = (float) widthX / width;
		}
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);

		try {
			out = new ByteArrayOutputStream();
			newBmp.compress(Bitmap.CompressFormat.JPEG, zoom, out);

			input = new ByteArrayInputStream(out.toByteArray());
			opts = new BitmapFactory.Options();
			width = newBmp.getWidth();
			height = newBmp.getHeight();

			if (width >= height) {
				opts.inSampleSize = calculateInSampleSize(opts, width * heightY
						/ height, heightY);
			} else {
				opts.inSampleSize = calculateInSampleSize(opts, widthX, height
						* widthX / width);
			}
			reBitmap = BitmapFactory.decodeStream(input, null, opts);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reBitmap;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
											 int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * Bitmap to Byte[]
	 * 
	 * @param bitmap b
	 * @return byte[]
	 */
	public static byte[] getBitmapByte(Bitmap bitmap, int zoom) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, zoom, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	// 获得圆角图片的方法
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_4444);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// 获得带倒影的图片方法
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 把Bitmap转换文件存在本地
	 * 
	 * @param bitmap
	 *            图片
	 * @param imageName
	 *            保存的图片名字.jpg
	 * @throws IOException
	 */
	private static File saveBitmap(Bitmap bitmap, String imageName)
			throws IOException {
		File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()
				+ "/clock/CameraCache");
		// 判断目录是否存在
		if (!PHOTO_DIR.exists()) {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
		}
		File file = new File(PHOTO_DIR, imageName);
		file.createNewFile();
		NativeUtil.compressBitmap(bitmap, 50, file.getAbsolutePath(), true);
		bitmap.recycle();
		return file;
	}

	/**
	 * 媒体内 Uri转File路径
	 * 
	 * @param contentUri uri
	 * @return s
	 */
	public static String getRealPathFromURI(Context context, Uri contentUri) {
		String res = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, projection,
				null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				res = cursor.getString(column_index);
			}
			cursor.close();
		}
		return res;
	}

	/**
	 * Uri 转 Bitmap
	 * 
	 * @param activity ac
	 * @param imageUri uri
	 * @return bm
	 */
	public static Bitmap uriToBitmap(Activity activity, Uri imageUri) {
		ContentResolver resolver = activity.getContentResolver();
		try {
			return MediaStore.Images.Media.getBitmap(resolver, imageUri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static Bitmap getRotaDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				int degree = readPictureDegree(file.getAbsolutePath());
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				bitmap = rotatingImageView(degree, bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	private static Bitmap rotatingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		return Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

}
