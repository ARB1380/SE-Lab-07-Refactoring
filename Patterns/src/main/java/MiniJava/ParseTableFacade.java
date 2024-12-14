package MiniJava;

import MiniJava.parser.ParseTable;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ParseTableFacade {
	public static ParseTable createParseTableFromFile(String path) throws Exception {
		return new ParseTable(Files.readAllLines(Paths.get(path)).get(0));
	}
}
