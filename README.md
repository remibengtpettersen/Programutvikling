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

160309 11:32 

Changed GUI, added menubar and toolbar, minimal design. Added dynamic canvas to fit window when size is altered. Implemented draw line method for continuous drawing on the game of life grid. 

160311 12:56

Added support for zooming with mouse scroll and a cell counter. 

160311 16:34
Added file-parser to import game of life patterns.

160314 10:30
Refactored FileParser.java, added PatternFormatException class.

160314 10:02
Refactored name for FileParser to PatternParser.

160314 11:27
Added method in PatternParser for importing patterns from the web, and modified the rest of the class to allow this

160315 14:15
Implemented support for reading GUI and game configurations from file. File is stored in ./resources. File name is config.properties. For this to work resources folder need to be set to "Source Root". A folder can be changed to "Source Root" by right clicking and then select "Mark directory as". New class was added to support reading from file. Class name "Configuration". To make this work method "getConfigurationFromFile" and "setConfiguration" must be executed after object is instantiated.

160315 15:24
Implemented support for starting and stopping game from GUI. Had to make "startAnimationTimer" and "stopAnimationTimer" public on CanvasController to implement this feature. Implemented method that will change image and text on "start and pause" button. Since game is starting with a glider going from left to right, button will start with the pause image and text "Pause".

160315 17:28
To be written pauseknappen og sånt :p 

160315 21:06
Implemented feature that will generate a "config.properties" file if it is missing. File will have default content. 

160316 11:10
Fixed bug when reading inputstream from file if file is missing. Added support for reading "canvas.grid" is true or false.

160316 12:31
Implemented a Rules dropdown menu, a custom rule dialog popup, and custom rules. Custom rules are parsed from Bx/Sx notation, and will evolve accordingly. Added the HighLife rule as a class

160318 09:30
Implemented a clearGrid function, available under the Edit dropdown menu. Added some popular, alternative rules (Life Without Death, Seeds, Diamoeba, Replicator, Day and Night) to the rules dropdown menu

160318 13:45
Moved Patterns out of src. Deleted the unreadable patterns. The FileChooser patternChooser will now have the Patterns folder as initial directory.

160329 14:28
Added GIFLib.jar to GoL project.

160329 14:54
Changes so that resource folder does not need to be set as a source folder. Removed method for reading inputsteam from source folder.

160404 09:09
Refactored the rule classes, especially CustomRule. All 2D-rules now have a ruleText which will be returned by toString().Changes so that resource folder does not need to be set as a source folder. Removed method for reading inputsteam from source folder.

160404 09:24
Refactored and updated formatting for all classes in controller package.

160405 12:47
Created test classes for Custom rule, HighLife rule, Configuration, PatternParser and GameOfLife2D. 

160406 22:38
Implemented simple statistics for s305061. The "Show history" button in "Stats", will now open a Linechart which shows population count and population growth

160412 12:14
Added RuleParser, and moved formatRuleText from CustomRule to this class as a static function. Refactored the rule parsing algorithm, now uses regex.

160412 13:48
Removed Rule interface and GameOfLife and GameOfLife3D classes. Renamed Rule2D to Rule, and GameOfLife2D to GameOfLife.

160412 17:10
Added more unit test for Ruls. Refactored Rules and FileParser.Added Exceptions class for Evolve exceptions.

160413 08:26
Added constructor that takes a string argument for PatternFormatException and EvolveException.

160413 11:04
Added more unit test to GameOfLifeTest class. Coverage is now 80%.

160414 09:57
Added RuleFormatException. Several classes refactored and documented with JavaDoc.

160418 11:34
Extracted cell object from canvas controller, created test class and refined zoom function.

160418 11:56
Added Editor for task 6, extended tasks for s305073.

160418 18:19
Implemented onAction launchEditor for editor for extra task -Pattern Editor-. Also included stage for for S305073.

160419 13:21
Implemented statistics for s305061. Merged with master

160421 22:41
Added progress indicator to s305061 statistics

160425 10:05
Implemented dynamic rule classes. The static rule classes and GameOfLife are now deprecated.

160427 10:49
Refactored s305061 StatController. Replaced ProgressIndicator with ProgressBar, which solved all previous bugs. Added busy flag, to prevent creation multiple task