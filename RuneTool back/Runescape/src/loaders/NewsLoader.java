package loaders;

import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import lucic.khalique.Runescape.MyXMLHandler;
import lucic.khalique.Runescape.News;
import lucic.khalique.Runescape.XMLList;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import utilities.DialogUtility;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class NewsLoader extends AsyncTask<String, Void, String>
{
	Exception exception;
	XMLList alogList = null;
	Dialog loadingDialog;
	Context screen;
	XMLList newsList;
	public NewsLoader(Context con)
	{
		screen = con;
		loadingDialog = new Dialog(screen);
	}
	protected void onPreExecute()
	{
		loadingDialog = DialogUtility.createLoadingScreen("Loading News ..", screen);
		loadingDialog.show();
	}
	@Override
	protected String doInBackground(String... params)
	{
		while(!isCancelled())
		{
			try 
			{
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				URL sourceUrl = new URL("http://services.runescape.com/m=news/latest_news.rss"); ;
				xr.setContentHandler(new MyXMLHandler());
				xr.parse(new InputSource(sourceUrl.openStream()));
				newsList = MyXMLHandler.xMLList;
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
	protected void onPostExecute(String result)
	{
		loadingDialog.dismiss();
		if(result.equals("completed"))
		{
			Intent in = new Intent(screen, News.class);
			Bundle b = new Bundle();
			b.putStringArrayList("titleList", newsList.getTitle());
			b.putStringArrayList("linkList", newsList.getLink());
			b.putStringArrayList("descriptionList", newsList.getDescription());
			b.putStringArrayList("dateList",newsList.getDate());
			b.putStringArrayList("categories", newsList.getCategory());
			in.putExtras(b);
			screen.startActivity(in);
		}
		else if(result.equals("UnknownHostException") || result.equals("ConnectException"))
		{
			Toast.makeText(screen, "Please double check your internet connection ..", Toast.LENGTH_LONG).show();
		}
		else if(result.contains("incomplete"))
		{
			Toast.makeText(screen, "Unable to load News at this time, please try again later ..",Toast.LENGTH_LONG).show();
			DialogUtility.createBugReportDialog(screen, "HomeActivity", "NewsLoader AsyncTask doInBackground()", exception);
		}
	}
	@Override
	protected void onCancelled()
	{
		loadingDialog.dismiss();
		Toast.makeText(screen, "News response from www.runescape.com is slow, please try again later ..",Toast.LENGTH_LONG).show();
	}
}
