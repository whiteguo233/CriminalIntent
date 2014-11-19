package com.example.criminalintent;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
	private final String TAG="CrimeListFragment";
	public static final int REQUEST_DATE=0;
	private ArrayList<Crime> mCrimes;
	private boolean mSubtitleVisible;
	private Callbacks mCallbacks;
	public interface Callbacks{
		void onCrimeSelected(Crime crime);
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
		setRetainInstance(true);
		setHasOptionsMenu(true);
		mSubtitleVisible=false;
		getActivity().setTitle(R.string.crimes_title);
		mCrimes=CrimeLab.get(getActivity()).getCrimes();
		CrimeAdapter adapter=new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
		
	}
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateView");
		if (mSubtitleVisible) {
			getActivity().getActionBar().setSubtitle(R.string.subtitle);
		}
		View v= super.onCreateView(inflater, container, savedInstanceState);
		ListView listView=(ListView)v.findViewById(android.R.id.list);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
		}else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					MenuInflater inflater=mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
						CrimeLab crimeLab=CrimeLab.get(getActivity());
						ListView listView=getListView();
						for (int i = 0; i < adapter.getCount(); i++) {
							if (listView.isItemChecked(i)) {
								crimeLab.deleteCrime(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
					}
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode mode, int position,
						long id, boolean checked) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return v;
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
		View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_list_empty, null);
		ListView listView=getListView();
		ViewGroup viewGroup=(ViewGroup)listView.getParent();
		viewGroup.addView(view);
		listView.setEmptyView(view);
		super.onStart();
		Log.d(TAG, "onStart");

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		((ArrayAdapter<Crime>)getListAdapter()).notifyDataSetChanged();
		Log.d(TAG, "onPause");

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
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Crime crime=((CrimeAdapter)getListAdapter()).getItem(position);
		Log.d(TAG, crime.getTitle()+"was clicked");
		mCallbacks.onCrimeSelected(crime);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle=menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle !=null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime crime=new Crime();
			CrimeLab.get(getActivity()).addCrime(crime);
//			Intent intent=new Intent(getActivity(),CrimePagerActivity.class);
//			intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//			startActivityForResult(intent, 0);
			mCallbacks.onCrimeSelected(crime);
			((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
			return true;
		case R.id.menu_item_show_subtitle:
			if (getActivity().getActionBar().getSubtitle()==null) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				mSubtitleVisible=true;
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				mSubtitleVisible=false;
				item.setTitle(R.string.show_subtitle);
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);		}
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int position=info.position;
		CrimeAdapter adapter=(CrimeAdapter)getListAdapter();
		Crime crime=adapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			adapter.notifyDataSetChanged();
			return true;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}

	private class CrimeAdapter extends ArrayAdapter<Crime>
	{
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (convertView==null) {
				convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}
			Crime crime=getItem(position);
			TextView titleTextView=(TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(crime.getTitle());
			TextView dateTextView=(TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(crime.getDate().toString());
			CheckBox solvedcCheckBox=(CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedcCheckBox.setChecked(crime.isSolved());
			Log.d("getView",crime.getTitle()+"-"+String.valueOf(position));
			return convertView;
		}
	
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(),0, crimes);
			// TODO Auto-generated constructor stub
		}
		
	}
	public void updateUI()
	{
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
}
