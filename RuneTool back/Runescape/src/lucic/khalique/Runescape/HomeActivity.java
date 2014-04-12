package lucic.khalique.Runescape;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import loaders.AlogLoader;
import loaders.NewsLoader;
import loaders.StatsLoader;
import utilities.DialogUtility;
import utilities.LoginSessionTracker;
import utilities.Utility;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class HomeActivity extends Activity
{
	Exception exception;
	String username = "";
	Button alog;
	Button news;
	Button forumTime;
	Button hiScores;
	Button ge;
	Button friends;
	EditText usernameEntry;
	SharedPreferences preferences;
	ImageView image;
	TextView usernametv;
	Dialog dialog;
	Boolean resyncRequired = false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		friends = (Button)findViewById(R.id.friendButton);
		forumTime = (Button)findViewById(R.id.timeButton);
		news = (Button) findViewById(R.id.newsButton);
		alog = (Button) findViewById(R.id.alogButton);
		hiScores = (Button)findViewById(R.id.hiScoresButton);
		ge = (Button)findViewById(R.id.geButton);
		
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		String loginCherryPopped = settings.getString("loginCherryPopped", null);
		String lastLoginTime = settings.getString("lastLoginTime", null);
		if(loginCherryPopped != null && loginCherryPopped.equals("yes"))
		{
			dialog = DialogUtility.createTutorialDialog(HomeActivity.this);
			dialog.show();
			editor.putString("loginCherryPopped", "no");
			editor.commit();
		}
		if(lastLoginTime == null)
		{
			editor.putString("lastLoginTime", fmt.format(date));
			Toast.makeText(this, "LastLoginTime:" + fmt.format(date), Toast.LENGTH_LONG).show();
			editor.commit();
			resyncRequired = true;
		}
		else if (lastLoginTime != null)
		{
			int lastLoginTimeHour = LoginSessionTracker.getLastLoginSession(lastLoginTime);
			int nowLoginTimeHour = LoginSessionTracker.getCurrentTime();
			// compare time now with lastLoginTime
			LoginSessionTracker.resyncRequired = LoginSessionTracker.reset(nowLoginTimeHour, lastLoginTimeHour);
			if(resyncRequired)
			{
				editor.putString("lastLoginTime", LoginSessionTracker.getCurrentTime() + "");
				editor.commit();
			}
		}
		String memberType = settings.getString("memberType",null);
		String memberName = settings.getString("memberName",null);		
	
		if(memberType.equals("free2play"))
		{
			alog.setVisibility(View.GONE);
			hiScores.setVisibility(View.GONE);
		}
		else if(memberType.equals("pay2play"))
		{
			username = memberName;
		}
		friends.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(HomeActivity.this, FriendsActivity.class);
				startActivity(intent);
			}
		});
		hiScores.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
				String userType = settings.getString("userType", null);
				Utility.callLoaderAndHandler(new StatsLoader(username, HomeActivity.this, userType));
			}
		});
		ge.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(HomeActivity.this, GeHome.class);
				startActivity(intent);
			}
		});
		alog.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Utility.callLoaderAndHandler(new AlogLoader(username, HomeActivity.this));
			}
		});
		news.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Utility.callLoaderAndHandler(new NewsLoader(HomeActivity.this));
			}
		});
		forumTime.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(HomeActivity.this, Time.class);
				startActivity(intent);
			}
		});
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
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case R.id.logout:
				SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
				SharedPreferences.Editor editor;
				editor = settings.edit();
				editor.clear();
				editor.putString("registered", "no");
				editor.commit();
				Intent intent = new Intent(HomeActivity.this,RegistrationActivity.class);
				startActivity(intent);
				finish();
				return true;
			case R.id.info:
				intent = new Intent(HomeActivity.this, Info.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}