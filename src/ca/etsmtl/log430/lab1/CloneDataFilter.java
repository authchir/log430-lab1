package ca.etsmtl.log430.lab1;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class CloneDataFilter extends Thread {
	
	private PipedReader inputPipe = new PipedReader();
	private PipedWriter[] outputPipes;

	public CloneDataFilter(PipedWriter input, PipedWriter... outputs) {
			try {
				inputPipe.connect(input);
				outputPipes = outputs;
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void run() {
		try {
			int character;
	
			while (-1 != (character = inputPipe.read())) {
				for (PipedWriter pipe : outputPipes)
					pipe.write(character);
			}

			inputPipe.close();
			
			for (PipedWriter pipe : outputPipes)
				pipe.close();

		} catch (Exception Error) {

			System.out.println("PartitionFilter:: Error closing pipes.");

		} // try/catch

	}
}
