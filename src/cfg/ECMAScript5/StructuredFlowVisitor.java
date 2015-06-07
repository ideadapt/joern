package cfg.ECMAScript5;

import cfg.CFG;
import jdk.nashorn.internal.ir.*;
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
		returnCFG = ES5CFGFactory.newInstance(varNode);
		return super.enterVarNode(varNode);
	}

	@Override
	public boolean enterIfNode(IfNode ifNode) {
		returnCFG = ES5CFGFactory.newInstance(ifNode);
		return false;
	}

	@Override
	public boolean enterBlock(Block block) {
		returnCFG = ES5CFGFactory.newInstance(block);
		return false;
	}

	CFG getCFG()
	{
		return returnCFG;
	}

}
