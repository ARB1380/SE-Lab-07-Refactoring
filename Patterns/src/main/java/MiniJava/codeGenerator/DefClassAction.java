package MiniJava.codeGenerator;

import MiniJava.codeGenerator.CodeGenerator;
import MiniJava.scanner.token.Token;

public class DefClassAction implements Action {
	@Override
	public void executeAction(CodeGenerator codeGenerator, Token next) {
		codeGenerator.defClass();
	}
}
