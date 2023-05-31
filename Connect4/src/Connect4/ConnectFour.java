package Connect4;

import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameState;
import sac.game.GameStateImpl;

import java.util.*;

//ctrl + alt + L

public class ConnectFour extends GameStateImpl {
    public static int row, col;
    public char[][] board;

    public ConnectFour(int row, int col) {
        ConnectFour.row = row;
        ConnectFour.col = col;
        board = new char[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                board[i][j] = '0';
    }

    public ConnectFour(ConnectFour parent) {
        board = new char[row][col];
        for (int i = 0; i < row; i++)
            if (col >= 0) System.arraycopy(parent.board[i], 0, board[i], 0, col);
        setMaximizingTurnNow(parent.isMaximizingTurnNow());
    }

    public String toString() {
        StringBuilder txt = new StringBuilder();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++)
                txt.append("|").append(board[i][j]).append("|");
            txt.append("\n");
        }

        txt.append("---".repeat(Math.max(0, col)));
        txt.append("\n");
        for (int len = 0; len < col; len++) {
//            txt.append("|").append(len).append("|");
            txt.append("|").append(len).append("|");

        }
        txt.append("\n");
//        txt.append("---".repeat(Math.max(0, col)));
        txt.append("---".repeat(Math.max(0, col)));

        return txt.toString();
    }

    public int hashCode() {
        int[] boardFlat = new int[row * col];
        int k = 0;
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                boardFlat[k++] = board[i][j];
        return Arrays.hashCode(boardFlat);
    }

    public void makeMove(int c) {
        if (c < col) {
            for (int d = row - 1; d >= 0; d--) {
                if (board[d][c] == '0') {
                    if (isMaximizingTurnNow()) {
                        board[d][c] = 'R';
                    } else {
                        board[d][c] = 'Y';
                    }
                    break;
                }
            }
            setMaximizingTurnNow(!isMaximizingTurnNow());
        }
    }

    public boolean isSolution() {
        for (int r = 1; r < row - 3; r++) //gora dol
            for (int c = 0; c < col; c++) {
                if (board[r][c] == 'R' && board[r + 1][c] == 'R' &&
                        board[r + 2][c] == 'R' && board[r + 3][c] == 'R') {
                    return true;
                }
                if (board[r][c] == 'Y' && board[r + 1][c] == 'Y' &&
                        board[r + 2][c] == 'Y' && board[r + 3][c] == 'Y')
                    return true;
            }
        for (int r = 2; r < row; r++) //boki
            for (int c = 0; c < col - 3; c++) {
                if (board[r][c] == 'R' && board[r][c + 1] == 'R' &&
                        board[r][c + 2] == 'R' && board[r][c + 3] == 'R')
                    return true;
                if (board[r][c] == 'Y' && board[r][c + 1] == 'Y' &&
                        board[r][c + 2] == 'Y' && board[r][c + 3] == 'Y')
                    return true;
            }

        for (int c = 0; c < board[1].length; c++) { //regula sufitu v2
            if (board[0][c] == 'Y')
                return true;
            if (board[0][c] == 'R')
                return true;
        }


        for (int r = row - 1; r > 2; r--) // slash
            for (int c = 0; c < col - 3; c++) {
                if (board[r][c] == 'Y' && board[r - 1][c + 1] == 'Y' &&
                        board[r - 2][c + 2] == 'Y' && board[r - 3][c + 3] == 'Y')
                    return true;
                if (board[r][c] == 'R' && board[r - 1][c + 1] == 'R' &&
                        board[r - 2][c + 2] == 'R' && board[r - 3][c + 3] == 'R')
                    return true;
            }
        for (int r = row - 1; r > 2; r--) //backslash
            for (int c = col - 1; c > 2; c--) {
                if (board[r][c] == 'Y' && board[r - 1][c - 1] == 'Y' &&
                        board[r - 2][c - 2] == 'Y' && board[r - 3][c - 3] == 'Y')
                    return true;
                if (board[r][c] == 'R' && board[r - 1][c - 1] == 'R' &&
                        board[r - 2][c - 2] == 'R' && board[r - 3][c - 3] == 'R')
                    return true;
            }

        return false;
    }

    public List<GameState> generateChildren() {
        List<GameState> list = new ArrayList<>();
        if (isSolution())
            return list;
        for (int i = 0; i <= board[1].length - 1; i++) {
            ConnectFour child = new ConnectFour(this);
            child.makeMove(i);
            list.add(child);
            child.setMoveName(Integer.toString(i));
        }
        return list;
    }

    public static void main(String[] args) {
        ConnectFour plansza = new ConnectFour(6, 7);
        plansza.setMaximizingTurnNow(plansza.isMaximizingTurnNow());
        Scanner scanner = new Scanner(System.in);
        ConnectFour.setHFunction(new Heuristic());
        GameSearchAlgorithm algo = new AlphaBetaPruning();
        algo.setInitial(plansza);
        System.out.println(plansza);
        while (!plansza.isSolution()) {
            if (plansza.isMaximizingTurnNow()) {
                System.out.println("Wpisz numer kolumny od 0 do " + (col - 1) + ":");
                while (true) {
                    int pMove = scanner.nextInt();
                    if (pMove < 0 || pMove >= col)
                        System.out.print("Zly numer! Wpisz jeszcze raz");
                    else {
                        plansza.makeMove(pMove);
                        System.out.println(plansza);
                        break;
                    }

                }
            } else {
                algo.execute();
                Map<String, Double> bestMoves = algo.getMovesScores();
                for (Map.Entry<String, Double> entry : bestMoves.entrySet())
                    System.out.println(entry.getKey() + " -> " + entry.getValue());
                int aiMove = Integer.parseInt(algo.getFirstBestMove());
                plansza.makeMove(aiMove);
                System.out.println(plansza);
            }

        }
        if (!plansza.isMaximizingTurnNow())
            System.out.println("Wygrales! :)");
        else {
            System.out.println("Wygralo AI!");
        }
        //player = 'R'
        // ai = 'Y'
        //slash
//        plansza.makeMove(0);
//        plansza.makeMove(1);
//        plansza.makeMove(1);
//        plansza.makeMove(2);
//        plansza.makeMove(2);
//        plansza.makeMove(3);
//        plansza.makeMove(2);
//        plansza.makeMove(3);
//        plansza.makeMove(3);
//        plansza.makeMove(5);
//        plansza.makeMove(3);


//backslash
//        plansza.makeMove(3);
//        plansza.makeMove(2);
//        plansza.makeMove(2);
//        plansza.makeMove(1);
//        plansza.makeMove(0);
//        plansza.makeMove(1);
//        plansza.makeMove(1);
//        plansza.makeMove(0);
//        plansza.makeMove(0);
//        plansza.makeMove(4);
//        plansza.makeMove(0);
//        Scanner sc = new Scanner(System.in);
//        System.out.println(plansza);
//        ConnectFour.setHFunction(new Heuristic());
//        GameSearchAlgorithm algo = new AlphaBetaPruning();
//        algo.setInitial(plansza);
//        while (!plansza.isSolution()) {
//            if (plansza.turn) {
//                System.out.println("Wpisz numer kolumny od 0 do " + (col - 1) + ":");
//                while (true) {
//                    int playerMove = sc.nextInt();
//                    if (playerMove < 0 || playerMove >= col) {
//                        System.out.println("Zły numer! Wpisz jeszcze raz");
//                    }
//                    else {
//                        plansza.makeMove(playerMove);
//                        break;
//                    }
//                }
//                System.out.println(plansza);
//            } else {
//                algo.execute();
//                Map<String, Double> bestMoves = algo.getMovesScores();
//                for (Map.Entry<String, Double> ent : bestMoves.entrySet())
//                    System.out.println(ent.getKey() + " -> " + ent.getValue());
//                String aim = algo.getFirstBestMove();
////                int help = Integer.parseInt(aim);
//                plansza.makeMove(Integer.parseInt(aim));
//                System.out.println(plansza);
//            }
//        }

//        System.out.println(plansza);
//        System.out.println(plansza.isSolution());
    }
}
