import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Sudoku {
	
	
	private SudokuSquare[][] sudokuSquares;			// Declares data member to store all the squares that form the Sudoku
													// as a bi-dimensional list
	
	public Sudoku() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void Sudoku()
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called as a constructor when an object of type Sudoku is instantiated.
		//							It instantiates sudokuSquares as and empty list with 9 lists of size 9
		//							that is then filled with a total of 81 squares that form the grid of the Sudoku.
		//
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-22		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		sudokuSquares = new SudokuSquare[9][9];								// Instantiates the data member "sudokuSquares" as an empty bi-dimensional
																			// array of 9 x 9
		
		int row, column;													// Instantiates integers that represent the current row and column to control
																			// the iterations of the loop
		
		for (row = 0; row <9; row ++) {										// Does a double loop that goes through all the rows and columns
			for(column = 0; column <9; column ++) {							// of the Sudoku and assigns each space a new object of type SudokuSquare
				sudokuSquares[row][column]= new SudokuSquare(row, column);	
			}
		}
		
	}
	
	
	void LoadSudokuFile(int sudokuFile) throws Exception {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	ArrayList<String>  LoadSaveFile()
		//
		// Method parameters	:	none
		//
		// Method return		:	ArrayList<String> 
		//
		// Synopsis				:   This method is called to read the files that store different Sudoku solutions.
		//							It goes through 9 lines of the file and makes sure that the numbers gotten are valid
		//							for the Sudoku so they can be stored as values of the different sudoku squares.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-22		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		String[] rowNumbers = new String[9];												// Instantiates list of strings to store numbers that are found
																							// in a line of the file as part of a row of the Sudoku
		
		int currentNumber;																	// Instantiates variable to check the integer value of the numbers
																							// that are written in the file
		int lineCounter, numberCounter;														// Instantiates ints to control the loops for each line of the file
																							// and each number within these lines
		
		try {
			FileReader inputFile = new FileReader("Data/Sudoku"+sudokuFile+".txt");					// create object to physical file based on the file that
																									// needs to be opened
			
			BufferedReader bufferedInputFile = new BufferedReader(inputFile);				// create buffered input reader to file
			
			for(lineCounter = 0; lineCounter <9; lineCounter ++) {							// Goes through 9 lines of the file corresponding to the 9 rows of
																							// a Sudoku
					
				rowNumbers = bufferedInputFile.readLine().split(" ");						// Splits the line into the different numbers written in it
				
				for (numberCounter = 0; numberCounter<9; numberCounter++) {					// For each number written in the line
					currentNumber = Integer.parseInt(rowNumbers[numberCounter]);			// takes its integer value
					if(currentNumber<1 || currentNumber>9)									// and makes sure is between 1 and 9
					{
						throw new Exception("Invalid numbers found in the sudoku file");	// else, throws an exception
					}
					
					sudokuSquares[lineCounter][numberCounter].setValue(currentNumber);		// if the number is valid, is set as the value of the current
																							// square of the Sudoku
					
				}
				
			}
			
			bufferedInputFile.close();														// close the buffered input reader
			inputFile.close();																// close the physical file
			
			
		} catch (FileNotFoundException e) {											// Throws an exception if the file is not found
		
			e.printStackTrace();
			
		} catch (IOException e) {													// Catches any other Exception
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}
	}



	public boolean[][] CheckGrid() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	 boolean[][] CheckGrid()
		//
		// Method parameters	:	none
		//
		// Method return		:	boolean[][]
		//
		// Synopsis				:   This method is called to find all the errors that are present in the Sudoku currently
		//							being shown.
		//							It first calls a method that distributes the squares of the Sudoku into the different areas
		//							that it can be organized as (rows, coluns and blocks) and then goes through each section
		//							checking if any number appears more than once, if it does, all squares in the area get 
		//							a +1 on their error counter.
		//							At the end. the method returns a bi dimensional array that indicates which areas present
		//							errors.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-22		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		
		int area, list, number, errorCounter;								//Defines integers that help manage the iterations
																			//of the different loops
		
		int[] numberCheck;													//Defines array that is  used to check how many times
																			//the numbers from 1 to 9 repeat in a given section
		
		int value;															//Instantiates int to store the values present in each square
		
		boolean[][] errorAreas = new boolean[3][9];							//Instantiates bi-dimensional array to store which areas
																			// have errors
		
		numberCheck = new int[9];											//instantiates array that counts how many times a number appears in a section
		
		SudokuSquare[][][] sudokuAreas = new SudokuSquare[3][9][9];			// Instantiates array that will contain all the different sections
																			// of the Sudoku that need to be checked
	
		sudokuAreas = DivideNumbersByArea();								// Calls method to get the squares of the Sudoku divided by the
																			// areas they form a part of
		
		for(area = 0; area<3; area++) {													//Goes through the 3 types of areas (row, columns and blocks)
			
			for(list = 0; list<9; list++) {												//Each area contains 9 lists
				for(number = 0; number<9; number ++) {									// and each list contains 9 numbers
					
					value = sudokuAreas[area][list][number].getValue() - 1;				//gets the number currently present in the square and substracts 1
					numberCheck[value]++;												//so its counter in the checking list its increased

					if(numberCheck[value]>1) {											//if the number has appeared more than once in the section
						errorAreas[area][list] = true;									//indicates that the area has an error present


						for(errorCounter = 0; errorCounter < 9; errorCounter++) {		// and goes through all the squares that are part of the area
							sudokuAreas[area][list][errorCounter].AddError();			// so their error counter gets increased by 1
						}
						
						break;															// and stops checking the section to go onto the next one
					}
				}
				Arrays.fill(numberCheck, 0);		// After checking a section, fills the array that counts
			}										// the numbers with 0s again to check the next area
		}
		
		return errorAreas;							// And returns the array that contains which area have errors
		
	}
	
	private SudokuSquare[][][] DivideNumbersByArea() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	SudokuSquare[][][] DivideNumbersByArea()
		//
		// Method parameters	:	none
		//
		// Method return		:	SudokuSquare[][][]
		//
		// Synopsis				:   This method is called to divide the squares of the Sudoku into the different sections
		//							that need to be analyzed to know whether there are any mistakes present or not,
		//							returning an array that that contains the different sections and their 9 corresponding
		//							squares.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-22		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

				
		int currentRow, currentColumn;													//Defines integers that help manage the iterations
																						//of the different loops
		
		SudokuSquare currentSquare;														//Defines variable to reference the square currently
																						// being used
		
		
		SudokuSquare[][]rows = new SudokuSquare[9][9];									//Instantiates arrays to organize the different
		SudokuSquare[][]columns = new SudokuSquare[9][9];								// rows, columns and blocks of the Sudoku
		SudokuSquare[][]blocks = new SudokuSquare[9][9];
		
		SudokuSquare[][][] allSections = new SudokuSquare[3][9][9];						//Instantiates array to store all the sections together
		
		for (currentRow = 0; currentRow <9; currentRow ++) {							//Goes through all the rows and columns of the Sudoku
			for(currentColumn = 0; currentColumn <9; currentColumn ++) {
				
				currentSquare = sudokuSquares[currentRow][currentColumn];				//takes the square on the current position of the grid
				currentSquare.setErrorCounter(0);										// sets its error counter to 0 so to make sure a higher 
																						// number doesn't carry over from previous checks
				
				rows[currentRow][currentColumn] = currentSquare;						//adds the square to its corresponding row, column and block
				columns[currentColumn][currentRow] = currentSquare;
				int positionInBlock = (currentRow%3) *3 + (currentColumn%3);			//defining its position on the block based on its row and column
				blocks[sudokuSquares[currentRow][currentColumn].getBlock()][positionInBlock]= currentSquare;
			}
		}
		
		allSections[0] = rows;								//integrates the different sections into the larger array
		allSections[1] = columns;
		allSections[2] = blocks;
		
		return allSections;									// and returns all the section together
	}

	
	

	public SudokuSquare[][] getSudokuSquares() {							//Getter for the data member "sudokuSquares"
		return sudokuSquares;												//returns the object's current bi-dimensional list of SudokuSquares
	}


	public void setSudokuSquares(SudokuSquare[][] sudokuSquares) {			// Setter for the data member "sudokuSquares"
		this.sudokuSquares = sudokuSquares;									// stores in the object's data member "sudokuSquares"
	}																		//  bi-dimensional list given
	
	

}
