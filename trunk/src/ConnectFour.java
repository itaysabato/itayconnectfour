import java.io.*;
import java.util.*;

public class ConnectFour {
	public static void main (String[] args) throws Exception {

		int rows=Integer.parseInt(args[2]);
		int cols=Integer.parseInt(args[3]);
		int timeOut=Integer.parseInt(args[4]);

		/** Create communication pipeline between the GUI and human player */
		PipedInputStream pin  = new PipedInputStream( );
		PipedOutputStream pout = new PipedOutputStream(pin);

		/** Create a new game board */
		Board b = new Board(rows,cols, pout);

		/** Using Class to create runtime-named classes (default ctors) */
		Class c1 = Class.forName(args[0]);
		Class c2 = Class.forName(args[1]);
		Player firstPlayer = (Player) c1.newInstance();
		Player secondPlayer = (Player) c2.newInstance();

		if (firstPlayer instanceof HumanPlayer)
			((HumanPlayer)firstPlayer).setPipe(pin);

		if (secondPlayer instanceof HumanPlayer)
			((HumanPlayer)secondPlayer).setPipe(pin);

		while (b.checkFull() == false) {

			Timer p1Timer = new Timer();
			p1Timer.schedule(new TimerTask() { public void run() { System.out.println("Player1 timeout!"); System.exit(0); } }, timeOut);

			int p1BR = firstPlayer.getBestResponse (b.getBoard(), Checker.RED);
			p1Timer.cancel();

			b.setCoin(p1BR, Checker.RED);

			if (b.checkWinner() == Checker.RED)
				break;

			Timer p2Timer = new Timer();
			p2Timer.schedule(new TimerTask() { public void run() { System.out.println("Player2 timeout!"); System.exit(0); } }, timeOut);

			int p2BR = secondPlayer.getBestResponse (b.getBoard(), Checker.BLACK);
			p2Timer.cancel();

			b.setCoin(p2BR, Checker.BLACK);

			if (b.checkWinner() == Checker.BLACK)
				break;

		}

		/** Print the winner to the prompter */
		if (b.checkWinner() == Checker.RED) {
			b.setPrompter("RED WINS !!!");
			System.out.println(args[0]);
		} else {
			b.setPrompter("BLACK WINS !!!");
			System.out.println(args[1]);
		}
	}
}
