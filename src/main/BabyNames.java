package main;

import java.util.Scanner;
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
		String input;
		boolean run = true;
		boolean keepOptions = false;
		char yesNoChar;
		Gender gender = null;
		char genderChar;
		int length = 0;
		int order = 0;
		MarkovModel m;
		String name;
		// The program can generate names repeatedly, unless the user decides to stop
		while(run)
		{
			// keepOptions determines whether to choose new options for the naame
			// or keep the same ones used for the last name.
			if(!keepOptions)
			{
				System.out.print("Enter the gender of the name you want to generate: ");
				input = s.nextLine();
				if(input.isEmpty())
					genderChar = '~';
				else
					genderChar = input.toLowerCase().charAt(0);
				while(!(genderChar == 'm' || genderChar == 'f'))
				{
					System.out.println("Sorry, that is not a valid input. Please try again.");
					System.out.print("Enter the gender of the name you want to generate: ");
					input = s.nextLine();
					if(input.isEmpty())
						genderChar = '~';
					else
						genderChar = input.toLowerCase().charAt(0);
				}
				
				if(genderChar == 'm')
					gender = Gender.MALE;
				else
					gender = Gender.FEMALE;
				
				System.out.print("Enter the maximum length of the name: ");
				input = s.nextLine();
				if(input.isEmpty())
					length = -1;
				else
					length = Integer.parseInt(input);
				while(length < 0)
				{
					System.out.println("Sorry, that is not a valid input. Please try again.");
					System.out.print("Enter the maximum length of the name: ");
					input = s.nextLine();
					if(input.length() == 0)
						length = -1;
					else
						length = Integer.parseInt(input);
				}
				
				System.out.print("Enter the order of the Markov model used to generate the names: ");
				input = s.nextLine();
				if(input.isEmpty())
					order = 0;
				else
					order = Integer.parseInt(input);
				while(order < 1)
				{
					System.out.println("Sorry, that is not a valid input. Please try again.");
					System.out.print("Enter the order of the Markov model used to generate the names: ");
					input = s.nextLine();
					if(input.isEmpty())
						order = 0;
					else
						order = Integer.parseInt(input);
				}
			}
			System.out.println("Generating name...");
			// Create a new MarkovModel and generate the name.
			// (Actually, it's pretty wasteful to make a new one each time,
			// but hey, if it works, it works.)
			m = new MarkovModel(gender, order);
			name = m.GenerateName(length);
			System.out.println("Name: " + name);
			// Ask if the user wants to generate another name
			System.out.print("The name has been generated. Would you like to generate another name? (y/n): ");
			input = s.nextLine();
			if(input.isEmpty())
				yesNoChar = '~';
			else
				yesNoChar = input.toLowerCase().charAt(0);
			while(!(yesNoChar == 'y' || yesNoChar == 'n'))
			{
				System.out.println("Sorry, that is not a valid response. Please try again.");
				System.out.print("Would you like to generate another name? (y/n): ");
				input = s.nextLine();
				if(!input.isEmpty())
					yesNoChar = input.toLowerCase().charAt(0);
				else
					yesNoChar = '~';
			}
			if(yesNoChar == 'n')
			{
				run = false;
			}
			else
			{
				// Ask if the user would like to keep the same options
				// that they used for the last name
				System.out.print("Would you like to use the same options as before? (y/n): ");
				input = s.nextLine();
				if(input.isEmpty())
					yesNoChar = '~';
				else
					yesNoChar = input.toLowerCase().charAt(0);
				while(!(yesNoChar == 'y' || yesNoChar == 'n'))
				{
					System.out.println("Sorry, that is not a valid response. Please try again.");
					System.out.print("Would you like to use the same options as before? (y/n): ");
					input = s.nextLine();
					if(input.isEmpty())
						yesNoChar = '~';
					else
						yesNoChar = input.toLowerCase().charAt(0);
				}
				if(yesNoChar == 'y') keepOptions = true;
				else keepOptions = false;
			}
		}
		// Close the scanner and display an exit message
		// when the program is done running.
		s.close();
		System.out.println("Thank you for using the name generator. Good bye!");
	}
}
