package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class PrintLnStatement extends Statement {
    private Expression e;

    public PrintLnStatement(Expression e) {
        this.e = e;
    }

    public Expression getExpression() {
        return this.e;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) throws SemanticException {
        return v.visit(this);
    }

    public Temp accept(IrVisitor v) {
        return v.visit(this);
    }
}