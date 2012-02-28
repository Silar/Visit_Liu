package com.visit;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Make Photos
 * 
 * @author Simple_liu
 * 
 */

public class mCamera extends Activity {

	static byte[] ph;
	private ImageView ima;
	private Button but1, but2, but3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.mcamera);

		ima = (ImageView) findViewById(R.id.ca_iv1);
		but1 = (Button) findViewById(R.id.ca_b1);
		but2 = (Button) findViewById(R.id.ca_b2);
		but3 = (Button) findViewById(R.id.ca_b3);

		// 照相
		but1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartCamera();
			}
		});

		//跳转到相片编辑页面
		but2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent inbut2 = new Intent();
				inbut2.setClass(mCamera.this,Photo_Message.class);
				startActivity(inbut2);
			}
		});
		
		//返回主页面
		but3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent inbut3 = new Intent();
				inbut3.setClass(mCamera.this,Main.class);
				startActivity(inbut3);
			}
		});
		
		
	}

	// 调用系统相机
	public final String SAVE_PATH_IN_SDCARD = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private void StartCamera() {

		Intent ca_intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// ca_intent2.putExtra("autofocus", true);

		ca_intent2.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(SAVE_PATH_IN_SDCARD, "YouTu.JPEG")));

		startActivityForResult(ca_intent2, 10);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 非常重要！！如果被呼叫的activity中途放弃，就没有返回值，要避免错误
		if (data == null)
			return;

		if (requestCode == 10) {
			if (resultCode == Activity.RESULT_OK) {
				
				
				
			}
			if (resultCode == RESULT_CANCELED)
				super.onRestart();
		}

		super.onActivityResult(requestCode, resultCode, data);		
		
		Bitmap bt=BitmapFactory.decodeFile("sdcard/YouTu.JPEG");
		Log.d("相片", bt.toString());
		ima.setImageBitmap(bt);
	}


	/**
	 * 重写Back键方法，直接返回Main主页面，而不是返回Camera照相页面.
	 */

	@Override
	public void onBackPressed() {
		Intent inten = new Intent();
		inten.setClass(mCamera.this, Main.class);
		startActivity(inten);
		finish();
		return;
	}

}
