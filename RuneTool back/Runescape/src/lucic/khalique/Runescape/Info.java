package lucic.khalique.Runescape;

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
import android.view.Window;
import android.widget.Button;

public class Info extends Activity
{
	
	Button about, dis,tut;
	Dialog dialog;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		about = (Button)findViewById(R.id.abt);
		dis = (Button)findViewById(R.id.dis);
		tut = (Button)findViewById(R.id.tut);
		
		about.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dialog = new Dialog(Info.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.about);
				dialog.setCancelable(true);
				dialog.show();
			}
		});
		dis.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dialog = new Dialog(Info.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.disclaimer);
				dialog.setCancelable(true);
				dialog.show();
			}
		});
		tut.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dialog = new Dialog(Info.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.tutorial);
				dialog.setCancelable(true);
				dialog.show();
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.runescape_menu, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Create settings and editor
		SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		// handle item selection
	     switch (item.getItemId()) 
	     {
	         case R.id.logout:
	        	 editor.clear();
	        	 editor.putString("registered","no");
	        	 editor.commit();
	        	 Intent intent = new Intent(Info.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         default:return super.onOptionsItemSelected(item);
	     }
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
}
