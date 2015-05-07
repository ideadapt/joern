package ast.ES;

public class ASTWalkerEvent
{

	public enum eventID
	{
		START_OF_UNIT, END_OF_UNIT, PROCESS_ITEM
	};

	public ASTWalkerEvent(eventID aId)
	{
		id = aId;
	}

	public eventID id;
	public String filename;
	public ESASTNode item;
}
