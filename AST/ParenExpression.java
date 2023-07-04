package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class ParenExpression extends Expression {
    private Expression e;

    public ParenExpression(Expression e) {
        this.e = e;
    }

    public Expression getExpression() {
        return e;
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