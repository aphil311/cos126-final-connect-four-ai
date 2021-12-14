COS126 Final Project: Implementation

Please complete the following questions and upload this readme.txt to the
TigerFile assignment for the "Final Project Implementation".


/**********************************************************************
 * Basic Information                                                  *
 **********************************************************************/

Name 1: Erin Lee

NetID 1: el5919

Name 2: Aidan Phillips

NetID 2: ap8914

Project preceptor name: Alan Kaplan

Project title: Connect 4

CodePost link for proposal feedback:

Link to project video: https://youtu.be/gj2KznukrCY

Approximate number of hours to complete the final project
Number of hours:

/**********************************************************************
 * Required Questions                                                 *
 **********************************************************************/

Describe your project in a few sentences.

Our project is a Connect 4 with a GUI with two modes of gameplay: one-player,where the user faces off against an AI, and two-player, where two users alternate playing in real time. Users play with 


Describe in detail your three features and where in your program
they are implemented (e.g. starting and ending line numbers,
function names, etc.).

1. Users can interact with the program to add checkers to a column of their choice. We would have users either input a number corresponding to a column through the command line or have them click buttons which would lie underneath the grid of the connect 4.

Implementation for adding checkers is in Board.java method insert, lines 67-76. 
In the GUI, this occurs in GameVisualizer.java when the user interacts with the GUI and clicks a button, which is handled in lines 88-170, but more specifically 117-154, which is the button handler.

2. The board will update according to user input and show updated layouts until it detects that one of the players has won or that the game is at a tie. We would have the program check whether or not a certain pattern is fulfilled by a color by iterating through and see which colors correspond to each slot in the grid. This would occur after every move.

Implementation for checking the state of the board is throughout lines 53-299 of Board.java, which checks for possible winning scenarios, adds checkers to the board (as desribed in the first feature), and also checks when the board is full. This is implemented in the GUI in method checkStatus, lines 61-72 of GameVisualizer.

3. Two modes of play - first mode is the "simple" one based on random choices by the computer. Second model is "more intelligent" and uses some extended logic to pay smarter.

This feature was modified in the case that we were unable to execute a more intelligent mode of play. We were ultimately able to create the latter feature rather than the simpler, randomized computer player. Implementation for the two modes of play is introduced in GameVisualizer method start on lines 173-214. Method handleGame handles the mode of play through the boolean isOnePlayer. The computer player is executed on lines 156-163. The setup of the AI is in all of MonteCarlo.java, which plays "intelligently."


Describe in detail how to compile and run your program. Include a few example
run commands and the expected results of running your program. For non-textual
outputs (e.g. graphical or auditory), feel free to describe in words what the
output should be or reference output files (e.g. images, audio files) of the
expected output.

JavaFX has already been included in the project. To run the program, compile with the command `javac --module-path javafx-sdk-17.0.1/lib --add-modules javafx.controls GameVisualizer.java` and then run it with `java --module-path javafx-sdk-17.0.1/lib --add-modules javafx.controls GameVisualizer`

The output should be a new window with the opening screen prompting the user for a mode of play.


Describe how your program accepts user input and mention the line number(s) at
which your program accepts user input.

GUI: our program accepts user input when the user clicks on a button This is executed in GameVisualizer, seen in lines 188-203, which accepts input for the gameplay mode, lines 50-55, which accepts input for restarting the game, and lines 117-147, which accepts input for adding checkers to the columns.


Describe how your program produces output based on user input (mention line
numbers).

GUI: our program produces output by changing the view of the board, seen in the method changeCircle in GameVisualizer on lines 36-59. It also produces output when alerting the user of a game's outcome, seen in lines 61-85.


Describe the data structure your program uses and how it supports your program's
functionality (include the variable name and the line number(s) at which it is
declared and initialized).

GameVisualizer:
Our program uses a 1D array to store information for GUI components such as buttons (line 28, stored line 168) or board coordinates (line 128 / 160). It uses a 2D array to store the Circle objects (initialized at line 26, filled in lines 100-108).


List the two custom functions written by your project group, including function
signatures and line numbers; if your project group wrote more than two custom
functions, choose the two functions that were most extensively tested.
1. public int[] insert(int c, int p) @ line 67 in Board.java

2. 

List the line numbers where you test each of your two custom functions twice.
For each of the four tests (two for each function), explain what was being
tested and the expected result. For non-textual results (e.g. graphical or
auditory), you may describe in your own words what the expected result
should be or reference output files (e.g. images, audio files).
1. Lines 330 and 331, tests the method's ability to stack result should have a 1 and 2 in the second column

2. Line 362, rests insertin an a full column, throws an error

3.

4.


/**********************************************************************
 * Citing Resources                                                   *
 **********************************************************************/

List below *EVERY* resource your project group looked at (bullet lists and
links suffice).

- https://openjfx.io/openjfx-docs/
- https://docs.oracle.com/javafx/2/get_started/hello_world.htm
- https://docs.oracle.com/javase/8/javafx/api/index.html
- https://www.youtube.com/watch?v=UXW2yZndl7U&ab_channel=JohnLevine
- https://www.educative.io/blog/data-structures-trees-java

Remember that you should *ALSO* be citing every resource that informed your code at/near the line(s) of code that it informed.

Did you receive help from classmates, past COS 126 students, or anyone else?
If so, please list their names.  ("A Sunday lab TA" or "Office hours on
Thursday" is ok if you don't know their name.)

No.


Did you encounter any serious problems? If so, please describe.

No.



List any other comments here.




/**********************************************************************
 * Submission Checklist                                               *
 **********************************************************************/

Please mark that you’ve done all of the following steps:
[ ] Created a project.zip file, unzipped its contents, and checked that our
    compile and run commands work on the unzipped contents. Ensure that the .zip
    file is under 50MB in size.
[ ] Created and uploaded a Loom or YouTube video, set its thumbnail/starting
    frame to be an image of your program or a title slide, and checked that
    the video is viewable in an incognito browser.
[ ] Uploaded all .java files to TigerFile.
[ ] Uploaded project.zip file to TigerFile.

After you’ve submitted the above on TigerFile, remember to do the following:
[ ] Complete and upload readme.txt to TigerFile.
[ ] Complete and submit Google Form
    (https://forms.cs50.io/de2ccd26-d643-4b8a-8eaa-417487ba29ab).


/**********************************************************************
 * Partial Credit: Bug Report(s)                                      *
 **********************************************************************/

For partial credit for buggy features, you may include a bug report for at
most 4 bugs that your project group was not able to fix before the submission
deadline. For each bug report, copy and paste the following questions and
answer them in full. Your bug report should be detailed enough for the grader
to reproduce the bug. Note: if your code appears bug-free, you should not
submit any bug reports.

BUG REPORT #1:
1. Describe in a sentence or two the bug.




2. Describe in detail how to reproduce the bug (e.g. run commands, user input,
   etc.).




3. Describe the resulting effect of bug and provide evidence (e.g.
   copy-and-paste the buggy output, reference screenshot files and/or buggy
   output files, include a Loom video of reproducing and showing the effects of
   the bug, etc.).




4. Describe where in your program code you believe the bug occurs (e.g. line
   numbers).




5. Please describe what steps you tried to fix the bug.





/**********************************************************************
 * Extra Credit: Runtime Analysis                                     *
 **********************************************************************/

You may earn a small amount of extra credit by analyzing the efficiency of one
substantial component of your project. Please answer the following questions if
you would like to be considered for the extra credit for program analysis
(remember to copy and paste your answers to the following questions into the
Google form as well for credit).

Specify the scope of the component you are analyzing (e.g. function name,
starting and ending lines of specific .java file).




What is the estimated runtime (e.g. big-O complexity) of this component?
Provide justification for this runtime (i.e. explain in your own words why
you expect this component to have this runtime performance).




Provide experimental evidence in the form of timed analysis supporting this
runtime estimate. (Hint: you may find it helpful to use command-line
arguments/flags to run just the specified component being analyzed).





/**********************************************************************
 * Extra Credit: Packaging project as an executable .jar file         *
 **********************************************************************/

You may earn a small amount of extra credit by packaging your project as an
executable .jar file. Please answer the following question if you would like to
be considered for this extra credit opportunity (remember to copy and paste your
answers to the following questions into the Google form as well for credit).

Describe in detail how to execute your .jar application (e.g. what execution
command to use on the terminal). Include a few example execution commands and
the expected results of running your program. For non-textual outputs
(e.g. graphical or auditory), feel free to describe in words what the output
should be or reference output files (e.g. images, audio files) of the expected
output.
