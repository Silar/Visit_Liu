package com.visit;

/**
 * 没有游记的主页面
 * 
 * @author Simple_liu
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class You_Main extends Activity {

	private Button you_b1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.you_main);
		MyApplication.getInstance().addActivity(this);

		you_b1 = (Button) findViewById(R.id.you_b1);

		you_b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(You_Main.this, New_Visit_First.class);
				startActivity(intent);
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

			dialog();

		}
			break;

		case 1: {
			// 事件处理代码
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
