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
        return maxValue(Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }

    private int maxValue(int alpha, int beta, boolean isRoot) {
        Checker winner = identifyWinner();
        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();

        int v = Integer.MIN_VALUE;
        remainingDepth--;
        int i = 0;
        for( ; i < currentState[0].length; i++) {
            dropChecker(i);
            v = Math.max(v, minValue(alpha, beta));
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

     private int minValue(int alpha, int beta) {
        Checker winner = identifyWinner();
        if(winner  != Checker.EMPTY) {
            return winner == myColor ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if(remainingDepth == 0) return applyHeuristic();

        int v = Integer.MAX_VALUE;
        remainingDepth--;
        for(int i = 0; i < currentState[0].length; i++) {
            dropChecker(i);
            v = Math.min(v, maxValue(alpha, beta,false));
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

    /**
     * @return the color which has four in a row or Empty if there isn't one.
     */
    private Checker identifyWinner() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     *  pot checker in column i.
     * @param i column number.
     */
    private void dropChecker(int i) {
        //To change body of created methods use File | Settings | File Templates.
    }

    /**
     *  pickup checker from column i.
     * @param i column number.
     */
     private void pickupChecker(int i) {
        //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * calculate heuristic value for the current state.
     * @return the heuristic value.
     */
    private int applyHeuristic() {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }
}
