package com.example.criminalintent;


import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG="CrimeCameraFragment";
	private View mProgressContainer;
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	public static final String EXTRA_PHOTO_FILENAME_STRING="photo_filename";
	private Camera.ShutterCallback mShutterCallback=new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	private Camera.PictureCallback mPictureCallback=new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			String filename=UUID.randomUUID().toString()+".jpg";
			FileOutputStream osFileOutputStream=null;
			boolean success=true;
			try {
				osFileOutputStream=getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				osFileOutputStream.write(data);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG,filename, e);
				success=false;
			}finally{
				try {
					if (osFileOutputStream!=null) {
						osFileOutputStream.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
					success=false;
				}
			}
			if (success) {
				Log.i(TAG, "save at "+filename);
				Intent intent=new Intent();
				intent.putExtra(EXTRA_PHOTO_FILENAME_STRING, filename);
				getActivity().setResult(Activity.RESULT_OK, intent);
			}else {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
		}
	};
	private Size getBestSupportedSize(List<Size> sizes,int width,int height)
	{
		Size bestSize=sizes.get(0);
		int largestArea=bestSize.width*bestSize.height;
		for (Size size : sizes) {
			int area=size.width*size.height;
			if (area>largestArea) {
				bestSize=size;
				largestArea=area;
			}
		}
		return bestSize;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_crime_camera,container,false);
		Button takePicButton=(Button)view.findViewById(R.id.crime_camera_takePic);
		takePicButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCamera.takePicture(mShutterCallback, null, mPictureCallback);
			}
			
		});
		mSurfaceView=(SurfaceView)view.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder=mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try {
					if (mCamera!=null) {
						mCamera.setPreviewDisplay(holder);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if (mCamera!=null) {
					mCamera.stopPreview();
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				Camera.Parameters parameters=mCamera.getParameters();
				Size s=getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try {
					mCamera.startPreview();
				} catch (Exception e) {
					// TODO: handle exception
					mCamera.release();
					mCamera=null;
				}
				
				
			}
		});
		mProgressContainer=view.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		return view;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mCamera!=null) {
			mCamera.release();
			mCamera=null;
		}
	}
	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCamera=android.hardware.Camera.open(0);
	}
	
	
}
