package game_2048;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GUI1024Panel extends JPanel {

	private JLabel[][] gameBoardUI;
	private NumberGameArrayList gameLogic;
	private int nRows, nCols, winningValue;
	private JButton upButton, downButton, leftButton, rightButton, undoButton;
	private JPanel playPanel, gamePanel, buttonPanel, statPanel;
	private JLabel numSlidesLabel, highScoreLabel, newGamesLabel;
	public static JLabel numSlides;
	public static JLabel highScore;
	public static JLabel newGames;
	private JMenuItem exit, reset, setGoal;
	
	public GUI1024Panel() {
		initializeGame();
	}

	public GUI1024Panel(JMenuItem exit, JMenuItem reset, JMenuItem setGoal) {
		this.exit = exit;
		this.reset = reset;
		this.setGoal = setGoal;

		initializeGame();
	}

	private void initializeGame() {

		// Initialize GridBagLayoutConstraints
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// Indicate which column
		c.gridx = 0;
		// Indicate which row
		c.gridy = 0;
		// Indicate number of columns to span
		//c.gridwidth = 1;
		// Specify custom height
		//c.ipady = 40;

		// Initialize the game board appearance
		setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		setLayout(new GridBagLayout());
		playPanel = new JPanel();
		buttonPanel = new JPanel();
		statPanel = new JPanel();
		playPanel.setLayout(new GridBagLayout());
		buttonPanel.setLayout(new GridBagLayout());
		statPanel.setLayout(new GridBagLayout());
		add(playPanel, c);

		// Initialize the game panel
		gamePanel = new JPanel();
		playPanel.add(gamePanel, c);
		c.gridx = 1;
		playPanel.add(buttonPanel, c);
		c.gridx = 0;
		c.gridy = 1;
		playPanel.add(statPanel, c);
		
		//Add Menu Bar
		
		//Add buttons
		upButton = new JButton("Up");
		leftButton = new JButton("Left");
		downButton = new JButton("Down");
		rightButton = new JButton("Right");
		undoButton = new JButton("Undo");

		c.gridx = 1;
		c.gridy = 0;
		buttonPanel.add(upButton, c);
		c.gridx = 0;
		c.gridy = 1;
		buttonPanel.add(leftButton, c);
		c.gridx = 1;
		c.gridy = 2;
		buttonPanel.add(downButton, c);
		c.gridx = 2;
		c.gridy = 1;
		buttonPanel.add(rightButton, c);
		c.gridx = 1;
		c.gridy = 1;
		buttonPanel.add(undoButton, c);
		
		upButton.addActionListener(new SlideListener());
		leftButton.addActionListener(new SlideListener());
		downButton.addActionListener(new SlideListener());
		rightButton.addActionListener(new SlideListener());
		undoButton.addActionListener(new SlideListener());
		exit.addActionListener(new SlideListener());
		reset.addActionListener(new SlideListener());
		setGoal.addActionListener(new SlideListener());
		
		//Add statistics panel
		numSlidesLabel = new JLabel("Number of Slides: ");
		highScoreLabel = new JLabel("High Score: ");
		newGamesLabel = new JLabel("New Games: ");
		numSlides = new JLabel(String.valueOf(0) + " ");
		highScore = new JLabel(String.valueOf(0) + " ");
		newGames = new JLabel(String.valueOf(0) + " ");
		
		statPanel.add(numSlidesLabel);
		statPanel.add(numSlides);
		statPanel.add(highScoreLabel);
		statPanel.add(highScore);
		statPanel.add(newGamesLabel);
		statPanel.add(newGames);

		// Allow keys to be pressed to control the game
		setFocusable(true);
		addKeyListener(new SlideListener());

		// Initialize the game GUI and logic
		resizeBoard();
	}

	private void updateBoard() {
		for (JLabel[] row : gameBoardUI)
			for (JLabel s : row) {
				s.setText("");
			}

		ArrayList<Cell> out = gameLogic.getNonEmptyTiles();
		ArrayList<Cell> out2 = gameLogic.getEmptyTiles();
		if (out == null) {
			JOptionPane.showMessageDialog(null, "Incomplete implementation getNonEmptyTiles()");
			return;
		}
		for (Cell c : out) {
			double greenLevel = 255.0 - (255.0 * ((double)c.getValue() / (double)winningValue));
			Color fontColor = new Color(255, (int)greenLevel, 0);
			JLabel z = gameBoardUI[c.getRow()][c.getColumn()];
			z.setText(String.valueOf(Math.abs(c.getValue())));
			z.setForeground(c.getValue() > 0 ? fontColor : Color.RED);
			z.setBackground(c.getValue() > 0 ? Color.DARK_GRAY : Color.GRAY);
			z.setOpaque(true);
			z.setHorizontalAlignment(SwingConstants.CENTER);
			z.setVerticalAlignment(SwingConstants.CENTER);
		}
		for (Cell c : out2) {
			JLabel z = gameBoardUI[c.getRow()][c.getColumn()];
			z.setBackground(c.getValue() > 0 ? Color.LIGHT_GRAY : Color.GRAY);
			z.setOpaque(false);
		}
	}



	private class SlideListener implements KeyListener, ActionListener {		
		@Override
		public void keyTyped(KeyEvent e) { }

		@Override
		public void keyPressed(KeyEvent e) {

			boolean moved = false;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				moved = gameLogic.slide(SlideDirection.UP);
				break;
			case KeyEvent.VK_LEFT:
				moved = gameLogic.slide(SlideDirection.LEFT);
				break;
			case KeyEvent.VK_DOWN:
				moved = gameLogic.slide(SlideDirection.DOWN);
				break;
			case KeyEvent.VK_RIGHT:
				moved = gameLogic.slide(SlideDirection.RIGHT);
				break;
			case KeyEvent.VK_U:
				try {
					System.out.println("Attempt to undo");
					gameLogic.undo();
					moved = true;
				} catch (IllegalStateException exp) {
					JOptionPane.showMessageDialog(null, "Can't undo beyond the first move");
					moved = false;
				}
			}
			if (moved) {
				updateBoard();
				if (gameLogic.getStatus().equals(GameStatus.USER_WON)) {
					//JOptionPane.showMessageDialog(null, "You won");
					int resp = JOptionPane.showConfirmDialog(null, "You won! Do you want to play again?", "Congratulations!!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					numSlides.setText(String.valueOf(0) + " ");
					highScore.setText(String.valueOf(winningValue) + " ");
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						updateBoard();
					} else {
						System.exit(0);
					}
				}
				else if (gameLogic.getStatus().equals(GameStatus.USER_LOST)) {
					int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						updateBoard();
					} else {
						System.exit(0);
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) { }

		@Override
		public void actionPerformed(ActionEvent e) {

			boolean moved = false;
			if (e.getSource() == upButton)
				moved = gameLogic.slide(SlideDirection.UP);
			if (e.getSource() == leftButton)
				moved = gameLogic.slide(SlideDirection.LEFT);
			if (e.getSource() == downButton)
				moved = gameLogic.slide(SlideDirection.DOWN);
			if (e.getSource() == rightButton)
				moved = gameLogic.slide(SlideDirection.RIGHT);
			if (e.getSource() == undoButton)
				try {
					System.out.println("Attempt to undo");
					gameLogic.undo();
					moved = true;
				} catch (IllegalStateException exp) {
					JOptionPane.showMessageDialog(null, "Can't undo beyond the first move");
					moved = false;
				}
			if (moved) {
				updateBoard();
				if (gameLogic.getStatus().equals(GameStatus.USER_WON)) {
					//JOptionPane.showMessageDialog(null, "You won");
					int resp = JOptionPane.showConfirmDialog(null, "You won! Do you want to play again?", "Congratulations!!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						updateBoard();
					} else {
						System.exit(0);
					}
				}
				else if (gameLogic.getStatus().equals(GameStatus.USER_LOST)) {
					int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						updateBoard();
					} else {
						System.exit(0);
					}
				}
			}
			if (e.getSource() == exit)
				System.exit(0);
			if (e.getSource() == reset) {
				gameLogic.reset();
				updateBoard();
			}
			if (e.getSource() == setGoal) {///////////////////////////////////////////
				int winValue;
				JTextField winValueInput = new JTextField();
				Object[] textDisplay = {"New Win Value: ", winValueInput};
				int goalChoice = JOptionPane.showConfirmDialog(null, textDisplay, "Input New Win Value", JOptionPane.OK_CANCEL_OPTION);
				
				if (goalChoice == JOptionPane.OK_OPTION) {
					try {
						winValue = Integer.parseInt(winValueInput.getText());
						winningValue = winValue;
						gameLogic.updateWinValue(winningValue);
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(exit, "Error: Win value must be an integer.");
						return;
					}
					try {
						if (winValue <= 1)
							throw new IllegalArgumentException("Error: Win value must be greater than 1.");
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(exit, ex);
						return;
					}
					try {
						int win = winValue;
						if (win % 2 == 0) {
							while(win != 2)
								win /= 2;
						}
						if (win != 2)
							throw new IllegalArgumentException("Error: Win value must be a power of 2.");
					} catch (IllegalArgumentException ex) {
						JOptionPane.showMessageDialog(exit, ex);
						return;
					}
					updateBoard();
				}
			}
		}
	}
	
	public void inputDialogPane() {
		int rows = 0;
		int cols = 0;
		int winValue = 0;
		
		JTextField rowInput = new JTextField();
		JTextField colInput = new JTextField();
		JTextField winValueInput = new JTextField();
		
		rowInput.setText("4");
		colInput.setText("4");
		winValueInput.setText("16");
		
		Object[] textDisplay = {"Rows: ", rowInput, "Columns: ", colInput, "Win Value: ", winValueInput};
		int choice = JOptionPane.showConfirmDialog(null, textDisplay, "Input Desired Values", JOptionPane.OK_CANCEL_OPTION);
		
		if (choice == JOptionPane.CANCEL_OPTION)
			System.exit(0);
		else if (choice == JOptionPane.CLOSED_OPTION)
			System.exit(0);
		else if (choice == JOptionPane.OK_OPTION) {
			try {
				rows = Integer.parseInt(rowInput.getText());
				cols = Integer.parseInt(colInput.getText());
				winValue = Integer.parseInt(winValueInput.getText());
				nRows = rows;
				nCols = cols;
				winningValue = winValue;
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, "Error: All inputs must be integers.");
				return;
			}
			try {
				if (rows <= 1 || cols <= 1 || winValue <= 1)
					throw new IllegalArgumentException("Error: All inputs must be greater than 1.");
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e);
				return;
			}
			try {
				int win = winValue;
				if (win % 2 == 0) {
					while(win != 2)
						win /= 2;
				}
				if (win != 2)
					throw new IllegalArgumentException("Error: Win value must be a power of 2.");
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e);
				return;
			}
			
			gameLogic.resizeBoard(rows, cols, winValue);
		}
	}

	public void resizeBoard() {

		
		// Initialize the game logic
		nRows = 4;
		nCols = 4;
		winningValue = 16;
		gameLogic = new NumberGameArrayList();
		inputDialogPane();
		//gameLogic.resizeBoard(nRows, nCols, winningValue);

		// Update the GUI
		// Start with changing the panel size and creating a new
		// gamePanel
		setSize(new Dimension(103*(nCols), 115*(nRows)));
		gamePanel.setLayout(new GridLayout(nRows, nCols));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		// Initialize the game board GUI
		gameBoardUI = new JLabel[nRows][nCols];

		Font myTextFont = new Font(Font.SERIF, Font.BOLD, 40);
		for (int k = 0; k < nRows; k++) {
			for (int m = 0; m < nCols; m++) {
				gameBoardUI[k][m] = new JLabel();
				gameBoardUI[k][m].setFont(myTextFont);
				gameBoardUI[k][m].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				gameBoardUI[k][m].setPreferredSize(new Dimension(100, 100));
				gameBoardUI[k][m].setMinimumSize(new Dimension(100, 100));

				//						c.fill = GridBagConstraints.HORIZONTAL;
				// Indicate which column
				c.gridx = m;
				// Indicate which row
				c.gridy = k;
				// Indicate number of columns to span
				c.gridwidth = 1;
				// Specify custom height
				//						c.ipady = 40;
				gamePanel.add(gameBoardUI[k][m]);
			}
		}

		gameLogic.reset();
		updateBoard();
	}
}
