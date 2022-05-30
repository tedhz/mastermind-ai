/*
Project Title: Codebreaker
Project Managers: Ted Zhang, Darren Yiu
Completed Date: October 23rd
*/

package codebreaker;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Codebreaker {

	/**
	 * Main method of the program. Welcomes user and lets the user know when the game has been terminated.
	 * Method also navigates user to the menu.
	 * 
	 * @param args <type String[]>
	 * @throws IOException
	 * @return void
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to Mastermind!");
		menu(); //Navigates user to the menu
		System.out.println("Game terminated");
	}
	
	/**
	 * This is the hub for all of the user's options in the game. The user can navigate through different
	 * gamemodes and difficulties from here. 
	 * 
	 * List of Local Variables:
	 * userInput- the user's response to a UFP <type String>
	 * sc- A scanner object used primarily for keyboard input <type Scanner>
	 * 
	 * @throws IOException
	 * @return void
	 */
	public static void menu() throws IOException {
		String userInput;
		Scanner sc = new Scanner(System.in);
		System.out.println("Would you like to play against AI or against someone? Enter PVP for player vs. player and AI for AI. Enter quit to exit.");
		userInput = sc.nextLine();
		
		while(!userInput.equalsIgnoreCase("quit")) { //loops until user enters quit
			if(userInput.equals("PVP")) {
				playerVPlayer(); //Navigates user to Player Vs. Player feature if chosen
			}
			else if (userInput.equals("AI")) {
				playerVsAI(); //Navigates player to AI menu if chosen
			}
			else {
				System.out.println("Invalid response, please try again.");
			}
			
			System.out.print("Would you like to play against AI or against someone? Enter PVP for player vs. player and AI for AI. Enter quit to exit.");
			userInput = sc.nextLine();
		}
	}
	
	/**
	 * This method is where the user is able to play against another player locally on the same computer.
	 * One player decides to be the codemaster and comes up with a code and the other will try to guess it 
	 * based on the codemaster's peg input. The game is won if the codemaster enters 'BBBB'. Each user is 
	 * prompted by the program to enter their guess and pegs. The codebreaker has a maximum of 10 guesses
	 * before they lose. 
	 * 
	 * List of Local Variables:
	 * sc- a Scanner object used primarily for keyboard input <type Scanner>
	 * call- a Codebreaker object used to call non-static methods <type Codebreaker>
	 * allCombinations- an array used to store all possible codes in the game <type String[]>
	 * allPegs- an array used to store all possible pegs in the game <type String[]>
	 * correct- a boolean used to verify if the codebreaker got the code correct <type boolean>
	 * verify- a boolean used to verify the validity of user input <type boolean>
	 * verify2- a second boolean used to verify validity of user input <type boolean>
	 * guess- used to store the codebreaker's current guess <type String>
	 * pegs- used to store the codemaster's current pegs <type String>
	 * count- used to count the number of times a loop has iterated <type int>
	 * 
	 * @throws IOException
	 * @return void
	 */
	public static void playerVPlayer() throws IOException {
		Scanner sc = new Scanner(System.in);
		Codebreaker call = new Codebreaker();
		String[] allCombinations = new String[1296]; //1296 is the number of possible codes in mastermind
		String[] allPegs = new String[15]; //15 is the number of possible peg combinations 
		call.filereader(allCombinations, "./resources/allCombinations.txt"); //fills allCombinations array with all 1296 possible codes
		call.filereader(allPegs, "./resources/allPegs.txt"); //files allPegs with all possible peg combinations
		boolean correct = false;
		boolean verify = false;
		boolean verify2 = false;
		String guess = "";
		String pegs = "";
		int count = 0;
		clearHistory(); //clears history of pegs and guesses
		
		System.out.println("Choose a codebreaker and a codemaster between yourselves.");
		System.out.println("Codemaster, think of a 4 colour code using the options Green, Red, Blue, Yellow, Orange and Purple.");
		while(!correct && count < 10) {
			verify = false;
			verify2 = false;
			printHistory(); //asks user if they want to display the history of pegs and guesses
			System.out.println("Codebreaker, enter your guess in the format '1122' 1. Green 2. Red 3. Blue 4. Yellow 5. Orange 6. Purple.");
			
			while(!verify) { //verifies that the user's guess is valid
				guess = sc.nextLine();
				if(verifyGuess(guess, allCombinations)) {
					verify = true;
				}
				else {
					System.out.println("Invalid guess, please try again.");
				}
			}
			filewriter(convertToColours(guess)); //writes the guess to history.txt
			
			while(!verify2) { //verifies that the user's pegs are valid
				System.out.println("The codebreaker's guess is " + convertToColours(guess));
				System.out.println("Enter the pegs in the format 'BBWW' (Make sure black pegs come before white pegs)");
				pegs = sc.nextLine();
				
				if(verifyPegs(allPegs, pegs)) {
					verify2 = true;
				}
				else {
					System.out.println("Invalid pegs. Please try again.");
				}
			}
			filewriter(pegs); //writes the pegs to history.txt
			
			System.out.println("The pegs from the codemaster are " + pegs);
			if(pegs.equals("BBBB")) { //checks to see if the codebreaker has won
				correct = true;
			}
			count++;
		}
		
		if(correct) { //Lets user know who has won the game once it is finished
			System.out.println("The codebreaker has won!");
		}
		else {
			System.out.println("The codemaster has won!");
		}
	}
	
	/**
	 * This method is another hub for the user to choose an option when playing against the AI.
	 * The player can either choose to be the codebreaker or codemaster and if they choose
	 * codemaster, they can choose whether or not they want an easy difficulty or a hard difficulty.
	 * 
	 * List of Local Variables:
	 * sc- a Scanner object primarily used for keyboard input. <type Scanner>
	 * level- used to indicate the difficulty the user has chosen if they choose codemaster <type int>
	 * userInput- used to store the user's reponse to a UFP <type String>
	 * 
	 * @throws IOException
	 * @return void
	 */
	public static void playerVsAI() throws IOException {
		Scanner sc = new Scanner(System.in);
		int level;
		String userInput;
		
		System.out.println("Would you like to be the codemaster or the codebreaker? Enter exit to go back");
		userInput = sc.nextLine();
		while(!userInput.equalsIgnoreCase("exit")){ //loops until user enters exit
			if(userInput.equals("codemaster")) {
				System.out.println("Would you like easy mode or hard mode?");
				userInput = sc.nextLine();
				
				//user is able to choose the difficulty and the difficulty is passed to the codemaster method
				if(userInput.equals("easy")) {
					level = 0;
					codemaster(level);
				}
				else if(userInput.equals("hard")) {
					level = 1;
					codemaster(level);
				}
				else {
					System.out.println("Invalid response, please try again.");
				}
			}
			else if(userInput.equals("codebreaker")) { //User is sent to codebreaker method if codebreaker is chosen
				codebreaker();
			}
			else {
				System.out.print("Invalid response, please try again.");
			}
			
			System.out.println("Would you like to be the codemaster or the codebreaker? Enter exit to go back");
			userInput = sc.nextLine();
		}
	}
	
	/**
	 * This is the part of the program where the user is able to play as a codemaster. The user is required to 
	 * think of a 4 colour code using the colours indicated and input that code in numbers. The AI then attempts to 
	 * guess said code and is provided feedback from the user in the form of pegs for a maximum of 10 guesses
	 * per game. If the AI guesses the code, the AI wins, but if the AI does not guess it right, the user wins. The
	 * AI is give a maximum of 10 guesses to guess the code.
	 * 
	 * List of Local Variables:
	 * sc- a Scanner object used primarily for keyboard input <type Scanner>
	 * call- a Codebreaker object used to call non-static methods <type Codebreaker>
	 * allCombinations- an array used to store all possible codes in the game <type String[]>
	 * allPegs- an array used to store all possible combinations of pegs in the game <type String[]>
	 * correct- boolean used to check if the AI has guessed the correct code <type boolean>
	 * verify- boolean used to verify the validity of user input <type boolean>
	 * duped- boolean used to check if the user has entered a combination of pegs that is not possible <type boolean>
	 * count- used to count the number of times a loop has iterated <type int>
	 * guess- used to store the AI's current guess <type String>
	 * pegs- used to store the user's current input of pegs <type String>
	 * 
	 * @param level- indicates the difficulty the user chose in the playerVsAI method <type int>
	 * @throws IOException
	 */
	public static void codemaster(int level) throws IOException {
		Scanner sc = new Scanner(System.in);
		Codebreaker call = new Codebreaker();
		String[] allCombinations = new String[1296]; //1296 is the number of codes possible in mastermind
		String[] allPegs = new String[15]; //15 is the number of pegs possible
		call.filereader(allCombinations, "./resources/allCombinations.txt"); //fills allCombinations with all possible codes 
		call.filereader(allPegs, "./resources/allPegs.txt"); //fills allPegs with all
		boolean correct = false;
		boolean verify;
		boolean duped = false;
		int count = 0;
		String guess = "";
		String pegs = "";
		clearHistory(); //clears the history of pegs and gueses from history.txt
		
		System.out.println("Think of a 4 colour code using the options Green, Red, Blue, Yellow, Orange, and Purple.");
		while(!correct && count < 10 && !duped) {
			printHistory(); //asks user if they want to print the history of pegs and guesses and prints them if they agree
			verify = false;
			if(count == 0) { //Sets an initial guess of 1122
				guess = "1122";
			}
			filewriter(convertToColours(guess)); //writes the current guess to history
			
			while(!verify) { //verifies that the user's pegs are valid
				System.out.println("The AI's guess is " + convertToColours(guess));
				System.out.println("Enter the pegs in the format BBWW (Make sure black pegs are first)");
				pegs = sc.nextLine();
				if(verifyPegs(allPegs, pegs)) {
					verify = true;
				}
				else {
					System.out.println("Invalid pegs. Please try again");
				}
			}
			filewriter(pegs); //writes the user's pegs to history.txt
			
			if(pegs.equals("BBBB")) { //checks to see if the AI has won the game
				correct = true;
			}
			
			if(level == 0) { //if the user chose easy, the guessed code is random
				guess = randomGuess();
			}
			else if(level == 1) { //if the user chose hard, an elimination technique is used to get the next guess
				guess = getGuess(pegs, allCombinations, guess);
				if(guess.equals("")) { //checks to see if there is no possible code for the user's combinations of pegs
					duped = true;
				}
			}
			count++;
		}
		
		if(correct) { //Let's the user know who has won and whether or not they have entered a non-possible combination of pegs
			System.out.println("The AI has won!");
		}
		else if(duped) {
			System.out.println("You've duped us! No code exists with that combination of pegs.");
		}
		else if(count == 10) {
			System.out.println("The AI has lost");
		}
	}
	
	/**
	 * This method is where the user is able to guess a randomly generated code by the AI.
	 * The user is given pegs in order to assist them in guessing and is given a maximum of 10 guesses
	 * before they lose. If the user is able to guess the AI's code within 10 guesses, the user wins. If not
	 * the AI wins. 
	 * 
	 * List of Local Variables:
	 * guess- used to store the user's current guess <type String>
	 * code- used to store the AI's randomly generated code <type String>
	 * correct- boolean used to verify whether or not the user's code is correct <type boolean>
	 * count- used to count the iterations of a loop <type int>
	 * 
	 * @throws IOException
	 * @return void
	 */
	public static void codebreaker() throws IOException { 
		String guess = "", code;
		String bw;
		boolean correct = false;
		int count = 0;
		code = randomGuess(); //The AI sets a random guess as its code
		clearHistory();
		
		while(count<10 && !correct) {
			System.out.println("Guess "+(count+1)+" of 10.");
			printHistory(); //Asks the user whether or not they want to display the history of guesses and pegs and displays it if they do
			
			guess=assignGuess(); //Asks the user to make a guess at the code the AI has come up with
			filewriter(convertToColours(guess)); //writes the user's current guess to history.txt
			bw = assignPegs(code, guess); //Assigns pegs to the user's guess
			filewriter(bw); //Writes the AI's pegs to history.txt
			System.out.println(bw);
			
			if(bw.equals("BBBB")) { //Checks to see if the user has won the game
				System.out.println("Congratulations! You guessed the correct code!");
				correct = true;
			}
			count++;
		}
		
		if(!correct) { //If the user has not won, let's the user know that they have lost
			System.out.println("Sorry about that, you've lost.");
		}
	}
	
	/**
	 * This method reads a .txt file line by line into a String array.
	 * 
	 * List of Local Variables:
	 * str- used to store the current line in the file <type String>
	 * in- BufferedReader object used to read files <type BufferedReader>
	 * list- ArrayList object used to store each line which is then transferred to an array. <type ArrayList>
	 * 
	 * @param array- the array in which the file will be read to line by line <type String[]>
	 * @param filename- the name of the file the method will read from <type String>
	 * @throws IOException
	 */
	public void filereader(String[] array, String filename) throws IOException{
		String str;
		BufferedReader in = new BufferedReader (new FileReader(filename));
		ArrayList<String> list = new ArrayList <String>();
		
		while((str = in.readLine()) != null) {
			list.add(str); //Adds each line in the file to an ArrayList
		}
		
		for(int i = 0; i < array.length; i++) { //Converts the ArrayList into a usable array
			array[i] = list.get(i);
		}
	}
	
	/**
	 * This method will take a String and write said String onto the history.txt file in resources. 
	 * 
	 * List of Local Variables:
	 * fout- a File object used to define the file that is to be used <type File>
	 * fw- a FileWrtier object used to define how the file will be written to <type FileWriter>
	 * bw- a BufferedWriter object used primarily to write to the file <type BufferedWriter>
	 * 
	 * @param line- the String passed into the method that will be written to the file <type String>
	 * @throws IOException
	 * @return void
	 */
	public static void filewriter(String line) throws IOException {
		File fout = new File("./resources/history.txt");
		
		FileWriter fw = new FileWriter(fout.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(line); //Writes the given String into history.txt
		bw.newLine(); //goes to the next line in the file
		bw.close(); //closes the file
	}
	
	/**
	 * This method clears the file history.txt of its contents
	 * 
	 * List of Local Variables:
	 * pw- a PrintWriter object used to write to the history.txt file <type PrintWriter>
	 * 
	 * @throws IOException
	 * @return void
	 */
	public static void clearHistory() throws IOException{
		PrintWriter pw = new PrintWriter("./resources/history.txt");
		pw.print(""); //clears history.txt
		pw.close(); //closes the file
	}
	
	/**
	 * This method is used to ask the user if they want to see the history of pegs and guesses and,
	 * if they so choose, print said history to the console. 
	 * 
	 * List of Local Variables:
	 * sc- a Scanner object used to obtain keyboard input from the console <type Scanner>
	 * str- a String used to store the current line in the file being read <type String>
	 * in- a BufferedReader object used primarily to read from a file <type BufferedReader>
	 * total- a String used to string all the words together. 
	 * 
	 * @throws IOException
	 * @return void
	 */
	public static String printHistory() throws IOException{
		Scanner sc = new Scanner(System.in);
		String total = "";
		System.out.println("Enter 'print' to view history of guesses and pegs. Hit enter to continue without printing.");
		if(sc.nextLine().equals("print")) { //If user wants to see the history of the pegs, it prints to console
			String str;
			BufferedReader in = new BufferedReader(new FileReader("./resources/history.txt"));
			while((str = in.readLine()) != null) {
				total = total.concat(" "+str);
				System.out.println(str); //Prints each line in the history file to console
			}
		}
		return total;
	}
	
	/**
	 * This method takes a String of numerical digits used to represent colours and
	 * converts them into readable text. 
	 * 
	 * List of Local Variables:
	 * guessColour- used to store the guess as readable text <type String>
	 * 
	 * @param guess- the String of numerical digits used to represent colours <type String>
	 * @return guessColour- the final String of readable colours <type String>
	 */
	public static String convertToColours(String guess) {
		String guessColour = "";
		for(int i = 0; i < guess.length(); i++) { //concatenates each colour based on its digit equivalent 
			if(guess.charAt(i) == ('1')) {
				guessColour = guessColour.concat("Green ");
			}
			else if(guess.charAt(i) == ('2')) {
				guessColour = guessColour.concat("Red ");
			}
			else if(guess.charAt(i) == ('3')) {
				guessColour = guessColour.concat("Blue ");
			}
			else if(guess.charAt(i) == ('4')) {
				guessColour = guessColour.concat("Yellow ");
			}
			else if(guess.charAt(i) == ('5')) {
				guessColour = guessColour.concat("Orange ");
			}
			else if(guess.charAt(i) == ('6')) {
				guessColour = guessColour.concat("Purple ");
			}
		}
		return guessColour;
		
		/*
		 * TEST CASE:
		 * 
		 * Input:
		 * "1326"
		 * Ouput:
		 * "Green Blue Red Purple "
		 */
	}
	
	/**
	 * This method verifies whether or not the user's entered pegs are consistent
	 * with what they were instructed to give and whether or not they are valid. 
	 * 
	 * @param allPegs- an array containing all possible combinations of pegs <type String[]>
	 * @param pegs- the pegs that the user has entered into the program <type String>
	 * @return true- program returns true if the pegs are in fact valid <type boolean>
	 * @return false- program returns false if the pegs are found to be invalid <type boolean>
	 */
	public static boolean verifyPegs(String[] allPegs, String pegs) {
		for(int i = 0; i<allPegs.length; i++) { //verifies that the pegs are contained in the allPegs method
			if(pegs.equals(allPegs[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method calls other methods to generate a guess using data the user has entered
	 * when they are playing as the codemaster against the AI. 
	 * 
	 * @param pegs- the current pegs the user entered <type String>
	 * @param allCombinations- an array containing all possible remaining combinations of codes <type String[]>
	 * @param guess- the programs previous guess for the code <type String>
	 * @param allPegs- an array containing all possible combinations of pegs <type String[]>
	 * @return the guess that the program has generated using the choose() method. 
	 */
	public static String getGuess(String pegs, String[] allCombinations, String guess) {
		eliminate(allCombinations, pegs, guess); //eliminates all non-possible codes 
		return choose(allCombinations); //chooses one of the remaining possible pegs to be the AI's guess
	}
	
	/**
	 * This method eliminates all combinations that are no longer possible based on the AI's previous guess
	 * and the pegs the user entered for said guess.
	 * 
	 * @param allCombinations- an array containing all remaining possible combinations of codes <type String[]>
	 * @param pegs- the user's current entered pegs in reponse to the previous guess <type String>
	 * @param guess- the AI's previous guess <type String>
	 */
	public static void eliminate(String[] allCombinations, String pegs, String guess) {
		for(int i = 0; i<allCombinations.length; i++) { //iterates through remaining codes in the array
			if(allCombinations[i] != "-1") { //skips over ones that have already been eliminated
				if(!(assignPegs(guess, allCombinations[i])).equals(pegs)) { //eliminates codes if it does not return the same pegs as the user, 
																			//if the current guess was to be the code
					allCombinations[i] = "-1"; 
				}
				if(allCombinations[i].equals(guess)) { //eliminates the current guess from the array
					allCombinations[i] = "-1";
				}
			}
		}
	}
	
	/**
	 * This method chooses the first code in the allCombinations array that isn't eliminated.
	 * 
	 * @param allCombinations- an array containing all remaining possible combinations of codes <type String[]>
	 * @return allCombinations[i]- the method returns the first non-eliminated code in the array <type String> 
	 * @return ""- the method returns "" if all codes in the array are eliminated <type String>
	 */
	public static String choose(String[] allCombinations) {
		for (int i = 0; i < allCombinations.length; i++) { //iterates through remaining codes in the array
			if(!allCombinations[i].equals("-1")) { //skips over all eliminated codes
				return allCombinations[i]; //returns the first non-eliminated code
			}
		}
		return ""; //returns "" if all codes have been eliminated
	}
	
	/**
	 * This method returns a random 4 digit code using the 6 colours possible. It uses Math.random
	 * in order to generate the appropriate code.
	 * 
	 * List of Local Variables:
	 * digit- used to store a digit from 1-6 randomly generated by Math.random() <type int>
	 * digitString- used to store a String version of the variable digit <type String>
	 * guessString- used to store the randomly generated code <type String>
	 * 
	 * @return guessString- the random 4 digit code generated by the method <type String>
	 */
	public static String randomGuess() {
		int digit;
		String digitString;
		String guessString = "";
		
		for(int i = 0; i < 4; i++) { 
			digit = (int) Math.round((Math.random()*5)+1); //generates a random number from 1-6
			digitString = digit + ""; //converts the randomly generated number into a String
			guessString = guessString.concat(digitString); //adds the number to the code
		}
		
		return guessString;
	}
	
	/**
	 * This method gets a guess from the user, verifies it's validity, and sends it back to the calling
	 * method when extracted and verified.
	 * 
	 * List of Local Variables:
	 * guess- used to store the guess the user enters into the console <type String>
	 * call- a Codebreaker object primarily used to call non-static methods <type Codebreaker>
	 * sc- a Scanner object used to read keyboard input <type Scanner>
	 * verify- a boolean used to verify whether or not an entered guess is valid <type boolean>
	 * 
	 * @throws IOException
	 * @return guess- the method returns the user's entered guess once verified <type String>
	 */
	public static String assignGuess() throws IOException { 
		String guess = "";
		Codebreaker call = new Codebreaker();
		String[] allCombinations = new String[1296]; //1296 is the number of possible codes in mastermind
		call.filereader(allCombinations, "./resources/allCombinations.txt"); //fills the array with all possible codes
		Scanner sc = new Scanner(System.in);
		boolean verify = false;
		
		System.out.println("What colours do you want to guess? Enter your guess in the format '1122' 1. Green 2. Red 3. Blue 4. Yellow 5. Orange 6. Purple.");
		while(!verify) { //verifies if the entered guess is valid
			guess=sc.nextLine();//gets a guess from the user
			if(verifyGuess(guess, allCombinations)) {
				verify = true;
			}
			else {
				System.out.println("Invalid guess, please try again.");
			}
		}
		return guess;
	}
	
	/**
	 * This method is where the AI determines the black and white pegs associated with the user's guess in comparison to 
	 * the randomly generated colour code by the computer. The code and guess are sent into the method by the parameters
	 * and the method returns black and white pegs in the format "BBWW".
	 * 
	 * List of Local Variables:
	 * tempcode - the code is changed to prevent the same peg creating more than one black/white peg <type StringBuilder>
	 * tempguess - guess is changed preventing more than one black/white peg to come from the same peg <type StringBuilder>
	 * compare - assigned a value based on the comparison of tempcode and tempguess lexicographically <type int>
	 * 
	 * @return bw - returns the black and white pegs in format "BBWW" <type String>
	 * @param code - the randomly generated 4 colour code by the computer <type int>
	 * @param guess - the code that was guessed by the user <type int>
	 */
	public static String assignPegs(String code, String guess) {
		String bw = "";
		StringBuilder tempcode = new StringBuilder(code);
		StringBuilder tempguess = new StringBuilder(guess);
		int compare;
		
		for(int i = 0; i < 4; i++) { //looking for black pegs
			compare = Character.compare(tempcode.charAt(i),tempguess.charAt(i));
			if(compare == 0) {
				bw=bw.concat("B");
				tempcode.setCharAt(i,'X'); //when a black peg is found, the value at the guess and code is removed to prevent it from being compared again
				tempguess.setCharAt(i, 'X');
			}
		}
		
		for(int i = 0; i < 4; i++) { //looking for white pegs
			for(int j = 0; j < 4; j++) {
				compare = Character.compare(tempcode.charAt(i),tempguess.charAt(j));
				if(compare == 0 && i != j && tempcode.charAt(i) != 88) { //makes sure that the character is also not 'X'
					bw = bw.concat("W");
					tempguess.setCharAt(j,'X');
					break;
				}
			}
		}
		return bw;
		
		/*
		 * TEST CASES:
		 * 
		 * Input: "1122", "3456"
		 * Ouput: ""
		 * 
		 * Input "1356", "1356"
		 * Ouput: "BBBB"
		 * 
		 * Input: "1453", 3452
		 * Output: "BBW"
		 * 
		 * Input: "1122", "3311"
		 * Output: WW
		 * 
		 */
	}
	
	/**
	 * This method verifies whether or not a guess entered by a user is valid
	 * 
	 * @param guess- the user's entered guess into the program <type String>
	 * @param allCombinations- an array containing all possible combinations of codes <type String[]>
	 * @return true- method returns true if the entered guess is valid <type boolean>
	 * @return false- method returns false if the entered guess is invalid <type boolean>
	 */
	public static boolean verifyGuess(String guess, String[] allCombinations) {
		for(int i = 0; i < allCombinations.length; i++) {
			if(guess.equals(allCombinations[i])) { //returns true if the guess is found in the allCombinations array
				return true;
			}
		}
		return false; //returns false if it is not found in the array
	}
}
