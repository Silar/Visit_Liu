package com.visit;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Simple_liu
 * 
 */

public class Main extends Activity {

	private Button main_b1, main_b2, main_b3;
	private TextView main_tv1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		MyApplication.getInstance().addActivity(this);

		main_b1 = (Button) findViewById(R.id.main_b1);
		main_b2 = (Button) findViewById(R.id.main_b2);
		main_b3 = (Button) findViewById(R.id.main_b3);
		main_tv1 = (TextView) findViewById(R.id.main_tv1);

		String STORE_NAME = "VisitName";
		SharedPreferences set = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
		String con = set.getString("Vcontent", "");
		Log.d("Main", con);
		main_tv1.setText(con);

		// 跳转到照相页面
		main_b1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent main_intent1 = new Intent();
				main_intent1.setClass(Main.this, mCamera.class);
				startActivity(main_intent1);
				finish();
			}

		});

		// 跳转到游记陈列管理页面
		main_b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent main_intent2 = new Intent();
				main_intent2.setClass(Main.this, Visit_List.class);
				startActivity(main_intent2);
			}

		});

		// 跳转到新建游记页面
		main_b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent main_intent3 = new Intent();
				main_intent3.setClass(Main.this, New_Visit_First.class);

				Intent main_intent4 = new Intent();
				main_intent4.setClass(Main.this, New_Visit.class);

				File folder = new File("sdcard/YouTu");
				File[] fo = folder.listFiles();

				if (fo.length == 0) {
					startActivity(main_intent3);
				} else {
					startActivity(main_intent4);
				}
			}

		});
	}

	/* 覆写下面两个方法 */
	/* 添加菜单 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* menu.add(组ID，项ID，显示顺序，显示标题) */
		menu.add(0, 0, 0, "About").setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(0, 1, 1, "Help").setIcon(android.R.drawable.ic_menu_help);
		menu.add(0, 2, 2, "Exit").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/* 处理菜单事件 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();// 得到当前选中MenuItem的ID
		switch (item_id) {
		case 0: {

		}
		case 1: {

		}
		case 2: {
			// 事件处理代码
			MyApplication.getInstance().exit();
		}
		}
		return true;
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

}
