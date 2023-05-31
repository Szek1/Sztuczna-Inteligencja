package Sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sac.StateFunction;
import sac.graph.AStar;
import sac.graph.BestFirstSearch;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphSearchConfigurator;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;

//user guide przeczytac preface
public class Sudoku extends GraphStateImpl{
    public static final int  n = 3; // jak duzy kwadrat
    public static final int n2 = n*n; // jak duza cala plansza
    private byte[][] board; 												//wskaznik badz referencja tablica tablic
    private int zeros = n2*n2;
    public Sudoku() {
        board = new byte[n2][n2];
        for(int i =0; i<n2;i++)
            for(int j=0;j<n2;j++)
                board[i][j]=0;
    }
    public Sudoku(Sudoku parent) { //konstruktor kopiujacy
        board = new byte[n2][n2];
        for(int i =0; i<n2;i++)
            for(int j =0;j<n2;j++)
                board[i][j] = parent.board[i][j];
        zeros = parent.zeros;
    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
//		return toString().hashCode(); //java String metoda hashCode sprawdzic
//		return super.hashCode();
        byte[] sudokuFlat = new byte[n2 * n2];
        int k = 0;
        for(int i = 0; i<n2; i++)
            for(int j = 0; j<n2;j++)
                sudokuFlat[k++] =board[i][j];
        return Arrays.hashCode(sudokuFlat);
        // po tym sktocenie czasu do 8.1 s
    }
    @Override
    public String toString() {  //to jest szybsze 10.1 s
        StringBuilder txt = new StringBuilder();
        for(int i =0; i<n2;i++) {
            for(int j=0;j<n2;j++)
                txt.append(board[i][j]+",");
            txt.append("\n");
        }
        return txt.toString();
//	@Override
//	public String toString() { 16.6s
//		String txt ="";
//		for(int i =0; i<n2;i++) {
//			for(int j=0;j<n2;j++)
//				txt += board[i][j]+",";
//			txt +="\n";
//		}
//		return txt;

    }

    private void refreshZeros() {
        zeros = 0;
        for(int i =0;i<n2;i++)
            for(int j = 0; j<n2; j++)
                if(board[i][j]==0)
                    zeros++;
    }
    public int getZeros() {
        return zeros;
    }
    public void fromString(String txt) {
        int k =0;
        for(int i=0; i<n2; i++)
            for(int j=0; j<n2;j++,k++)
                board[i][j]= Byte.valueOf(txt.substring(k,k+1));
        refreshZeros();
    }
    public boolean isLegal(){
        byte[] group = new byte[n2];
        // wiersze
        for (int i =0;i<n2;i++){
            for(int j = 0; j<n2; j++)
                group[j]=board[i][j];
            if (!isGroupLegal(group))
                return false;
        }
        for (int i =0;i<n2;i++){
            for(int j = 0; j<n2; j++)
                group[j]=board[j][i];
            if (!isGroupLegal(group))
                return false;
        }
        for(int i=0; i<n; i++)
            for(int j =0; j<n; j++) {
                int q =0;
                for(int k =0; k<n; k++)
                    for(int l =0;l<n;l++)
                        group[q++] = board[i*n+k][j*n+l];
                if(!isGroupLegal(group))
                    return false;

            }
        return true;
    }

    private  boolean isGroupLegal(byte [] group){
        boolean[] visited = new boolean[n2 + 1];
        for(int i=1; i<visited.length;i++)
            visited[i]=false;
        for(int i=0; i<n2;i++)
            if(group[i]>0){
                if(visited[group[i]])
                    return false;
                visited[group[i]] = true;
            }
        return  true;
    }

    @Override
    public List<GraphState> generateChildren() {
        // TODO Auto-generated method stub
        List<GraphState> children = new ArrayList();
        int i=0, j=0;
        zeroFinder:
        for(;i<n2;i++)
            for(j=0;j<n2;j++)
                if(board[i][j]==0)
                    break zeroFinder;
        if(i==n2)
            return children;
        for(int k = 1; k <= n2; k++) {
            Sudoku child = new Sudoku(this);
            child.board[i][j] =(byte)k;
            if(child.isLegal()) {
                children.add(child);
                child.zeros--;
            }
        }
        return children;
    }
    @Override
    public boolean isSolution() {
        // TODO Auto-generated method stub
        return ((zeros==0) && (isLegal()));
    }

    public static void main(String[] args) {
//        String sudokuAsTxt = "000000907"
//                + "000420100"
//                + "000705026"
//                + "100004000"
//                + "050000040"
//                + "000507009"
//                + "020100000"
//                + "034059000"
//                + "507000000";
		String sudokuAsTxt = "000000907"
				+ "000420180"
				+ "000705026"
				+ "100904000"
				+ "050000040"
				+ "000507009"
				+ "920108000"
				+ "034059000"
				+ "507000000";
        Sudoku s = new Sudoku();
        s.fromString(sudokuAsTxt);// dla 9x9
        System.out.println(s);
        System.out.println(s.isLegal());
        System.out.println(s.zeros);
        AStar a = new AStar();
//        Sudoku.setHFunction(new EmptyPlacesHeuristics());
        GraphSearchConfigurator conf = new GraphSearchConfigurator();
//        conf.setWantedNumberOfSolutions(Integer.MAX_VALUE);

        GraphSearchAlgorithm algo = new BestFirstSearch(s,conf);
        algo.execute();
        List<GraphState> solutions = algo.getSolutions();
//        for(GraphState sol:solutions) {
//            System.out.println(sol);
//            System.out.println("----");
//        }
        System.out.println("Time[ms]: " + algo.getDurationTime());
        System.out.println("Closed: " + algo.getClosedStatesCount());
        System.out.println("Open: " + algo.getOpenSet().size());
        System.out.println("Solutions: " + solutions.size());

//		Object o1 = new Object();
//		Object o2 = new Object();
//		Object o3 = o1; //badanie 1
//		String o1 = new String("abc");
//		String o2 = new String("abc"); //badanie 2
//		int [] o1 = new int[] {10,20,30};
//		int [] o2 = new int[] {10,20,30}; //badanie 3
//		System.out.println(o1.hashCode());
//		System.out.println(o2.hashCode());
//		System.out.println(Arrays.hashCode(o1));
//		System.out.println(Arrays.hashCode(o2)); //badanie 4
        //		System.out.println(o3.hashCode());
    }
}

