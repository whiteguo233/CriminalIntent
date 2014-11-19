package com.example.criminalintent;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class TimePickerFragment extends DialogFragment {
	public static final String DIALOG_DATE="date";
	public static final String EXTRA_DATE="change_date";
	private Date mDate;
	private void sendResult(int resultCode)
	{
		if (getTargetFragment()==null) {
			return;
		}
		Intent intent=new Intent();
		intent.putExtra(EXTRA_DATE, mDate);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
	public static TimePickerFragment newInstance(Date date)
	{
		Bundle bundle=new Bundle();
		bundle.putSerializable(EXTRA_DATE, date);
		TimePickerFragment fragment=new TimePickerFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mDate=(Date)getArguments().getSerializable(EXTRA_DATE);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(mDate);
		int hour=calendar.get(Calendar.HOUR);
		int min=calendar.get(Calendar.MINUTE);
		View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		TimePicker timePicker=(TimePicker)v.findViewById(R.id.dialog_time_timePicker);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(min);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(mDate);
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				mDate=calendar.getTime();
				getArguments().putSerializable(EXTRA_DATE, mDate);
				
			}
		});
		return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sendResult(Activity.RESULT_OK);
				
			}
		}).create();
	}
}
