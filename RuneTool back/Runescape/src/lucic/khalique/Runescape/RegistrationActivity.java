package lucic.khalique.Runescape;

import loaders.UserValidator;
import utilities.DialogUtility;
import utilities.Utility;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity
{	
	String username = "";
	EditText memberEditText;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	LinearLayout regButtons;
	LinearLayout memberTextField;
	Button memberButton;
	Button member;
	Button free2Play;
	Dialog dialog;
	Context con;
	LinearLayout everything;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Utility.callLoaderAndHandler(new InternetChecker());
	}
	public void initializePageView()
	{
		setContentView(R.layout.registrationview);
		everything = (LinearLayout)findViewById(R.id.everything);
		// Check to see if SharedPreferences have been set ever
		SharedPreferences existingSettings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		String settingExists = existingSettings.getString("memberType",null);
		if(settingExists!=null) // Been set, move to home activity
		{
			Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
			startActivity(intent);
		}
		everything.setVisibility(View.VISIBLE);
		free2Play = (Button)findViewById(R.id.free2play);
		member = (Button) findViewById(R.id.pay2play);
		memberButton = (Button) findViewById(R.id.memberButton);
		memberEditText = (EditText)findViewById(R.id.memberEditText);
		regButtons = (LinearLayout)findViewById(R.id.accountButtons);
		memberTextField = (LinearLayout)findViewById(R.id.memberTextField);
		
		// Create Listeners
		free2Play.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
				Bundle b = new Bundle();
				b.putString("memberType", "free2play");
				intent.putExtras(b);
				settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
				editor = settings.edit();
				editor.putString("memberType", "free2play");
				editor.putString("registered", "yes");
				editor.putString("loginCherryPopped" , "yes");
				editor.commit();
				startActivity(intent);
			}
		});
		member.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				regButtons.setVisibility(View.GONE);
				memberTextField.setVisibility(View.VISIBLE);
			}
		});
		memberButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				onUserInput();
			}
		});
		TextView.OnEditorActionListener enterListener = new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView memberEditText, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) 
				{ 
					onUserInput();     
				}
				return true;
			}
		};
		memberEditText.setOnEditorActionListener(enterListener);
	}
	public void onUserInput()
	{
		username = memberEditText.getText().toString();
		if(username.equals("Enter Member Name") ||username.equals(""))
		{
			Toast.makeText(RegistrationActivity.this,"Please enter a valid username ..", Toast.LENGTH_LONG).show();
		}
		else
		{
			username = memberEditText.getText().toString();
			username = username.trim();
			Utility.callLoaderAndHandler(new UserValidator(username, RegistrationActivity.this));
		}
	}
	
	public void onBackPressed()
	{
		super.onBackPressed();
		this.finish();
	}
	private class InternetChecker extends AsyncTask<String, Void, String>
	{
		Dialog loadingDialog;
		protected void onPreExecute()
		{
			loadingDialog = DialogUtility.createLoadingScreen("Loading ..", RegistrationActivity.this);
			loadingDialog.show();
		}
		protected String doInBackground(String... params)
		{
			while(!isCancelled())
			{
					if(!isOnline())
					{
						return "nic";
					}
					return "good";
			}
			return "cancelled";
		}
		protected void onPostExecute(String result)
		{
			if(result.equals("good"))
			{
				loadingDialog.dismiss();
				initializePageView();
			}
			if(result.equals("nic"))
			{
				loadingDialog.dismiss();
				dialog = createInternetDialog();
				dialog.show();
			}
		}
		@Override
		protected void onCancelled()
		{
			loadingDialog.dismiss();
			dialog = createInternetDialog();
			dialog.show();
		}
	}
	public Dialog createInternetDialog()
	{
		dialog = new Dialog(RegistrationActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.internetdialog);
		dialog.setCancelable(false);
		Button quit = (Button)dialog.findViewById(R.id.quit);
		quit.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dialog.dismiss();
				finish();
			}
		});
		return dialog;
	}
	public boolean isOnline() 
	{
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) 
	    {
	        return true;
	    }
	    return false;
	}
}