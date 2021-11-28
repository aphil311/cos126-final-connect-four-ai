# About the Project
This is a playable game of connect four. The final version will include a GUI and one or more opponents. Inspiration is taken from the AlphaGo project which utilized a Monte Carlo Tree Search (MCTS) algorithm to play Go. The AI is being developed seperately in the "MCTS" branch as it requires moderate redesign of the "Board.java" class.

# Usage
**v0.1.0-alpha**  
A compiled .jar file can be found on the releases page along with zipped source code.
In the terminal type  
```
java -jar ConnectFour.jar <mode>
```
where `<mode>` is either "easy" or "ai". Any other arguement will default to a human opponent (user input for both players).

The board is seven units wide and six units tall. When prompted `Column selection: `, type the index of the column to be played. For visual reference:
```
| | | | | | | | 
| | | | | | | |
| | | | | | | |
| | | | | | | |
| | | |x| | | |
|x|o|o|o|x| | |
 0 1 2 3 4 5 6
```

# Documentation
***Screenshot of Javadoc will go here***
### Demo Video:
*demo video will be embedded here*
### Resources looked at:
- https://www.youtube.com/watch?v=UXW2yZndl7U&ab_channel=JohnLevine
- https://www.educative.io/blog/data-structures-trees-java

# Analysis
*Methods will be analyzed for complexity here*
