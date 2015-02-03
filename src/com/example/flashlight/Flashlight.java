package com.example.flashlight;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Flashlight extends Activity {
	private ToggleButton mLightButton;
	private ToggleButton mFlashButton;
	private Camera mCamera;
	private Parameters mParams;
	
	public Flashlight(){
		//Constructor
		//System.out.println("Flashlight start....");
	}
	
	private void OpenLight(){
		mParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
		mCamera.setParameters(mParams);
		mCamera.startPreview();
//		System.out.println("open light...");
	}
	
	private void CloseLight(){
		mParams.setFlashMode(Parameters.FLASH_MODE_OFF);
		mCamera.setParameters(mParams);
		mCamera.stopPreview();
//		mCamera.release();
//		System.out.println("close light...");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flashlight);
		
		mLightButton = (ToggleButton)findViewById(R.id.light);
		mFlashButton = (ToggleButton)findViewById(R.id.flash);
		//设置打开应用时的默认显示
		mLightButton.setText(R.string.open_light);
		mFlashButton.setText(R.string.open_flash);
		
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
		
		//保持屏幕常亮，5分钟自动关闭
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	
		mLightButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
//					mFlashButton.setEnabled(false);
					OpenLight();
				}else{
					CloseLight();
					mCamera.release();
//					mFlashButton.setEnabled(true);
				}
			}
		});
		
		//闪烁10次后自动停止
		mFlashButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				mFlashButton.setEnabled(false);
//				mLightButton.setEnabled(false);
				//如果开关已打开
				if(isChecked){
					int i = 0;
					while(i++ < 10){
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						OpenLight();
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						CloseLight();
					}
					mCamera.release();
					mFlashButton.setChecked(false);
				}
				
//				mFlashButton.setEnabled(true);
//				mLightButton.setEnabled(true);
			}
		});
	}
}
