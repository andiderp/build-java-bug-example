package monto.service.ast;

import monto.service.region.IRegion;

public interface AST extends IRegion {
    void accept(ASTVisitor visitor);
}
