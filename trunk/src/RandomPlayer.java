import java.io.*;
import java.util.*;

public class RandomPlayer implements Player {

	public int getBestResponse(Checker[][] state, Checker myColor) {
		Random randGen = new Random();
		return randGen.nextInt(state[0].length);
	}

}
