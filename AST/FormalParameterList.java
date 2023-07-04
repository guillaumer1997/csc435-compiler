package AST;

import java.util.*;

import Types.*;
import TypeCheck.*;
import IR.*;

public class FormalParameterList extends ASTNode {
    private List<FormalParameter> params;

    public FormalParameterList() {
        this.params = new ArrayList<FormalParameter>();
    }

    public void add(FormalParameter element) {
        params.add(element);
    }

    public int getSize() {
        return this.params.size();
    }

    public FormalParameter getElement(int i) {
        return this.params.get(i);
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
