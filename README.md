# About the Project
This is a playable game of connect four. The final version will include a GUI and one or more opponents. Inspiration is taken from the AlphaGo project which utilized a Monte Carlo Tree Search (MCTS) algorithm to play Go. The AI is being developed seperately in the "MCTS" branch as it requires moderate redesign of the "Board.java" class.

# Usage
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
append `-v` in combination with `ai` to use the ai in verbose mode. This prints the ai's preference of column over time as the algorithm is running.

# Documentation
https://aphil311.github.io/cos126-final-javadoc/package-summary.html

### Demo Video:
*demo video will be embedded here*
### Resources looked at:
- [John Levine - Monte Carlo Tree Search](https://www.youtube.com/watch?v=UXW2yZndl7U&ab_channel=JohnLevine)
- [Educative - Java Tree Data Structure](https://www.educative.io/blog/data-structures-trees-java)
- [Java Docs - Arrays Class](https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html)
- [Stack Exchange AI - Interesting Thread on MCTS](https://ai.stackexchange.com/questions/21019/should-monte-carlo-tree-search-be-able-to-consistently-beat-me-in-the-connect-fo)
- [Stack Overflow - Get Native Windows 10 Look and Feel](https://stackoverflow.com/questions/11425103/how-to-get-the-windows-native-look-in-java-gui-programming/11426036)
- [Java Docs - SwingUI](https://docs.oracle.com/javase/tutorial/uiswing/)
- [FlatLaf readme.md](https://github.com/JFormDesigner/FlatLaf)

# Analysis
*Methods will be analyzed for complexity here*
