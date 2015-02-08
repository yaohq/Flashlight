package com.example.flashlight;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
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
	
	private void getCamera(){
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
		mLightButton.setChecked(false);
		mFlashButton.setChecked(false);
		mLightButton.setText(R.string.open_light);
		mFlashButton.setText(R.string.open_flash);
		
		//保持屏幕常亮，5分钟自动关闭
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	
		mLightButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(mCamera != null){
						Toast.makeText(Flashlight.this, R.string.light_open_toast, Toast.LENGTH_SHORT).show();
					}else{
						//打开手电时，使打开闪光按钮不可用，否则就会抢夺闪光灯资源
						mFlashButton.setEnabled(false);
						//每次使用闪光灯之前，都要先获取资源
						getCamera();
						OpenLight();
					}
				}else{
					CloseLight();
					//每次关闭闪光灯后，都要释放资源
					mCamera.release();
					mCamera = null;
					//关闭手电时，使闪光按钮可用
					mFlashButton.setEnabled(true);
				}
			}
		});
		
		//闪烁10次后自动停止
		mFlashButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//如果开关被打开
				if(isChecked){
					//如果闪光灯正在被使用，不执行闪光功能，提示“闪光灯正在被使用"
					if(mCamera != null){
						Toast.makeText(Flashlight.this, R.string.light_open_toast, Toast.LENGTH_SHORT).show();
					}else{
						//每次使用闪光灯之前，都要先获取资源
						getCamera();
						//获取闪光灯资源后，要使打开手电和打开闪光两个按钮都不可用，否则本次闪光结束后，会继续执行刚才的点击动作
						mLightButton.setEnabled(false);
						mFlashButton.setEnabled(false);
						
						for(int i = 0; i < 10; i++){
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
						//关闭闪光后，释放资源
						mCamera.release();
						mCamera = null;
						//打开闪光按钮调整为关闭状态
						mFlashButton.setChecked(false);
						//闪光结束后，使两个按钮恢复可用
						mLightButton.setEnabled(true);
						mFlashButton.setEnabled(true);
					}
				}
			}
		});
	}
	
	//Activity销毁时
	@Override
	protected void onDestroy() {
		//如果闪光灯正打开，就关闭闪光灯，释放资源
		if(mCamera != null){
			CloseLight();
			mCamera.release();
			mCamera = null;
		}
		super.onDestroy();
	}
}
