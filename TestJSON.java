class TestJSON
{
	
	public static void main (String[] args)
	{
		///*
		BubblesJSONParser parser = new BubblesJSONParser("{\"sdfsdfsdf\":\"sdfsdfsdf}\"}");
		BubblesJSONObject object = parser.parseString();
		System.out.println(object.toFormattedString());
		//*/
		/*
		BubblesJSONObject object = new BubblesJSONObject();
		object.add(1, false);
		System.out.println(object.toFormattedString());
		*/
	}
	
}