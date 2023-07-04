package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class TypeNode extends ASTNode {
    private Type type;

    public TypeNode(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
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