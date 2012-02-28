package com.visit;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class Map_Photo extends MapActivity {

	private MapView mapview;
	private MapController mapc;
	private GeoPoint geop;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_photo);
		
		mapview=(MapView)findViewById(R.id.map_view);
		
		//设为卫星模式
		mapview.setSatellite(true);
		
		//设置地图支持缩放
		mapview.setBuiltInZoomControls(true);
		
		//设置地点为武汉
		geop=new GeoPoint((int)(30.35*1000000),(int)(114.17*1000000));
		
		//定位到武汉
		mapc.animateTo(geop);
		
		//设置倍数（1-21）
		mapc.setZoom(12);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
