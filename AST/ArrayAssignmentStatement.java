package AST;

import IR.*;
import Types.*;
import TypeCheck.*;

public class ArrayAssignmentStatement extends Statement {
    private Identifier id;
    private Expression e1;
    private Expression e2;

    public ArrayAssignmentStatement(Identifier id, Expression e1, Expression e2) {
        this.id = id;
        this.e1 = e1;
        this.e2 = e2;
    }

    public Identifier getIdentifier() {
        return this.id;
    }

    public Expression getIndex() {
        return this.e1;
    }

    public Expression getAssignment() {
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