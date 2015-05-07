package tools.index;

import java.nio.file.Path;

import outputModules.neo4j.Neo4JASTWalker;
import outputModules.neo4j.importers.DirectoryTreeImporter;
import parsing.ANTLRParserDriver;
import parsing.C.FileParser;
import fileWalker.SourceFileListener;
import parsing.FileParserInterface;

public abstract class Indexer extends SourceFileListener
{
	protected FileParserInterface parser;
	protected IndexerState state;
	protected DirectoryTreeImporter dirTreeImporter;
	protected SourceLanguage sourceLanguage;

	protected String outputDir;

	protected abstract void initializeDirectoryImporter();

	protected abstract void initializeDatabase();

	protected abstract void shutdownDatabase();

	protected void initializeParser(){
		if(sourceLanguage == SourceLanguage.C){
			ANTLRParserDriver driver = new parsing.C.Modules.ANTLRCModuleParserDriver();
			parser = new FileParser(driver);
			IndexerASTWalker astWalker = new Neo4JASTWalker();
			astWalker.setIndexerState(state);
			parser.addObserver(astWalker);
		}else if (sourceLanguage == SourceLanguage.ECMAScript5){
			parser = new parsing.ECMAScript5.FileParser();
			outputModules.neo4j.ES5.Neo4JASTWalker astWalker = new outputModules.neo4j.ES5.Neo4JASTWalker();
			astWalker.setState(state);
			parser.addObserver(astWalker);
		} else{
			throw new IllegalArgumentException("source language not supported");
		}
	}

	protected void initializeIndexerState()
	{
		state = new IndexerState();
	}
	
	public void setOutputDir(String anOutputDir)
	{
		outputDir = anOutputDir;
	}

	public void setSourceLanguage(SourceLanguage sourceLanguage){
		this.sourceLanguage = sourceLanguage;
	}

	@Override
	public void initialize()
	{
		initializeIndexerState();
		initializeDirectoryImporter();
		initializeDatabase();
		initializeParser();
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
		dirTreeImporter.setState(state);
	}

}
