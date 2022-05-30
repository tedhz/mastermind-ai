package codebreaker;
import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CodebreakerGUI extends JFrame implements ActionListener {
	//declaring all JComponents used in the GUI
	private static JLabel username,aipvp,yourguess,go,rect,rect2,currentguess,peg1,peg2,peg3,peg4,guesspeg1,guesspeg2,guesspeg3,guesspeg4,bw1,bw2,bw3,bw4,enterfour,blackwhite,blw1,blw2,blw3,blw4,console,logo,mainmenu,logo2;
	private static JButton name,ai,pvp,master,breaker,instr,r,gr,b,o,y,p,submit,back,exit,bl,wh,nextb,easy,hard,filewriter;
	private static JLayeredPane brk;
	private static JTextPane instructions,history;
	private static JPanel startup,menu,gameover;
	private static JTextField nametext;
	private static JFrame frame;
	static String guess = "";
	static String pegs = "";
	static int count = 1; //count is changed as guesses are being made
	static int next = 0; //next represents the steps in the PvP gamemode (e.g. enter guess, enter black pegs, enter white pegs)
	public String code = Codebreaker.randomGuess(); //randomized code for the AI codebreaker gamemode

	static int aitimes = 0;

	/**
	 * Main method of the GUI program. Calls the Start Up Window which welcomes the user into the program. Also
	 * clears history of txt file that is filled from previous games.
	 * 
	 * @param args <type []String>
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		try {
			Codebreaker.clearHistory(); //history is cleared so user can only see their own plays, guesses, etc.
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		startUpWindow(); //calls start up window
	} 

	/**
	 * Start up window welcomes the user into the program. The user enters their username which is recorded into the
	 * txt file.
	 * 
	 * @return void
	 */
	public static void startUpWindow() {
		ImageIcon codebr = new ImageIcon("resources/codebreaker.png"); 
		Image codebr2 = codebr.getImage(); //resizing image
		Image codebr3 = codebr2.getScaledInstance(300, 120, java.awt.Image.SCALE_SMOOTH);
		codebr = new ImageIcon(codebr3);
		startup = new JPanel();
		frame = new JFrame();
		name = new JButton("Enter!");
		nametext = new JTextField();
		username = new JLabel("Enter Username to Continue", SwingConstants.CENTER); //centers text on JLabel

		logo = new JLabel(codebr);

		logo.setBounds(300,120,600,240); //setting up where each JComponent goes on the panel
		startup.setLayout(null);
		username.setBounds(450,425,300,30);
		startup.add(username);
		nametext.setBounds(360,390,400,30);
		startup.add(nametext);
		name.setBounds(770,390,100,30);
		startup.add(name);
		name.addActionListener(new CodebreakerGUI());

		startup.add(logo);
		frame.add(startup);
		frame.setTitle("Codebreaker");
		frame.setSize(1200,675);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Program terminates when you X out the GUI
		frame.setVisible(true);
	}

	/**
	 * Menu method allows the user to pick which game mode they would like to play. User chooses by selecting
	 * buttons, which give them the option between PvP, AI Codebreaker, AI Codemaster Easym and AI Codemaster
	 * Hard. 
	 * 
	 * @return void
	 */
	public static void menu() {
		ImageIcon main = new ImageIcon("resources/mainmenu.png");
		Image main2 = main.getImage();
		Image main3 = main2.getScaledInstance(600, 90, java.awt.Image.SCALE_SMOOTH);//resizing image
		main = new ImageIcon(main3);
		mainmenu = new JLabel(main);

		menu = new JPanel();
		menu.setLayout(null);
		instructions = new JTextPane();
		instructions.setText("Welcome to Codebreaker! PVP: Play against another player. Pick a codemaster and codebreaker amongst yourselves. Correct colour and correct position pegs are represented by black pegs, and correct colour, but wrong position is represented by white pegs. The codebreaker gets 10 guesses! AI: Codemaster - Think of a 4 colour code in your head and have the AI guess your code! Enter black and white pegs to guide the AI. Codebreaker - Take guesses at the AI's secret code! You have 10 guesses, and the AI will give you hints thorugh the black and white pegs. Good Luck!");
		instructions.setEditable(false);
		ai = new JButton("AI");//if user wants to play against AI
		pvp = new JButton("PvP");//if user wants to play against another player
		instr = new JButton("Instructions");//allows user to read the rules
		aipvp = new JLabel("Welcome to Codebreaker! Do you want to play vs. AI or another player?", SwingConstants.CENTER);
		console = new JLabel("Look at the console to play codemaster.", SwingConstants.CENTER);
		filewriter = new JButton("History");//if user wants to see previous guesses, gamemodes

		filewriter.setBounds(1050,600,120,30);//sets coordinates for where each JComponent goes
		aipvp.setBounds(300,240,600,30);
		ai.setBounds(630,300,150,30);
		pvp.setBounds(420,300,150,30);
		instr.setBounds(470,600,240,30);
		instructions.setBounds(210,480,780,90);
		console.setBounds(465,405,270,30);
		mainmenu.setBounds(300,90,600,90);
		menu.add(mainmenu);

		console.setVisible(false);
		ai.addActionListener(new CodebreakerGUI());
		pvp.addActionListener(new CodebreakerGUI());
		filewriter.addActionListener(new CodebreakerGUI());
		instr.addActionListener(new CodebreakerGUI());
		instructions.setVisible(false);//add action listeners for all the buttons

		menu.add(instructions);
		menu.add(console);
		menu.add(instr);
		menu.add(aipvp);
		menu.add(ai);
		menu.add(pvp);

		frame.add(menu);
	}
	/**
	 * Masterbreaker method allows the user to choose between codemaseter or codebreaker
	 * when the AI option is selected from the menu.
	 * 
	 * @param menu - buttons 'master' and 'breaker' are added to menu <type JPanel>
	 * @return void
	 */
	public static void masterbreaker(JPanel menu) {
		aipvp.setText("Do you want to be the Codebreaker or the Codemaster?");
		master = new JButton("Codemaster");//if the user wants to play as codemaster
		breaker = new JButton("Codebreaker");//if the user wants to play as codebreaker
		master.setBounds(545,335,150,30);//sets bounds
		breaker.setBounds(715,335,150,30);
		master.addActionListener(new CodebreakerGUI());
		breaker.addActionListener(new CodebreakerGUI());

		menu.add(master);
		menu.add(breaker);
		pvp.setEnabled(false);//doesnt allow the user to click pvp option after ai is selected
		menu.repaint();
	}
	/**
	 * Easyhard method allows the user to choose between easy or hard AI difficulty if the
	 * user selects the Codemaster gamemode from the menu (maseterbreaker method)
	 * 
	 * @param menu - two buttons 'easy' and 'hard' are added to menu <type JPanel>
	 * @return void
	 */
	public static void easyhard(JPanel menu) {
		easy = new JButton("Easy");//picks difficulty of AI for codemaster
		hard = new JButton("Hard");
		easy.setBounds(455,370,150,30);
		hard.setBounds(625,370,150,30);
		easy.addActionListener(new CodebreakerGUI());
		hard.addActionListener(new CodebreakerGUI());
		menu.add(hard);
		menu.add(easy);
		breaker.setEnabled(false);
		menu.repaint();
	}
	/**
	 * Breaker method is used for the codebreaker gamemode mainly, but is also used for the 
	 * PvP gamemode. It is the interface the user sees when they pick the colours they want to
	 * guess, and also contains other features such as viewing history of the game (previous 
	 * guesses and pegs)
	 * 
	 * @return void
	 */
	public static void breaker() {
		brk = new JLayeredPane();//JLayeredPane allows components to be added in different layers (e.g. one layer on top of another)
		brk.setLayout(null);

		Icon green = new ImageIcon("resources/green.jpg");//pictures from resource folder
		Icon red = new ImageIcon("resources/red.png");
		Icon blue = new ImageIcon("resources/blue.png");
		Icon yellow = new ImageIcon("resources/yellow.jpg");
		Icon orange = new ImageIcon("resources/orange.png");
		Icon purple = new ImageIcon("resources/purple.jpg");
		Icon rectangle = new ImageIcon("resources/grey.jpg");
		ImageIcon logo = new ImageIcon("resources/codebreaker.png");

		currentguess = new JLabel("Current Guess:", SwingConstants.CENTER);

		rect = new JLabel(rectangle);
		r = new JButton(red);
		gr = new JButton(green);
		b = new JButton(blue);
		y = new JButton(yellow);
		o = new JButton(orange);
		p = new JButton(purple);
		submit = new JButton("Submit");
		peg1 = new JLabel();
		peg2 = new JLabel();
		peg3 = new JLabel();
		peg4 = new JLabel();
		enterfour = new JLabel("",SwingConstants.CENTER);

		gr.setText("Green");
		r.setText("Red");
		b.setText("Blue");
		y.setText("Yellow");
		o.setText("Orange");
		p.setText("Purple");
		yourguess = new JLabel("Enter 4 colours you want to guess:", SwingConstants.CENTER);


		Image pic2 = logo.getImage();
		Image pic3 = pic2.getScaledInstance(400, 150, java.awt.Image.SCALE_SMOOTH);
		logo = new ImageIcon(pic3);
		logo2 = new JLabel(logo);
		logo2.setBounds(230,150,400,150);
		brk.add(logo2);

		history = new JTextPane();
		history.setEditable(false);
		history.setBounds(840,320,300,250);//setting bounds for all components
		currentguess.setBounds(270,530,330,30);
		rect.setBounds(210,360,450,165);
		yourguess.setBounds(270,390,330,30);
		gr.setBounds(280,560,30,30);
		r.setBounds(315,560,30,30);
		b.setBounds(350,560,30,30);
		y.setBounds(385,560,30,30);
		o.setBounds(420,560,30,30);
		p.setBounds(455,560,30,30);
		submit.setBounds(490,560,102,30);
		peg1.setBounds(330,435,30,30);
		peg2.setBounds(390,435,30,30);
		peg3.setBounds(450,435,30,30);
		peg4.setBounds(510,435,30,30);
		enterfour.setBounds(270,590,330,30);

		gr.addActionListener(new CodebreakerGUI());//adding action listener for all buttons
		r.addActionListener(new CodebreakerGUI());
		b.addActionListener(new CodebreakerGUI());
		y.addActionListener(new CodebreakerGUI());
		o.addActionListener(new CodebreakerGUI());
		p.addActionListener(new CodebreakerGUI());
		submit.addActionListener(new CodebreakerGUI());
		history.setVisible(false);

		brk.add(history);//adding all components
		brk.add(filewriter);
		brk.add(yourguess);
		brk.add(gr);
		brk.add(r);
		brk.add(b);
		brk.add(y);
		brk.add(o);
		brk.add(p);
		brk.add(submit);
		brk.add(currentguess);
		brk.add(peg1);
		brk.add(peg2);
		brk.add(peg3);
		brk.add(peg4);
		brk.add(enterfour);
		brk.add(rect);

		frame.add(brk);
	}
	/**
	 * GameOver method is shown either if the user wins or loses (at the end of every
	 * game). It is meant to thank the user for playing, as well as give them the option
	 * to play again, or exit the game.
	 * 
	 * @return void
	 */
	public static void gameOver() {
		gameover = new JPanel();
		back = new JButton("Main Menu");
		exit = new JButton("Exit");

		gameover.setLayout(null);
		go = new JLabel("",SwingConstants.CENTER);

		go.setBounds(300,90,600,240);//setting bounds
		back.setBounds(450,350,120,30);
		exit.setBounds(630,350,120,30);
		back.addActionListener(new CodebreakerGUI());//adding action listener
		exit.addActionListener(new CodebreakerGUI());

		gameover.add(go);
		gameover.add(exit);
		gameover.add(back);

		frame.add(gameover);
	}
	/**
	 * GuessDisplay method shows the user's previous guesses on the right side of
	 * the screen. It is meant to allow the user to refer to previous guesses and black
	 * white pegs in order to influence their next guess.
	 * 
	 * List of Local Variables
	 * y - linear function determines the y coordinate of each peg based on number of guesses made <type int>
	 * y2 - linear function expands the rectangle the pegs are stored in as more guesses are made
	 * 
	 * @param brk - all the pegs are added to the brk panel <type JLayeredPane>
	 * @param peg1 - represents the first peg from the guess and is copied to the first guesspeg <type JLabel>
	 * @param peg2 - represents the second peg from the guess and is copied to the second guesspeg <type JLabel>
	 * @param peg3 - represents the third peg from the guess and is copied to the third guesspeg <type JLabel>
	 * @param peg4 - represents the fourth peg from the guess and is copied to the fourth guesspeg <type JLabel>
	 * @param count - count controls where each peg is, as well as how big the rectangle should be <type int>
	 * @return void
	 */
	public static void guessDisplay(JLayeredPane brk, JLabel peg1, JLabel peg2, JLabel peg3, JLabel peg4, int count) {
		int y = 42*(count-1)+100; 
		int y2 = 42*(count-1)+45; 

		Icon rectangle = new ImageIcon("resources/grey.jpg");//getting image from resource folder
		rect2 = new JLabel(rectangle);
		guesspeg1 = new JLabel();
		guesspeg2 = new JLabel();
		guesspeg3 = new JLabel();
		guesspeg4 = new JLabel();

		guesspeg1.setIcon(peg1.getIcon());//changing icon to the corresponding peg from parameters
		guesspeg2.setIcon(peg2.getIcon());
		guesspeg3.setIcon(peg3.getIcon());
		guesspeg4.setIcon(peg4.getIcon());

		rect2.setBounds(820,90,300,y2);
		guesspeg1.setBounds(850,y,30,30);//setting bounds
		guesspeg2.setBounds(895,y,30,30);
		guesspeg3.setBounds(940,y,30,30);
		guesspeg4.setBounds(985,y,30,30);

		brk.add(guesspeg1);
		brk.add(guesspeg2);
		brk.add(guesspeg3);
		brk.add(guesspeg4);
		brk.add(rect2, JLayeredPane.DEFAULT_LAYER);//adds it to the bottom layer

		brk.repaint();
	}
	/**
	 * BW method displays the predetermined black and white pegs in alignment with the guessed pegs from the
	 * guessDisplay method. Shows the user which pegs they got right or were close on to give the user hints
	 * while playing.
	 * 
	 * List of Local Variables
	 * y - linear function determines the y coordinate of each peg based on number of guesses made <type int>
	 * y2 - linear function expands the rectangle the pegs are stored in as more guesses are made
	 * 
	 * @param brk - the black and white pegs are added to the brk panel <type JLayeredPane>
	 * @param bw - bw represents the assigned black and white pegs for the respective guess <type String>
	 * @param count - represents how many guesses were made to determine where the pegs are placed on the panel <type int>
	 * @return void
	 */
	public static void bw(JLayeredPane brk, String bw, int count) {
		int y = 42*(count-1)+105;
		int y2 = 42*(count-1)+90;

		Icon rectangle = new ImageIcon("resources/grey.jpg");//gets pictures from resource folder
		Icon black = new ImageIcon("resources/black.jpg");
		Icon white = new ImageIcon("resources/white.png");
		rect2 = new JLabel(rectangle);
		bw1 = new JLabel();
		bw2 = new JLabel();
		bw3 = new JLabel();
		bw4 = new JLabel();
		bw1.setBounds(1020,y,10,10);
		bw2.setBounds(1035,y,10,10);
		bw3.setBounds(1050,y,10,10);
		bw4.setBounds(1065,y,10,10);
		rect2.setBounds(840,90,300,y2);

		for(int i = 0;i<bw.length();i++) {//sets the correct icon for each peg
			if(bw.charAt(i)==('B')&& i==0) 
				bw1.setIcon(black);
			if(bw.charAt(i)==('W')&& i==0) 
				bw1.setIcon(white);
			if(bw.charAt(i)==('B')&& i==1) 
				bw2.setIcon(black);
			if(bw.charAt(i)==('W')&& i==1) 
				bw2.setIcon(white);
			if(bw.charAt(i)==('B')&& i==2) 
				bw3.setIcon(black);
			if(bw.charAt(i)==('W')&& i==2) 
				bw3.setIcon(white);
			if(bw.charAt(i)==('B')&& i==3) 
				bw4.setIcon(black);
			if(bw.charAt(i)==('W')&& i==3) 
				bw4.setIcon(white);
		}

		brk.add(bw1, JLayeredPane.DRAG_LAYER);//adds it to the top layer (above the grey rectangle)
		brk.add(bw2, JLayeredPane.DRAG_LAYER);
		brk.add(bw3, JLayeredPane.DRAG_LAYER);
		brk.add(bw4, JLayeredPane.DRAG_LAYER);
		brk.repaint();//repaint the panel
	}
	/**
	 * PVPBW method gives the user the option to enter black and white pegs that corresponds
	 * with the other player's guess. The black and white pegs are shown above the buttons
	 * when they are pressed, and the black and white pegs are required in alphabetical order
	 * (black pegs before white pegs)
	 * 
	 * @param brk - buttons, labels, icons and text are added to panel brk <type JLayeredPane>
	 * @return void
	 */
	public static void pvpbw(JLayeredPane brk) {
		Icon black = new ImageIcon("resources/black.jpg");//get pictures from resource folder
		Icon white = new ImageIcon("resources/white.png");
		Icon rectangle = new ImageIcon("resources/grey.jpg");
		rect = new JLabel(rectangle);
		blackwhite = new JLabel("Enter black pegs.",SwingConstants.CENTER);
		blw1 = new JLabel();
		blw2 = new JLabel();
		blw3 = new JLabel();
		blw4 = new JLabel();

		bl = new JButton(black);
		wh = new JButton(white);
		nextb = new JButton("Next");
		bl.setText("Black");
		wh.setText("White");

		bl.addActionListener(new CodebreakerGUI());
		wh.addActionListener(new CodebreakerGUI());
		nextb.addActionListener(new CodebreakerGUI());

		rect.setBounds(210,110,450,120);//sets bounds for all components
		blackwhite.setBounds(300,125,270,30);
		bl.setBounds(330,260,30,30);
		wh.setBounds(390,260,30,30);
		nextb.setBounds(450,260,90,30);
		blw1.setBounds(330,170,30,30);
		blw2.setBounds(390,170,30,30);
		blw3.setBounds(450,170,30,30);
		blw4.setBounds(510,170,30,30);

		brk.add(blw1);
		brk.add(blw2);
		brk.add(blw3);
		brk.add(blw4);
		brk.add(bl);
		brk.add(blackwhite);
		brk.add(nextb);
		brk.add(rect);
		bl.setEnabled(false);//black peg button and next is disabled until the first player enters their guess
		nextb.setEnabled(false);

		brk.repaint();//repaints panel
	}
	/**
	 * Reader gives the option for the user to view previous guesses and gamemodes played by clicking
	 * the 'History' button. Reader uses filereader to read from the history.txt file that is written 
	 * in throughout the program.
	 * 
	 * List of Local Variables
	 * str - str is set to the text from the history.txt file and is used to set the text in the JTextPanel <type String>
	 * 
	 * @param brk - 'history' is in the brk panel, and it is in the parameters to repaint the panel <type JLayeredPane>
	 * @throws IOException - used for filereader
	 * @return void
	 */
	public static void reader(JLayeredPane brk) throws IOException{
		String str;
		str = printHistory();
		history.setText(str);

		brk.repaint();
	}
	/**
	 * PrintHistory method is used to get the text from history.txt file and return it to the reader method above
	 * 
	 * List of Local Variables
	 * str - str reads line for line from history.txt <type String>
	 * total - keeps track of the total text from history.txt that is read <type String>
	 * 
	 * @return total - returns the text in the history.txt file in a String <type String>
	 * @throws IOException
	 */
	public static String printHistory() throws IOException{
		String total = "";
		String str;
		BufferedReader in = new BufferedReader(new FileReader("./resources/history.txt"));
		while((str = in.readLine()) != null) {
			total=total.concat("\n"+str);
		}
		return total;
	}
	/**
	 * actionPerformed method is used to manage events, as well as to set actions for when buttons are pressed.
	 * 
	 * List of Local Variables
	 * name - name is entered by the user at the beginning and is written to the txt file <type String>
	 * bw - bw represents the black and white pegs that are either assigned by assignPegs method or entered by the user <type String>
	 * level - lets the AI know what difficulty the codemaster will be set at
	 * 
	 * @return void
	 */
	public void actionPerformed(ActionEvent e) {
		String name;
		String bw="";
		int level;
		Icon green = new ImageIcon("resources/green.jpg");//getting pictures from resource folder
		Icon red = new ImageIcon("resources/red.png");
		Icon blue = new ImageIcon("resources/blue.png");
		Icon yellow = new ImageIcon("resources/yellow.jpg");
		Icon orange = new ImageIcon("resources/orange.png");
		Icon purple = new ImageIcon("resources/purple.jpg");
		Icon black = new ImageIcon("resources/black.jpg");
		Icon white = new ImageIcon("resources/white.png");
		ImageIcon lose = new ImageIcon("resources/youlose.png");
		Image lose2 = lose.getImage();
		Image lose3 = lose2.getScaledInstance(600, 240, java.awt.Image.SCALE_SMOOTH);//resizing image
		lose = new ImageIcon(lose3);
		ImageIcon win = new ImageIcon("resources/youwin.png");
		Image win2 = win.getImage();
		Image win3 = win2.getScaledInstance(600, 240, java.awt.Image.SCALE_SMOOTH);
		win = new ImageIcon(win3);
		ImageIcon p1 = new ImageIcon("resources/p1.png");
		Image p12 = p1.getImage();
		Image p13 = p12.getScaledInstance(550, 240, java.awt.Image.SCALE_SMOOTH);
		p1 = new ImageIcon(p13);
		ImageIcon p2 = new ImageIcon("resources/p2.png");
		Image p22 = p2.getImage();
		Image p23 = p22.getScaledInstance(550, 240, java.awt.Image.SCALE_SMOOTH);
		p2 = new ImageIcon(p23);
		
		if(e.getActionCommand().equals("Submit")) {//for codebreaker mode
			enterfour.setText(null);//text is shown if the player does not enter four colours to guess
			System.out.println(code);
			if(guess.length()!=4) {//if the user does not guess 4 colours
				guess = "";
				peg1.setIcon(null);
				peg2.setIcon(null);
				peg3.setIcon(null);
				peg4.setIcon(null);//resets guess
				enterfour.setText("Please enter four colours.");
			}
			else if(count==10) {//if the user guessed 10 times (they lose)
				brk.setVisible(false);
				guess="";
				gameOver();
				go.setIcon(lose);
				count=1;
			}
			else if(count<=10 && guess.length()==4){//for valid guesses
				bw = Codebreaker.assignPegs(guess,code);//checks for black and white pegs
				try {
					Codebreaker.filewriter(Codebreaker.convertToColours(guess));//writes the player's guess into txt file
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Codebreaker.filewriter(bw);//writes the black and white pegs into txt file
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				guessDisplay(brk,peg1,peg2,peg3,peg4,count);//displays guess
				bw(brk,bw,count);//displays black and white pegs

				peg1.setIcon(null);//resets peg colours
				peg2.setIcon(null);
				peg3.setIcon(null);
				peg4.setIcon(null);
				if(bw.equals("BBBB")) {//if the user wins
					brk.setVisible(false);
					gameOver();
					go.setIcon(win);
					count=1;
				}
				guess = "";//resets the guess
				count++;//counts how many guesses the user has made
			}
		}
		if(e.getActionCommand().equals("Enter!")) {//this button is clicked when the user enters their name in the startup window
			startup.setVisible(false);//closes start up panel
			name = nametext.getText();
			System.out.println(name);
			try {
				Codebreaker.filewriter("Username: "+name);//name is written to txt file
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			menu();//opens the menu
		}
		if(e.getActionCommand().equals("PvP")) {//if the user wants to play PvP
			menu.setVisible(false);
			try {
				Codebreaker.filewriter("PvP");//written into txt file
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			breaker();//PvP method needs colour guesses as well as black/white pegs. breaker is the coloured pegs
			logo2.setVisible(false);
			submit.setText("Enter");//changes button name so it does not get mixed up with other game modes
			pvpbw(brk);//black white pegs
		}

		if(e.getActionCommand().equals("AI")) {//if the user wants to play versus AI
			masterbreaker(menu);//adds codemaster and codebreaker button
			breaker.setEnabled(true);
			master.setEnabled(true);
			aitimes++;
		}
		if(e.getActionCommand().equals("Instructions")) {//toggle opens the instruction textpane
			if(instructions.isVisible())
				instructions.setVisible(false);
			else
				instructions.setVisible(true);
		}
		if(e.getActionCommand().equals("Codebreaker")) {//if the user wants to play codebreaker
			menu.setVisible(false);//closes menu panel
			try {
				Codebreaker.filewriter("Codebreaker");//history.txt keeps track of game modes played
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			breaker();//opens codebreaker method gamemode
		}
		if(e.getActionCommand().equals("Codemaster")) {//if user wants to play codemaster
			easyhard(menu);//user gets to choose difficulty
			try {
				Codebreaker.filewriter("Codemaster");//keeps track of gamemode
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			console.setVisible(true);//lets user know they have to play codemaster in the console
		}
		if(e.getActionCommand().equals("Easy")) {//easy difficulty
			level = 0;
			try {
				Codebreaker.codemaster(level);//sends to codemaster method in Codemaster class (easy difficulty because level is 0)
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("Hard")) {//hard difficulty
			level = 1;
			try {
				Codebreaker.codemaster(level);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("Green")) {//if user clicks the green colour button
			guess = guess.concat("1");//keeps track of guess
			if(guess.length()==1)//if green is the first colour pressed, it makes first peg green
				peg1.setIcon(green);
			if(guess.length()==2)//if green second colour pressed, it makes second peg green
				peg2.setIcon(green);
			if(guess.length()==3)
				peg3.setIcon(green);
			if(guess.length()==4)
				peg4.setIcon(green);
		}
		if(e.getActionCommand().equals("Red")) {//same as green
			guess = guess.concat("2");
			if(guess.length()==1)
				peg1.setIcon(red);
			if(guess.length()==2)
				peg2.setIcon(red);
			if(guess.length()==3)
				peg3.setIcon(red);
			if(guess.length()==4)
				peg4.setIcon(red);
		}
		if(e.getActionCommand().equals("Blue")) {//same as green
			guess = guess.concat("3");
			if(guess.length()==1)
				peg1.setIcon(blue);
			if(guess.length()==2)
				peg2.setIcon(blue);
			if(guess.length()==3)
				peg3.setIcon(blue);
			if(guess.length()==4)
				peg4.setIcon(blue);
		}
		if(e.getActionCommand().equals("Yellow")) {//same as green
			guess = guess.concat("4");
			if(guess.length()==1)
				peg1.setIcon(yellow);
			if(guess.length()==2)
				peg2.setIcon(yellow);
			if(guess.length()==3)
				peg3.setIcon(yellow);
			if(guess.length()==4)
				peg4.setIcon(yellow);
		}
		if(e.getActionCommand().equals("Orange")) {//same as green
			guess = guess.concat("5");
			if(guess.length()==1)
				peg1.setIcon(orange);
			if(guess.length()==2)
				peg2.setIcon(orange);
			if(guess.length()==3)
				peg3.setIcon(orange);
			if(guess.length()==4)
				peg4.setIcon(orange);
		}
		if(e.getActionCommand().equals("Purple")) {//same as green
			guess = guess.concat("6");
			if(guess.length()==1)
				peg1.setIcon(purple);
			if(guess.length()==2)
				peg2.setIcon(purple);
			if(guess.length()==3)
				peg3.setIcon(purple);
			if(guess.length()==4)
				peg4.setIcon(purple);
		}
		if(e.getActionCommand().equals("Exit")) {//closes the window
			frame.dispose();
		}
		if(e.getActionCommand().equals("Main Menu")) {//brings user back to the main menu screen
			count = 1;//resets count
			gameover.setVisible(false);
			menu.setVisible(true);//changes panels
			if(aitimes!=0) {//removes buttons exposed if clicked earlier
				menu.remove(master);
				menu.remove(breaker);
				while(easy!=null) {
					menu.remove(easy);
					menu.remove(hard);
				}
				aipvp.setText("Do you want to play vs. AI or another player?");//resets text back to default
				aitimes = 0;
			}
			pvp.setEnabled(true);//brings back PvP button
		}
		if(e.getActionCommand().equals("Black")) {//same as green, except it is added to pegs variable
			pegs = pegs.concat("B");
			if(pegs.length()==1)
				blw1.setIcon(black);
			if(pegs.length()==2)
				blw2.setIcon(black);
			if(pegs.length()==3)
				blw3.setIcon(black);
			if(pegs.length()==4)
				blw4.setIcon(black);
		}
		if(e.getActionCommand().equals("White")) {//same as black
			pegs=pegs.concat("W");
			if(pegs.length()==1)
				blw1.setIcon(white);
			if(pegs.length()==2)
				blw2.setIcon(white);
			if(pegs.length()==3)
				blw3.setIcon(white);
			if(pegs.length()==4)
				blw4.setIcon(white);
		}
		if(e.getActionCommand().equals("Next")) {//for the black/white pegs in PvP
			next++;//moves on to next step
			if(next==1) {//step 2: after black pegs are inputted, disables black button and enables white button
				if(brk.isVisible()) {
					brk.add(wh);
					brk.remove(bl);
				}
				blackwhite.setText("Enter White pegs.");//changes text to instruct player what to do
			}
			if(next==2) {//step 3: add black/white pegs to the display on the right side
				brk.add(bl);//resets the buttons, removes white and adds black
				brk.remove(wh);
				blackwhite.setText("Enter Black pegs.");//resets instruction statement
				if(pegs.length()>4) {//if there are more than 4 pegs, the pegs are not taken
					pegs = "";
					blw1.setIcon(null);
					blw2.setIcon(null);
					blw3.setIcon(null);
					blw4.setIcon(null);
				}
				else if(count==10) {//if the user guesses 10 times
					brk.setVisible(false);//switches panel to game over
					gameOver();
					go.setIcon(p1);//player 1 wins image
					count = 1;//resets count
				}
				else if(pegs.equals("BBBB")) {//if codebreaker wins
					brk.setVisible(false);
					try {
						Codebreaker.filewriter(pegs);//writes pegs into txt file
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					pegs = "";//resets pegs
					gameOver();
					go.setIcon(p2);//player 2 wins image
				}
				else if(pegs.length()<=4) {
					bw(brk,pegs,count);//displays black and white pegs
					try {
						Codebreaker.filewriter(pegs);//pegs are written in txt file
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					pegs = "";//resets pegs
					blw1.setIcon(null);//resets icons
					blw2.setIcon(null);
					blw3.setIcon(null);
					blw4.setIcon(null);
					count++;//counts number of guesses
					submit.setEnabled(true);
					gr.setEnabled(true);
					r.setEnabled(true);
					b.setEnabled(true);
					y.setEnabled(true);
					o.setEnabled(true);
					p.setEnabled(true);
					bl.setEnabled(false);
					nextb.setEnabled(false);
				}
				next = 0;
			}
		}
		if(e.getActionCommand().equals("Enter")) {
			if(guess.length()!=4) {				
				guess = "";
				peg1.setIcon(null);
				peg2.setIcon(null);
				peg3.setIcon(null);
				peg4.setIcon(null);
				enterfour.setText("Please enter four colours.");
			}
			else if(guess.length()==4){
				guessDisplay(brk,peg1,peg2,peg3,peg4,count);
				peg1.setIcon(null);
				peg2.setIcon(null);
				peg3.setIcon(null);
				peg4.setIcon(null);
				try {
					Codebreaker.filewriter(Codebreaker.convertToColours(guess));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				guess = "";
				submit.setEnabled(false);
				gr.setEnabled(false);
				r.setEnabled(false);
				b.setEnabled(false);
				y.setEnabled(false);
				o.setEnabled(false);
				p.setEnabled(false);
				bl.setEnabled(true);
				nextb.setEnabled(true);
			}
		}
		if(e.getActionCommand().equals("History")) {
			try {
				reader(brk);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(history.isVisible()) {
				history.setVisible(false);
			}
			else {
				history.setVisible(true);
			}

		}
	}
}