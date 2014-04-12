package lucic.khalique.Runescape;

import java.util.ArrayList;
import java.util.HashMap;

import utilities.Utility;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PlayerStats extends ListActivity
{
	Exception exception;
	ArrayList<String> skillsList = new ArrayList<String>();
	static HashMap<String, Integer> images = new HashMap<String, Integer>();
	public static ArrayList<HashMap<String, Object>> list;
	public static String[][] skills;
	public static int RANK = 0;
	public static int LEVEL = 1;
	public static int TOTALXP = 2;
	public static String[] skillNames = { "Overall", "Attack", "Defense", "Strength",
			"Constitution", "Ranged", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", "Hunter", "Construction", "Summoning",
			"Dungeoneering" };
	public static SimpleAdapter adapter;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hslistview);
		Bundle b = this.getIntent().getExtras();
	    skillsList = b.getStringArrayList("skillList");
	    construct2dArray(skillsList);
		initAlogMap();
		list = new ArrayList<HashMap<String, Object>>();
		adapter = new SimpleAdapter(this, list,
				R.layout.hslistitem, new String[] { "image", "skill", "level",
						"xp" }, new int[] { R.id.hsimage, R.id.skillname,
						R.id.hslevel, R.id.hsxp });
		populateList();
		setListAdapter(adapter);
	}
	public void construct2dArray(ArrayList<String> skillList)
	{
		skills = new String[26][3];
		for(int i = 0; i< 26; i++)
		{
			String[] rankLevelXp = skillsList.get(i).split(",");
			for(int j = 0; j < 3; j++)
			{
				skills[i][j] = rankLevelXp[j+1];
			}
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
	@Override
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
	        	 Intent intent = new Intent(PlayerStats.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         default:return super.onOptionsItemSelected(item);
	     }
	}
	public static void populateList()
	{
		HashMap<String, Object> temp = new HashMap<String, Object>();
		for (int i = 0; i < skills.length; i++)
		{
			temp = new HashMap<String, Object>();
			temp.put("image", images.get(skillNames[i]));
			temp.put("skill", skillNames[i]);
			temp.put("level", "" + skills[i][LEVEL]);
			temp.put("xp", skills[i][TOTALXP]);
			list.add(temp);
		}
	}
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		final Dialog dialog = new Dialog(PlayerStats.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.showleveldesc);
		dialog.setCancelable(true);

		Button image = (Button) dialog.findViewById(R.id.levelimage);
		TextView title = (TextView) dialog.findViewById(R.id.leveltitle);
		TextView level = (TextView) dialog.findViewById(R.id.level);
		TextView rank = (TextView) dialog.findViewById(R.id.rank);
		TextView xp = (TextView) dialog.findViewById(R.id.totalxp);
		TextView nextxp = (TextView) dialog.findViewById(R.id.nextxp);
		ProgressBar bar = (ProgressBar) dialog.findViewById(R.id.progress);
		
		TextView percentText = (TextView)dialog.findViewById(R.id.percent);

		String skill = skillNames[position];
		int levelNo = Integer.parseInt(skills[position][LEVEL]);
		int xpNo = Integer.parseInt(skills[position][TOTALXP]);
		int rankNo = Integer.parseInt(skills[position][RANK]);

		image.setBackgroundResource(images.get(skillNames[position]));
		title.setText(skill);
		level.setText("Level : " + levelNo);
		rank.setText("Rank : " + rankNo);
		xp.setText("Experience : " + xpNo);

		double nextLevel = 0;
		for (int i = 1; i < levelNo + 1; i++) // maybe its not levelNo + 1?
		{
			double o = i / 7.0;
			double p = Math.pow(2, o);
			double q = 300 * p;

			nextLevel += Math.floor(i + q);
		}
		nextLevel = Math.floor(nextLevel / 4);

		double missingXp = nextLevel - xpNo;

		double minXpForCurrentLevel = 0;
		for (int i = 1; i < levelNo; i++) // maybe its not levelNo + 1?
		{
			double o = i / 7.0;
			double p = Math.pow(2, o);
			double q = 300 * p;

			minXpForCurrentLevel += Math.floor(i + q);
		}
		minXpForCurrentLevel = Math.floor(minXpForCurrentLevel / 4);

		double progressThroughLevel = xpNo - minXpForCurrentLevel;
		double totalXpForLevel = nextLevel - minXpForCurrentLevel;
		double remainingXp = nextLevel - xpNo;
		double percentProgress = (progressThroughLevel * 100) / totalXpForLevel;

		double meleeLevel = calculateMelee();
		double magicLevel = calculateMagic();
		double rangedLevel = calculateRanged();
		
		double combatLevel = Utility.largest(meleeLevel, magicLevel, rangedLevel);

		if(skill.contains("Overall"))
		{
			nextxp.setText("Combat Level is: " + (int)combatLevel + "\n" +  "\n" + "Progress to Level 138 is:");
			double percent = combatLevel/138;
			percent = percent*100;
			bar.setProgress((int)percent);
			percentText.setText("" + (int)percent + "%");
		}
		if (xpNo == 0 || percentProgress == 0)
		{
			bar.setProgress(0);
			nextxp.setText("Xp to Level " + (levelNo + 1) + " : " + (int)remainingXp);
			percentText.setText("0%");
		} else
		{
			if(levelNo==99 && !(skill.contains("Dungeoneering")))
			{
				
				nextxp.setVisibility(View.GONE);
				bar.setProgress(100);
				percentText.setText("100%");
			}
			if(levelNo == 120 && skill.contains("Dungeoneering"))
			{
				nextxp.setVisibility(View.GONE);
				bar.setProgress(100);
				percentText.setText("100%");
			}
			if(levelNo != 99 && !(skill.contains("Overall")))
			{
				Log.i("OOOOOOO", "");
				Log.i("percent Through Level: ", "" + percentProgress);
				Log.i("DEBUG", "xp for " + levelNo + " is " + minXpForCurrentLevel);
				Log.i("DEBUG", "xp for " + (levelNo + 1) + " is " + nextLevel);
				Log.i("DEBUG", "xp left for " + (levelNo + 1) + " "
					+ skillNames[position] + " is " + remainingXp);
				Log.i("next Level: ", "" + missingXp);
				bar.setProgress((int) percentProgress);
				percentText.setText((int)percentProgress + "%");
				nextxp.setText("Xp to Level " + (levelNo + 1) + " : " + (int)remainingXp);
			}	
		}
		dialog.show();
	}
	public void initAlogMap()
	{
		images.put("Overall", R.drawable.overall);
		images.put("Fletching", R.drawable.fletching);
		images.put("Agility", R.drawable.agility);
		images.put("Attack", R.drawable.attack);
		images.put("Construction", R.drawable.construction);
		images.put("Cooking", R.drawable.cooking);
		images.put("Crafting", R.drawable.crafting);
		images.put("Defense", R.drawable.defense);
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
		images.put("Smithing", R.drawable.smithing);
	}
	public double calculateRanged()
	{
		double ranged = Integer.parseInt(skills[5][LEVEL]);
		double defense = Integer.parseInt(skills[2][LEVEL]);
		double hp = Integer.parseInt(skills[4][LEVEL]);
		double prayer = Integer.parseInt(skills[6][LEVEL]);
		double summon = Integer.parseInt(skills[24][LEVEL]);
		prayer = 0.5*prayer;
		summon = 0.5*summon;
		ranged = 1.3*1.5*ranged;
		double level = 0.25*(ranged + defense + hp +prayer + summon);
		return level;
	}
	public double calculateMelee()
	{
		double attack = Integer.parseInt(skills[1][LEVEL]);
		double strength = Integer.parseInt(skills[3][LEVEL]);
		double defense = Integer.parseInt(skills[2][LEVEL]);
		double hp = Integer.parseInt(skills[4][LEVEL]);
		double prayer = Integer.parseInt(skills[6][LEVEL]);
		double summon = Integer.parseInt(skills[24][LEVEL]);
		double mele = 1.3*(attack+strength);
		prayer = 0.5*prayer;
		summon = 0.5*summon;
		double level = 0.25*(mele+defense+hp+prayer+summon);
		return level;
	}
	public double calculateMagic()
	{
		double magic = Integer.parseInt(skills[7][LEVEL]);
		double defense = Integer.parseInt(skills[2][LEVEL]);
		double hp = Integer.parseInt(skills[4][LEVEL]);
		double prayer = Integer.parseInt(skills[6][LEVEL]);
		double summon = Integer.parseInt(skills[24][LEVEL]);
		prayer = 0.5*prayer;
		summon = 0.5*summon;
		magic = 1.3*1.5*magic;
		double level = 0.25*(magic+defense+hp+prayer+summon);
		return level;
	}
}
