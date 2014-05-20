class BubblesJSONParser
{
	
	public static final String DATA_TYPE_ARRAY = "DATA_TYPE_ARRAY";
	public static final String DATA_TYPE_OBJECT = "DATA_TYPE_OBJECT";
	
	public static final String PARSE_STATE_SYNTAX = "PARSE_STATE_SYNTAX";
	public static final String PARSE_STATE_KEY = "PARSE_STATE_KEY";
	public static final String PARSE_STATE_VALUE = "PARSE_STATE_VALUE";
	
	public static final String PARSE_TYPE_NULL = "PARSE_TYPE_NULL";
	public static final String PARSE_TYPE_BOOLEAN = "PARSE_TYPE_BOOLEAN";
	public static final String PARSE_TYPE_INT = "PARSE_TYPE_INT";
	public static final String PARSE_TYPE_DOUBLE = "PARSE_TYPE_DOUBLE";
	public static final String PARSE_TYPE_STRING = "PARSE_TYPE_STRING";
	public static final String PARSE_TYPE_ARRAY = "PARSE_TYPE_ARRAY";
	public static final String PARSE_TYPE_OBJECT = "PARSE_TYPE_OBJECT";
	public static final String PARSE_TYPE_OTHER = "PARSE_TYPE_OTHER";
	
	private static final String CHAR_TYPE_WHITESPACE = " \t\r\n";
	private static final String CHAR_TYPE_ELEMENT_DELIMITER = ":,";
	private static final String CHAR_TYPE_OPENING_OBJECT_DELIMITER = "[{";
	private static final String CHAR_TYPE_CLOSING_OBJECT_DELIMITER = "]}";
	
	public String JSONString;
	public BubblesJSONObject object;
	
	private String keyType;
	private String valueType;
	private Object key;
	private Object value;
	
	private String currentDataType;
	private String currentParseState;
	private String currentParseType;
	
	private int subStringCharIndex;
	private BubblesJSONParser subParser;
	private BubblesJSONObject subObject;
	
	//private int previousDelimiterErrorIndex; // Revisit. Use a general error index if required.
	
	public BubblesJSONParser (String JSONString)
	{
		this.JSONString = JSONString;
		this.currentParseState = BubblesJSONParser.PARSE_STATE_SYNTAX;
		this.currentParseType = BubblesJSONParser.PARSE_TYPE_OTHER;
	}
	
	private String getStringAtCharIndex (int charIndex, String characters)
	{
		return String.valueOf(characters.charAt(charIndex));
	}
	private String getStringAtCharIndex (int charIndex)
	{
		return getStringAtCharIndex(charIndex, this.JSONString);
	}
	private boolean compareCharAtIndexWithString (int charIndex, String inputString, String characters)
	{
		for (int i = 0; i < characters.length(); i++)
		{
			if (this.getStringAtCharIndex(charIndex, inputString).equals(this.getStringAtCharIndex(i, characters)))
			{
				return true;
			}
		}
		return false;
	}
	private boolean compareCharAtIndexWithString (int charIndex, String characters) // Revisit. Need long?
	{
		return this.compareCharAtIndexWithString(charIndex, this.JSONString, characters);
	}
	
	private String getSubObjectStringFromCharIndex (int charIndex)
	{
		String output = "";
		boolean insideString = false;
		String stringClosingQuote = "";
		int stringClosingQuoteIndex = 0;
		int arrayNestCount = 0;
		int objectNestCount = 0;
		for (int i = charIndex; i < this.JSONString.length(); i++)
		{
			if (insideString == false)
			{
				switch (this.currentParseType)
				{
					case BubblesJSONParser.PARSE_TYPE_ARRAY:
						if (this.compareCharAtIndexWithString(i, "["))
						{
							arrayNestCount++;
						}
						else if (this.compareCharAtIndexWithString(i, "]"))
						{
							arrayNestCount--;
						}
						break;
					case BubblesJSONParser.PARSE_TYPE_OBJECT:
						if (this.compareCharAtIndexWithString(i, "{"))
						{
							objectNestCount++;
						}
						else if (this.compareCharAtIndexWithString(i, "}"))
						{
							objectNestCount--;
						}
						break;
					default:
						break;
				}
				if (
					(
						(this.currentParseType == BubblesJSONParser.PARSE_TYPE_ARRAY && arrayNestCount == 0) || 
						(this.currentParseType == BubblesJSONParser.PARSE_TYPE_OBJECT && objectNestCount == 0)
					) &&
					this.compareCharAtIndexWithString(i, "]}")
				)
				{
					this.subStringCharIndex = i;
					return this.JSONString.substring(charIndex, i+1);
				}
				else if (this.compareCharAtIndexWithString(i, "\"'"))
				{
					stringClosingQuote = (String.valueOf(this.JSONString.charAt(i))); // 4-17-13
					insideString = true;
				}
			}
			//else if (insideString == true && compareCharAtIndexWithString(i, "\"'") && !(this.compareCharAtIndexWithString(i-1, "\\")))
			else if (insideString == true && this.compareCharAtIndexWithString(i, stringClosingQuote) && !(this.compareCharAtIndexWithString(i-1, "\\")))
			{
				insideString = false;
				stringClosingQuoteIndex = i; // Necessary?
			}
		}
		return output;
	}
	
	private String parseElementFromCharIndex (int charIndex)
	{
		this.currentParseType = BubblesJSONParser.PARSE_TYPE_INT;
		int startIndex = 0;
		boolean insideString = false;
		String stringClosingQuote = "";
		int stringClosingQuoteIndex = 0;
		for (int i = charIndex; i < this.JSONString.length(); i++)
		{
			//System.out.println(this.JSONString.charAt(i)+"asd"+stringClosingQuote); //Debug
			if (insideString == false)
			{
				if (this.compareCharAtIndexWithString(i, BubblesJSONParser.CHAR_TYPE_ELEMENT_DELIMITER+BubblesJSONParser.CHAR_TYPE_CLOSING_OBJECT_DELIMITER))
				{
					for (int x = i; x >= charIndex; x--)
					{
						// Revisit. Error index pointer required?
						/*
						if (x == previousDelimiterErrorIndex && (stringClosingQuote.equals("\"") || stringClosingQuote.equals("'")))
						{
							//System.out.println("thing"+i); // Debug
							//System.out.println(this.previousDelimiterErrorIndex); // Debug
							this.subStringCharIndex = i;
							return "";
						}
						*/
						if
						(
							(this.currentParseType == BubblesJSONParser.PARSE_TYPE_STRING && stringClosingQuoteIndex == x+1) ||
							(this.currentParseType != BubblesJSONParser.PARSE_TYPE_STRING && !(this.compareCharAtIndexWithString(x, BubblesJSONParser.CHAR_TYPE_WHITESPACE+BubblesJSONParser.CHAR_TYPE_ELEMENT_DELIMITER+BubblesJSONParser.CHAR_TYPE_CLOSING_OBJECT_DELIMITER+stringClosingQuote)))
							//!(this.compareCharAtIndexWithString(x, BubblesJSONParser.CHAR_TYPE_WHITESPACE+BubblesJSONParser.CHAR_TYPE_ELEMENT_DELIMITER+BubblesJSONParser.CHAR_TYPE_CLOSING_OBJECT_DELIMITER+stringClosingQuote))
						)
						{
							//this.previousDelimiterErrorIndex = i; // Revisit
							//System.out.println(this.previousDelimiterErrorIndex); // Debug
							this.subStringCharIndex = x;
							return this.JSONString.substring(startIndex, x+1);
						}
					}
				}
				if (startIndex == 0 && !(this.compareCharAtIndexWithString(i, BubblesJSONParser.CHAR_TYPE_WHITESPACE)))
				{
					if (this.compareCharAtIndexWithString(i, BubblesJSONParser.CHAR_TYPE_OPENING_OBJECT_DELIMITER))
					{
						startIndex = i;
						if (this.compareCharAtIndexWithString(i, "["))
						{
							this.currentParseType = BubblesJSONParser.PARSE_TYPE_ARRAY;
						}
						else if (this.compareCharAtIndexWithString(i, "{"))
						{
							this.currentParseType = BubblesJSONParser.PARSE_TYPE_OBJECT;
						}
						return this.getSubObjectStringFromCharIndex(i);
					}
					else if (this.compareCharAtIndexWithString(i, "\"'"))
					{
						stringClosingQuote = (String.valueOf(this.JSONString.charAt(i)));
						insideString = true;
						startIndex = i+1;
						this.currentParseType = BubblesJSONParser.PARSE_TYPE_STRING;
						
					}
					else
					{
						startIndex = i;
					}
				}
				if (i >= startIndex && !this.compareCharAtIndexWithString(i, BubblesJSONParser.CHAR_TYPE_WHITESPACE+BubblesJSONParser.CHAR_TYPE_ELEMENT_DELIMITER+BubblesJSONParser.CHAR_TYPE_CLOSING_OBJECT_DELIMITER))
				{
					if (!this.compareCharAtIndexWithString(i, "0123456789."))
					{
						if (this.JSONString.substring(i, i+4).equals("null"))
						{
							this.currentParseType = BubblesJSONParser.PARSE_TYPE_NULL;
							i += 3;
						}
						else if (this.JSONString.substring(i, i+4).equals("true"))
						{
							this.currentParseType = BubblesJSONParser.PARSE_TYPE_BOOLEAN;
							i += 3;
						}
						else if (this.JSONString.substring(i, i+5).equals("false"))
						{
							this.currentParseType = BubblesJSONParser.PARSE_TYPE_BOOLEAN;
							i += 4;
						}
						else if (!(this.currentParseType.equals(BubblesJSONParser.PARSE_TYPE_NULL)) && !(this.currentParseType.equals(BubblesJSONParser.PARSE_TYPE_BOOLEAN)))
						{
							this.currentParseType = BubblesJSONParser.PARSE_TYPE_STRING;
						}
					}
					else if (this.compareCharAtIndexWithString(i, "."))
					{
						this.currentParseType = BubblesJSONParser.PARSE_TYPE_DOUBLE;
					}
				}
			}
			else if (insideString == true && this.compareCharAtIndexWithString(i, stringClosingQuote) && !(this.compareCharAtIndexWithString(i-1, "\\")))
			{
				insideString = false;
				stringClosingQuoteIndex = i;
				//System.out.println(stringClosingQuoteIndex); // Debug
			}
		}
		this.subStringCharIndex = this.JSONString.length();
		return "";
	}
	
	public BubblesJSONObject parseString ()
	{
		for (int i = 0; i < this.JSONString.length(); i++)
		{
			switch (this.currentParseState)
			{
				case BubblesJSONParser.PARSE_STATE_SYNTAX:
					if (this.compareCharAtIndexWithString(i, BubblesJSONParser.CHAR_TYPE_OPENING_OBJECT_DELIMITER))
					{
						this.object = new BubblesJSONObject();
						if (this.compareCharAtIndexWithString(i, "["))
						{
							this.currentDataType = BubblesJSONParser.DATA_TYPE_ARRAY;
							this.currentParseState = BubblesJSONParser.PARSE_STATE_VALUE;
						}
						else if (this.compareCharAtIndexWithString(i, "{"))
						{
							this.currentDataType = BubblesJSONParser.DATA_TYPE_OBJECT;
							this.currentParseState = BubblesJSONParser.PARSE_STATE_KEY;
						}
					}
					else if (this.compareCharAtIndexWithString(i, ":"))
					{
						this.currentParseState = BubblesJSONParser.PARSE_STATE_VALUE;
					}
					else if (this.compareCharAtIndexWithString(i, ","))
					{
						if (this.currentDataType.equals(BubblesJSONParser.DATA_TYPE_ARRAY))
						{
							this.object.add(this.value);
							this.currentParseState = BubblesJSONParser.PARSE_STATE_VALUE;
						}
						else if (this.currentDataType.equals(BubblesJSONParser.DATA_TYPE_OBJECT))
						{
							this.object.add(this.key, this.value);
							this.currentParseState = BubblesJSONParser.PARSE_STATE_KEY;
						}
					}
					else if (this.compareCharAtIndexWithString(i, BubblesJSONParser.CHAR_TYPE_CLOSING_OBJECT_DELIMITER))
					{
						if (this.currentDataType.equals(BubblesJSONParser.DATA_TYPE_ARRAY))
						{
							this.object.add(this.value);
						}
						else if (this.currentDataType.equals(BubblesJSONParser.DATA_TYPE_OBJECT))
						{
							this.object.add(this.key, this.value);
						}
						return this.object;
					}
					break;
				case BubblesJSONParser.PARSE_STATE_KEY:
					this.key = this.parseElementFromCharIndex(i);
					switch (this.currentParseType)
					{
						case BubblesJSONParser.PARSE_TYPE_INT:
							this.key = Integer.parseInt((String)this.key);
							break;
						case BubblesJSONParser.PARSE_TYPE_DOUBLE:
							this.key = Double.parseDouble((String)this.key);
							break;
						default:
							break;
					}
					this.currentParseType = BubblesJSONParser.PARSE_TYPE_OTHER;
					this.currentParseState = BubblesJSONParser.PARSE_STATE_SYNTAX;
					i = this.subStringCharIndex;
					break;
				case BubblesJSONParser.PARSE_STATE_VALUE:
					this.value = this.parseElementFromCharIndex(i);
					//System.out.println("value"+this.value); // Debug
					switch (this.currentParseType)
					{
						case BubblesJSONParser.PARSE_TYPE_NULL:
							this.value = null;
							break;
						case BubblesJSONParser.PARSE_TYPE_BOOLEAN:
							this.value = ((String)this.value).equals("true") ? true : (((String)this.value).equals("false") ? false : null);
							break;
						case BubblesJSONParser.PARSE_TYPE_INT:
							this.value = Integer.parseInt((String)this.value);
							break;
						case BubblesJSONParser.PARSE_TYPE_DOUBLE:
							this.value = Double.parseDouble((String)this.value);
							break;
						case BubblesJSONParser.PARSE_TYPE_ARRAY:
							this.subParser = new BubblesJSONParser((String)this.value);
							this.value = this.subParser.parseString();
							break;
						case BubblesJSONParser.PARSE_TYPE_OBJECT:
							this.subParser = new BubblesJSONParser((String)this.value);
							this.value = this.subParser.parseString();
							break;
						default:
							break;
					}
					this.currentParseState = BubblesJSONParser.PARSE_STATE_SYNTAX;
					i = this.subStringCharIndex;
					break;
				default:
					break;
			}
		}
		return this.object;
	}
	
	public BubblesJSONObject getObject ()
	{
		return this.object;
	}
	
}