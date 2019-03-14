package file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import enums.Gender;

/*********************
 * Class: NameReader *
 *********************
 * Description:	A static class that reads the list of names from a file.
 * Variables:	None
 */
public class NameReader {
	/********
	 * Read *
	 ********
	 * Reads in the baby names from one of the files and puts them in
	 * an array of Strings, then returns the array.
	 * Arguments:
	 * 		gender: The gender of the names to read, represented as an enum
	 * Return values:
	 * 		An array of Strings containing the names. Note that all the strings
	 * 		have an underscore added to the beginning and the end to serve as markers.
	 */
	public static String[] Read(Gender gender)
	{
		String[] results = new String[1000]; // Each list contains 1000 entries
		String filename = "";
		
		// The file to read from is determined by the gender enum
		if(gender == Gender.MALE)
		{
			filename = "namesBoys.txt";
		}
		else
		{
			filename = "namesGirls.txt";
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			// Read in the names one at a time
			for(int i = 0; i < results.length; i++)
			{
				try {
					results[i] = "_" + br.readLine().toLowerCase() + "_";
				} catch(IOException ex) {
					System.err.println("Error while reading from file \"" + filename + "\" at line " + i + ".");
				}	
			}
			br.close();
		} catch(Exception ex) {
			System.err.println("The file \"" + filename + "\" could not be found.");
		}
		// Return the array of names
		return results;
	}
}
