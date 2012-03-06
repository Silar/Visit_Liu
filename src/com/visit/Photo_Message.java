package com.visit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Simple_liu
 * 
 */

public class Photo_Message extends Activity {
	TextView pm_tv2, pm_tv3;
	Button pm_b1, pm_b2, pm_b3;
	EditText pm_et1, pm_et2;
	ImageView pm_iv1;
	Bitmap bt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_message);
		MyApplication.getInstance().addActivity(this);

		pm_tv2 = (TextView) findViewById(R.id.pm_tv2);
		pm_tv3 = (TextView) findViewById(R.id.pm_tv3);
		pm_et1 = (EditText) findViewById(R.id.pm_et1);
		pm_et2 = (EditText) findViewById(R.id.pm_et2);
		pm_b2 = (Button) findViewById(R.id.pm_b2);
		pm_b3 = (Button) findViewById(R.id.pm_b3);
		pm_iv1 = (ImageView) findViewById(R.id.pm_iv1);

		/*
		 * 利用BitmapFactory.Options.inSampleSize方法将文件地址直接转码成bitmap. 防止bitmap size
		 * exceeds VM budget的发生
		 */
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile("sdcard/You.JPEG", opts);

		opts.inSampleSize = computeSampleSize(opts, -1, 1280 * 960);
		opts.inJustDecodeBounds = false;

		try {
			bt = BitmapFactory.decodeFile("sdcard/You.JPEG", opts);
		} catch (OutOfMemoryError err) {
		}

		Log.d("bitmapfile", bt.toString());

		// 将照片旋转90度
		int width = bt.getWidth();
		int height = bt.getHeight();

		Matrix matrix = new Matrix();
		matrix.postRotate(90);

		final Bitmap newbt = Bitmap.createBitmap(bt, 0, 0, width, height,
				matrix, true);

		pm_iv1.setImageBitmap(newbt);

		// 返回主页面
		pm_b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Photo_Message.this)
						.setTitle("温馨提示")
						.setMessage("确定放弃该图片？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										Intent pm_intent2 = new Intent();
										pm_intent2.setClass(Photo_Message.this,
												Main.class);
										startActivity(pm_intent2);
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub

									}
								}).show();
			}
		});

		/**
		 * 保存图片和相关信息
		 */

		pm_b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// 获取游记目录去保存图片.
				String STORE_NAME = "VisitName";
				SharedPreferences set = getSharedPreferences(STORE_NAME,
						MODE_PRIVATE);
				String con = set.getString("Vcontent", "");

				// 获取为游记图片命名所需的计数.
				SharedPreferences pcount = getSharedPreferences("VisitCount", 0);
				int num = pcount.getInt(con, 0);
				num = num + 1;

				// 重新保存计数提交.
				SharedPreferences.Editor pe = pcount.edit();
				pe.putInt(con, num);
				pe.commit();

				// String photo_name = pm_et1.getText().toString().trim();

				String photo_name = null;
				if (0 < num && num < 10) {

					if ("".equals(pm_et1.getText().toString())) {

						photo_name = "00" + String.valueOf(num) + "_IMG";

					} else
						photo_name = "00" + String.valueOf(num)
								+ pm_et1.getText().toString().trim();

				} else if (num < 100) {
					if ("".equals(pm_et1.getText().toString())) {

						photo_name = "0" + String.valueOf(num) + "_IMG";

					} else

						photo_name = "0" + String.valueOf(num)
								+ pm_et1.getText().toString().trim();

				} else {
					if ("".equals(pm_et1.getText().toString())) {

						photo_name = String.valueOf(num) + "_IMG";

					} else
						photo_name = String.valueOf(num)
								+ pm_et1.getText().toString().trim();
				}

				// 照片名字存入SharedPreferences相册中，作为该相册的首页
				String visit = "Album";
				SharedPreferences sh = getSharedPreferences(visit, 0);
				SharedPreferences.Editor edi = sh.edit();
				edi.putString(con, photo_name);
				edi.commit();

				// 将点击的Item相应的游记名字暂时保存，用来显示这个游记里面的照片.
				String dataN = "Fname";
				SharedPreferences pl = getSharedPreferences(dataN, MODE_PRIVATE);
				SharedPreferences.Editor editor = pl.edit();
				editor.putString("Fn", con);
				editor.commit();

				// 保存图片
				String fs = "sdcard/YouTu/" + con + "/";

				File save = new File(fs + photo_name + ".JPEG");
				try {
					save.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				FileOutputStream fOut = null;
				try {
					fOut = new FileOutputStream(save);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newbt.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

				// 跳转到相片列表页面
				Intent pm_intent3 = new Intent();
				pm_intent3.setClass(Photo_Message.this, Photo_List.class);
				startActivity(pm_intent3);
				finish();
			}

		});

	}

	/**
	 * transform bytes[] to bitmap object.
	 * 
	 * @param bytes
	 * @param opts
	 * @return
	 */
	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean IsSdCard() {
		boolean nIsSaveSdCard = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			nIsSaveSdCard = true;
		else
			nIsSaveSdCard = false;
		return nIsSaveSdCard;
	}

	public static boolean MakeDirs(String aPath) {
		boolean nIsExist = false;
		if (IsSdCard()) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			String p = sdCardDir.getAbsolutePath() + "/" + aPath;
			Log.i("New_Visit.MakeDirs", p);
			File nPath = new File(p);
			nIsExist = nPath.exists();
			if (!nIsExist)
				nIsExist = nPath.mkdirs();
		} else {
			File nPath = new File("./" + aPath);
			nIsExist = nPath.exists();
			if (!nIsExist)
				nIsExist = nPath.mkdirs();
		}
		return nIsExist;
	}

	public static FileOutputStream GetFileOutStream(String aFileName,
			boolean aIsApp) {
		try {
			if (IsSdCard()) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				File saveFile = new File(sdCardDir, aFileName);
				FileOutputStream outStream = new FileOutputStream(saveFile,
						aIsApp);
				return outStream;
			} else {
				File saveFile = new File("./", aFileName);
				FileOutputStream outStream = new FileOutputStream(saveFile,
						aIsApp);
				return outStream;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 重写Back键方法，直接返回Main主页面，而不是返回Camera照相页面.
	 */

	@Override
	public void onBackPressed() {

		new AlertDialog.Builder(Photo_Message.this).setTitle("温馨提示")
				.setMessage("确定放弃该图片？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent pm_intent2 = new Intent();
						pm_intent2.setClass(Photo_Message.this, Main.class);
						startActivity(pm_intent2);
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	// 动态计算恰当的inSampleSize方法.
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}