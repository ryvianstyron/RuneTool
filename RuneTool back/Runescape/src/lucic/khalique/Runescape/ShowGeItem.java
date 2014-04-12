package lucic.khalique.Runescape;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class ShowGeItem extends Activity
{
	WebView img;
	TextView nametxt;
	TextView desctxt;
	TextView typetxt;
	TextView urltxt;
	TextView pctxt;
	TextView tctxt;
	TextView idtxt;
	Button dismiss;
	
	String name = "";
	String desc = "";
	String type = "";
	String url ="";
	String pc = "";
	String tc = "";
	String id = "";
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showgedesc);
		name = getIntent().getExtras().getString("name");
		desc = getIntent().getExtras().getString("desc");
		type = getIntent().getExtras().getString("type");
		url = getIntent().getExtras().getString("url");
		Log.i("URL WITHOUT QUOTES",url);
		pc = getIntent().getExtras().getString("pc");
		tc = getIntent().getExtras().getString("tc");
		if(tc.contains("0"))
		{
			tc = "No Change";
		}
		id = getIntent().getExtras().getString("id");
		
		nametxt = (TextView)findViewById(R.id.itemName);
		desctxt = (TextView)findViewById(R.id.desc);
		typetxt = (TextView)findViewById(R.id.type);
		idtxt = (TextView)findViewById(R.id.id);
		pctxt = (TextView)findViewById(R.id.current);
		tctxt = (TextView)findViewById(R.id.today);
		img = (WebView)findViewById(R.id.webview);
		//build image tag
		url = "\"" + url + "\""; //surround url with quotes
		Log.i("URL WITH QUOTES",url);
	    String html = "<div style=\"text-align:center;\"><a href=" + url;
	    html = html + "target=\"_blank\">";
	    html = html + "<img src=" + url + ">" + "<img src=" + url;
	    html = html + "></a></div>";
	    html = "<img src = " + url + "></a>";
	    
	    html = "<body style=\"text-align:center;color:white;font-size:17px\">" + html + "</body>";
	    //html = "<div style=\"text-align:center;\"><a href=\"http://services." +
	    		//"runescape.com/m=rswikiimages/en/2012/5/monkey_cape-24170202.jpg\" target=\"_blank\">" + 
	   // "<img src=\"http://services.runescape.com/m=rswikiimages/en/2012/5/monkey_cape_thumb-24170208.gif\"></a></div>";
	    img.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
		img.setBackgroundColor(0);
		img.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		//img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		img.setVerticalScrollBarEnabled(false);
		//img.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		//img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		nametxt.setText(name);
		desctxt.setText("Description: " + desc);
		typetxt.setText("Type: " + type);
		pctxt.setText("Current Price: " + pc);
		tctxt.setText("Price Change Today: " + tc);
		idtxt.setText("Item ID: " + id);
	}

	public void onBackPressed()
	{
		super.onBackPressed();
		this.finish();
	}
}
