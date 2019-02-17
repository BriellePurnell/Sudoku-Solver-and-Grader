package sudoku;

import java.util.*;


public class Grid 
{
	private int[][] values;
	

	//
	// DON'T CHANGE THIS.
	//
	// Constructs a Grid instance from a string[] as provided by TestGridSupplier.
	// See TestGridSupplier for examples of input.
	// Dots in input strings represent 0s in values[][].
	//
	public Grid(String[] rows)
	{
		values = new int[9][9];
		for (int j=0; j<9; j++)
		{
			String row = rows[j];
			char[] charray = row.toCharArray();
			for (int i=0; i<9; i++)
			{
				char ch = charray[i];
				if (ch != '.')
					values[j][i] = ch - '0';
			}
		}
	}
	
	
	//
	// DON'T CHANGE THIS.
	//
	public String toString()
	{
		String s = "";
		for (int j=0; j<9; j++)
		{
			for (int i=0; i<9; i++)
			{
				int n = values[j][i];
				if (n == 0)
					s += '.';
				else
					s += (char)('0' + n);
			}
			s += "\n";
		}
		return s;
	}


	//
	// Copy ctor. Duplicates its source. You’ll call this 9 times in next9Grids.
	//
	Grid(Grid src)
	{
		values = new int[9][9];
		for (int j=0; j<9; j++)
			for (int i=0; i<9; i++)
				values[j][i] = src.values[j][i];
	}
	
	
	//
	// This comment is kept here for future reference.
	//
	//
	// Finds an empty member of values[][]. Returns an array list of 9 grids that look like the current grid,
	// except the empty member contains 1, 2, 3 .... 9. Returns null if the current grid is full. Don’t change
	// “this” grid. Build 9 new grids.
	// 
	//
	// Example: if this grid = 1........
	//                         .........
	//                         .........
	//                         .........
	//                         .........
	//                         .........
	//                         .........
	//                         .........
	//                         .........
	//
	// Then the returned array list would contain:
	//
	// 11.......          12.......          13.......          14.......    and so on     19.......
	// .........          .........          .........          .........                  .........
	// .........          .........          .........          .........                  .........
	// .........          .........          .........          .........                  .........
	// .........          .........          .........          .........                  .........
	// .........          .........          .........          .........                  .........
	// .........          .........          .........          .........                  .........
	// .........          .........          .........          .........                  .........
	// .........          .........          .........          .........                  .........
	//
	public ArrayList<Grid> next9Grids()
	{
		int xOfNextEmptyCell;
		int yOfNextEmptyCell;
		
		ArrayList<Grid> grids = new ArrayList<Grid>();
		
		// Find x,y of an empty cell.
		boolean found = false;
		for(int j = 0; j < 9; j++) // iterates through each row
		{ 
		    for(int i = 0; i < 9; i++) // iterates through each col
		    { 
		        if (!found && values[j][i] == 0)// if cell is empty
		        { 
		        	found = true;
		        	xOfNextEmptyCell = i;
		        	yOfNextEmptyCell = j;
		        	// Makes 9 new grids. Add them to grids.
		            for(int next = 1; next <= 9; next++)
		            {
		            	// More grids are made
		                Grid temp9 = new Grid(this);
		                // Changes value of empty space to 1 through 9
		                temp9.setValue(xOfNextEmptyCell, yOfNextEmptyCell, next);
		                // Add the grid to the ArrayList
		                grids.add(temp9);
		            }
		        }
		    }
		}
		return grids;
	}
	
	public void setValue(int x, int y, int value)
	{
		this.values[y][x] = value;
	}
	
	//
	// Returns true if this grid is legal. A grid is legal if no row, column, or
	// 3x3 block contains a repeated 1, 2, 3, 4, 5, 6, 7, 8, or 9.
	//
	

	public boolean isLegal()
	{
		// Check every row. If you find an illegal row, return false.
		for (int row = 0; row < values[0].length; row++)
		{
			if(!rowIsLegal(row))
			{
				return false;
			}
		}

		// Check every column. If you find an illegal column, return false.
		for(int column = 0; column < values[0].length; column++)
		{
			if(!columnIsLegal(column))
			{
				return false;
			}
		}

		// Check every block. If you find an illegal block, return false.
		int[][]block = new int[3][3];
		// Grid is divided into blocks
		for(int startOfBlockRow = 0; startOfBlockRow < values[0].length; startOfBlockRow += 3)
		{
			for(int startOfBlockCol = 0; startOfBlockCol < values[0].length; startOfBlockCol += 3)
			{
				// Iterates through each square in the block
				for(int blockRow = startOfBlockRow; blockRow < 3; blockRow++)
				{
					for (int blockCol = startOfBlockCol; blockCol < 3; blockCol++)
					{
						block[blockRow][blockCol] = values[startOfBlockRow + blockRow][startOfBlockCol + blockCol];
					}
				}
				// checks if the block is legal
				if(!blockIsLegal(block))
				{
					return false;
				}
			}
		}
		//If everything is legal
		return true;
	}

	/*
	 * rowIsLegal, columnIsLegal, and blockIsLegal methods below
	 */
	
	public boolean rowIsLegal(int row)
	{
		int[]rowValues = values[row];
		return !containsRepeat(rowValues);
	}
	
	public boolean columnIsLegal(int column)
	{
		int[] columnValues = new int[values[0].length];
		for(int row = 0; row < values[0].length; row++)
		{
			columnValues[row] = values[row][column];
		}
		return !containsRepeat(columnValues);
	}
	
	public boolean blockIsLegal(int[][] block)
	{
		int[] blockValues = new int[9];
		int blockValueIndex = 0;
		for(int j = 0; j < block[0].length; j++)
		{
			for(int i = 0; i < block[0].length;i++)
			{
				blockValues[blockValueIndex] = block[j][i];
				blockValueIndex++;
			}
		}
		return !containsRepeat(blockValues);
	}
	
	// Checks for repeats if the value is non-zero
	public boolean containsRepeat(int[] possibleDuplicates)
	{
		int[] sortedPossibleDuplicates = possibleDuplicates.clone();
		Arrays.sort(sortedPossibleDuplicates);
		for(int i = 0; i < 8; i++)
		{
			if((sortedPossibleDuplicates[i] != 0) && (sortedPossibleDuplicates[i] == sortedPossibleDuplicates[i+1])) 
			{
				return true;
			}
		}
		return false;
	}
	
	//
	// Returns true if every cell member of values[][] is a digit from 1-9.
	//
	public boolean isFull()
	{
		for(int j = 0; j < 9; j++)
		{
			for(int i = 0; i < 9; i++)
			{
				if (values[j][i] == 0)
				{
					return false;
				}
			}
		}
		return true;
	}
	//
	// Returns true if x is a Grid and, for every (i,j), 
	// x.values[i][j] == this.values[i][j].
	//
	public boolean equals(Object x)
	{
		Grid that = (Grid) x;
		for(int j = 0; j < 9; j++)
		{
			for(int i = 0; i < 9; i++)
			{
				if (this.values[j][i] != that.values[j][i])
				{
					return false;
				}
			}
		}
		return true;
	}
}
