package outputModules.neo4j;

import tools.index.IndexerASTWalker;
import tools.index.SourceLanguage;


public class Neo4JASTWalker extends IndexerASTWalker
{	
	public Neo4JASTWalker()
	{
		astVisitor = new Neo4JASTNodeVisitor();
	}
}
