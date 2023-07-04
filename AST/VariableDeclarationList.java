package AST;

import java.util.*;
import Types.*;
import TypeCheck.*;
import IR.*;

public class VariableDeclarationList extends ASTNode {
    private List<VariableDeclaration> variables;

    public VariableDeclarationList() {
        this.variables = new ArrayList<VariableDeclaration>();
    }

    public void addVarDecl(VariableDeclaration v) {
        this.variables.add(v);
    }

    public int getSize() {
        return this.variables.size();
    }

    public VariableDeclaration getElement(int i) {
        return this.variables.get(i);
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