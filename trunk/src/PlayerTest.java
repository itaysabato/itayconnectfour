import junit.framework.TestCase;

public class PlayerTest extends TestCase {

    public void testWinner() {
        ItayRotem player = new ItayRotem();

        // [[RED, RED, RED, EMPTY], [BLACK, BLACK, BLACK, EMPTY], [RED, RED, RED, EMPTY], [BLACK, BLACK, BLACK, EMPTY]]
        Checker[][] state = {
                {Checker.RED,Checker. RED, Checker.RED,Checker. EMPTY},
                {Checker.BLACK,Checker.BLACK, Checker.BLACK, Checker.BLACK},
                {Checker.RED, Checker.RED, Checker.RED, Checker.EMPTY},
                {Checker.BLACK, Checker.BLACK ,Checker.BLACK ,Checker.EMPTY}} ;

        player.currentState = state;
    }
}
