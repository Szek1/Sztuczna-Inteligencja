package Sudoku;

import sac.State;
import sac.StateFunction;

//EmptyPlacesHeuristics


public class EmptyPlacesHeuristics extends StateFunction{
    public double calculate(State state) {
        Sudoku sudoku = (Sudoku) state;
        return sudoku.getZeros();
    }
}
