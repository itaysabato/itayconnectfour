import java.io.*;
import java.util.*;

public class HumanPlayer implements Player {

	PipedInputStream pin;

	public void setPipe (PipedInputStream pin) {
		this.pin = pin;
	}

	public int getBestResponse(Checker[][] state, Checker myColor) {
		int retVal=-1;
		while (retVal == -1) {
			try {
				retVal = pin.read();
			} catch (Exception e) {}
		}
		return retVal;
	}

}
