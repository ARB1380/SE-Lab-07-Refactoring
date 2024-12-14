package MiniJava;

import MiniJava.errorHandler.ErrorHandler;
import MiniJava.parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParserFacade {

	public static void parseFile(String filePath) {
		Parser parser = new Parser();
		try {
			// start parsing
			parser.startParse(new Scanner(new File(filePath)));
		} catch (FileNotFoundException e) {
			ErrorHandler.printError(e.getMessage());
		}
	}
}
