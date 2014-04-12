package lucic.khalique.Runescape;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class MyXMLHandler extends DefaultHandler
{
	public static XMLList xMLList;
	Boolean currentElement = false;
	String currentValue = null;
	
	Boolean inTitle = false;
	Boolean inDescription = false;
	Boolean inItem = false;
	Boolean inDate = false;
	Boolean inLink = false;
	Boolean inCategory = false;
	
	StringBuilder buff = null;
	
	public MyXMLHandler()
	{
		xMLList = new XMLList();
	}
	// All methods auto called in this order - start, characters, end
	/*
	 * Called when an xml tag starts
	 * imgView.setImageResource(R.drawable.newImage);
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		if(xMLList == null)
		{
			xMLList = new XMLList();
		}
		if(localName.equals("item"))
		{
			inItem = true;
		}
		if (inItem) 
		{
			Log.d("START " + localName,"");
	    	if (localName.equals("title")) 
	    	{
	    		inTitle = true;
	    		buff = new StringBuilder();
	    	}
	    	if (localName.equals("description")) 
	    	{
	    		inDescription = true;
	    		buff = new StringBuilder();
	    	}
	    	if (localName.equals("link")) 
	    	{
	    		inLink = true;
	    		buff = new StringBuilder();
	    	}
	    	if (localName.equals("pubDate")) 
	    	{
	    		inDate = true;
	    		buff = new StringBuilder();
	    	}
	    	if (localName.equals("category")) 
	    	{
	    		inCategory = true;
	    		buff = new StringBuilder();
	    	}
	    }
	}
	/*
	 * Called when an xml tag ends
	 */
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException
	{
		if (localName.equals("item"))
		{
			Log.d("END ITEM","");
			inItem = false;
		}
	    
		if (inTitle) 
	    {
	    	String check = buff.toString().trim();
	    	Log.d("TITLE:", check);
	    	Log.d("END " + localName,"");
	    	xMLList.setTitle(check);
	    	inTitle = false;
	    	buff = null;
	    }
	    else if (inDescription) 
	    {
	    	String check  = buff.toString().trim();
	    	Log.d("DESC:", check);
	    	Log.d("END " + localName,"");
	    	xMLList.setDescription(check);
	    	inDescription = false;
	    	buff = null;
	    }
	    else if (inLink) 
	    {
	    	String check  = buff.toString().trim();
	    	Log.d("LINK:", check);
	    	Log.d("END " + localName,"");
	    	xMLList.setLink(check);
	    	inLink = false;
	    	buff = null;
	    }
	    else if (inDate) 
	    {
	    	String check  = buff.toString().trim();
	    	Log.d("DATE:", check);
	    	Log.d("END " + localName,"");
	    	check = check.substring(0,16);
	    	xMLList.setDate(check);
	    	inDate = false;
	    	buff = null;
	    }
	    else if(inCategory)
	    {
	    	String check  = buff.toString().trim();
	    	Log.d("CATEGORY:", check);
	    	Log.d("END " + localName,"");
	    	xMLList.setCategory(check);
	    	inCategory = false;
	    	buff = null;
	    }
	}

	/*
	 * Called to get tag characters
	 */
	@Override
	public void characters(char[] ch, int start, int length)throws SAXException
	{
    	if (buff != null) 
    	{
    		for (int i = start; i < start + length; i++) 
    		{
    			buff.append(ch[i]);
    		}
    	}

    }
}

