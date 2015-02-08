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
		//���ô�Ӧ��ʱ��Ĭ����ʾ
		mLightButton.setChecked(false);
		mFlashButton.setChecked(false);
		mLightButton.setText(R.string.open_light);
		mFlashButton.setText(R.string.open_flash);
		
		//������Ļ������5�����Զ��ر�
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	
		mLightButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(mCamera != null){
						Toast.makeText(Flashlight.this, R.string.light_open_toast, Toast.LENGTH_SHORT).show();
					}else{
						//���ֵ�ʱ��ʹ�����ⰴť�����ã�����ͻ������������Դ
						mFlashButton.setEnabled(false);
						//ÿ��ʹ�������֮ǰ����Ҫ�Ȼ�ȡ��Դ
						getCamera();
						OpenLight();
					}
				}else{
					CloseLight();
					//ÿ�ιر�����ƺ󣬶�Ҫ�ͷ���Դ
					mCamera.release();
					mCamera = null;
					//�ر��ֵ�ʱ��ʹ���ⰴť����
					mFlashButton.setEnabled(true);
				}
			}
		});
		
		//��˸10�κ��Զ�ֹͣ
		mFlashButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//������ر���
				if(isChecked){
					//�����������ڱ�ʹ�ã���ִ�����⹦�ܣ���ʾ����������ڱ�ʹ��"
					if(mCamera != null){
						Toast.makeText(Flashlight.this, R.string.light_open_toast, Toast.LENGTH_SHORT).show();
					}else{
						//ÿ��ʹ�������֮ǰ����Ҫ�Ȼ�ȡ��Դ
						getCamera();
						//��ȡ�������Դ��Ҫʹ���ֵ�ʹ�����������ť�������ã����򱾴���������󣬻����ִ�иղŵĵ������
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
						//�ر�������ͷ���Դ
						mCamera.release();
						mCamera = null;
						//�����ⰴť����Ϊ�ر�״̬
						mFlashButton.setChecked(false);
						//���������ʹ������ť�ָ�����
						mLightButton.setEnabled(true);
						mFlashButton.setEnabled(true);
					}
				}
			}
		});
	}
	
	//Activity����ʱ
	@Override
	protected void onDestroy() {
		//�����������򿪣��͹ر�����ƣ��ͷ���Դ
		if(mCamera != null){
			CloseLight();
			mCamera.release();
			mCamera = null;
		}
		super.onDestroy();
	}
}
