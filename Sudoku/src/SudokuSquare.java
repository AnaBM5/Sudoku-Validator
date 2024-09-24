import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class SudokuSquare extends JTextField{	// Class inherits from JTextField so it can be implemented as part of the UI

	private int value;							// Declares data member to store the number that is entered as part of the
												// solution to the Sudoku
	
	private int row;							// Declares data member to store the row in which the square is positioned
	private int column;							// Declares data member to store the column in which the square is positioned
	private int block;							// Declares data member to store the block in which the square is positioned
	
	private int errorCounter;					// Declares data member to store how many errors overlap in the square
	
	public SudokuSquare(int row, int column) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	SudokuSquare(int row, int column)
		//
		// Method parameters	:	row -			used to define the data member "row" of the object
		//
		//							column -		used to define the data member  "column" of the object
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called as a constructor when an object of type SudokuSquare is instantiated.
		//							It defines the row and column data members of the object based on the parameters and 
		//							calls a method to define the value of the block data member, then it defines the value in
		//							the square as -1 as no file has been loaded yet and defines the errorCount as 0.
		//							Finally, it defines all the visual attributes of the JTextField and makes it so its text
		//							can't be edited yet.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-22		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		this.row = row;														// Instantiates the data member "row" as the value passed to the constructor
		this.column = column;												// Instantiates the data member "column" as the value passed to the constructor
		DefineBlock();														// Calls method to define the value of block now that row and column are defined
		

		this.value = -1;													// Instantiates data member "value" as -1 until a valid number is entered
		this.errorCounter = 0;												// Instantiates the data member "errorCounter" as 0 so that later it can be added upon
		
		setHorizontalAlignment(SwingConstants.CENTER);						//Sets the position, colour and font of the text inside
		setColumns(10);														// the JTextField as well as its background colour,
		setFont(new Font("Source Code Pro Semibold", Font.BOLD, 12));		// then it takes the border out and makes it so
		setBackground(Color.DARK_GRAY);										// the JTextField can't be edited until later in the 
		setForeground(Color.WHITE);											// program
		setBorder(null);
		setEditable(false);
		
	}
	
	public void DefineBlock() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void DefineBlock()
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called to define the value of the data member "block" based on the row 
		//							and column position of the object.
		//							
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-22		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		int rowPosition;							// Instantiates int to store the area in which the row is located
		int columnPosition;							// Instantiates int to store the area in which the column is located
		
		rowPosition = row/3;						// Divides the row and column of the object by 3 and truncates the number
		columnPosition = column/3;					// into a integer to know in what general area of the sudoku the current square
													// is located
		
		this.block = (rowPosition * 3) + columnPosition;		//Defines the data member block as the previously defined rowPosition times 3
																// plus the position of the column
		
		//Each block contains 9 squares from 3 different rows and columns
		//The value of the block based on its position on the Sudoku is the following
		// 0	1	 2
		// 3	4	 5
		// 6	7	 8
	}
	
	public void AddError() {							//Method that adds 1 to the error counter of the square
		this.errorCounter++;
	}

	public int getRow() {								//Getter for the data member "row"
		return row;										//returns the object's current row value
	}

	public void setRow(int row) {						// Setter for the data member "row"
		this.row = row;									// stores in the object's data member "row"
	}													// the number given

	public int getColumn() {							//Getter for the data member "column"
		return column;									//returns the object's current column value
	}

	public void setColumn(int column) {					// Setter for the data member "column"
		this.column = column;							// stores in the object's data member "column"
	}													// the number given

	public int getBlock() {								//Getter for the data member "block"
		return block;									//returns the object's current block value
	}

	public void setBlock(int block) {					// Setter for the data member "block"
		this.block = block;								// stores in the object's data member "block"
	}													// the number given

	public int getValue() {								//Getter for the data member "value"
		return value;									//returns the object's current value
	}

	public void setValue(int value) {					// Setter for the data member "value"
		this.value = value;								// stores in the object's data member "value"
	}													// the number given

	public int getErrorCounter() {						//Getter for the data member "errorCounter"
		return errorCounter;							//returns the object's current errorCounter value
	}

	public void setErrorCounter(int errorCounter) {		// Setter for the data member "errorCounter"
		this.errorCounter = errorCounter;				// stores in the object's data member "errorCounter"
	}													// the number given
	
	
}
