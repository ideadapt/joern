package tools.index;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import tools.CommonCommandLineInterface;

public class CommandLineInterface extends CommonCommandLineInterface
{

	private String[] filenames;

	String outputDir = ".joernIndex/";
	SourceLanguage sourceLanguage = SourceLanguage.C;

	public String[] getFilenames()
	{
		return filenames;
	}

	public String getOutputDir()
	{
		return outputDir;
	}

	public SourceLanguage getSourceLanguage()
	{
		return sourceLanguage;
	}

	public CommandLineInterface()
	{
		super();
	}

	@Override
	protected void initializeOptions()
	{
		super.initializeOptions();

		Option outputDirectory = OptionBuilder
				.withArgName("outdir")
				.hasArg()
				.withDescription("specifies where the neo4j database will be written. Default is .joernIndex/")
				.create("outdir");

		Option sourceLanguage = OptionBuilder
				.withArgName("lang")
				.hasArg()
				.withDescription("specifies which language the source code is in. Either C (default) or ECMAScript5.")
				.create("lang");

		options.addOption(outputDirectory);
		options.addOption(sourceLanguage);

	}

	public void parseCommandLine(String[] args) throws ParseException
	{
		if (args.length == 0)
			throw new RuntimeException("At least one file needs to be supplied for parsing.");

		cmd = parser.parse(options, args);
		filenames = cmd.getArgs();

		if (cmd.hasOption("outdir"))
			outputDir = cmd.getOptionValue("outdir");

		if (cmd.hasOption("lang"))
			sourceLanguage = Enum.valueOf(SourceLanguage.class, cmd.getOptionValue("lang"));
	}

	public void printHelp()
	{
		formater.printHelp("joern [SOURCE_DIR1] ...", options);
	}

}
