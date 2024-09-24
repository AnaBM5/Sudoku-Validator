import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SudokuApp extends JDialog {
	
	private Sudoku sudoku;			// Declares data member to manage the data of the Sudoku
	
	
	//Visual components that are needed as global to perform different methods
	
	private JPanel ErrorPanel;
	private JPanel NoErrorPanel;
	private JPanel WelcomePanel;
	private JButton btnCheckGrid;
	
	private JToggleButton btnGrid1;
	private JToggleButton btnGrid2;
	private JToggleButton btnGrid3;
	private JLabel lblErrorRows;
	private JLabel lblErrorColumns;
	private JLabel lblErrorBlocks;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SudokuApp dialog = new SudokuApp();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public SudokuApp() {
		
		sudoku = new Sudoku();					//Instantiates a new Sudoku to manage the numbers and call the necessary methods
		
		setBounds(100, 100, 850, 588);
		getContentPane().setLayout(null);
		
		JPanel sudokuPanel = new JPanel();
		sudokuPanel.setBackground(Color.BLACK);
		sudokuPanel.setBounds(34, 26, 500, 500);
		getContentPane().add(sudokuPanel);
		sudokuPanel.setLayout(null);
		
		JPanel verticalLine1 = new JPanel();
		verticalLine1.setBounds(170, 0, 5, 500);
		sudokuPanel.add(verticalLine1);
		verticalLine1.setBackground(new Color(0, 0, 0));
		verticalLine1.setLayout(null);
		
		JPanel verticalLine2 = new JPanel();
		verticalLine2.setBounds(329, 0, 5, 500);
		sudokuPanel.add(verticalLine2);
		verticalLine2.setLayout(null);
		verticalLine2.setBackground(new Color(0, 0, 0));
		
		JPanel horizontalLine1 = new JPanel();
		horizontalLine1.setLayout(null);
		horizontalLine1.setBackground(new Color(0, 0, 0));
		horizontalLine1.setBounds(0, 170, 500, 5);
		sudokuPanel.add(horizontalLine1);
		
		JPanel horizontalLine2 = new JPanel();
		horizontalLine2.setLayout(null);
		horizontalLine2.setBackground(new Color(0, 0, 0));
		horizontalLine2.setBounds(0, 329, 500, 5);
		sudokuPanel.add(horizontalLine2);
		
		JPanel numbersPanel = new JPanel();
		numbersPanel.setBackground(Color.GRAY);
		numbersPanel.setBounds(15, 15, 475, 475);
		sudokuPanel.add(numbersPanel);
		numbersPanel.setLayout(new GridLayout(9, 9, 5, 5));
		
		
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Synopsis				:   This section is used to integrate the sudokuSquares into the GUI by going through all of them
		//							and adding them to the panel that contains the Sudoku.
		//							It also instantiates their listeners so that the appropiate error handling can be made
		//							when the users changes the value of any given sudokuSquare by changing the text in the JTextField.
		//
		// References			:	Dackwe. (2013, January 31). Re: Select All on Focus in lots of JTextField. Meta Stack Overflow.
		//								https://stackoverflow.com/questions/7361291/select-all-on-focus-in-lots-of-jtextfield
		//
		//							Oracle. (2023). How to Use the Focus Subsystem.
		//								https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html
		//							
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-22		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	
		int row,column;														// Instantiates integers that represent the current row and column of
																			// the Sudoku to control the iterations of the loop
		
		SudokuSquare[][] sudokuSquares = sudoku.getSudokuSquares();			// Instantiates bi-dimensional list to reference all the squares of the Sudoku
		
		for (row = 0; row <9; row ++) {												//goes through all the rows and columns of the Sudoku
			for(column = 0; column <9; column ++) {
				
				SudokuSquare currentSquare = sudokuSquares[row][column];			// saves a reference for the current sudokuSquare in a variable
				
				currentSquare.addFocusListener(new FocusAdapter() {								// adds focus listener to know when the user interacts with the square
					@Override
					public void focusGained(FocusEvent e) {										// if the user selects a square, all of its text get highlighted
						currentSquare.select(0, currentSquare.getText().length());				// so that its easier to modify it
					}
					
					public void focusLost(FocusEvent e) {																	// If the user tries to go to another text field
						if(currentSquare.isEditable() && currentSquare.getText().equals("")) {								// while the square is blank
							JOptionPane.showMessageDialog(getContentPane(), "Error. Please don't leave any space blank");	// an error message is shown to the user
							currentSquare.requestFocus();																	// and the focus is put back on the blank square so that the
																															// user has to edit it
							btnCheckGrid.setEnabled(false);																	// Then the "Check Grid" button stops being enabled
																															// to avoid checking an incomplete Sudoku
						}
							
				}
				});
				
				
				
				currentSquare.addKeyListener(new KeyAdapter() {									// adds key listener to know if the user is changing the text of the square
					@Override
					public void keyReleased(KeyEvent e) {										
						
						if(currentSquare.isEditable()) {															//When the texField is edited
							int numberEntered;

							try {																					// takes the new text and tries to convert it
								numberEntered = Integer.parseInt(currentSquare.getText());							// into an integer

								if(numberEntered<1 || numberEntered > 9) 											// if the number is lower than 1 or higher than 9
									throw new Exception();															// throws an exception, interrupting the event

								sudoku.getSudokuSquares()[currentSquare.getRow()][currentSquare.getColumn()].setValue(numberEntered); //else, sets the input as the new value of the square
								btnCheckGrid.setEnabled(true);																		  // and lets the user validate the grid

							}catch (Exception error) {																					// If an error is caught
								JOptionPane.showMessageDialog(getContentPane(), "Invalid Input. Please enter another value");			// Shows message to the user
								currentSquare.setText("");																				// and empties the textField so a new value can be entered,
								btnCheckGrid.setEnabled(false);																			// then disables the button that lets the user 
							}																											// validate the sudoku
							
						}

					}
				});
			
				numbersPanel.add(currentSquare);						// adds the current square to the UI panel that contains all the numbers
			}
		}
		/////-----------------------------------------------------------------------////
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		messagePanel.setBounds(557, 26, 231, 263);
		getContentPane().add(messagePanel);
		messagePanel.setLayout(null);
		
		WelcomePanel = new JPanel();
		WelcomePanel.setLayout(null);
		WelcomePanel.setBackground(Color.BLACK);
		WelcomePanel.setBounds(10, 10, 211, 243);
		messagePanel.add(WelcomePanel);
		
		JLabel lblWelcome1 = new JLabel("Welcome to the Sudoku");
		lblWelcome1.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome1.setForeground(new Color(0, 255, 102));
		lblWelcome1.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblWelcome1.setBounds(10, 44, 191, 37);
		WelcomePanel.add(lblWelcome1);
		
		JLabel lblValidator = new JLabel("Validator");
		lblValidator.setVerticalAlignment(SwingConstants.TOP);
		lblValidator.setHorizontalAlignment(SwingConstants.CENTER);
		lblValidator.setForeground(new Color(0, 255, 102));
		lblValidator.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblValidator.setBounds(10, 77, 191, 37);
		WelcomePanel.add(lblValidator);
		
		JLabel lblPleaseSelectA = new JLabel("Please select a grid");
		lblPleaseSelectA.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseSelectA.setForeground(new Color(0, 255, 102));
		lblPleaseSelectA.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblPleaseSelectA.setBounds(10, 139, 191, 37);
		WelcomePanel.add(lblPleaseSelectA);
		
		JLabel lblAndThenPress = new JLabel("and then press Check");
		lblAndThenPress.setVerticalAlignment(SwingConstants.TOP);
		lblAndThenPress.setHorizontalAlignment(SwingConstants.CENTER);
		lblAndThenPress.setForeground(new Color(0, 255, 102));
		lblAndThenPress.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblAndThenPress.setBounds(10, 172, 191, 37);
		WelcomePanel.add(lblAndThenPress);
		
		ErrorPanel = new JPanel();
		ErrorPanel.setBackground(Color.BLACK);
		ErrorPanel.setBounds(10, 10, 211, 243);
		messagePanel.add(ErrorPanel);
		ErrorPanel.setLayout(null);
		
		JLabel lblError = new JLabel("ERRORS DETECTED IN");
		lblError.setForeground(Color.RED);
		lblError.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setBounds(10, 5, 191, 31);
		ErrorPanel.add(lblError);
		
		JLabel lblRows = new JLabel("ROWS:");
		lblRows.setForeground(new Color(204, 0, 51));
		lblRows.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblRows.setBounds(10, 39, 191, 13);
		ErrorPanel.add(lblRows);
		
		JLabel lblColumns = new JLabel("COLUMNS:");
		lblColumns.setForeground(new Color(204, 0, 51));
		lblColumns.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblColumns.setBounds(10, 87, 191, 13);
		ErrorPanel.add(lblColumns);
		
		JLabel lblBlocks = new JLabel("BLOCKS:");
		lblBlocks.setForeground(new Color(204, 0, 51));
		lblBlocks.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblBlocks.setBounds(10, 135, 191, 13);
		ErrorPanel.add(lblBlocks);
		
		lblErrorRows = new JLabel("1 2 3 4 5 6 7 8 9");
		lblErrorRows.setForeground(new Color(204, 0, 51));
		lblErrorRows.setFont(new Font("Source Code Pro Black", Font.PLAIN, 12));
		lblErrorRows.setBounds(20, 60, 181, 13);
		ErrorPanel.add(lblErrorRows);
		
		lblErrorColumns = new JLabel("1 2 3 4 5 6 7 8 9");
		lblErrorColumns.setForeground(new Color(204, 0, 51));
		lblErrorColumns.setFont(new Font("Source Code Pro Black", Font.PLAIN, 12));
		lblErrorColumns.setBounds(20, 108, 181, 13);
		ErrorPanel.add(lblErrorColumns);
		
		lblErrorBlocks = new JLabel("1 2 3 4 5 6 7 8 9");
		lblErrorBlocks.setForeground(new Color(204, 0, 51));
		lblErrorBlocks.setFont(new Font("Source Code Pro Black", Font.PLAIN, 12));
		lblErrorBlocks.setBounds(20, 156, 181, 13);
		ErrorPanel.add(lblErrorBlocks);
		
		JPanel ColorPanel = new JPanel();
		ColorPanel.setBackground(Color.DARK_GRAY);
		ColorPanel.setBounds(10, 188, 191, 45);
		ErrorPanel.add(ColorPanel);
		ColorPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 25, 10, 10);
		ColorPanel.add(panel);
		panel.setBackground(new Color(0, 204, 102));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.ORANGE);
		panel_1.setBounds(55, 25, 10, 10);
		ColorPanel.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 102, 0));
		panel_2.setBounds(100, 25, 10, 10);
		ColorPanel.add(panel_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(204, 0, 51));
		panel_3.setBounds(145, 25, 10, 10);
		ColorPanel.add(panel_3);
		
		JLabel lblErrors = new JLabel("# OF ERRORS IN A SQUARE");
		lblErrors.setForeground(Color.WHITE);
		lblErrors.setFont(new Font("Source Code Pro", Font.ITALIC, 12));
		lblErrors.setBounds(10, 2, 191, 13);
		ColorPanel.add(lblErrors);
		
		JLabel lblErrorBlocks_1 = new JLabel("0     1     2      3");
		lblErrorBlocks_1.setForeground(Color.LIGHT_GRAY);
		lblErrorBlocks_1.setFont(new Font("Source Code Pro Black", Font.PLAIN, 12));
		lblErrorBlocks_1.setBounds(27, 25, 160, 13);
		ColorPanel.add(lblErrorBlocks_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.LIGHT_GRAY);
		panel_4.setBounds(8, 184, 195, 52);
		ErrorPanel.add(panel_4);
		
		NoErrorPanel = new JPanel();
		NoErrorPanel.setBackground(new Color(0, 0, 0));
		NoErrorPanel.setLayout(null);
		NoErrorPanel.setBounds(10, 10, 211, 243);
		messagePanel.add(NoErrorPanel);
		
		JLabel lblNoError = new JLabel("THE SUDOKU IS VALID");
		lblNoError.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoError.setForeground(new Color(0, 255, 102));
		lblNoError.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblNoError.setBounds(10, 43, 191, 107);
		NoErrorPanel.add(lblNoError);
		
		JLabel lblNoErrorsWere = new JLabel("NO ERRORS WERE FOUND");
		lblNoErrorsWere.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoErrorsWere.setForeground(new Color(0, 255, 102));
		lblNoErrorsWere.setFont(new Font("Source Code Pro Black", Font.PLAIN, 13));
		lblNoErrorsWere.setBounds(10, 92, 191, 87);
		NoErrorPanel.add(lblNoErrorsWere);
		
		JPanel loadFileBorder = new JPanel();
		loadFileBorder.setBackground(Color.WHITE);
		loadFileBorder.setForeground(Color.WHITE);
		loadFileBorder.setBounds(557, 299, 231, 227);
		getContentPane().add(loadFileBorder);
		loadFileBorder.setLayout(null);
		
		JPanel loadFilePanel = new JPanel();
		loadFilePanel.setBounds(10, 10, 211, 207);
		loadFileBorder.add(loadFilePanel);
		loadFilePanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Select a predetermined");
		lblNewLabel.setBounds(0, 10, 211, 19);
		loadFilePanel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Source Code Pro Light", Font.BOLD, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblGridToLoad = new JLabel("grid to load");
		lblGridToLoad.setBounds(0, 29, 211, 19);
		loadFilePanel.add(lblGridToLoad);
		lblGridToLoad.setHorizontalAlignment(SwingConstants.CENTER);
		lblGridToLoad.setFont(new Font("Source Code Pro Light", Font.BOLD, 12));
		
		btnGrid1 = new JToggleButton("1");
		btnGrid1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	//If the button for file 1 is Selected
				SelectFile(1);								//Calls method that chooses the file
			}
		});
		btnGrid1.setBounds(10, 72, 46, 42);
		loadFilePanel.add(btnGrid1);
		btnGrid1.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 14));
		
		btnGrid2 = new JToggleButton("2");
		btnGrid2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	//If the button for file 2 is Selected
				SelectFile(2);								//Calls method that chooses the file
			}
		});
		btnGrid2.setBounds(84, 72, 46, 42);
		loadFilePanel.add(btnGrid2);
		btnGrid2.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 14));
		
		btnGrid3 = new JToggleButton("3");
		btnGrid3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	//If the button for file 1 is Selected
				SelectFile(3);								//Calls method that chooses the file
			}
		});
		btnGrid3.setBounds(155, 72, 46, 42);
		loadFilePanel.add(btnGrid3);
		btnGrid3.setFont(new Font("Source Code Pro Semibold", Font.BOLD, 14));
		
		/////////////////////////////////////////////////////////////////
		btnCheckGrid = new JButton("Check Grid");
		btnCheckGrid.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {			// If the button for "Check Grid" is pressed			
				ShowErrorText();									// Calls method to show panel that shows where are the errors
				HighlightErrors();									// And calls method so the squares of the Sudoku change color
			}														// based on the errors found
		});
		/////////////////////////////////////////////////////////////
		btnCheckGrid.setEnabled(false);
		btnCheckGrid.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 12));
		btnCheckGrid.setBounds(10, 141, 191, 42);
		loadFilePanel.add(btnCheckGrid);

	}
	
	private void SelectFile(int fileSelected) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void SelectFile(int fileSelected)
		//
		// Method parameters	:	fileSelected -		used to know which Sudoku file should be loaded depending on
		//												the user's choice and which button should stay selected
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called to make the necessary visual changes when a file is selected.
		//							It makes sure the button corresponfing to the other files are deselected and then 
		//							calls the necessary methods for loading the Sudoku and showing it in the UI.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-23		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		
		
		switch (fileSelected) {										//Depending on the button that is selected
		case 1:														//makes it so the other to button are unselected 
			btnGrid2.setSelected(false);
			btnGrid3.setSelected(false);
			break;
		
		case 2:
			btnGrid1.setSelected(false);
			btnGrid3.setSelected(false);
			break;
		
		case 3:
			btnGrid1.setSelected(false);
			btnGrid2.setSelected(false);
			break;
		
		}
		
		try {														//Tries to call the method to load the corresponding file
			sudoku.LoadSudokuFile(fileSelected);					// and fill the Sudoku accordingly
			FillSudokuGrid();
		} catch (Exception e1) {									// And shows an error message to the user if the file can't be read or loaded
			JOptionPane.showMessageDialog(getContentPane(), "Error reading the file");
			e1.printStackTrace();
		}
	}
	
	private void FillSudokuGrid() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void FillSudokuGrid()
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called to make the necessary visual changes when a new Sudoku is loaded.
		//							It changes the number based on the file loaded, sets all the squares of the Sudoku as gray
		//							in case the colour was previously changed due to a validation of the Sudoku.
		//							Then, it enables the button so the Sudoku can be validated.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-23		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		SudokuSquare[][] sudokuSquares;											//Defines variable to store all the squares that forms the Sudoku
		String currentNumber;													//Defines variable to store the value of the squares as strings
		int row, column;														//Defines ints to control the iterations of the loops
		SudokuSquare currentSquare;												//Defines variable to store the square currently being changed
		
		sudokuSquares = sudoku.getSudokuSquares();								//Gets all the squares of the Sudoku	
		
		for (row = 0; row <9; row ++) {											//goes through all the rows and columns of the Sudoku
			for(column = 0; column <9; column ++) {
				
				currentSquare = sudokuSquares[row][column];						//stores a reference to the square and its value
				currentNumber = String.valueOf(currentSquare.getValue());		// as a string
				
				currentSquare.setText(currentNumber);							//changes the text on the text field to the new value
				currentSquare.setBackground(Color.DARK_GRAY);					//changes the backgroung colour to gray
				currentSquare.setForeground(Color.WHITE);						// and the text colour to white
				currentSquare.setEditable(true);								// and makes the TextField editable for the user
			}
		}
		
		btnCheckGrid.setEnabled(true);											//enables the button to let the user check the Sudoku
		
	}
	

	private void ShowErrorText() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void ShowErrorText()
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called to show the correct message and panel depending on whether
		//							errors where found on the Sudoku or not. And if they where, the method then
		//							adjust the text so that it shows which rows, columns and blocks contain the errors.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-23		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		String rowErrors;												//Defines strings to form the different texts 
		String columnErrors;											// that show which areas have errors
		String blockErrors;
		
		boolean errorsDetected;											//Defines string to know wheter there were errors present
		boolean[][]errorAreas = new boolean[3][9];						//Defines array that stores which areas have errors

		
		int counter;													//Defines int to control iterations of the loop
		

		rowErrors="";													//Instantiates the different error strings as empty so that
		columnErrors="";												// more information can be added upon them
		blockErrors = "";
		
		errorsDetected = false;
		errorAreas = sudoku.CheckGrid();								//calls method to check where are the errors in the Sudoku


		for (counter = 0; counter <9; counter++) {						//Goes through all the areas

			if(errorAreas[0][counter]) {								// And adds to the text those that have
				rowErrors+=(counter+1)+" ";								// errors present
				errorsDetected = true;
			}
			if(errorAreas[1][counter]) {
				columnErrors+=(counter+1)+" ";
				errorsDetected = true;
			}
				
			if(errorAreas[2][counter]) {
				blockErrors+=(counter+1)+" ";
				errorsDetected = true;
			}
				
		}

		lblErrorRows.setText(rowErrors);								//Then shows that text in the UI
		lblErrorColumns.setText(columnErrors);
		lblErrorBlocks.setText(blockErrors);

		WelcomePanel.setVisible(false);									//Makes the welcome panel invisible
		
		if(errorsDetected) {											//and depending on whether there where errors
			ErrorPanel.setVisible(true);								// present or not, makes a determined panel visible
			NoErrorPanel.setVisible(false);
		}else {
			ErrorPanel.setVisible(false);
			NoErrorPanel.setVisible(true);
		}
		
		
	}
	
	private void HighlightErrors() {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	void HighlightErrors()
		//
		// Method parameters	:	none
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is called to change the background colours of the different squares forming 
		//							the grid of the Sudoku depending on the amount of errors that overlap in it.
		//
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2023-09-23		A. Mojica				Initial setup
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		SudokuSquare currentSquare;															//Instantiates variable to store the current Square being checked
		int errorCounter;																	//Instantiates integer to store the amount of errors overlapping in the square
		int currentRow, currentColumn;														//Instantiates variables to handle the iterations of the loops that go trough the Sudoku
		
		Color noErrorsColor = new Color(0, 204, 102);										//Defines the colour corresponding to each amount of errors
		Color oneErrorColor = Color.ORANGE;													// so that a new colour doesn't have to be called in each loop
		Color twoErrorsColor = new Color(255, 102, 0);
		Color threeErrorsColor = new Color(204, 0, 51);
		

		for (currentRow = 0; currentRow <9; currentRow ++) {								//goes through all the rows and columns of the Sudoku
			for(currentColumn = 0; currentColumn <9; currentColumn ++) {
				
				currentSquare = sudoku.getSudokuSquares()[currentRow][currentColumn];		//gets the square in each postion
				errorCounter = currentSquare.getErrorCounter();								// and the number of errors that overlap in it
				
				currentSquare.setForeground(Color.BLACK);									// Then it changes the colour of the text to black for readability
				
				switch(errorCounter) {														//Depending on the amount of errors that overlap in the square
																							// the background of the square changes to a different colour
				case 0:
					currentSquare.setBackground(noErrorsColor);								// If there are no errors the colour is green
					break;
				case 1:																		// If there's 1 error, the colour changes to light orange
					currentSquare.setBackground(oneErrorColor);								
					break;
				case 2:																		// If there are 2 errors, the colour changes to dark orange
					currentSquare.setBackground(twoErrorsColor);
					break;
				case 3:																		// If there are 3 errors, the colour changes to red
					currentSquare.setBackground(threeErrorsColor);
				}
				
			}
		}
	}
}
