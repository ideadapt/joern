package outputModules.neo4j.ES5;

import ast.ES.ASTWalkerEvent;
import tools.index.IndexerState;

import java.util.Observer;


public class Neo4JASTWalker implements Observer
{	
	Neo4JASTNodeVisitor astVisitor;

	IndexerState state;

	public Neo4JASTWalker()
	{
		astVisitor = new Neo4JASTNodeVisitor();
	}

	public void setState(IndexerState state) {
		this.state = state;
	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		ASTWalkerEvent ev = (ASTWalkerEvent) arg;
		System.out.println("new event:"+ev.id);
		switch(ev.id){
			case START_OF_UNIT:
				astVisitor.startUnit(state.getCurrentFileNode());
				break;
			case END_OF_UNIT:
				break;
			case PROCESS_ITEM:
				ev.item.accept(astVisitor);
				break;
		}
	}
}
