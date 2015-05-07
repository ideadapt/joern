package parsing.C;

import ast.walking.ASTWalker;
import parsing.ANTLRParserDriver;
import parsing.FileParserInterface;
import parsing.ParserException;

import java.util.Observer;

public class FileParser implements FileParserInterface {
	ANTLRParserDriver parserDriver;
	
	public FileParser(ANTLRParserDriver driver)
	{
		parserDriver = driver;
	}

	@Override
	public void parseFile(String filename){
		System.out.println(filename);

		try
		{
			parserDriver.parseAndWalkFile(filename);
		}
		catch (ParserException ex)
		{
			System.err.println("Error parsing file: " + filename);
		}
	}

	@Override
	public void addObserver(Observer anObserver){
		parserDriver.addObserver(anObserver);
	}

	@Override
	public void parseString(String code){
		try
		{
			parserDriver.parseAndWalkString(code);
		}
		catch (ParserException ex)
		{
			System.err.println("Error parsing string.");
		}
	}
}
