package lucic.khalique.Runescape;

public class GeItem
{
	private String name = "";
	private String description = "";
	private String priceCurrent = "";
	private String type = "";
	private String iconUrl = "";
	private String id="";
	private String priceToday ="";

    public void setName(String s)
    {
    	name = s;
    }
    public void setDescription(String s)
    {
    	description  = s;
    }
 
    public void setPriceCurrent(String p)
    {
    	priceCurrent = p;
    }
    public void setPriceToday(String p)
    {
    	priceToday = p;
    }
    public String getPriceToday()
    {
    	return priceToday;
    }
    public void setType(String t)
    {
    	type = t;
    }
    public void setIconUrl(String u)
    {
    	iconUrl = u;
    }
    public String getName()
    {
    	return name;
    }
    public String getDescription()
    {
    	return description;
    }
 
    public String getPriceCurrent()
    {
    	return priceCurrent;
    }
    public String getType()
    {
    	return type;
    }
    public String getIconUrl()
    {
    	return iconUrl;
    }
  
    public void setId(int s)
    {
    	id = "" + s;
    }
    public String getId()
    {
    	return id;
    }
   
}	

