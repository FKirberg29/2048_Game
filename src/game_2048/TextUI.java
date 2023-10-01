package game_2048;

import java.util.Scanner;

public class TextUI {
    private NumberSlider game;
    private int[][] grid;
    private static int CELL_WIDTH = 3;
    private static String NUM_FORMAT, BLANK_FORMAT;
    private Scanner inp;

    public TextUI() {
        game = new NumberGameArrayList();

        if (game == null) {
            System.err.println ("*---------------------------------------------*");
            System.err.println ("| 										       |");
            System.err.println ("|  										   |");
            System.err.println ("*---------------------------------------------*");
            System.exit(0xE0);
        }
        game.resizeBoard(4, 4, 64);
        grid = new int[4][4];

        /* Set the string to %4d */
        NUM_FORMAT = String.format("%%%dd", CELL_WIDTH + 1);

        /* Set the string to %4s, but without using String.format() */
        BLANK_FORMAT = "%" + (CELL_WIDTH + 1) + "s";
        inp = new Scanner(System.in);
    }

    private void renderBoard() {
        /* reset all the 2D array elements to ZERO */
        for (int k = 0; k < grid.length; k++)
            for (int m = 0; m < grid[k].length; m++)
                grid[k][m] = 0;

        
        for (int i = 0; i < game.getNonEmptyTiles().size(); i++)
        	grid[game.getNonEmptyTiles().get(i).getRow()][game.getNonEmptyTiles().get(i).getColumn()] = game.getNonEmptyTiles().get(i).getValue();

        /* Print the 2D array using dots and numbers */
        for (int k = 0; k < grid.length; k++) {
            for (int m = 0; m < grid[k].length; m++)
                if (grid[k][m] == 0)
                    System.out.printf (BLANK_FORMAT, ".");
                else
                    System.out.printf (NUM_FORMAT, grid[k][m]);
            System.out.println();
        }
    }

    public void playLoop() {
        /* Place the first two random tiles */
//        game.placeRandomValue();
//        game.placeRandomValue();
        renderBoard();
        char input;
        
        
        do {
        	System.out.print ("Slide direction (W, A, S, D), " +
                    "[U]ndo or [Q]uit? ");
        	input = inp.next().trim().toUpperCase().charAt(0);
        	
        	if (input == 'W')
        		game.slide(SlideDirection.UP);
        	else if (input == 'A')
        		game.slide(SlideDirection.LEFT);
        	else if (input == 'S')
        		game.slide(SlideDirection.DOWN);
        	else if (input == 'D')
        		game.slide(SlideDirection.RIGHT);
        	else if (input == 'U') {
        		try {
        			game.undo();
        		}
        		catch (IllegalStateException e){
        			System.err.println("Can't undo beyond the first move.");
        		}
        	}
        	
        	renderBoard();
        } while (input != 'Q' && game.getStatus() == GameStatus.IN_PROGRESS);

        /* Almost done.... */
        switch (game.getStatus()) {
            case IN_PROGRESS:
                System.out.println ("Thanks for playing!");
                break;
            case USER_WON:
                System.out.println ("Congratulation!");
                break;
            case USER_LOST:
                System.out.println ("Sorry....!");
                break;

        }
    }

    public static void main(String[] arg) {
        TextUI t = new TextUI();
        t.playLoop();
    }
}



