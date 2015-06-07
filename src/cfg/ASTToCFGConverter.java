package cfg;

import ast.IFunctionNode;
import cfg.C.CCFGFactory;
import cfg.ECMAScript5.ES5CFGFactory;
import tools.index.SourceLanguage;

public class ASTToCFGConverter
{
	private final SourceLanguage sourceLanguage;

	public ASTToCFGConverter(SourceLanguage sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}

	public CFG convert(IFunctionNode node)
	{
		CFGFactory factory = null;
		if(sourceLanguage == SourceLanguage.C){
			factory = new CCFGFactory();
		}else if(sourceLanguage == SourceLanguage.ECMAScript5) {
			factory = new ES5CFGFactory();
		}

		return factory.newInstance(node);
	}
}
