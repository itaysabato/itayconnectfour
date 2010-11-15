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
    Checker myColor = null;

    public int getBestResponse(Checker[][] state, Checker myColor) {
        this.myColor = myColor;
        currentState = state;
        return maxValue(Integer.MIN_VALUE, Integer.MAX_VALUE, true,0,0);
    }

    private int maxValue(int alpha, int beta, boolean isRoot,int row,int col) {
        Checker winner = identifyWinner(row,col);
        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();
        int tempRow;
        int v = Integer.MIN_VALUE;
        remainingDepth--;
        int i = 0;
        for( ; i < currentState[0].length; i++) {
            tempRow = dropChecker(i);
            v = Math.max(v, minValue(alpha, beta,tempRow,i));
            if(v >= beta) {
                pickupChecker(i);
                return isRoot ? i : v;
            }
            alpha = Math.max(v,alpha);
            pickupChecker(i);
        }
        remainingDepth++;
        return isRoot ? i : v;
    }

     private int minValue(int alpha, int beta, int row,int col) {
        Checker winner = identifyWinner(row,col);
        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();
         int tempRow;
        int v = Integer.MAX_VALUE;
        remainingDepth--;
        for(int i = 0; i < currentState[0].length; i++) {
            tempRow = dropChecker(i);
            v = Math.min(v, maxValue(alpha, beta,false,tempRow,i));
            if(v <= alpha) {
                pickupChecker(i);
                return v;
            }
            beta = Math.min(v,beta);
            pickupChecker(i);
        }
        remainingDepth++;
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
    private int dropChecker(int i) {
        int row = 0;
    	for(;row<currentState.length;row++) {
    		if(currentState[row][i]==Checker.EMPTY) {
    			currentState[row][i] = myColor;
    			return row;
    		}
    	}
        return 0;
    }

    /**
     *  pickup checker from column i.
     * @param i column number.
     */
     private void pickupChecker(int i) {
        int row = currentState.length-1;
     	for(;row<currentState.length;row++) {
     		if(currentState[row][i]!=Checker.EMPTY) {
     			currentState[row][i] = Checker.EMPTY;
     			return;
     		}
     	}
    }

    /**
     * calculate heuristic value for the current state.
     * @return the heuristic value.
     */
    private int applyHeuristic() {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }
}
