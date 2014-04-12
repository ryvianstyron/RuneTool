package lucic.khalique.Runescape;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utilities.DialogUtility;
import utilities.Utility;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Time extends Activity
{
	Exception exception;
	TextView time;
	Button timeBorder;
	String result = "";
	String result2 = "";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	Dialog dialog;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time);
		timeBorder = (Button)findViewById(R.id.timeBorder);
		timeBorder.setText("");
		timeBorder.setVisibility(View.GONE);
		Utility.callLoaderAndHandler(new TimeLoader());
	}
	public void onResume()
	{
		super.onResume();
		SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		String registered = settings.getString("registered","null");
		if(registered != null && registered.equals("no"))
		{
			finish();
		}
	}
	public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.runescape_menu, menu);
        return true;
    }
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		editor = settings.edit();
	     switch (item.getItemId()) 
	     {
	         case R.id.logout:
	        	 editor.clear();
	        	 editor.putString("registered","no");
	        	 editor.commit();
	        	 Intent intent = new Intent(Time.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         default:return super.onOptionsItemSelected(item);
	     }
	}
	private class TimeLoader extends AsyncTask<String, Void, String>
	{
		protected void onPreExecute()
		{
			dialog = DialogUtility.createLoadingScreen("Loading Forum Time ..", Time.this);
			dialog.show();
		}
		@Override
		protected String doInBackground(String... arg0)
		{
			while(!isCancelled())
			{
				String url = "http://services.runescape.com/m=toolbar/jagextime2.ws";
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(url);
				result = ""; // result of http Post
				try
				{
					HttpResponse response = httpclient.execute(httpget); // Execute HTTP Post Request
	
					// Extract the page content returned from the response
					BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					StringBuffer sb = new StringBuffer("");
					String line = "";
					String NL = System.getProperty("line.separator");
					while ((line = in.readLine()) != null)
					{
						sb.append(line + NL);
					}
					in.close();
					String array1[];
					String array2[];
					result = sb.toString();
					array1 = result.split("<DEFAULT_BUTTON_TEXT>");
					array2 = array1[1].split("</DEFAULT_BUTTON_TEXT>");
					result2 = array2[0];
					result2 = result2.substring(9,14);
					return "completed";	
				} 
				catch(ConnectException ce)
				{
					ce.printStackTrace();
					return "ConnectException";
				}
				catch(UnknownHostException unh)
				{
					unh.printStackTrace();
					return "UnknownHostException";
				}
				catch (Exception e)
				{
					e.printStackTrace();
					exception = e;
					return "incomplete";
				}
			}
			return "cancelled";
		}
		protected void onPostExecute(String result)
		{
			System.out.println(result);
			if(result.equals("completed") && dialog.isShowing())
			{
				timeBorder.setVisibility(View.VISIBLE);
				timeBorder.setText(result2);
				dialog.dismiss();
			}
			else if(result.equals("UnknownHostException") || result.equals("ConnectException"))
			{
				dialog.dismiss();
				Toast.makeText(Time.this, "Please double check your internet connection ..", Toast.LENGTH_LONG).show();
				startActivity(new Intent(Time.this, HomeActivity.class));
			}
			if(result.contains("incomplete"))
			{
				Toast.makeText(Time.this, "Unable to load Forum Time, please try again later ..", Toast.LENGTH_LONG).show();
				dialog.dismiss();
				DialogUtility.createBugReportDialog(Time.this, "Time", "TimeLoader AsyncTask doInBackground()", exception);
			}
		}
		protected void onCancelled()
		{
			dialog.dismiss();
			Toast.makeText(Time.this, "Response from www.runescape.com is slow, please try again later ..", Toast.LENGTH_LONG).show();
			startActivity(new Intent(Time.this, HomeActivity.class));
		}
	}
}
