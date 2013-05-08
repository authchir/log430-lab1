package ca.etsmtl.log430.lab1;

import java.io.PipedReader;
import java.io.PipedWriter;

public class PartitionFilter extends Thread {

	// Declarations

	boolean Done;

	Predicate<String> predicate;
	PipedReader inputPipe = new PipedReader();
	PipedWriter outputPipeTrue = new PipedWriter();
	PipedWriter outputPipeFalse = new PipedWriter();

	public PartitionFilter(Predicate<String> predicate, PipedWriter inputPipe,
			PipedWriter outputPipeTrue, PipedWriter outputPipeFalse) {

		this.predicate = predicate;

		try {

			// Connect inputPipe to Main

			this.inputPipe.connect(inputPipe);
			System.out.println("GenericFilter:: connected to upstream filter.");

			// Connect outputPipe to Merge

			this.outputPipeTrue = outputPipeTrue;
			System.out.println("PartitionFilter:: connected to downstream true filter.");
			
			this.outputPipeFalse = outputPipeFalse;
			System.out.println("PartitionFilter:: connected to downstream false filter.");

		} catch (Exception Error) {

			System.out.println("PartitionFilter:: Error connecting to other filters.");

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

						System.out.println("PartitionFilter:: received: " + LineOfText + ".");

						if (predicate.eval(LineOfText)) {

							System.out.println("PartitionFilter:: sending: "
									+ LineOfText + " to output pipe.");
							LineOfText += new String(CharacterValue);
							outputPipeTrue.write(LineOfText, 0, LineOfText.length());
							outputPipeTrue.flush();
							
						} else {
							
							System.out.println("PartitionFilter:: sending: "
									+ LineOfText + " to output pipe.");
							LineOfText += new String(CharacterValue);
							outputPipeFalse.write(LineOfText, 0, LineOfText.length());
							outputPipeFalse.flush();
							
						} // if

						LineOfText = "";

					} else {

						LineOfText += new String(CharacterValue);

					} // if //

				} // if

			} // while

		} catch (Exception Error) {

			System.out.println("PartitionFilter:: Interrupted.");

		} // try/catch

		try {

			inputPipe.close();
			System.out.println("PartitionFilter:: input pipe closed.");

			outputPipeTrue.close();
			System.out.println("PartitionFilter:: output pipe true closed.");
			
			outputPipeFalse.close();
			System.out.println("PartitionFilter:: output pipe false closed.");

		} catch (Exception Error) {

			System.out.println("PartitionFilter:: Error closing pipes.");

		} // try/catch

	} // run

} // class