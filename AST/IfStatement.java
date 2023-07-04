package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class IfStatement extends Statement {
    private Expression expr;
    private Block b1;
    private Block b2;
    private Boolean isElse;

    public IfStatement(Expression expr, Block b1, Block b2) {
        this.expr = expr;
        this.b1 = b1;
        this.b2 = b2;
        this.isElse = true;
    }

    public IfStatement(Expression expr, Block b1) {
        this.expr = expr;
        this.b1 = b1;
        this.b2 = null;
        this.isElse = false;

    }

    public Expression getExpression() {
        return this.expr;
    }

    public Block getIfBlock() {
        return this.b1;
    }

    public Block getElseBlock() {
        return this.b2;
    }

    public Boolean isElse() {
        return this.isElse;
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