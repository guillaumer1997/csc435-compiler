package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class Block extends ASTNode {
    private StatementList statements;

    public Block() {
        this.statements = new StatementList();
    }

    public void setStatementList(StatementList s) {
        this.statements = s;
    }

    public StatementList getStatementList() {
        return this.statements;
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