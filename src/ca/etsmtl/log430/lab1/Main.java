package ca.etsmtl.log430.lab1;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ************************************************************************************
 ** Class name: Main Original author: A.J. Lattanze, CMU Date: 12/3/99 Version 1.2 * * Adapted by R.
 * Champagne, Ecole de technologie superieure 2002-May-08, 2011-Jan-12, 2012-Jan-11. *
 * ************************************************************************************** * Purpose:
 * Assignment 1 for LOG430, Architecture logicielle. This assignment is designed to illustrate a pipe and
 * filter architecture. For the instructions, refer to the assignment write-up. * * Abstract: This class
 * contains the main method for assignment 1. The assignment 1 program consists of these files: * * 1) Main:
 * instantiates all filters and pipes, starts all filters. 2) FileReaderFilter: reads input file and sends
 * each line to its output pipe. 3) TypeFilter: separates the input stream in two languages (FRA, EN) and
 * writes lines to the appropriate output pipe. 4) SeverityFilter: determines if an entry contains a
 * particular severity specified at instantiation. If so, sends the whole line to its output pipe. 5)
 * MergeFilter: accepts inputs from 2 input pipes and writes them to its output pipe. 6) FileWriterFilter:
 * sends its input stream to a text file. * * Pseudo Code: * * instantiate all filters and pipes start
 * FileReaderFilter start TypeFilter start SeverityFilter for CRI start SeverityFilter for MAJ start
 * MergeFilter start FileWriterFilter * * Running the program * * java Main IputFile OutputFile > DebugFile *
 * * Main - Program name InputFile - Text input file (see comments below) OutputFile - Text output file with
 * students DebugFile - Optional file to direct debug statements * * Modification Log
 * ************************************************************************************* *
 * ************************************************************************************
 */
public class Main {

	public static void main(String[] argv) {
		if ("A".equalsIgnoreCase(argv[0])) {
			systemA(argv);
		} else if ("B".equalsIgnoreCase(argv[0])) {
			systemB(argv);
		} else {
			System.out.println("Le premier arg doit être A ou B");
		}
	}

	private static void systemA(String[] argv) {
		// Lets make sure that input and output files are provided on the command line
		if (argv.length != 4) {
			System.out.println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
			System.out.println("\njava Main <fichier d'entree> <fichier de sortie> <fichier de sortie>");
		} else {
			Collection<Thread> filters = new ArrayList<Thread>();
			PipedWriter pipe01 = new PipedWriter();
			PipedWriter pipe02 = new PipedWriter();
			PipedWriter pipe03 = new PipedWriter();
			PipedWriter pipe04 = new PipedWriter();
			PipedWriter pipe05 = new PipedWriter();
			PipedWriter pipe06 = new PipedWriter();
			PipedWriter pipe07 = new PipedWriter();
			PipedWriter pipe08 = new PipedWriter();
			PipedWriter pipe09 = new PipedWriter();
			PipedWriter pipe10 = new PipedWriter();
			PipedWriter pipe11 = new PipedWriter();
			PipedWriter pipe12 = new PipedWriter();
			PipedWriter pipe13 = new PipedWriter();
			PipedWriter pipe14 = new PipedWriter();
			PipedWriter pipe15 = new PipedWriter();
			PipedWriter pipe16 = new PipedWriter();
			PipedWriter pipe17 = new PipedWriter();

			filters.add(new FileReaderFilter(argv[1], pipe01));
			filters.add(new TypeFilter(pipe01, pipe02, pipe03));
			filters.add(new CloneDataFilter(pipe02, pipe04, pipe05));
			filters.add(new CloneDataFilter(pipe03, pipe06, pipe07));
			filters.add(new SeverityFilter("CRI", pipe04, pipe08, true));
			filters.add(new SeverityFilter("CRI", pipe05, pipe09, false));
			filters.add(new SeverityFilter("MAJ", pipe06, pipe10, true));
			filters.add(new SeverityFilter("MAJ", pipe07, pipe11, false));
			filters.add(new MergeFilter(pipe08, pipe10, pipe12));
			filters.add(new MergeFilter(pipe09, pipe11, pipe13));
			filters.add(new FormatFilter(pipe12, pipe14, 5, 4, 1, 0));
			filters.add(new FormatFilter(pipe13, pipe15, 5, 4, 1, 0));
			filters.add(new AlphabeticalFilter(0, pipe14, pipe16));
			filters.add(new AlphabeticalFilter(0, pipe15, pipe17));
			filters.add(new FileWriterFilter(argv[2], pipe16));
			filters.add(new FileWriterFilter(argv[3], pipe17));

			// Start the threads (these are the filters)
			for (Thread t : filters) {
				t.start();
			}
		}
	} // main

	private static void systemB(String[] argv) {
		if (argv.length != 4) {
			System.out.println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
			System.out.println("\njava Main <fichier d'entree> <fichier de sortie> <fichier de sortie>");
		} else {
			Collection<Thread> filters = new ArrayList<Thread>();
			PipedWriter pipe01 = new PipedWriter();
			PipedWriter pipe02 = new PipedWriter();
			PipedWriter pipe03 = new PipedWriter();
			PipedWriter pipe04 = new PipedWriter();
			PipedWriter pipe05 = new PipedWriter();
			PipedWriter pipe06 = new PipedWriter();
			PipedWriter pipe07 = new PipedWriter();
			PipedWriter pipe08 = new PipedWriter();
			PipedWriter pipe09 = new PipedWriter();
			PipedWriter pipe10 = new PipedWriter();
			PipedWriter pipe11 = new PipedWriter();
			PipedWriter pipe12 = new PipedWriter();
			PipedWriter pipe13 = new PipedWriter();
			PipedWriter pipe14 = new PipedWriter();
			PipedWriter pipe15 = new PipedWriter();
			PipedWriter pipe16 = new PipedWriter();
			PipedWriter pipe17 = new PipedWriter();
			PipedWriter pipe18 = new PipedWriter();
			PipedWriter pipe19 = new PipedWriter();

			filters.add(new FileReaderFilter(argv[1], pipe01));
			filters.add(new TypeFilter(pipe01, pipe02, pipe03));

			filters.add(new StatusFilter("NOU", pipe02, pipe04, true));
			filters.add(new CloneDataFilter(pipe04, pipe05, pipe06));

			filters.add(new StatusFilter("RES", pipe03, pipe12, false));
			filters.add(new CloneDataFilter(pipe12, pipe13, pipe14));

			filters.add(new SeverityFilter("CRI", pipe05, pipe07, true));
			filters.add(new SeverityFilter("MAJ", pipe06, pipe08, true));

			filters.add(new SeverityFilter("NOR", pipe13, pipe15, true));
			filters.add(new SeverityFilter("MAJ", pipe14, pipe16, true));

			filters.add(new MergeFilter(pipe07, pipe08, pipe09));

			filters.add(new MergeFilter(pipe15, pipe16, pipe17));

			filters.add(new FormatFilter(pipe09, pipe10, 5, 4, 1, 0));
			filters.add(new AlphabeticalFilter(0, pipe10, pipe11));
			filters.add(new FileWriterFilter(argv[2], pipe11));

			filters.add(new FormatFilter(pipe17, pipe18, 5, 4, 1, 0));
			filters.add(new AlphabeticalFilter(0, pipe18, pipe19));
			filters.add(new FileWriterFilter(argv[3], pipe19));

			// Start the threads (these are the filters)
			for (Thread t : filters) {
				t.start();
			}
		}
	}
}