package loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import lucic.khalique.Runescape.PlayerStats;
import lucic.khalique.Runescape.HomeActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import utilities.DialogUtility;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class StatsLoader extends AsyncTask<String, Void, String>
{
	Context screen;
	String username;
	String userType;
	Dialog loadingDialog;
	Exception exception;
	String[] statScores;
	String[][] skills;
	ArrayList <String> skillsList = new ArrayList<String>();
	public StatsLoader(String username, Context con, String userType)
	{
		this.userType = userType;
		this.username = username;
		screen = con;
		loadingDialog = new Dialog(screen);
	}
	protected void onPreExecute()
	{
		String name = username.replace("+", " ");
		loadingDialog = DialogUtility.createLoadingScreen("Loading " + name + " Stats ..", screen);
		loadingDialog.show();
	}
	@Override
	protected String doInBackground(String... arg0)
	{
		HttpClient client = new DefaultHttpClient();
		while(!isCancelled())
		{
			username = username.replace(" ", "+");
			String url = "http://hiscore.runescape.com/index_lite.ws?player=" + username;
			HttpGet httpGet = new HttpGet(url);
			String result = ""; // Where the resulting webpage will be stored
			try
			{
				HttpResponse response = client.execute(httpGet);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null)
				{
					sb.append(line + NL);
				}
				in.close();
				result = sb.toString(); // Resulting content of the page
				if(result.contains(" "))
				{
					return "incomplete: " + "Found spaces in response for: " + url;
				}
				else
				{
					parseStats(result);
					cacheStats(result);
					return "completed";
				}
			}
			catch(ConnectException ce)
			{
				ce.printStackTrace();
				return "ConnectException";
			}
			catch(FileNotFoundException fnf)
			{
				fnf.printStackTrace();
				return "FileNotFoundException";
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
		loadingDialog.dismiss();
		if(result.equals("completed"))
		{
			Intent intent = new Intent(screen, PlayerStats.class);
			Bundle b = new Bundle();
			b.putStringArrayList("skillList", skillsList);
			intent.putExtras(b);
			screen.startActivity(intent);
		}
		else if(result.equals("FileNotFoundException"))
		{
			if(userType.equals("friend"))
			{
				Toast.makeText(screen, "Your friend's High Score's has not been found.\nAre they still a member?", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(screen, "Your High Score's is not Found.\nAre you still a member?", Toast.LENGTH_LONG).show();
				screen.startActivity(new Intent(screen, HomeActivity.class));
			}
		}
		else if(result.equals("UnknownHostException") || result.equals("ConnectException"))
		{
			Toast.makeText(screen, "Please double check your internet connection ..", Toast.LENGTH_LONG).show();
			screen.startActivity(new Intent(screen, HomeActivity.class));
		}
		else if(result.contains("incomplete"))
		{
			Toast.makeText(screen, "HighScores is unavailable at this time, please try again later ..", Toast.LENGTH_LONG).show();
			DialogUtility.createBugReportDialog(screen, "HighScores2", "HighScoresLoader AsyncTask doInBackground()", exception);
		}
	}
	@Override
	protected void onCancelled()
	{
		loadingDialog.dismiss();
		Toast.makeText(screen, "HighScores response from www.runescape.com is slow, please try again later ..", Toast.LENGTH_LONG).show();
		screen.startActivity(new Intent(screen, HomeActivity.class));
	}
	public void parseStats(String scores)
	{
		StringTokenizer st = new StringTokenizer(scores);
		int count = st.countTokens();
		statScores = new String[count];
		count = 0;
		while (st.hasMoreElements())
		{
			statScores[count] = "" + st.nextElement();
			count++;
		}
		skills = new String[26][3];
		for (int i = 0; i < 26; i++)
		{
			st = new StringTokenizer(statScores[i], ",");
			int count2 = 0;
			while (st.hasMoreElements())
			{
				skills[i][count2] = "" + st.nextElement();
				if (skills[i][count2].contains("-1"))
				{
					skills[i][count2] = "0";
				}
				count2++;
			}
		}
		String tempSkill = "";
		for(int i = 0; i < 26; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				tempSkill+= skills[i][j] + ",";
			}
			skillsList.add("" + i + "," + tempSkill);
			tempSkill = "";
		}
	}
	public void cacheStats(String stats)
	{
		SharedPreferences settings = screen.getSharedPreferences("SETTINGS",0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(username + ".stats", stats);
		editor.commit();
	}
}