package com.example.criminalintent;

import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public class SelectFragment extends DialogFragment {
	public static final String EXTRA_DATE="SelectFragment";
	public static final String DIALOG_SELECT="select";
	private Date mDate;
	public static SelectFragment newInstance(Date date)
	{
		Bundle bundle=new Bundle();
		bundle.putSerializable(EXTRA_DATE, date);
		SelectFragment fragment=new SelectFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate=(Date)getArguments().getSerializable(EXTRA_DATE);
		return new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.date_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				FragmentManager fm=getActivity().getSupportFragmentManager();
				DatePickerFragment fragment=DatePickerFragment.newInstance(mDate);
				fragment.setTargetFragment(getTargetFragment(), getTargetRequestCode());
				fragment.show(fm, DatePickerFragment.DIALOG_DATE);
			}
		}).setNegativeButton(R.string.time_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				FragmentManager fm=getActivity().getSupportFragmentManager();
				TimePickerFragment fragment=TimePickerFragment.newInstance(mDate);
				fragment.setTargetFragment(getTargetFragment(), getTargetRequestCode());
				fragment.show(fm, TimePickerFragment.DIALOG_DATE);
				
			}
		}).create();
	}
	
}
