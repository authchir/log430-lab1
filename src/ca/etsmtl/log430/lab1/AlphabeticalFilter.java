package ca.etsmtl.log430.lab1;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlphabeticalFilter extends Thread {

	private int statusIndex = 0;
	private PipedReader inputPipe = new PipedReader();
	private PipedWriter outputPipe;

	public AlphabeticalFilter(int statusIndex, PipedWriter inputPipe, PipedWriter outputPipe) {

		this.statusIndex = statusIndex;

		try {

			// Connect inputPipe to Main
			this.inputPipe.connect(inputPipe);
			System.out.println("AlphabeticalStatusFilter :: connected to upstream filter.");

			// Connect outputPipe to Merge

			this.outputPipe = outputPipe;
			System.out.println("AlphabeticalStatusFilter :: connected to downstream filter.");

		} catch (Exception Error) {

			System.out.println("AlphabeticalStatusFilter :: Error connecting to other filters.");
			Error.printStackTrace();

		} // try/catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main
	public void run() {

		// Declarations

		ArrayList<String> tickets = new ArrayList<String>();

		char[] CharacterValue = new char[1];
		// char array is required to turn char into a string
		String LineOfText = "";
		// string is required to look for the keyword
		int IntegerCharacter; // the integer value read from the pipe

		boolean done = false;

		try {

			while (!done) {

				IntegerCharacter = inputPipe.read();
				CharacterValue[0] = (char) IntegerCharacter;

				if (IntegerCharacter == -1) { // pipe is closed

					done = true;

				} else {

					if (IntegerCharacter == '\n') { // end of line

						System.out.println("AlphabeticalStatusFilter " + ":: received: " + LineOfText + ".");

						tickets.add(LineOfText);
						LineOfText = "";

					} else {

						LineOfText += new String(CharacterValue);

					} // if //

				} // if

			} // while

			Collections.sort(tickets, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return (o1.split(" ")[statusIndex]).compareTo(o2.split(" ")[statusIndex]);
				}

			});

			for (String s : tickets) {

				System.out.println("AlphabeticalStatusFilter " + ":: sending: " + s + ".");

				outputPipe.write(s + '\n');
				outputPipe.flush();
			}

		} catch (Exception Error) {

			System.out.println("AlphabeticalStatusFilter::" + " Interrupted.");

		} // try/catch

		try {

			inputPipe.close();
			System.out.println("AlphabeticalStatusFilter " + ":: input pipe closed.");

			outputPipe.close();
			System.out.println("AlphabeticalStatusFilter " + ":: output pipe closed.");

		} catch (Exception Error) {

			System.out.println("AlphabeticalStatusFilter " + ":: Error closing pipes.");

		} // try/catch

	} // run

}
