package MiniJava.codeGenerator;

import MiniJava.codeGenerator.CodeGenerator;
import MiniJava.scanner.token.Token;

public class LastTypeBoolAction implements Action {
    @Override
    public void executeAction(CodeGenerator codeGenerator, Token next) {
        codeGenerator.lastTypeBool();
    }
}
