package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class FormalParameter extends ASTNode {
    private TypeNode type;
    private Identifier id;

    public FormalParameter(TypeNode type, Identifier id) {
        this.type = type;
        this.id = id;
    }

    public TypeNode getType() {
        return this.type;
    }

    public Identifier getIdentifier() {
        return this.id;
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