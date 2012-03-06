package com.visit;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 存在游记的主页面
 * 
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

		/* 显示正在进行的游记的名字 */
		String STORE_NAME = "VisitName";
		SharedPreferences set = getSharedPreferences(STORE_NAME, MODE_PRIVATE);
		String con = set.getString("Vcontent", "");
		Log.d("Main", con);
		main_tv1.setText(con);

		/* 调用系统相机进行拍照 */
		main_b1.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartCamera();
			}

		});

		/* 跳转到游记陈列管理页面 */
		main_b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent main_intent2 = new Intent();
				main_intent2.setClass(Main.this, Visit_List.class);
				startActivity(main_intent2);
			}

		});

		/* 跳转到新建游记页面 */
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
		menu.add(0, 0, 0, "关于")
				.setIcon(android.R.drawable.ic_menu_info_details);
		menu.add(0, 1, 1, "退出").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/* 处理菜单事件 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();// 得到当前选中MenuItem的ID
		switch (item_id) {
		case 0: {
			// 关于游图
			dialog();
		}
			break;
		case 1: {
			// 退出程序
			MyApplication.getInstance().exit();
		}
			break;
		}
		return true;
	}

	/**
	 * 重写Back键方法，弹出对话框，确定是否要关闭程序
	 */

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("您是否要退出游图？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						MyApplication.getInstance().exit();

					}
				})
				.setNegativeButton("放弃", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();

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

	// 调用系统相机
	public final String SAVE_PATH_IN_SDCARD = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private void StartCamera() {

		Intent ca_intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// 将图片暂时存储在SD上
		ca_intent2.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(SAVE_PATH_IN_SDCARD, "You.JPEG")));

		startActivityForResult(ca_intent2, 10);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == 10) {

				// 跳转到图片编辑界面.
				Intent pm = new Intent();
				pm.setClass(Main.this, Photo_Message.class);
				startActivity(pm);
			}
		}
		if (resultCode == RESULT_CANCELED)
			return;

		super.onActivityResult(requestCode, resultCode, data);
	}

	// 定义对话框
	protected void dialog() {

		LayoutInflater inflater = LayoutInflater.from(this);
		View layout = inflater.inflate(R.layout.about_visit,
				(ViewGroup) findViewById(R.id.av));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		AlertDialog alertDialog = builder.create();
		alertDialog.setButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();

			}
		});
		alertDialog.show();
	}

}
