package com.example.criminalintent;

import java.util.UUID;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CrimeActivity extends SingleFragmentActivity
implements CrimeFragment.Callbacks
{
	private static final String TAG="CrimeActivity";
	
    @Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");	
	}
	@Override
    protected void onRestart()
    {
    	super.onRestart();
    	Log.d(TAG, "onRestart");
    }
    @Override
	protected void onResume()
	{
		super.onResume();
		Log.d(TAG, "onResume");

		
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.d(TAG, "onPause");
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");
	}
	@Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	Log.d(TAG, "onDestroy");

    }
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		UUID crimeId=(UUID)intent.getExtras().getSerializable(CrimeFragment.EXTRA_CRIME_ID);
		CrimeFragment fragment=CrimeFragment.newInstance(crimeId);
		return fragment;
	}
	@Override
	public void onCrimeUpdated(Crime crime) {
		// TODO Auto-generated method stub
		
	}
}
