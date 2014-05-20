import java.util.*;

class BubblesJSONObject
{
	
	// 2-10-13: To do: Handle nulls and empty arrays/objects.
	
	public static final String TYPE_ARRAY = "TYPE_ARRAY";
	public static final String TYPE_OBJECT = "TYPE_OBJECT";
	
	//private boolean isEmpty;
	
	private String type;
	private ArrayList<Object> arrayElements;
	private HashMap<Object, Object> hashElements;
	
	private boolean outputFormattedString = false;
	
	public BubblesJSONObject ()
	{
		//this.isEmpty = true;
		this.type = BubblesJSONObject.TYPE_OBJECT;
		this.arrayElements = new ArrayList<Object>(0);
		this.hashElements = new HashMap<Object, Object>(0);
	}
	public BubblesJSONObject (Object value)
	{
		//this.isEmpty = false;
		this.arrayElements = new ArrayList<Object>(0);
		this.addValue(value);
	}
	public BubblesJSONObject (Object key, Object value)
	{
		//this.isEmpty = false;
		this.hashElements = new HashMap<Object, Object>(0);
		this.addKeyValue(key, value);
	}
	
	public boolean add (Object value)
	{
		return this.addValue(value);
	}
	public boolean add (Object key, Object value)
	{
		return this.addKeyValue(key, value);
	}
	public boolean addValue (Object value)
	{
		//this.isEmpty = false;
		this.type = BubblesJSONObject.TYPE_ARRAY;
		this.arrayElements.add(value);
		return true;
	}
	public boolean addKeyValue (Object key, Object value)
	{
		if (!(key instanceof Integer || key instanceof String))
		{
			return false;
		}
		//this.isEmpty = false;
		this.type = BubblesJSONObject.TYPE_OBJECT;
		this.hashElements.put(key, value);
		return true;
	}
	
	/*
	public Object getIterableObject ()
	{
		switch (this.type)
		{
			case BubblesJSONObject.TYPE_ARRAY:
				return this.arrayElements;
			case BubblesJSONObject.TYPE_OBJECT:
				return this.hashElements;
			default:
				break;
		}
		return false;
	}
	*/
	
	public boolean doesKeyExist (Object key)
	{
		if (this.type == BubblesJSONObject.TYPE_OBJECT)
		{
			return this.hashElements.containsKey(key);
		}
		return false;
	}
	
	public boolean isObjectEmpty ()
	{
		switch (this.type)
		{
			case BubblesJSONObject.TYPE_ARRAY:
				return this.arrayElements.isEmpty();
			case BubblesJSONObject.TYPE_OBJECT:
				return this.hashElements.isEmpty();
			default:
				break;
		}
		return true;
	}
	
	public Set<Object> getKeySet ()
	{
		//if (this.type == BubblesJSONObject.TYPE_OBJECT)
		{
			return this.hashElements.keySet();
		}
		//return false;
	}
	
	public Object get (Object key)
	{
		switch (this.type)
		{
			case BubblesJSONObject.TYPE_ARRAY:
				return this.arrayElements.get((Integer)key);
			case BubblesJSONObject.TYPE_OBJECT:
				return this.hashElements.get(key);
			default:
				break;
		}
		return false;
	}
	public BubblesJSONObject getObject (Object key)
	{
		return (BubblesJSONObject)this.get(key);
	}
	
	public String repeatTab (int times)
	{
		String output = "";
		for (int i = 0; i < times; i++)
		{
			output += "    ";
		}
		return output;
	}
	
	public String toFormattedString ()
	{
		this.outputFormattedString = true;
		String output = this.toString(0);
		this.outputFormattedString = false;
		return output;
	}
	
	public String toFormattedString (int depth)
	{
		this.outputFormattedString = true;
		String output = this.toString(depth);
		this.outputFormattedString = false;
		return output;
	}
	
	public String toString ()
	{
		return this.toString(0);
	}
	
	public String toString (int depth)
	{
		boolean firstElement = true;
		String output = "";
		if (this.type == TYPE_ARRAY)
		{
			output += (this.outputFormattedString == true) ? repeatTab(depth)+"[\n" : "[ ";
			for (Object value : this.arrayElements)
			{
				if (firstElement == false)
				{
					output += (this.outputFormattedString == true) ? ",\n" : ", ";
				}
				firstElement = false;
				output += repeatTab(depth+1);
				if (value instanceof Integer || value instanceof Double)
				{
					output += value;
				}
				else if (value instanceof String)
				{
					output += "\""+value+"\"";
				}
				else if (value instanceof Boolean)
				{
					output += (boolean)value ? "true" : "false";
				}
				else if (value instanceof BubblesJSONObject)
				{
					output += (this.outputFormattedString) ? "\n"+((BubblesJSONObject)value).toFormattedString(depth+1) : ((BubblesJSONObject)value).toString(depth+1);
				}
			}
			output += (this.outputFormattedString == true) ? "\n"+repeatTab(depth)+"]" : " ]";
		}
		else if (this.type == TYPE_OBJECT)
		{
			output += (this.outputFormattedString == true) ? repeatTab(depth)+"{\n" : "{ ";
			for (Map.Entry<Object, Object> entry : this.hashElements.entrySet())
			{
				if (firstElement == false)
				{
					output += (this.outputFormattedString == true) ? ",\n" : ", ";
				}
				firstElement = false;
				output += repeatTab(depth+1);
				if (entry.getKey() instanceof Integer)
				{
					output += entry.getKey();
				}
				else if (entry.getKey() instanceof String)
				{
					output += "\""+entry.getKey()+"\"";
				}
				output += " : ";
				if (entry.getValue() instanceof Integer || entry.getValue() instanceof Double)
				{
					output += entry.getValue();
				}
				else if (entry.getValue() instanceof String)
				{
					output += "\""+entry.getValue()+"\"";
				}
				else if (entry.getValue() instanceof Boolean)
				{
					output += (boolean)(entry.getValue()) ? "true" : "false";
				}
				else if (entry.getValue() instanceof BubblesJSONObject)
				{
					output += (this.outputFormattedString) ? "\n"+((BubblesJSONObject)entry.getValue()).toFormattedString(depth+1) : ((BubblesJSONObject)entry.getValue()).toString(depth+1);
				}
			}
			output += (this.outputFormattedString == true) ? "\n"+repeatTab(depth)+"}" : " }";
		}
		return output;
	}
	
}