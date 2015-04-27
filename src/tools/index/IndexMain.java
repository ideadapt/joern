package tools.index;

import java.io.IOException;

import org.apache.commons.cli.ParseException;

import outputModules.neo4j.Neo4JIndexer;
import fileWalker.OrderedWalker;
import fileWalker.SourceFileWalker;

/**
 * Main Class for the indexer: This class parses command line arguments and
 * configures the indexer in accordance. It then uses a SourceFileWalker to
 * visit source-files and directories and report them to the indexer.
 * */
public class IndexMain
{
	private static CommandLineInterface cmd = new CommandLineInterface();
	private static SourceFileWalker sourceFileWalker;
	private static Indexer indexer;

	public static void main(String[] args)
	{
		parseCommandLine(args);
		String[] fileAndDirNames = getFileAndDirNamesFromCommandLine();
		String outputDir = cmd.getOutputDir();
		SourceLanguage sourceLanguage = cmd.getSourceLanguage();

		setupIndexer(outputDir, sourceLanguage);
		walkCodebase(fileAndDirNames);
	}

	private static void parseCommandLine(String[] args)
	{
		try
		{
			cmd.parseCommandLine(args);
		}
		catch (RuntimeException | ParseException ex)
		{
			printHelpAndTerminate(ex);
		}
	}

	private static void printHelpAndTerminate(Exception ex)
	{
		System.err.println(ex.getMessage());
		cmd.printHelp();
		System.exit(1);
	}

	private static String[] getFileAndDirNamesFromCommandLine()
	{
		return cmd.getFilenames();
	}

	private static void setupIndexer(String outputDir, SourceLanguage sourceLanguage)
	{
		// In the future, allow other indexers to be instantiated
		indexer = new Neo4JIndexer();
		indexer.setOutputDir(outputDir);
		indexer.setSourceLanguage(sourceLanguage);
		indexer.initialize();
		sourceFileWalker = new OrderedWalker(sourceLanguage.getExtensions());
		sourceFileWalker.addListener(indexer);
	}

	private static void walkCodebase(String[] fileAndDirNames)
	{
		try
		{
			sourceFileWalker.walk(fileAndDirNames);
		}
		catch (IOException err)
		{
			System.err.println("Error walking source files: "
					+ err.getMessage());
		}
		finally
		{
			indexer.shutdown();
		}
	}
}
