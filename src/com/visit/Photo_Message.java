package com.visit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.media.ExifInterface;

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
	Location location;
	LocationManager lm;
	LocationListener locationListener;
	String lon, lat;

	// via network to get location
	private String networkProvider = LocationManager.NETWORK_PROVIDER;
	// via gps to get location
	private String GpsProvider = LocationManager.GPS_PROVIDER;

//	public final String SAVE_PATH_IN_SDCARD = android.os.Environment
//			.getExternalStorageDirectory().getAbsolutePath();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_message);
		MyApplication.getInstance().addActivity(this);

//		StartCamera();

		pm_tv2 = (TextView) findViewById(R.id.pm_tv2);
		pm_tv3 = (TextView) findViewById(R.id.pm_tv3);
		pm_et1 = (EditText) findViewById(R.id.pm_et1);
		pm_et2 = (EditText) findViewById(R.id.pm_et2);
		pm_b1 = (Button) findViewById(R.id.pm_b1);
		pm_b2 = (Button) findViewById(R.id.pm_b2);
		pm_b3 = (Button) findViewById(R.id.pm_b3);
		pm_iv1 = (ImageView) findViewById(R.id.pm_iv1);

		// show the photo.
		// get the Bundle object from Intent.
		// Bundle getPhoto = this.getIntent().getExtras();

		// final byte[] photo=getPhoto.getByteArray("photo_key");
		// BitmapFactory.Options opt = new BitmapFactory.Options();
		// opt.inSampleSize = 2;
		// final Bitmap bt=(Bitmap)getPicFromBytes(photo, null);
		// Log.i("Photo_Message", "show the picture success~~");

		// File f = new File("sdcard/YouTu/temp.JPEG");

		final Bitmap bt = BitmapFactory.decodeFile("sdcard/YouTu.JPEG");
		Log.d("bitmapfile", bt.toString());

		pm_iv1.setImageBitmap(bt);

		// call getLocations method to get the location object
		getLocation(Photo_Message.this);

		/**
		 * call ShowPlace method, view the detailed address.
		 */
		pm_b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowPlace();
			}
		});

		// 返回主页面
		pm_b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent pm_intent2 = new Intent();
				pm_intent2.setClass(Photo_Message.this, Main.class);
				startActivity(pm_intent2);
				finish();
			}
		});

		/**
		 * 保存图片和相关信息
		 */
		pm_b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String photo_name = pm_et1.getText().toString().trim();

				if ("".equals(photo_name)) {
					Toast.makeText(getApplicationContext(),
							"相片还没有名字，赶快给它起一个吧！", Toast.LENGTH_SHORT).show();
				} else {

					// 获取游记目录去保存图片.
					String STORE_NAME = "VisitName";
					SharedPreferences set = getSharedPreferences(STORE_NAME,
							MODE_PRIVATE);
					String con = set.getString("Vcontent", "");

					// 将图片的地址存入Album中，提供相册封面
					String visit = "Album";
					SharedPreferences sh = getSharedPreferences(visit, 0);
					SharedPreferences.Editor edi = sh.edit();
					edi.putString(con, photo_name);
					edi.commit();

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
					bt.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

					// 保存经纬度信息.
					if (lat != null && lon != null) {
						try {
							ExifInterface exif = new ExifInterface(fs
									+ photo_name + ".JPEG");
							exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
									lat);
							exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
									lon);
							exif.saveAttributes();
							Log.i("LATITUDE: ", lat);
							Log.i("LONGITUDE: ", lon);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					// }
					// //当一个游记都没有，点击继续本次旅行时，默认生成一个“游图”目录.
					// else{
					//
					//
					// File f=new File("YouTu/"+"You");
					// f.mkdirs();
					//
					// SharedPreferences
					// sete=getSharedPreferences("VisitName",0);
					// SharedPreferences.Editor et=sete.edit();
					// et.putString("Vcontent", "You");
					// et.commit();
					// //
					// // String you = "You";
					// // MakeDirs("YouTu/"+you);
					// //
					// // Log.i("VB试验", "YouTu/You");
					//
					// // String fis="sdcard/YouTu"+you+"/";
					//
					//
					// //因为此时一个游记都没有，所以SharedPreference("VisiName")中的内容是为空的，
					// //我们需要将默认的"游图"名字写进去.
					//
					// // SharedPreferences
					// sete=getSharedPreferences("VisitName",0);
					// // SharedPreferences.Editor et=sete.edit();
					// // et.putString("Vcontent", you);
					// // et.commit();
					// // Log.i("tag", you);
					//
					// //将图片的地址存入Album中，提供相册封面
					// String visit="Album";
					// SharedPreferences sh=getSharedPreferences(visit, 0);
					// SharedPreferences.Editor edi=sh.edit();
					// edi.putString("You", photo_name);
					// edi.commit();
					//
					// File save= new File(f+"/"+photo_name+".JPEG");
					// Log.i("tag1",save.getName().toString());
					//
					// try {
					// save.createNewFile();
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					//
					// FileOutputStream fOut = null;
					// try {
					// fOut = new FileOutputStream(save);
					// } catch (FileNotFoundException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// bt.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					//
					// //保存经纬度信息.
					// if(lat!=null && lon !=null){
					// try{
					// ExifInterface exif=new
					// ExifInterface(f+"/"+photo_name+".JPEG");
					// exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
					// exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, lon);
					// exif.saveAttributes();
					// Log.i("LATITUDE: ", lat);
					// Log.i("LONGITUDE: ", lon);
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					// }

					Intent pm_intent3 = new Intent();
					pm_intent3.setClass(Photo_Message.this, Photo_List.class);
					startActivity(pm_intent3);
				}
			}

		});

	}

	/**
	 * get location object.
	 * 
	 * @param mContext
	 */
	private void getLocation(Context mContext) {

		// get the system service--LocationManager object.
		lm = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		// if networkProvider exits,than get it.nor than gpsPro6vider.
		// if two of them are not exiting, than show a toast.
		if (startLocation(networkProvider, mContext)) {
			updateLocation(location, mContext);
		} else if (startLocation(GpsProvider, mContext)) {
			updateLocation(location, mContext);
		} else {
			Toast.makeText(this, "没有GPS设备", 5000).show();
		}
	}

	/**
	 * start to get location message.
	 * 
	 * @param provider
	 * @param mContext
	 * @return
	 */
	private boolean startLocation(String provider, final Context mContext) {

		Location location = lm.getLastKnownLocation(provider);

		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				System.out.println(location.toString());
				updateLocation(location, mContext);
			}

			@Override
			public void onProviderDisabled(String arg0) {
				System.out.println(arg0);
			}

			@Override
			public void onProviderEnabled(String arg0) {
				System.out.println(arg0);
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				System.out.println("onStatusChanged");
			}
		};

		// 500ms change again.ignore location.
		lm.requestLocationUpdates(provider, 500, 0, locationListener);

		if (location != null) {
			this.location = location;
			return true;
		}
		return false;

	}

	/**
	 * update location message and show it.
	 * 
	 * @param location
	 * @param mContext
	 */
	private void updateLocation(Location location, Context mContext) {
		if (location != null) {
			pm_tv2.setText("地理位置:" + location.getLongitude() + ','
					+ location.getLatitude());
			// byte n[]=byte[location.getLatitude(),location.getLongitude()];

			lon = String.valueOf(location.getLongitude());
			lat = String.valueOf(location.getLatitude());

			lm.removeUpdates(locationListener);
		} else {
			System.out.println("没有GPS设备");
		}
	}

	/**
	 * Destroy the locationListener.
	 */
	protected void onDestroy() {

		lm.removeUpdates(locationListener);
		super.onDestroy();
	}

	/**
	 * show the detail location message.
	 */
	private void ShowPlace() {
		Geocoder gc = new Geocoder(this, Locale.getDefault());

		List<Address> addresses = null;
		try {
			addresses = gc.getFromLocation(location.getLatitude(),
					location.getLongitude(), 5);
			// save location message.
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) {
				Address a = addresses.get(0);
				for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
					// address.
					sb.append(a.getAddressLine(i));
				}
				pm_et2.setText(sb.toString());
			}
		} catch (IOException e) {

		}
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

		Intent inten = new Intent();
		inten.setClass(Photo_Message.this, Main.class);
		startActivity(inten);
		finish();
		return;
	}

//	private void StartCamera() {
//
//		Intent ca_intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		// ca_intent2.putExtra("autofocus", true);
//
//		ca_intent2.putExtra(MediaStore.EXTRA_OUTPUT,
//				Uri.fromFile(new File(SAVE_PATH_IN_SDCARD, "YouTu.JPEG")));
//
//		startActivityForResult(ca_intent2, 10);
//	}
//
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		// 非常重要！！如果被呼叫的activity中途放弃，就没有返回值，要避免错误
//		if (data == null)
//			return;
//
//		if (requestCode == 10) {
//			if (resultCode == Activity.RESULT_OK) {
//			}
//			if (resultCode == RESULT_CANCELED)
//				super.onRestart();
//		}
//
//		super.onActivityResult(requestCode, resultCode, data);
//	}
}