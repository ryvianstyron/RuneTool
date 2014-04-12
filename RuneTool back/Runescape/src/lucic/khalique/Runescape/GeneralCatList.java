package lucic.khalique.Runescape;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class GeneralCatList extends ListActivity
{
	boolean alphabetoptionclicked = false;
	int titleToSet = R.drawable.buttonback3;
	Button img;
	GeItem geItem;
	int category = 0; // Which category is it
	String categoryTitle = "";
	ArrayList<HashMap<String, Object>> list;
	ArrayList<String> letters;
	ArrayList<Integer> items;
	ArrayList<Integer> pages;
	ArrayList<GeItem> geItems;
	static HashMap<String, Integer> letterAndPages = new HashMap<String, Integer>();
	ArrayList<String> images;
	String[] itemsInCategory;
	Dialog dialog;
	SimpleAdapter adapter;
	LinearLayout alphabets;
	HashMap<String, Integer> buttonsAndItems = new HashMap<String, Integer>();
	int noOfPages = 0;
	String processingLetter = null;
	boolean dialogShowing = false;
	Dialog search;
	String searchFor = "";
	SearchLoader searchLoader;
	
	String currentLetter = null;
	// Buttons
	Button hash;
	Button a; //0
	Button b; //1
	Button c; //2
	Button d; //3
	Button e; //4
	Button f; //5
	Button g; //6
	Button h; //7
	Button i; //8
	Button j; //9
	Button k; //10
	Button l; //11
	Button m; //12
	Button n; //13
	Button o; //14
	Button p; //15
	Button q; //16
	Button r; //17
	Button s; //18
	Button t; //19
	Button u; //20
	Button v; //21
	Button w; //22
	Button x; //23
	Button y; //24
	Button z; //25
	GeCatLoader geCatLoader;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gecategory);

		//Setting buttons
		a = (Button)findViewById(R.id.a);
		b = (Button)findViewById(R.id.b);
		c = (Button)findViewById(R.id.c);
		d = (Button)findViewById(R.id.d);
		e = (Button)findViewById(R.id.e);
		f = (Button)findViewById(R.id.f);
		g = (Button)findViewById(R.id.g);
		h = (Button)findViewById(R.id.h);
		i = (Button)findViewById(R.id.i);
		j = (Button)findViewById(R.id.j);
		k = (Button)findViewById(R.id.k);
		l = (Button)findViewById(R.id.l);
		m = (Button)findViewById(R.id.m);
		n = (Button)findViewById(R.id.n);
		o = (Button)findViewById(R.id.o);
		p = (Button)findViewById(R.id.p);
		q = (Button)findViewById(R.id.q);
		r = (Button)findViewById(R.id.r);
		s = (Button)findViewById(R.id.s);
		t = (Button)findViewById(R.id.t);
		u = (Button)findViewById(R.id.u);
		v = (Button)findViewById(R.id.v);
		w = (Button)findViewById(R.id.w);
		x = (Button)findViewById(R.id.x);
		y = (Button)findViewById(R.id.y);
		z = (Button)findViewById(R.id.z);
		hash = (Button)findViewById(R.id.hash);
		
		//Setting button listeners
		hash.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("#");}});
		a.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("a");}});
		b.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("b");}});
		c.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("c");}});
		d.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("d");}});
		e.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("e");}});
		f.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("f");}});
		g.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("g");}});
		h.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("h");}});
		i.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("i");}});
		j.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("j");}});
		k.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("k");}});
		l.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("l");}});
		m.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("m");}});
		n.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("n");}});
		o.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("o");}});
		p.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("p");}});
		q.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("q");}});
		r.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("r");}});
		s.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("s");}});
		t.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("t");}});
		u.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("u");}});
		v.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("v");}});
		w.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("w");}});
		x.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("x");}});
		y.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("y");}});
		z.setOnClickListener(new OnClickListener(){public void onClick(View v){setLetter("z");}});
		
		alphabets = (LinearLayout) findViewById(R.id.alhphabetlist);
		img = (Button) findViewById(R.id.gehometitle);
		img.setBackgroundResource(titleToSet);
		letters = getIntent().getExtras().getStringArrayList("letterList");
		items = getIntent().getExtras().getIntegerArrayList("itemList");
		category = getIntent().getExtras().getInt("category");
		categoryTitle = getIntent().getExtras().getString("categoryTitle");
		pages = new ArrayList<Integer>();
		callLoader();
	}
	public void setLetter(String letter)
	{
		processingLetter = letter;
		alphabets.setVisibility(View.GONE);
		alphabetoptionclicked = false;
		callLoader();
	}
	public void callLoader()
	{
		Utility.callLoaderAndHandler(new GeCatLoader());
	}
	public void updateItemsInCategory()
	{
		images = new ArrayList<String>();
		String urlTillCat = "http://services.runescape.com/m=itemdb_rs/api/catalogue/items.json?category=";
		String urlTillLetter = "&alpha=";
		String urlTillPage = "&page=";
		String letter = "0";
		int page = 0;
		String json = "";
		geItems = new ArrayList<GeItem>();
		try
		{
			JSONArray jItem = null;
			if(processingLetter.equals("#") || processingLetter.equals("%23"))
			{
				processingLetter = "%23";
			}
			letter = processingLetter;
			page = noOfPages;
			
			// Go thru all pages
			for (int j = 1; j < page + 1; j++)
			{
				String urlToLoad = urlTillCat + category + urlTillLetter + letter + urlTillPage + j;
				
				URLConnection feedUrl = new URL(urlToLoad).openConnection();
				InputStream in = feedUrl.getInputStream();
				json = Utility.convertStreamToString(in);
				JSONObject jsonObj = new JSONObject(json);
				jItem = jsonObj.getJSONArray("items");
				
				for (int k = 0; k < jItem.length(); k++)
				{
					addGeItem(jItem.getJSONObject(k));
				}
			}
		} 
		catch (Exception e)
		{
			System.out.println("Exception caught in updateItems: " + e.toString());
		}
	}
	public void onBackPressed()
	{
		if(alphabets.getVisibility() == View.VISIBLE)
		{
			alphabets.setVisibility(View.GONE);
			alphabetoptionclicked = false;
		}
		else
		{
			this.finish();
		}
	}
	public void addGeItem(JSONObject item) throws JSONException
	{
		GeItem geItem = new GeItem();
		
		geItem.setName(item.getString("name"));
		geItem.setDescription(item.getString("description"));
		geItem.setType(item.getString("type"));
		geItem.setId(item.getInt("id"));
		geItem.setIconUrl(item.getString("icon_large"));
		
		JSONObject pItem = item.getJSONObject("current");
		geItem.setPriceCurrent(pItem.getString("price"));
		
		pItem = item.getJSONObject("today");
		geItem.setPriceToday(pItem.getString("price"));
		geItems.add(geItem);
	}
	public void populateList()
	{
		for (int i = 0; i < geItems.size(); i++)
		{
			HashMap<String, Object> temp = new HashMap<String, Object>();
			temp.put("title", geItems.get(i).getName());
			list.add(temp);
		}
	}
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		
		final Dialog dialog = new Dialog(GeneralCatList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.showgedesc);
        
        WebView img;
    	TextView nametxt;
    	TextView desctxt;
    	TextView typetxt;
    	TextView pctxt;
    	TextView tctxt;
    	TextView idtxt;
    	
    	String name = geItems.get(position).getName();
    	String desc = geItems.get(position).getDescription();
    	String type = geItems.get(position).getType();
    	String url =geItems.get(position).getIconUrl();
    	String pc = geItems.get(position).getPriceCurrent();
    	String tc = geItems.get(position).getPriceToday();
    	String idnumber = geItems.get(position).getId();
    	if(tc.contains("0"))
		{
			tc = "No Change";
		}
    	nametxt = (TextView)dialog.findViewById(R.id.itemName);
		desctxt = (TextView)dialog.findViewById(R.id.desc);
		typetxt = (TextView)dialog.findViewById(R.id.type);
		idtxt = (TextView)dialog.findViewById(R.id.id);
		pctxt = (TextView)dialog.findViewById(R.id.current);
		tctxt = (TextView)dialog.findViewById(R.id.today);
		img = (WebView)dialog.findViewById(R.id.webview);
		
		//build image tag
		url = "\"" + url + "\""; //surround url with quotes
		Log.i("URL WITH QUOTES",url);
	    String html = "<div style=\"text-align:center;\"><a href=" + url;
	    html = html + "target=\"_blank\">";
	    html = html + "<img src=" + url + ">" + "<img src=" + url;
	    html = html + "></a></div>";
	    html = "<img src = " + url + "></a>";
	   
	    img.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
		img.setBackgroundColor(0);
		img.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
		img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		img.setVerticalScrollBarEnabled(false);
		//img.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		//img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		nametxt.setText(name);
		desctxt.setText("Description: " + desc);
		typetxt.setText("Type: " + type);
		pctxt.setText("Current Price: " + pc);
		tctxt.setText("Price Change Today: " + tc);
		idtxt.setText("Item ID: " + idnumber);
		dialog.show();
	}
	private class GeCatLoader extends AsyncTask<String, Void, String>
	{
		protected void onPreExecute()
		{
			dialog = DialogUtility.createLoadingScreen("Loading " + categoryTitle + " ..", GeneralCatList.this);
			dialog.show();
		}
		@Override
		protected String doInBackground(String... params)
		{
				if(!isCancelled())
				{
				if(processingLetter == null) // default case
				{
					for(int j = 0; j < letters.size(); j++) // go through the letters
					{
						if(items.get(j) >= 1 && items.get(j) <= 12)
						{
							noOfPages = 1;
						}
						else if(items.get(j) > 12)
						{
							noOfPages = (int)items.get(j)/12 + 1;
						}
						if(noOfPages > 0 && processingLetter == null)
						{
								processingLetter = letters.get(j);
								break;
						}
						
					}
				}
				else
				{
					for(int k = 0; k < letters.size(); k++) // if a letter has been selected by the user
					{
						if(letters.get(k).equals(processingLetter))
						{
							if(items.get(k) == 0)
							{
								noOfPages = 0;
							}
							else if(items.get(k) >=1 && items.get(k) <= 12 )
							{
								noOfPages = 1;
								processingLetter = letters.get(k);
							}
							else if(items.get(k) > 12)
							{
								noOfPages = (int)items.get(k)/12 + 1;
								processingLetter = letters.get(k);
							}
						}
					}
				}
				if(noOfPages == 0)
				{
					return "incomplete";
				}
				else
				{
					updateItemsInCategory();
	
					list = new ArrayList<HashMap<String, Object>>();
					adapter = new SimpleAdapter(GeneralCatList.this, list,
							R.layout.catlistitem, new String[] { "title" },
							new int[] { R.id.catTitle });
					return "completed";
				}
			}
			return "cancelled";
		}
		@Override
		protected void onPostExecute(String result)
		{
			img.setVisibility(View.VISIBLE);
			if(processingLetter.equals("%23"))
			{
				processingLetter = "#";
			}
			if(result.equals("completed") && dialog.isShowing())
			{
				img.setText(categoryTitle + " - " + processingLetter.toUpperCase());
				populateList();
				setListAdapter(adapter);
				dialog.dismiss();
			}
			if(result.equals("incomplete") && dialog.isShowing())
			{
				Toast.makeText(GeneralCatList.this, "No items in this letter..", Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		}
		@Override
		protected void onCancelled()
		{
			dialog.dismiss();
			Toast.makeText(GeneralCatList.this, "Response from www.runescape.com is slow, please try again later", Toast.LENGTH_LONG).show();
		}
	}
	public int calculatePages(int items)
	{
		int result = 0;
		if(items == 0)
		{
			;;
		}
		else if(items > 0 && items <= 12)
		{
			result = 1;
		}
		else if(items > 12)
		{
			result = (int)items/12 + 1;
		}
		return result;
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	    {
	    	MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.gecategorymenu, menu);
	        return true;
	    }
	public boolean onOptionsItemSelected(MenuItem item) 
	 {
	     // Handle item selection
		switch (item.getItemId()) 
		{
			case R.id.logout:
				SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
				SharedPreferences.Editor editor;
				editor = settings.edit();
				editor.clear();
				editor.putString("registered","no");
				editor.commit();
				Intent intent = new Intent(GeneralCatList.this, RegistrationActivity.class);
				startActivity(intent);
				finish();
				return true;
	        case R.id.alphabets:
				if(alphabetoptionclicked == false)
				{
					alphabets.setVisibility(View.VISIBLE);
					alphabetoptionclicked = true;
				}
				else if(alphabetoptionclicked == true)
				{
					alphabets.setVisibility(View.GONE);
					alphabetoptionclicked = false;
				}
				return true;
	         case R.id.search:
	        	search = new Dialog(GeneralCatList.this);
	        	search.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        	search.setContentView(R.layout.addordeletefrienddialog);
	        	Button dismiss = (Button)search.findViewById(R.id.dismiss);
	        	Button searchButton = (Button)search.findViewById(R.id.addordelete);
	        	Button searchImage = (Button)search.findViewById(R.id.adddeleteimage);
	 			TextView title = (TextView)search.findViewById(R.id.adddeletetitle);
	 			final EditText searchField = (EditText)search.findViewById(R.id.friendname);
	 			searchImage.setBackgroundResource(R.drawable.search);
	 			title.setText("Search");
	 			searchButton.setText("Search");
	 			searchField.setHint("Enter search string ..");
	 			search.setCancelable(false);
	 			if(!dialogShowing)
	        	{
	        		search.show();
	        		dialogShowing = true;
	        	}
	        	else if(dialogShowing)
	        	{
	        		search.dismiss();
	        		dialogShowing = false;
	        	}
	 			dismiss.setOnClickListener(new OnClickListener()
	 			{
	 				public void onClick(View v)
	 				{
	 					if(search.isShowing())
	 					{
	 						search.dismiss();
	 						dialogShowing = false;
	 					}
	 				}
	 			});
	 			searchButton.setOnClickListener(new OnClickListener()
	 			{
	 				public void onClick(View v)
	 				{
	 					if(search.isShowing())
	 					{
	 						search.dismiss();
	 						dialogShowing = false;
	 						String searchText = searchField.getText().toString().trim();
	 						if(searchText != null && !searchText.isEmpty() && searchText.length() >= 3)
	 						{
	 							searchCategory(category, searchText);
	 						}
	 						else Toast.makeText(GeneralCatList.this,"Please enter a valid search string, 3 or more letters ..",Toast.LENGTH_LONG).show();
	 					}
	 				}
	 			});
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
	public void searchCategory(int category, String searchText)
	{
		searchFor = searchText.toLowerCase();
		Utility.callLoaderAndHandler(new SearchLoader());
	}
	private class SearchLoader extends AsyncTask<String, Void, String>
	{
		protected void onPreExecute()
		{
			dialog = DialogUtility.createLoadingScreen("Searching Grand Exchange Database ..", GeneralCatList.this);
			dialog.show();
		}
		protected String doInBackground(String... params)
		{
			while(!isCancelled())
			{
				String result = "";
				int totalPages = 0;
				images = new ArrayList<String>();
				String urlTillCat = "http://services.runescape.com/m=itemdb_rs/api/catalogue/items.json?category=" + category;
				String urlTillLetter = "&alpha=" + searchFor;
				String urlTillPage = "&page=";
				String json = "";
				
				list = new ArrayList<HashMap<String, Object>>();
				adapter = new SimpleAdapter(GeneralCatList.this, list,
						R.layout.catlistitem, new String[] { "title" },
						new int[] { R.id.catTitle });
				// find number of pages
				String urlToLoad = urlTillCat + urlTillLetter + urlTillPage + "1";
				try
				{
					URLConnection feedUrl = new URL(urlToLoad).openConnection();
					InputStream in = feedUrl.getInputStream();
					json = Utility.convertStreamToString(in);
					
					//Parse JSON 
					JSONObject jsonObj = new JSONObject(json); 
					@SuppressWarnings("unused")
					int itemsFound = Integer.parseInt(jsonObj.getString("total"));
					
					if(itemsFound > 0)
					{
						geItems.clear();
						totalPages = calculatePages(itemsFound);
						for(int page = 1; page < totalPages + 1; page++)
						{
							urlToLoad = urlTillCat + urlTillLetter + urlTillPage + page;
							feedUrl = new URL(urlToLoad).openConnection();
							in = feedUrl.getInputStream();
							json = Utility.convertStreamToString(in);
							jsonObj = new JSONObject(json); 
							JSONArray itemList = jsonObj.getJSONArray("items");
							
							for(int i = 0; i< itemList.length(); i++)
							{
								addGeItem(itemList.getJSONObject(i));
							}
						}
						return "completed";	
					}
					else if(itemsFound == 0)
					{
						return "No Items";
					}
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					return "incomplete";
				}
			}
			return "cancelled";
		}
		protected void onPostExecute(String result)
		{
			img.setVisibility(View.VISIBLE);
			if(result.equals("completed") && dialog.isShowing())
			{
				img.setText(categoryTitle + " - " + searchFor + " results");
				populateList();
				setListAdapter(adapter);
				dialog.dismiss();
			}
			else if(result.equals("incomplete") && dialog.isShowing())
			{
				dialog.dismiss();
				Toast.makeText(GeneralCatList.this, "Search is not available at the moment ..", Toast.LENGTH_LONG).show();
			}
			else if(result.equals("No Items"))
			{
				dialog.dismiss();
				dialog = DialogUtility.createSimpleDialog("No items found."+
						"\n1. Make sure the item you are searching for is spelt correctly." +
						"\n2. Always search with the start of the word." + "\n" + 
						"Example - To search for Raw Lobster - Enter Raw or Raw Lobster, not Lobster.", GeneralCatList.this);
				dialog.show();
			}
		}
		@Override
		protected void onCancelled()
		{
			dialog.dismiss();
			Toast.makeText(GeneralCatList.this, "Search response from www.runescape.com is slow, please try again later ..", Toast.LENGTH_LONG).show();
		}
	}
}
