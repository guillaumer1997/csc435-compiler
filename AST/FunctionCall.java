package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class FunctionCall extends Expression {
    private Identifier id;
    private ExpressionList exprlist;

    public FunctionCall(Identifier id, ExpressionList exprlist) {
        this.id = id;
        this.exprlist = exprlist;
    }

    public Identifier getId() {
        return this.id;
    }

    public ExpressionList getExpressions() {
        return this.exprlist;
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