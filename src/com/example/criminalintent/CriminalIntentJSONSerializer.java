package com.example.criminalintent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class CriminalIntentJSONSerializer {
	private Context mContext;
	private String mFilename;
	public CriminalIntentJSONSerializer (Context context,String f)
	{
		mContext=context;
		mFilename=f;
	}
	public ArrayList<Crime> loadCrimes() throws IOException,JSONException
	{
		ArrayList<Crime> crimes=new ArrayList<Crime>();
		BufferedReader reader=null;
		try {
			InputStream inputStream=mContext.openFileInput(mFilename);
			reader=new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder jsonStringBuilder=new StringBuilder();
			String line=null;
			while ((line=reader.readLine())!=null) {
				jsonStringBuilder.append(line);
				
			}
			JSONArray array=(JSONArray)new JSONTokener(jsonStringBuilder.toString()).nextValue();
			for (int i = 0; i < array.length(); i++) {
				crimes.add(new Crime(array.getJSONObject(i)));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			if (reader!=null) {
				reader.close();
			}
		}
		return crimes;
	}
	public void saveCrimes(ArrayList<Crime> crimes)
	throws JSONException,IOException{
		JSONArray array=new JSONArray();
		for (Crime crime : crimes) {
			array.put(crime.toJSON());
		}
		Writer writer=null;
		try {
			OutputStream outputStream=mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer=new OutputStreamWriter(outputStream);
			writer.write(array.toString());
		}catch(Exception e)
		{
			Log.e("saveCrimes","save error",e);
		}
		finally
		{
			if (writer!=null) {
				writer.close();
			}
		}
	}
}
