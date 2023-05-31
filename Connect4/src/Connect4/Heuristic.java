package Connect4;


import sac.State;
import sac.StateFunction;

public class Heuristic extends StateFunction {
    public double calculate(State state) {
        ConnectFour connectFour = (ConnectFour) state;
        if (connectFour.isSolution() && connectFour.isMaximizingTurnNow())
            return Double.NEGATIVE_INFINITY;
        else if (connectFour.isSolution() && !connectFour.isMaximizingTurnNow())
            return Double.POSITIVE_INFINITY;
        else {
            double cntP = 0.;
            double cntAI = 0.;
            int row = connectFour.board.length;
            int col = connectFour.board[1].length;
            for (int r = 1; r < row - 3; r++) //gora dol
                for (int c = 0; c < col; c++) {
                    if (connectFour.board[r][c] == 'R' && connectFour.board[r + 1][c] == 'R' &&
                            connectFour.board[r + 2][c] == 'R' && connectFour.board[r + 3][c] == 'R') {
                        cntAI += 2;
                    }
                    if (connectFour.board[r][c] == 'Y' && connectFour.board[r + 1][c] == 'Y' &&
                            connectFour.board[r + 2][c] == 'Y' && connectFour.board[r + 3][c] == 'Y')
                        cntP += 2;
                }
            for (int r = 2; r < row; r++) //boki
                for (int c = 0; c < col - 3; c++) {
                    if (connectFour.board[r][c] == 'R' && connectFour.board[r][c + 1] == 'R' &&
                            connectFour.board[r][c + 2] == 'R' && connectFour.board[r][c + 3] == 'R')
                        cntAI += 2;
                    if (connectFour.board[r][c] == 'Y' && connectFour.board[r][c + 1] == 'Y' &&
                            connectFour.board[r][c + 2] == 'Y' && connectFour.board[r][c + 3] == 'Y')
                        cntP+=2;
                }
            for (int r = row - 1; r > 2; r--) // slash
                for (int c = 0; c < col - 3; c++) {
                    if (connectFour.board[r][c] == 'Y' && connectFour.board[r - 1][c + 1] == 'Y' &&
                            connectFour.board[r - 2][c + 2] == 'Y' && connectFour.board[r - 3][c + 3] == 'Y')
                        cntP += 2;
                    if (connectFour.board[r][c] == 'R' && connectFour.board[r - 1][c + 1] == 'R' &&
                            connectFour.board[r - 2][c + 2] == 'R' && connectFour.board[r - 3][c + 3] == 'R')
                        cntAI += 2;
                }
            for (int r = row - 1; r > 2; r--) //backslash
                for (int c = col - 1; c > 2; c--) {
                    if (connectFour.board[r][c] == 'Y' && connectFour.board[r - 1][c - 1] == 'Y' &&
                            connectFour.board[r - 2][c - 2] == 'Y' && connectFour.board[r - 3][c - 3] == 'Y')
                        cntP += 2;
                    if (connectFour.board[r][c] == 'R' && connectFour.board[r - 1][c - 1] == 'R' &&
                            connectFour.board[r - 2][c - 2] == 'R' && connectFour.board[r - 3][c - 3] == 'R')
                        cntAI += 2;
                }
            for (int c = 0; c < connectFour.board[1].length; c++) { //regula sufitu v2
                if (connectFour.board[0][c] == 'Y')
                    cntP += 10;
                if (connectFour.board[0][c] == 'R')
                    cntAI -= 10;
            }
            double cnt = -(cntAI - cntP);
//            double cnt = 0.;
//            cnt += rows(connectFour);
//            cnt += cols(connectFour);
//            cnt += slash(connectFour);
//            cnt += backslash(connectFour);
//            cnt += ceil(connectFour);
            return cnt;
        }
    }

    public double rows(ConnectFour connectFour) {
        double cnt = 0;
        int row = connectFour.board.length;
        int col = connectFour.board[1].length;
        for (int r = 1; r < row - 3; r++) //gora dol
            for (int c = 0; c < col; c++) {
                if (connectFour.board[r][c] == 'R' && connectFour.board[r + 1][c] == 'R' &&
                        connectFour.board[r + 2][c] == 'R' && connectFour.board[r + 3][c] == 'R') {
                    cnt -= 2;
                }
                if (connectFour.board[r][c] == 'Y' && connectFour.board[r + 1][c] == 'Y' &&
                        connectFour.board[r + 2][c] == 'Y' && connectFour.board[r + 3][c] == 'Y')
                    cnt += 2;
            }

        return cnt;
    }

    public double cols(ConnectFour connectFour) {
        double cnt = 0;
        int row = connectFour.board.length;
        int col = connectFour.board[1].length;
        for (int r = 2; r < row; r++) //boki
            for (int c = 0; c < col - 3; c++) {
                if (connectFour.board[r][c] == 'R' && connectFour.board[r][c + 1] == 'R' &&
                        connectFour.board[r][c + 2] == 'R' && connectFour.board[r][c + 3] == 'R')
                    cnt -= 2;
                if (connectFour.board[r][c] == 'Y' && connectFour.board[r][c + 1] == 'Y' &&
                        connectFour.board[r][c + 2] == 'Y' && connectFour.board[r][c + 3] == 'Y')
                    cnt+=2;
            }
        return cnt;
    }

    public double slash(ConnectFour connectFour) {
        double cnt = 0;
        int row = connectFour.board.length;
        int col = connectFour.board[1].length;
        for (int r = row - 1; r > 2; r--) // slash
            for (int c = 0; c < col - 3; c++) {
                if (connectFour.board[r][c] == 'Y' && connectFour.board[r - 1][c + 1] == 'Y' &&
                        connectFour.board[r - 2][c + 2] == 'Y' && connectFour.board[r - 3][c + 3] == 'Y')
                    cnt += 2;
                if (connectFour.board[r][c] == 'R' && connectFour.board[r - 1][c + 1] == 'R' &&
                        connectFour.board[r - 2][c + 2] == 'R' && connectFour.board[r - 3][c + 3] == 'R')
                    cnt -= 2;
            }
        return cnt;
    }

    public double backslash(ConnectFour connectFour) {
        double cnt = 0;
        int row = connectFour.board.length;
        int col = connectFour.board[1].length;
        for (int r = row - 1; r > 2; r--) //backslash
            for (int c = col - 1; c > 2; c--) {
                if (connectFour.board[r][c] == 'Y' && connectFour.board[r - 1][c - 1] == 'Y' &&
                        connectFour.board[r - 2][c - 2] == 'Y' && connectFour.board[r - 3][c - 3] == 'Y')
                    cnt += 2;
                if (connectFour.board[r][c] == 'R' && connectFour.board[r - 1][c - 1] == 'R' &&
                        connectFour.board[r - 2][c - 2] == 'R' && connectFour.board[r - 3][c - 3] == 'R')
                    cnt -= 2;
            }
        return cnt;
    }

    public double ceil(ConnectFour connectFour) {
        double cnt = 0;
        for (int c = 0; c < connectFour.board[1].length; c++) { //regula sufitu v2
            if (connectFour.board[0][c] == 'Y')
                cnt += 10;
            if (connectFour.board[0][c] == 'R')
                cnt -= 10;
        }
        return cnt;
    }
}