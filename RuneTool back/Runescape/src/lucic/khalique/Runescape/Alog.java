package lucic.khalique.Runescape;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utilities.DialogUtility;
import utilities.Utility;

import android.app.Dialog;
import android.app.ListActivity;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class Alog extends ListActivity
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
	int drawable;
	TextView category;
	TextView back;
	String temp = "didnt load yet";
	Boolean clicked = false;
	ScrollView scroll;
	ArrayList<HashMap<String,Object>> list;
	private static ArrayList<String> titles;
    private static ArrayList<String> descriptions;
    private static ArrayList<String> dates;
    SharedPreferences settings;
	SharedPreferences.Editor editor;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);       
        setContentView(R.layout.listview);
	    list = new ArrayList<HashMap<String,Object>>();
	    SimpleAdapter adapter = new SimpleAdapter(
	    	    this,
	    	    list,
	    	    R.layout.aloglistitem,
	    	    new String[] {"image","title","date"},
	    	    new int[] {R.id.newsImage,R.id.newsTitle,R.id.dateText}
	    	    );
	    
	    Bundle b = this.getIntent().getExtras();
	    titles = b.getStringArrayList("titleList");
	    descriptions = b.getStringArrayList("descriptionList");
	    dates = b.getStringArrayList("dateList");
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
	        	 Intent intent = new Intent(Alog.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         default:return super.onOptionsItemSelected(item);
	     }
	}
	public void onBackPressed()
	{
		super.onBackPressed();
		this.finish();
	}
	public  void populateList()
	{
		initAlogMap();
		for (int i = 0; i < titles.size(); i++) 
		{
	    		HashMap<String, Object> temp = new HashMap<String, Object>();
	    		int drawable = R.drawable.item;
	    		for (Map.Entry<String, Integer> entry : images.entrySet()) 
				{
				    String key = entry.getKey();
				    Object value = entry.getValue();
				    if(titles.get(i).contains(entry.getKey()))
				    {
				    	drawable = entry.getValue();
				    	break;
				    }
				    else drawable = R.drawable.item;
				}
	    		temp.put("title", titles.get(i));
	    		temp.put("image", drawable);
	    		temp.put("date", dates.get(i));
	    		list.add(temp);	
	    }
	}
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		String titleText = null;
        String dateText = null;
        String description = null;
        
        final Dialog dialog = new Dialog(Alog.this);
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
        moreinfo.setVisibility(View.GONE);
		category.setVisibility(View.GONE);

		scroll.setVisibility(View.GONE);
		
		titleText = titles.get(position);
		for (Map.Entry<String, Integer> entry : images.entrySet()) 
		{
		    //String key = entry.getKey(); Object value = entry.getValue();
		    if(titleText.contains(entry.getKey()))
		    {
		    	drawable = entry.getValue();
		    	break;
		    }
		    else drawable = R.drawable.item;
		}
        dateText = dates.get(position);
        description = descriptions.get(position);
        String array [];
    	int index = 0;
    	if (description.contains("/>"))
		{
    		array = description.split("/>");
    		index = description.indexOf("10;");
    		description = (array[array.length - 1].trim());
		}
        
        image.setBackgroundResource(drawable);
        title.setText(titleText);
        date.setText(dateText);
        desc.setText(description);
        dialog.show();
	}
	public void initAlogMap()
	{
		images.put("Fletching", R.drawable.fletching);
		images.put("Agility", R.drawable.agility);
		images.put("Attack", R.drawable.attack);
		images.put("Construction", R.drawable.construction);
		images.put("Cooking", R.drawable.cooking);
		images.put("Crafting", R.drawable.crafting);
		images.put("Defence", R.drawable.defense);
		images.put("Dungeoneering", R.drawable.dungeon);
		images.put("Farming", R.drawable.farming);
		images.put("Firemaking", R.drawable.firemaking);
		images.put("Fishing", R.drawable.fishing);
		images.put("Herblore", R.drawable.herblore);
		images.put("Constitution", R.drawable.hitpoint);
		images.put("Hunter", R.drawable.hunter);
		images.put("Magic", R.drawable.magic);
		images.put("Mining", R.drawable.mining);
		images.put("Ranged", R.drawable.ranged);
		images.put("Slayer", R.drawable.slayer);
		images.put("Strength", R.drawable.strength);
		images.put("Summoning", R.drawable.summoning);
		images.put("Thieving", R.drawable.thieving);
		images.put("Woodcutting", R.drawable.woodcutting);
		images.put("Runecrafting", R.drawable.runecrafting);
		images.put("Prayer", R.drawable.prayer);
		images.put("Easy", R.drawable.easy);
		images.put("Elite", R.drawable.elite);
		images.put("Hard", R.drawable.hard);
		images.put("Medium", R.drawable.medium);
		images.put("songs", R.drawable.music);
		images.put("killed", R.drawable.kill);
		images.put("defeated",R.drawable.kill);
		images.put("Hunted", R.drawable.hunter);
		images.put("Levelled all skills over", R.drawable.level);
		images.put("Quest Points obtained", R.drawable.quest);
		images.put("Quest complete", R.drawable.complete);
		images.put("found", R.drawable.casket);
		images.put("Total levels gained", R.drawable.medal);
		images.put("reached",  R.drawable.medal);
		images.put("Improved",  R.drawable.medal);
		images.put("Smithing",R.drawable.smithing);
		images.put("Tattoo",R.drawable.tattoo);
		images.put("case",R.drawable.justice);
	}
	public String removeTags(String string)
	{
		if(string.contains("<p>") || string.contains("</p>"))
		{
			string = string.replaceAll("<p>", "");
			string = string.replaceAll("</p>","");
		}
		if(string.contains("<b>") || string.contains("</b>"))
		{
			string = string.replaceAll("<b>", "");
			string = string.replaceAll("</b>","");
		}
		if(string.contains("<i>") || string.contains("</i>"))
		{
			string = string.replaceAll("<i>", "");
			string = string.replaceAll("</i>","");
		}
		return string;
	}
}
