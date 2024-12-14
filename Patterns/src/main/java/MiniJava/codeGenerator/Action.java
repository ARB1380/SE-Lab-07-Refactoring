package MiniJava.codeGenerator;
import MiniJava.scanner.token.Token;

public interface Action {
	void executeAction(CodeGenerator codeGenerator, Token next);
}
