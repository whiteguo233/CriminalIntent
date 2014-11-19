package com.example.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class CrimeListActivity extends SingleFragmentActivity 
implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks
{
	
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new CrimeListFragment();
	}
	@Override
	protected int getLayoutResId()
	{
		return R.layout.activity_masterdetail;
	}
	@Override
	public void onCrimeSelected(Crime crime) {
		// TODO Auto-generated method stub
		if (findViewById(R.id.detailFragmentContainer)==null) {
			Intent intent=new Intent(this,CrimePagerActivity.class);
			intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivity(intent);
		}else {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction fTransaction=fm.beginTransaction();
			Fragment oldDetail=fm.findFragmentById(R.id.detailFragmentContainer);
			Fragment newDetail=CrimeFragment.newInstance(crime.getId());
			if (oldDetail!=null) {
				fTransaction.remove(oldDetail);
			}
			fTransaction.add(R.id.detailFragmentContainer, newDetail);
			fTransaction.commit();
		}
	}
	@Override
	public void onCrimeUpdated(Crime crime) {
		// TODO Auto-generated method stub
		FragmentManager fManager=getSupportFragmentManager();
		CrimeListFragment crimeListFragment=(CrimeListFragment) fManager.findFragmentById(R.id.fragmentContainer);
		crimeListFragment.updateUI();
	}

}
