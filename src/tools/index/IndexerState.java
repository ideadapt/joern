package tools.index;

import databaseNodes.FileDatabaseNode;

/**
 * IndexerState allows importers to share information, e.g., the
 * Function-importer may need to know the current file node id from the File
 * importer.
 * */

public class IndexerState
{
	private FileDatabaseNode currentFileNode;

	public void setCurrentFileNode(FileDatabaseNode node)
	{
		currentFileNode = node;
	}

	public FileDatabaseNode getCurrentFileNode()
	{
		return currentFileNode;
	}
	
}
