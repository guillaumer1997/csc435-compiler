package AST;

import IR.*;
import Types.*;
import TypeCheck.*;

public abstract class ASTNode {
    private int line;
    private int offset;

    public ASTNode() {
        this.line = 0;
        this.offset = 0;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLine() {
        return this.line;
    }

    public int getOffset() {
        return this.line;
    }

    public abstract void accept(Visitor v);

    public abstract Type accept(TypeVisitor v) throws SemanticException;

    public abstract Temp accept(IrVisitor v);

}