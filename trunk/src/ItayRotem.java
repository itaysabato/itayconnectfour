/**
 * Names: Itay Sabato, Rotem Barzilay <br/>
 * Logins: itays04, rotmus  <br/>
 * IDs: 036910008, RID  <br/>
 * Date: 13/11/2010  <br/>
 * Time: 15:52:40  <br/>
 */
public class ItayRotem implements Player {

    private static final int MAX_DEPTH = 10;

    int remainingDepth = MAX_DEPTH;
    Checker[][] currentState = null;
    ChildChooser chooser = null;
    Checker myColor = null;
    Checker hisColor = null;
    int[][] table = null;
    int rows = 0;
    int cols = 0;

//    Board b;        //


    public int getBestResponse(Checker[][] state, Checker myColor) {
//        this.b = b;           //
        if(table == null){
            rows =  state.length;
            cols = state[0].length;
            this.myColor = myColor;
            chooser = new ChildChooser(cols);
            if(myColor!=Checker.RED) hisColor = Checker.RED ;
            else hisColor = Checker.BLACK ;
            buildTable();
        }
        currentState = state;
        return bestChild();
    }

    private void buildTable() {
        table = new int[rows][cols];

        int row = 0,col = 0, k= 0;
        for(row=0;row<table.length;row++)
            for(col=0;col<table[0].length;col++)
                table[row][col] = 0;

        for(row=0;row<table.length;row++)
            for(col=0;col<table[0].length;col++) {
                //horizontal check
                if(row+3<table.length) {
                    for(k=0;k<=3;k++)
                        table[row+k][col]++;
                }
                //vertical check
                if(col+3<table[0].length) {
                    for(k=0;k<=3;k++)
                        table[row][col+k]++;
                }
                //diagonal1 check
                if(row+3<table.length && col+3<table[0].length) {
                    for(k=0;k<=3;k++)
                        table[row+k][col+k]++;
                }
                //diagonal2 check
                if(row-3>=0 && col+3<table[0].length) {
                    for(k=0;k<=3;k++)
                        table[row-k][col+k]++;
                }
            }
    }

    private int bestChild() {
        int alpha =  Integer.MIN_VALUE;
        int tempRow;
        int v = Integer.MIN_VALUE;
        int bestChild = 0;

        for( int i = 0; i < cols; i++) {
            tempRow = dropChecker(i,myColor);
            if(tempRow==-1) continue;

            remainingDepth--;
            int m =  minValue(alpha, Integer.MAX_VALUE,tempRow,i);
            remainingDepth++;
            currentState[tempRow][i] = Checker.EMPTY;

            if(v <= m ) {
                v = m;
                bestChild = i;
            }
        }
        return bestChild;
    }


    private int maxValue(int alpha, int beta,int row,int col) {
        Checker winner = identifyWinner(row,col);

        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();

        int tempRow;
        int v = Integer.MIN_VALUE;
        for( int i = 0; i < cols; i++) {
            tempRow = dropChecker(i,myColor);
            if(tempRow==-1) continue;

            remainingDepth--;
            v = Math.max(minValue(alpha, beta,tempRow,i), v);
            remainingDepth++;
            currentState[tempRow][i] = Checker.EMPTY;

            if(v >= beta) {
                return v;
            }
            alpha = Math.max(v,alpha);
        }
        return v;
    }

    private int minValue(int alpha, int beta, int row,int col) {
        Checker winner = identifyWinner(row,col);

        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();

        int tempRow;
        int v = Integer.MAX_VALUE;
        for(int i = 0; i < cols; i++) {
            tempRow = dropChecker(i,hisColor);
            if(tempRow==-1) continue;

            remainingDepth--;
            v = Math.min(v, maxValue(alpha, beta,tempRow,i));
            remainingDepth++;
            currentState[tempRow][i] = Checker.EMPTY;

            if(v <= alpha) {
                return v;
            }
            beta = Math.min(v,beta);
        }
        return v;
    }

    private boolean checkDiagonal2(int row,int col,Checker curColor) {
        int counter = 1,i;
        for(i=1;i<=3 && col+i<cols && row-i>=0;i++) {
            if(currentState[row-i][col+i]!=curColor)  break;
            counter++;
        }
        for(i=1;i<=3 && col-i>=0 && row+i<rows;i++) {
            if(currentState[row+i][col-i]!=curColor)  break;
            counter++;
        }
        return counter>3;
    }

    private boolean checkDiagonal1(int row,int col,Checker curColor) {
        int counter = 1,i;
        for(i=1;i<=3 && col+i<cols && row+i<rows;i++) {
            if(currentState[row+i][col+i]!=curColor)  break;
            counter++;
        }
        for(i=1;i<=3 && col-i>=0 && row-i>=0;i++) {
            if(currentState[row-i][col-i]!=curColor)  break;
            counter++;
        }
        return counter>3;
    }

    private boolean checkVertical(int row,int col,Checker curColor) {
        int counter = 1,i;
        for(i=1;i<=3 && col+i<cols;i++) {
            if(currentState[row][col+i]!=curColor)  break;
            counter++;
        }
        for(i=1;i<=3 && col-i>=0;i++) {
            if(currentState[row][col-i]!=curColor)  break;
            counter++;
        }
        return counter>3;
    }

    private boolean checkHorizontal(int row,int col,Checker curColor) {
        int counter =1,i;
        for(i=1;i<=3 && row+i<rows;i++) {
            if(currentState[row+i][col]!=curColor)  break;
            counter++;
        }
        for(i=1;i<=3 && row-i>=0;i++) {
            if(currentState[row-i][col]!=curColor)  break;
            counter++;
        }
        return counter>3;
    }

    /**
     * @return the color which has four in a row or Empty if there isn't one.
     */
    private Checker identifyWinner(int row,int col) {
        Checker curColor = currentState[row][col];
        if(checkHorizontal(row,col,curColor) ||
                checkVertical(row,col,curColor) ||
                checkDiagonal1(row,col,curColor) ||
                checkDiagonal2(row,col,curColor)) {
            return curColor;
        }
        return Checker.EMPTY;
    }

    /**
     *  pot checker in column i.
     * @param i column number.
     */
    private int dropChecker(int i,Checker color) {
        for(int row = rows - 1;row >= 0; row--) {
            if(currentState[row][i] == Checker.EMPTY) {
                currentState[row][i] = color;
                return row;
            }
        }
        return -1;
    }

    /**
     * calculate heuristic value for the current state.
     * @return the heuristic value.
     */
    private int applyHeuristic() {
        int i,j,result = 0;
        for(i=0;i<rows;i++)
            for(j=0;j<cols;j++) {
                if(currentState[i][j]==myColor) result += table[i][j];
                else if(currentState[i][j]==hisColor)  result -= table[i][j];
            }
        return result;
    }

    private class ChildChooser {
        private int[] orderedChildren;

        public ChildChooser(int cols) {
            orderedChildren = new int[cols];
            orderedChildren[0] = cols / 2;
            
            for(int i = 1; i < cols / 2; i++) {
                orderedChildren[i] = (cols / 2) - i;
                orderedChildren[i+1] = (cols / 2) + i;
            }
        }
    }
}
