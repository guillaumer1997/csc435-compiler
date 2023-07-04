package AST;

import IR.*;
import Types.*;
import TypeCheck.*;

public class AddExpression extends Expression {
    private Expression e1;
    private Expression e2;

    public AddExpression(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getExpression1() {
        return this.e1;
    }

    public Expression getExpression2() {
        return this.e2;
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