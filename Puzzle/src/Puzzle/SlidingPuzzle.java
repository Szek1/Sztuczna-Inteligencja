package Puzzle;

import sac.game.RefutationTableAsHashMap;
import sac.graph.AStar;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;

import java.util.*;


public class SlidingPuzzle extends GraphStateImpl{
    protected static byte n;
    protected int[] emptyIndex = {0,0};
    protected byte up = 1;
    protected byte down = 2;
    protected byte left = 3;
    protected byte right = 4;
    protected byte [][] board;

    public SlidingPuzzle(int n){
        SlidingPuzzle.n = (byte) n;
        byte cnt = 0;
        board = new byte[n][n];
        for (byte i = 0; i<n; i++)
            for (byte j = 0; j<n; j++) {
                board[i][j] = cnt++;
            }
    }
    public SlidingPuzzle(SlidingPuzzle parent){
        board = new byte[n][n];
        for (byte i = 0; i<n; i++)
            for (byte j = 0; j<n; j++) {
                board[i][j] = parent.board[i][j];
            }
        this.emptyIndex[0] = parent.emptyIndex[0];
        this.emptyIndex[1] = parent.emptyIndex[1];
    }

    public String toString(){
        StringBuilder txt =new StringBuilder();
        for(int i = 0; i<n; i++){
            for(int j =0;j<n; j++)
                txt.append(board[i][j]+",");
            txt.append("\n");
        }
        return txt.toString();
    }
    public boolean isMovePossible (int i)
    {
        if(i==1) // gora
            return (emptyIndex[0] - 1 >= 0);
        else if (i == 2) //dol
            return (emptyIndex[0] + 1 <= n-1);
        else if (i == 3)//lewo
            return (emptyIndex[1] - 1 >= 0);
        else if (i == 4) //prawo
            return (emptyIndex[1] + 1 <= n-1);

        return false;
    }
    public void makeMove(int mov) {
        SlidingPuzzle temp = new SlidingPuzzle(this);
        if (mov==1)//gora
        {
            board[emptyIndex[0] - 1][emptyIndex[1]] = 0;
            board[emptyIndex[0]][emptyIndex[1]] = temp.board[emptyIndex[0]-1][emptyIndex[1]];
            emptyIndex[0] -= 1;
        }
        else if (mov==2)//dol
        {
            board[emptyIndex[0] + 1][emptyIndex[1]] = 0;
            board[emptyIndex[0]][emptyIndex[1]] = temp.board[emptyIndex[0]+1][emptyIndex[1]];
            emptyIndex[0] += 1;
        }
        else if (mov == 3)//lewo
        {
            board[emptyIndex[0]][emptyIndex[1] - 1] = 0;
            board[emptyIndex[0]][emptyIndex[1]] = temp.board[emptyIndex[0]][emptyIndex[1]-1];
            emptyIndex[1] -= 1;
        }
        else if (mov == 4)//prawo
        {
            board[emptyIndex[0]][emptyIndex[1]+1] = 0;
            board[emptyIndex[0]][emptyIndex[1]] = temp.board[emptyIndex[0]][emptyIndex[1]+1];
            emptyIndex[1] +=1;
        }
    }
    public void shuffle(int howManyMoves)
    {
        int i = 0;
        Random r = new Random();
        while (i<howManyMoves)
        {
            int los = r.nextInt(5);
            if (isMovePossible(los))
            {
                makeMove(los);
                i++;
            }
        }
    }
    @Override
    public List<GraphState> generateChildren()
    {
        List<GraphState> list = new ArrayList<>();
        if (isSolution())
            return list;
        for (byte i = 1; i<= 4; i++){
            SlidingPuzzle child = new SlidingPuzzle(this);
            if (child.isMovePossible(i)){
                child.makeMove(i);
                list.add(child);
                String moveName = "";
                if (i == 1)
                    moveName = "U";
                else if (i == 2)
                    moveName = "D";
                else if (i == 3)
                    moveName = "L";
                else
                    moveName = "R";
                child.setMoveName(moveName);
            }
        }
        return list;
    }
    public int hashCode(){
        byte[] puzzleFlat = new byte[n * n];
        int k = 0;
        for (int i =0; i<n;i++)
            for (int j = 0; j<n; j++)
                puzzleFlat[k++] = board[i][j];
        return Arrays.hashCode(puzzleFlat);
    }
    @Override
    public boolean isSolution()
    {
        byte cnt = 0;
        for (byte i = 0; i<n; i++)
            for (byte j = 0; j<n; j++)
            {
                if(board[i][j] != cnt)
                    return false;
                cnt++;
            }
        return true;
    }
    public static void main(String[] args) {
        SlidingPuzzle slidingPuzzle = new SlidingPuzzle(3);
        slidingPuzzle.shuffle(1000);

        System.out.println("Puzzle do rozwiazania:\n" + slidingPuzzle);
//        slidingPuzzle.setHFunction(new HFunctionMisplacedTiles());
     slidingPuzzle.setHFunction(new Manhattan());
        GraphSearchAlgorithm algorithm = new AStar(slidingPuzzle);
        algorithm.execute();
        GraphState solution = algorithm.getSolutions().get(0);

     List<GraphState> path = solution.getPath();
        System.out.println("Sciezka rozwiazania\n-----");
     for (GraphState pat:path)
     {
         System.out.println(pat);
         System.out.println("-----");
     }


        System.out.println("SOLUTION: \n" + solution);
        System.out.println("PATH LENGTH: " + solution.getPath().size());
        System.out.println("MOVES ALONG PATH:" + solution.getMovesAlongPath());
        System.out.println("CLOSED_STATES: " + algorithm.getClosedStatesCount());
        System.out.println("OPEN STATES:" + algorithm.getOpenSet().size());
        System.out.println("DURATION TIME: " + algorithm.getDurationTime() + " ms");

//        int iloscUkladanek = 100;
//
//        int openMis = 0;
//        int closedMis = 0;
//        int durMis = 0;
//        int pathMis = 0;
//
//        int openMan = 0;
//        int closedMan = 0;
//        int durMan = 0;
//        int pathMan = 0;
//     for (int i = 0; i<iloscUkladanek ;i++)
//     {
//         SlidingPuzzle mspld = new SlidingPuzzle(3);
//         mspld.shuffle(100);
//         SlidingPuzzle.setHFunction(new HFunctionMisplacedTiles());
//         GraphSearchAlgorithm algorithm = new AStar(mspld);
//         algorithm.execute();
//         GraphState solution = algorithm.getSolutions().get(0);
//         openMis += algorithm.getOpenSet().size();
//         closedMis += algorithm.getClosedStatesCount();
//         durMis += algorithm.getDurationTime();
//         pathMis += solution.getPath().size();
//
//         SlidingPuzzle manhattan = new SlidingPuzzle(mspld);
//         SlidingPuzzle.setHFunction(new Manhattan());
//         GraphSearchAlgorithm algoMan = new AStar(manhattan);
//         algoMan.execute();
//         GraphState solMan = algorithm.getSolutions().get(0);
//         openMan += algoMan.getOpenSet().size();
//         closedMan += algoMan.getClosedStatesCount();
//         durMan += algoMan.getDurationTime();
//         pathMan += solMan.getPath().size();
//
    }
//        System.out.println("MisplacedTiles\n-------------------");
//        System.out.println("OPEN STATES: " + openMis/iloscUkladanek);
//        System.out.println("CLOSED STATES: " + closedMis/iloscUkladanek);
//        System.out.println("PATH LENGTH: " + pathMis/iloscUkladanek);
//        System.out.println("DURATION TIME: " + durMis/iloscUkladanek + " ms");
//        System.out.println("-------------------\nManhattan\n-------------------");
//        System.out.println("OPEN STATES: " + openMan/iloscUkladanek);
//        System.out.println("CLOSED STATES: " + closedMan/iloscUkladanek);
//        System.out.println("PATH LENGTH: " + pathMan/iloscUkladanek);
//        System.out.println("DURATION TIME: " + durMan/iloscUkladanek + " ms");
//    }
}
