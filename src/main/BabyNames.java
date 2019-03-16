package main;

import java.util.Scanner;
import java.util.LinkedList;
import enums.Gender;
import model.MarkovModel;

/********************
 * Class: BabyNames *
 ********************
 * The class containing the main function. Handles user input and calls
 * the appropriate functions for generating a name.
 */
public class BabyNames {
	// Main function
	public static void main(String[] args)
	{
		// Scanner and variables to keep track of user input
		Scanner s = new Scanner(System.in);
		boolean run = true;
		boolean keepOptions = false;
		Gender gender = null;
		int minLength = 0;
		int maxLength = 0;
		int order = 0;
		int numNames = 0;
		MarkovModel m = new MarkovModel();
		LinkedList<String> names = new LinkedList<String>();
		
		System.out.println("***********************\n* Baby Name Generator *\n***********************");
		System.out.println("Welcome to the baby name generator!\nMade by Daniel Ziegler for CSC 480");
		
		// The program can generate names repeatedly, unless the user decides to stop
		while(run)
		{
			// keepOptions determines whether to choose new options for the name
			// or keep the same ones used for the last name.
			if(!keepOptions)
			{
				gender = GetGender(s, "Enter the gender of the names you want to generate: ");
				minLength = GetIntInput(s, "Enter the minimum length for each name: ");
				maxLength = GetIntInput(s, "Enter the maximum length for each name: ");
				order = GetIntInput(s, "Enter the order of the Markov model used to generate the names: ");
				numNames = GetIntInput(s, "How many names do you want to generate? ");
				m.Initialize(gender, order);
			}
			else
			{
				m.ReSeedRNG();
			}
			
			// Generate the names
			System.out.println("Generating names...");
			names.clear();
			for(int i = 0; i < numNames; i++)
				names.add(m.GenerateName(minLength, maxLength));
			
			// Print out the names
			System.out.println("Done!\nThe following names have been generated:");
			for(String n : names)
				System.out.println(n);
			
			// Ask if the user wants to generate another name
			run = GetYesNoInput(s, "The generator has finished running. Would you like to generate more names? (y/n): ");
			
			if(run)
			{
				// Ask if the user would like to keep the same options that they used for the last name
				keepOptions = GetYesNoInput(s, "Would you like to use the same options as before? (y/n): ");
			}
		}
		// Close the scanner and display an exit message when the program is done running.
		s.close();
		System.out.println("Thank you for using the name generator. Good bye!");
	}
	
	/*************
	 * GetGender *
	 *************
	 * Prompts the user to input the desired gender, then returns the corresponding gender enum.
	 * If the input is not valid, prompts the user again. Words beginning with "m" or "b" are considered male,
	 * while words beginning with "f", "g", or "w" are considered female. Accepts upper- and lower-case words.
	 * Arguments:
	 * 		s:			The scanner the program is using
	 * 		message:	The message used to prompt the user for input
	 * Return value:
	 * 		A gender enum corresponding to the user's input
	 */
	private static Gender GetGender(Scanner s, String message)
	{
		String input;
		char genderChar;
		System.out.print(message);
		input = s.nextLine();
		if(input.isEmpty())
			genderChar = '~';
		else
			genderChar = input.toLowerCase().charAt(0);
		while(!(genderChar == 'm' || genderChar == 'f' || genderChar == 'b' || genderChar == 'g' || genderChar == 'w'))
		{
			System.out.println("Sorry, that is not a valid input. Please try again.");
			System.out.print(message);
			input = s.nextLine();
			if(input.isEmpty())
				genderChar = '~';
			else
				genderChar = input.toLowerCase().charAt(0);
		}
		
		if(genderChar == 'm' || genderChar == 'b')
			return Gender.MALE;
		else
			return Gender.FEMALE;
	}
	
	/*****************
	 * GetYesNoInput *
	 *****************
	 * Prompts the user to answer yes or no, then returns true for yes or false for no.
	 * If the input is invalid, keeps trying until the input is valid. Words beginning
	 * with "y" or "t" are considered "yes," and words beginning with "n" or "f" are considered "no."
	 * Accepts upper- and lower-case input.
	 * Arguments:
	 * 		s:			The scanner the program is using
	 * 		message:	The message used to prompt the user for input
	 * Return value:
	 * 		A boolean corresponding to the user's input
	 */
	private static boolean GetYesNoInput(Scanner s, String message)
	{
		String input;
		char yesNoChar;
		System.out.print(message);
		input = s.nextLine();
		if(input.isEmpty())
			yesNoChar = '~';
		else
			yesNoChar = input.toLowerCase().charAt(0);
		while(!(yesNoChar == 'y' || yesNoChar == 'n' || yesNoChar == 't' || yesNoChar == 'f'))
		{
			System.out.println("Sorry, that is not a valid response. Please try again.");
			System.out.print(message);
			input = s.nextLine();
			if(input.isEmpty())
				yesNoChar = '~';
			else
				yesNoChar = input.toLowerCase().charAt(0);
		}
		if(yesNoChar == 'y' || yesNoChar == 't')
			return true;
		else
			return false;
	}
	
	/***************
	 * GetIntInput *
	 ***************
	 * Prompts the user to enter an integer and returns the value. Does not accept
	 * negative numbers. If the input is invalid, keeps trying until the input is valid.
	 * Arguments:
	 * 		s:			The scanner the program is using
	 * 		message:	The message used to prompt the user for input
	 * Return value:
	 * 		The integer value of the string entered by the user
	 */
	private static int GetIntInput(Scanner s, String message)
	{
		String input;
		int result;
		System.out.print(message);
		input = s.nextLine();
		if(input.isEmpty())
		{
			result = 0;
		}
		else
		{
			try {
				result = Integer.parseInt(input);
			} catch(Exception e) {
				result = 0;
			}
		}
		
		while(result < 1)
		{
			System.out.println("Sorry, that is not a valid input. Please try again.");
			System.out.print(message);
			input = s.nextLine();
			if(input.isEmpty())
			{
				result = -1;
			}
			else
			{
				try {
					result = Integer.parseInt(input);
				} catch(Exception e) {
					result = -1;
				}
			}
		}
		return result;
	}
}
