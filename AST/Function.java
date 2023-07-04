package AST;

import Types.*;
import TypeCheck.*;
import IR.*;

public class Function extends ASTNode {
    private FunctionDeclaration func_decl;
    private FunctionBody func_body;

    public Function(FunctionDeclaration func_decl, FunctionBody func_body) {
        this.func_decl = func_decl;
        this.func_body = func_body;
    }

    public FunctionDeclaration getDeclaration() {
        return this.func_decl;
    }

    public FunctionBody getFunctionBody() {
        return this.func_body;
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