package java.MiniJava.codeGenerator;
import MiniJava.codeGenerator.CodeGenerator;
import MiniJava.scanner.token.Token

public interface Action {
    void executeAction(CodeGenerator codeGenerator, Token next);
}
