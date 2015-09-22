package monto.service.ast;

public interface ASTVisitor {
    void visit(NonTerminal node);

    void visit(Terminal token);
}
