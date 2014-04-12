package lucic.khalique.Runescape;

import java.util.ArrayList;
import java.util.HashMap;

import loaders.AlogLoader;
import loaders.StatsLoader;
import loaders.UserValidator;
import utilities.Utility;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsActivity extends ListActivity
{
	Exception exception;
	String function = "no function";
	String stringToReturn = null;
	String friendsString = null;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
	Dialog dialog;
	String friendName = "";
	XMLList alogList;
	Dialog friendInfo;
	boolean friendValidated = false;
	AlogLoader loader;
	EditText friend;
	Button addFriend;
	Button dismiss;
	Button addorDelete;
	Button addorDeleteImage;
	TextView title;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);
		
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.friendlistitem, new String[] { "friend" },
				new int[] { R.id.friend });
		populateList();
		setListAdapter(adapter);
		addFriend = (Button)findViewById(R.id.addfriendbutton);
		
		// Create dialog 
		dialog = new Dialog(FriendsActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.addordeletefrienddialog);
		dialog.setCancelable(false);
		
		dismiss = (Button)dialog.findViewById(R.id.dismiss);
		friend = (EditText)dialog.findViewById(R.id.friendname);
		addorDelete = (Button)dialog.findViewById(R.id.addordelete);
		addorDeleteImage = (Button)dialog.findViewById(R.id.adddeleteimage);
		title = (TextView)dialog.findViewById(R.id.adddeletetitle);
		
		addFriend.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				function = "add";
	        	addorDeleteImage.setBackgroundResource(R.drawable.addfriend);
	 			title.setText("Add Friend");
	 			addorDelete.setText("Add Friend");
	 			dialog.show();
			}
		});
		TextView.OnEditorActionListener enterListener = new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView friend, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) 
				{ 
					onAddDeleteFriend();    
				}
				return true;
			}
		};
		friend.setOnEditorActionListener(enterListener);
		addorDelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onAddDeleteFriend();
			}
		});
		dismiss.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
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
	public void populateList()
	{
		SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		
		String friendsString = settings.getString("friends", null);
		
		if(friendsString != null)
		{
			String [] friends = friendsString.split(",");
			for (int i = 0; i < friends.length; i++)
			{
				HashMap<String, Object> temp = new HashMap<String, Object>();
				temp.put("friend", friends[i]);
				list.add(temp);
			}
		}
	}
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		friendName = l.getItemAtPosition(position).toString().substring(8, l.getItemAtPosition(position).toString().length()-1);
		friendInfo = new Dialog(FriendsActivity.this);
		friendInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
		friendInfo.setContentView(R.layout.viewfriendstuff);
		friendInfo.setCancelable(true);
		TextView title = (TextView) friendInfo.findViewById(R.id.friendtitle);
		LinearLayout al= (LinearLayout)friendInfo.findViewById(R.id.clickal);
		LinearLayout hs= (LinearLayout)friendInfo.findViewById(R.id.clickhs);
		title.setText(friendName);
		al.setOnClickListener(new OnClickListener()
		{
			public void onClick(View w)
			{
				Utility.callLoaderAndHandler(new AlogLoader(friendName, FriendsActivity.this));
				friendInfo.dismiss();
			}
		});
		hs.setOnClickListener(new OnClickListener()
		{
			public void onClick(View w)
			{
				SharedPreferences settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
				String userType = settings.getString("userType", null);
				Utility.callLoaderAndHandler(new StatsLoader(friendName, FriendsActivity.this, userType));
				friendInfo.dismiss();
			}
		});
		friendInfo.show();
	}

	public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friends_menu, menu);
        return true;
    }
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Create settings and editor
		settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		editor = settings.edit();
	     switch (item.getItemId()) 
	     {
	         case R.id.logout:
	        	 editor.clear();
	        	 editor.putString("registered","no");
	        	 editor.commit();
	        	 Intent intent = new Intent(FriendsActivity.this, RegistrationActivity.class);
	        	 startActivity(intent);
	        	 finish();
	             return true;
	         case R.id.addfriend:
	        	function = "add";
	        	addorDeleteImage.setBackgroundResource(R.drawable.addfriend);
	 			title.setText("Add Friend");
	 			addorDelete.setText("Add Friend");
	 			dialog.show();
	     		return true;
	         case R.id.deletefriend:
	        	function = "delete"; 
	        	addorDeleteImage.setBackgroundResource(R.drawable.deletefriend);
	 			title.setText("Delete Friend");
	 			addorDelete.setText("Delete Friend");
	 			friendsString = settings.getString("friends", null);
	 			if(friendsString == null)
	 			{
	 				Toast.makeText(FriendsActivity.this, "There are no friends to delete..", Toast.LENGTH_SHORT).show();
	 			}
	 			else
	 			{
	 				dialog.show();
	 			}
	        	return true;
	         default:return super.onOptionsItemSelected(item);
	     }
	}
	
	public void onAddDeleteFriend()
	{
		if(friend.getText().toString().equals(""))
		{
			Toast.makeText(FriendsActivity.this, "Please enter a username..", Toast.LENGTH_LONG).show();
		}
		else
		{
			dialog.dismiss();
			stringToReturn = friend.getText().toString().trim();
			if(function.equals("add")) // validate friend being added
			{
				if(stringToReturn!=null)
	     		{
					if(matchesCurrentUser(stringToReturn))
					{
						Toast.makeText(FriendsActivity.this,"Your username cannot be added to your friendslist .. add a friend instead :)", Toast.LENGTH_LONG).show();
					}
					else if(!matchesCurrentUser(stringToReturn))
					{
						// check for duplicates
						String friendString = settings.getString("friends",null);
						boolean duplicateExists = duplicate(stringToReturn, friendString);
						if(!duplicateExists)
						{
							// validate friend being added
							friendName = stringToReturn; 
							Utility.callLoaderAndHandler(new UserValidator(stringToReturn, FriendsActivity.this));
						}
						else if(duplicateExists == true)
						{
							Toast.makeText(FriendsActivity.this,"Friend name already exists..", Toast.LENGTH_LONG).show();
						}
					}
	     		}
			}
			else if(function.equals("delete"))
			{
		        	 friendsString = settings.getString("friends", null);
		        	 String [] friends = friendsString.split(",");
		        	 ArrayList<String> friendList = new ArrayList<String>();
		        	 for(String s : friends)
		        	 {
		        		 friendList.add(s);
		        	 }
		        	 ArrayList<String> temp = friendList; // This is creating a reference
		        	 for(String s : friendList)
		        	 {
		        		if(stringToReturn.equals(s))
	        			 {
	        				 temp.remove(s);
	        				 break;
	        			 }
		        	 }
		        	 //form string again
		        	 String updatedfriends = "";
		        	 if(temp.size() > 0)
		        	 {	 
		        		 for(String s : temp)
		        		 {
		        			 updatedfriends = updatedfriends + s + ",";
		        		 }
		        		 updatedfriends = updatedfriends.substring(0, updatedfriends.length()-1);
		        	 }
		        	 else updatedfriends = null;
		        	 editor.putString("friends", updatedfriends);
		        	 editor.commit();
		        	 updateList();
			}
		}
	}
	public boolean matchesCurrentUser(String friendToAdd)
	{
		boolean result = false;
		settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		editor = settings.edit();
		String memberType = settings.getString("memberType", null);
		if (memberType.equals("free2play"))
		{
			return result;
		}
		String currentUser = settings.getString("memberName", null);
		if(currentUser.equals(friendToAdd))
		{
			result = true;
		}
		else if(!currentUser.equals(friendToAdd))
		{
			result = false;
		}
		return result;
	}
	public boolean duplicate(String friendToAdd, String friendlist)
	{
		boolean duplicate = false;
		if(friendlist == null)
		{
			return false;
		}
		else
		{
			String [] friendArray = friendlist.split(",");
			for(String s : friendArray)
			{
				if(s.equals(friendToAdd))
				{
					duplicate = true;
					break;
				}
			}
			return duplicate;
		}
	}
	public void clearFriends()
	{
		settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
		editor = settings.edit();
		editor.putString("friends", null);
 		editor.commit();
	}
	public void updateList()
	{
		 Intent intentRestart = new Intent(FriendsActivity.this, FriendsActivity.class);
    	 startActivity(intentRestart);
    	 finish();
	}
}
