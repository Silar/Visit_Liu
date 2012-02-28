package com.visit;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity get all photos from a Visit and display them.
 *  
 * @author Simple_liu
 *
 */
public class Photo_List extends Activity{

	Button pl_b1,pl_b2,pl_b3;
	ListView pl_list;
	TextView pl_tv1;
	ArrayList<HashMap<String,Object>> pl_listItem= new ArrayList<HashMap<String,Object>>();
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.photo_list);  
        MyApplication.getInstance().addActivity(this);
        
        pl_b1 = (Button) findViewById(R.id.pl_b1);
        pl_b3 = (Button) findViewById(R.id.pl_b3);
        pl_list = (ListView)findViewById(R.id.pl_list);
        pl_tv1 = (TextView)findViewById(R.id.pl_tv1);
        
        
        //List all photos of one File.
        
        //获取从点击VisitList中的游记存储的名字
        String dataN="Fname";
        SharedPreferences sp=getSharedPreferences(dataN, MODE_PRIVATE);
        final String Fn=sp.getString("Fn", "");
        
        Log.i("Photo_List", Fn);
        
        String STORE_NAME="VisitName";
		SharedPreferences set=getSharedPreferences(STORE_NAME, MODE_PRIVATE);
		final String con=set.getString("Vcontent", "");
		
		
		SimpleDateFormat Pformat=new SimpleDateFormat("yyyy/MM/dd hh:mm");
		
		if(Fn!=null){
         
		 //删除"Fname"中的值
		 sp.edit().clear();
			
        File file=new File("sdcard/YouTu"+"/"+Fn+"/");
        File[] pl_files=file.listFiles();
        pl_tv1.setText(Fn);
        
        
        if(pl_files.length >0){
        	//文件按日期排序（最新的排在上面） 
        	Arrays.sort(pl_files, new Photo_List.CompratorByLastModified());
        	for(File mFile : pl_files){
        	//判断获得的内容是文件夹还是文件，如果是文件则列出来.
        		if(mFile.isFile()){
        			if(isJPEG(mFile.toString())){
        			//获取文件创建时间
        				long time=mFile.lastModified();
        			    
        			    Date currentTime=new Date(time);
                		String ping=Pformat.format(currentTime);
        			
        			    Bitmap bt=BitmapFactory.decodeFile(mFile.toString());
        			
        			    String nFile=mFile.getName();
        			    String nf=nFile.substring(0, nFile.lastIndexOf("."));
        			
        			    HashMap<String, Object> map = new HashMap<String, Object>();
                	    map.put("ItemImage", bt);
                        map.put("ItemTitle", nf);
                        map.put("ItemText", ping);
                        pl_listItem.add(map);
                        }
        			}
        	}
        }else{
        	Toast toast=Toast.makeText(getApplicationContext(), "暂时还没有相片，赶紧拍一张吧！", Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER, 0, 0);
        	toast.show();
        }
        }
        else{
        	File file=new File("sdcard/YouTu"+"/"+con+"/");
            File[] pl_files=file.listFiles();
            pl_tv1.setText(con);
            //文件按日期排序（最新的排在上面）
            Arrays.sort(pl_files, new Photo_List.CompratorByLastModified());
           
            if(pl_files.length >0){
            for(File mFile : pl_files){
            	//判断获得的内容是文件夹还是文件，如果是文件则列出来.
            	if(mFile.isFile()){
            		long time=mFile.lastModified();
    			    
    			    Date currentTime=new Date(time);
            		String ping=Pformat.format(currentTime);
        			
        			String nFile=mFile.getName();
        			String nf=nFile.substring(0, nFile.lastIndexOf("."));
        			
        			Bitmap bt=BitmapFactory.decodeFile(mFile.toString());
            		
        			HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemImage", bt);
                    map.put("ItemTitle", nf);
                    map.put("ItemText", ping);
                    pl_listItem.add(map);
            	}
            }
           }
        	
        }
      //生成动态数组，加入数据. 
      
        	
        //生成适配器的Item和动态数组对应的元素
        final SimpleAdapter listItemAdapter = new SimpleAdapter(this,pl_listItem,//数据源   
                R.layout.photo_item,//ListItem的XML实现  
                //动态数组与ImageItem对应的子项          
                new String[] {"ItemImage","ItemTitle", "ItemText"},   
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] {R.id.ItemI,R.id.ItemT1,R.id.ItemT2}  
            );  
        
        //在Listview中实现显示图片.
        listItemAdapter.setViewBinder(new ViewBinder(){

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// TODO Auto-generated method stub
				if(view instanceof ImageView && data instanceof Bitmap){   
					ImageView iv = (ImageView) view;   
					iv.setImageBitmap((Bitmap) data);   
					return true;   
					}else
						return false; 
			}
        	
        });
        //添加并且显示  
        pl_list.setAdapter(listItemAdapter);
        
       
        //长按确定是否删除照片
       pl_list.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			
			final AdapterView.AdapterContextMenuInfo info=
					(AdapterView.AdapterContextMenuInfo)menuInfo;
			
			new AlertDialog.Builder(Photo_List.this)
			/*弹窗口最上面的文字*/
			.setTitle("提示信息")
			
			.setIcon(R.drawable.icon)
			
			.setMessage("确定删除当前相片？")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					//获取位置索引,mListPos为listview选项的位置 
					int mListPos=info.position;
					//获取对应HashMap的数据内容
					HashMap<String,Object> ma=pl_listItem.get(mListPos);
					//listview删除该Item
					pl_listItem.remove(mListPos);
					
					//获取图片对应的名字
					String s=ma.get("ItemTitle").toString();
					Log.d("Photo_List", s);
					//获取对应图片的地址，并进行删除图片操作
					
					if(Fn!=null){
						File f=new File("sdcard/YouTu"+"/"+Fn+"/"+s+".JPEG");
						Log.d("Photo_List", f.getName().toString());
						f.delete();
					}
					
					listItemAdapter.notifyDataSetChanged();
					
				}
			})
			
			.setNegativeButton("否", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
			
		}
		});
       
       
        //return the Visit_list activity.
        pl_b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent pl_intent1 =new Intent();
				pl_intent1.setClass(Photo_List.this, Visit_List.class);
				startActivity(pl_intent1);
			}
		});
        
        
        //take new photos.
        pl_b3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String STORE_NAME="VisitName";
				SharedPreferences set=getSharedPreferences(STORE_NAME,MODE_PRIVATE);
				SharedPreferences.Editor editor=set.edit();
				
				if(Fn!=null)editor.putString("Vcontent", Fn);
				else editor.putString("Vcontent", con);
				editor.commit();
				
				Intent pl_intent3 =new Intent();
				pl_intent3.setClass(Photo_List.this, mCamera.class);
				startActivity(pl_intent3);
			}
		});
        
	}
	
    
	

	//判断文件是否是JPEG文件.
	protected boolean isJPEG(String stri){
		
		if(stri.endsWith(".txt"))return false;
		
		else return true;
		
	}
	
	//对比两个文件创建时间.
	static class CompratorByLastModified implements Comparator<File>  
	  {  
	   public int compare(File f1, File f2) {  
	    long diff = f1.lastModified()-f2.lastModified();  
	        if(diff>0)  
	          return -1;  
	        else if(diff==0)  
	          return 0;  
	        else  
	          return 1;  
	        }  
	  public boolean equals(Object obj){  
	    return true;  
	    }  
	  }  
}
