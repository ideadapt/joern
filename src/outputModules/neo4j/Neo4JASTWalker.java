package outputModules.neo4j;

import tools.index.IndexerASTWalker;
import tools.index.SourceLanguage;


public class Neo4JASTWalker extends IndexerASTWalker
{	
	Neo4JASTWalker(SourceLanguage sourceLanguage)
	{
		astVisitor = new Neo4JASTNodeVisitor(sourceLanguage);
	}
}
