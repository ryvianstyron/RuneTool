package lucic.khalique.Runescape;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utilities.DialogUtility;
import utilities.Utility;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class News extends ListActivity
{
	Exception exception;
	static HashMap<String, Integer> images = new HashMap<String, Integer>();
	TextView title;
	TextView date;
	TextView desc;
	Button image;
	Button moreinfo;
	Button lessinfo;
	TextView link;
	TextView info;
	String result ="";
	int drawable;
	TextView category;
	Boolean clicked = false;
	ScrollView scroll;
	ArrayList<HashMap<String,Object>> list;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	Dialog loadingDialog;
	Dialog dialog;
	int positionInList = 0;
	
	private static ArrayList<String> titles;
    private static ArrayList<String> descriptions;
    private static ArrayList<String> links;
    private static ArrayList<String> dates;
    private static ArrayList<String> categories;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);       
        setContentView(R.layout.listview);
        list = new ArrayList<HashMap<String,Object>>();
        SimpleAdapter adapter = new SimpleAdapter(
        	    this,
        	    list,
        	    R.layout.listitem,
        	    new String[] {"image","title","date", "category"},
        	    new int[] {R.id.newsImage,R.id.newsTitle, R.id.dateText, R.id.categoryText}
        	    );
        Bundle b = this.getIntent().getExtras();
        titles = b.getStringArrayList("titleList");
        descriptions = b.getStringArrayList("descriptionList");
        links = b.getStringArrayList("linkList");
        dates = b.getStringArrayList("dateList");
        categories = b.getStringArrayList("categories");
        populateList();
        setListAdapter(adapter);
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
	public void onBackPressed()
	{
		super.onBackPressed();
		this.finish();
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
	        	 Intent intent = new Intent(News.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         default:return super.onOptionsItemSelected(item);
	     }
	}
	public  void populateList()
	{
		initNewsMap();
		String category = "";
		for (int i = 0; i < titles.size(); i++) 
		{
			category = categories.get(i);
    		HashMap<String, Object> temp = new HashMap<String, Object>();
    		int drawable = R.drawable.item;
    		
    		for (Map.Entry<String, Integer> entry : images.entrySet()) 
			{
			    //String key = entry.getKey();
			    //Object value = entry.getValue();
			    if(category.contains(entry.getKey()))
			    {
			    	drawable = entry.getValue();
			    	break;
			    }
			}
    		temp.put("title", titles.get(i));
    		temp.put("image", drawable);
    		temp.put("date", dates.get(i));
    		temp.put("category", categories.get(i));
    		list.add(temp);
	    }
	}
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
	    initNewsMap();
	    final int spot = position;
        
        dialog = new Dialog(News.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.showdescription);
        dialog.setCancelable(true);

        title = (TextView)dialog.findViewById(R.id.title);
        date = (TextView)dialog.findViewById(R.id.date);
        desc = (TextView)dialog.findViewById(R.id.desc);
        link = (TextView)dialog.findViewById(R.id.link);
        image =(Button)dialog.findViewById(R.id.newsImage);
        info = (TextView)dialog.findViewById(R.id.infodetail);
        moreinfo = (Button)dialog.findViewById(R.id.moreinfo);
        category = (TextView)dialog.findViewById(R.id.category);
        scroll = (ScrollView)dialog.findViewById(R.id.scroll);
        
        link.setVisibility(View.GONE);
		category.setVisibility(View.GONE);
		scroll.setVisibility(View.GONE);
		positionInList = position;

		Utility.callLoaderAndHandler(new MoreInfoLoader());
        
        final Button moreInfo = (Button)dialog.findViewById(R.id.moreinfo);
        moreInfo.setOnClickListener(new OnClickListener() 
        {
        	@Override
	        public void onClick(View v) 
	        {
        		Intent intent = new Intent(News.this, WebInfo.class);
        		Bundle b = new Bundle();
				b.putString("webInfo",result);
				b.putString("category",categories.get(spot));
				b.putString("title",titles.get(spot));
				b.putString("date",dates.get(spot));
				intent.putExtras(b);
				dialog.dismiss();
				startActivity(intent);
	        }
        });
	}
	public void setupMoreInfoDialog()
	{
		String categoryText = categories.get(positionInList);
		for (Map.Entry<String, Integer> entry : images.entrySet()) 
		{
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    if(categoryText.contains(entry.getKey()))
		    {
		    	drawable = entry.getValue();
		    	break;
		    }
		    else drawable = R.drawable.item;
		}
		String titleText = titles.get(positionInList);
        String dateText = dates.get(positionInList);
        
        String description = descriptions.get(positionInList);
        String array [];
    	int index = 0;
    	if (description.contains("/>"))
		{
    		array = description.split("/>");
    		index = description.indexOf("10;");
    		description = (array[array.length - 1].trim());
		}
        
        String linkText = links.get(positionInList);
        
        image.setBackgroundResource(drawable);
        title.setText(titleText);
        date.setText(dateText);
        desc.setText(description);
        link.setText(linkText + " ");
	}
	private class MoreInfoLoader extends AsyncTask<String,Void,String>
	{
		protected void onPreExecute()
		{
			loadingDialog = DialogUtility.createLoadingScreen("Loading ..", News.this);
			loadingDialog.show();
		}
		@Override
		protected String doInBackground(String... params)
		{
			while(!isCancelled())
			{
				try
				{
					HttpClient httpclient = new DefaultHttpClient();
					HttpGet httpget = new HttpGet(links.get(positionInList));
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httpget);
	
					// Extract the page content returned from the response
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
					result = sb.toString();
				
					String array1[];
					array1 = result.split("<div class=\"Content\">");
					int divCounter = 0;
					Boolean done = false;
					StringBuffer builder = new StringBuffer();
					//array1[1] = array1[1].replaceAll("'", "");
					String divString[] = array1[1].split("</div>");
					//StringTokenizer st = new StringTokenizer(array1[1],"</div>");
					int length = divString.length;
					for(int i = 0; i < length && !done; i++)
					{
						Log.i("dIV STRING" , divString[i]);
					
						String temp = divString[i];
						divCounter = temp.indexOf("<div");
						if(divCounter== -1)
						{
							builder.append(temp);
							done = true;
							Log.i("TEMP IF", temp);
						}
						else
						{
							builder.append(temp);
							builder.append("</div>");
							Log.i("TEMP ELSE", temp);
						}
					}
					// put in extracting the video link from the iframe here. IF it contains an iframe!
					result = builder.toString();
					return "completed";
				} 
				catch (UnknownHostException unh)
				{
					return "UnknownHostException";
				}
				catch (ConnectException ce)
				{
					return "ConnectException";
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
			if(result.equals("completed"))
			{
				loadingDialog.dismiss();
				setupMoreInfoDialog();
				dialog.show();
			}
			else if(result.equals("UnknownHostException") || result.equals("ConnectException"))
			{
				dialog.dismiss();
				Toast.makeText(News.this, "Please double check your internet connection ..", Toast.LENGTH_LONG).show();
			}
			else if(result.contains("incomplete"))
			{
				Toast.makeText(News.this, "An error occured loading News, please try again and later ..", Toast.LENGTH_LONG).show();
				loadingDialog.dismiss();
				DialogUtility.createBugReportDialog(News.this, "News", "More Info AsyncTask doInBackground()", exception);
			}
		}
		@Override
		protected void onCancelled()
		{
			loadingDialog.dismiss();
			Toast.makeText(News.this, "News response from www.runescape.com is slow, please try again later ..", Toast.LENGTH_LONG).show();
		}
	}
	public void initNewsMap()
	{
		images.put("Behind the Scenes News",R.drawable.bts);
		images.put("Customer Support News",R.drawable.cs);
		images.put("Game Update News",R.drawable.ga);
		images.put("Website News",R.drawable.db);
		images.put("Technical News",R.drawable.tn);
	}
}
