package MiniJava.codeGenerator;

import MiniJava.codeGenerator.CodeGenerator;
import MiniJava.scanner.token.Token;

public class WhileAction implements Action {
    @Override
    public void executeAction(CodeGenerator codeGenerator, Token next) {
        codeGenerator._while();
    }
}
