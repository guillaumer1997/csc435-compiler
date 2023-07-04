package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class Identifier extends Expression {
    private IdentifierValue value;

    public Identifier(IdentifierValue v) {
        this.value = v;
    }

    public IdentifierValue getValue() {
        return this.value;
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