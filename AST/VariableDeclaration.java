package AST;

import Types.Type;
import Types.*;
import TypeCheck.*;
import IR.*;

public class VariableDeclaration extends ASTNode {
    private TypeNode type;
    private Identifier id;

    public VariableDeclaration(TypeNode type, Identifier id) {
        this.type = type;
        this.id = id;
    }

    public TypeNode getType() {
        return this.type;
    }

    public Identifier getId() {
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