package lucic.khalique.Runescape;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowDescription extends Activity 
{
	TextView title;
	TextView date;
	TextView desc;
	TextView link;
	Button image;
	int drawable;
	TextView category;
	HashMap<String, Integer> images = new HashMap<String, Integer>();
    public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        setContentView(R.layout.showdescription);
        String titleText = null;
        String dateText = null;
        String description = null;
        String linkText = null;
        String categoryText = null;
        
        title = (TextView)findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);
        desc = (TextView)findViewById(R.id.desc);
        link = (TextView)findViewById(R.id.link);
        image =(Button)findViewById(R.id.newsImage);
        category = (TextView)findViewById(R.id.category);
        
        Intent startingIntent = getIntent();
        
        if (startingIntent != null)
        {
            Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
            if (b == null)
            {
                titleText = "undefined";
                dateText = "undefined";
                linkText = "undefined";
                description = "undefined";
                categoryText = "undefined";
            }
            else
            {
                
            	if(b.getString("intent").contains("news"))
            	{
            		initNewsMap();
            		categoryText = b.getString("category");
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
                    category.setVisibility(View.GONE);
            	}
            	if(b.getString("intent").contains("alog"))
            	{
            		
            		initAlogMap();
            		category.setVisibility(View.GONE);
            		link.setVisibility(View.GONE);
            		titleText = b.getString("title"); 
            		for (Map.Entry<String, Integer> entry : images.entrySet()) 
        			{
        			    String key = entry.getKey();
        			    Object value = entry.getValue();
        			    if(titleText.contains(entry.getKey()))
        			    {
        			    	drawable = entry.getValue();
        			    	break;
        			    }
        			    else drawable = R.drawable.item;
        			}
            	}
                titleText = b.getString("title"); 
                dateText = b.getString("pubdate").toString();
                description = b.getString("description").trim();
                linkText = b.getString("link");
            }
        }
        else
        {
            
            titleText = "";
            dateText = "";
            linkText = "";
            description = "";
        
        }
        image.setBackgroundResource(drawable);
        title.setText(titleText);
        date.setText(dateText);
        desc.setText(description);
        link.setText(linkText);
    }
    public void initNewsMap()
	{
		images.put("Behind the Scenes News",R.drawable.behindthescenes);
		images.put("Customer Support News",R.drawable.customerservice);
		images.put("Game Update News",R.drawable.gameupdate);
		images.put("Website News",R.drawable.devblog);
		images.put("Technical News",R.drawable.technicalnews);
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
		images.put("Hunted", R.drawable.hunter);
		images.put("Levelled all skills over", R.drawable.level);
		images.put("Quest Points obtained", R.drawable.quest);
		images.put("Quest complete", R.drawable.complete);
		images.put("found", R.drawable.casket);
	}
}
