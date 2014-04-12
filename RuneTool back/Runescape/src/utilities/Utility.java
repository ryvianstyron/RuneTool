package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Utility
{
	public static String convertStreamToString(InputStream in)throws UnsupportedEncodingException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public static void callLoaderAndHandler(final AsyncTask<String, Void, String> loaderClass)
	{
		loaderClass.execute();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{
			public void run()
			{
				if(loaderClass.getStatus() == AsyncTask.Status.RUNNING)
				{
					loaderClass.cancel(true);
				}
			}
		}, 30000);
	}
	public static double largest(double a, double b, double c)
	{
		double largest = 0;
		if(a > b && a > c)
		{
			largest = a;
		}
		else if(b > a && b > c)
		{
			largest = b;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		}
		else if(c > a && c > b)
		{
			largest = c;
		}
		return largest;
	}
}
