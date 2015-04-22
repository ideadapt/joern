package cfg.ECMAScript5;

import cfg.CFG;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.VarNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;


class StructuredFlowVisitor extends NodeVisitor
{

	private CFG returnCFG;

	public StructuredFlowVisitor(LexicalContext lc) {
		super(lc);
	}

	@Override
	public Node leaveVarNode(VarNode varNode) {
		return super.leaveVarNode(varNode);
	}

	@Override
	public boolean enterVarNode(VarNode varNode) {
		returnCFG = ECMAScript5CFGFactory.newInstance(varNode);
		return super.enterVarNode(varNode);
	}

	CFG getCFG()
	{
		return returnCFG;
	}

}
