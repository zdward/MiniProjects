package edu.rpi.cs.cs4963.u19.wardz2.hw02.gol_gui;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.filechooser.FileNameExtensionFilter;
/* Homework 2 by Zachary Ward*/
public class Life{
  private int[][] board;
  private int rows;
  private int cols;
  Color aliveCol;
  Color deadCol;



/**
* @param in_board a grid to copy into this.board_copy
* @param rows the number of rows of this.board
* @param cols the number of cols of this.board
*/
public Life(int[][] in_board, int rows, int cols, Color al, Color dead){
    this.rows = rows;
    this.cols = cols;

    //populate this.board accordingly
    board = new int[this.rows][this.cols];
    for(int i = 0; i < in_board.length; i++){
      for(int j = 0; j < in_board[0].length; j++){
        board[i][j] = in_board[i][j];
      }
    }

    //take in colors
    this.aliveCol = al;
    this.deadCol = dead;
}

/** Life object copy constructor, makes a Life object copy
* from another instance
* @param copy a Life object to copy
*/
public Life(Life copy){
  int[][] to_copy = copy.getBoard();
  this.rows = to_copy.length;
  this.cols = to_copy[0].length;
  this.board = new int[this.rows][this.cols];
  for(int i = 0; i < this.rows; i++){
    for(int j = 0; j < this.cols; j++){
      this.board[i][j] = to_copy[i][j];
    }
  }
  this.deadCol = copy.getDeadColor();
  this.aliveCol = copy.getAliveColor();
}

/** returns the alive cell color of this Life object
* @return this.aliveCol
*/
public Color getAliveColor(){
  return this.aliveCol;
}

/** returns the dead cell color of this Life object
* @return this.deadCol
*/
public Color getDeadColor(){
  return this.deadCol;
}

/** Sets our alive color to whatever the input is.
* Requires valid input.
* @param set the color which we set this.aliveCol to
*/
public void setAliveColor(Color set){
  this.aliveCol = set;
}

/** Sets our dead color to whatever the input is.
* Requires valid input.
* @param set the color which we set this.deadCol to
*/
public void setDeadColor(Color set){
  this.deadCol = set;
}

/** darken function. Returns a color that has RGB values 92%
* of the input color, returning a thus darker color.
* @param col the input color to be darkened
* @return a color object whose r,g and b values are 92% of
* the input's
*/
public Color darken(Color col){
  //calculate new rgb values
  double r = 0.92*col.getRed();
  double g = 0.92*col.getGreen();
  double b = 0.92*col.getBlue();

  //pass them into returned color
  return new Color((int)r,(int)g,(int)b);
}

/** Resize function, resizes our board to have 'row' rows
* and 'col' cols.
* @param row number of rows to resize our board to have
* @param col number of cols to resize our board to have
*/
public void resize(int row, int col){
  this.rows = row;
  this.cols = col;
  this.board = new int[this.rows][this.cols];
  for(int i = 0; i < this.rows; i++){
    for(int j = 0; j < this.cols; j++){
      this.board[i][j] = 0;
    }
  }
}


/** replaceBoard function. Replaces our this.board values with
* the values of 'replacement_board'
* @param replacement_board, contains the values which we want to populate
* this.board with
*/
public void replaceBoard(int[][] replacement_board){
  for(int i = 0; i < replacement_board.length; i++){
    for(int j = 0; j < replacement_board[0].length; j++){
      this.board[i][j] = replacement_board[i][j];
    }
  }
}

/** getCell function. Returns this.board[i][j], if it exists.
* @param i, the row in this.board to retrieve from
* @param j, the col in this.board to retrieve from
* @return an integer denoting the value of board[i][j]
*/
public int getCell(int i, int j){
  if(i < this.board.length && j < this.board[0].length)
    return this.board[i][j];
  return -1;
}


/** devolve function. Retrieves board info from 'last_gen' and
* changes this.board to represent the board in this file.
* @param last_gen, the file to base the devolving from
*/
public void devolve(File last_gen){
  try{
    FileReader fr = new FileReader(last_gen);
    BufferedReader br = new BufferedReader(fr);

    //read the first line, ignore it
    String str = br.readLine();

    int lineNum = 1;
    int gameRow = 0;

    //read in the grid
    while((str = br.readLine()) != null && lineNum <= rows){
      String[] split = str.split(", ");

      //grab current row as an integer array
      int[] row = MyFrame.intArrToStrArr(split,this.board[0].length);

      //re-populate this.board one col at a time at gameRow
      for (int gameCol = 0; gameCol < row.length; gameCol++){
        this.board[gameRow][gameCol] = row[gameCol];
      }

      gameRow+=1;
      lineNum +=1;

    }

    //close the file reader
    br.close();
  }
  catch(IOException e){
    //we should never have this happen
    System.out.println("File not found!");
  }
}

/** getRows function. Returns this.board.length
* @return an integer this.board.length denoting the amount of rows
* this.board contains
*/
public int getRows(){
  return this.board.length;
}

/** getCols function. Returns this.board[0].length
* @return an integer this.board[0].length denoting the amount of cols
* this.board contains
*/
public int getCols(){
  return this.board[0].length;
}

/** getBoard() function.
* @return this.board, the internal board in our Life object instance
*/
public int[][] getBoard(){
  return this.board;
}


/** acquireSum function, returns the sum of all neighbors of board[row][col]
* @param row, the row of the current cell
* @param col, the col of the current cell
* @return a circular sum of all the cells surrounding board[row][col]
*/
public int acquireSum(int row, int col){
  //Checking for bad input
  if(row < 0 || row > board.length - 1 ||
  col < 0 || col > board[0].length - 1)
    return 0;

  int sum = 0;

  //starting point of the circular sum [FIXED]
  int[] start = new int[]{row-1,col-1};

  //starting point which we will use in our double loop
  int[] start2 = new int[2];
  int i = 0;
  while(i < 3){
    int j = 0;
    //reset starting point
    start2[0] = start[0]+i;
    start2[1] = start[1];
    while(j < 3){
      //To handle wrapping the board
      if(start2[0] < 0) start2[0] = board.length - 1;
      if(start2[0] > board.length-1) start2[0] = 0;
      if(start2[1] < 0) start2[1] = board[0].length - 1;
      if(start2[1] > board[0].length - 1) start2[1] = 0;

      //adjust our sum and move to the next column
      sum+= board[start2[0]][start2[1]];
      start2[1]+=1;
      j+=1;
    }
    i+=1;
  }
  //return the sum of all cells except the center
  return sum - board[row][col];
}
/** lives function
* @param row, an integer signifying the row of the cell in question.
* @param col, an integer signifying the col of the cell in question.
* @return true or false depending on whether board[row][col] lives
* according to the rules described in progressCells specs.
*/
public boolean lives(int row, int col){
  int circularSum = acquireSum(row,col);

  //Any live cell with < 2 live neighbors dies
  if(circularSum < 2){
    return false;
  }

  //Any live cell with two or three live neighbors lives
  else if (board[row][col] == 1 && (circularSum == 2 || circularSum == 3)){
    return true;
  }

  //Any live cell with more than three live neighbors dies
  else if(board[row][col] == 1 && circularSum > 3){
    return false;
  }

  //Any dead cell with exactly three live neighbors becomes alive
  else if(board[row][col] == 0 && circularSum == 3){
    return true;
  }

  return false;
}

  /** progressCells function.
  *
  * modifies the 2d-array 'board' by applying the following rules to each cell:
  * Any live cell with fewer than two live neighbors dies,
  * as if caused by underpopulation. Any live cell with two or three
  * live neighbors lives on to the next generation.
  * Any live cell with more than three live neighbors dies, as
  * if by overpopulation. Any dead cell with exactly three live
  * neighbors becomes a live cell, as if by reproduction.
  * @return an int[][] representing the updated board after one step
  */
  public int[][] progressCells(){
    int[][] board_copy = new int[this.board.length][this.board[0].length];
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[0].length; j++){
        if(lives(i,j)) board_copy[i][j] = 1;
        else board_copy[i][j] = 0;
      }
    }

    this.board = board_copy.clone();

    return board_copy;
  }

  /** main function
  *
  * @param args, an array containing command line arguments (if any)
  */
  public static void main(String[] args){
    //we know this file will always be formatted correctly
    EventQueue.invokeLater( () ->
    {
      var frame = new MyFrame();
      frame.setResizable(false);
      frame.setTitle("Conway's Game of Life");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);

    });

  }


}

class MyFrame extends JFrame{
  //instance variables
  private int screenHeight;
  private int screenWidth;
  MyComponent frame_component;
  Life conway;
  int tickNum;
  JFileChooser chooser;
  Color alive;
  Color dead;
  int killedCells;


  /** MyFrame constructor. Properly initializes a MyFrame object
  */
  public MyFrame(){
    //sizing is based on screenSize
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();

    //we always initialize our Life object by parsing our DEFAULT file
    this.conway = parseFile("DEFAULT.txt");

    //in case, the default
    alive = this.conway.getAliveColor();
    dead = this.conway.getDeadColor();

    //initialize the embedded component
    this.frame_component = new MyComponent(conway);

    //prepare output files to contains tick and cell slaughter info
    // cell slaughter = how many cells were killed after one evolve
    File tickFile = new File("tick.txt");
    File killedCells = new File("killed.txt");

    //either the user set a DefaultPath or we have to set one
    File defaultPath = new File("DEFPATH.txt");
    File workingDirectory;
    if(!defaultPath.exists())
      workingDirectory = new File(System.getProperty("user.dir").toString()+"/LoadableGrids");
    else{
      workingDirectory = new File(parseDefPath());
    }
    chooser = new JFileChooser(workingDirectory);

    //so you can only choose text files
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
    "Text Files", "txt");
    chooser.setFileFilter(filter);

    //either get current tick or set it to 0
    if(tickFile.exists())
      this.tickNum = get_tick(tickFile);
    else
      this.tickNum = 0;

    //if a killed.txt file exists, load in the data
    if(killedCells.exists())
      this.killedCells = get_killed(killedCells);
    else
      this.killedCells = 0;
    write_tick(tickFile);
    add(frame_component);
    pack();
    this.screenHeight = screenSize.height;
    this.screenWidth = screenSize.width;

    //frame dimensions
    setSize(this.screenWidth/2,this.screenHeight/2);

    setLocationByPlatform(true);

    addToolBar();
    addMenuBar();
  }

///////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////
  /** Given the first line of an input file, parseRows parses out the # of rows
  *@param line, meant to be the first line of an input file of the form ("int1, int2")
  *@return the first integer (int1), representing the number of rows of this.board
  */
  public static int parseRows(String line){
    String[] rowsAndCols = line.split(", ");

    if(rowsAndCols.length == 2) return Integer.valueOf(rowsAndCols[0]);
    return -1;
  }
////////////////////////////////////////////////////////////////////////////////////////
  /** Given the first line of an input file, parseCols parses out the # of cols
  *@param line, meant to be the first line of an input file of the form ("int1, int2")
  *@return the second integer (int2), representing the number of cols of this.board
  */
  public static int parseCols(String line){
    String[] rowsAndCols = line.split(", ");

    if(rowsAndCols.length == 2) return Integer.valueOf(rowsAndCols[1]);
    return -1;
  }

////////////////////////////////////////////////////////////////////////////////////////////
/** get_killed function. This function returns an integer representing
* the amount of cells killed as a result of evolving from the last generation.
* @param killFile a file to read kill data from
* @return an integer representing the amount of cells killed as a
* result of evolving from the last generation.
*/

public int get_killed(File killFile){
  int killed = 0;
  if(!killFile.exists())
    return killed;

  try{
    FileReader fr = new FileReader(killFile);
    BufferedReader br = new BufferedReader(fr);

    killed = Integer.valueOf(br.readLine());
  }
  catch(Exception e){
    e.printStackTrace();
  }
  return killed;
}
///////////////////////////////////////////////////////////////////////////////////////////
/** converts string array to integer array
* @param arr, a String array, where each string is truly one character long
* @param cols, the amount of cols in our input board
* @return an array of integers, where each element is an element of arr converted
*/
public static int[] intArrToStrArr(String[] arr, int cols){
  //If we have an invalid file, these two cases should handle it
  if(arr.length != cols){
    JOptionPane.showMessageDialog(null,"Invalid File Format! Please see the README");
    System.exit(0);
  }
  if(arr.length == cols && cols == 1 && arr[0].length() != cols){
    JOptionPane.showMessageDialog(null,"Invalid File Format! Please see the README");
    System.exit(0);
  }
  //At this point our file should be valid
  int[] ret = new int[arr.length];
  int i = 0;
  for(String s: arr){
    ret[i] = Integer.valueOf(s);
    i+=1;
  }
  return ret;
}
//////////////////////////////////////////////////////////////////////////////////
/** isNumeric function, determines whether a String input is numeric (integer)
* @param str, a word to be tested as to whether or not it is numeric
* @return true or false depending on whether or not input can successfully be
* converted to an integer
*/
public static boolean isNumeric(String str)
{
    if(str.equals(""))
      return false;

    for (char c : str.toCharArray())
    {
        if (!Character.isDigit(c)) return false;
    }
    return true;
}

///////////////////////////////////////////////////////////////////////////////////
/** update_killed function. This function updates killed.txt with the difference
* of nextAlive and currentAlive (i.e currentAlive - nextAlive)
* @param currentAlive, an integer number of cells alive in generation g_0
* @param nextAlive, an integer number of cells alive in generation g_1
*/
public void update_killed(int currentAlive, int nextAlive){
  File killedFile = new File("killed.txt");
  try{
    FileWriter fw = new FileWriter(killedFile);
    BufferedWriter bw = new BufferedWriter(fw);

    //write the difference currentAlive - nextAlive
    bw.write("" + (currentAlive - nextAlive));
    bw.close();
  }
  catch(Exception e){
    //this will never get called
    e.printStackTrace();
  }
}
///////////////////////////////////////////////////////////////////////////////////
/** jumpTick function. This function takes our GUI forward or backward to a specific
* tick, advancing all relevant statistics and recording all necessary data.
* @param tick, an integer simulation tick number to jump to
*/
public void jumpTick(int tick){
  if(tick == tickNum)
    return;

  //if we want to jump to a higher tick
  if(tickNum < tick){
    //advance until we are at tick-1
    while(tickNum < tick-1){
      advance_board("evolution");
    }
    //get number of cells alive in generation g_0
    int currentAlive = calcAliveCells();

    advance_board("evolution");

    //get number of cells alive in generation g_1
    int nextAlive = calcAliveCells();

    //write to killed.txt the difference currentAlive - nextAlive
    update_killed(currentAlive,nextAlive);

    //update our internal value
    killedCells = get_killed(new File("killed.txt"));
  }

  //if we must go backwards
  else if(tickNum > tick){
    if(tick == 0){
      //0 cells killed before generation 0
      update_killed(0,0);
      while(tickNum > tick){
        devolve_board();
      }
    }
    //if there exists a tick before tick (mathematically, tick-1>=0)
    else{
      //devolve to tick-1
      while(tickNum >= tick){
        devolve_board();
      }

      int currentAlive = calcAliveCells();
      advance_board("evolution");
      int nextAlive = calcAliveCells();

      update_killed(currentAlive,nextAlive);

      killedCells = get_killed(new File("killed.txt"));
    }
  }
}
/////////////////////////////////////////////////////////////////////////
/** advance_board function. This function advances our GUI one simulation tick
* and applies all rules of Conway's Game of Life to each cell simultaneously.
* All necessary statistics are advanced and all necessary simulation data is stored.
* @param patternName either "evolution" or any other string. If "evolution", then
* the board data will be saved in the evolutions sub-folder. Otherwise, the board
* data will be saved in SavedGrids.
*/
public void advance_board(String patternName){

  //write current grid to a file so we remember it
  String gridName;
  int currentAlive = calcAliveCells();
  //when to save in user/.../HW2/evolutions
  if(patternName.equals("evolution")){
    gridName = System.getProperty("user.dir").toString() + "/evolutions/";
    gridName += patternName + tickNum + ".txt";
  }

  //when to save in user/.../HW2/SavedGrids
  else{
    gridName = System.getProperty("user.dir").toString() + "/SavedGrids/";
    gridName += patternName + tickNum + ".txt";
  }
  File store_grid = new File(gridName);
  write_grid(conway.getBoard(), store_grid);
  tickNum+=1;
  write_tick(new File("tick.txt"));

  //update board
  conway.progressCells();

  int nextAlive = calcAliveCells();

  //reset file
  File oldFile = new File("DEFAULT.txt");
  oldFile.delete();
  File newFile = new File("DEFAULT.txt");

  //so we can display the cells we killed after advancing
  update_killed(currentAlive,nextAlive);

  //update # of killed cells from last generation internally
  killedCells = get_killed(new File("killed.txt"));
  conway.setAliveColor(conway.darken(conway.getAliveColor()));

  write_grid(conway.getBoard(),newFile);

  //finally, repaint so we see the changes in real-time
  frame_component.repaint();
}
/////////////////////////////////////////////////////////////////////////////////////
/** write_tick function. This function writes our current simulation tick number
* to 'tickHolder.'
* @param tickHolder, a file to write this.tickNum to
*/
public void write_tick(File tickHolder){
  try{
    FileWriter fw = new FileWriter(tickHolder);
    BufferedWriter bw = new BufferedWriter(fw);

    //write this.tickNum to file
    bw.write("" + this.tickNum);
    bw.close();
  }
  catch(Exception e){
    e.printStackTrace();
  }
}
//////////////////////////////////////////////////////////////////////////////////////
/** get_tick function. This function retrieves a stored tick number
* from a file 'tickHolder.' The tick number is returned as an integer.
* @param tickHolder a file which holds a tick number
* @return an integer representing the tick number contained in 'tickHolder'
*/
public int get_tick(File tickHolder){
  int ret = -1;
  try{
    FileReader fr = new FileReader(tickHolder);
    BufferedReader br = new BufferedReader(fr);

    /* Grab tick and convert it to an integer. This conversion
    will never go wrong, so there is no need to handle any potential
    errors*/
    String tick = br.readLine();
    ret = Integer.valueOf(tick);
  }
  catch(Exception e){
    e.printStackTrace();
  }
  return ret;
}
//////////////////////////////////////////////////////////////////////////////////////
/** devolve_board function. This function uses previously stored files regarding the state
* of the board at a certain tick and "devolves" back one generation. No other data is stored,
* only used to facilitate the "devolving."
*/
public void devolve_board(){

  //check to make sure evolution0.txt exists; if it doesn't, we can't de-volve
  String dir = System.getProperty("user.dir").toString() + "/evolutions/";
  dir += "evolution0.txt";
  File generation_0 = new File(dir);
  if(!generation_0.exists()){
    JOptionPane.showMessageDialog(null,"Can't devolve past generation 0!");
  }
  if(get_tick(new File("tick.txt")) == 0){
    JOptionPane.showMessageDialog(null,"Can't devolve past generation 0!");
  }


  //if we're all good to go...
  if(generation_0.exists() && get_tick(new File("tick.txt")) != 0){
    tickNum -=1;

    //update tick.txt to contain tickNum-1
    write_tick(new File("tick.txt"));

    //load in board from 'evolution' + tickNum + '.txt'
    String previous_gen = System.getProperty("user.dir") + "/evolutions/";
    previous_gen += "evolution" + tickNum + ".txt";
    File prev = new File(previous_gen);

    //change conway's inner board/2D-array accordingly
    this.conway.devolve(prev);

    //reset file
    File oldFile = new File("DEFAULT.txt");
    oldFile.delete();
    File newFile = new File("DEFAULT.txt");

    if(tickNum > 0){
      int nextAlive = calcAliveCells();
      previous_gen = System.getProperty("user.dir") + "/evolutions/";
      previous_gen += "evolution" + (tickNum-1) + ".txt";
      File prevPrev = new File(previous_gen);

      //go back so we can see how many alive cells there were
      this.conway.devolve(prevPrev);

      //take note of that number
      int currentAlive = calcAliveCells();

      //find out how many cells were killed
      killedCells = currentAlive - nextAlive;

      //advance back to where we were before
      this.conway.progressCells();

    }
    else{
      killedCells = 0;
    }

    //change color to represent the color of the previous generation
    String relevant_evo = System.getProperty("user.dir").toString() + "/evolutions/";
    relevant_evo += "evolution" + tickNum + ".txt";
    this.conway.setAliveColor(parseAlive(new File(relevant_evo)));

    //revert killed.txt to hold value from previous generation
    try{
      FileWriter fw = new FileWriter(new File("killed.txt"));
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write("" + killedCells);
      bw.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }

    write_grid(conway.getBoard(),newFile);

    //refresh changes in real-time
    frame_component.repaint();
}

}
//////////////////////////////////////////////////////////////////////////////////////
/** addToolBar function. This function creates a toolbar with interactive buttons
* and adds it to our Frame. Each button's actions are contained within this function.
*/
  public void addToolBar(){

    //create a new toolbar to house buttons
    JToolBar toolbar = new JToolBar();
    toolbar.setRollover(true);

    //add evolve button
    JButton evolve = new JButton("Next Generation");
    evolve.addActionListener( new ActionListener()
    {
    //  @Override
      public void actionPerformed(ActionEvent e)
      {
        //advance the board when we click the button
        advance_board("evolution");
      }
    });

    //add devolve button
    JButton devolve = new JButton("Prev Generation");
    devolve.addActionListener( new ActionListener()
    {
    //  @Override
      public void actionPerformed(ActionEvent e)
      {
          //devolve board when we click the button
          devolve_board();
      }
    });

    //add jump to tick button
    JButton jump = new JButton("Jump to Tick");
    jump.addActionListener( new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        //take in a String, must be an integer >= 0 or else nothing happens
        String tick = JOptionPane.showInputDialog("Enter a Tick Number (Integer) to jump to >= 0");
        if(tick!=null){
          if(tick.length() > 0 && !isNumeric(tick) || tick.length() == 0){
            JOptionPane.showMessageDialog(null, "Please enter an INTEGER");
          }
          if(tick.length() > 0 && isNumeric(tick) && Integer.valueOf(tick) < 0){
            JOptionPane.showMessageDialog(null, "Please enter an Integer >= 0");
          }
          if(tick.length() > 0 && isNumeric(tick) && Integer.valueOf(tick) >= 0){
            //perform jumpTick function
            jumpTick(Integer.valueOf(tick));
          }
        }
      }
    });

    //add all buttons to toolbar
    toolbar.add(evolve);
    toolbar.addSeparator();
    toolbar.add(devolve);
    toolbar.addSeparator();
    toolbar.add(jump);

    Container content_pane = this.getContentPane();
    //add toolbar to  content pane
    content_pane.add(toolbar, BorderLayout.SOUTH);
  }

//////////////////////////////////////////////////////////////////////////////////////////
/** resizeBoard function. Takes in two integers, 'row' and 'col'.
* this.conway's inner board will be resized to have 'row' rows and
* 'col' columns.
* @param row the number of rows to resize conway's board to have
* @param col the number of cols to resize conway's board to have
*/
public void resizeBoard(int row, int col){
  //call back to Life object's resize function
  conway.resize(row,col);
}

///////////////////////////////////////////////////////////////////////////////////////////
/** saveFiles function. This function writes output files containing the state of
* the board from startTick to endTick. Thus, (endTick-startTick+1) files will be saved in total.
* A typical output file will be saved in user/.../HW2/SavedGrids and will be called
* 'fileName' + tickNumber + '.txt', where fileName is an input to this function.
* @param startTick, a tick to start saving files at
* @param endTick, a tick to end saving files at (we include this tick too)
* @param fileName, a output file name pattern
*/
public void saveFiles(int startTick, int endTick, String fileName){
  //keep track of where we started
  int currentTick = tickNum;


  if(startTick < tickNum){
    //back up to our startTick
    while(startTick < tickNum){
      devolve_board();
    }

    //evolve the board until we hit the end
    while(tickNum <= endTick){
      advance_board(fileName);
    }

    //back up to where we started at
    while(tickNum > currentTick){
      devolve_board();
    }
  }

  else if (startTick == tickNum){
    //evolve the board until we hit end
    while(tickNum <= endTick){
      advance_board(fileName);
    }
    //back up to where we were before
    while(tickNum > currentTick){
      devolve_board();
    }

  }

  else{
    //evolve until we hit starting point
    while(tickNum < startTick){
      advance_board("evolution");
    }

    //evolve until we hit ending point, write out file each time
    while(tickNum <= endTick){
      advance_board(fileName);
    }

    //devolve until we hit our previous starting point
    while(tickNum > currentTick){
      devolve_board();
    }
  }


}
///////////////////////////////////////////////////////////////////////////////////////////
/** clear_evolutions function. This function clears all files in the directory
* user/.../HW2/evolutions
*/

public void clear_evolutions(){
  String working_dir = System.getProperty("user.dir").toString() + "/evolutions/";
  String pattern_name = "evolution";
  int startTick = 0;
  File f = new File(working_dir + pattern_name + startTick + ".txt");

  //delete all files in the directory
  while(f.exists()){
    f.delete();
    startTick+=1;
    f = new File(working_dir + pattern_name + startTick + ".txt");
  }
}
///////////////////////////////////////////////////////////////////////////////////////////
/** addMenuBar function. This function is responsible for adding the menubar at the top
* of our GUI. Further, all JMenuItems have their actions implemented in this function.
*/
  public void addMenuBar(){
    //Create Menus File, Options and Help
    JMenuBar menuBar = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu options = new JMenu("Options");
    JMenu help = new JMenu("Help");

    //Create options for 'File'
    JMenuItem load_grid = new JMenuItem("Load Grid");

    //add click action
    load_grid.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            Life parsedResult = parseFile(chooser.getSelectedFile().toString());
            int[][] board_to_write = parsedResult.getBoard();

            //so repaint works, add else later
            if(board_to_write.length >= 0 && board_to_write[0].length >= 0){
              conway.resize(board_to_write.length, board_to_write[0].length);
              conway.replaceBoard(board_to_write);
            }

            //just to be safe, delete and re-write DEFAULT.txt
            File oldFile = new File("DEFAULT.txt");
            oldFile.delete();
            File newFile = new File("DEFAULT.txt");

            write_grid(board_to_write, newFile);
            tickNum = 0;

            //whenever we load a new grid, ticks are reset to 0
            File tick_file = new File("tick.txt");
            tick_file.delete();

            clear_evolutions();

            //clear default path preferences
            File def_path_file = new File("DEFPATH.txt");
            if(def_path_file.exists()) def_path_file.delete();

            //clear any data about cells we've killed
            File killed = new File("killed.txt");
            if(killed.exists()) killed.delete();
            //update board in real time

            conway.setAliveColor(parseAlive(chooser.getSelectedFile()));

            //refresh in real-time
            frame_component.repaint();
        }
      }
    });

    //File > Save Grids option
    JMenuItem save_grid = new JMenuItem("Save Grid(s)");

    //click listener for SaveGrids
    save_grid.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e){
        boolean startGreater = false;

        //take in a starting tick and make sure its valid
        String startTick = JOptionPane.showInputDialog("Enter a starting tick [a non-negative integer]");
        if(startTick != null && !(isNumeric(startTick))){
          JOptionPane.showMessageDialog(null, "Please enter a non-negative integer");
        }

        //take in an end tick and make sure its valid
        String endTick = JOptionPane.showInputDialog("Enter an ending tick [a non-negative integer]");
        if(endTick != null && !(isNumeric(endTick))){
          JOptionPane.showMessageDialog(null, "Please enter a non-negative integer");
        }

        //if endTick > startTick
        if(startTick != null && endTick != null){
          if(isNumeric(startTick) && isNumeric(endTick) && Integer.valueOf(endTick) < Integer.valueOf(startTick)){
            startGreater = true;
            JOptionPane.showMessageDialog(null, "Please enter an end tick greater than or equal to your start tick!");
          }
        }

        //if tick numbers are valid, take in an output name pattern
        if(startTick != null && endTick != null && isNumeric(startTick) && isNumeric(endTick) && !startGreater){
          String save_name = JOptionPane.showInputDialog("Please enter an output name pattern");
          if(save_name != null && !save_name.equals(""))
            saveFiles(Integer.valueOf(startTick),Integer.valueOf(endTick),save_name);
          else
            JOptionPane.showMessageDialog(null, "Please don't leave this field blank.");
        }
      }
    });

    //add an exit option and close app when clicked
    JMenuItem exit_grid = new JMenuItem("Exit");
    exit_grid.addActionListener( new ActionListener() {

      public void actionPerformed(ActionEvent e){
          System.exit(0);
      }

    });

    //Create Options > Config Panel option
    JMenuItem configPanel = new JMenuItem("Config Panel");
    configPanel.addActionListener( new ActionListener()
    {
    //  @Override
      public void actionPerformed(ActionEvent e)
      {
        //contains config panel implementation
        createConfig();
      }
    });


    //Create Options > Statistics option
    JMenuItem stats = new JMenuItem("Statistics");
    stats.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        //creates frame displaying current stats
        statsFrame();
      }
    });

    //Create Help > README option
    JMenuItem readMe = new JMenuItem("README");
    readMe.addActionListener( new ActionListener(){

      public void actionPerformed(ActionEvent e){
        try{

          //open README file in folder
          Desktop.getDesktop().open(new File("README.txt"));
        }
        catch(Exception e2){
          e2.printStackTrace();
        }
      }

    });

    //Create Help > About option
    JMenuItem aboutMe = new JMenuItem("About");
    aboutMe.addActionListener( new ActionListener() {

      public void actionPerformed(ActionEvent e){
        //display about page
        aboutFrame();
      }
    });

    //add menu items
    file.add(load_grid);
    file.add(save_grid);
    file.add(exit_grid);
    options.add(configPanel);
    options.add(stats);
    help.add(readMe);
    help.add(aboutMe);

    //add menus to menubar
    menuBar.add(file);
    menuBar.add(options);
    menuBar.add(help);

    //set our menubar as the frame menubar
    this.setJMenuBar(menuBar);
  }
  /////////////////////////////////////////////////////////////////////////////////////////////
  /** calcAliveCells function. This function iterates over all cells in
  * the board of this.conway and returns the number of 1's, or all alive
  * cells.
  * @return an integer representing the number of currently alive cells
  */
  public int calcAliveCells(){
    int[][] board = this.conway.getBoard();
    int alive = 0;

    //count the number of 1's in the board
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[0].length; j++){
        if(board[i][j] == 1)
          alive+=1;
      }
    }

    return alive;
  }
  /////////////////////////////////////////////////////////////////////////////////////////////
  /** statsFrame function. This function displays a JPanel which contains four relevant
  * statistics regarding the state of the board: current tick, cells alive, cells dead and
  * cells killed (from the previous generation).
  */
  public void statsFrame(){
    var new_frame = new JFrame();
    new_frame.setSize(300,300);
    new_frame.setTitle("Statistics");
    new_frame.setVisible(true);
    new_frame.setResizable(false);

    JPanel panel = new JPanel();
    panel.setLayout(null);
    JLabel currentTick = new JLabel("Current tick: " + tickNum);
    currentTick.setBounds(10,10,300,20);

    JLabel aliveCells = new JLabel("Cells Alive: " + calcAliveCells());
    aliveCells.setBounds(10,40,300,20);

    int[][] conwayBoard = this.conway.getBoard();
    int numCells = conwayBoard.length * conwayBoard[0].length;
    JLabel deadCells = new JLabel("Cells Dead: " + (numCells - calcAliveCells()));
    deadCells.setBounds(10,70,300,20);

    JLabel killedlabel = new JLabel("Cells killed: " + get_killed(new File("killed.txt")));
    killedlabel.setBounds(10,100,300,20);

    panel.add(currentTick);
    panel.add(aliveCells);
    panel.add(deadCells);
    panel.add(killedlabel);



    new_frame.getContentPane().add(panel);
  }
  /////////////////////////////////////////////////////////////////////////////////////////////
  /** createConfig function. This function displays a JPanel containing 5 configuration choices:
  * default load-grid path, default number of rows, default number of cols, alive cell color
  * and dead cell color.
  */
  public void createConfig(){
    var new_frame = new JFrame();
    new_frame.setSize(600,600);
    new_frame.setTitle("Configuration Page");
    new_frame.setVisible(true);
    new_frame.setResizable(false);

    JPanel panel = new JPanel();
    panel.setLayout(null);

    //for setting default load grid path
    JButton path = new JButton("Choose");
    path.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e){

        //set up JFileChooser to select from subfolders in user/.../HW2
        JFileChooser directory_choice = new JFileChooser(System.getProperty("user.dir").toString());
        directory_choice.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = directory_choice.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
          try{
            //write new default load path to a file called "DEFPATH.txt"
            FileWriter fw = new FileWriter(new File("DEFPATH.txt"));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(directory_choice.getSelectedFile().toString());
            bw.close();
          }
          catch(Exception e2){
            //this will never happen, but is required syntactically
            e2.printStackTrace();
          }
        }
        /* so load-grid opens up in the directory selected by the user in the config page
        by default */
        chooser.setCurrentDirectory(directory_choice.getSelectedFile());
      }
    });

    //placing all buttons, boxes and labels


    JLabel defPath = new JLabel("Choose a Default Load Path");
    defPath.setBounds(10,10,600,20);
    path.setBounds(310,10,100,20);
    //10,10,60,20

    String alive_colors[] = {"Red","Blue","Green","Aqua"};

    JComboBox<String> alive_choices = new JComboBox<>(alive_colors);
    JLabel alive_selection = new JLabel("Select Alive Cell Color");
    alive_choices.setBounds(310,50,60,20);
    alive_selection.setBounds(10,50,600,20);

    String dead_options[] = {"Pink","Gray","White","Magenta"};

    JComboBox<String> dead_choices = new JComboBox<>(dead_options);
    JLabel deadCols = new JLabel("Select Dead Cell Color");
    deadCols.setBounds(10,90,600,20);
    dead_choices.setBounds(310,90,60,20);

    JTextField gridRows = new JTextField();
    JLabel gridRowNum = new JLabel("Enter Desired Number of Grid Rows");
    gridRows.setBounds(310,130,60,20);
    //310,170,60,20
    gridRowNum.setBounds(10,130,600,20);

    JTextField gridCols = new JTextField();
    JLabel gridColNum = new JLabel("Enter Desired Number of Grid Cols");
    gridCols.setBounds(310,170,60,20);
    gridColNum.setBounds(10,170,600,20);

    JButton submit = new JButton("Apply Changes");
    submit.setBounds(250,250,200,30);

    //handle all input when "Apply Changes" is clicked...
    submit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        String rows = gridRows.getText();
        String cols = gridCols.getText();

        String aliveColor = String.valueOf(alive_choices.getSelectedItem());
        String deadColor = String.valueOf(dead_choices.getSelectedItem());

        //to contain alive color info
        int r1 = 0, g1 = 0, b1 = 0;
        //to contain dead color info
        int r2 = 60, g2 = 60, b2 = 60;


        if(aliveColor.equals("Red")){
          r1 = 255;
          g1 = 0;
          b1 = 0;

        }

        else if(aliveColor.equals("Blue")){
          r1 = 0;
          g1 = 0;
          b1 = 255;
        }

        else if(aliveColor.equals("Green")){
          r1 = 0;
          g1 = 255;
          b1 = 0;
        }
        else{
          r1 = 0;
          g1 = 255;
          b1 = 255;
        }
        if(deadColor.equals("Pink")){
          r2 = Color.PINK.getRed();
          g2 = Color.PINK.getGreen();
          b2 = Color.PINK.getBlue();
        }

        else if (deadColor.equals("Gray")){
          r2 = Color.GRAY.getRed();
          g2 = Color.GRAY.getGreen();
          b2 = Color.GRAY.getBlue();
        }

        else if (deadColor.equals("White")){
          r2 = Color.WHITE.getRed();
          g2 = Color.WHITE.getGreen();
          b2 = Color.WHITE.getBlue();
        }

        else{
          r2 = Color.MAGENTA.getRed();
          g2 = Color.MAGENTA.getGreen();
          b2 = Color.MAGENTA.getBlue();
        }

       aliveColor = r1 + ", " + g1 + ", " + b1;
       deadColor = r2 + ", " + g2 + ", " + b2;

       //adjusting color to match user input
        conway.setAliveColor(new Color(r1,g1,b1));
        conway.setDeadColor(new Color(r2,g2,b2));
        File defaultFile = new File("DEFAULT.txt");

        write_grid(conway.getBoard(),defaultFile);

        //refresh the board in real time
        frame_component.repaint();
        //Handling input for number of default rows
        if(!isNumeric(rows) && !rows.equals("")) JOptionPane.showMessageDialog(null, "Please enter a Number for rows");
        if(isNumeric(rows) && (Integer.valueOf(rows) <= 0 || Integer.valueOf(rows) > 100)){
          JOptionPane.showMessageDialog(null, "Please enter a Number for rows between 1 and 100");
        }

        //Handling input for number
        if(!isNumeric(cols) && !cols.equals("")) JOptionPane.showMessageDialog(null, "Please enter a Number for cols");
        if(isNumeric(cols) && (Integer.valueOf(cols) <= 0 || Integer.valueOf(cols) > 100)){
          JOptionPane.showMessageDialog(null, "Please enter a Number for cols between 1 and 100");
        }

        //if Grid Rows and Grid Cols boxes contained VALID input...resize the board!
        if(isNumeric(rows) && isNumeric(cols) && Integer.valueOf(rows) > 0 && Integer.valueOf(cols) > 0){
          if(Integer.valueOf(rows) <= 100 && Integer.valueOf(cols) <= 100){
            //delete old file
            File oldFile = new File("DEFAULT.txt");
            oldFile.delete();
            File newFile = new File("DEFAULT.txt");

            //create new grid with specified rows and cols
            int[][] new_grid = new int[Integer.valueOf(rows)][Integer.valueOf(cols)];
            for(int i = 0; i < new_grid.length; i++){
              for(int j = 0; j < new_grid[0].length; j++){
                new_grid[i][j] = 0;
              }
            }
            //recreate DEFAULT.txt
            write_grid(new_grid,newFile);

            clear_evolutions();
            update_killed(0,0);
            tickNum = 0;

            resizeBoard(Integer.valueOf(rows),Integer.valueOf(cols));

            //refresh in real-time
            frame_component.repaint();
        }
      }
    }
  });

    //add all elements to panel
    panel.add(path);
    panel.add(defPath);
    panel.add(alive_choices);
    panel.add(alive_selection);
    panel.add(gridRows);
    panel.add(gridRowNum);
    panel.add(gridCols);
    panel.add(gridColNum);
    panel.add(deadCols);
    panel.add(dead_choices);
    panel.add(submit);

    //add panel to frame
    new_frame.getContentPane().add(panel);

  }
////////////////////////////////////////////////////////////////////////////////////////////
  /** aboutFrame function. This function creates the about page which is displayed
  * by clicking Help and then About
  */

  public void aboutFrame(){
    //create frame and set properties
    var new_frame = new JFrame();
    new_frame.setSize(600,600);
    new_frame.setTitle("About This App");
    new_frame.setVisible(true);
    new_frame.setResizable(false);

    //create panel
    JPanel panel = new JPanel();
    panel.setLayout(null);

    //create relevant labels and set their bounds
    JLabel credit = new JLabel("Credits:");
    credit.setPreferredSize(new Dimension(100,100));
    credit.setBounds(275,50,80,20);

    JLabel maker = new JLabel("Zachary D. Ward -- RPI Class of 2021");
    maker.setPreferredSize(new Dimension(100,100));
    maker.setBounds(175,100,300,20);

    JLabel graphics = new JLabel("Technologies Used:");
    graphics.setBounds(230,250,300,20);

    JLabel graphicsTech = new JLabel("Java and Swing");
    graphicsTech.setBounds(245,300,300,20);

    JLabel course = new JLabel("CSCI Course Info: ");
    course.setBounds(245,400,300,20);

    JLabel csciinfo = new JLabel("CSCI 4963 - Application Prog. Using Java");
    csciinfo.setBounds(200, 450, 300, 20);

    JLabel instructor = new JLabel("Instructor - Konstantin Kuzmin");
    instructor.setBounds(200,500,300,20);

    //add panel elements
    panel.add(credit);
    panel.add(maker);
    panel.add(graphics);
    panel.add(graphicsTech);
    panel.add(course);
    panel.add(csciinfo);
    panel.add(instructor);

    //add panel to frame
    new_frame.getContentPane().add(panel);
  }
/////////////////////////////////////////////////////////////////////////////////////////////////////
/** parseDefPath function. This function is responsible for returning a String representing
* a file path contained in "DEFPATH.txt".
* @return a String path contained in DEFPATH.txt
*/
public String parseDefPath(){
  String set_path = "";
  try{
    FileReader fr = new FileReader("DEFPATH.txt");
    BufferedReader br = new BufferedReader(fr);

    //read in the path
    set_path = br.readLine();
  }
  catch(Exception e){
    e.printStackTrace();
  }
  return set_path;
}
/////////////////////////////////////////////////////////////////////////////////////////////////////
/** parseAlive function. This function parses out the color of alive cells
* from a specific file 'evolution.'
* @param evolution a file to parse from (looking for the color of our alive cells)
* @return a Color object representing the color of alive cells
*/
public Color parseAlive(File evolution){
  Color ret;
  int r, g, b;
  r = 0;
  g = 0;
  b = 0;
  try{
    FileReader fr = new FileReader(evolution);
    BufferedReader br = new BufferedReader(fr);

    int i = 1;
    String str = br.readLine();
    int rows = parseRows(str);

    //advance to the line containing the color info
    while(i <= rows+1){
      str = br.readLine();
      i++;
    }

    //form an array ["REDINFO", "GREENINFO", "BLUEINFO"]
    String[] rgb = str.split(", ");

    //grab r,b,g integer values from the array
    r = Integer.valueOf(rgb[0]);
    g = Integer.valueOf(rgb[1]);
    b = Integer.valueOf(rgb[2]);

  }
  catch(Exception e){
    //this will never happen
    e.printStackTrace();
  }
  ret = new Color(r,g,b);
  return ret;
}
///////////////////////////////////////////////////////////////////////////////
/** parseDead function. This function parses out the color of alive cells
* from a specific file 'evolution.'
* @param evolution a file to parse from (looking for the color of our dead cells)
* @return a Color object representing the color of dead cells
*/
public Color parseDead(File evolution){
  Color ret;
  int r,g,b;
  r = 0;
  g = 0;
  b = 0;
  try{
    FileReader fr = new FileReader(evolution);
    BufferedReader br = new BufferedReader(fr);

    String str = br.readLine();
    int rows = parseRows(str);
    int i = 1;

    //advance to the relevant line in the file
    while(i <= rows+2){
      str = br.readLine();
      i++;
    }

    //split line into an rbg array = ["REDINFO", "BLUEINFO", "GREENINFO"]
    String[] rgb = str.split(", ");

    //grab integer rgb values from the array
    r = Integer.valueOf(rgb[0]);
    g = Integer.valueOf(rgb[1]);
    b = Integer.valueOf(rgb[2]);
  }
  catch(Exception e){
    //this will never happen
    e.printStackTrace();
  }
  ret = new Color(r,g,b);
  return ret;

}
/////////////////////////////////////////////////////////////////////////////////////////////////////
/** parseFile function. This function takes in a String 'file' and returns the corresponding Life
* object represented by the data in the file.
* @param file, a file containing the data of a Life object
* @return a Life object represented by the data in 'file'
*/
  public Life parseFile(String file){
    String filename = file;
    int rows;
    int cols;
    Life conway = null;
    try{
      FileReader fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(fr);

      String str = br.readLine();

      //prepare parameters for our Life object
      rows = parseRows(str);
      cols = parseCols(str);

      //this should never happen, but we account for it anyway
      if(rows == -1 || cols == -1){
        JOptionPane.showMessageDialog(null,"INVALID FILE FORMAT! Please see the README");
        System.exit(0);
      }

      //can't go over max grid size; 100 x 100
      if(rows > 100 || cols > 100){
        JOptionPane.showMessageDialog(null, "INVALID FILE! Can't have more than 100 rows or cols.");
        System.exit(0);
      }
      int[][] gameBoard = new int[rows][cols];

      int lineNum = 1;
      int gameRow = 0;
      while((str = br.readLine()) != null && lineNum <= rows){
        String[] split = str.split(", ");

        //create a row to add to the board and add it
        int[] row = intArrToStrArr(split,cols);
        for (int gameCol = 0; gameCol < row.length; gameCol++){
          gameBoard[gameRow][gameCol] = row[gameCol];
        }

        gameRow+=1;
        lineNum +=1;

      }
      //initialize conway
      conway = new Life(gameBoard,rows,cols, parseAlive(new File("DEFAULT.txt")), parseDead(new File("DEFAULT.txt")));
      br.close();
    }
    catch(IOException e){
      //we should never have this happen
      System.out.println("File not found!");
    }

    return conway;
  }

//////////////////////////////////////////////////////////////////////////////////////////////////////
  /** write function, used as a helper for our file_write function. This function
  * actually writes our new grid to an input file along with the alive and dead cell
  * colors. The grid file format is further described in the README.
  * @param toWrite, a file in which we will write an input grid (and alive and dead cell colors) to
  * @param grid, a grid to write to the file 'toWrite'
  */
  public void write_grid(int[][] grid, File toWrite){
    try{
      FileWriter fw = new FileWriter(toWrite);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(grid.length + ", " + grid[0].length + "\n");
      //write the grid to the output file
      for(int i = 0; i < grid.length; i++){
        for(int j = 0; j < grid[0].length; j++){
          if(j < grid[0].length-1)
            bw.write(grid[i][j] + ", ");
          else
            bw.write(grid[i][j]+"\n");
        }
      }

      //grab alive and dead cell RGB values
      int alive_r = conway.getAliveColor().getRed();
      int alive_g = conway.getAliveColor().getGreen();
      int alive_b = conway.getAliveColor().getBlue();

      int dead_r = conway.getDeadColor().getRed();
      int dead_g = conway.getDeadColor().getGreen();
      int dead_b = conway.getDeadColor().getBlue();

      //write alive and dead RGB values after grid was written
      bw.write(alive_r + ", " + alive_g + ", " + alive_b + "\n");
      bw.write(dead_r + ", " + dead_g + ", " + dead_b);

      //close our writer
      bw.close();
    }
    catch(Exception e){
      //this will never happen
      e.printStackTrace();
    }
  }
}

class MyComponent extends JComponent{

  //instance variables
  private int screenHeight;
  private int screenWidth;
  Life game_of_life;

    /** MyComponent constructor
    * @param conway a life object to link to this.game_of_life
    */
    public MyComponent(Life conway){
      //grab the screen dimensions
      Toolkit kit = Toolkit.getDefaultToolkit();
      Dimension screenSize = kit.getScreenSize();
      this.screenHeight = screenSize.height/2;
      this.screenWidth = screenSize.width/2;
      //initialize our game_of_life embedded object
      game_of_life = conway;
    }

    /** paintComponent function. This function paints our grid in our
    * GUI when the program is started and when repaint() is called.
    * @param g a Graphics object
    */
    public void paintComponent(Graphics g){
      var g2 = (Graphics2D) g;
      //draw outside grid frame/rectangle
      g2.draw(new Rectangle2D.Double(0,10,screenWidth,screenHeight*0.8));

      //construct our board
      construct_board(g,0,10,(screenWidth),(screenHeight*0.8));

      }

      /** construct_board function. This function takes in 5 parameters: the top left coordinates (x and y) of our grid
      * rectangle, the rectangle's width and height, along with the graphics object in paintComponent. This function
      * splits up this rectangle appropriately with lines and fills in each cell to match the state of this.game_of_life
      * @param g a Graphics object from paintComponent
      * @param x the x coordinate of the grid frame/rectangle
      * @param y the y coordinate of the grid frame/rectangle
      * @param rectWidth the width of the grid frame/rectangle
      * @param rectHeight the height of the grid frame/rectangle
      */
      public void construct_board(Graphics g, double x, double y, double rectWidth, double rectHeight){
        int[][] board = game_of_life.getBoard();
        var g2 = (Graphics2D) g;
        double incremental_width = rectWidth / this.game_of_life.getCols();
        double incremental_height = rectHeight / this.game_of_life.getRows();
        double startWidth = x;
        double startHeight = y;
        //filling the grid appropriately
        int i = 0;
        while(startHeight <= y+rectHeight){
          startWidth = x;
          int j = 0;
          while(startWidth <= rectWidth){

              // taking advantage of lazy boolean evaluation =)
              if(i < board.length && j < board[0].length && board[i][j] == 0){

                //dead cells are colored with the game_of_life dead cell color
                g2.setColor(game_of_life.getDeadColor());

                //draw a cell
                g2.fill(new Rectangle2D.Double(startWidth,startHeight,incremental_width,incremental_height));
              }
              else if (i < board.length && j < board[0].length && board[i][j] == 1){

                //set appropriate color
                g2.setColor(game_of_life.getAliveColor());

                //draw a cell
                g2.fill(new Rectangle2D.Double(startWidth,startHeight,incremental_width,incremental_height));
              }

            startWidth += incremental_width;
            j+=1;
          }
          i+=1;
          startHeight += incremental_height;
        }

        //DRAW LINES on top of rectangles to distinguish them
        startWidth = x;
        startHeight = y;
        while(startWidth <= rectWidth){
          g.setColor(Color.BLACK);
          g2.draw(new Line2D.Double(startWidth,y,startWidth,y+rectHeight));
          startWidth+=incremental_width;
        }
        //drawing HORIZONTAL LINES
        while(startHeight <= y+rectHeight){
          g.setColor(Color.BLACK);
          g2.draw(new Line2D.Double(x,startHeight,x+rectWidth,startHeight));

          startHeight+=incremental_height;
        }
       }
}
