package com.example.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class PictureUtils {
	public static final String TAG="PictureUtils";
	public static BitmapDrawable getScaleDrawable(Activity a,String path) {
		Display display=a.getWindowManager().getDefaultDisplay();
		float destWidth=display.getWidth();
		float destHeight=display.getHeight();
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(path,options);
		float srcWidth=options.outWidth;
		float srcHeight=options.outHeight;
		int inSampleSize=1;
		if (srcHeight >destHeight || srcWidth >destWidth) {
			if (srcWidth >srcHeight) {
				inSampleSize=Math.round(srcHeight/destHeight);
			} else {
				inSampleSize=Math.round(srcWidth/destWidth);
			}
		}
		options=new BitmapFactory.Options();
		options.inSampleSize=inSampleSize;
		Bitmap bitmap=BitmapFactory.decodeFile(path,options);
		return new BitmapDrawable(a.getResources(), bitmap);
	}
	public static void cleanImageView(ImageView imageView) {
		if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
			return;
		}
		BitmapDrawable bitmapDrawable=(BitmapDrawable)imageView.getDrawable();
		if (bitmapDrawable!=null) {
			bitmapDrawable.getBitmap().recycle();
		}
		imageView.setImageDrawable(null);
	}
	public static void removePhoto(Context context,String path)
	{
		boolean pic_delecte=context.deleteFile(path);
		if (pic_delecte) {
			Log.i(TAG, "pic delete success");
		} else {
			Log.i(TAG, "pic delete error");

		}
	}
}
