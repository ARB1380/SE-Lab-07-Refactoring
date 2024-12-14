package MiniJava.codeGenerator;

import MiniJava.scanner.token.Token;

public class AddAction implements Action {
    @Override
    public void executeAction(CodeGenerator codeGenerator, Token next) {
        codeGenerator.add();
    }
}
