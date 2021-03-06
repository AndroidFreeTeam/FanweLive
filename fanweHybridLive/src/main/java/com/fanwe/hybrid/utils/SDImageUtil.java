package com.fanwe.hybrid.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

public class SDImageUtil
{

	public static String bitmapToBase64(Bitmap bitmap)
	{

		String result = null;
		ByteArrayOutputStream baos = null;
		try
		{
			if (bitmap != null)
			{
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (baos != null)
				{
					baos.flush();
					baos.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Bitmap drawable2Bitmap(Drawable drawable)
	{

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bitmap)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b)
	{
		if (b != null && b.length != 0)
		{
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else
		{
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static Drawable Bitmap2Drawable(Bitmap bitmap)
	{
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		return bd;
	}

	@SuppressWarnings("deprecation")
	public static String getImageFilePathFromIntent(Intent intent, Activity activity)
	{
		if (intent != null && activity != null)
		{
			Uri uri = intent.getData();
			String[] projection =
			{ MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);
			return path;
		} else
		{
			return null;
		}
	}

	/** ??????srcPath???????????????width height */
	public static Bitmap compressImageFromFile(String srcPath)
	{
		float hh = 800f;//
		float ww = 480f;//
		return compressImageFromFile(srcPath, ww, hh);
	}

	/** ??????srcPath???????????????width height */
	public static Bitmap compressImageFromFile(String srcPath, float width, float height)
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// ?????????,????????????
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		float hh = width;//
		float ww = height;//
		int be = 1;
		if (w > h && w > ww)
		{
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh)
		{
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// ???????????????
		newOpts.inPreferredConfig = Config.ARGB_8888;// ?????????????????????,?????????
		newOpts.inPurgeable = true;// ????????????????????????
		newOpts.inInputShareable = true;// ???????????????????????????????????????????????????

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//????????????????????????????????????????????????????????????
		// ??????????????????,??????????????????
		return bitmap;
	}

	/** ????????????????????????Bitmap */
	public static Bitmap pathToBitmap(String path)
	{
		return BitmapFactory.decodeFile(path);
	}

	public static boolean compressImageFileToNewFile(File oldFile, File newFile, int quality)
	{
		if (oldFile != null && newFile != null && oldFile.exists())
		{
			try
			{
				if (!newFile.exists())
				{
					newFile.createNewFile();
				}
				if (newFile.exists())
				{
					Bitmap bmpOld = compressImageFromFile(oldFile.getAbsolutePath());
					bmpOld.compress(Bitmap.CompressFormat.JPEG, quality, new FileOutputStream(newFile));
					return true;
				} else
				{
					return false;
				}
			} catch (Exception e)
			{
				return false;
			}
		} else
		{
			return false;
		}

	}

	/**
	 * * ????????????????????????
	 */
	public static Bitmap ImageCrop(Bitmap bitmap)
	{
		int w = bitmap.getWidth(); // ????????????????????????
		int h = bitmap.getHeight();

		int wh = w > h ? h : w;// ???????????????????????????????????????

		int retX = w > h ? (w - h) / 2 : 0;// ????????????????????????????????????x??????
		int retY = w > h ? 0 : (h - w) / 2;

		// ?????????????????????
		return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
	}

	/** ????????????????????? newPath ?????????fileName ??????Bitmap,????????????quality */
	@SuppressLint("SdCardPath")
	public static String dealImageCompress(String newPath, String fileName, Bitmap bitmap, int quality)
	{
		File dir = new File(newPath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		String filePath = newPath + fileName;
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);// ?????????????????????
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return newPath;
	}

}
