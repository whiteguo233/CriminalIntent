package com.example.criminalintent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CrimeFragment extends Fragment {
	private final String TAG="CrimeFragment";
	public static final String EXTRA_CRIME_ID="CrimeFragment_Crime_Id";
	public static final int REQUEST_DATE=0;
	public static final int REQUEST_PHOTO=1;
	public static final int REQUEST_CONTACT=2;
	private static final String DIALOG_IMAGE="image";
	private Crime mCrime;
	private EditText mTitleText;
	private ImageView mPhotoView;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageButton mPhotoButton;
	private Button mSuspectButton;
	private Callbacks mCallbacks;
	public interface Callbacks{
		void onCrimeUpdated(Crime crime);
	}
	public static CrimeFragment newInstance(UUID crimeId)
	{
		Bundle args=new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		CrimeFragment fragment=new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.d(TAG, "onAttach");
		mCallbacks=(Callbacks)activity;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setHasOptionsMenu(true);
		UUID crimeId=(UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
		
	}
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateView");
		View view=inflater.inflate(R.layout.fragment_crime, container,false);
		mTitleText=(EditText)view.findViewById(R.id.crime_title);
		mDateButton=(Button)view.findViewById(R.id.crime_date);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mmaa");
		mDateButton.setText(sDateFormat.format(mCrime.getDate()));
		if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm=getActivity().getSupportFragmentManager();
				SelectFragment dialog=SelectFragment.newInstance(mCrime.getDate());
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(fm, DatePickerFragment.DIALOG_DATE);
			}
		});
		mSolvedCheckBox=(CheckBox)view.findViewById(R.id.crime_solved);
		mTitleText.setText(mCrime.getTitle());
		mDateButton.setText(mCrime.getDate().toString());
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mCrime.setSolved(isChecked);
				mCallbacks.onCrimeUpdated(mCrime);
			}
		});
		mTitleText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mCrime.setTitle(s.toString());
				getActivity().setTitle(mCrime.getTitle());
				mCallbacks.onCrimeUpdated(mCrime);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		mPhotoButton=(ImageButton)view.findViewById(R.id.crime_image);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),CrimeCameraActivity.class);
				startActivityForResult(intent, REQUEST_PHOTO);
			}
		});
		PackageManager pm=getActivity().getPackageManager();
		boolean hasCamera=pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
				android.hardware.Camera.getNumberOfCameras() >0;
		if (!hasCamera) {
			mPhotoButton.setEnabled(false);
		}
		mPhotoView=(ImageView)view.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Photo photo=mCrime.getPhoto();
				if (photo==null) {
					return;
				}
				FragmentManager fm=getActivity().getSupportFragmentManager();
				String path=getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
		registerForContextMenu(mPhotoView);
		Button reportbButton=(Button)view.findViewById(R.id.crime_reportButton);
		reportbButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport() );
				intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
				startActivity(intent);
			}
		});
		mSuspectButton=(Button)view.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, REQUEST_CONTACT);
			}
		});
		if (mCrime.getSuspect()!=null) {
			mSuspectButton.setText(mCrime.getSuspect());
		}
		return view;
		}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		showPhoto();
		Log.d(TAG, "onStart");

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
		CrimeLab.get(getActivity()).saveCrimes();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");
		PictureUtils.cleanImageView(mPhotoView);

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.d(TAG, "onDetach");
		mCallbacks=null;

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode!=Activity.RESULT_OK) {
			return;
		}
		if (requestCode==REQUEST_DATE) {
			Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			mDateButton.setText(mCrime.getDate().toString());
			mCallbacks.onCrimeUpdated(mCrime);
		}else if (requestCode==REQUEST_PHOTO) {
			String filename=data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME_STRING);
			if(filename!=null)
			{
				Log.i(TAG, filename);
				Photo photo=new Photo(filename);
				Photo old_photo=mCrime.getPhoto();
				if (old_photo!=null) {
					boolean delete_state=getActivity().deleteFile(old_photo.getFilename());
					if (delete_state) {
						Log.i(TAG, "delete success");
					}else {
						Log.i(TAG, "delete fail");
					}
				}
				mCrime.setPhoto(photo);
				mCallbacks.onCrimeUpdated(mCrime);
				showPhoto();
			}
				
		}else if (requestCode == REQUEST_CONTACT) {
			Uri contactUri=data.getData();
			String[] queryFields=new String[] {ContactsContract.Contacts.DISPLAY_NAME};
			Cursor cursor=getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
			if (cursor.getCount()==0) {
				cursor.close();
				return;
			}
			cursor.moveToFirst();
			String suspect=cursor.getString(0);
			mCrime.setSuspect(suspect);
			mSuspectButton.setText(suspect);
			cursor.close();
		}
	}
	private void showPhoto()
	{
		Photo photo=mCrime.getPhoto();
		BitmapDrawable bitmapDrawable=null;
		if (photo!=null) {
			String path=getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
			bitmapDrawable=PictureUtils.getScaleDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(bitmapDrawable);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity())!=null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_crime_delete:
			CrimeLab.get(getActivity()).deleteCrime(mCrime);
			if (NavUtils.getParentActivityName(getActivity())!=null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_photo_delete_crime:
			mPhotoView.setImageDrawable(null);
			PictureUtils.removePhoto(getActivity(),mCrime.getPhoto().getFilename());
			mCrime.setPhoto(null);
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.crime_item_context, menu);
	}
	private String getCrimeReport()
	{
		String solvedString=null;
		if (mCrime.isSolved()) {
			solvedString=getString(R.string.crime_report_solved);
		}else {
			solvedString=getString(R.string.crime_report_unsolved);
		}
		String dateFormat="EEE,MMM dd";
		String datesString=android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();
		String suspect=mCrime.getSuspect();
		if (suspect==null) {
			suspect=getString(R.string.crime_report_no_suspect);
		}else {
			suspect=getString(R.string.crime_report_suspect,suspect);
		}
		String report=getString(R.string.crime_report,mCrime.getTitle(),datesString,solvedString,suspect);
		return report;
	}
	
}
