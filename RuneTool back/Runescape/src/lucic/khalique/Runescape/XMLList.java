package lucic.khalique.Runescape;

import java.util.ArrayList;

public class XMLList
{

	/** Variables */
	private ArrayList<String> title = new ArrayList<String>();
	private ArrayList<String>  detail = new ArrayList<String>();
	private ArrayList<String> description = new ArrayList<String>();
	private ArrayList<String> link = new ArrayList<String>();
	private ArrayList<String> date = new ArrayList<String>();
	private ArrayList<String> category = new ArrayList<String>();
	/**
	 * In Setter method default it will return arraylist change that to add
	 */

	public XMLList()
	{
		title.clear();
		detail.clear();
		description.clear();
		link.clear();
		date.clear();
		category.clear();
	}
	public ArrayList<String> getTitle()
	{
		return title;
	}
	public void setTitle(String name)
	{
		this.title.add(name);
	}
	public ArrayList<String> getDetail()
	{
		return detail;
	}
	public void setDetail(String detail)
	{
		this.detail.add(detail);
	}
	public ArrayList<String> getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description.add(description);
	}
	public void setLink(String link)
	{
		this.link.add(link);
	}
	public ArrayList<String> getLink()
	{
		return link;
	}
	public void setDate(String date)
	{
		this.date.add(date);
	}
	public ArrayList<String> getDate()
	{
		return date;
	}
	public void setCategory(String cat)
	{
		this.category.add(cat);
	}
	public ArrayList<String> getCategory()
	{
		return category;
	}
	public void clearLists()
	{
		
	}
	
}