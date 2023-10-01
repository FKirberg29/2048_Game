package game_2048;


import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class NumberGameArrayList implements NumberSlider {

	ArrayList<Cell> gameBoard = new ArrayList<Cell>();
	private Stack<ArrayList<Cell>> undoStack = new Stack<ArrayList<Cell>>();
	private Stack<Integer> scoreStack = new Stack<Integer>();
	boolean tileMoved = false;
	int nRows = 0;
	int nCols = 0;
	int winValue = 0;
	int score = 0;
	int slides = 0;
	int games = -1;
	
	public void updateWinValue(int newWinValue) {
		this.winValue = newWinValue;
	}

	@Override
	public void resizeBoard(int height, int width, int winningValue) {
		this.nRows = height;
		this.nCols = width;
		this.winValue = winningValue;
		int index = 0;
		
		slides = 0;
		
		for (int row = 0; row < width; row++) {
			for (int col = 0; col < height; col++) {
				gameBoard.add(index, null);
				index++;
			}
		}
		
		
		placeRandomValue();
		placeRandomValue();
	}
	
	/**
	 * 
	 */
	@Override
	public void reset() {
		int index = 0;
		
		slides = 0;
		
		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nCols; col++) {
				gameBoard.set(index, null);
				index++;
			}
		}
		
		games++;
		GUI1024Panel.newGames.setText(String.valueOf(games) + " ");
		placeRandomValue();
		placeRandomValue();
	}
	
	/**
	 * Created a new grid by copying the current grid's values, for testing purposes.
	 */
	@Override
	public void setValues(int[][] ref) {
		int r;
		int c;
		int v;
		int index = 0;
		
		for (int row = 0; row < ref.length; row++) {
			for (int col = 0; col < ref[row].length; col++) {
				r = row;
				c = col;
				v = ref[row][col];
				gameBoard.get(index).setRow(r);
				gameBoard.get(index).setColumn(c);
				gameBoard.get(index).setValue(v);
				index++;
			}
		}
	}

	public int randomValue() {
		Random rand = new Random();
		int probability = rand.nextInt(5);
		int randomValue = 0;
		
		if (probability >= 0 && probability <= 1)
			randomValue = 1;
		else if (probability >= 2 && probability <= 3)
			randomValue = 2;
		else if (probability == 4)
			randomValue = 4;
		
		return randomValue;
	}
	
	@Override
	public Cell placeRandomValue() {
		if (getEmptyTiles().size() != 0) {
			Random rand = new Random();
			int index = rand.nextInt(getEmptyTiles().size());
			int gameBoardIndex = 0;

			Cell c = new Cell(getEmptyTiles().get(index).getRow(), getEmptyTiles().get(index).getColumn(), randomValue());
			gameBoardIndex = c.getCellIndexAt(nRows, c.getRow(), c.getColumn());
			gameBoard.add(gameBoardIndex, c);
			gameBoard.remove(gameBoardIndex + 1);
			return c;
		}
		else
			return null;
	}
	
	public boolean canMove() {
		if(getEmptyTiles().size() != 0)
			return true;
		
		
		return false;
	}

	@Override
	public boolean slide(SlideDirection dir) {
		boolean moved = false;
		ArrayList<Cell> gameBoardCopy = new ArrayList<Cell>(gameBoard);
		int scoreCopy = score;
		
		if (dir == SlideDirection.UP) {
			for (int i = 0; i < nRows; i++)
				slideUp();
			mergeUp();
			for (int i = 0; i < nRows; i++)
				slideUp();
			moved = tileMoved;
			if (moved)
				slides++;
		}
		else if (dir == SlideDirection.DOWN) {
			for (int i = 0; i < nRows; i++)
				slideDown();
			mergeDown();
			for (int i = 0; i < nRows; i++)
				slideDown();
			moved = tileMoved;
			if (moved)
				slides++;
		}
		else if (dir == SlideDirection.LEFT) {
			for (int i = 0; i < nCols; i++)
				slideLeft();
			mergeLeft();
			for (int i = 0; i < nCols; i++)
				slideLeft();
			moved = tileMoved;
			if (moved)
				slides++;
		}
		else if (dir == SlideDirection.RIGHT) {
			for (int i = 0; i < nCols; i++)
				slideRight();
			mergeRight();
			for (int i = 0; i < nCols; i++)
				slideRight();
			moved = tileMoved;
			if (moved)
				slides++;
		}
		else
			moved = false;
		
		if (moved) {
			placeRandomValue();
			undoStack.add(gameBoardCopy);
			scoreStack.add(scoreCopy);
		}
		tileMoved = false;
		GUI1024Panel.numSlides.setText(String.valueOf(slides) + " ");
		return moved;
	}
	
	public void slideUp() {
		int index = 0;
		int cellBelowIndex = 0;
		
		for (int row = 0; row < nRows - 1; row++) {
			for (int col = 0; col < nCols; col++) {
				cellBelowIndex = index + this.nRows;
				if (gameBoard.get(index) == null && gameBoard.get(cellBelowIndex) != null) {
					Cell c = new Cell(row, col, gameBoard.get(cellBelowIndex).getValue());
					gameBoard.set(index, c);
					gameBoard.set(cellBelowIndex, null);
					tileMoved = true;
				}
				index++;
				cellBelowIndex = 0;
			}
		}
	}
	
	public void mergeUp() {
		int index = 0;
		int cellBelowIndex = 0;

		for (int row = 0; row < nRows - 1; row++) {
			for (int col = 0; col < nCols; col++) {
				cellBelowIndex = index + this.nRows;
				if (gameBoard.get(index) != null && gameBoard.get(cellBelowIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellBelowIndex).getValue())) {
					Cell c = new Cell(row, col, gameBoard.get(cellBelowIndex).getValue() * 2);
					gameBoard.set(index, c);
					gameBoard.set(cellBelowIndex, null);
					tileMoved = true;
				}
				index++;
				cellBelowIndex = 0;
			}
		}
	}
	
	public void slideDown() {
		int index = gameBoard.size() - 1;
		int cellAboveIndex = 0;
		
		for (int row = nRows - 1; row > 0; row--) {
			for (int col = nCols - 1; col >= 0; col--) {
				cellAboveIndex = index - this.nRows;
				if (gameBoard.get(index) == null && gameBoard.get(cellAboveIndex) != null) {
					Cell c = new Cell(row, col, gameBoard.get(cellAboveIndex).getValue());
					gameBoard.set(index, c);
					gameBoard.set(cellAboveIndex, null);
					tileMoved = true;
				}
				index--;
				cellAboveIndex = 0;
			}
		}
	}
	
	public void mergeDown() {
		int index = gameBoard.size() - 1;
		int cellAboveIndex = 0;
		
		for (int row = nRows - 1; row > 0; row--) {
			for (int col = nCols - 1; col >= 0; col--) {
				cellAboveIndex = index - this.nRows;
				if (gameBoard.get(index) != null && gameBoard.get(cellAboveIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellAboveIndex).getValue())) {
					Cell c = new Cell(row, col, gameBoard.get(cellAboveIndex).getValue() * 2);
					gameBoard.set(index, c);
					gameBoard.set(cellAboveIndex, null);
					tileMoved = true;
				}
				index--;
				cellAboveIndex = 0;
			}
		}
	}
	
	public void slideLeft() {
		int index = 0;
		int cellRightOfIndex = 0;
		
		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nCols - 1; col++) {
				index = getIndexAt(row, col);
				cellRightOfIndex = index + 1;
				if (gameBoard.get(index) == null && gameBoard.get(cellRightOfIndex) != null) {
					Cell c = new Cell(row, col, gameBoard.get(cellRightOfIndex).getValue());
					gameBoard.set(index, c);
					gameBoard.set(cellRightOfIndex, null);
					tileMoved = true;
				}
				cellRightOfIndex = 0;
			}
		}
	}
	
	public void mergeLeft() {
		int index = 0;
		int cellRightOfIndex = 0;

		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nCols - 1; col++) {
				index = getIndexAt(row, col);
				cellRightOfIndex = index + 1;
				if (gameBoard.get(index) != null && gameBoard.get(cellRightOfIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellRightOfIndex).getValue())) {
					Cell c = new Cell(row, col, gameBoard.get(cellRightOfIndex).getValue() * 2);
					gameBoard.set(index, c);
					gameBoard.set(cellRightOfIndex, null);
					tileMoved = true;
				}
				cellRightOfIndex = 0;
			}
		}
	}
	
	public void slideRight() {
		int index = gameBoard.size() - 1;
		int cellLeftOfIndex = 0;
		
		for (int row = nRows - 1; row >= 0; row--) {
			for (int col = nCols - 1; col >= 1; col--) {
				index = getIndexAt(row, col);
				cellLeftOfIndex = index - 1;
				if (gameBoard.get(index) == null && gameBoard.get(cellLeftOfIndex) != null) {
					Cell c = new Cell(row, col, gameBoard.get(cellLeftOfIndex).getValue());
					gameBoard.set(index, c);
					gameBoard.set(cellLeftOfIndex, null);
					tileMoved = true;
				}
				cellLeftOfIndex = 0;
			}
		}
	}
	
	public void mergeRight() {
		int index = gameBoard.size() - 1;
		int cellLeftOfIndex = 0;
		
		for (int row = nRows - 1; row >= 0; row--) {
			for (int col = nCols - 1; col >= 1; col--) {
				index = getIndexAt(row, col);
				cellLeftOfIndex = index - 1;
				if (gameBoard.get(index) != null && gameBoard.get(cellLeftOfIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellLeftOfIndex).getValue())) {
					Cell c = new Cell(row, col, gameBoard.get(cellLeftOfIndex).getValue() * 2);
					gameBoard.set(index, c);
					gameBoard.set(cellLeftOfIndex, null);
					tileMoved = true;
				}
				cellLeftOfIndex = 0;
			}
		}
	}
	
	
	/**
	 *
	 */
	@Override
	public ArrayList<Cell> getNonEmptyTiles() {
		ArrayList<Cell> nonEmptyTileArrayList = new ArrayList<Cell>();
		int index = 0;
		
		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nCols; col++) {
				if (gameBoard.get(index) != null) {
					Cell c = new Cell(gameBoard.get(index).getRow(), gameBoard.get(index).getColumn(), gameBoard.get(index).getValue());
					nonEmptyTileArrayList.add(c);
				}
				index++;
			}
		}
		return nonEmptyTileArrayList;
	}
	
	/**
	 *
	 */
	public ArrayList<Cell> getEmptyTiles() {
		ArrayList<Cell> emptyTileArrayList = new ArrayList<Cell>();
		int index = 0;
		
		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nCols; col++) {
				if (gameBoard.get(index) == null) {
					Cell c = new Cell(row, col, 0);
					emptyTileArrayList.add(c);
				}
				index++;
			}
		}
		return emptyTileArrayList;
	}
	
	/**
	 * Return the current state of the game.
	 */
	@Override
	public GameStatus getStatus() {
		for(int i = 0; i < gameBoard.size(); i++) {
			if(gameBoard.get(i) != null) {
				if (gameBoard.get(i).getValue() == this.winValue) {
					return GameStatus.USER_WON;
				}
			}
		}
		if (isMovePossible())
			return GameStatus.IN_PROGRESS;
		else if (!isMovePossible())
			return GameStatus.USER_LOST;
		return null;
	}
	
	public boolean isMovePossible() {
		boolean isMovePossible = false;
		int index = 0;
		int cellAboveIndex = 0;
		int cellBelowIndex = 0;
		int cellRightOfIndex = 0;
		int cellLeftOfIndex = 0;


		if (getEmptyTiles().size() != 0)
			return true;

		for (int row = 0; row < nRows; row++) {
			for (int col = 0; col < nCols; col++) {
				index = getIndexAt(row, col);
				cellAboveIndex = getIndexAt(row - 1, col);
				cellBelowIndex = getIndexAt(row + 1, col);
				cellRightOfIndex = getIndexAt(row, col + 1);
				cellLeftOfIndex = getIndexAt(row, col - 1);

				if (cellAboveIndex >= 0 && cellAboveIndex <= gameBoard.size()) {
					if (gameBoard.get(index) != null && gameBoard.get(cellAboveIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellAboveIndex).getValue()))
						isMovePossible = true;
				}
				else if (cellBelowIndex >= 0 && cellBelowIndex <= gameBoard.size()) {
					if (gameBoard.get(index) != null && gameBoard.get(cellBelowIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellBelowIndex).getValue()))
						isMovePossible = true;
				}
				else if (cellRightOfIndex >= 0 && cellRightOfIndex <= gameBoard.size()) {
					if (gameBoard.get(index) != null && gameBoard.get(cellRightOfIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellRightOfIndex).getValue()))
						isMovePossible = true;
				}
				else if (cellLeftOfIndex >= 0 && cellLeftOfIndex <= gameBoard.size()) {
					if (gameBoard.get(index) != null && gameBoard.get(cellLeftOfIndex) != null && (gameBoard.get(index).getValue() == gameBoard.get(cellLeftOfIndex).getValue()))
						isMovePossible = true;
				}
			}
		}
		return isMovePossible;
	}
	
	/**
	 * Undo the most recent action by returning the board to the previous state. Calling this multiple
	 * times would eventually restore the games initial state. Attempting to restore beyond the
	 * games initial state will throw an IllegalStateException.
	 */
	@Override
	public void undo() {
		if (undoStack.size() < 1)
			throw new IllegalStateException();
		
		gameBoard = undoStack.pop();
		score = scoreStack.pop();
	}
	
	private int getIndexAt(int row, int col) {
		int index = nRows * row + col;
		return index;
	}
}
