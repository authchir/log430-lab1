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
public class FormatFilter extends Thread {

	boolean done;
	int columnDefinitions[][] = {
		{0, 4}, //0001
		{5, 3}, //AME
		{9, 6}, //LAB001
		{16, 5}, //01.01
		{22, 3}, //MAJ
		{26, 3}, //NOU
		{30, 30} //Modifier le lab1 de LOG430
	};
	int outputColumns[];
	//pipes
	PipedReader inputPipe = new PipedReader();
	PipedWriter outputPipe = new PipedWriter();

	public FormatFilter(PipedWriter inputPipe, PipedWriter outputPipe, int... columns) {
		outputColumns = columns;
		try {

			// Connect inputPipe
			this.inputPipe.connect(inputPipe);
			log("connected to upstream filter.");

			// Connect outputPipe
			this.outputPipe = outputPipe;
			log("connected to downstream filter.");

		} catch (Exception e) {
			log("error connecting to other filters.");
		}
	}

	public void run() {

		// Declarations

		char[] CharacterValue = new char[1];
		// char array is required to turn char into a string

		String lineOfText = "";
		// string is required to look for the language code
		int intChar; // the integer value read from the pipe

		try {

			done = false;
			while (!done) {
				intChar = inputPipe.read();
				CharacterValue[0] = (char) intChar;

				if (intChar == -1) { // pipe is closed
					done = true;
				} else {
					if (intChar == '\n') { // end of line
						log("received: " + lineOfText + ".");

						String formatedLine = formatLine(lineOfText);

						outputPipe.write(formatedLine);

						log("written: " + formatedLine);

						lineOfText = "";

					} else {
						lineOfText += new String(CharacterValue);
					}
				}
			}

		} catch (Exception ex) {
			log("Interrupted.");
		}

		try {
			inputPipe.close();
			log("input pipe closed.");

			outputPipe.close();
			log("output pipe closed.");
		} catch (Exception ex) {
			log("error closing pipes.");
		}
	}

	String formatLine(String line) {
		StringBuilder sb = new StringBuilder();
		String separator = " ";

		for (int i = 0; i < outputColumns.length; i++) {
			int column = outputColumns[i];

			int startIndex = columnDefinitions[column][0];
			int length = Math.min(columnDefinitions[column][1], line.length() - startIndex);

			sb.append(line.substring(startIndex, startIndex + length));

			if (i != outputColumns.length) {
				sb.append(separator);
			}
		}
		sb.append('\n');

		return sb.toString();
	}

	void log(String log) {
		System.out.print("FormatFilter:: ");
		System.out.println(log);
	}
}