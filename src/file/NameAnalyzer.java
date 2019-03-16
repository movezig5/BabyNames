package file;

import java.util.HashMap;

/***********************
 * Class: NameAnalyzer *
 ***********************
 * Description:
 * 		A static class that iterates over a list of names (given as an array of Strings)
 * 		and counts the number of times specific characters follow others.
 * Variables:
 * 		gender:	A gender enum
 * 		order:	The order of the Markov model. This is needed to determine
 * 				the length of the string to get the counts for.
 */
public class NameAnalyzer {
	/*****************
	 * PopulateModel *
	 *****************
	 * First uses the name reader to get the list of names, then
	 * iterates over the list to create a HashMap. The HashMap
	 * maps a String to another HashMap, which maps characters to floats.
	 * The String is a sequence of characters that appears in at least one
	 * of the names. Each character is a character that follows the String,
	 * and the float is the number of times that character follows that String.
	 * 
	 * The reason I used a float instead of an int is because I need use it
	 * in a division later and don't want to have to convert it later, and because
	 * a float is the same size as an int.
	 * 
	 * Arguments:
	 * 		names:	An array of Strings read in from one of the name list files
	 * 		order:	The order of the desired Markov model
	 * Return value:
	 * 		The completed HashMap
	 */
	public static HashMap<String, HashMap<Character, Float>> PopulateModel(String names[], int order)
	{
		HashMap<String, HashMap<Character, Float>> counts =
				new HashMap<String, HashMap<Character, Float>>();
		String curr;
		char next;
		// Iterate over the names
		for(String name : names)
		{
			// First get the patterns at the beginning of the string
			// that are smaller than the order of the model. This is
			// necessary in order to start building the model from an
			// empty string.
			for(int i = 1; i < order; i++)
			{
				if(i < name.length())
				{
					curr = name.substring(0, i);
					next = name.charAt(i);
					AddCount(curr, next, counts);
				}
			}
			// Next, get the patterns with length equal to
			// the order of the model.
			for(int j = 0; j < name.length() - order - 1; j++)
			{
				curr = name.substring(j, j + order);
				next = name.charAt(j + order);
				AddCount(curr, next, counts);
			}
		}
		return counts;
	}
	
	/************
	 * AddCount *
	 ************
	 * Increments the value of the given char following the given
	 * pattern string; if either one isn't in the appropriate HashMap,
	 * the function adds it.
	 * 
	 * Arguments:
	 * 		curr:	The pattern String
	 * 		next:	The character that follows the pattern String
	 * 		counts:	The HashMap thus far 
	 */
	public static void AddCount(String curr, char next, HashMap<String, HashMap<Character, Float>> counts)
	{
		float val;
		// Check if counts contains the pattern string as a key.
		if(counts.containsKey(curr))
		{
			// If it does, check if the value contains the character as a key.
			if(counts.get(curr).containsKey(next))
			{
				// If so, increment the value associated with that character.
				val = counts.get(curr).get(next);
				counts.get(curr).put(next, val + 1.0f);
			}
			else
			{
				// If not, add the character key and set the value to 1.
				counts.get(curr).put(next, 1.0f);
			}
			// The '~' key represents the total counts of ALL the
			// character keys in the current sub-HashMap. I chose
			// '~' because I felt it was unlikely to be in any names.
			// I increment it here.
			val = counts.get(curr).get('~');
			counts.get(curr).put('~', val + 1);
		}
		else
		{
			// If the pattern string doesn't appear, add it and add the
			// character key to the sub-HashMap with the value 1. Also
			// add the '~' key, representing the total counts of all characters,
			// to the sub-HashMap with the value 1.
			counts.put(curr, new HashMap<Character, Float>());
			counts.get(curr).put(next, 1.0f);
			counts.get(curr).put('~', 1.0f);
		}
	}
}
