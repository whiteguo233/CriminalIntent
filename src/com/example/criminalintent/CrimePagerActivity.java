package com.example.criminalintent;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity
implements CrimeFragment.Callbacks
{
	private ViewPager mViewPager;
	private CrimeLab mCrimeLab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager=new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		mCrimeLab=CrimeLab.get(this);
		FragmentManager fm=getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mCrimeLab.getCount();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				Crime crime=mCrimeLab.getCrimeByPos(arg0);
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		UUID crimeId=(UUID)getIntent().getExtras().getSerializable(CrimeFragment.EXTRA_CRIME_ID);
		mViewPager.setCurrentItem(mCrimeLab.getCrimePos(crimeId));
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Crime crime=mCrimeLab.getCrimeByPos(arg0);
				setTitle(crime.getTitle());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void onCrimeUpdated(Crime crime) {
		// TODO Auto-generated method stub
		
	}
}
