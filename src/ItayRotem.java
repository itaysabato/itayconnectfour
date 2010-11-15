/**
* Names: Itay Sabato, Rotem Barzilay <br/>
* Logins: itays04, rotmus  <br/>
* IDs: 036910008, RID  <br/>
* Date: 13/11/2010  <br/>
* Time: 15:52:40  <br/>
*/
public class ItayRotem implements Player {

    private static final int MAX_DEPTH = 700;

    int remainingDepth = MAX_DEPTH;
    Checker[][] currentState = null;
    Checker myColor = null;
    Checker hisColor = null;
     int[][] table = null;
//    Board b;        //


        private void buildTable() {
        table = new int[currentState.length][currentState[0].length];
        int i = 0,j = 0, k=0;
        //horizontal
        for(i=0;i<currentState.length;i++)
            for(j=0;j<currentState[0].length-3;j++)
                for(k=0;k<=3;k++)
                    table[i][j+k]++;
        //vertical
        for(i=0;i<currentState[0].length;i++)
            for(j=0;j<currentState.length-3;j++)
                for(k=0;k<=3;k++)
                    table[j+k][i]++;
        //diagonal1
        for(i=0;i<currentState.length;i++)     {
            for(j=0;j<currentState[0].length-3;j++) {
                 if(j+3+i>=currentState.length || j+i<0)  continue;
                 for(k=0;k<=3;k++)
                     table[j+k+i][j+k]++;
            }
        }
        //diagonal2
        for(i=0;i<table.length;i++)     {
            for(j=table[0].length-1;j>=3;j--) {
                 if(j-3-i<0 || j-i>=table.length)  continue;
                 for(k=0;k<=3;k++)
                     table[j-k-i][j-k]++;
            }
        }

        for(i=0;i<currentState.length;i++) {
            for(j=0;j<currentState[0].length;j++)
                    System.out.print(table[i][j]+",");
            System.out.println();
        }

    }


    public int getBestResponse(Checker[][] state, Checker myColor) {
//        this.b = b;           //
        this.myColor = myColor;
        if(myColor!=Checker.RED) hisColor = Checker.RED ;
        else hisColor = Checker.BLACK ;
        currentState = state;
        buildTable();
        return bestChild();       //Integer.MIN_VALUE, Integer.MAX_VALUE, true,0,0
    }

    private int bestChild() {
        int alpha =   Integer.MIN_VALUE;
        int tempRow;
        int v = Integer.MIN_VALUE;
        int bestChild = 0;

        for( int i = 0; i < currentState[0].length; i++) {
            tempRow = dropChecker(i,myColor);
            if(tempRow==-1) continue;

//            b.setCoin(tempRow,i,myColor);          //

            remainingDepth--;
            int m =  minValue(alpha, Integer.MAX_VALUE,tempRow,i);
            remainingDepth++;
            currentState[tempRow][i] = Checker.EMPTY;

//            b.setCoin(tempRow,i,Checker.EMPTY);          //

            if(v <= m ) {
                v = m;
                bestChild = i;
            }
//            alpha = Math.max(v,alpha);
            System.out.println("v="+v+", best="+bestChild);                   //
        }
        return bestChild;
    }


    private int maxValue(int alpha, int beta,int row,int col) {
        Checker winner = identifyWinner(row,col);

//        System.out.println("drop "+hisColor+" in: " +row+", "+col);           //
//        System.out.println(Arrays.deepToString(currentState));             //
//        System.out.println(winner);                                                                        //

        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();

        int tempRow;
        int v = Integer.MIN_VALUE;
        for( int i = 0; i < currentState[0].length; i++) {
            tempRow = dropChecker(i,myColor);
            if(tempRow==-1) continue;

//            b.setCoin(tempRow,i,myColor);          //

            remainingDepth--;
            v = Math.max(minValue(alpha, beta,tempRow,i), v);
            remainingDepth++;
            currentState[tempRow][i] = Checker.EMPTY;

//            b.setCoin(tempRow,i,Checker.EMPTY);       //

            if(v >= beta) {
                return v;
            }
            alpha = Math.max(v,alpha);
        }
        return v;
    }

    private int minValue(int alpha, int beta, int row,int col) {
        Checker winner = identifyWinner(row,col);

//        System.out.println("drop "+myColor+" in: " +row+", "+col);                                   //
//        System.out.println(Arrays.deepToString(currentState));                                  //
//        System.out.println(winner);                                                                                              //

        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();

        int tempRow;
        int v = Integer.MAX_VALUE;
        for(int i = 0; i < currentState[0].length; i++) {
            tempRow = dropChecker(i,hisColor);
            if(tempRow==-1) continue;

//            b.setCoin(tempRow,i,hisColor);                          //

            remainingDepth--;
            v = Math.min(v, maxValue(alpha, beta,tempRow,i));
            remainingDepth++;
            currentState[tempRow][i] = Checker.EMPTY;

//            b.setCoin(tempRow,i,Checker.EMPTY);           //

            if(v <= alpha) {
                return v;
            }
            beta = Math.min(v,beta);
        }
        return v;
    }


    private boolean checkDiagonal2(int row,int col,Checker curColor) {
        int counter = 1,i;
        for(i=1;i<=3 && col+i<currentState[0].length && row-i>=0;i++) {
            if(currentState[row-i][col+i]!=curColor)  break;
            counter++;
        }
        for(i=1;i<=3 && col-i>=0 && row+i<currentState.length;i++) {
            if(currentState[row+i][col-i]!=curColor)  break;
            counter++;
        }
        return counter>3;
    }

    private boolean checkDiagonal1(int row,int col,Checker curColor) {
        int counter = 1,i;
        for(i=1;i<=3 && col+i<currentState[0].length && row+i<currentState.length;i++) {
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
        for(i=1;i<=3 && col+i<currentState[0].length;i++) {
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
        for(i=1;i<=3 && row+i<currentState.length;i++) {
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
        for(int row = currentState.length - 1;row >= 0; row--) {
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
        for(i=0;i<currentState.length;i++)
            for(j=0;j<currentState[0].length;j++) {
                if(currentState[i][j]==myColor) result += table[i][j];
                else if(currentState[i][j]==hisColor)  result -= table[i][j];
            }
        return result;
    }
}
