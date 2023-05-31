package Puzzle;

import sac.State;
import sac.StateFunction;

public class Manhattan extends StateFunction {
    @Override
    public double calculate(State state)
    {
        SlidingPuzzle slidingPuzzle = (SlidingPuzzle) state;
        double h = 0.0;
        for(int i =0; i < slidingPuzzle.board.length; i++)
            for (int j = 0; j < slidingPuzzle.board.length; j++)
            {
                if (slidingPuzzle.board[i][j] != 0)
                {
                    h += manhattan(slidingPuzzle,i, j);
                }

            }
        return h;
    }
    protected int manhattan(SlidingPuzzle slidingPuzzle, int index1, int index2)
    {
        int n = SlidingPuzzle.n;

        int i1 = slidingPuzzle.board[index1][index2] / n;
        int j1 = slidingPuzzle.board[index1][index2] % n;
//        int i2 = index1 / n;
//        int i22 = index1 % n;
//        int j2 = index2 /n;
//        int j22 = index2 % n;
        //return  Math.abs(i1-i2 -i22) + Math.abs(j1 - j2 - j22);
        return  Math.abs(i1-index1) + Math.abs(j1 - index2);
    }
}