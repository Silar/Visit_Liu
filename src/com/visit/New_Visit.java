package com.visit;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Create new Visit.
 * 
 * @author Simple_liu
 * 
 */
public class New_Visit extends Activity {

	private Button nv_b1, nv_b2;
	private EditText nv_et1, nv_et2;
	private TextView nv_tiv5, nv_tiv6, nv_tiv7, nv_tiv8;
	private ImageView nv_iv, nv_iv0;
	Bitmap bt1, bt2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_visit);
		MyApplication.getInstance().addActivity(this);

		nv_b1 = (Button) findViewById(R.id.nv_b1);
		nv_b2 = (Button) findViewById(R.id.nv_b2);
		nv_et1 = (EditText) findViewById(R.id.nv_et1);
		nv_et2 = (EditText) findViewById(R.id.nv_et2);
		nv_tiv5 = (TextView) findViewById(R.id.nv_tiv5);
		nv_tiv6 = (TextView) findViewById(R.id.nv_tiv6);
		nv_tiv7 = (TextView) findViewById(R.id.nv_tiv7);
		nv_tiv8 = (TextView) findViewById(R.id.nv_tiv8);

		nv_iv = (ImageView) findViewById(R.id.nv_iv);
		nv_iv0 = (ImageView) findViewById(R.id.nv_iv0);

		// 显示已有游记

		File file = new File("sdcard/YouTu");
		File[] files = file.listFiles();
		Arrays.sort(files, new New_Visit.CompratorByLastModified());

		String i = String.valueOf(files.length);
		Log.d("测试", i);

		// 获取游记创建时间
		// 自定义文件创建时间显示
		SimpleDateFormat Nformat = new SimpleDateFormat("yyyy.MM.dd");

		// 显示最近的一个游记
		if (files.length == 1) {
			File f1 = files[0];
			Log.d("New_Visit_列表", f1.getName().toString());

			long time = f1.lastModified();
			Date nt = new Date(time);
			String ts = Nformat.format(nt);

			nv_tiv5.setText(f1.getName().toString().trim());
			nv_tiv6.setText(ts);

			if (f1.isDirectory()) {

				File[] ph1 = f1.listFiles();
				if (ph1.length > 0) {

					Arrays.sort(ph1, new New_Visit.CompratorByLastModified());

					File p1 = ph1[0];
					String st1 = "sdcard/YouTu" + "/"
							+ f1.getName().toString().trim() + "/"
							+ p1.getName().toString().trim();
					Log.d("列表", st1);

					bt1 = BitmapFactory.decodeFile(st1);

					nv_iv.setImageBitmap(bt1);

					// 隐藏第二个游记的背景
					nv_iv0.setVisibility(View.GONE);
				}

			}

		}

		// 两个游记时.
		if (files.length >= 2) {
			File f1 = files[0];
			File f2 = files[1];

			long time1 = f1.lastModified();
			Date currentTime1 = new Date(time1);
			String tim1 = Nformat.format(currentTime1);

			long time2 = f2.lastModified();
			Date currentTime2 = new Date(time2);
			String tim2 = Nformat.format(currentTime2);

			nv_tiv5.setText(f1.getName().toString());
			nv_tiv7.setText(f2.getName().toString());
			Log.d("New_Visit_列表", f1.getName().toString());
			Log.d("New_Visit_列表", f2.getName().toString());

			nv_tiv6.setText(tim1);
			nv_tiv8.setText(tim2);

			if (f1.isDirectory()) {
				File[] ph1 = f1.listFiles();

				if (ph1.length > 0) {

					Arrays.sort(ph1, new New_Visit.CompratorByLastModified());
					File p1 = ph1[0];
					String st1 = "sdcard/YouTu" + "/"
							+ f1.getName().toString().trim() + "/"
							+ p1.getName().toString().trim();

					Log.d("列表", st1);

					bt1 = BitmapFactory.decodeFile(st1);
					nv_iv.setImageBitmap(bt1);
				}
			}

			if (f2.isDirectory()) {
				File[] ph2 = f2.listFiles();
				if (ph2.length > 0) {

					Arrays.sort(ph2, new New_Visit.CompratorByLastModified());
					File p2 = ph2[0];
					String st2 = "sdcard/YouTu" + "/"
							+ f2.getName().toString().trim() + "/"
							+ p2.getName().toString().trim();

					bt2 = BitmapFactory.decodeFile(st2);

					nv_iv0.setImageBitmap(bt2);
				}
			}
		}

		// 保存游记
		nv_b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// trim()方法是将String前后的空格去掉
				String name = nv_et1.getText().toString().trim();
				String miaosu = nv_et2.getText().toString().trim();

				// 游记的名字不能为空
				if ("".equals(name)) {
					Toast.makeText(getApplicationContext(),
							"游记还没有名字，赶快给它起一个吧！", Toast.LENGTH_SHORT).show();
				} else {

					// 保存新建游记的名字到SharedPreferences--"Visit_Name"中

					SharedPreferences set = getSharedPreferences("VisitName",
							MODE_PRIVATE);
					SharedPreferences.Editor editor = set.edit();
					editor.putString("Vcontent", name);
					editor.commit();

					// 创建游记文件夹
					MakeDirs("YouTu/" + name);

					// 保存游记相关信息到SharedPreferences--"Visit_Message"中
					SharedPreferences vm = getSharedPreferences(
							"Visit_Message", 0);
					SharedPreferences.Editor ve = vm.edit();
					// 将游记的名字作为key，保存相应游记的信息.
					ve.putString(name, miaosu);
					ve.commit();

					// 获取游记名字，为保存图片命名用于计数.
					int num = 0;
					SharedPreferences pcount = getSharedPreferences(
							"VisitCount", 0);
					SharedPreferences.Editor pe = pcount.edit();
					pe.putInt(name, num);
					pe.commit();

					// 跳转页面
					Intent nv_intent1 = new Intent();
					nv_intent1.setClass(New_Visit.this, Begin_Visit.class);
					startActivity(nv_intent1);
				}
			}
		});

		// 取消创建游记.
		nv_b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nv_intent2 = new Intent();
				nv_intent2.setClass(New_Visit.this, Main.class);
				startActivity(nv_intent2);
			}
		});
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
	 * 对比文件创建时间
	 * 
	 * @author Simple_liu
	 * 
	 */
	static class CompratorByLastModified implements Comparator<File> {
		public int compare(File f1, File f2) {
			long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0)
				return -1;
			else if (diff == 0)
				return 0;
			else
				return 1;
		}

		public boolean equals(Object obj) {
			return true;
		}
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
