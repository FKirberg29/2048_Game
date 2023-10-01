package game_2048;

public class Cell implements Comparable<Cell> {
    private int row, column, value;

    
	public Cell()
    {
        this(0,0,0);
    }
    public Cell (int r, int c, int v)
    {
        row = r;
        column = c;
        value = v;
    }

    @Override
    public int compareTo (Cell other) {
        if (this.row < other.row) return -1;
        if (this.row > other.row) return +1;

        /* break the tie using column */
        if (this.column < other.column) return -1;
        if (this.column > other.column) return +1;

        return this.value - other.value;
    }
    
    /**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Return the index of the cell using the number of rows in the game board and the cell's row and column value.
	 * @param numOfRows the number of rows in game board
	 * @param row the row value of the cell
	 * @param col the column value of the cell
	 * @return the index value of the cell
	 */
	public int getCellIndexAt(int numOfRows, int row, int col) {
		int index = numOfRows * row + col;
		return index;
	}
}
