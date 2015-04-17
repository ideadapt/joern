package tools.index;

import java.nio.file.Path;

import outputModules.neo4j.importers.DirectoryTreeImporter;
import parsing.ANTLRParserDriver;
import parsing.ModuleParser;
import parsing.C.Modules.ANTLRCModuleParserDriver;
import fileWalker.SourceFileListener;

import javax.xml.transform.Source;


public abstract class Indexer extends SourceFileListener
{
	protected ModuleParser parser;
	protected IndexerState state;
	protected IndexerASTWalker astWalker;
	protected DirectoryTreeImporter dirTreeImporter;
	protected SourceLanguage sourceLanguage;

	protected String outputDir;

	protected abstract void initializeDirectoryImporter();

	protected abstract void initializeWalker();

	protected abstract void initializeDatabase();

	protected abstract void shutdownDatabase();

	protected void initializeIndexerState()
	{
		state = new IndexerState(this);
	}
	
	public void setOutputDir(String anOutputDir)
	{
		outputDir = anOutputDir;
	}

	public void setSourceLanguage(SourceLanguage sourceLanguage){
		ANTLRParserDriver driver = null;
		if(sourceLanguage == SourceLanguage.C){
			driver = new parsing.C.Modules.ANTLRCModuleParserDriver();
		}else if (sourceLanguage == SourceLanguage.ECMAScript5){
			driver = new parsing.ECMAScript5.ANTLRECMAScriptParserDriver();
		}
		parser = new ModuleParser(driver);
	}

	@Override
	public void initialize()
	{
		initializeIndexerState();
		initializeDirectoryImporter();
		initializeWalker();
		initializeDatabase();
		connectComponents();
	}

	@Override
	public void preVisitDirectory(Path dir)
	{
		dirTreeImporter.enterDir(dir);
	}

	@Override
	public void postVisitDirectory(Path dir)
	{
		dirTreeImporter.exitDir(dir);
	}

	@Override
	public void visitFile(Path pathToFile)
	{
		dirTreeImporter.enterFile(pathToFile);
		parser.parseFile(pathToFile.toString());
	}

	@Override
	public void shutdown()
	{
		shutdownDatabase();
	}

	private void connectComponents()
	{
		astWalker.setIndexerState(state);
		dirTreeImporter.setState(state);
		parser.addObserver(astWalker);
	}

}
