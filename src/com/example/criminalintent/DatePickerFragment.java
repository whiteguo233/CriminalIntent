package com.example.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
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
	public static DatePickerFragment newInstance(Date date)
	{
		Bundle bundle=new Bundle();
		bundle.putSerializable(EXTRA_DATE, date);
		DatePickerFragment fragment=new DatePickerFragment();
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
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		DatePicker datePicker=(DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(year, month, day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				mDate=new GregorianCalendar(year , monthOfYear , dayOfMonth).getTime();
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
