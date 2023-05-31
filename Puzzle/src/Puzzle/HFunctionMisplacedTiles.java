package Puzzle;

import sac.State;
import sac.StateFunction;

public class HFunctionMisplacedTiles extends StateFunction {
    @Override
    public double calculate(State state){
        SlidingPuzzle slidingPuzzle = (SlidingPuzzle) state;
        double h = 0.0;
        int cnt = 0;
        for (int i = 0; i<slidingPuzzle.board.length; i++)
            for (int j =0; j<slidingPuzzle.board.length; j++){
                //if (slidingPuzzle.board[i][j] != cnt && slidingPuzzle.emptyIndex[0] != i && slidingPuzzle.emptyIndex[1] != j){
                if ((slidingPuzzle.board[i][j] != cnt) && (slidingPuzzle.board[i][j] != 0)){
                    h+=1.0;
                }
                cnt++;
            }
        return h;
    }
}

