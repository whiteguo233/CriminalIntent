package com.example.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class CrimeLab {
	private static final String TAG="CrimeLab";
	private static final String FILENAME="crimes.json";
	private static CrimeLab sCrimeLab;
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mCriminalIntentJSONSerializer;
	private Context mContext;
	private CrimeLab(Context context)
	{
		this.mContext=context;
		mCriminalIntentJSONSerializer=new CriminalIntentJSONSerializer(context, FILENAME);
		try {
			mCrimes=mCriminalIntentJSONSerializer.loadCrimes();
		} catch (Exception e) {
			// TODO: handle exception
			mCrimes=new ArrayList<Crime>();
			Log.e(TAG, "loading e",e);
		}
	}
	public static CrimeLab get(Context context)
	{
		if (sCrimeLab==null) {
			sCrimeLab=new CrimeLab(context.getApplicationContext());
		}
		return sCrimeLab;
	}
	public ArrayList<Crime> getCrimes()
	{
		return this.mCrimes;
	}
	public int getCount()
	{
		return this.mCrimes.size();
	}
	public Crime getCrimeByPos(int pos)
	{
		return mCrimes.get(pos);
	}
	public Crime getCrime(UUID id)
	{
		for (Crime c:mCrimes) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}
	public int getCrimePos(UUID id)
	{
		
		for (int i = 0; i < mCrimes.size(); i++) {
			if (mCrimes.get(i).getId().equals(id)) {
				return i;
			}
		}
		return 0;
	}
	public void addCrime(Crime crime)
	{
		mCrimes.add(crime);
	}
	public void deleteCrime(Crime crime)
	{
		Photo photo=crime.getPhoto();
		if (photo!=null) {
			PictureUtils.removePhoto(mContext, photo.getFilename());
		}
		mCrimes.remove(crime);
	}
	public boolean saveCrimes()
	{
		try {
			mCriminalIntentJSONSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "save to file");
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "saveCrimes",e);
			return false;
		}
	}
}
