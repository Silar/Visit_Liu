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

/**
 * Create new Visit.
 * 
 * @author Simple_liu
 *
 */
public class New_Visit extends Activity{

	private Button nv_b1,nv_b2;
	private EditText nv_et1,nv_et2;
	private TextView nv_t1,nv_t2,nv_t3,nv_time1,nv_time2,nv_time3;
	private ImageView nv_iv1,nv_iv2,nv_iv3;

	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.new_visit);  
        MyApplication.getInstance().addActivity(this);

        nv_b1 =(Button) findViewById(R.id.nv_b1);
        nv_b2 =(Button) findViewById(R.id.nv_b2);
        nv_et1 =(EditText) findViewById(R.id.nv_et1);
        nv_et2 =(EditText) findViewById(R.id.nv_et2);
        nv_t1 =(TextView) findViewById(R.id.test1);
        nv_t2 =(TextView) findViewById(R.id.test2);
        nv_t3 =(TextView) findViewById(R.id.test3);
        nv_time1 =(TextView) findViewById(R.id.time1);
        nv_time2 =(TextView) findViewById(R.id.time2);
        nv_time3 =(TextView) findViewById(R.id.time3);
        nv_iv1 =(ImageView) findViewById(R.id.nv_iv1);
        nv_iv2 =(ImageView) findViewById(R.id.nv_iv2);
        nv_iv3 =(ImageView) findViewById(R.id.nv_iv3);
        
        //显示已有游记
        
        File file=new File("sdcard/YouTu");
        File[] files=file.listFiles();
        Arrays.sort(files, new New_Visit.CompratorByLastModified());
        
        
        String i=String.valueOf(files.length);
        Log.d("测试", i);
        
        //获取游记创建时间
        //自定义文件创建时间显示	
        SimpleDateFormat Nformat=new SimpleDateFormat("yyyy.MM.dd");
    	
        //只有一个游记.
        if(files.length ==1){ 
        	File f1=files[0];
        	Log.d("New_Visit_列表", f1.getName().toString());
        	
        	long time= f1.lastModified();
        	Date currentTime=new Date(time);
    		String tim1=Nformat.format(currentTime);
         	
    		nv_t1.setText(f1.getName().toString().trim());
    		nv_time1.setText(tim1);
         	
         	
         	
         	File [] ph1=f1.listFiles();
         	if(ph1.length>0){
         		
         		Arrays.sort(ph1, new New_Visit.CompratorByLastModified());
         		
         		File p1=ph1[0];
         		String st1="sdcard/YouTu"+"/"+f1.getName().toString().trim()+"/"+p1.getName().toString().trim();
         		
         		Log.d("列表", st1);
         		Bitmap bit1=BitmapFactory.decodeFile(st1);
         		nv_iv1.setImageBitmap(bit1);
         	}
         	
         	
        }
        
        //两个游记时.
        if(files.length ==2){
        	File f1=files[0];
        	File f2=files[1];
        	
        	long time1= f1.lastModified();
        	Date currentTime1=new Date(time1);
    		String tim1=Nformat.format(currentTime1);
    		
    		long time2= f2.lastModified();
        	Date currentTime2=new Date(time2);
    		String tim2=Nformat.format(currentTime2);
        	
        	nv_t1.setText(f1.getName().toString());
        	nv_t2.setText(f2.getName().toString());
        	Log.d("New_Visit_列表", f1.getName().toString());
            Log.d("New_Visit_列表", f2.getName().toString());
            
            nv_time1.setText(tim1);
            nv_time2.setText(tim2);
            
            File [] ph1=f1.listFiles();
            if(ph1.length>0){
         		
         		Arrays.sort(ph1, new New_Visit.CompratorByLastModified());
         		File p1=ph1[0];
         		String st1="sdcard/YouTu"+"/"+f1.getName().toString().trim()+"/"+p1.getName().toString().trim();

         		Log.d("列表", st1);
         		Bitmap bit1=BitmapFactory.decodeFile(st1);
         		nv_iv1.setImageBitmap(bit1);
            }
            
            File [] ph2=f2.listFiles();
            if(ph2.length>0){
            	
            	Arrays.sort(ph2, new New_Visit.CompratorByLastModified());
         		File p2=ph2[0];
         		String st2="sdcard/YouTu"+"/"+f2.getName().toString().trim()+"/"+p2.getName().toString().trim();
         		Bitmap bit2=BitmapFactory.decodeFile(st2);
         		nv_iv2.setImageBitmap(bit2);
            }
        }
        
        //三个游记以上时.
        if(files.length >=3){
        	 File f1=files[0];
             File f2=files[1];
             File f3=files[2];
             
             long time1= f1.lastModified();
         	 Date currentTime1=new Date(time1);
     		 String tim1=Nformat.format(currentTime1);
     		
     		 long time2= f2.lastModified();
         	 Date currentTime2=new Date(time2);
     		 String tim2=Nformat.format(currentTime2);
     		 
     		 long time3= f3.lastModified();
        	 Date currentTime3=new Date(time3);
    		 String tim3=Nformat.format(currentTime3);
         	
     		 nv_time1.setText(tim1);
             nv_time2.setText(tim2);
             nv_time3.setText(tim3);
     		 
     		 
             nv_t1.setText(f1.getName().toString());
             nv_t2.setText(f2.getName().toString());
             nv_t3.setText(f3.getName().toString());
             
             Log.d("New_Visit_列表", f1.getName().toString());
             Log.d("New_Visit_列表", f2.getName().toString());
             Log.d("New_Visit_列表", f3.getName().toString());
             
             File [] ph1=f1.listFiles();
             if(ph1.length>0){
          		
          		 Arrays.sort(ph1, new New_Visit.CompratorByLastModified());
          		 File p1=ph1[0];
          		 String st1="sdcard/YouTu"+"/"+f1.getName().toString().trim()+"/"+p1.getName().toString().trim();

          		 Log.d("列表", st1);
          		 Bitmap bit1=BitmapFactory.decodeFile(st1);
          		 nv_iv1.setImageBitmap(bit1);
             }
             
             File [] ph2=f2.listFiles();
             if(ph2.length>0){
             	
             	 Arrays.sort(ph2, new New_Visit.CompratorByLastModified());
          		 File p2=ph2[0];
          		 String st2="sdcard/YouTu"+"/"+f2.getName().toString().trim()+"/"+p2.getName().toString().trim();
          		 Bitmap bit2=BitmapFactory.decodeFile(st2);
          		 nv_iv2.setImageBitmap(bit2);
             }
             
             File [] ph3=f3.listFiles();
             if(ph3.length>0){
            	
            	 Arrays.sort(ph3, new New_Visit.CompratorByLastModified());
           		 File p3=ph3[0];
           		 String st3="sdcard/YouTu"+"/"+f3.getName().toString().trim()+"/"+p3.getName().toString().trim();
           		 Bitmap bit3=BitmapFactory.decodeFile(st3);
           		 nv_iv3.setImageBitmap(bit3);
             }
        }
       
        
        
       
        
        
        
        
        // 保存游记
        nv_b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//trim()方法是将String前后的空格去掉
				String name =nv_et1.getText().toString().trim();
				String miaosu =nv_et2.getText().toString().trim();
				
				//保存新建游记的名字到SharedPreferences--"Visit_Name"中
				
				SharedPreferences set=getSharedPreferences("VisitName",MODE_PRIVATE);
				SharedPreferences.Editor editor=set.edit();
				editor.putString("Vcontent", name);
				editor.commit();
				
				//创建游记文件夹
				MakeDirs("YouTu/"+name);
				
				//保存游记相关信息到SharedPreferences--"Visit_Message"中
				SharedPreferences vm=getSharedPreferences("Visit_Message", 0);
				SharedPreferences.Editor ve=vm.edit();
				//将游记的名字作为key，保存相应游记的信息.
				ve.putString(name, miaosu);
				ve.commit();
				
				//跳转页面
				Intent nv_intent1= new Intent();
				nv_intent1.setClass(New_Visit.this, Begin_Visit.class);
				startActivity(nv_intent1);
			}
        	
        });
        
        //取消创建游记.
        nv_b2.setOnClickListener(new OnClickListener(){

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
	 * @return
	 */
	public static boolean IsSdCard() 
	{ 
		boolean nIsSaveSdCard=false; 
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
			nIsSaveSdCard= true; 
		else 
			nIsSaveSdCard= false; 
		return nIsSaveSdCard; 
		} 


	public static boolean MakeDirs(String aPath) 
	{ 
		boolean nIsExist=false; 
		if (IsSdCard()) { 
			File sdCardDir = Environment.getExternalStorageDirectory();
			String p = sdCardDir.getAbsolutePath()+"/"+aPath;
			Log.i("New_Visit.MakeDirs", p);
	        File nPath=new File(p); 
	        nIsExist=nPath.exists(); 
	        if(!nIsExist) 
	        	nIsExist=nPath.mkdirs(); 
	        }	
		else 
		{ 
			File nPath=new File("./"+aPath); 
			nIsExist=nPath.exists(); 
			if(!nIsExist) 
				nIsExist=nPath.mkdirs(); 
			} 
		return nIsExist; 
		} 

	
	public static FileOutputStream GetFileOutStream(String aFileName,boolean aIsApp) 
	{ 
		try { 
			if (IsSdCard()) { 
				File sdCardDir = Environment.getExternalStorageDirectory();
				File saveFile = new File(sdCardDir, aFileName); 
				FileOutputStream outStream = new FileOutputStream(saveFile,aIsApp); 
				return outStream; 
				} 
			else 
			{ 
				File saveFile = new File("./", aFileName); 
				FileOutputStream outStream = new FileOutputStream(saveFile,aIsApp); 
				return outStream; 
				} 
			} catch (Exception e) { 
				e.printStackTrace(); 
				return null; 
				} 
		}
	
	/**
	 * 对比文件创建时间
	 * @author Simple_liu
	 *
	 */
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
