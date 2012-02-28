package com.visit;

import javax.security.auth.callback.Callback;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Auto_Camera extends Activity {

	// 声明拍照界面组件
	private SurfaceView surfaceView;

	// 声明界面控件组件
	private SurfaceHolder surfaceHolder;

	// 声明照相机
	private Camera camera;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_camera);

		// 获取拍照界面组件
		surfaceView = (SurfaceView) findViewById(R.id.auto_surfaceView1);
		// 获取界面控制控件
		surfaceHolder = surfaceView.getHolder();

		// 界面控件组件回调，处理打开相机，关闭相机及照片尺寸改变
		Callback surfaceCallback = new Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				// 停止预览
				camera.stopPreview();
				// 释放相机资源
				camera.release();
				camera = null;
			}

			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				// 打开相机
				camera = Camera.open();
				try {
					// 设置预览
					camera.setPreviewDisplay(holder);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
				// 获得相机参数
				Camera.Parameters params = camera.getParameters();
				// 设置照片大小
				params.setPreviewSize(1024, 768);
				// 设置照片格式
				params.setPictureFormat(PixelFormat.JPEG);
				// 设置相机参数
				camera.setParameters(params);
				// 开始预览
				camera.startPreview();
			}
		};

		// 为SurfaceHolder添加回调并设置类型
//		surfaceHolder.addCallback(surfaceCallback);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	class SavePictureTask extends AsyncTask<byte[], String, String> {

		@Override
		protected String doInBackground(byte[]... params) {
			// TODO Auto-generated method stub
			
			//
			
			return null;
		}

		private void takePic(){
			
		}
	}
}
