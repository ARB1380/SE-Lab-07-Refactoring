package MiniJava.codeGenerator;

import MiniJava.scanner.token.Token;

public class PidAction implements Action {
    @Override
    public void executeAction(CodeGenerator codeGenerator, Token next) {
        codeGenerator.pid(next);
    }
}
