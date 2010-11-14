import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

enum Checker { RED, BLACK, EMPTY };

public class Board {

	private int boardCols, boardRows;
	private JLabel[][] coins;
	private JButton[] colBut;
	private JLabel prompter;
	PipedOutputStream pout;

	private ImageIcon emptyCoin = createImageIcon("empty.jpg");
	private ImageIcon redCoin = createImageIcon("red.jpg");
	private ImageIcon blackCoin = createImageIcon("black.jpg");

	public Board (int rows, int cols, PipedOutputStream pout) {

		boardRows = rows;
		boardCols = cols;

		coins = new JLabel[boardRows][boardCols];
		for (int i=0; i<boardRows; i++) {
			for (int j=0; j<boardCols; j++)
				coins[i][j] = new JLabel(/*"["+i+","+j+"]");*/emptyCoin);
		}

		colBut = new JButton[boardCols];
		for (int j=0; j<boardCols; j++) {
			colBut[j] = new JButton ("("+j+")");
			colBut[j].addActionListener(new ButtonActionListener());
		}

		prompter = new JLabel("BEGIN");

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		JFrame frame = new JFrame("Connect Four HUJI Contest 2010");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//creating the actual board panel
		JPanel coinsPanel = new JPanel(new GridLayout(boardRows,boardCols));
		for (int i=0; i<boardRows; i++)
			for (int j=0; j<boardCols; j++)
				coinsPanel.add(coins[i][j]);

		JPanel butPanel = new JPanel(new GridLayout(1,boardCols));
			for (int j=0; j<boardCols; j++)
				butPanel.add(colBut[j]);

		//adding a panel and a label
		JPanel gamePanel = new JPanel(new BorderLayout());
		gamePanel.add(butPanel,BorderLayout.NORTH);
		gamePanel.add(coinsPanel,BorderLayout.CENTER);
		gamePanel.add(prompter ,BorderLayout.SOUTH);

		//set window to feet componants
		frame.add(gamePanel);
		frame.pack();
		frame.setVisible(true);

		// set pipelineout
		this.pout = pout;

	}

	/** Set the buttom line String */
	public void setPrompter (String text) {
		prompter.setText(text);
	}

	/** Set a coin on board */
	public void setCoin (int col, Checker color) {
		for (int row=boardRows-1; row>=0; row--) {
			if (coins[row][col].getIcon() == emptyCoin) {
				setCoin(row,col,color);
				row = -1; //break;
			}
		}
	}

	/** Set a coin on board */
	public void setCoin (int row, int col, Checker color) {
		if (color == Checker.EMPTY)
			coins[row][col].setIcon(emptyCoin);
		if (color == Checker.RED)
			coins[row][col].setIcon(redCoin);
		if (color == Checker.BLACK)
			coins[row][col].setIcon(blackCoin);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Board.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/** Returns the board description */
	public Checker[][] getBoard() {
		Checker[][] boardDesc = new Checker[boardRows][boardCols];
		for (int i=0; i<boardRows; i++)
			for (int j=0; j<boardCols; j++) {
				if (coins[i][j].getIcon() == emptyCoin)
					boardDesc[i][j] = Checker.EMPTY;
				if (coins[i][j].getIcon() == redCoin)
					boardDesc[i][j] = Checker.RED;
				if (coins[i][j].getIcon() == blackCoin)
					boardDesc[i][j] = Checker.BLACK;
			}
		return boardDesc;
	}

	/** Returns the board description */
	public boolean checkFull() {
		for (int i=0; i<boardRows; i++)
			for (int j=0; j<boardCols; j++)
				if (coins[i][j].getIcon() == emptyCoin)
					return false;
		return true;
	}

	/** Check for winner */
	public Checker checkWinner () {
		Checker[][] currBoard = getBoard();
		int retVal = 1;
		for (int i=0; i<boardRows; i++)
			for (int j=0; j<boardCols; j++) {
				try {
				if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i+1][j]) &&
					     (currBoard[i][j] == currBoard[i+2][j]) &&
					     (currBoard[i][j] == currBoard[i+3][j]) )
						return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
				try {	if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i-1][j]) &&
					     (currBoard[i][j] == currBoard[i-2][j]) &&
					     (currBoard[i][j] == currBoard[i-3][j]) )
					return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
				try {	if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i][j+1]) &&
					     (currBoard[i][j] == currBoard[i][j+2]) &&
					     (currBoard[i][j] == currBoard[i][j+3]) )
					return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
				try {	if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i][j-1]) &&
				   	     (currBoard[i][j] == currBoard[i][j-2]) &&
					     (currBoard[i][j] == currBoard[i][j-3]) )
					return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
				try {	if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i+1][j+1]) &&
					     (currBoard[i][j] == currBoard[i+2][j+2]) &&
					     (currBoard[i][j] == currBoard[i+3][j+3]) )
					return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
				try {	if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i-1][j+1]) &&
					     (currBoard[i][j] == currBoard[i-2][j+2]) &&
					     (currBoard[i][j] == currBoard[i-3][j+3]) )
					return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
				try {	if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i+1][j-1]) &&
					     (currBoard[i][j] == currBoard[i+2][j-2]) &&
					     (currBoard[i][j] == currBoard[i+3][j-3]) )
					return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
				try {	if ( (currBoard[i][j] != Checker.EMPTY) &&
					     (currBoard[i][j] == currBoard[i-1][j-1]) &&
					     (currBoard[i][j] == currBoard[i-2][j-2]) &&
					     (currBoard[i][j] == currBoard[i-3][j-3]) )
					return currBoard[i][j];
				} catch (ArrayIndexOutOfBoundsException e) { }
		}
		return Checker.EMPTY;
	}

	// Listener inner class
	class ButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String colStr = ((JButton)e.getSource()).getText();
			int colInt = (int)colStr.charAt(1) - '0';
			try {
				pout.write(colInt);
			} catch (Exception excp) {}
		}	
	}

}
