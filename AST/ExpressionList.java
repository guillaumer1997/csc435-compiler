package AST;

import java.util.*;
import Types.*;
import TypeCheck.*;
import IR.*;

public class ExpressionList {
    private List<Expression> expressions;

    public ExpressionList() {
        this.expressions = new ArrayList<Expression>();
    }

    public void addExpression(Expression e) {
        this.expressions.add(e);
    }

    public int getSize() {
        return this.expressions.size();
    }

    public Expression getElement(int i) {
        return this.expressions.get(i);
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