package lucic.khalique.Runescape;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class WebInfo extends Activity
{
	static HashMap<String, Integer> images = new HashMap<String, Integer>();
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		TextView categorytv = (TextView)findViewById(R.id.category);
		TextView datetv = (TextView)findViewById(R.id.date);
		TextView titletv = (TextView)findViewById(R.id.newsTitle);

		WebView webview = (WebView)findViewById(R.id.webview);
		String webInfo = getIntent().getExtras().getString("webInfo");
		String category = getIntent().getExtras().getString("category");
		String title = getIntent().getExtras().getString("title");
		String date = getIntent().getExtras().getString("date");
		Log.i("result",webInfo);
		
		categorytv.setText(category);
		datetv.setText(date);
		titletv.setText(title);
		
		webInfo = "<body style=\"text-aling:center;color:white;font-size:17px\">" + webInfo + "</body>";
		webview.loadDataWithBaseURL(null,webInfo,"text/html","utf-8",null);
		//webview.setBackgroundColor(0);
		webview.setBackgroundColor(0x01000000);
		webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
	}
	public void onResume()
	{
		super.onResume();
		settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
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
		// Create settings and editor
		settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		editor = settings.edit();
		// handle item selection
	     switch (item.getItemId()) 
	     {
	         case R.id.logout:
	        	 editor.clear();
	        	 editor.putString("registered","no");
	        	 editor.commit();
	        	 Intent intent = new Intent(WebInfo.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         default:return super.onOptionsItemSelected(item);
	     }
	}
}
