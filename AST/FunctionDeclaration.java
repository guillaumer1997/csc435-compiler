package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class FunctionDeclaration extends ASTNode {
    private TypeNode type;
    private Identifier id;
    private FormalParameterList formal_parameters;

    public FunctionDeclaration(TypeNode type, Identifier id, FormalParameterList formal_parameters) {
        this.type = type;
        this.id = id;
        this.formal_parameters = formal_parameters;
    }

    public TypeNode getType() {
        return this.type;
    }

    public Identifier getid() {
        return this.id;
    }

    public FormalParameterList getParams() {
        return this.formal_parameters;
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