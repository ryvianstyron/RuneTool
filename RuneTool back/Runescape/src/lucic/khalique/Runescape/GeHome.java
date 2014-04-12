package lucic.khalique.Runescape;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GeHome extends ListActivity
{
	Exception exception;
	Button img;
	ArrayList<HashMap<String, Object>> list;
	static HashMap<String, Integer> images = new HashMap<String, Integer>();
	private ArrayList<String> letter; 
	private ArrayList<Integer> items; 
	Dialog dialog;
	String[] cats = 
	{ 		
			"Miscellaneous", 						// 0
			"Ammo", 								// 1
			"Arrows", 								// 2
			"Bolts", 								// 3
			"Construction Materials", 				// 4
			"Construction Projects", 				// 5
			"Cooking Ingredients", 					// 6				
			"Costumes", 							// 7
			"Crafting Materials", 					// 8
			"Familiars", 							// 9
			"Farming Produce", 						// 10
			"Fletching Materials", 					// 11			
			"Food and Drink", 						// 12
			"Herblore Materials", 					// 13				
			"Hunting Equipment", 					// 14
			"Hunting Produce", 						// 15
			"Jewellery", 							// 16
			"Mage Armour", 							// 17
			"Mage Weapons", 						// 18
			"Melee Armour - Low Level", 			// 19
			"Melee Armour - Mid Level", 			// 20
			"Melee Armour - High Level",			// 21
			"Melee Weapons - Low Level",			// 22
			"Melee Weapons - Mid Level",			// 23
			"Melee Weapons - High Level",			// 24
			"Mining and Smithing", 					// 25			
			"Potions", 								// 26
			"Prayer Armour", 						// 27
			"Prayer Materials", 					// 28
			"Range Armour", 						// 29
			"Range Weapons", 						// 30
			"Runecrafting", 						// 31				
			"Runes, Spells and Teleports",			// 32
			"Seeds", 								// 33
			"Summoning Scrolls", 					// 34
			"Tools and Containers", 				// 35
			"Woodcutting Product", 					// 36
			"Pocket Items" 							//37
	};
	SharedPreferences settings;
	SharedPreferences.Editor editor;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gehome);
		
		initImages();
		img = (Button)findViewById(R.id.gehometitle);
		img.setBackgroundResource(R.drawable.buttonback3);
		img.setText("Categories");

		list = new ArrayList<HashMap<String, Object>>();
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.gelistitem, new String[] { "image", "title", },
				new int[] { R.id.catImage, R.id.catTitle });
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
	        	 Intent intent = new Intent(GeHome.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         default:return super.onOptionsItemSelected(item);
	     }
	}
	public void populateList()
	{
		for (int i = 0; i < cats.length; i++)
		{
			HashMap<String, Object> temp = new HashMap<String, Object>();
			int drawable = R.drawable.item;
			
			String category = cats[i];
			for (Map.Entry<String, Integer> entry : images.entrySet())  // was Map.Entry
			{
				if(category.contains(entry.getKey())) 
				{ 
					drawable = entry.getValue(); 
					break; 
				} 
			}
			temp.put("title", cats[i]);
			temp.put("image", drawable);
			list.add(temp);
		}
	}
	public void initImages()
	{
		images.put("Cooking Ingredients", R.drawable.cooking);
		images.put("Fletching Materials", R.drawable.fletching);
		images.put("Herblore Materials", R.drawable.herblore);
		images.put("Runecrafting",R.drawable.runecrafting);
		images.put("Crafting Materials", R.drawable.crafting);
		images.put("Farming Produce", R.drawable.farming);
		images.put("Mining and Smithing", R.drawable.smithing);
		images.put("Prayer Materials", R.drawable.prayer);
		images.put("Construction Materials", R.drawable.constructionm);
		images.put("Construction Projects", R.drawable.constructionp);
		images.put("Hunting Equipment", R.drawable.huntinge);
		images.put("Hunting Produce", R.drawable.huntingp);
		images.put("Melee Armour - Low Level",R.drawable.armourl);
		images.put("Melee Armour - Mid Level", R.drawable.armourm);
		images.put("Melee Armour - High Level", R.drawable.armourh);
		images.put("Melee Weapons - Low Level",R.drawable.attackl);
		images.put("Melee Weapons - Mid Level",R.drawable.attackm);
		images.put("Melee Weapons - High Level",R.drawable.attackh);
		images.put("Mage Armour", R.drawable.magica);
		images.put("Mage Weapons", R.drawable.magicw);
		images.put("Range Armour", R.drawable.rangea);
		images.put("Range Weapons", R.drawable.rangew);
		images.put("Prayer Materials", R.drawable.prayerm);
		images.put("Prayer Armour", R.drawable.prayera);
		images.put("Summoning Scrolls", R.drawable.summoning);
		images.put("Woodcutting Product", R.drawable.woodcutting);
		images.put("Jewellery", R.drawable.jewelry);
		images.put("Tools and Containers", R.drawable.casket);
		images.put("Runes, Spells and Teleports", R.drawable.rune);
		images.put("Potions", R.drawable.potion);
		images.put("Miscellaneous", R.drawable.misc);
		images.put("Familiars",R.drawable.famil);
		images.put("Pocket Items", R.drawable.pock);
		images.put("Bolts", R.drawable.bolts);
		images.put("Arrows",R.drawable.arrow);
		images.put("Seeds",R.drawable.seed);
		images.put("Costumes",R.drawable.costume);
		images.put("Ammo",R.drawable.ammo);
		images.put("Food and Drink", R.drawable.food);
		images.put("Pocket Items", R.drawable.pock);
	}
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		String urlToLoad = "http://services.runescape.com/m=itemdb_rs/api/catalogue/category.json?category=";
		int categoryToLoad = position;
		urlToLoad = urlToLoad + categoryToLoad;
		GeCatLoader geCatLoader = new GeCatLoader();
		geCatLoader.urlToLoad = urlToLoad;
		geCatLoader.position = position;
		Utility.callLoaderAndHandler(geCatLoader);
	}
	private class GeCatLoader extends AsyncTask<String, Void, String>
	{
		String urlToLoad = "";
		int position = 0;
		protected void onPreExecute()
		{
			dialog = DialogUtility.createLoadingScreen("Loading " + cats[position] + " Category ..", GeHome.this);
			dialog.show();
		}
		@Override
		protected String doInBackground(String... params)
		{
			while(!isCancelled())
			{
				try 
				{
					// Send URL to parse JSON Objects
					JSONArray jItem = null;
					// Access and retrieve JSON file to be parsed
					URLConnection feedUrl = new URL(urlToLoad).openConnection();
					InputStream in = feedUrl.getInputStream();
					String json = Utility.convertStreamToString(in);
					Log.i("JASON", json);
					//Parse JSON 
					JSONObject jsonObj = new JSONObject(json); 
					jItem = jsonObj.getJSONArray("alpha");
					
					letter = new ArrayList<String>();
					items = new ArrayList<Integer>(); 
					for (int i = 0; i < jItem.length(); i++)
					{ 
						JSONObject item = jItem.getJSONObject(i);
						setLetter(item.getString("letter"));
						setItem(Integer.parseInt(item.getString("items")));
					} 
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
		@Override
		protected void onPostExecute(String result)
		{
			if(result.equals("completed") && dialog.isShowing())
			{
				Intent intent = new Intent(GeHome.this, GeneralCatList.class);
				Bundle b = new Bundle();
				b.putStringArrayList("letterList", getLetters());
				b.putIntegerArrayList("itemList", getItems());
				b.putInt("category", position);
				b.putString("categoryTitle", cats[position]);
				intent.putExtras(b);
				dialog.dismiss();
				startActivity(intent);
			}
			else if(result.equals("UnknownHostException") || result.equals("ConnectException"))
			{
				dialog.dismiss();
				Toast.makeText(GeHome.this, "Please double check your internet connection ..", Toast.LENGTH_LONG).show();
			}
			else if(result.contains("incomplete"))
			{
				dialog.dismiss();
				Toast.makeText(GeHome.this, "Unable to load category at this time, please try again later ..", Toast.LENGTH_LONG).show();
				DialogUtility.createBugReportDialog(GeHome.this, "GeHome", "GeCatLoader AsyncTask doInBackground()", exception);
			}
		}
		@Override
		protected void onCancelled()
		{
			dialog.dismiss();
			Toast.makeText(GeHome.this, "Response from wwww.runescape.com is slow, please try again later ..", Toast.LENGTH_LONG).show();
		}
	}
	public void setLetter(String alphabet)
	{
		letter.add(alphabet);
	}
	public void setItem(int total)
	{
		items.add(total);
	}
	public ArrayList<String> getLetters()
	{
		return letter;
	}
	public ArrayList<Integer> getItems()
	{
		return items;
	}
}
