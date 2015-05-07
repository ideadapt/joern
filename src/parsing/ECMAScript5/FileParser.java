package parsing.ECMAScript5;

import ast.ES.ASTWalkerEvent;
import ast.ES.ESASTNode;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;
import parsing.FileParserInterface;
import parsing.ParserException;
import tests.ES5.ASTUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observer;

public class FileParser implements FileParserInterface {

	Observer observer;

	@Override
	public void parseFile(String filename){
		ASTWalkerEvent ev = new ASTWalkerEvent(ASTWalkerEvent.eventID.START_OF_UNIT);
		ev.filename = filename;
		this.observer.update(null, ev);

		File file = new File(filename);
		ESASTNode node = ASTUtil.getASTForCode(file);

		ev = new ASTWalkerEvent(ASTWalkerEvent.eventID.PROCESS_ITEM);
		ev.filename = filename;
		ev.item = node;
		this.observer.update(null, ev);

		ev = new ASTWalkerEvent(ASTWalkerEvent.eventID.END_OF_UNIT);
		ev.filename = filename;
		this.observer.update(null, ev);
	}

	@Override
	public void addObserver(Observer anObserver){
		this.observer = anObserver;
	}

	@Override
	public void parseString(String code){

	}
}
