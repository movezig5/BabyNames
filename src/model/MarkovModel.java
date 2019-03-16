package model;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import enums.Gender;
import file.NameReader;
import file.NameAnalyzer;

/**********************
 * Class: MarkovModel *
 **********************
 * Description:
 * 		A class representing the finished Markov model.
 * Variables:
 * 		model:	A HashMap that maps pattern Strings to another HashMap
 * 				which in turn maps characters to the decimal probability
 * 				that they will follow the pattern string.
 * 		names:	An array of Strings representing the names read from
 * 				the names list file
 * 		order:	The order of the Markov model
 * 		rand:	A Math.Random object used in generating names
 */
public class MarkovModel {
	public HashMap<String, HashMap<Character, Float>> model;
	public String[] names;
	private int order;
	private Random rand;
	
	// Default Constructor
	public MarkovModel()
	{
		order = 0;
	}
	
	/**************
	 * Initialize *
	 **************
	 * Creates a new random number generator, initializes the list of names, populates the model,
	 * percentifies the model, and finally trims the underscores off the names in the list.
	 * Arguments:
	 * 		gender:	The enum of the gender of the names
	 * 		order:	The order of the Markov model
	 */
	public void Initialize(Gender gender, int order)
	{
		this.order = order;
		this.rand = new Random(System.currentTimeMillis());
		this.names = NameReader.Read(gender);
		this.model = NameAnalyzer.PopulateModel(names, order);
		Percentify();
		TrimNames();
	}
	
	/**************
	 * Percentify *
	 **************
	 * Takes the HashMap obtained from the NameAnalyzer class and converts the number
	 * of times each character appears to a percent probability that it will
	 * follow its corresponding pattern string.
	 */
	private void Percentify()
	{
		float currNum;
		float total;
		for(Map.Entry<String, HashMap<Character, Float>> entry : model.entrySet())
		{
			// The '~', or total quantity, will be the denominator.
			total = entry.getValue().get('~');
			entry.getValue().remove('~');
			for(Map.Entry<Character, Float> entry2 : entry.getValue().entrySet())
			{
				// The quantity of each character will be its respective numerator.
				currNum = entry2.getValue();
				model.get(entry.getKey()).put(entry2.getKey(), currNum / total);
			}
		}
	}
	
	/*************
	 * TrimNames *
	 *************
	 * Trims the leading and trailing underscores off the names in the list.
	 */
	private void TrimNames()
	{
		for(int i = 0; i < names.length; i++)
		{
			names[i] = names[i].substring(1, names[i].length() - 1);
		}
	}
	
	/****************
	 * GenerateName *
	 ****************
	 * Generates a name, checks that it's within the required minimum and maximum lengths,
	 * then makes sure it isn't already in the list. If the name fails either of these tests,
	 * the function retries until it gets a valid name.
	 * Arguments:
	 * 		minLength: The minimum length of the name
	 * 		maxLength: The maximum length of the name
	 * Return value:
	 * 		A String representing a valid generated name
	 */
	public String GenerateName(int minLength, int maxLength)
	{
		String name = "";
		if(maxLength == 0 || minLength > maxLength)
			return name;
		boolean valid = false;
		while(!valid)
		{
			name = GenerateName();
			
			if(!(name.length() < minLength || name.length() > maxLength))
				valid = true;
			
			if(valid == true)
			{
				for(String s : names)
				{
					if(name.equals(s))
					{
						valid = false;
						break;
					}
				}
			}
		}
		if(name.length() > 1)
			name = name.toUpperCase().charAt(0) + name.substring(1);
		else
			name = name.toUpperCase();
		return name;
	}
	
	/****************
	 * GenerateName *
	 ****************
	 * Generates a name randomly based on the Markov model.
	 * Return value:
	 * 		A randomly generated name
	 */
	private String GenerateName()
	{
		String name = "";
		String currString = "_";
		char nextChar = GetNextChar(currString);
		while(nextChar != '_')
		{
			name += nextChar;
			currString += nextChar;
			if(currString.length() > order)
				currString = currString.substring(currString.length() - order);
			nextChar = GetNextChar(currString);
		}
		return name;
	}
	
	public void ReSeedRNG()
	{
		rand = new Random(System.currentTimeMillis());
	}
	
	/***************
	 * GetNextChar *
	 ***************
	 * Using the Markov model and a random number generator, determines
	 * which character should follow the given pattern string.
	 * 
	 * The function first generates a random float from 0.0 to 1.0,
	 * then compares it with a percent probability. The probability
	 * is generated by sequentially adding the probabilities of each
	 * character; the end result is equivalent to "0.0 to 0.1 is 'a',
	 * 0.1 to 0.25 is 'b', etc."
	 * Arguments:
	 * 		s:		The pattern string
	 * 		rand:	A Math.Random object
	 * Return value:
	 * 		The next char in the name given the pattern
	 */
	private char GetNextChar(String s)
	{
		float RNG = rand.nextFloat();
		// The percent starts at zero
		float currPercent = 0.0f;
		// Make sure the model contains the key in the first place
		if(model.containsKey(s))
		{
			// Get the appropriate hash map for the pattern string 
			HashMap<Character, Float> map = model.get(s);
			for(Map.Entry<Character, Float> entry : map.entrySet())
			{
				// Ignore the "total" key, '~'
				if(entry.getKey() != '~')
				{
					// Add the probability of the current char
					// to the total, then compare the RNG value to it.
					currPercent += entry.getValue();
					if(RNG < currPercent)
					{
						// If the RNG value is in the appropriate range,
						// return the character
						return entry.getKey();
					}
				}
			}
		}
		// If the pattern string isn't in the model,
		// return the "end of name" character.
		return '_';
	}
}
