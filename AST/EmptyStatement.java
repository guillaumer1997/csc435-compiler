package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class EmptyStatement extends Statement {
    public EmptyStatement() {

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