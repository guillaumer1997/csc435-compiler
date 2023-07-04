package AST;

import java.util.*;
import Types.*;
import TypeCheck.*;
import IR.*;

public class Program extends ASTNode {
    private List<Function> function_list;

    public Program() {
        this.function_list = new ArrayList<Function>();
    }

    public void add(Function element) {
        this.function_list.add(element);
    }

    public int getSize() {
        return this.function_list.size();
    }

    public Function getElement(int i) {
        return this.function_list.get(i);
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