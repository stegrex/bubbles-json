class TestJSON
{
	
	public static void main (String[] args)
	{
		BubblesJSONParser parser = new BubblesJSONParser("{0: false }");
		BubblesJSONObject object = parser.parseString();
		System.out.println(object.toFormattedString());
	}
	
}