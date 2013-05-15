/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.etsmtl.log430.lab1;

import java.io.PipedReader;
import java.io.PipedWriter;

/**
 *
 * @author Charles
 */
public class StatusFilter extends Thread {

	// Declarations

	boolean Done;

	String status;
	PipedReader inputPipe = new PipedReader();
	PipedWriter outputPipe = new PipedWriter();
	boolean accept;

	public StatusFilter(String status, PipedWriter inputPipe,
			PipedWriter outputPipe, boolean accept) {

		this.status = status;
		this.accept = accept;

		try {

			// Connect inputPipe to Main

			this.inputPipe.connect(inputPipe);
			System.out.println("StatusFilter " + status
					+ ":: connected to upstream filter.");

			// Connect outputPipe to Merge

			this.outputPipe = outputPipe;
			System.out.println("StatusFilter " + status
					+ ":: connected to downstream filter.");

		} catch (Exception Error) {

			System.out.println("StatusFilter " + status
					+ ":: Error connecting to other filters.");

		} // try/catch

	} // Constructor

	// This is the method that is called when the thread is started in
	// Main
	public void run() {

		// Declarations

		char[] CharacterValue = new char[1];
		// char array is required to turn char into a string
		String LineOfText = "";
		// string is required to look for the keyword
		int IntegerCharacter; // the integer value read from the pipe

		try {

			Done = false;

			while (!Done) {

				IntegerCharacter = inputPipe.read();
				CharacterValue[0] = (char) IntegerCharacter;

				if (IntegerCharacter == -1) { // pipe is closed

					Done = true;

				} else {

					if (IntegerCharacter == '\n') { // end of line

						System.out.println("StatusFilter " + status
								+ ":: received: " + LineOfText + ".");

						if (accept == (LineOfText.contains(status))) {

							System.out.println("StatusFilter "
									+ status + ":: sending: "
									+ LineOfText + " to output pipe.");
							LineOfText += new String(CharacterValue);
							outputPipe
									.write(LineOfText, 0, LineOfText.length());
							outputPipe.flush();

						} // if

						LineOfText = "";

					} else {

						LineOfText += new String(CharacterValue);

					} // if //

				} // if

			} // while

		} catch (Exception Error) {

			System.out.println("StatusFilter::" + status
					+ " Interrupted.");

		} // try/catch

		try {

			inputPipe.close();
			System.out.println("StatusFilter " + status
					+ ":: input pipe closed.");

			outputPipe.close();
			System.out.println("StatusFilter " + status
					+ ":: output pipe closed.");

		} catch (Exception Error) {

			System.out.println("StatusFilter " + status
					+ ":: Error closing pipes.");

		} // try/catch

	} // run

} // class
