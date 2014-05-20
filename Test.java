class Test
{
	
	public static void main (String[] args)
	{
		BubblesJSONParser parser = new BubblesJSONParser("{123:{\"key\" : [12, 13, 15], \"anotherkey\" :   \"some'quoted'value\" }, 12:[{1:2, 2:4}, 12, 56, \"string\"]}}");
		BubblesJSONObject obj = parser.parseString();
		System.out.println(obj.toFormattedString());
	}
	
}