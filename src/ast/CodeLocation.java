package ast;

import org.antlr.v4.runtime.ParserRuleContext;

public class CodeLocation implements ICodeLocation
{

	final private int NOT_SET = -1;

	public int startLine = NOT_SET;
	public int startPos = NOT_SET;
	public int startIndex = NOT_SET;
	public int stopIndex = NOT_SET;

	public CodeLocation()
	{
	}

	public CodeLocation(ParserRuleContext ctx)
	{
		initializeFromContext(ctx);
	}

	private void initializeFromContext(ParserRuleContext ctx)
	{
		startLine = ctx.start.getLine();
		startPos = ctx.start.getCharPositionInLine();
		startIndex = ctx.start.getStartIndex();
		if (ctx.stop != null)
			stopIndex = ctx.stop.getStopIndex();
		else
			stopIndex = NOT_SET;
	}

	@Override
	public Integer getStartIndex() {
		return startIndex;
	}

	@Override
	public Integer getStartLine() {
		return startLine;
	}

	@Override
	public Integer getStopIndex() {
		return stopIndex;
	}

	@Override
	public Integer setStartIndex(Integer i) {
		return this.startIndex = i;
	}

	@Override
	public Integer setStartLine(Integer i) {
		return this.startLine = i;
	}

	@Override
	public Integer setStopIndex(Integer i) {
		return this.stopIndex = i;
	}

	@Override
	public String toString()
	{
		return String.format("%d:%d:%d:%d", startLine, startPos, startIndex,
				stopIndex);
	}

}
