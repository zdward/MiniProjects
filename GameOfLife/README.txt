Conway's Game of Life Simulator README (Zachary Ward)
====================================================================================================
**NOTE** README and USER MANUAL are combined into this file.

README:
_____________________________________________________________________________________________________

All contents in the provided zip file (this directory) are necessary for successful compilation / run of this program. If the wrong file is deleted, compilation errors may occur. That being said, as long as nothing is deleted, everything will be okay!

To run the program, use the command line to cd into the given folder (this folder). Then, simply run

./make.sh

Upon running this command, all necessary documentation will be generated in a sub-folder called docs
and an instance of the program will be run. 


For further instructions on how to use the program, please see USER MANUAL.



USER MANUAL:
____________________________________________________________________________________________________

INITIAL STATE:

Upon first opening the application, you should be presented with an empty
10 x 10 board. This is because DEFAULT.txt is loaded with a 10 x 10 empty board.

You can play with the size of this empty board by going into Options > Config Panel.

After you open the Config Panel, you can change the number of rows or number of cols shown initially.

LOADING FILES:

If you want to load in a board to the simulator, you must go into File > Load Grid

The default path for the file explorer is the subfolder in the GameOfLife folder known as LoadableGrids.

You can change this path to whatever folder you like in the Config Panel.

Once you are in your desired path, you can load in a grid folder by double clicking the file.

The requirements for a successful load are the following:

- The file should be of the form "gridNUMBER.txt", where NUMBER denotes a non-negative integer.

- The format of the file must be consistent with the format of inputs for HW1's conway's game of life
	(see grids in LoadableGrids as examples). 

- Two lines of RGB values formatted like this: (255, 0, 255) must be present in the file denoting the alive cell colors and dead cell colors

- Overall, the file must be VALID for a successful load.

- Feel free to create testcases or input grids to load in. I created a subfolder called GraderInputFiles for you to store them.
However, you do not have to use this folder.

EXAMPLE FILE TEXT:
10, 10
1, 1, 0, 0, 0, 0, 0, 0, 1, 1
0, 1, 1, 0, 0, 1, 1, 0, 0, 1
0, 0, 0, 0, 0, 1, 1, 0, 0, 0
0, 0, 0, 0, 1, 0, 0, 0, 0, 0
0, 0, 0, 1, 1, 1, 0, 0, 0, 1
1, 1, 0, 0, 1, 0, 0, 0, 0, 0
0, 0, 0, 0, 0, 0, 0, 1, 0, 0
0, 1, 0, 0, 0, 0, 1, 1, 0, 0
1, 1, 0, 0, 0, 0, 1, 1, 1, 0
1, 1, 0, 0, 0, 0, 0, 0, 0, 0
255, 0, 0
255, 192, 203


SAVING FILES:

To save an output grid, or multiple output grids, go to File > Save Grid(s).

You will then be prompted with a starting simulation tick. Enter a non-negative integer.

You will then be prompted with an ending simulation tick. Enter a non-negative integer once more, greater than your starting tick.

Finally, you will be prompted with an output file pattern name. Simply enter a String of your choice.

You cannot change the path of saved files. They are saved always in the subfolder known as 'SavedGrids.' (user/.../GameOfLife/SavedGrids)

SIMULATING:

Once you have loaded in a grid, you can move to the next generation (as defined by conway's game of life rules) by clicking the 'Next Generation' button.

You can move back to the previous generation by clicking the 'Prev Generation' button.

To jump to a simulation tick of your choice, click the "Jump to Tick" button and enter a non-negative integer.

When you jump forward one tick, all ALIVE cells get darker. This is a nice added effect to make the GUI more visually appealing. Eventually, all ALIVE cells will be black.

STATISTICS:

To see some statistics regarding the simulation, go into Options > Statistics.

A panel will then come up displaying important simulation statistics. Each time you advance or go back a generation, you must close the window and re-open it, as it does not refresh.

CONFIGURATION PANEL:

To change the load grid default path, you can press the "choose" button in the panel. You can then choose any subfolder in user/.../GameOfLife/ that you like or you can change the directory in the JFileChooser and pick whichever folder you like as a default path.

You can set alive and dead cell colors with the drop-down boxes.

To set the default grid size, you can enter numbers for "Grid Rows" and Grid Cols". Be careful, however, as if you fill in valid values for these boxes, the grid will reset, erasing all simulation data. 

Max Size for a grid is 100 by 100. My code will stop you from trying anything bigger.

We note that these features are mostly for show, as when you load in a new grid, the size of the board adjusts accordingly.

However, if you set alive and dead cell colors, leave the "Grid Cols" and "Grid Rows" boxes EMPTY, and click APPLY CHANGES, you will see the cells change color in real-time.

One last note, if you do decide to change the cell colors through the configuration panel,
you can simply click "Prev Generation" to undo your change.


