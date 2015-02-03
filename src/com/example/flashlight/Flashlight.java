package com.example.flashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Flashlight extends Activity {
	private ToggleButton mButton;
	private Camera mCamera;
	private Parameters mParams;
	
	public Flashlight(){
		//constuctor
		//System.out.println("Flashlight start....");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flashlight);
		
		mButton = (ToggleButton)findViewById(R.id.open_close);
		
		try{
			mCamera = Camera.open();
			System.out.println("mCamera = " + mCamera.toString());
		}catch(NullPointerException e){
			System.out.println(e);
			System.exit(-1);
		}catch(Exception e){
			System.out.println(e);
			System.exit(-1);
		}
		
		mParams = mCamera.getParameters();
		
		//保持屏幕常亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
		
		mButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//打开开关按钮
				if(isChecked){
					mParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(mParams);
					mCamera.startPreview();
				}else{
					mParams.setFlashMode(Parameters.FLASH_MODE_OFF);
					mCamera.stopPreview();
					mCamera.release();
				}
				//打开或者关闭闪光灯
			}
		});
	}

}
