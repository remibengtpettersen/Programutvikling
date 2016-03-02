# Programutvikling
**************************************
# Semesteroppgave Game of Life
**************************************

s305080 - Truls Stenrud, 
s305061 - Andreas Berg Ophus, 
s305073 - Remi Bengt Pettersen

**************************************

160205 17:38

Created GoL JavaFx project in git repository named "Programutvikling". Shared repository with Truls Stendrud - "Stenrud" and Andreas Berg Ophus - "Andoph" 

160212 12:30

Added packages: controller, model, test, view
Added classes: MasterController, FrontPageController, GameController, ClassicRule, CustomRule, GameBoard, GameBoardTest
Project contains a running verison of GoL, only with gameboard and no functionality other launch.

Changed Rule from interface to abstract
Added unit test for checking the evolve logic.

160224 13:06

Added two unit test for Game of life class. In GameOfLife added listeners for MouseMove, MouseClick, MouseDrag and key pressed. Cleaned up comments and added more data fields. Test application and verified that GUI is behaving as expected.    

160229 11:39

Implemented support for both 2D and 3D gameboards. Changed Rule to interface. Created the abstract class Rule2D, which implements Rule. Added tests for ClassicRule and GameOfLife2D.

160302 11:43

Added mouse input to pan the grid, and to draw on grid/add new cells. Follow-up on javadoc for classes and methods. 