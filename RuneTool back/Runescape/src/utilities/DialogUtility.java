package utilities;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;

import lucic.khalique.Runescape.HomeActivity;
import lucic.khalique.Runescape.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogUtility
{
	static Dialog dialog;
	static Button yes;
	static Button no;
	static Button image;
	static TextView text;
	static TextView title;
	static LinearLayout heading;
	static LinearLayout buttonHolder;
	static ImageView imageView;
	static AnimationDrawable splashAnimation;
	
	public static void intializeSimpleDialog()
	{
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.simpledialog);
		dialog.setCancelable(true);
		text = (TextView)dialog.findViewById(R.id.sdtext);
		no = (Button)dialog.findViewById(R.id.sdno);
		yes = (Button)dialog.findViewById(R.id.sdyes);
		image = (Button)dialog.findViewById(R.id.sdimage);
		heading = (LinearLayout)dialog.findViewById(R.id.sdheading);
		title = (TextView)dialog.findViewById(R.id.sdtitle);
		buttonHolder = (LinearLayout)dialog.findViewById(R.id.sdbuttonholder);
	}
	public static Dialog createTutorialDialog(final Context con)
	{
		dialog = new Dialog(con);
		intializeSimpleDialog();
		dialog.setCancelable(false);
		image.setBackgroundResource(R.drawable.info);
		
		text.setVisibility(View.GONE);
		title.setText("View Rune Tool Tutorial?");
		yes.setText("Yes");
		no.setText("No");
		no.setOnClickListener(new OnClickListener()
		{
			public void onClick(View w)
			{
				title.setText("To view the tutorial later, tap menu options and select info.");
				title.setTextSize(13);
				image.setVisibility(View.GONE);
				yes.setVisibility(View.GONE);
				no.setVisibility(View.GONE);
				dialog.setCancelable(true);
			}
		});
		yes.setOnClickListener(new OnClickListener()
		{
			public void onClick(View w)
			{
				dialog.dismiss();
				dialog = new Dialog(con);
				dialog.setCancelable(true);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.tutorial);
				dialog.show();
			}
		});
		return dialog;
	}
	public static Dialog createLoadingScreen(String message, Context con)
	{
		dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.miniloading);
		imageView = (ImageView)dialog.findViewById(R.id.loadinglogo);
		text = (TextView)dialog.findViewById(R.id.loadingtext);
		imageView.setImageBitmap(null);
		imageView.setBackgroundResource(R.drawable.loadingscreenanimation);
		splashAnimation = (AnimationDrawable) imageView.getBackground();
		imageView.post(new Runnable()
		{
			public void run() 
			{
				if ( splashAnimation != null ) splashAnimation.start();
			}
		});
		dialog.setCancelable(false);
		text.setText(message);
		return dialog;
	}
	public static Dialog createSimpleDialog(String message, Context con)
	{
		dialog = new Dialog(con);
		intializeSimpleDialog();
		buttonHolder.setVisibility(View.GONE);
		heading.setVisibility(View.GONE);
		text.setText(message);
		return dialog;
	}
	public static void createBugReportDialog(final Context con, final String className, final String methodName, final Exception e)
	{
		StringWriter sw = new StringWriter();
    	e.printStackTrace(new PrintWriter(sw));
    	final String stackTrace = sw.toString();
		dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.bugdialog);
		yes = (Button)dialog.findViewById(R.id.yes);
		no = (Button)dialog.findViewById(R.id.no);
		dialog.setCancelable(false);
		dialog.show();
		yes.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dialog.dismiss();
				String dateTime = DateFormat.getDateTimeInstance().format(new Date());
				String message = "Thrown from:\t" + className +"\nThrown in:\t" + methodName +"\nException Thrown:\t" + e.toString() + 
						"\nException Stack Trace:\n" + stackTrace; 
				Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, dateTime + " - Rune Tool Bug Report");
				intent.putExtra(Intent.EXTRA_TEXT, message);
				intent.setData(Uri.parse("mailto:darkravedev@gmail.com")); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
				con.startActivity(intent);
				
				if(con.toString().contains("HighScores2"))
				{
					con.startActivity(new Intent(con, HomeActivity.class));
				}
				if(con.toString().contains("Time"))
				{
					con.startActivity(new Intent(con, HomeActivity.class));
				}
				((Activity) con).finish();
			}
		});
		no.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				dialog.dismiss();
				if(con.toString().contains("HighScores2"))
				{
					con.startActivity(new Intent(con, HomeActivity.class));
				}
				if(con.toString().contains("Time"))
				{
					con.startActivity(new Intent(con, HomeActivity.class));
				}
				((Activity) con).finish();
			}
		});
	}
}
