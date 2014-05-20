class TestJSON
{
	
	public static void main (String[] args)
	{
		BubblesJSONObject mainObject = new BubblesJSONObject();
		mainObject.add("key", "value");
		
		BubblesJSONObject arrayElement = new BubblesJSONObject();
		arrayElement.add("arrayvalue1");
		arrayElement.add("arrayvalue2");
		arrayElement.add("arrayvalue3");
		
		mainObject.add("somearray", arrayElement);
		
		BubblesJSONObject anotherObject = new BubblesJSONObject();
		anotherObject.add("innerkey", "innervalue");
		mainObject.add("nestedobject", anotherObject);
		
		System.out.println(mainObject.toFormattedString()); // Prints the JSON representation as a formatted String.
		
		mainObject.getObject("nestedobject").add("innerkey2", "added element"); // Adding a key-value pair to the nested object.
		mainObject.getObject("nestedobject").add("innerkey", "changed value!"); // Changing an existing key-value pair of the nested object.
		
		System.out.println("\n\nNow printing the JSON representation after changes to the data structure...\n\n");
		
		System.out.println(mainObject.toFormattedString()); // Prints the JSON representation as a formatted String.
		
	}
	
}